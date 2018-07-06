/**
 * 
 */
package rsbudget.data.api.dao;

import java.util.List;

import rs.baselib.util.RsMonth;
import rsbudget.data.api.bo.Plan;

/**
 * DAO for plans.
 * @author ralph
 *
 */
public interface PlanDAO extends RsBudgetDAO<Long, Plan> {

	/**
	 * Find the plan of the current month.
	 * @return plan or null if not found
	 */
	public Plan findCurrent();
	
	/**
	 * Find the plan for the given month.
	 * @param month month to be found
	 * @return plan or null if not found
	 */
	public Plan findBy(RsMonth month);
	
	/**
	 * Find the first plan (lowest available month).
	 * @return plan or null if not found
	 */
	public Plan findFirst();
	
	/**
	 * Find the latest plans.
	 * @param from - earliest plan to retrieve
	 * @return plans found
	 */
	public List<Plan> findLatest(RsMonth from);
}
