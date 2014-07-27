/**
 * 
 */
package rsbudget.parts.budgets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.LoggerFactory;

import rs.baselib.util.IWrapper;
import rsbudget.Plugin;
import rsbudget.data.api.bo.RsBudgetBO;
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
		if ((customPart != null) && (customPart instanceof BudgetPart)) {
			BudgetPart txPart = (BudgetPart)customPart;
			try {
				IObservableList budgets = txPart.getBudgets();
				List<RsBudgetBO<?>> l = new ArrayList<RsBudgetBO<?>>();
				for (Object o : budgets) {
					IWrapper row = (IWrapper)o;
					l.add((RsBudgetBO<?>)row.getWrapped());
				}
				ExportUtil.exportPlanning(shell, l);
			} catch (IOException e) {
				LoggerFactory.getLogger(getClass()).error("Error while exporting transactions", e);
				MessageDialog.openError(shell, Plugin.translate("%export.error.title"), Plugin.translate("export.error.io.message"));
			} catch (Exception e) {
				LoggerFactory.getLogger(getClass()).error("Error while exporting transactions", e);
				MessageDialog.openError(shell, Plugin.translate("%export.error.title"), Plugin.translate("export.error.unknown.message"));
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
		return (customPart != null) && (customPart instanceof BudgetPart);
	}
}
