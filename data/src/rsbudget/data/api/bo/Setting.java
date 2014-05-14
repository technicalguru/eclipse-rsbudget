/**
 * 
 */
package rsbudget.data.api.bo;



/**
 * A user setting.
 * @author ralph
 *
 */
public interface Setting extends RsBudgetBO<Long> {

	public static final String KEY_APPLICATION_ID        = "application.id";
	public static final String KEY_APPLICATION_SALT      = "application.salt";
	public static final String PROPERTY_KEY              = "key";
	public static final String PROPERTY_VALUE            = "value";
	
	/**
	 * Returns the key.
	 * @return the key
	 */
	public String getKey();

	/**
	 * Sets the key.
	 * @param key the key to set
	 */
	public void setKey(String key);

	/**
	 * Returns the value.
	 * @return the value
	 */
	public String getValue();

	/**
	 * Sets the value.
	 * @param value the value to set
	 */
	public void setValue(String value);

}
