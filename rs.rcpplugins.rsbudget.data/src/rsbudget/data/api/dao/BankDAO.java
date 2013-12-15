/**
 * 
 */
package rsbudget.data.api.dao;

import rsbudget.data.api.bo.Bank;
import rsbudget.data.api.bo.BankInfo;

/**
 * DAO for bank.
 * @author ralph
 *
 */
public interface BankDAO extends RsBudgetDAO<Long, Bank> {

	/** Find the given Bank by BLZ */
	public Bank findByBlz(String blz);
	
	/** Create an record or return the record for that bank Info */
	public Bank newInstance(BankInfo bankInfo);
}
