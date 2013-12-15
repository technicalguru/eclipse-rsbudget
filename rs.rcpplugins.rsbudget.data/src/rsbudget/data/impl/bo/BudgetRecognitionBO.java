/**
 * 
 */
package rsbudget.data.impl.bo;

import java.util.regex.Pattern;

import rsbudget.data.api.bo.BudgetRecognition;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.dto.BudgetRecognitionDTO;

/**
 * Budget Recognition BO wrapper
 * @author ralph
 *
 */
public class BudgetRecognitionBO extends AbstractRsBudgetDbBO<BudgetRecognitionDTO> implements BudgetRecognition {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1L;

	/** The pattern cached */
	private Pattern pattern = null;
	
	/**
	 * Constructor with empty DTO.
	 */
	public BudgetRecognitionBO() {
		this(new BudgetRecognitionDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public BudgetRecognitionBO(BudgetRecognitionDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PeriodicalBudget getBudget() {
		return (PeriodicalBudget)getBusinessObject(getTransferObject().getBudget());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBudget(PeriodicalBudget budget) {
		PeriodicalBudget oldValue = getBudget();
		getTransferObject().setBudget(((PeriodicalBudgetBO)budget).getTransferObject());
		firePropertyChange(PROPERTY_BUDGET, oldValue, budget);
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
	public int compareTo(BudgetRecognition o) {
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
