/**
 * 
 */
package rsbudget.parts.budgets;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import rsbudget.Plugin;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.util.PlannedPeriod;

/**
 * Creates a new category.
 * @author ralph
 *
 */
public class NewBudgetHandler {

	@Inject
	private RsBudgetDaoFactory factory;
	
	/**
	 * Constructor.
	 */
	public NewBudgetHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, @Active MPart part) {
		// Always create a transaction
		factory.begin();
		PeriodicalTransaction newRow = factory.getPeriodicalTransactionDAO().newInstance();
		String name = Plugin.translate("part.budgets.label.newtx"); int index = 0;
		while (factory.getPeriodicalTransactionDAO().findBy(name) != null) {
			index++;
			name = Plugin.translate("part.budgets.label.newtx.indexed", index);
		}
		newRow.setName(name);
		newRow.setAccount(factory.getAccountDAO().findDefault());
		newRow.setCategory(factory.getCategoryDAO().findDefault());
		newRow.setPlannedPeriod(PlannedPeriod.MONTHLY);
		newRow.setMonthSequenceNumber(1);
		newRow.setAmount(0);
		newRow.setDisplayOrder(factory.getPeriodicalTransactionDAO().getMaxDisplayOrder()+1);
		factory.getPeriodicalTransactionDAO().create(newRow);
		factory.commit();
		
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof BudgetPart)) {
			BudgetPart budgetPart = (BudgetPart)customPart;
			budgetPart.edit(newRow);
		}
	}
	
}
