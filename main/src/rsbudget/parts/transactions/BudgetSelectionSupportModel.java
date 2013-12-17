/**
 * 
 */
package rsbudget.parts.transactions;

import java.util.ArrayList;
import java.util.List;

import rs.baselib.util.IWrapper;
import rsbudget.celledit.BoSelectionSupportModel;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.api.dao.BudgetDAO;

/**
 * Selects the options according to the current row.
 * @author ralph
 *
 */
public class BudgetSelectionSupportModel extends BoSelectionSupportModel<Budget> {

	/**
	 * Constructor.
	 */
	public BudgetSelectionSupportModel(BudgetDAO dao, String beanProperty) {
		super(dao, beanProperty);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Budget> _getOptions(Object object) {
		if (object instanceof IWrapper) object = ((IWrapper)object).getWrapped();
		Plan plan = null;
		if (object instanceof PlannedTransaction) {
			plan = ((PlannedTransaction)object).getPlan();
		} else 	if (object instanceof Transaction) {
			plan = ((Transaction)object).getPlan();
		}
		List<Budget> rc = new ArrayList<>();
		if (plan != null) {
			rc.addAll(plan.getBudgets());
		}
		return rc;
	}

	
}
