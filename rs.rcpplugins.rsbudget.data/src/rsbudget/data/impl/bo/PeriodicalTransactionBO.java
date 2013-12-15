/**
 * 
 */
package rsbudget.data.impl.bo;

import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.dto.PeriodicalTransactionDTO;
import rsbudget.data.util.PlannedPeriod;

/**
 * Periodical budget BO wrapper.
 * @author ralph
 *
 */
public class PeriodicalTransactionBO extends AbstractRsBudgetDbBO<PeriodicalTransactionDTO> implements PeriodicalTransaction {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -5492416785237519778L;

	/**
	 * Constructor with empty DTO.
	 */
	public PeriodicalTransactionBO() {
		this(new PeriodicalTransactionDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public PeriodicalTransactionBO(PeriodicalTransactionDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getAmount() {
		return getTransferObject().getAmount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAmount(float amount) {
		float oldValue = getAmount();
		getTransferObject().setAmount(amount);
		firePropertyChange(PROPERTY_AMOUNT, oldValue, amount);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return getTransferObject().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		String oldValue = getName();
		getTransferObject().setName(name);
		firePropertyChange(PROPERTY_NAME, oldValue, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account getAccount() {
		return (Account)getBusinessObject(getTransferObject().getAccount());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAccount(Account account) {
		Account oldValue = getAccount();
		getTransferObject().setAccount(((AccountBO)account).getTransferObject());
		firePropertyChange(PROPERTY_ACCOUNT, oldValue, account);
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
	public String getAnnotation() {
		return getTransferObject().getAnnotation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAnnotation(String annotation) {
		String oldValue = getAnnotation();
		getTransferObject().setAnnotation(annotation);
		firePropertyChange(PROPERTY_ANNOTATION, oldValue, annotation);
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
		if (budget != null) {
			getTransferObject().setBudget(((PeriodicalBudgetBO)budget).getTransferObject());
		} else {
			getTransferObject().setBudget(null);
		}
		firePropertyChange(PROPERTY_BUDGET, oldValue, budget);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlannedPeriod getPlannedPeriod() {
		return getTransferObject().getPlannedPeriod();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlannedPeriod(PlannedPeriod plannedPeriod) {
		PlannedPeriod oldValue = getPlannedPeriod();
		getTransferObject().setPlannedPeriod(plannedPeriod);
		if (getMonthSequenceNumber() > plannedPeriod.getMaxSequence()) setMonthSequenceNumber(plannedPeriod.getMaxSequence());
		firePropertyChange(PROPERTY_PLANNED_PERIOD, oldValue, plannedPeriod);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMonthSequenceNumber() {
		return getTransferObject().getMonthSequenceNumber();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMonthSequenceNumber(int monthSequenceNumber) {
		int oldValue = getMonthSequenceNumber();
		getTransferObject().setMonthSequenceNumber(monthSequenceNumber);
		firePropertyChange(PROPERTY_MONTH_SEQUENCE_NUMBER, oldValue, monthSequenceNumber);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDisplayOrder() {
		return getTransferObject().getDisplayOrder();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDisplayOrder(int displayOrder) {
		int oldValue = getDisplayOrder();
		getTransferObject().setDisplayOrder(displayOrder);
		firePropertyChange(PROPERTY_DISPLAY_ORDER, oldValue, displayOrder);
	}

}
