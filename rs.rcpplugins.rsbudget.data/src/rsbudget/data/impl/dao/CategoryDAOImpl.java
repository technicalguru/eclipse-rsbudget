/**
 * 
 */
package rsbudget.data.impl.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import rsbudget.data.api.bo.Category;
import rsbudget.data.api.dao.CategoryDAO;
import rsbudget.data.dto.CategoryDTO;
import rsbudget.data.impl.bo.CategoryBO;

/**
 * DAO for transaction categories.
 * @author ralph
 *
 */
public class CategoryDAOImpl extends AbstractRsBudgetDbDAO<CategoryDTO, CategoryBO, Category> implements CategoryDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Category findDefault() {
		Criteria crit = buildCriteria(Restrictions.eq("default", Boolean.TRUE));
		return findSingleByCriteria(crit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Category findBy(String name) {
		Criteria crit = buildCriteria(Restrictions.eq("name", name));
		return findSingleByCriteria(crit);
	}

	
}
