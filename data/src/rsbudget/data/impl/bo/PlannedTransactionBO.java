/**
 * 
 */
package rsbudget.data.impl.bo;

import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.dto.PlannedTransactionDTO;

/**
 * Planned transaction BO wrapper.
 * @author ralph
 *
 */
public class PlannedTransactionBO extends AbstractRsBudgetDbBO<PlannedTransactionDTO> implements PlannedTransaction {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -2552460025155833202L;

	/**
	 * Constructor with empty DTO.
	 */
	public PlannedTransactionBO() {
		this(new PlannedTransactionDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public PlannedTransactionBO(PlannedTransactionDTO dto) {
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
	public Budget getBudget() {
		return (Budget)getBusinessObject(getTransferObject().getBudget());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBudget(Budget budget) {
		Budget oldValue = getBudget();
		if (oldValue != null) oldValue.removePlannedTransaction(this);
		if (budget != null) {
			getTransferObject().setBudget(((BudgetBO)budget).getTransferObject());
			budget.addPlannedTransaction(this);
		} else {
			getTransferObject().setBudget(null);
		}
		firePropertyChange(PROPERTY_BUDGET, oldValue, budget);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Transaction getTransaction() {
		return (Transaction)getBusinessObject(getTransferObject().getTransaction());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTransaction(Transaction transaction) {
		Transaction oldValue = getTransaction();
		if (transaction != null) {
			getTransferObject().setTransaction(((TransactionBO)transaction).getTransferObject());
		} else {
			getTransferObject().setTransaction(null);
		}
		firePropertyChange(PROPERTY_TRANSACTION, oldValue, transaction);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Plan getPlan() {
		return (Plan)getBusinessObject(getTransferObject().getPlan());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlan(Plan plan) {
		Plan oldValue = getPlan();
		if (oldValue != null) oldValue.removePlannedTransaction(this);
		if (plan != null) {
			getTransferObject().setPlan(((PlanBO)plan).getTransferObject());
			plan.addPlannedTransaction(this);
		} else {
			getTransferObject().setPlan(null);
		}
		firePropertyChange(PROPERTY_PLAN, oldValue, plan);
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
	public String toString() {
		return super.toString()+"("+getName()+")";
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
