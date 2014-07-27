/**
 * 
 */
package rsbudget.parts.transactions;

import java.io.IOException;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.LoggerFactory;

import rsbudget.Plugin;
import rsbudget.data.api.bo.Plan;
import rsbudget.util.ExportUtil;

/**
 * Handles the export of the transactions.
 * @author ralph
 *
 */
public class ExportHandler {

	/**
	 * Constructor.
	 */
	public ExportHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, @Active MPart part, Shell shell) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			Plan plan = txPart.getPlan();
			if (plan != null) {
				try {
					ExportUtil.exportTransactions(shell, plan.getMixedTransactions());
				} catch (IOException e) {
					LoggerFactory.getLogger(getClass()).error("Error while exporting transactions", e);
					MessageDialog.openError(shell, Plugin.translate("%export.error.title"), Plugin.translate("export.error.io.message"));
				} catch (Exception e) {
					LoggerFactory.getLogger(getClass()).error("Error while exporting transactions", e);
					MessageDialog.openError(shell, Plugin.translate("%export.error.title"), Plugin.translate("export.error.unknown.message"));
				}
			}
		}
	}

	/**
	 * Only active when there is a plan.
	 * @param context the context
	 * @return <code>true</code> when the handler can be executed
	 */
	@CanExecute
	public boolean canExecute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		if  ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			return txPart.getPlan() != null;
		}
		return false;
	}
}
