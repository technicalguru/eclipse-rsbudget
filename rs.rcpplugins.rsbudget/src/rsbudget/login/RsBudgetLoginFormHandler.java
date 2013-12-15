/**
 * 
 */
package rsbudget.login;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import rs.e4.splash.ISplashFormHandler;
import rsbudget.Plugin;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;

/**
 * @author ralph
 *
 */
public class RsBudgetLoginFormHandler implements ISplashFormHandler {

	private String password;
	
	/**
	 * Constructor.
	 */
	public RsBudgetLoginFormHandler() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Plugin.translate("splash.login.message.authenticating"), 6+RsBudgetModelService.TOTAL_LOAD_PROGRESS_TASK_COUNT);
		monitor.worked(1);
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory(monitor, 1);
		monitor.worked(2+RsBudgetModelService.TOTAL_LOAD_PROGRESS_TASK_COUNT);
		factory.begin();
		monitor.worked(3+RsBudgetModelService.TOTAL_LOAD_PROGRESS_TASK_COUNT);
		factory.getSettingDAO().setUserPassword(password.toCharArray());
		monitor.worked(4+RsBudgetModelService.TOTAL_LOAD_PROGRESS_TASK_COUNT);
		boolean rc = factory.getSettingDAO().checkUserPassword();
		monitor.worked(5+RsBudgetModelService.TOTAL_LOAD_PROGRESS_TASK_COUNT);
		factory.commit();
		monitor.done();
		if (rc) {
			return new Status(IStatus.OK, getClass().getSimpleName(), 0, Plugin.translate("splash.login.message.loggedin"), null);
		} 
		return new Status(IStatus.ERROR, getClass().getSimpleName(), 0, Plugin.translate("splash.login.message.invalidpassword"), null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSplashProperty(String property, Object value) {
		if (property.equals(ISplashFormHandler.PROPERTY_PASSWORD)) {
			password = (String)value;
		}
	}

	

}
