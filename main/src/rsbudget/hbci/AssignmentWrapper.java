/**
 * 
 */
package rsbudget.hbci;

import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;

/**
 * A wrapper that will hold a budget, a planned TX or a certain action.
 * @author ralph
 *
 */
public class AssignmentWrapper {

	public static enum Action {
		SEPARATOR,
		IGNORE,
		NEW,
		ASSIGNED,
		BUDGET,
		PLANNED_TX,
		CATEGORY;
	}
	
	private Action action;
	private Transaction transaction;
	private PlannedTransaction plannedTransaction;
	private Budget budget;
	private Category category;
	
	/**
	 * Constructor.
	 */
	public AssignmentWrapper(Action action) {
		this.action = action;
	}

	/**
	 * Constructor.
	 */
	public AssignmentWrapper(Transaction tx) {
		action             = Action.ASSIGNED;
		transaction        = tx;
		plannedTransaction = tx.getPlannedTransaction();
	}

	/**
	 * Constructor.
	 */
	public AssignmentWrapper(PlannedTransaction tx) {
		action             = Action.PLANNED_TX;
		plannedTransaction = tx;
	}

	/**
	 * Constructor.
	 */
	public AssignmentWrapper(Budget budget) {
		action      = Action.BUDGET;
		this.budget = budget;
	}

	/**
	 * Constructor.
	 */
	public AssignmentWrapper(Category category) {
		action             = Action.CATEGORY;
		this.category      = category;
	}

	/**
	 * Returns the action.
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Returns the transaction.
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * Returns the plannedTransaction.
	 * @return the plannedTransaction
	 */
	public PlannedTransaction getPlannedTransaction() {
		return plannedTransaction;
	}

	/**
	 * Returns the budget.
	 * @return the budget
	 */
	public Budget getBudget() {
		return budget;
	}

	/**
	 * Returns the category.
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}


}
