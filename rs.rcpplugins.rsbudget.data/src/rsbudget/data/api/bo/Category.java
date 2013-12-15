/**
 * 
 */
package rsbudget.data.api.bo;



/**
 * Category of a transaction.
 * @author ralph
 *
 */
public interface Category extends RsBudgetBO<Long> {

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_DEFAULT = "default";

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
	 * Returns the default.
	 * @return the default
	 */
	public boolean isDefault();

	/**
	 * Sets the default.
	 * @param defaultCategory the default to set
	 */
	public void setDefault(boolean defaultCategory);

}
