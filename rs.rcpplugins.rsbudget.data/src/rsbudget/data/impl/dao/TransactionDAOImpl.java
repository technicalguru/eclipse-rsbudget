/**
 * 
 */
package rsbudget.data.impl.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;

import rs.baselib.util.DateTimePeriod;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.api.dao.TransactionDAO;
import rsbudget.data.dto.TransactionDTO;
import rsbudget.data.impl.bo.TransactionBO;

/**
 * DAO for actual transactions.
 * @author ralph
 *
 */
public class TransactionDAOImpl extends AbstractRsBudgetDbDAO<TransactionDTO, TransactionBO, Transaction> implements TransactionDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transaction> findBy(Account account, DateTimePeriod period) {
		Criterion c1 = Restrictions.eq("account.id", account.getId());
		Criterion c2 = Restrictions.sqlRestriction("{alias}.value_date >= ?", new Timestamp(period.getFrom().getTimeInMillis()), StandardBasicTypes.TIMESTAMP);
		Criterion c3 = Restrictions.sqlRestriction("{alias}.value_date <= ?", new Timestamp(period.getUntil().getTimeInMillis()), StandardBasicTypes.TIMESTAMP);
		return findByCriteria(buildCriteria(c1, c2, c3));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void afterCreate(Transaction tx) {
		super.afterCreate(tx);
		// Inform the planned transaction
		PlannedTransaction ptx = tx.getPlannedTransaction();
		if (ptx != null) {
			ptx.setTransaction(tx);
			ptx.getDao().saveObject(ptx);
		}
		// Inform the budget
		Budget budget = tx.getBudget();
		if (budget != null) {
			budget.addTransaction(tx);
		}
		// Inform the plan
		Plan plan = tx.getPlan();
		if (plan != null) {
			plan.addTransaction(tx);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void afterDelete(Transaction tx) {
		super.afterDelete(tx);
		// Inform the planned transaction
		PlannedTransaction ptx = tx.getPlannedTransaction();
		if (ptx != null) {
			ptx.setTransaction(null);
			ptx.getDao().saveObject(ptx);
		}
		// Inform the budget
		Budget budget = tx.getBudget();
		if (budget != null) {
			budget.removeTransaction(tx);
		}
		// Inform the plan
		Plan plan = tx.getPlan();
		if (plan != null) {
			plan.removeTransaction(tx);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transaction> findBy(Category category) {
		return findByCriteria(buildCriteria(Restrictions.eq("category.id", category.getId())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Transaction> findBy(Budget budget) {
		return findByCriteria(buildCriteria(Restrictions.eq("budget.id", budget.getId())));
	}
	
}
