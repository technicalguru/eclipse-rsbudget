/**
 * 
 */
package rsbudget.data.api.bo;

import rsbudget.data.util.PlannedPeriod;

/**
 * A periodical budget, usually a month.
 * @author ralph
 *
 */
public interface PeriodicalBudget extends RsBudgetBO<Long>, MonetaryValue {

	public static final String PROPERTY_NAME                  = "name";
	public static final String PROPERTY_CATEGORY              = "category";
	public static final String PROPERTY_AMOUNT                = "amount";
	public static final String PROPERTY_PLANNED_PERIOD        = "plannedPeriod";
	public static final String PROPERTY_DISPLAY_ORDER         = "displayOrder";
	public static final String PROPERTY_MONTH_SEQUENCE_NUMBER = "monthSequenceNumber";
	public static final String PROPERTY_MATCH_RULE            = "matchRule";

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
	 * Returns the plannedPeriod.
	 * @return the plannedPeriod
	 */
	public PlannedPeriod getPlannedPeriod();

	/**
	 * Sets the plannedPeriod.
	 * @param plannedPeriod the plannedPeriod to set
	 */
	public void setPlannedPeriod(PlannedPeriod plannedPeriod);

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
	 * Returns the monthSequenceNumber.
	 * @return the monthSequenceNumber
	 */
	public int getMonthSequenceNumber();

	/**
	 * Sets the monthSequenceNumber.
	 * @param monthSequenceNumber the monthSequenceNumber to set
	 */
	public void setMonthSequenceNumber(int monthSequenceNumber);
	
	/**
	 * Returns the amount that was planned on this budget.
	 * The return value will be either the budget itself or the total of periodical transactions
	 * if they exceed this budget.
	 * @return amount planned
	 */
	public float getPlanned();

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
