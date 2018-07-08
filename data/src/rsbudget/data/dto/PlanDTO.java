/**
 * 
 */
package rsbudget.data.dto;

import java.math.BigDecimal;
import java.util.Set;

import rs.baselib.util.RsMonth;

/**
 * Existence of such a DTO indicates that a plan exists for this DTO.
 * @author ralph
 *
 */
public class PlanDTO extends RsBudgetDTO {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 1134792478907420028L;
	
	private RsMonth month;
	private BigDecimal balanceStart;
	private BigDecimal balanceEnd;
	private Set<BudgetDTO> budgets;
	private Set<PlannedTransactionDTO> plannedTransactions;
	private Set<TransactionDTO> transactions;
	private String comment;
	
	/**
	 * Constructor.
	 */
	public PlanDTO() {
	}

	/**
	 * Returns the month.
	 * @return the month
	 */
	public RsMonth getMonth() {
		return month;
	}

	/**
	 * Sets the month.
	 * @param month the month to set
	 */
	public void setMonth(RsMonth month) {
		this.month = month;
	}

	/**
	 * Returns the balanceStart.
	 * @return the balanceStart
	 */
	public BigDecimal getBalanceStart() {
		return balanceStart;
	}

	/**
	 * Sets the balanceStart.
	 * @param balanceStart the balanceStart to set
	 */
	public void setBalanceStart(BigDecimal balanceStart) {
		this.balanceStart = balanceStart;
	}

	/**
	 * Returns the balanceEnd.
	 * @return the balanceEnd
	 */
	public BigDecimal getBalanceEnd() {
		return balanceEnd;
	}

	/**
	 * Sets the balanceEnd.
	 * @param balanceEnd the balanceEnd to set
	 */
	public void setBalanceEnd(BigDecimal balanceEnd) {
		this.balanceEnd = balanceEnd;
	}

	/**
	 * Returns the budgets.
	 * @return the budgets
	 */
	public Set<BudgetDTO> getBudgets() {
		return budgets;
	}

	/**
	 * Sets the budgets.
	 * @param budgets the budgets to set
	 */
	public void setBudgets(Set<BudgetDTO> budgets) {
		this.budgets = budgets;
	}

	/**
	 * Returns the plannedTransactions.
	 * @return the plannedTransactions
	 */
	public Set<PlannedTransactionDTO> getPlannedTransactions() {
		return plannedTransactions;
	}

	/**
	 * Sets the plannedTransactions.
	 * @param plannedTransactions the plannedTransactions to set
	 */
	public void setPlannedTransactions(
			Set<PlannedTransactionDTO> plannedTransactions) {
		this.plannedTransactions = plannedTransactions;
	}

	/**
	 * Returns the transactions.
	 * @return the transactions
	 */
	public Set<TransactionDTO> getTransactions() {
		return transactions;
	}

	/**
	 * Sets the transactions.
	 * @param transactions the transactions to set
	 */
	public void setTransactions(Set<TransactionDTO> transactions) {
		this.transactions = transactions;
	}

	/**
	 * Returns the comment.
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the comment.
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
