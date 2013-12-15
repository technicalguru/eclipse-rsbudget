/**
 * 
 */
package rsbudget.data.dto;

/**
 * Keeps track of other historical information, such as miles etc.
 * @author ralph
 *
 */
public class HistoricalItemDTO extends RsBudgetDTO {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1535572940840810653L;

	private String name;
	private String unit;
	private boolean showHistory;
	private boolean showChanges;
	private boolean floatValue;
	
	/**
	 * Constructor.
	 */
	public HistoricalItemDTO() {
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
	 * Returns the unit.
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the unit.
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Returns the showHistory.
	 * @return the showHistory
	 */
	public boolean isShowHistory() {
		return showHistory;
	}

	/**
	 * Sets the showHistory.
	 * @param showHistory the showHistory to set
	 */
	public void setShowHistory(boolean showHistory) {
		this.showHistory = showHistory;
	}

	/**
	 * Returns the showChanges.
	 * @return the showChanges
	 */
	public boolean isShowChanges() {
		return showChanges;
	}

	/**
	 * Sets the showChanges.
	 * @param showChanges the showChanges to set
	 */
	public void setShowChanges(boolean showChanges) {
		this.showChanges = showChanges;
	}

	/**
	 * Returns the floatValue.
	 * @return the floatValue
	 */
	public boolean isFloatValue() {
		return floatValue;
	}

	/**
	 * Sets the floatValue.
	 * @param floatValue the floatValue to set
	 */
	public void setFloatValue(boolean floatValue) {
		this.floatValue = floatValue;
	}

	
}
