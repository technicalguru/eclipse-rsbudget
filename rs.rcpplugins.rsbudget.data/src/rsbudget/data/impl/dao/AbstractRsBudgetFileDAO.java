/**
 * 
 */
package rsbudget.data.impl.dao;

import rs.data.impl.bo.AbstractGeneralBO;
import rs.data.impl.dao.AbstractGeneralDAO;
import rs.data.type.UrlKey;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.api.dao.RsBudgetDAO;

/**
 * Implements the common DB DAO functions for the app.
 * @author ralph
 *
 */
public abstract class AbstractRsBudgetFileDAO<K extends UrlKey<?>, B extends RsBudgetBO<K>> extends AbstractGeneralDAO<K, AbstractGeneralBO<K>, B> implements RsBudgetDAO<K, B> {

	/**
	 * Constructor.
	 * @param factory
	 */
	public AbstractRsBudgetFileDAO() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsBudgetDaoFactory getFactory() {
		return (RsBudgetDaoFactory)super.getFactory();
	}

	
}
