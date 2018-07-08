/**
 * 
 */
package rsbudget.data.impl.bo;

import java.math.BigDecimal;

import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.util.RsDate;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.dto.AccountDTO;
import rsbudget.data.dto.TransactionDTO;

/**
 * Transaction BO wrapper
 * @author ralph
 *
 */
public class TransactionBO extends AbstractRsBudgetDbBO<TransactionDTO> implements Transaction {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6583979925888205023L;

	/**
	 * Constructor with empty DTO.
	 */
	public TransactionBO() {
		this(new TransactionDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public TransactionBO(TransactionDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHash() {
		return getTransferObject().getHash();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setHash(String hash) {
		String oldValue = getHash();
		getTransferObject().setHash(hash);
		firePropertyChange(PROPERTY_HASH, oldValue, hash);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateHash() {
		String hashed = getTransactionDate().getTimeInMillis()+":"+getAmount()+":"+getName();
		String hash = EncryptionUtils.encodeBase64(EncryptionUtils.createMD5(hashed));
		if (hash.length() > 255) hash = hash.substring(0, 255);
		setHash(hash);
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
		getTransferObject().setAccount((AccountDTO)getTransferObject((AccountBO)account));
		firePropertyChange(PROPERTY_ACCOUNT, oldValue, account);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText() {
		return getTransferObject().getText();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setText(String text) {
		String oldValue = getText();
		getTransferObject().setText(text);
		firePropertyChange(PROPERTY_TEXT, oldValue, text);
	}

	/**
	 * Returns the text of this transaction.
	 * @return text
	 * @see #getText()
	 */
	public String getName() {
		return getText();
	}
	
	/**
	 * Sets the text of this transaction.
	 * @param name text
	 * @see #setText(String)
	 */
	public void setName(String name) {
		setText(name);
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
	public String getPartnerName() {
		return getTransferObject().getPartnerName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPartnerName(String partnerName) {
		String oldValue = getPartnerName();
		getTransferObject().setPartnerName(partnerName);
		firePropertyChange(PROPERTY_PARTNER_NAME, oldValue, partnerName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPartnerBank() {
		return getTransferObject().getPartnerBank();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPartnerBank(String partnerBank) {
		String oldValue = getPartnerBank();
		getTransferObject().setPartnerBank(partnerBank);
		firePropertyChange(PROPERTY_PARTNER_BANK, oldValue, partnerBank);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPartnerAccountNumber() {
		return getTransferObject().getPartnerAccountNumber();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPartnerAccountNumber(String partnerAccountNumber) {
		String oldValue = getPartnerAccountNumber();
		getTransferObject().setPartnerAccountNumber(partnerAccountNumber);
		firePropertyChange(PROPERTY_PARTNER_ACCOUNT_NUMBER, oldValue, partnerAccountNumber);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getTransactionDate() {
		return getTransferObject().getTransactionDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTransactionDate(RsDate transactionDate) {
		RsDate oldValue = getTransactionDate();
		getTransferObject().setTransactionDate(transactionDate);
		firePropertyChange(PROPERTY_TRANSACTION_DATE, oldValue, transactionDate);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getValueDate() {
		return getTransferObject().getValueDate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueDate(RsDate valueDate) {
		RsDate oldValue = getValueDate();
		getTransferObject().setValueDate(valueDate);
		firePropertyChange(PROPERTY_VALUE_DATE, oldValue, valueDate);
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
	public PlannedTransaction getPlannedTransaction() {
		return (PlannedTransaction)getBusinessObject(getTransferObject().getPlannedTransaction());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlannedTransaction(PlannedTransaction plannedTransaction) {
		PlannedTransaction oldValue = getPlannedTransaction();
		if (oldValue != null) oldValue.setTransaction(null);
		if (plannedTransaction != null) {
			getTransferObject().setPlannedTransaction(((PlannedTransactionBO)plannedTransaction).getTransferObject());
			plannedTransaction.setTransaction(this);
		} else {
			getTransferObject().setPlannedTransaction(null);
		}
		firePropertyChange(PROPERTY_PLANNED_TRANSACTION, oldValue, plannedTransaction);
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
		if (oldValue != null) oldValue.removeTransaction(this);
		if (budget != null) {
			getTransferObject().setBudget(((BudgetBO)budget).getTransferObject());
			budget.addTransaction(this);
		} else {
			getTransferObject().setBudget(null);
		}
		firePropertyChange(PROPERTY_BUDGET, oldValue, budget);
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
		if (oldValue != null) oldValue.removeTransaction(this);
		if (plan != null) {
			getTransferObject().setPlan(((PlanBO)plan).getTransferObject());
			plan.addTransaction(this);
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
	public String toString() {
		return super.toString()+"("+getName()+")";
	}

	
}
