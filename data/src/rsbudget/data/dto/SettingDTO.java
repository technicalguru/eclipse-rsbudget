/**
 * 
 */
package rsbudget.data.dto;


/**
 * A user setting.
 * @author ralph
 *
 */
public class SettingDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3749400798779807199L;

	private String key;
	private String value;
	
	/**
	 * Constructor.
	 */
	public SettingDTO() {
	}

	/**
	 * Returns the key.
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key.
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns the value.
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	
}
