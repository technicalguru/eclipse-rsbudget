/**
 * 
 */
package rsbudget.data.dto;

import rsbudget.data.util.PlannedPeriod;

/**
 * A periodical budget, usually a month.
 * @author ralph
 *
 */
public class PeriodicalBudgetDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6735049118115951171L;

	private String name;
	private CategoryDTO category;
	private float amount;
	private PlannedPeriod plannedPeriod;
	private int displayOrder;
	private int monthSequenceNumber = 1;
	private String matchRule;

	/**
	 * Constructor.
	 */
	public PeriodicalBudgetDTO() {
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
