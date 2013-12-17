/**
 * 
 */
package rsbudget.data.dto;


/**
 * A recognition record for budgets.
 * @author ralph
 *
 */
public class BudgetRecognitionDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	private String expression;
	private PeriodicalBudgetDTO budget;
	private int rank;
	
	/**
	 * Constructor.
	 */
	public BudgetRecognitionDTO() {
	}

	/**
	 * Returns the expression.
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}
	
	/**
	 * Sets the expression.
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
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
	 * Returns the rank.
	 * @return the rank
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * Sets the rank.
	 * @param rank the rank to set
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

}
