/**
 * 
 */
package rsbudget.data.impl.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import rs.baselib.util.RsDate;
import rsbudget.data.api.bo.HistoricalItem;
import rsbudget.data.api.bo.HistoricalItemStatus;
import rsbudget.data.api.dao.HistoricalItemStatusDAO;
import rsbudget.data.dto.HistoricalItemStatusDTO;
import rsbudget.data.impl.bo.HistoricalItemStatusBO;

/**
 * DAO for item status.
 * @author ralph
 *
 */
public class HistoricalItemStatusDAOImpl extends AbstractRsBudgetDbDAO<HistoricalItemStatusDTO, HistoricalItemStatusBO, HistoricalItemStatus> implements HistoricalItemStatusDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HistoricalItemStatus findLatestBy(HistoricalItem item, RsDate timestamp) {
		Criterion crit1 = Restrictions.eq("item.id", item.getId());
		Criterion crit2 = Restrictions.le("timestamp", timestamp);
		Criteria criteria = buildCriteria(crit1, crit2);
		criteria.addOrder(Order.desc("timestamp"));
		return findSingleByCriteria(criteria);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HistoricalItemStatus findNearest(HistoricalItem item, RsDate timestamp, long maxDiffMilliseconds) {
		Criterion crit1 = Restrictions.eq("item.id", item.getId());
		Criterion crit2 = Restrictions.le("timestamp", new RsDate(timestamp.getTimeInMillis()+maxDiffMilliseconds));
		Criterion crit3 = Restrictions.ge("timestamp", new RsDate(timestamp.getTimeInMillis()-maxDiffMilliseconds));
		List<HistoricalItemStatus> l = findByCriteria(buildCriteria(crit1, crit2, crit3));
		HistoricalItemStatus rc = null;
		for (HistoricalItemStatus status : l) {
			if (rc == null) rc = status;
			else {
				long diffStatus = Math.abs(timestamp.getTimeInMillis()-status.getTimestamp().getTimeInMillis());
				long diffRc     = Math.abs(timestamp.getTimeInMillis()-rc.getTimestamp().getTimeInMillis());
				if (diffStatus < diffRc) rc = status;
			}
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HistoricalItemStatus findFirst() {
		Criteria criteria = getDefaultCriteria();
		criteria.addOrder(Order.asc("timestamp"));
		return findSingleByCriteria(criteria);
	}

	
}
