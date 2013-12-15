/**
 * 
 */
package rsbudget.data.api.dao;

import rs.baselib.util.RsDate;
import rsbudget.data.api.bo.HistoricalItem;
import rsbudget.data.api.bo.HistoricalItemStatus;

/**
 * DAO for account status.
 * @author ralph
 *
 */
public interface HistoricalItemStatusDAO extends RsBudgetDAO<Long, HistoricalItemStatus> {

	/**
	 * Returns the latest status information before a given point in time for an item.
	 * @param item item
	 * @param timestamp timestamp
	 * @return last status before timestamp
	 */
	public HistoricalItemStatus findLatestBy(HistoricalItem item, RsDate timestamp);
	
	/**
	 * Returns the nearest status information around a given point in time for an item.
	 * @param item item
	 * @param timestamp timestamp
	 * @param maxDiffMilliseconds maximum difference from timestamp allowed
	 * @return last status before timestamp
	 */
	public HistoricalItemStatus findNearest(HistoricalItem item, RsDate timestamp, long maxDiffMilliseconds);

	/**
	 * Returns the first status information (lowest month)
	 * @return first status
	 */
	public HistoricalItemStatus findFirst();

}
