/**
 * 
 */
package rsbudget.parts.transactions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.LoggerFactory;

import rs.baselib.bean.AbstractBean;
import rs.baselib.util.CommonUtils;
import rs.baselib.util.IWrapper;
import rs.baselib.util.RsDate;
import rs.data.api.bo.IGeneralBO;
import rsbaselib.util.RsCommonUtils;
import rsbudget.Plugin;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.BudgetRecognition;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.CategoryRecognition;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;
import rsbudget.preferences.PreferencesUtils;
import rsbudget.util.CurrencyLabelProvider;

/**
 * Wraps periodical budgets and transactions so the usage in a table is unified.
 * @author ralph
 *
 */
public class TxRowWrapper extends AbstractBean implements IWrapper {

	public static final String PROPERTY_VALUE_DATE = "valueDate";
	public static final String PROPERTY_PLANNED_AMOUNT = "plannedAmount";
	public static final String PROPERTY_ACTUAL_AMOUNT = "actualAmount";
	public static final String PROPERTY_CATEGORY = "category";
	public static final String PROPERTY_CREATION_DATE = "creationDate";
	public static final String PROPERTY_DETAILS = "details";
	public static final String PROPERTY_DISPLAY_ORDER = "displayOrder";
	public static final String PROPERTY_TEXT = "text";
	public static final String PROPERTY_TX_BUDGET = "txBudget";
	public static final String PROPERTY_TX_TRANSACTION = "txTransaction";
	public static final String PROPERTY_TX_PLANNED_TRANSACTION = "txPlannedTransaction";
	
