/**
 * 
 */
package rsbudget.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;

/**
 * Exits the application.
 * @author ralph
 *
 */
public class ExitHandler {

	/**
	 * Constructor.
	 */
	public ExitHandler() {
	}

	/**
	 * Close the workbench
	 * @param workbench
	 */
	@Execute
	public void execute(IWorkbench workbench) {
		workbench.close();
	}
}
