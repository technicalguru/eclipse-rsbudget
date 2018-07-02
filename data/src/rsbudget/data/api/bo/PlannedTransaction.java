/**
 * 
 */
package rsbudget.data.api.bo;





/**
 * A transaction that shall take place in a specific month.
 * The planned transaction is an instance of a periodical transaction.
 * @author ralph
 *
 */
public interface PlannedTransaction extends RsBudgetBO<Long>, MonetaryValue {

	public static final String PROPERTY_NAME        = "name";
	public static final String PROPERTY_ACCOUNT     = "account";
	public static final String PROPERTY_CATEGORY    = "category";
	public static final String PROPERTY_AMOUNT      = "amount";
	public static final String PROPERTY_ANNOTATION  = "annotation";
	public static final String PROPERTY_BUDGET      = "budget";
	public static final String PROPERTY_TRANSACTION = "transaction";
	public static final String PROPERTY_PLAN        = "plan";
	public static final String PROPERTY_DISPLAY_ORDER = "displayOrder";
	public static final String PROPERTY_MATCH_STRING = "matchString";
	
	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName();

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name);

	/**
	 * Returns the account.
	 * @return the account
	 */
	public Account getAccount();

	/**
	 * Sets the account.
	 * @param account the account to set
	 */
	public void setAccount(Account account);

	/**
	 * Returns the category.
	 * @return the category
	 */
	public Category getCategory();

	/**
	 * Sets the category.
	 * @param category the category to set
	 */
	public void setCategory(Category category);

	/**
	 * Returns the annotation.
	 * @return the annotation
	 */
	public String getAnnotation();

	/**
	 * Sets the annotation.
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(String annotation);

	/**
	 * Returns the budget.
	 * @return the budget
	 */
	public Budget getBudget();

	/**
	 * Sets the budget.
	 * @param budget the budget to set
	 */
	public void setBudget(Budget budget);

	/**
	 * Returns the transaction.
	 * @return the transaction
	 */
	public Transaction getTransaction();

	/**
	 * Sets the transaction.
	 * @param transaction the transaction to set
	 */
	public void setTransaction(Transaction transaction);

	/**
	 * Returns the plan.
	 * @return the plan
	 */
	public Plan getPlan();

	/**
	 * Sets the plan.
	 * @param plan the plan to set
	 */
	public void setPlan(Plan plan);

	/**
	 * Returns the displayOrder.
	 * @return the displayOrder
	 */
	public int getDisplayOrder();

	/**
	 * Sets the displayOrder.
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder);
	
	/**
	 * Returns the matching string.
	 * @return the mtching string
	 */
	public String getMatchString();

	/**
	 * Sets the matching string.
	 * @param matchString the matching string to set
	 */
	public void setMatchString(String matchString);

}
