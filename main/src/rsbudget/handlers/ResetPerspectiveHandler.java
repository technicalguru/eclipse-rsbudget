/**
 * 
 */
package rsbudget.handlers;

import java.util.prefs.BackingStoreException;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import rs.baselib.prefs.IPreferences;
import rsbudget.Plugin;
import rsbudget.preferences.PreferencesUtils;

/**
 * Resets the perspective.
 * @author ralph
 *
 */
public class ResetPerspectiveHandler {
	
	@Execute
	public void execute(Shell shell, IPreferences preferences, IWorkbench workbench) {
		if (MessageDialog.openConfirm(shell, Plugin.translate("handler.resetperspective.title"), Plugin.translate("handler.resetperspective.message"))) {
			PreferencesUtils.setResetLayout(true);
			try {
				preferences.flush();
			} catch (BackingStoreException e) {
				e.printStackTrace();
			}
			Plugin.doRestart(workbench);
		}
	}
	
}