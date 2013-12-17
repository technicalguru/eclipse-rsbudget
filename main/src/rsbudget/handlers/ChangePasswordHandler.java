/**
 * 
 */
package rsbudget.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;

import rs.e4.swt.ChangePasswordDialog;
import rs.e4.swt.IPasswordChangeRunnable;
import rsbudget.Plugin;
import rsbudget.data.api.dao.SettingDAO;

/**
 * Opens the dialog for changing passwords
 * @author ralph
 *
 */
public class ChangePasswordHandler implements IPasswordChangeRunnable {

	@Inject
	private SettingDAO settingDao;

	/**
	 * Constructor.
	 */
	public ChangePasswordHandler() {
		
	}

	@Execute
	public void execute(Shell shell) {
		ChangePasswordDialog dlg = new ChangePasswordDialog(shell, this);
		dlg.open();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCheckingOldPassword() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkOldPassword(char password[]) {
		return settingDao.checkUserPassword(password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String setNewPassword(char password[]) {
		if (!settingDao.changePassword(password)) {
			return Plugin.translate("changepassword.cannotset");
		}
		return null;
	}
	
	
}
