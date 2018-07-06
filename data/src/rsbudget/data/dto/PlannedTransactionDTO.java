/**
 * 
 */
package rsbudget.data.dto;





/**
 * A transaction that shall take place in a specific month.
 * The planned transaction is an instance of a periodical transaction.
 * @author ralph
 *
 */
public class PlannedTransactionDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 5470972040797923179L;

	private AccountDTO account;
	private float amount;
	private String name;
	private CategoryDTO category;
	private String annotation;
	private BudgetDTO budget;
	private TransactionDTO transaction;
	private PlanDTO plan;
	private int displayOrder;
	private String matchRule;

	/**
	 * Constructor.
	 */
	public PlannedTransactionDTO() {
	}

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the account.
	 * @return the account
	 */
	public AccountDTO getAccount() {
		return account;
	}

	/**
	 * Sets the account.
	 * @param account the account to set
	 */
	public void setAccount(AccountDTO account) {
		this.account = account;
	}

	/**
	 * Returns the amount.
	 * @return the amount
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * Sets the amount.
	 * @param amount the amount to set
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * Returns the category.
	 * @return the category
	 */
	public CategoryDTO getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 * @param category the category to set
	 */
	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	/**
	 * Returns the annotation.
	 * @return the annotation
	 */
	public String getAnnotation() {
		return annotation;
	}

	/**
	 * Sets the annotation.
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	/**
	 * Returns the budget.
	 * @return the budget
	 */
	public BudgetDTO getBudget() {
		return budget;
	}

	/**
	 * Sets the budget.
	 * @param budget the budget to set
	 */
	public void setBudget(BudgetDTO budget) {
		this.budget = budget;
	}

	/**
	 * Returns the transaction.
	 * @return the transaction
	 */
	public TransactionDTO getTransaction() {
		return transaction;
	}

	/**
	 * Sets the transaction.
	 * @param transaction the transaction to set
	 */
	public void setTransaction(TransactionDTO transaction) {
		this.transaction = transaction;
	}

	/**
	 * Returns the plan.
	 * @return the plan
	 */
	public PlanDTO getPlan() {
		return plan;
	}

	/**
	 * Sets the plan.
	 * @param plan the plan to set
	 */
	public void setPlan(PlanDTO plan) {
		this.plan = plan;
	}

	/**
	 * Returns the displayOrder.
	 * @return the displayOrder
	 */
	public int getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * Sets the displayOrder.
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * Returns the matchRule.
	 * @return the matchRule
	 */
	public String getMatchRule() {
		return matchRule;
	}

	/**
	 * Sets the matchRule.
	 * @param matchRule the matchRule to set
	 */
	public void setMatchRule(String matchRule) {
		this.matchRule = matchRule;
	}


}
