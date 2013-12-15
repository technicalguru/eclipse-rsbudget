/**
 * 
 */
package rsbudget.data.api.dao;

import rs.baselib.util.RsDate;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.AccountStatus;

/**
 * DAO for account status.
 * @author ralph
 *
 */
public interface AccountStatusDAO extends RsBudgetDAO<Long, AccountStatus> {

	/**
	 * Returns the latest status information before a given point in time for an account.
	 * @param account account
	 * @param timestamp timestamp
	 * @return last status before timestamp
	 */
	public AccountStatus findLatestBy(Account account, RsDate timestamp);
	
	/**
	 * Returns the nearest status information around a given point in time for an account.
	 * @param account account
	 * @param timestamp timestamp
	 * @param maxDiffMilliseconds maximum difference from timestamp allowed
	 * @return last status before timestamp
	 */
	public AccountStatus findNearest(Account account, RsDate timestamp, long maxDiffMilliseconds);

	/**
	 * Returns the first status information (lowest month)
	 * @return first status
	 */
	public AccountStatus findFirst();

}
