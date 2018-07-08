/**
 * 
 */
package rsbudget.data.impl.bo;

import java.math.BigDecimal;

import rs.baselib.util.RsDate;
import rsbudget.data.api.bo.HistoricalItem;
import rsbudget.data.api.bo.HistoricalItemStatus;
import rsbudget.data.dto.HistoricalItemStatusDTO;

/**
 * Account status BO wrapper.
 * @author ralph
 *
 */
public class HistoricalItemStatusBO extends AbstractRsBudgetDbBO<HistoricalItemStatusDTO> implements HistoricalItemStatus {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 9069442872933942852L;

	/**
	 * Constructor with empty DTO.
	 */
	public HistoricalItemStatusBO() {
		this(new HistoricalItemStatusDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public HistoricalItemStatusBO(HistoricalItemStatusDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HistoricalItem getItem() {
		return (HistoricalItem)getBusinessObject(getTransferObject().getItem());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setItem(HistoricalItem item) {
		HistoricalItem oldValue = getItem();
		getTransferObject().setItem(((HistoricalItemBO)item).getTransferObject());
		firePropertyChange(PROPERTY_ITEM, oldValue, item);
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
	public BigDecimal getValue() {
		return getTransferObject().getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(BigDecimal value) {
		BigDecimal oldValue = getValue();
		getTransferObject().setValue(value);
		firePropertyChange(PROPERTY_VALUE, oldValue, value);
	}

	
}
