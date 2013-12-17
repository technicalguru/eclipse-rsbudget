/**
 * 
 */
package rsbudget.data.api.dao;

import java.util.List;

import rs.baselib.util.DateTimePeriod;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.Transaction;

/**
 * DAO for actual transactions.
 * @author ralph
 *
 */
public interface TransactionDAO extends RsBudgetDAO<Long, Transaction> {

	/**
	 * Find all transactions from this period.
	 * @param account account to be looked for
	 * @param period time period
	 * @return transactions
	 */
	public List<Transaction> findBy(Account account, DateTimePeriod period);

	/**
	 * Find all transactions from this category.
	 * @param category category to be found
	 * @return transactions
	 */
	public List<Transaction> findBy(Category category);
	
	/**
	 * Find all transactions from this budget.
	 * @param budget budget to be found
	 * @return transactions
	 */
	public List<Transaction> findBy(Budget budget);
}
