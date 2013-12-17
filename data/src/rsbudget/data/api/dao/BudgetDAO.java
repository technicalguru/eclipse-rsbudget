/**
 * 
 */
package rsbudget.data.api.dao;

import java.util.List;

import rs.baselib.util.RsMonth;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.Plan;

/**
 * DAO for budgets.
 * @author ralph
 *
 */
public interface BudgetDAO extends RsBudgetDAO<Long, Budget> {

	/**
	 * Find all budgets from this month.
	 * @param month month to be found
	 * @return budgets
	 */
	public List<Budget> findBy(RsMonth month);
	
	/**
	 * Find all budgets from this month.
	 * @param plan month to be found
	 * @return budgets
	 */
	public List<Budget> findBy(Plan plan);

	/**
	 * Find all budgets from this category.
	 * @param category category to be found
	 * @return budgets
	 */
	public List<Budget> findBy(Category category);
}
