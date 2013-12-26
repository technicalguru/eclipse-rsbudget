/**
 * 
 */
package rsbudget.update;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.prefs.BackingStoreException;

import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.prefs.IPreferences;
import rsbudget.Plugin;

/**
 * The handler for the Update action.
 * @author ralph
 *
 */
public class UpdateHandler {

	@Execute
	public void execute(final IProxyService proxyService, final IProvisioningAgent agent, final Shell parent, final UISynchronize sync, final IWorkbench workbench) {		
		Job job = new Job("Update Job") {
			private boolean doInstall = false;

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				/* 1. Prepare update plumbing */

				final ProvisioningSession session = new ProvisioningSession(agent);
				final UpdateOperation operation = new UpdateOperation(session);

				// create uri
				URI uri = null;
				try {
					String location = Plugin.getUserPreferences().get("updateChannel", Plugin.RELEASE_CHANNEL);
					LoggerFactory.getLogger(getClass()).info("Using update site: "+location);
					uri = new URI(location);
				} catch (final URISyntaxException e) {
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openError(parent, "URI invalid", e.getMessage());
						}
					});
					return Status.CANCEL_STATUS;
				} catch (final BackingStoreException e) {
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openError(parent, "Cannot detect update channel", e.getMessage());
						}
					});
					return Status.CANCEL_STATUS;
				}

				// set location of artifact and metadata repo
				operation.getProvisioningContext().setArtifactRepositories(new URI[] { uri });
				operation.getProvisioningContext().setMetadataRepositories(new URI[] { uri });

				/* 2. check for updates */

				// run update checks causing I/O
				final IStatus status = operation.resolveModal(monitor);

				// failed to find updates (inform user and exit)
				//System.out.println(status.getCode()+" "+status.getMessage()+" "+status.getSeverity());
				if (!status.isOK() || (status.getCode() == 10001)) {
					String msgTitle = "update.error.unknown.title";
					String msgText  = "update.error.unknown.message";

					switch (status.getCode()) {
					case UpdateOperation.STATUS_NOTHING_TO_UPDATE:
						msgTitle = "update.error.noupdates.title";
						msgText  = "update.error.noupdates.message";
						break;
					case 10001:
						msgTitle = "update.error.invalidsite.title";
						msgText  = "update.error.invalidsite.message";
						break;
					}
					final String title   = Plugin.translate(msgTitle);
					final String message = Plugin.translate(msgText);
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							MessageDialog.openWarning(parent, title, message);
							Logger log = LoggerFactory.getLogger(UpdateHandler.class);
							log.info(message);
							log.info(status.getMessage());
							if (status.getException() != null) {
								log.error("exception is ", status.getException());
							}
						}
					});
					return Status.CANCEL_STATUS;
				}

				/* 3. Ask if updates should be installed and run installation */

				// found updates, ask user if to install?
				if (status.isOK() && status.getSeverity() != IStatus.ERROR) {
					sync.syncExec(new Runnable() {
						@Override
						public void run() {
							String updates = "";
							Update[] possibleUpdates = operation.getPossibleUpdates();
							for (Update update : possibleUpdates) {
								updates += update + "\n";
							}
							doInstall = MessageDialog.openQuestion(parent, "Really install updates?", updates);
						}
					});
				}

				// start installation
				if (doInstall) {
					final ProvisioningJob provisioningJob = operation.getProvisioningJob(monitor);
					// updates cannot run from within Eclipse IDE!!!
					if (provisioningJob == null) {
						System.err.println("Running update from within Eclipse IDE? This won't work!!!");
						throw new NullPointerException();
					}

					// register a job change listener to track
					// installation progress and notify user upon success
					provisioningJob.addJobChangeListener(new JobChangeAdapter() {
						@Override
						public void done(IJobChangeEvent event) {
							if (event.getResult().isOK()) {
								sync.syncExec(new Runnable() {

									@Override
									public void run() {
										boolean restart = MessageDialog.openQuestion(parent, "Updates installed, restart?", "Updates have been installed successfully, do you want to restart?");
										try {
											IPreferences prefs = Plugin.getUserPreferences();
											prefs.putBoolean("resetApplication", true);
											prefs.flush();
										} catch (BackingStoreException e) {
											
										}
										if (restart) {
											workbench.restart();
										}
									}
								});

							}
							super.done(event);
						}
					});

					provisioningJob.schedule();
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
}
