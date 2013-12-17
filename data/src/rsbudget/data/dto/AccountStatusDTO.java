/**
 * 
 */
package rsbudget.data.dto;

import rs.baselib.util.RsDate;

/**
 * The status of the account (balance) at a certain point in time.
 * @author ralph
 *
 */
public class AccountStatusDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 5624126810418577856L;

	private AccountDTO account;
	private RsDate timestamp;
	private float balance;
	
	/**
	 * Constructor.
	 */
	public AccountStatusDTO() {
	}

	/**
	 * Returns the account.
	 * @return the account
	 */
	public AccountDTO getAccount() {
		return account;
	}

	/**
	 * Sets the account.
	 * @param account the account to set
	 */
	public void setAccount(AccountDTO account) {
		this.account = account;
	}

	/**
	 * Returns the timestamp.
	 * @return the timestamp
	 */
	public RsDate getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(RsDate timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Returns the balance.
	 * @return the balance
	 */
	public float getBalance() {
		return balance;
	}

	/**
	 * Sets the balance.
	 * @param balance the balance to set
	 */
	public void setBalance(float balance) {
		this.balance = balance;
	}

	
}
