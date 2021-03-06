/**
 * 
 */
package rsbudget.data.impl.bo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.MonetaryValue;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.dto.BudgetDTO;
import rsbudget.data.dto.PlannedTransactionDTO;
import rsbudget.data.dto.TransactionDTO;

/**
 * Budget BO wrapper.
 * @author ralph
 *
 */
public class BudgetBO extends AbstractRsBudgetDbBO<BudgetDTO> implements Budget {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -7991370495378166887L;

	private Set<Transaction> transactions;
	private Set<PlannedTransaction> plannedTransactions;

	/**
	 * Constructor with empty DTO.
	 */
	public BudgetBO() {
		this(new BudgetDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public BudgetBO(BudgetDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getAmount() {
		return getTransferObject().getAmount();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAmount(BigDecimal amount) {
		BigDecimal oldValue = getAmount();
		getTransferObject().setAmount(amount);
		firePropertyChange(PROPERTY_AMOUNT, oldValue, amount);
	}

	/**
	 * 
	 * @{inheritDoc}
	 */
	@Override
	public BigDecimal getAvailable() {
		BigDecimal budget = getAmount();
		BigDecimal actual = getActual();
		if (budget.signum() < 0) {
			if (budget.subtract(actual).signum() > 0) return BigDecimal.ZERO;
		} else {
			if (budget.subtract(actual).signum() < 0) return BigDecimal.ZERO;
		}
		return budget.subtract(actual);
	}

	/**
	 * What is the total of this values.
	 * @param coll collection of amounts
	 * @return total of amounts
	 */
	protected BigDecimal getTotal(Collection<? extends MonetaryValue> coll) {
		BigDecimal rc = BigDecimal.ZERO;
		for (MonetaryValue v : coll) {
			rc = rc.add(v.getAmount());
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getActual() {
		return getTotal(getTransactions());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getPlanned() {
		BigDecimal amount = getAmount();
		BigDecimal planned = BigDecimal.ZERO;
		for (PlannedTransaction ptx : getPlannedTransactions()) {
			planned = planned.add(ptx.getAmount());
		}
		if (amount.signum() < 0) {
			return amount.compareTo(planned) < 0 ? amount : planned;
		}
		return amount.compareTo(planned) > 0 ? amount : planned;
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
	public Plan getPlan() {
		return (Plan)getBusinessObject(getTransferObject().getPlan());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlan(Plan plan) {
		Plan oldValue = getPlan();
		getTransferObject().setPlan(((PlanBO)plan).getTransferObject());
		if (oldValue != null) oldValue.removeBudget(this);
		if (plan != null)     plan.addBudget(this);
		firePropertyChange(PROPERTY_PLAN, oldValue, plan);
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
	public Set<PlannedTransaction> getPlannedTransactions() {
		return Collections.unmodifiableSet(_getPlannedTransactions());
	}

	/**
	 * For internal use only, returns the budget set.
	 * @return the budget set
	 */
	protected Set<PlannedTransaction> _getPlannedTransactions() {
		if (plannedTransactions == null) {
			plannedTransactions = new HashSet<PlannedTransaction>();
			beginTx();
			Set<PlannedTransactionDTO> set = getAttachedTransferObject().getPlannedTransactions();
			if (set != null) {
				for (PlannedTransactionDTO t : set) {
					plannedTransactions.add((PlannedTransaction)getBusinessObject(t));
				}
			}
			commitTx();
		}
		return plannedTransactions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addPlannedTransaction(PlannedTransaction plannedTransaction) {
		if (plannedTransaction.getId() == null) return false; // We cannot add it yet!
		Set<PlannedTransaction> set = _getPlannedTransactions();
		boolean rc = set.add(plannedTransaction);
		if (rc) firePropertyChange(PROPERTY_PLANNED_TRANSACTIONS, null, plannedTransaction);
		return rc;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removePlannedTransaction(PlannedTransaction plannedTransaction) {
		Set<PlannedTransaction> set = _getPlannedTransactions();
		boolean rc = set.remove(plannedTransaction);
		if (rc) firePropertyChange(PROPERTY_PLANNED_TRANSACTIONS, plannedTransaction, null);
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Transaction> getTransactions() {
		return Collections.unmodifiableSet(_getTransactions());
	}

	/**
	 * For internal use only, returns the budget set.
	 * @return the budget set
	 */
	protected Set<Transaction> _getTransactions() {
		if (transactions == null) {
			transactions = new HashSet<Transaction>();
			beginTx();
			Set<TransactionDTO> set = getAttachedTransferObject().getTransactions();
			if (set != null) {
				for (TransactionDTO t : set) {
					transactions.add((Transaction)getBusinessObject(t));
				}
			}
			commitTx();
		}
		return transactions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addTransaction(Transaction transaction) {
		if (transaction.getId() == null) return false; // We cannot add it yet!
		Set<Transaction> set = _getTransactions();
		boolean rc = set.add(transaction);
		if (rc) firePropertyChange(PROPERTY_TRANSACTIONS, null, transaction);
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeTransaction(Transaction transaction) {
		Set<Transaction> set = _getTransactions();
		boolean rc = set.remove(transaction);
		if (rc) firePropertyChange(PROPERTY_TRANSACTIONS, transaction, null);
		return rc;
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
	public String getMatchRule() {
		return getTransferObject().getMatchRule();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMatchRule(String matchRule) {
		String oldValue = getMatchRule();
		getTransferObject().setMatchRule(matchRule);
		firePropertyChange(PROPERTY_MATCH_RULE, oldValue, matchRule);
	}

}
