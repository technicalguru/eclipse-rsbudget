/**
 * 
 */
package rsbudget.data.api.bo;

import rs.baselib.util.RsDate;

/**
 * A transaction.
 * @author ralph
 *
 */
public interface Transaction extends RsBudgetBO<Long>, MonetaryValue {

	public static final String PROPERTY_HASH                   = "hash";
	public static final String PROPERTY_ACCOUNT                = "account";
	public static final String PROPERTY_TEXT                   = "text";
	public static final String PROPERTY_CATEGORY               = "category";
	public static final String PROPERTY_PARTNER_NAME           = "partnerName";
	public static final String PROPERTY_PARTNER_BANK           = "partnerBank";
	public static final String PROPERTY_PARTNER_ACCOUNT_NUMBER = "partnerAccountNumber";
	public static final String PROPERTY_TRANSACTION_DATE       = "transactionDate";
	public static final String PROPERTY_VALUE_DATE             = "valueDate";
	public static final String PROPERTY_ANNOTATION             = "annotation";
	public static final String PROPERTY_PLANNED_TRANSACTION    = "plannedTransaction";
	public static final String PROPERTY_AMOUNT                 = "amount";
	public static final String PROPERTY_BUDGET                 = "budget";
	public static final String PROPERTY_PLAN                   = "plan";
	public static final String PROPERTY_DISPLAY_ORDER          = "displayOrder";

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
	 * Returns the identifying hash of this transaction.
	 * The hash shall be computed from transaction date, amount and balance after transaction.
	 * @return the hash of this transaction.
	 */
	public String getHash();

	/**
	 * Sets the identifying hash of this transaction.
	 * The hash shall be computed from transaction date, amount and balance after transaction.
	 * @param hash the hash of this transaction.
	 */
	public void setHash(String hash);
	
	/**
	 * Ask the object to update its hash.
	 * The hash shall be computed from transaction date, value and original usage text of a transaction.
	 */
	public void updateHash();

	/**
	 * Returns the text.
	 * @return the text
	 */
	public String getText();

	/**
	 * Sets the text.
	 * @param text the text to set
	 */
	public void setText(String text);

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
	 * Returns the partnerName.
	 * @return the partnerName
	 */
	public String getPartnerName();

	/**
	 * Sets the partnerName.
	 * @param partnerName the partnerName to set
	 */
	public void setPartnerName(String partnerName);

	/**
	 * Returns the partnerBank.
	 * @return the partnerBank
	 */
	public String getPartnerBank();

	/**
	 * Sets the partnerBank.
	 * @param partnerBank the partnerBank to set
	 */
	public void setPartnerBank(String partnerBank);

	/**
	 * Returns the partnerAccountNumber.
	 * @return the partnerAccountNumber
	 */
	public String getPartnerAccountNumber();

	/**
	 * Sets the partnerAccountNumber.
	 * @param partnerAccountNumber the partnerAccountNumber to set
	 */
	public void setPartnerAccountNumber(String partnerAccountNumber);

	/**
	 * Returns the transactionDate. This is the date for the accounting into a certain
	 * plan or budget. Compare to {@link #getValueDate() value date}.
	 * @return the transactionDate
	 */
	public RsDate getTransactionDate();

	/**
	 * Sets the transactionDate. This is the date for the accounting into a certain
	 * plan or budget. Compare to {@link #getValueDate() value date}.
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(RsDate transactionDate);

	/**
	 * Returns the valueDate. This is the date the amount was credited/debited
	 * to the account. Compare to {@link #getTransactionDate() transaction date}.
	 * @return the valueDate
	 */
	public RsDate getValueDate();

	/**
	 * Sets the valueDate. This is the date the amount was credited/debited
	 * to the account. Compare to {@link #getTransactionDate() transaction date}.
	 * @param valueDate the valueDate to set
	 */
	public void setValueDate(RsDate valueDate);

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
	 * Returns the plannedTransaction.
	 * @return the plannedTransaction
	 */
	public PlannedTransaction getPlannedTransaction();

	/**
	 * Sets the plannedTransaction.
	 * @param plannedTransaction the plannedTransaction to set
	 */
	public void setPlannedTransaction(PlannedTransaction plannedTransaction);

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
}