	private Budget budget;
	private PlannedTransaction plannedTransaction;
	private Transaction transaction;
	private PropertyChangeListener listener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			handlePropertyChange(evt);
		}
	};
	
	/**
	 * Constructor.
	 */
	public TxRowWrapper(Budget budget) {
		this.budget = budget;
		budget.addPropertyChangeListener(listener);
	}

	/**
	 * Constructor.
	 */
	public TxRowWrapper(Transaction transaction) {
		this.transaction = transaction;
		transaction.addPropertyChangeListener(listener);
	}

	/**
	 * Constructor.
	 */
	public TxRowWrapper(PlannedTransaction plannedTransaction) {
		this.plannedTransaction = plannedTransaction;
		plannedTransaction.addPropertyChangeListener(listener);
	}

	/**
	 * Refires the event.
	 * @param evt
	 */
	protected void handlePropertyChange(PropertyChangeEvent evt) {
		String propertyName = null;
		String evtProperty = evt.getPropertyName();
		if (evt.getSource() == budget) {
			switch (evtProperty) {
			case Budget.PROPERTY_AMOUNT: propertyName = PROPERTY_PLANNED_AMOUNT; break;
			case Budget.PROPERTY_CATEGORY: propertyName = PROPERTY_CATEGORY; break;
			case Budget.CREATION_DATE: propertyName = PROPERTY_CREATION_DATE; break;
			case Budget.PROPERTY_DISPLAY_ORDER: propertyName = PROPERTY_DISPLAY_ORDER; break;
			case Budget.PROPERTY_NAME: propertyName = PROPERTY_TEXT; break;
			case Budget.PROPERTY_PLANNED_TRANSACTIONS:
			case Budget.PROPERTY_TRANSACTIONS:
				if (evt.getNewValue() != null) bindBudgetTx(evt.getNewValue());
				else unbindBudgetTx(evt.getOldValue());
				
			}
		} else if (evt.getSource() == plannedTransaction) {
			switch (evtProperty) {
			case PlannedTransaction.PROPERTY_AMOUNT: propertyName = PROPERTY_PLANNED_AMOUNT; break;
			case PlannedTransaction.PROPERTY_CATEGORY: propertyName = PROPERTY_CATEGORY; break;
			case PlannedTransaction.CREATION_DATE: propertyName = PROPERTY_CREATION_DATE; break;
			case PlannedTransaction.PROPERTY_ANNOTATION: propertyName = PROPERTY_DETAILS; break;
			case PlannedTransaction.PROPERTY_DISPLAY_ORDER: propertyName = PROPERTY_DISPLAY_ORDER; break;
			case PlannedTransaction.PROPERTY_NAME: propertyName = PROPERTY_TEXT; break;
			case PlannedTransaction.PROPERTY_BUDGET: propertyName = PROPERTY_TX_BUDGET; break;
			}
		} else if (evt.getSource() == transaction) {
			switch (evtProperty) {
			case Transaction.PROPERTY_VALUE_DATE: propertyName = PROPERTY_VALUE_DATE; break;
			case Transaction.PROPERTY_AMOUNT: propertyName = PROPERTY_ACTUAL_AMOUNT; break;
			case Transaction.PROPERTY_CATEGORY: propertyName = PROPERTY_CATEGORY; break;
			case Transaction.CREATION_DATE: propertyName = PROPERTY_CREATION_DATE; break;
			case Transaction.PROPERTY_ANNOTATION: propertyName = PROPERTY_DETAILS; break;
			case Transaction.PROPERTY_DISPLAY_ORDER: propertyName = PROPERTY_DISPLAY_ORDER; break;
			case Transaction.PROPERTY_TEXT: propertyName = PROPERTY_TEXT; break;
			case Transaction.PROPERTY_BUDGET: propertyName = PROPERTY_TX_BUDGET; break;
			}
		} else if (isBudget()) {
			checkBudget();
		}
		if (propertyName != null) firePropertyChange(propertyName, evt.getOldValue(), evt.getNewValue());
	}
	
	/**
	 * Watch this BO's values.
	 * @param o
	 */
	protected void bindBudgetTx(Object o) {
		if (o instanceof IGeneralBO<?>) {
			((IGeneralBO<?>)o).addPropertyChangeListener(listener);
			checkBudget();
		}
	}
	
	/**
	 * Un-watch this BO's values.
	 * @param o
	 */
	protected void unbindBudgetTx(Object o) {
		if (o instanceof IGeneralBO<?>) {
			((IGeneralBO<?>)o).removePropertyChangeListener(listener);
			checkBudget();
		}
	}
	
	/**
	 * Checks actual and planned amount of the budget.
	 */
	protected void checkBudget() {
		BigDecimal oldPlanned = getPlannedAmount();
		BigDecimal oldActual  = getActualAmount();
		
		BigDecimal actual  = budget.getActual();
		BigDecimal planned = budget.getPlanned();
		BigDecimal available = budget.getAvailable();
				
		boolean planChanged   = !RsCommonUtils.equals(oldPlanned, planned);
		boolean actualChanged = !RsCommonUtils.equals(oldActual, actual);
		
		//System.out.println("oldActual="+oldActual+" actual="+actual+" changed="+actualChanged);

		if (planChanged) {
			firePropertyChange(PROPERTY_PLANNED_AMOUNT, oldPlanned, planned);
		}
		if (actualChanged) {
			firePropertyChange(PROPERTY_ACTUAL_AMOUNT, oldActual, actual);
		}
		
		if (actualChanged || planChanged) {
			firePropertyChange(PROPERTY_DETAILS, "", "Noch verf\u00fcgbar: "+CurrencyLabelProvider.INSTANCE.getText(available));
		}
	}
	
	/**
	 * Returns the id.
	 * @return
	 */
	public long getId() {
		if (budget != null) return budget.getId();
		if (transaction != null) return transaction.getId();
		return plannedTransaction.getId();
	}
	
	/**
	 * Returns the plan.
	 * @return the plan
	 */
	public Plan getPlan() {
		if (budget != null) return budget.getPlan();
		if (transaction != null) return transaction.getPlan();
		return plannedTransaction.getPlan();
	}
	
	/**
	 * Returns the category.
	 * @return the category
	 */
	public Category getCategory() {
		if (budget != null) return budget.getCategory();
		if (transaction != null) return transaction.getCategory();
		return plannedTransaction.getCategory();
	}
	
	/**
	 * Sets the category.
	 * @param category new category.
	 */
	public void setCategory(Category category) {
		if (budget != null) budget.setCategory(category);
		else if (transaction != null) transaction.setCategory(category);
		else plannedTransaction.setCategory(category);
	}
	
	/**
	 * Returns the displayOrder.
	 * @return the displayOrder
	 */
	public int getDisplayOrder() {
		if (budget != null) return budget.getDisplayOrder();
		if (transaction != null) return transaction.getDisplayOrder();
		return plannedTransaction.getDisplayOrder();
	}

	/**
	 * Sets the displayOrder.
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder) {
		if (budget != null) budget.setDisplayOrder(displayOrder);
		else if (transaction != null) {
			transaction.setDisplayOrder(displayOrder);
			if (transaction.getPlannedTransaction() != null) transaction.getPlannedTransaction().setDisplayOrder(displayOrder);
		} else plannedTransaction.setDisplayOrder(displayOrder);
	}
	
	/**
	 * Returns the displayOrder.
	 * @return the displayOrder
	 */
	public RsDate getValueDate() {
		if (transaction != null) return transaction.getValueDate();
		return null;
	}

	/**
	 * Sets the value date.
	 * @param date
	 */
	public void setValueDate(RsDate date) {
		if (transaction != null) transaction.setValueDate(date);
		else if (plannedTransaction != null) {
			// Convert to transaction
			createTransaction();
			transaction.setValueDate(date);
			firePropertyChange(PROPERTY_ACTUAL_AMOUNT, BigDecimal.ZERO, transaction.getAmount());
		}
	}
	
	/**
	 * Returns the planned amount.
	 * @return the planned amount
	 */
	public BigDecimal getPlannedAmount() {
		if (budget != null) return budget.getAmount();
		if (transaction != null) {
			if (transaction.getPlannedTransaction() != null) return transaction.getPlannedTransaction().getAmount();
			return BigDecimal.ZERO;
		}
		return plannedTransaction.getAmount();
	}
	
	/**
	 * Sets the planned amount.
	 * @param amount the value
	 */
	public void setPlannedAmount(BigDecimal amount) {
		if (budget != null) budget.setAmount(amount);
		else if (transaction != null) {
			if (transaction.getPlannedTransaction() == null) {
				createPlannedTransaction();
			}
			transaction.getPlannedTransaction().setAmount(amount);
		} else plannedTransaction.setAmount(amount);
	}

	/**
	 * Returns the account.
	 * @return the account
	 */
	public Account getAccount() {
		if (budget != null) return null;
		if (transaction != null) return transaction.getAccount();
		return plannedTransaction.getAccount();
	}
	
	/**
	 * Sets the account.
	 * @param account the value
	 */
	public void setAccount(Account account) {
		if (transaction != null) transaction.setAccount(account);
		else if (plannedTransaction != null) plannedTransaction.setAccount(account);
	}

	/**
	 * Returns the actual amount.
	 * @return the planned amount
	 */
	public BigDecimal getActualAmount() {
		if (budget != null) return budget.getActual();
		if (transaction != null) return transaction.getAmount();
		return null;
	}

	/**
	 * Sets the amount.
	 * @param amount the value
	 */
	public void setActualAmount(BigDecimal amount) {
		if (transaction != null) transaction.setAmount(amount);
		else if (plannedTransaction != null) {
			createTransaction();
			transaction.setAmount(amount);
			firePropertyChange(PROPERTY_ACTUAL_AMOUNT, BigDecimal.ZERO, amount);
		}
	}

	/**
	 * Returns the displayable name.
	 * @return the displayable name
	 */
	public String getText() {
		if (budget != null) return budget.getName();
		if (transaction != null) return transaction.getText();
		return plannedTransaction.getName();
	}
	
	/**
	 * Sets the displayable name.
	 * @param text
	 */
	public void setText(String text) {
		String oldText = getText();
		if (budget != null) budget.setName(text);
		else if (transaction != null) {
			transaction.setText(text);
			if (CommonUtils.isEmpty(oldText) || CommonUtils.equals(oldText, Plugin.translate("part.transactions.label.newtx"))) {
				if (PreferencesUtils.isMatchBudgets()) guessBudget(text);
				if (PreferencesUtils.isMatchCategories()) guessCategory(text);
			}
		} else {
			plannedTransaction.setName(text);
			if (CommonUtils.isEmpty(oldText) || CommonUtils.equals(oldText, Plugin.translate("part.transactions.label.newtx"))) {
				if (PreferencesUtils.isMatchBudgets()) guessBudget(text);
				if (PreferencesUtils.isMatchCategories()) guessCategory(text);
			}
		}
	}
	
	/**
	 * Returns the details to be displayed.
	 * @return the details.
	 */
	public String getDetails() {
		if (budget != null) return Plugin.translate("%part.transactions.column.details.stillavailable")+": "+CurrencyLabelProvider.INSTANCE.getText(budget.getAvailable());
		if (transaction != null) return transaction.getAnnotation();
		return plannedTransaction.getAnnotation();
	}
	
	/**
	 * Sets the details.
	 * @param details
	 */
	public void setDetails(String details) {
		if (transaction != null) transaction.setAnnotation(details);
		else if (plannedTransaction != null) plannedTransaction.setAnnotation(details);
	}
	
	/**
	 * Returns the budget for the transaction.
	 * @return
	 */
	public Budget getTxBudget() {
		if (budget != null) return null;
		if (transaction != null) return transaction.getBudget();
		return plannedTransaction.getBudget();
	}
	
	/**
	 * Sets the budget for the transaction.
	 * No impact if the wrapper is a budget itself.
	 * @param budget
	 */
	public void setTxBudget(Budget budget) {
		if (transaction != null) {
			transaction.setBudget(budget);
			if (transaction.getPlannedTransaction() != null) {
				transaction.getPlannedTransaction().setBudget(budget);
			}
		} else if (plannedTransaction != null) plannedTransaction.setBudget(budget);
	}
	
	/**
	 * Returns true when this is a budget row.
	 * @return
	 */
	public boolean isBudget() {
		return budget != null;
	}
	
	/**
	 * Returns true when this is a transaction row.
	 * @return
	 */
	public boolean isTransaction() {
		return transaction != null;
	}
	
	/**
	 * Returns true when this is a transaction row.
	 * @return
	 */
	public boolean isPlannedTransaction() {
		return (transaction == null) && (plannedTransaction != null);
	}
	
	/**
	 * Returns the creation date.
	 * @return
	 */
	public RsDate getCreationDate() {
		if (budget != null) return budget.getCreationDate();
		if (transaction != null) return transaction.getCreationDate();
		return plannedTransaction.getCreationDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getWrapped() {
		if (budget != null) return budget;
		if (transaction != null) return transaction;
		return plannedTransaction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		if (budget != null) return budget.hashCode();
		if (transaction != null) return transaction.hashCode();
		return plannedTransaction.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TxRowWrapper) obj = ((TxRowWrapper)obj).getWrapped();
		if (budget != null) return budget.equals(obj);
		if (transaction != null) return transaction.equals(obj);
		return plannedTransaction.equals(obj);
	}
	
	/**
	 * Creates the appropriate actual transaction from the planned TX.
	 */
	protected void createTransaction() {
		if (transaction != null) return;
		if (budget != null) return;
		
		// We assume to have a current transaction
		RsBudgetDaoFactory factory = (RsBudgetDaoFactory)plannedTransaction.getDao().getFactory();
		transaction = factory.getTransactionDAO().newInstance();
		
		// Set properties
		transaction.setAccount(plannedTransaction.getAccount());
		transaction.setAmount(plannedTransaction.getAmount());
		transaction.setAnnotation(plannedTransaction.getAnnotation());
		transaction.setBudget(plannedTransaction.getBudget());
		transaction.setCategory(plannedTransaction.getCategory());
		transaction.setDisplayOrder(plannedTransaction.getDisplayOrder());
		transaction.setPlan(plannedTransaction.getPlan());
		transaction.setPlannedTransaction(plannedTransaction);
		transaction.setText(plannedTransaction.getName());
		transaction.setTransactionDate(new RsDate());
		transaction.setValueDate(new RsDate());
		transaction.setPartnerName("");
		transaction.setPartnerBank("");
		transaction.setPartnerAccountNumber("");
		transaction.setHash("");
		
		// Convert this object and save
		plannedTransaction.setTransaction(transaction);
		factory.getTransactionDAO().create(transaction);
		factory.getPlannedTransactionDAO().save(plannedTransaction);
		plannedTransaction.getPlan().addTransaction(transaction);
		
		transaction.addPropertyChangeListener(listener);
	}
	
	/**
	 * Creates the appropriate planned transaction from the actual TX.
	 */
	protected void createPlannedTransaction() {
		if (transaction == null) return;
		
		// We assume to have a current transaction
		RsBudgetDaoFactory factory = (RsBudgetDaoFactory)transaction.getDao().getFactory();
		PlannedTransaction ptx = factory.getPlannedTransactionDAO().newInstance();
		
		// Set properties
		ptx.setAccount(transaction.getAccount());
		ptx.setAmount(transaction.getAmount());
		ptx.setAnnotation(transaction.getAnnotation());
		ptx.setBudget(transaction.getBudget());
		ptx.setCategory(transaction.getCategory());
		ptx.setDisplayOrder(transaction.getDisplayOrder());
		ptx.setName(transaction.getText());
		ptx.setPlan(transaction.getPlan());
		ptx.setTransaction(transaction);

		// Save
		factory.getPlannedTransactionDAO().create(ptx);
		transaction.setPlannedTransaction(ptx);
		factory.getTransactionDAO().create(transaction);
	}
	
	public void transactionDeleted() {
		plannedTransaction = transaction.getPlannedTransaction();
		BigDecimal oldValue = transaction.getAmount();
		transaction.removePropertyChangeListener(listener);
		transaction = null;
		firePropertyChange(PROPERTY_ACTUAL_AMOUNT, oldValue, null);
	}
	
	/**
	 * Tries to guess a proper budget from the transaction text.
	 * @param text the new text to guess from
	 */
	protected void guessBudget(String text) {
		if (CommonUtils.isEmpty(text)) return;
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		try {
			factory.begin();
			Budget guessed = null;
			List<BudgetRecognition> expressions = factory.getBudgetRecognitionDAO().findAll();
			for (BudgetRecognition expr : expressions) {
				if (expr.matches(text)) {
					guessed = findBudget(expr.getBudget());
				}
				if (guessed != null) break;
			}
			if (guessed != null) setTxBudget(guessed);
			factory.commit();
		} catch (Exception e) {
			LoggerFactory.getLogger(TxRowWrapper.class).error("Cannot guess budget", e);
			try {
				factory.rollback();
			} catch (Exception e2) {
				LoggerFactory.getLogger(TxRowWrapper.class).error("Cannot rollback TX", e2);
			}
		}
	}
	
	/**
	 * Find a budget in current plan that matches the given periodical budget 
	 * @param periodicalBudget the budget to be found
	 * @return the budget within this plan
	 */
	protected Budget findBudget(PeriodicalBudget periodicalBudget) {
		String name = periodicalBudget.getName();
		for (Budget b : getPlan().getBudgets()) {
			if (name.equalsIgnoreCase(b.getName())) {
				return b;
			}
		}
		return null;
	}
		
	/**
	 * Tries to guess a proper category from the transaction text.
	 * @param text the new text to guess from
	 */
	protected void guessCategory(String text) {
		if (CommonUtils.isEmpty(text)) return;
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		try {
			factory.begin();
			Category guessed = null;
			List<CategoryRecognition> expressions = factory.getCategoryRecognitionDAO().findAll();
			for (CategoryRecognition expr : expressions) {
				if (expr.matches(text)) {
					guessed = expr.getCategory();
				}
				if (guessed != null) break;
			}
			if (guessed != null) setCategory(guessed);
			factory.commit();
		} catch (Exception e) {
			LoggerFactory.getLogger(TxRowWrapper.class).error("Cannot guess category", e);
			try {
				factory.rollback();
			} catch (Exception e2) {
				LoggerFactory.getLogger(TxRowWrapper.class).error("Cannot rollback TX", e2);
			}
		}
		
	}
}
