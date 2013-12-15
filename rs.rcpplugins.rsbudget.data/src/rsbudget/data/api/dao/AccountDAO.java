/**
 * 
 */
package rsbudget.data.api.dao;

import java.util.List;

import rsbudget.data.api.bo.Account;

/**
 * DAO for account.
 * @author ralph
 *
 */
public interface AccountDAO extends RsBudgetDAO<Long, Account> {

	/**
	 * Returns the account used as default for new objects. 
	 * @return default account
	 */
	public Account findDefault();
	
	/**
	 * Returns all accounts relevant for planning. 
	 * @return list of relevant accounts
	 */
	public List<Account> findRelevant();
	
	/**
	 * Find account by its name.
	 * @param name name of account
	 * @return account or null if not found
	 */
	public Account findByName(String name);
}
