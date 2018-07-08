/**
 * 
 */
package rsbudget.data.dto;

import java.math.BigDecimal;

import rs.baselib.util.RsDate;

/**
 * A transaction.
 * @author ralph
 *
 */
public class TransactionDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 2132396052459415259L;

	private String hash;
	private AccountDTO account;
	private BigDecimal amount;
	private String text;
	private CategoryDTO category;
	private String partnerName;
	private String partnerBank;
	private String partnerAccountNumber;
	private RsDate transactionDate;
	private RsDate valueDate;
	private String annotation;
	private PlannedTransactionDTO plannedTransaction;
	private BudgetDTO budget;
	private PlanDTO plan;
	private int displayOrder;
	
	/**
	 * Constructor.
	 */
	public TransactionDTO() {
	}

	
	/**
	 * Returns the hash.
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}


	/**
	 * Sets the hash.
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
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
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Sets the amount.
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Returns the text.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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
	 * Returns the partnerName.
	 * @return the partnerName
	 */
	public String getPartnerName() {
		return partnerName;
	}

	/**
	 * Sets the partnerName.
	 * @param partnerName the partnerName to set
	 */
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	/**
	 * Returns the partnerBank.
	 * @return the partnerBank
	 */
	public String getPartnerBank() {
		return partnerBank;
	}

	/**
	 * Sets the partnerBank.
	 * @param partnerBank the partnerBank to set
	 */
	public void setPartnerBank(String partnerBank) {
		this.partnerBank = partnerBank;
	}

	/**
	 * Returns the partnerAccountNumber.
	 * @return the partnerAccountNumber
	 */
	public String getPartnerAccountNumber() {
		return partnerAccountNumber;
	}

	/**
	 * Sets the partnerAccountNumber.
	 * @param partnerAccountNumber the partnerAccountNumber to set
	 */
	public void setPartnerAccountNumber(String partnerAccountNumber) {
		this.partnerAccountNumber = partnerAccountNumber;
	}

	/**
	 * Returns the transactionDate.
	 * @return the transactionDate
	 */
	public RsDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Sets the transactionDate.
	 * @param transactionDate the transactionDate to set
	 */
	public void setTransactionDate(RsDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * Returns the valueDate.
	 * @return the valueDate
	 */
	public RsDate getValueDate() {
		return valueDate;
	}

	/**
	 * Sets the valueDate.
	 * @param valueDate the valueDate to set
	 */
	public void setValueDate(RsDate valueDate) {
		this.valueDate = valueDate;
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
	 * Returns the plannedTransaction.
	 * @return the plannedTransaction
	 */
	public PlannedTransactionDTO getPlannedTransaction() {
		return plannedTransaction;
	}

	/**
	 * Sets the plannedTransaction.
	 * @param plannedTransaction the plannedTransaction to set
	 */
	public void setPlannedTransaction(PlannedTransactionDTO plannedTransaction) {
		this.plannedTransaction = plannedTransaction;
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

}
