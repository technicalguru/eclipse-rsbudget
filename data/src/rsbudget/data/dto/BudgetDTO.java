/**
 * 
 */
package rsbudget.data.dto;

import java.util.Set;

/**
 * A budget for a specific time period, usually a specific month in a specific year.
 * @author ralph
 *
 */
public class BudgetDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4976067960800877024L;

	private String name;
	private CategoryDTO category;
	private float amount;
	private PlanDTO plan;
	private int displayOrder;
	private Set<TransactionDTO> transactions;
	private Set<PlannedTransactionDTO> plannedTransactions;
	private String matchRule;

	/**
	 * Constructor.
	 */
	public BudgetDTO() {
	}

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the category.
	 * @return the category
	 */
	public CategoryDTO getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category the category to set
	 */
	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	/**
	 * Returns the amount.
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * Sets the amount.
	 * @param amount the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * Returns the plan.
	 * @return the plan
	 */
	public PlanDTO getPlan() {
		return plan;
	}

	/**
	 * Sets the plan.
	 * @param plan the plan to set
	 */
	public void setPlan(PlanDTO plan) {
		this.plan = plan;
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
	public void setPlannedTransactions(Set<PlannedTransactionDTO> plannedTransactions) {
		this.plannedTransactions = plannedTransactions;
	}

	/**
	 * Returns the displayOrder.
	 * @return the displayOrder
	 */
	public int getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * Sets the displayOrder.
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * Returns the matchRule.
	 * @return the matchRule
	 */
	public String getMatchRule() {
		return matchRule;
	}

	/**
	 * Sets the matchRule.
	 * @param matchRule the matchRule to set
	 */
	public void setMatchRule(String matchRule) {
		this.matchRule = matchRule;
	}

}
