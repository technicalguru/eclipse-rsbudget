/**
 * 
 */
package rsbudget.data.impl.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import rs.baselib.util.RsMonth;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.dao.BudgetDAO;
import rsbudget.data.dto.BudgetDTO;
import rsbudget.data.impl.bo.BudgetBO;

/**
 * DAO for budgets.
 * @author ralph
 *
 */
public class BudgetDAOImpl extends AbstractRsBudgetDbDAO<BudgetDTO, BudgetBO, Budget> implements BudgetDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Budget> findBy(RsMonth month) {
		return findBy(getFactory().getPlanDAO().findBy(month));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Budget> findBy(Plan plan) {
		if (plan == null) return null;
		Criterion c1 = Restrictions.sqlRestriction("{alias}.effective_from >= ?", new Timestamp(plan.getMonth().getBegin().getTimeInMillis()), StandardBasicTypes.TIMESTAMP);
		Criterion c2 = Restrictions.sqlRestriction("{alias}.effective_until <= ?", new Timestamp(plan.getMonth().getEnd().getTimeInMillis()), StandardBasicTypes.TIMESTAMP);
		return findByCriteria(buildCriteria(c1, c2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void afterDelete(Budget budget) {
		super.afterDelete(budget);
		Plan plan = budget.getPlan();
		if (plan != null) plan.removeBudget(budget);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Budget> findBy(Category category) {
		return findByCriteria(buildCriteria(Restrictions.eq("category.id", category.getId())));
	}
	
	
}
