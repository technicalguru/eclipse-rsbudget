/**
 * 
 */
package rsbudget.data.dto;


/**
 * An account at a bank.
 * @author ralph
 *
 */
public class AccountDTO extends RsBudgetDTO {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 8880623743270335233L;

	private String name;
	private String owner;
	private BankDTO bank;
	private String accountNumber;
	private String login;
	private String password;
	private boolean active;
	private boolean planningRelevant;

	/**
	 * Constructor.
	 */
	public AccountDTO() {
	}

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the owner.
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Sets the owner.
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}


	/**
	 * Returns the bank.
	 * @return the bank
	 */
	public BankDTO getBank() {
		return bank;
	}

	/**
	 * Sets the bank.
	 * @param bank the bank to set
	 */
	public void setBank(BankDTO bank) {
		this.bank = bank;
	}

	/**
	 * Returns the accountNumber.
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * Sets the accountNumber.
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * Returns the login.
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Sets the login.
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Returns the password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns the active.
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Returns the planningRelevant.
	 * @return the planningRelevant
	 */
	public boolean isPlanningRelevant() {
		return planningRelevant;
	}

	/**
	 * Sets the planningRelevant.
	 * @param planningRelevant the planningRelevant to set
	 */
	public void setPlanningRelevant(boolean planningRelevant) {
		this.planningRelevant = planningRelevant;
	}

	
}
