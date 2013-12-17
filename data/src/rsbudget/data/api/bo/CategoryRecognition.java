/**
 * 
 */
package rsbudget.data.api.bo;


/**
 * A regular expression for recognition of categories.
 * @author ralph
 *
 */
public interface CategoryRecognition extends RsBudgetBO<Long>, Comparable<CategoryRecognition> {

	public static final String PROPERTY_EXPRESSION             = "expression";
	public static final String PROPERTY_CATEGORY               = "category";
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
