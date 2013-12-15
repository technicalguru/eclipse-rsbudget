/**
 * 
 */
package rsbudget.parts.budgets;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import rsbudget.Plugin;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;

/**
 * Deletes a periodcial transaction or row.
 * @author ralph
 *
 */
public class DeleteBudgetHandler {

	@Inject
	private RsBudgetDaoFactory factory;
	/** The selected row */
	private BudgetRowWrapper row;
	
	/**
	 * Constructor.
	 */
	public DeleteBudgetHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(Shell shell) {
		try {
			factory.begin();
			// Check that row record has no associates records
			if (row.isBudget()) {
				boolean rc = MessageDialog.openConfirm(shell, Plugin.translate("part.budgets.dialog.deleterow.title"), Plugin.translate("part.budgets.dialog.deletebudget.message", row.getText()));
				if (rc) {
					factory.getPeriodicalBudgetDAO().delete((PeriodicalBudget)row.getWrapped());
					this.row = null;
				}
			} else {
				boolean rc = MessageDialog.openConfirm(shell, Plugin.translate("part.budgets.dialog.deleterow.title"), Plugin.translate("part.budgets.dialog.deletetx.message", row.getText()));
				if (rc) {
					factory.getPeriodicalTransactionDAO().delete((PeriodicalTransaction)row.getWrapped());
					this.row = null;
				}
			}
		} finally {
			factory.commit();
		}
	}
	
	/**
	 * Can we execute?
	 * @return
	 */
	@CanExecute
	public boolean canExecute() {
		if (row == null) return false;
		if (row.isTransaction()) return true;
		PeriodicalBudget budget = (PeriodicalBudget)row.getWrapped();
		try {
			factory.begin();
			List<PeriodicalTransaction> l = factory.getPeriodicalTransactionDAO().findBy(budget);
			return (l == null) || (l.size() == 0);
		} finally {
			factory.commit();
		}
	}

	/**
	 * Get selection from service.
	 * @param row
	 */
	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) BudgetRowWrapper budgets[]) {
		if (budgets == null) {
			// No selection
		} else if (budgets.length == 1) {
			this.row = budgets[0];
		} else {
			this.row = null;
		}
	}

}
