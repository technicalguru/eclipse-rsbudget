/**
 * 
 */
package rsbudget.data.api.bo;



/**
 * An account at a bank.
 * @author ralph
 *
 */
public interface Account extends RsBudgetBO<Long> {

	public static final String PROPERTY_NAME              = "name";
	public static final String PROPERTY_OWNER             = "owner";
	public static final String PROPERTY_BANK              = "bank";
	public static final String PROPERTY_ACCOUNT_NUMBER    = "accountNumber";
	public static final String PROPERTY_LOGIN             = "login";
	public static final String PROPERTY_PASSWORD          = "password";
	public static final String PROPERTY_ACTIVE            = "active";
	public static final String PROPERTY_PLANNING_RELEVANT = "planningRelevant";
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName();

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name);

	/**
	 * Returns the owner.
	 * @return the owner
	 */
	public String getOwner();

	/**
	 * Sets the owner.
	 * @param owner the owner to set
	 */
	public void setOwner(String owner);


	/**
	 * Returns the bank.
	 * @return the bank
	 */
	public Bank getBank();

	/**
	 * Sets the bank.
	 * @param bank the bank to set
	 */
	public void setBank(Bank bank);

	/**
	 * Returns the accountNumber.
	 * @return the accountNumber
	 */
	public String getAccountNumber();

	/**
	 * Sets the accountNumber.
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber);

	/**
	 * Returns the login.
	 * @return the login
	 */
	public String getLogin();

	/**
	 * Sets the login.
	 * @param login the login to set
	 */
	public void setLogin(String login);

	/**
	 * Returns the password.
	 * @return the password
	 */
	public String getPassword();

	/**
	 * Sets the password.
	 * @param password the password to set
	 */
	public void setPassword(String password);

	/**
	 * Returns the active.
	 * @return the active
	 */
	public boolean isActive();

	/**
	 * Sets the active.
	 * @param active the active to set
	 */
	public void setActive(boolean active);

	/**
	 * Returns the planningRelevant.
	 * @return the planningRelevant
	 */
	public boolean isPlanningRelevant();

	/**
	 * Sets the planningRelevant.
	 * @param planningRelevant the planningRelevant to set
	 */
	public void setPlanningRelevant(boolean planningRelevant);

}
