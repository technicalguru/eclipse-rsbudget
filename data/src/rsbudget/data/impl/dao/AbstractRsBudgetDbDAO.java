/**
 * 
 */
package rsbudget.data.impl.dao;

import rs.data.hibernate.dao.AbstractHibernateLongDAO;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.api.dao.RsBudgetDAO;
import rsbudget.data.dto.RsBudgetDTO;
import rsbudget.data.impl.bo.AbstractRsBudgetDbBO;

/**
 * Implements the common DB DAO functions for the app.
 * @author ralph
 *
 */
public abstract class AbstractRsBudgetDbDAO<T extends RsBudgetDTO, B extends AbstractRsBudgetDbBO<T>, C extends RsBudgetBO<Long>> extends AbstractHibernateLongDAO<T, B, C> implements RsBudgetDAO<Long, C> {

	/**
	 * Constructor.
	 * @param factory
	 */
	public AbstractRsBudgetDbDAO() {
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
