/**
 * 
 */
package rsbudget.data.impl.bo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import rs.baselib.util.RsMonth;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.dto.BudgetDTO;
import rsbudget.data.dto.PlanDTO;
import rsbudget.data.dto.PlannedTransactionDTO;
import rsbudget.data.dto.TransactionDTO;

/**
 * Transaction category BO wrapper.
 * @author ralph
 *
 */
public class PlanBO extends AbstractRsBudgetDbBO<PlanDTO> implements Plan {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 3471879816623946664L;

	private Set<Budget> budgets;
	private Set<PlannedTransaction> plannedTransactions;
	private Set<Transaction> transactions;
	
	/**
	 * Constructor with empty DTO.
	 */
	public PlanBO() {
		this(new PlanDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public PlanBO(PlanDTO dto) {
		super(dto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsMonth getMonth() {
		return getTransferObject().getMonth();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMonth(RsMonth month) {
		RsMonth oldValue = getMonth();
		getTransferObject().setMonth(month);
		firePropertyChange(PROPERTY_MONTH, oldValue, month);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBalanceStart() {
		return getTransferObject().getBalanceStart();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBalanceStart(BigDecimal balanceStart) {
		BigDecimal oldValue = getBalanceStart();
		getTransferObject().setBalanceStart(balanceStart);
		firePropertyChange(PROPERTY_BALANCE_START, oldValue, balanceStart);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBalanceEnd() {
		return getTransferObject().getBalanceEnd();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBalanceEnd(BigDecimal balanceEnd) {
		BigDecimal oldValue = getBalanceEnd();
		getTransferObject().setBalanceEnd(balanceEnd);
		firePropertyChange(PROPERTY_BALANCE_END, oldValue, balanceEnd);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Budget> getBudgets() {
		return Collections.unmodifiableSet(_getBudgets());
	}

	/**
	 * For internal use only, returns the budget set.
	 * @return the budget set
	 */
	protected Set<Budget> _getBudgets() {
		if (budgets == null) {
			budgets = new HashSet<Budget>();
			beginTx();
			Set<BudgetDTO> set = getAttachedTransferObject().getBudgets();
			if (set != null) {
				for (BudgetDTO t : set) {
					budgets.add((Budget)getBusinessObject(t));
				}
			}
			commitTx();
		}
		return budgets;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addBudget(Budget budget) {
		if (budget.getId() == null) return false; // We cannot add it yet!
		Set<Budget> set = _getBudgets();
		boolean rc = set.add(budget);
		if (rc) firePropertyChange(PROPERTY_BUDGETS, null, budget);
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeBudget(Budget budget) {
		Set<Budget> set = _getBudgets();
		boolean rc = set.remove(budget);
		if (rc) firePropertyChange(PROPERTY_BUDGETS, budget, null);
		return rc;
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
		Set<PlannedTransaction> set = _getPlannedTransactions();
		if (plannedTransaction.getId() == null) {
			return false; // We cannot add it yet!
		}
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
	public String getComment() {
		return getTransferObject().getComment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setComment(String comment) {
		String oldValue = getComment();
		getTransferObject().setComment(comment);
		firePropertyChange(PROPERTY_COMMENT, oldValue, comment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay() {
		return getMonth().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxTxDisplayOrder() {
		int rc = 0;
		for (Transaction tx : getTransactions()) {
			rc = Math.max(tx.getDisplayOrder(), rc);
		}
		for (PlannedTransaction tx : getPlannedTransactions()) {
			rc = Math.max(tx.getDisplayOrder(), rc);
		}
		return rc;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxBudgetDisplayOrder() {
		int rc = 0;
		for (Budget b : getBudgets()) {
			rc = Math.max(b.getDisplayOrder(), rc);
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<RsBudgetBO<?>> getMixedTransactions() {
		Set<RsBudgetBO<?>> rc = new HashSet<RsBudgetBO<?>>();
		
		Set<PlannedTransaction> planned = new HashSet<PlannedTransaction>(getPlannedTransactions()); 
		Set<Transaction> actual = getTransactions();
		if ((planned != null) && (actual != null)) {
			for (Transaction t : actual) {
				PlannedTransaction p = t.getPlannedTransaction();
				if ((p != null) && planned.contains(p)) planned.remove(p);
				rc.add(t);
			}
		}
		if (planned != null) rc.addAll(planned);
		return rc;
	}	
}
