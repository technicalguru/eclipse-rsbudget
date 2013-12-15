/**
 * 
 */
package rsbudget.preferences;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Handles the preferences dialog.
 * @author ralph
 *
 */
public class PreferencesHandler {

	/**
	 * Constructor.
	 */
	public PreferencesHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IWorkbench workbench, IEclipseContext context, Shell shell) {
		PreferencesDialog dlg = new PreferencesDialog(shell, workbench);
		dlg.create();
		if (dlg.open() == Dialog.OK) {
			// Do something!
		}
	}

}
