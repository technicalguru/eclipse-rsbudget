/**
 * 
 */
package rsbudget.parts.transactions;

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
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;

/**
 * Deletes a transaction.
 * @author ralph
 *
 */
public class DeleteTransactionHandler {

	@Inject
	private RsBudgetDaoFactory factory;
	/** The selected row */
	private TxRowWrapper row;
	
	
	/**
	 * Constructor.
	 */
	public DeleteTransactionHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(Shell shell) {
		try {
			factory.begin();
			// Check that category has no transactions or budgets 
			if (row.isBudget()) {
				boolean rc = MessageDialog.openConfirm(shell, Plugin.translate("part.transactions.dialog.deleterow.title"), Plugin.translate("part.transactions.dialog.deletebudget.message", row.getText()));
				if (rc) {
					factory.getBudgetDAO().delete((Budget)row.getWrapped());
					this.row = null;
				}
			} else if (row.isPlannedTransaction()) {
				boolean rc = MessageDialog.openConfirm(shell, Plugin.translate("part.transactions.dialog.deleterow.title"), Plugin.translate("part.transactions.dialog.deleteptx.message", row.getText()));
				if (rc) {
					PlannedTransaction ptx = (PlannedTransaction)row.getWrapped();
					if (ptx.getTransaction() != null) {
						ptx.getTransaction().setPlannedTransaction(null);
						factory.getTransactionDAO().save(ptx.getTransaction());
					}
					factory.getPlannedTransactionDAO().delete(ptx);
					this.row = null;
				}
			} else {
				boolean rc = MessageDialog.openConfirm(shell, Plugin.translate("part.transactions.dialog.deleterow.title"), Plugin.translate("part.transactions.dialog.deletetx.message", row.getText()));
				if (rc) {
					Transaction tx = (Transaction)row.getWrapped();
					if (tx.getPlannedTransaction() != null) {
						tx.getPlannedTransaction().setTransaction(null);
						factory.getPlannedTransactionDAO().save(tx.getPlannedTransaction());
						this.row.transactionDeleted();
					}
					factory.getTransactionDAO().delete(tx);
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
		if (row.isTransaction() || row.isPlannedTransaction()) return true;
		Budget budget = (Budget)row.getWrapped();
		try {
			factory.begin();
			List<Transaction> l = factory.getTransactionDAO().findBy(budget);
			if ((l != null) && (l.size() > 0)) return false;
			List<PlannedTransaction> l2 = factory.getPlannedTransactionDAO().findBy(budget);
			return (l2 == null) || (l2.size() == 0);
		} finally {
			factory.commit();
		}
	}
	
	/**
	 * Get selection from service.
	 * @param row
	 */
	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) TxRowWrapper transactions[]) {
		if (transactions == null) {
			// No selection
		} else if (transactions.length == 1) {
			this.row = transactions[0];
		} else {
			this.row = null;
		}
	}


}
