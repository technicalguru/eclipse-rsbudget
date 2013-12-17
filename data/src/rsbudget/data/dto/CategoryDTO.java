/**
 * 
 */
package rsbudget.data.dto;




/**
 * Category of a transaction.
 * @author ralph
 *
 */
public class CategoryDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3609791700642145819L;

	private String name;
	private boolean defaultCategory;
	
	/**
	 * Constructor.
	 */
	public CategoryDTO() {
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
	 * Returns the default.
	 * @return the default
	 */
	public boolean isDefault() {
		return defaultCategory;
	}

	/**
	 * Sets the default.
	 * @param defaultCategory the default to set
	 */
	public void setDefault(boolean defaultCategory) {
		this.defaultCategory = defaultCategory;
	}

	
}
