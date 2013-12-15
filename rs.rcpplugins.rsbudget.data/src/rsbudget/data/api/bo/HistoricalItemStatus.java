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
public interface HistoricalItemStatus extends RsBudgetBO<Long> {

	public static final String PROPERTY_ITEM      = "item";
	public static final String PROPERTY_TIMESTAMP = "timestamp";
	public static final String PROPERTY_VALUE     = "value";

	/**
	 * Returns the item.
	 * @return the item
	 */
	public HistoricalItem getItem();

	/**
	 * Sets the item.
	 * @param item the item to set
	 */
	public void setItem(HistoricalItem item);
	
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
	 * Returns the value.
	 * @return the value
	 */
	public float getValue();

	/**
	 * Sets the value.
	 * @param value the value to set
	 */
	public void setValue(float value);

	
}
