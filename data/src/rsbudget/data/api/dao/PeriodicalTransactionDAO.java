/**
 * 
 */
package rsbudget.data.api.dao;

import java.util.List;

import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;

/**
 * DAO for periodical transactions (general plan).
 * @author ralph
 *
 */
public interface PeriodicalTransactionDAO extends RsBudgetDAO<Long, PeriodicalTransaction> {

	/**
	 * Find all periodical transactions from this category.
	 * @param category category to be found
	 * @return periodical transactions
	 */
	public List<PeriodicalTransaction> findBy(Category category);
	
	/**
	 * Returns the transaction with given name. 
	 * @return transaction
	 */
	public PeriodicalTransaction findBy(String name);
	
	/**
	 * Returns the highest display order in use.
	 * @return
	 */
	public int getMaxDisplayOrder();
	
	/**
	 * Find all periodical transactions from this budget.
	 * @param budget budget to be found
	 * @return periodical transactions
	 */
	public List<PeriodicalTransaction> findBy(PeriodicalBudget budget);
	
}
