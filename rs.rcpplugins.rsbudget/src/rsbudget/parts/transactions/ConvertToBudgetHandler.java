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
public class ConvertToBudgetHandler {

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
			Budget budget = factory.getBudgetDAO().newInstance();
			PlannedTransaction tx = (PlannedTransaction)row.getWrapped();
			budget.setName(tx.getName());
			budget.setCategory(tx.getCategory());
			budget.setPlan(tx.getPlan());
			budget.setChangeDate(tx.getChangeDate());
			budget.setCreationDate(tx.getCreationDate());
			budget.setAmount(tx.getAmount());
			budget.setDisplayOrder(budget.getPlan().getMaxBudgetDisplayOrder()+1);
			factory.getBudgetDAO().create(budget, false);
			factory.getPlannedTransactionDAO().delete(tx);
			this.row = null;
			Object customPart = part.getObject();
			if ((customPart != null) && (customPart instanceof TransactionsPart)) {
				TransactionsPart txPart = (TransactionsPart)customPart;
				txPart.reveal(budget);
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
		return (row != null) && row.isPlannedTransaction();
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
