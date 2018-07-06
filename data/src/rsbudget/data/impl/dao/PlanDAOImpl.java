/**
 * 
 */
package rsbudget.data.impl.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import rs.baselib.util.RsMonth;
import rs.data.util.CID;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.dao.PlanDAO;
import rsbudget.data.dto.PlanDTO;
import rsbudget.data.impl.bo.PlanBO;

/**
 * DAO for plans.
 * @author ralph
 *
 */
public class PlanDAOImpl extends AbstractRsBudgetDbDAO<PlanDTO, PlanBO, Plan> implements PlanDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Plan findCurrent() {
		return findBy(new RsMonth());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Plan findBy(RsMonth month) {
		Criterion criterion = Restrictions.eq("month", month);
		Criteria criteria = buildCriteria(criterion);
		return findSingleByCriteria(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Plan findFirst() {
		Criteria criteria = getDefaultCriteria();
		criteria.addOrder(Order.asc("month"));
		return findSingleByCriteria(criteria);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Plan getCached(CID cid) {
		Plan rc = super.getCached(cid);
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Plan> findLatest(RsMonth from) {
		Criterion criterion = Restrictions.ge("month", from);
		Criteria criteria = buildCriteria(criterion);
		criteria.addOrder(Order.desc("month"));
		return findByCriteria(criteria);
	}

	
}
