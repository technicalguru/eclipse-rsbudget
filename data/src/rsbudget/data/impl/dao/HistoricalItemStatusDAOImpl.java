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
		List<HistoricalItemStatus> l = findByCriteria(buildCriteria(crit1));
		HistoricalItemStatus rc = null;
		for (HistoricalItemStatus status : l) {
			if (status.getTimestamp().before(timestamp)) {
				if (rc == null) rc = status;
				else {
					if (status.getTimestamp().after(rc.getTimestamp())) rc = status;
				}
			}
		}
		return rc;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HistoricalItemStatus findNearest(HistoricalItem item, RsDate timestamp, long maxDiffMilliseconds) {
		Criterion crit1 = Restrictions.eq("item.id", item.getId());
		List<HistoricalItemStatus> l = findByCriteria(buildCriteria(crit1));
		HistoricalItemStatus rc = null;
		long min = timestamp.getTimeInMillis()-maxDiffMilliseconds;
		long max = timestamp.getTimeInMillis()+maxDiffMilliseconds;
		for (HistoricalItemStatus status : l) {
			long t = status.getTimestamp().getTimeInMillis();
			if ((t >= min) && (t <= max)) {
				if (rc == null) rc = status;
				else {
					long diffStatus = Math.abs(timestamp.getTimeInMillis()-status.getTimestamp().getTimeInMillis());
					long diffRc     = Math.abs(timestamp.getTimeInMillis()-rc.getTimestamp().getTimeInMillis());
					if (diffStatus < diffRc) rc = status;
				}
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
