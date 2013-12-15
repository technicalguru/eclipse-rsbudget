/**
 * 
 */
package rsbudget.data.impl.bo;

import java.util.regex.Pattern;

import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.CategoryRecognition;
import rsbudget.data.dto.CategoryRecognitionDTO;

/**
 * Category Recognition BO wrapper
 * @author ralph
 *
 */
public class CategoryRecognitionBO extends AbstractRsBudgetDbBO<CategoryRecognitionDTO> implements CategoryRecognition {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/** The pattern cached */
	private Pattern pattern = null;
	
	/**
	 * Constructor with empty DTO.
	 */
	public CategoryRecognitionBO() {
		this(new CategoryRecognitionDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public CategoryRecognitionBO(CategoryRecognitionDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Category getCategory() {
		return (Category)getBusinessObject(getTransferObject().getCategory());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCategory(Category category) {
		Category oldValue = getCategory();
		getTransferObject().setCategory(((CategoryBO)category).getTransferObject());
		firePropertyChange(PROPERTY_CATEGORY, oldValue, category);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getRank() {
		return getTransferObject().getRank();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRank(int rank) {
		int oldValue = getRank();
		getTransferObject().setRank(rank);
		firePropertyChange(PROPERTY_RANK, oldValue, rank);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getExpression() {
		return getTransferObject().getExpression();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setExpression(String expression) {
		String oldValue = getExpression();
		getTransferObject().setExpression(expression);
		this.pattern = null;
		firePropertyChange(PROPERTY_EXPRESSION, oldValue, expression);
	}

	/**
	 * {@inheritDoc}
	 * <p>Compares with rank</p>
	 */
	@Override
	public int compareTo(CategoryRecognition o) {
		return getRank() - o.getRank();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean matches(String s) {
		String expression = getExpression();
		if (expression.startsWith("/")) {
			if (pattern != null) {
				pattern = Pattern.compile(".*"+expression.substring(1)+".*", Pattern.CASE_INSENSITIVE);
			}
			return pattern.matcher(s).matches();
		} else {
			return s.toLowerCase().indexOf(expression.toLowerCase()) >= 0;
		}
	}

}
