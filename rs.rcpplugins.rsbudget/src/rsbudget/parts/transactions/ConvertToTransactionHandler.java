/**
 * 
 */
package rsbudget.parts.transactions;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;

import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.PlannedTransaction;

/**
 * Converts a transaction into a row.
 * @author ralph
 *
 */
public class ConvertToTransactionHandler {

	@Inject
	private RsBudgetDaoFactory factory;
	/** The selected row */
	private TxRowWrapper row;

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, @Active MPart part) {
		try {
			factory.begin();
			Budget budget = (Budget)row.getWrapped();
			PlannedTransaction tx = factory.getPlannedTransactionDAO().newInstance();
			tx.setName(budget.getName());
			tx.setCategory(budget.getCategory());
			tx.setPlan(budget.getPlan());
			tx.setAccount(factory.getAccountDAO().findDefault());
			tx.setChangeDate(budget.getChangeDate());
			tx.setCreationDate(budget.getCreationDate());
			tx.setAmount(budget.getAmount());
			tx.setDisplayOrder(tx.getPlan().getMaxTxDisplayOrder()+1);
			factory.getPlannedTransactionDAO().create(tx, false);
			factory.getBudgetDAO().delete(budget);
			this.row = null;
			Object customPart = part.getObject();
			if ((customPart != null) && (customPart instanceof TransactionsPart)) {
				TransactionsPart txPart = (TransactionsPart)customPart;
				txPart.reveal(tx);
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
		return (row != null) && row.isBudget();
	}
	
	/**
	 * Get selection from service.
	 * @param row
	 */
	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) TxRowWrapper budgets[]) {
		if (budgets == null) {
			// No selection
		} else if (budgets.length == 1) {
			this.row = budgets[0];
		} else {
			this.row = null;
		}
	}

}
