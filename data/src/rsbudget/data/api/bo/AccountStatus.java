/**
 * 
 */
package rsbudget.data.api.bo;

import rs.baselib.util.RsDate;

/**
 * The status of the account (balance) at a certain point in time.
 * @author ralph
 *
 */
public interface AccountStatus extends RsBudgetBO<Long> {

	public static final String PROPERTY_ACCOUNT   = "account";
	public static final String PROPERTY_TIMESTAMP = "timestamp";
	public static final String PROPERTY_BALANCE   = "balance";

	/**
	 * Returns the account.
	 * @return the account
	 */
	public Account getAccount();

	/**
	 * Sets the account.
	 * @param account the account to set
	 */
	public void setAccount(Account account);

	/**
	 * Returns the timestamp.
	 * @return the timestamp
	 */
	public RsDate getTimestamp();

	/**
	 * Sets the timestamp.
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(RsDate timestamp);

	/**
	 * Returns the balance.
	 * @return the balance
	 */
	public float getBalance();

	/**
	 * Sets the balance.
	 * @param balance the balance to set
	 */
	public void setBalance(float balance);

	
}
