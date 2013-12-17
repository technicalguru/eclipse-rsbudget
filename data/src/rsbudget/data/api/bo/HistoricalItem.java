/**
 * 
 */
package rsbudget.data.api.bo;

/**
 * Keeps track of historical information, such as miles etc.
 * @author ralph
 *
 */
public interface HistoricalItem extends RsBudgetBO<Long>, Comparable<HistoricalItem> {

	public static final String PROPERTY_NAME             = "name";
	public static final String PROPERTY_UNIT             = "unit";
	public static final String PROPERTY_SHOW_HISTORY     = "showHistory";
	public static final String PROPERTY_SHOW_CHANGES     = "showChanges";
	public static final String PROPERTY_FLOAT_VALUE      = "floatValue";
	
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
	 * Returns the unit.
	 * @return the unit
	 */
	public String getUnit();

	/**
	 * Sets the unit.
	 * @param unit the unit to set
	 */
	public void setUnit(String unit);

	/**
	 * Returns the showHistory.
	 * @return the showHistory
	 */
	public boolean isShowHistory();

	/**
	 * Sets the showHistory.
	 * @param showHistory the showHistory to set
	 */
	public void setShowHistory(boolean showHistory);

	/**
	 * Returns the showChanges.
	 * @return the showChanges
	 */
	public boolean isShowChanges();

	/**
	 * Sets the showChanges.
	 * @param showChanges the showChanges to set
	 */
	public void setShowChanges(boolean showChanges);

	/**
	 * Returns the showChanges.
	 * @return the showChanges
	 */
	public boolean isFloatValue();

	/**
	 * Sets the showChanges.
	 * @param showChanges the showChanges to set
	 */
	public void setFloatValue(boolean floatValue);

}
