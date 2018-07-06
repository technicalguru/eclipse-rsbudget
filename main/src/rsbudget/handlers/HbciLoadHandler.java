
package rsbudget.handlers;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Shell;

import rsbudget.Plugin;
import rsbudget.hbci.HbciSyncDialog;
import rsbudget.license.Module;
import rsbudget.license.RsBudgetLicenseManager;

/**
 * Handles the "Load by HBCI" command by opening the dialog.
 * @author ralph
 *
 */
public class HbciLoadHandler {

	@Execute
	public void execute(IWorkbench workbench, IEclipseContext context, Shell shell) {
		// Open the HBCI Sync Dialog
		if (RsBudgetLicenseManager.isLicensed(Module.HBCI)) {
			final HbciSyncDialog dlg = new HbciSyncDialog(shell, workbench);
			BusyIndicator.showWhile(shell.getDisplay(), new Runnable() {
				public void run() {
					dlg.create();
				}
			});
			dlg.open();
		} else {
			// ErrorDialog
			MessageDialog.openInformation(shell, Plugin.translate("error.nolicense.title"), Plugin.translate("error.nolicense.text"));
		}
	}

}