/**
 * 
 */
package rsbudget.data.api.dao;

import java.util.List;

import rs.baselib.util.RsMonth;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;

/**
 * DAO for planned transactions (plan for a month).
 * @author ralph
 *
 */
public interface PlannedTransactionDAO extends RsBudgetDAO<Long, PlannedTransaction> {

	/**
	 * Find all planned transactions from this month.
	 * @param month month to be found
	 * @return planned transactions
	 */
	public List<PlannedTransaction> findBy(RsMonth month);
	
	/**
	 * Find all planned transactions from this month.
	 * @param plan month to be found
	 * @return planned transactions
	 */
	public List<PlannedTransaction> findBy(Plan plan);
	
	/**
	 * Find all planned transactions from this category.
	 * @param category category to be found
	 * @return planned transactions
	 */
	public List<PlannedTransaction> findBy(Category category);
		
	/**
	 * Find all planned transactions from this budget.
	 * @param budget budget to be found
	 * @return planned transactions
	 */
	public List<PlannedTransaction> findBy(Budget budget);
		
}
