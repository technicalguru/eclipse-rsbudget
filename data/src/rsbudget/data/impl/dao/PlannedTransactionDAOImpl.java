/**
 * 
 */
package rsbudget.data.impl.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rs.baselib.util.RsMonth;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.api.dao.PlannedTransactionDAO;
import rsbudget.data.dto.PlannedTransactionDTO;
import rsbudget.data.impl.bo.PlannedTransactionBO;

/**
 * DAO for planned transactions (plan for a month).
 * @author ralph
 *
 */
public class PlannedTransactionDAOImpl extends AbstractRsBudgetDbDAO<PlannedTransactionDTO, PlannedTransactionBO, PlannedTransaction> implements PlannedTransactionDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PlannedTransaction> findBy(RsMonth month) {
		return findBy(getFactory().getPlanDAO().findBy(month));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PlannedTransaction> findBy(Plan plan) {
		if (plan == null) return null;
		Criterion c = Restrictions.eqOrIsNull("plan.id", plan.getId());
//		Criterion c1 = Restrictions.sqlRestriction("{alias}.effective_from >= ?", new Timestamp(plan.getMonth().getBegin().getTimeInMillis()), StandardBasicTypes.TIMESTAMP);
//		Criterion c2 = Restrictions.sqlRestriction("{alias}.effective_until <= ?", new Timestamp(plan.getMonth().getEnd().getTimeInMillis()), StandardBasicTypes.TIMESTAMP);
		return findByCriteria(buildCriteria(c));
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void beforeCreate(PlannedTransaction ptx) {
		super.beforeCreate(ptx);
		if (ptx.getDisplayOrder() == 0) {
			ptx.setDisplayOrder(ptx.getPlan().getMaxTxDisplayOrder()+1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void afterCreate(PlannedTransaction ptx) {
		super.afterCreate(ptx);
		Plan plan = ptx.getPlan();
		if (plan != null) {
			plan.addPlannedTransaction(ptx);
		}
		Budget budget = ptx.getBudget();
		if (budget != null) {
			budget.addPlannedTransaction(ptx);
		}
		Transaction tx = ptx.getTransaction();
		if (tx != null) {
			tx.setPlannedTransaction(ptx);
			tx.getDao().saveObject(tx);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void afterDelete(PlannedTransaction ptx) {
		super.afterDelete(ptx);
		Transaction tx = ptx.getTransaction();
		if (tx != null) {
			tx.setPlannedTransaction(null);
			tx.getDao().saveObject(tx);
		}
		Budget budget = ptx.getBudget();
		if (budget != null) {
			budget.removePlannedTransaction(ptx);
		}
		Plan plan = ptx.getPlan();
		if (plan != null) {
			plan.removePlannedTransaction(ptx);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PlannedTransaction> findBy(Category category) {
		return findByCriteria(buildCriteria(Restrictions.eq("category.id", category.getId())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PlannedTransaction> findBy(Budget budget) {
		return findByCriteria(buildCriteria(Restrictions.eq("budget.id", budget.getId())));
	}
	
	
}
