/**
 * 
 */
package rsbudget.data.impl.bo;

import rs.baselib.util.RsDate;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.AccountStatus;
import rsbudget.data.dto.AccountStatusDTO;

/**
 * Account status BO wrapper.
 * @author ralph
 *
 */
public class AccountStatusBO extends AbstractRsBudgetDbBO<AccountStatusDTO> implements AccountStatus {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 711619059038129758L;

	/**
	 * Constructor with empty DTO.
	 */
	public AccountStatusBO() {
		this(new AccountStatusDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public AccountStatusBO(AccountStatusDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account getAccount() {
		return (Account)getBusinessObject(getTransferObject().getAccount());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAccount(Account account) {
		Account oldValue = getAccount();
		getTransferObject().setAccount(((AccountBO)account).getTransferObject());
		firePropertyChange(PROPERTY_ACCOUNT, oldValue, account);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getTimestamp() {
		return getTransferObject().getTimestamp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTimestamp(RsDate timestamp) {
		RsDate oldValue = getTimestamp();
		getTransferObject().setTimestamp(timestamp);
		firePropertyChange(PROPERTY_TIMESTAMP, oldValue, timestamp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getBalance() {
		return getTransferObject().getBalance();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBalance(float balance) {
		float oldValue = getBalance();
		getTransferObject().setBalance(balance);
		firePropertyChange(PROPERTY_BALANCE, oldValue, balance);
	}

	
}
