/**
 * 
 */
package rsbudget.data.api.dao;

import rsbudget.data.api.bo.BankInfo;
import rsbudget.data.type.BankInfoId;

/**
 * DAO for bank information.
 * @author ralph
 *
 */
public interface BankInfoDAO extends RsBudgetDAO<BankInfoId, BankInfo>{

	/**
	 * Find an information by the BLZ.
	 * @param blz the BLZ
	 * @return the bank info ro null
	 */
	public BankInfo findBy(String blz);
}
