/**
 * 
 */
package rsbudget.data.impl.bo;

import java.net.URL;

import rsbudget.data.api.bo.Bank;
import rsbudget.data.dto.BankDTO;

/**
 * Bank BO wrapper.
 * @author ralph
 *
 */
public class BankBO extends AbstractRsBudgetDbBO<BankDTO> implements Bank {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1962141821983029160L;

	/**
	 * Constructor with empty DTO.
	 */
	public BankBO() {
		this(new BankDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public BankBO(BankDTO dto) {
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
	public String getBlz() {
		return getTransferObject().getBlz();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBlz(String blz) {
		String oldValue = getBlz();
		getTransferObject().setBlz(blz);
		firePropertyChange(PROPERTY_BLZ, oldValue, blz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getUrl() {
		return getTransferObject().getUrl();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUrl(URL url) {
		getTransferObject().setUrl(url);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay() {
		return getName();
	}

}
