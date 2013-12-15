/**
 * 
 */
package rsbudget.data.dto;


/**
 * A recognition record for categories.
 * @author ralph
 *
 */
public class CategoryRecognitionDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	private String expression;
	private CategoryDTO category;
	private int rank;
	
	/**
	 * Constructor.
	 */
	public CategoryRecognitionDTO() {
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
