/**
 * 
 */
package rsbudget.data.api.bo;

import java.util.Set;

/**
 * A budget for a specific time period, usually a specific month in a specific year.
 * @author ralph
 *
 */
public interface Budget extends RsBudgetBO<Long>, MonetaryValue {

	public static final String PROPERTY_NAME                 = "name";
	public static final String PROPERTY_CATEGORY             = "category";
	public static final String PROPERTY_PLAN                 = "plan";
	public static final String PROPERTY_TRANSACTIONS         = "transactions";
	public static final String PROPERTY_PLANNED_TRANSACTIONS = "plannedTransactions";
	public static final String PROPERTY_AMOUNT               = "amount";
	public static final String PROPERTY_DISPLAY_ORDER        = "displayOrder";
	public static final String PROPERTY_MATCH_RULE           = "matchRule";
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName();

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name);

	/**
	 * Returns the category.
	 * @return the category
	 */
	public Category getCategory();

	/**
	 * Sets the category.
	 * @param category the category to set
	 */
	public void setCategory(Category category);

	/**
	 * Returns the plan.
	 * @return the plan
	 */
	public Plan getPlan();

	/**
	 * Sets the plan.
	 * @param plan the plan to set
	 */
	public void setPlan(Plan plan);

	/**
	 * Returns the transactions.
	 * @return the transactions
	 */
	public Set<Transaction> getTransactions();

	/**
	 * Adds an transaction to the list of associated transactions.
	 * @param transaction the new transaction
	 * @return true when the transaction could be added
	 */
	public boolean addTransaction(Transaction transaction);

	/**
	 * Removes an transaction from the list of associated transactions.
	 * @param transaction the transaction to be removed
	 * @return true when the transaction could be removed
	 */
	public boolean removeTransaction(Transaction transaction);
	
	/**
	 * Returns the plannedTransactions.
	 * @return the plannedTransactions
	 */
	public Set<PlannedTransaction> getPlannedTransactions();


	/**
	 * Adds a planned transaction to the list of associated planned transactions.
	 * @param plannedTransaction the new planned transaction
	 * @return true when the planned transaction could be added
	 */
	public boolean addPlannedTransaction(PlannedTransaction plannedTransaction);

	/**
	 * Removes a planned transaction from the list of associated planned transactions.
	 * @param plannedTransaction the planned transaction to be removed
	 * @return true when the planned transaction could be removed
	 */
	public boolean removePlannedTransaction(PlannedTransaction plannedTransaction);
	
	/**
	 * Returns the amount still available.
	 * @return amoujt still available
	 */
	public float getAvailable();
	
	/**
	 * Returns the amount still available.
	 * @return amoujt still available
	 */
	public float getActual();
	
	/**
	 * Returns the amount that was planned on this budget.
	 * @return amount planned
	 */
	public float getPlanned();

	/**
	 * Returns the displayOrder.
	 * @return the displayOrder
	 */
	public int getDisplayOrder();

	/**
	 * Sets the displayOrder.
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder);
	
	/**
	 * Returns the matching rule.
	 * @return the matching rule
	 */
	public String getMatchRule();

	/**
	 * Sets the matching rule.
	 * @param matchRule the matching rule to set
	 */
	public void setMatchRule(String matchRule);

}
