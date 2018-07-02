/**
 * 
 */
package rsbudget.data.dto;

import rsbudget.data.util.PlannedPeriod;

/**
 * A transaction that is planned to occur periodically.
 * A planned periodical transaction can have assigned a periodical budget, but doesnt have to.
 * @author ralph
 *
 */
public class PeriodicalTransactionDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 5470972040797923179L;

	private AccountDTO account;
	private float amount;
	private String name;
	private CategoryDTO category;
	private String annotation;
	private PeriodicalBudgetDTO budget;
	private PlannedPeriod plannedPeriod;
	/** The number of the month within the planned period this transaction shall occur */
	private int monthSequenceNumber = 1;
	private int displayOrder;
	private String matchString;
	
	/**
	 * Constructor.
	 */
	public PeriodicalTransactionDTO() {
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
	 * Returns the account.
	 * @return the account
	 */
	public AccountDTO getAccount() {
		return account;
	}

	/**
	 * Sets the account.
	 * @param account the account to set
	 */
	public void setAccount(AccountDTO account) {
		this.account = account;
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
	 * Returns the annotation.
	 * @return the annotation
	 */
	public String getAnnotation() {
		return annotation;
	}

	/**
	 * Sets the annotation.
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/**
	 * Returns the budget.
	 * @return the budget
	 */
	public PeriodicalBudgetDTO getBudget() {
		return budget;
	}

	/**
	 * Sets the budget.
	 * @param budget the budget to set
	 */
	public void setBudget(PeriodicalBudgetDTO budget) {
		this.budget = budget;
	}

	/**
	 * Returns the plannedPeriod.
	 * @return the plannedPeriod
	 */
	public PlannedPeriod getPlannedPeriod() {
		return plannedPeriod;
	}

	/**
	 * Sets the plannedPeriod.
	 * @param plannedPeriod the plannedPeriod to set
	 */
	public void setPlannedPeriod(PlannedPeriod plannedPeriod) {
		this.plannedPeriod = plannedPeriod;
	}

	/**
	 * Returns the monthSequenceNumber.
	 * @return the monthSequenceNumber
	 */
	public int getMonthSequenceNumber() {
		return monthSequenceNumber;
	}

	/**
	 * Sets the monthSequenceNumber.
	 * @param monthSequenceNumber the monthSequenceNumber to set
	 */
	public void setMonthSequenceNumber(int monthSequenceNumber) {
		this.monthSequenceNumber = monthSequenceNumber;
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
	 * Returns the matchString.
	 * @return the matchString
	 */
	public String getMatchString() {
		return matchString;
	}

	/**
	 * Sets the matchString.
	 * @param matchString the matchString to set
	 */
	public void setMatchString(String matchString) {
		this.matchString = matchString;
	}
	
	
}
