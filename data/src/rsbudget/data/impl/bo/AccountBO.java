/**
 * 
 */
package rsbudget.data.impl.bo;

import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Bank;
import rsbudget.data.dto.AccountDTO;

/**
 * The Account BO wrapper.
 * @author ralph
 *
 */
public class AccountBO extends AbstractRsBudgetDbBO<AccountDTO> implements Account {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -8132460234787045758L;

	/**
	 * Constructor with empty DTO.
	 */
	public AccountBO() {
		this(new AccountDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public AccountBO(AccountDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return getTransferObject().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		String oldValue = getName();
		getTransferObject().setName(name);
		firePropertyChange(PROPERTY_NAME, oldValue, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getOwner() {
		return getTransferObject().getOwner();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setOwner(String owner) {
		String oldValue = getOwner();
		getTransferObject().setOwner(owner);
		firePropertyChange(PROPERTY_OWNER, oldValue, owner);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bank getBank() {
		return (Bank)getBusinessObject(getTransferObject().getBank());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBank(Bank bank) {
		Bank oldValue = getBank();
		getTransferObject().setBank(((BankBO)bank).getTransferObject());
		firePropertyChange(PROPERTY_BANK, oldValue, bank);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAccountNumber() {
		return getTransferObject().getAccountNumber();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAccountNumber(String accountNumber) {
		String oldValue = getAccountNumber();
		getTransferObject().setAccountNumber(accountNumber);
		firePropertyChange(PROPERTY_ACCOUNT_NUMBER, oldValue, accountNumber);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLogin() {
		return getTransferObject().getLogin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLogin(String login) {
		String oldValue = getLogin();
		getTransferObject().setLogin(login);
		firePropertyChange(PROPERTY_LOGIN, oldValue, login);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPassword() {
		return getTransferObject().getPassword();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPassword(String password) {
		String oldValue = getPassword();
		getTransferObject().setPassword(password);
		firePropertyChange(PROPERTY_PASSWORD, oldValue, password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isActive() {
		return getTransferObject().isActive();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setActive(boolean active) {
		boolean oldValue = isActive();
		getTransferObject().setActive(active);
		firePropertyChange(PROPERTY_ACTIVE, oldValue, active);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPlanningRelevant() {
		return getTransferObject().isPlanningRelevant();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlanningRelevant(boolean planningRelevant) {
		boolean oldValue = isPlanningRelevant();
		getTransferObject().setPlanningRelevant(planningRelevant);
		firePropertyChange(PROPERTY_PLANNING_RELEVANT, oldValue, planningRelevant);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay() {
		return getName();
	}

}
