/**
 * 
 */
package rsbudget.data.impl.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import rsbudget.data.api.bo.Account;
import rsbudget.data.api.dao.AccountDAO;
import rsbudget.data.dto.AccountDTO;
import rsbudget.data.impl.bo.AccountBO;

/**
 * DAO for account.
 * @author ralph
 *
 */
public class AccountDAOImpl extends AbstractRsBudgetDbDAO<AccountDTO, AccountBO, Account> implements AccountDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account findDefault() {
		Criteria crit = buildCriteria(Restrictions.eq("planningRelevant", Boolean.TRUE), Restrictions.eq("active", Boolean.TRUE));
		return findSingleByCriteria(crit);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Account> findRelevant() {
		Criteria crit = buildCriteria(Restrictions.eq("planningRelevant", Boolean.TRUE), Restrictions.eq("active", Boolean.TRUE));
		return findByCriteria(crit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account findByName(String name) {
		Criteria crit = buildCriteria(Restrictions.eq("name", name));
		return findSingleByCriteria(crit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Criterion[] getDefaultCriterions() {
		return new Criterion[] {
				Restrictions.eq("active", Boolean.TRUE)
		};
	}
	
	
}
