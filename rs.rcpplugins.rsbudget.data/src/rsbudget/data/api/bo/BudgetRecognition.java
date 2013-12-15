/**
 * 
 */
package rsbudget.data.api.bo;


/**
 * A regular expression for recognition of budgets.
 * @author ralph
 *
 */
public interface BudgetRecognition extends RsBudgetBO<Long>, Comparable<BudgetRecognition> {

	public static final String PROPERTY_EXPRESSION             = "expression";
	public static final String PROPERTY_BUDGET                 = "budget";
	public static final String PROPERTY_RANK                   = "rank";

	/**
	 * Returns the expression.
	 * @return the expression
	 */
	public String getExpression();

	/**
	 * Sets the expression.
	 * @param expression the expression to set
	 */
	public void setExpression(String expression);

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
	 * Returns the rank.
	 * @return the rank
	 */
	public int getRank();

	/**
	 * Sets the rank.
	 * @param rank the rank to set
	 */
	public void setRank(int rank);
	
	/**
	 * Matches the expression against the string.
	 * @param s string to be matched
	 * @return true when s matches the expression
	 */
	public boolean matches(String s);

}
