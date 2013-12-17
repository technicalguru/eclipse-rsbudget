/**
 * 
 */
package rsbudget.data.api.dao;

import java.util.List;

import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PeriodicalBudget;

/**
 * DAO for periodical budgets. (general budget plan)
 * @author ralph
 *
 */
public interface PeriodicalBudgetDAO extends RsBudgetDAO<Long, PeriodicalBudget> {

	/**
	 * Find all periodical budgets from this category.
	 * @param category category to be found
	 * @return periodical budgets
	 */
	public List<PeriodicalBudget> findBy(Category category);
}
