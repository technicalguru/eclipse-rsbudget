/**
 * 
 */
package rsbudget.data.api.bo;

import rsbudget.data.util.PlannedPeriod;

/**
 * A transaction that is planned to occur periodically.
 * A planned periodical transaction can have assigned a periodical budget, but doesnt have to.
 * @author ralph
 *
 */
public interface PeriodicalTransaction extends RsBudgetBO<Long>, MonetaryValue {

	public static final String PROPERTY_NAME                  = "name";
	public static final String PROPERTY_ACCOUNT               = "account";
	public static final String PROPERTY_CATEGORY              = "category";
	public static final String PROPERTY_AMOUNT                = "amount";
	public static final String PROPERTY_ANNOTATION            = "annotation";
	public static final String PROPERTY_BUDGET                = "budget";
	public static final String PROPERTY_PLANNED_PERIOD        = "plannedPeriod";
	public static final String PROPERTY_MONTH_SEQUENCE_NUMBER = "monthSequenceNumber";
	public static final String PROPERTY_DISPLAY_ORDER         = "displayOrder";
	public static final String PROPERTY_MATCH_STRING          = "matchString";

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
	 * Returns the account.
	 * @return the account
	 */
	public Account getAccount();

	/**
	 * Sets the account.
	 * @param account the account to set
	 */
	public void setAccount(Account account);

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
	 * Returns the annotation.
	 * @return the annotation
	 */
	public String getAnnotation();

	/**
	 * Sets the annotation.
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(String annotation);

	/**
	 * Returns the budget.
	 * @return the budget
	 */
	public PeriodicalBudget getBudget();

	/**
	 * Sets the budget.
	 * @param budget the budget to set
	 */
	public void setBudget(PeriodicalBudget budget);

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
	 * Returns the matching string.
	 * @return the mtching string
	 */
	public String getMatchString();

	/**
	 * Sets the matching string.
	 * @param matchString the matching string to set
	 */
	public void setMatchString(String matchString);


}
