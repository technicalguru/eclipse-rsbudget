/**
 * 
 */
package rsbudget.parts.budgets;

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
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;

/**
 * Converts a transaction into a row.
 * @author ralph
 *
 */
public class ConvertToBudgetHandler {

	@Inject
	private RsBudgetDaoFactory factory;
	/** The selected row */
	private BudgetRowWrapper row;

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, @Active MPart part) {
		try {
			factory.begin();
			PeriodicalBudget budget = factory.getPeriodicalBudgetDAO().newInstance();
			PeriodicalTransaction tx = (PeriodicalTransaction)row.getWrapped();
			budget.setName(tx.getName());
			budget.setCategory(tx.getCategory());
			budget.setPlannedPeriod(tx.getPlannedPeriod());
			budget.setChangeDate(tx.getChangeDate());
			budget.setCreationDate(tx.getCreationDate());
			budget.setAmount(tx.getAmount());
			budget.setMonthSequenceNumber(tx.getMonthSequenceNumber());
			budget.setDisplayOrder(factory.getPeriodicalBudgetDAO().getObjectCount()+1);
			factory.getPeriodicalBudgetDAO().create(budget, false);
			factory.getPeriodicalTransactionDAO().delete(tx);
			this.row = null;
			Object customPart = part.getObject();
			if ((customPart != null) && (customPart instanceof BudgetPart)) {
				BudgetPart budgetPart = (BudgetPart)customPart;
				budgetPart.reveal(budget);
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
		return (row != null) && row.isTransaction();
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
