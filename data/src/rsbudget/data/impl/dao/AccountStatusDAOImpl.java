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
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.AccountStatus;
import rsbudget.data.api.dao.AccountStatusDAO;
import rsbudget.data.dto.AccountStatusDTO;
import rsbudget.data.impl.bo.AccountStatusBO;

/**
 * DAO for account status.
 * @author ralph
 *
 */
public class AccountStatusDAOImpl extends AbstractRsBudgetDbDAO<AccountStatusDTO, AccountStatusBO, AccountStatus> implements AccountStatusDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountStatus findLatestBy(Account account, RsDate timestamp) {
		Criterion crit1 = Restrictions.eq("account.id", account.getId());
		List<AccountStatus> l = findByCriteria(buildCriteria(crit1));
		AccountStatus rc = null;
		for (AccountStatus status : l) {
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
	public AccountStatus findNearest(Account account, RsDate timestamp, long maxDiffMilliseconds) {
		Criterion crit1 = Restrictions.eq("account.id", account.getId());
		List<AccountStatus> l = findByCriteria(buildCriteria(crit1));
		AccountStatus rc = null;
		long min = timestamp.getTimeInMillis()-maxDiffMilliseconds;
		long max = timestamp.getTimeInMillis()+maxDiffMilliseconds;
		for (AccountStatus status : l) {
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
	public AccountStatus findFirst() {
		Criteria criteria = getDefaultCriteria();
		criteria.addOrder(Order.asc("timestamp"));
		return findSingleByCriteria(criteria);
	}


}
