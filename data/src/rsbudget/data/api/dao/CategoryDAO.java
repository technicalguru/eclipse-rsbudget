/**
 * 
 */
package rsbudget.data.api.dao;

import rsbudget.data.api.bo.Category;

/**
 * DAO for transaction categories.
 * @author ralph
 *
 */
public interface CategoryDAO extends RsBudgetDAO<Long, Category> {

	/**
	 * Returns the category used as default for new objects. 
	 * @return default category
	 */
	public Category findDefault();

	/**
	 * Returns the category with given name. 
	 * @return category
	 */
	public Category findBy(String name);
}
