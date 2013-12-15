/**
 * 
 */
package rsbudget.data.impl.dao;

import java.net.MalformedURLException;
import java.net.URL;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import rs.baselib.util.CommonUtils;
import rsbudget.data.api.bo.Bank;
import rsbudget.data.api.bo.BankInfo;
import rsbudget.data.api.dao.BankDAO;
import rsbudget.data.dto.BankDTO;
import rsbudget.data.impl.bo.BankBO;

/**
 * DAO for bank.
 * @author ralph
 *
 */
public class BankDAOImpl extends AbstractRsBudgetDbDAO<BankDTO, BankBO, Bank> implements BankDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bank findByBlz(String blz) {
		Criteria crit = buildCriteria(Restrictions.eq("blz", blz));
		return findSingleByCriteria(crit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bank newInstance(BankInfo bankInfo) {
		Bank rc = findByBlz(bankInfo.getBlz());
		if (rc == null) {
			rc = newInstance();
			rc.setBlz(bankInfo.getBlz());
			rc.setName(bankInfo.getName());
			String url = bankInfo.getUrl();
			if (!CommonUtils.isEmpty(url)) try {
				rc.setUrl(new URL(url));
			} catch (MalformedURLException e) {}
		}
		return rc;
	}


}
