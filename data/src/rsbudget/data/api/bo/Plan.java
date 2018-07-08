/**
 * 
 */
package rsbudget.data.api.bo;

import java.math.BigDecimal;
import java.util.Set;

import rs.baselib.util.RsMonth;


/**
 * Category of a transaction.
 * @author ralph
 *
 */
public interface Plan extends RsBudgetBO<Long> {

	public static final String PROPERTY_MONTH                = "month";
	public static final String PROPERTY_BALANCE_START        = "balanceStart";
	public static final String PROPERTY_BALANCE_END          = "balanceEnd";
	public static final String PROPERTY_BUDGETS              = "budgets";
	public static final String PROPERTY_PLANNED_TRANSACTIONS = "plannedTransactions";
	public static final String PROPERTY_TRANSACTIONS         = "transactions";
	public static final String PROPERTY_COMMENT              = "comment";

	/**
	 * Returns the month.
	 * @return the month
	 */
	public RsMonth getMonth();

	/**
	 * Sets the month.
	 * @param month the month to set
	 */
	public void setMonth(RsMonth month);
	
	/**
	 * Returns the balanceStart.
	 * @return the balanceStart
	 */
	public BigDecimal getBalanceStart();

	/**
	 * Sets the balanceStart.
	 * @param balanceStart the balanceStart to set
	 */
	public void setBalanceStart(BigDecimal balanceStart);

	/**
	 * Returns the balanceEnd.
	 * @return the balanceEnd
	 */
	public BigDecimal getBalanceEnd();

	/**
	 * Sets the balanceEnd.
	 * @param balanceEnd the balanceEnd to set
	 */
	public void setBalanceEnd(BigDecimal balanceEnd);

	/**
	 * Returns all associated budgets.
	 * @return budgets for this month
	 */
	public Set<Budget> getBudgets();

	/**
	 * Adds the budget to this plan.
	 * @param budget budget to be added
	 * @return true if budget could be added
	 */
	public boolean addBudget(Budget budget);
	
	/**
	 * Removes the budget from this plan.
	 * @param budget budget to be removed
	 * @return true if budget could be removed
	 */
	public boolean removeBudget(Budget budget);
	
	/**
	 * Returns all associated planned transactions.
	 * @return planned transactions for this month
	 */
	public Set<PlannedTransaction> getPlannedTransactions();
	
	/**
	 * Adds the transaction to this plan.
	 * @param transaction transaction to be added
	 * @return true if transaction could be added
	 */
	public boolean addTransaction(Transaction transaction);
	
	/**
	 * Removes the transaction from this plan.
	 * @param transaction transaction to be removed
	 * @return true if transaction could be removed
	 */
	public boolean removeTransaction(Transaction transaction);
	
	/**
	 * Returns all associated transactions.
	 * @return transactions for this month
	 */
	public Set<Transaction> getTransactions();
	
	
	/**
	 * Adds the planned transaction to this plan.
	 * @param plannedTransaction planned transaction to be added
	 * @return true if planned transaction could be added
	 */
	public boolean addPlannedTransaction(PlannedTransaction plannedTransaction);
	
	/**
	 * Removes the planned transaction from this plan.
	 * @param plannedTransaction planned transaction to be removed
	 * @return true if planned transaction could be removed
	 */
	public boolean removePlannedTransaction(PlannedTransaction plannedTransaction);

	/**
	 * Returns the maximum display order used in transactions.
	 * @return max display order.
	 */
	public int getMaxTxDisplayOrder();
	
	/**
	 * Returns the maximum display order used in budgets.
	 * @return max display order
	 */
	public int getMaxBudgetDisplayOrder();
	
	/**
	 * Returns the mixed set of planned and actual transactions.
	 * No budget will be returned!
	 * @return mixed set
	 */
	public Set<RsBudgetBO<?>> getMixedTransactions();
	
	/**
	 * Returns the comment.
	 * @return the comment
	 */
	public String getComment();

	/**
	 * Sets the comment.
	 * @param comment the comment to set
	 */
	public void setComment(String comment);

}
