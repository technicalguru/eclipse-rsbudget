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
public class HistoricalItemStatusDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -4705618778940730224L;

	private HistoricalItemDTO item;
	private RsDate timestamp;
	private float value;
	
	/**
	 * Constructor.
	 */
	public HistoricalItemStatusDTO() {
	}

	/**
	 * Returns the item.
	 * @return the item
	 */
	public HistoricalItemDTO getItem() {
		return item;
	}

	/**
	 * Sets the item.
	 * @param item the item to set
	 */
	public void setItem(HistoricalItemDTO item) {
		this.item = item;
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
	 * Returns the value.
	 * @return the value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}


}
