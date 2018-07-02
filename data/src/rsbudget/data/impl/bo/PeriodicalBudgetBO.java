/**
 * 
 */
package rsbudget.data.impl.bo;

import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.dto.PeriodicalBudgetDTO;
import rsbudget.data.util.PlannedPeriod;

/**
 * Periodical budget BO wrapper.
 * @author ralph
 *
 */
public class PeriodicalBudgetBO extends AbstractRsBudgetDbBO<PeriodicalBudgetDTO> implements PeriodicalBudget {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 2475623679280498516L;

	/**
	 * Constructor with empty DTO.
	 */
	public PeriodicalBudgetBO() {
		this(new PeriodicalBudgetDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public PeriodicalBudgetBO(PeriodicalBudgetDTO dto) {
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
		if ((getMonthSequenceNumber() == 0) && (plannedPeriod.getMaxSequence() > 0)) setMonthSequenceNumber(1);
		firePropertyChange(PROPERTY_PLANNED_PERIOD, oldValue, plannedPeriod);
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
	public float getPlanned() {
		float rc = 0;
		beginTx();
		for (PeriodicalTransaction tx : ((RsBudgetDaoFactory)getFactory()).getPeriodicalTransactionDAO().findBy(this)) {
			if (tx.getPlannedPeriod().equals(getPlannedPeriod())) rc += tx.getAmount();
		}
		float budget = getAmount();
		commitTx();
		
		if (budget < 0) {
			// Less budget than planned expenses
			if (rc >= budget) rc = budget; 
		} else {
			// More plans than budgeted
			if (rc >= budget) rc = budget;
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMatchString() {
		return getTransferObject().getMatchString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMatchString(String matchString) {
		String oldValue = getMatchString();
		getTransferObject().setMatchString(matchString);
		firePropertyChange(PROPERTY_MATCH_STRING, oldValue, matchString);
	}

}
