/**
 * 
 */
package rsbudget.data.impl.bo;

import rs.baselib.util.DefaultComparator;
import rsbudget.data.api.bo.HistoricalItem;
import rsbudget.data.dto.HistoricalItemDTO;

/**
 * The Account BO wrapper.
 * @author ralph
 *
 */
public class HistoricalItemBO extends AbstractRsBudgetDbBO<HistoricalItemDTO> implements HistoricalItem {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -213589589726199267L;

	/**
	 * Constructor with empty DTO.
	 */
	public HistoricalItemBO() {
		this(new HistoricalItemDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public HistoricalItemBO(HistoricalItemDTO dto) {
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
	public String getUnit() {
		return getTransferObject().getUnit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUnit(String unit) {
		String oldValue = getUnit();
		getTransferObject().setUnit(unit);
		firePropertyChange(PROPERTY_UNIT, oldValue, unit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isShowHistory() {
		return getTransferObject().isShowHistory();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShowHistory(boolean showHistory) {
		boolean oldValue = isShowHistory();
		getTransferObject().setShowHistory(showHistory);
		firePropertyChange(PROPERTY_SHOW_HISTORY, oldValue, showHistory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isShowChanges() {
		return getTransferObject().isShowChanges();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setShowChanges(boolean showChanges) {
		boolean oldValue = isShowChanges();
		getTransferObject().setShowChanges(showChanges);
		firePropertyChange(PROPERTY_SHOW_CHANGES, oldValue, showChanges);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFloatValue() {
		return getTransferObject().isFloatValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFloatValue(boolean floatValue) {
		boolean oldValue = isFloatValue();
		getTransferObject().setFloatValue(floatValue);
		firePropertyChange(PROPERTY_FLOAT_VALUE, oldValue, floatValue);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(HistoricalItem o) {
		return DefaultComparator.INSTANCE.compare(getName(), o.getName());
	}

	
}
