/**
 * 
 */
package rsbudget.parts.budgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import rs.baselib.bean.AbstractBean;
import rs.baselib.util.IWrapper;
import rs.baselib.util.RsDate;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.util.PlannedPeriod;

/**
 * Wraps periodical budgets and transactions so the usage in a table is unified.
 * @author ralph
 *
 */
public class BudgetRowWrapper extends AbstractBean implements IWrapper {

	public static final String PROPERTY_AMOUNT = "amount";
	public static final String PROPERTY_CATEGORY = "category";
	public static final String PROPERTY_CREATION_DATE = "creationDate";
	public static final String PROPERTY_DETAILS = "details";
	public static final String PROPERTY_DISPLAY_ORDER = "displayOrder";
	public static final String PROPERTY_PLANNED_PERIOD = "plannedPeriod";
	public static final String PROPERTY_TEXT = "text";
	public static final String PROPERTY_SEQUENCE_NUMBER = "sequenceNumber";
	public static final String PROPERTY_TX_BUDGET = "txBudget";
	
	private PeriodicalBudget budget;
	private PeriodicalTransaction transaction;
	private PropertyChangeListener listener = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			handlePropertyChange(evt);
		}
	};
	
	/**
	 * Constructor.
	 */
	public BudgetRowWrapper(PeriodicalBudget budget) {
		this.budget = budget;
		budget.addPropertyChangeListener(listener);
	}

	/**
	 * Constructor.
	 */
	public BudgetRowWrapper(PeriodicalTransaction transaction) {
		this.transaction = transaction;
		transaction.addPropertyChangeListener(listener);
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
			case PeriodicalBudget.PROPERTY_AMOUNT: propertyName = PROPERTY_AMOUNT; break;
			case PeriodicalBudget.PROPERTY_CATEGORY: propertyName = PROPERTY_CATEGORY; break;
			case PeriodicalBudget.PROPERTY_CREATION_DATE: propertyName = PROPERTY_CREATION_DATE; break;
			case PeriodicalBudget.PROPERTY_DISPLAY_ORDER: propertyName = PROPERTY_DISPLAY_ORDER; break;
			case PeriodicalBudget.PROPERTY_PLANNED_PERIOD: propertyName = PROPERTY_PLANNED_PERIOD; break;
			case PeriodicalBudget.PROPERTY_NAME: propertyName = PROPERTY_TEXT; break;
			}
		} else {
			switch (evtProperty) {
			case PeriodicalTransaction.PROPERTY_AMOUNT: propertyName = PROPERTY_AMOUNT; break;
			case PeriodicalTransaction.PROPERTY_CATEGORY: propertyName = PROPERTY_CATEGORY; break;
			case PeriodicalTransaction.PROPERTY_CREATION_DATE: propertyName = PROPERTY_CREATION_DATE; break;
			case PeriodicalTransaction.PROPERTY_ANNOTATION: propertyName = PROPERTY_DETAILS; break;
			case PeriodicalTransaction.PROPERTY_DISPLAY_ORDER: propertyName = PROPERTY_DISPLAY_ORDER; break;
			case PeriodicalTransaction.PROPERTY_PLANNED_PERIOD: propertyName = PROPERTY_PLANNED_PERIOD; break;
			case PeriodicalTransaction.PROPERTY_NAME: propertyName = PROPERTY_TEXT; break;
			case PeriodicalTransaction.PROPERTY_BUDGET: propertyName = PROPERTY_TX_BUDGET; break;
			case PeriodicalTransaction.PROPERTY_MONTH_SEQUENCE_NUMBER: propertyName = PROPERTY_SEQUENCE_NUMBER; break;
			}
		}
		if (propertyName != null) firePropertyChange(propertyName, evt.getOldValue(), evt.getNewValue());
	}
	
	/**
	 * Returns the id.
	 * @return
	 */
	public long getId() {
		return budget != null ? budget.getId() : transaction.getId();
	}
	
	/**
	 * Returns the category.
	 * @return the category
	 */
	public Category getCategory() {
		return budget != null ? budget.getCategory() : transaction.getCategory();
	}
	
	/**
	 * Sets the category.
	 * @param category new category.
	 */
	public void setCategory(Category category) {
		if (budget != null) budget.setCategory(category);
		else transaction.setCategory(category);
	}
	
	/**
	 * Returns the planned period.
	 * @return the planned period
	 */
	public PlannedPeriod getPlannedPeriod() {
		return budget != null ? budget.getPlannedPeriod() : transaction.getPlannedPeriod();
	}
	
	/**
	 * Sets the planned period.
	 * @param plannedPeriod
	 */
	public void setPlannedPeriod(PlannedPeriod plannedPeriod) {
		if (budget != null) budget.setPlannedPeriod(plannedPeriod);
		else transaction.setPlannedPeriod(plannedPeriod);
	}
	
	/**
	 * Returns the displayOrder.
	 * @return the displayOrder
	 */
	public int getDisplayOrder() {
		return budget != null ? budget.getDisplayOrder() : transaction.getDisplayOrder();
	}

	/**
	 * Sets the displayOrder.
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder) {
		if (budget != null) budget.setDisplayOrder(displayOrder);
		else transaction.setDisplayOrder(displayOrder);
	}
	
	/**
	 * Returns the amount.
	 * @return the amount
	 */
	public float getAmount() {
		return budget != null ? budget.getAmount() : transaction.getAmount();
	}
	
	/**
	 * Sets the amount.
	 * @param amount the value
	 */
	public void setAmount(float amount) {
		if (budget != null) budget.setAmount(amount);
		else transaction.setAmount(amount);
	}

	/**
	 * Returns the displayable name.
	 * @return the displayable name
	 */
	public String getText() {
		return (budget != null) ? budget.getName() : transaction.getName();
	}
	
	/**
	 * Sets the displayable name.
	 * @param text
	 */
	public void setText(String text) {
		if (budget != null) budget.setName(text);
		else transaction.setName(text);
	}
	
	/**
	 * Returns the details to be displayed.
	 * @return the details.
	 */
	public String getDetails() {
		return (budget != null) ? "" : transaction.getAnnotation();
	}
	
	/**
	 * Sets the details.
	 * @param details
	 */
	public void setDetails(String details) {
		if (transaction != null) transaction.setAnnotation(details);
	}
	
	/**
	 * Returns the sequence number to be displayed.
	 * @return the sequence number.
	 */
	public Integer getSequenceNumber() {
		return (budget != null) ? null : transaction.getMonthSequenceNumber();
	}
	
	/**
	 * Sets the sequence number.
	 * @param sequence number
	 */
	public void setSequenceNumber(Integer sequenceNumber) {
		if (transaction != null) transaction.setMonthSequenceNumber(sequenceNumber);
	}
	
	/**
	 * Returns the budget for the transaction.
	 * @return
	 */
	public PeriodicalBudget getTxBudget() {
		return budget != null ? null : transaction.getBudget();
	}
	
	/**
	 * Sets the budget for the transaction.
	 * No impact if the wrapper is a budget itself.
	 * @param budget
	 */
	public void setTxBudget(PeriodicalBudget budget) {
		if (this.transaction != null) transaction.setBudget(budget);
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
	 * Returns the creation date.
	 * @return
	 */
	public RsDate getCreationDate() {
		return budget != null ? budget.getCreationDate() : transaction.getCreationDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getWrapped() {
		return budget != null ? budget : transaction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return budget != null ? budget.hashCode() : transaction.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BudgetRowWrapper) obj = ((BudgetRowWrapper)obj).getWrapped();
		return budget != null ? budget.equals(obj) : transaction.equals(obj);
	}
	
	
}
