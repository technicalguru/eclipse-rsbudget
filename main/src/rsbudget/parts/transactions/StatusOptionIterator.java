/**
 * 
 */
package rsbudget.parts.transactions;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import rs.baselib.util.DateTimePeriod;
import rs.baselib.util.RsDate;
import rs.baselib.util.RsMonth;
import rsbudget.Plugin;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.AccountStatus;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.util.PlanStatus;
import rsbudget.parts.transactions.SetBalanceForm.OptionDescriptor;
import rsbudget.util.CurrencyLabelProvider;

/**
 * Iterate over possible status options for begin/end of the given month
 * @author ralph
 *
 */
public class StatusOptionIterator implements Iterator<OptionDescriptor>{

	private Plan plan;
	private boolean doBegin;
	private List<OptionDescriptor> options;
	private int index = 0;
	private RsBudgetDaoFactory factory;
	
	/**
	 * Constructor.
	 * @param plan plan to be handled
	 * @param doBegin true when option for begin of plan shall be created, false for end of plan
	 */
	public StatusOptionIterator(Plan plan, boolean doBegin) {
		this.plan = plan;
		this.doBegin = doBegin;
		factory = (RsBudgetDaoFactory)plan.getDao().getFactory();
		
		createOptions();
	}

	/**
	 * Create the options.
	 */
	protected void createOptions() {
		options = new ArrayList<SetBalanceForm.OptionDescriptor>();
		factory.begin();
		String text = null;
		BigDecimal amount = BigDecimal.ZERO;
		boolean recommended = true;
		
		// Retrieve next/previous plan
		RsMonth month = plan.getMonth();
		Plan adjacentPlan = null;
		RsMonth adjacentMonth = null;
		if (doBegin) adjacentMonth = month.getPrevious();
		else adjacentMonth = month.getNext();
		adjacentPlan = factory.getPlanDAO().findBy(adjacentMonth);
		
		// Option: adjacent plan
		text = null;
		if (adjacentPlan != null) {
			if (doBegin && (adjacentPlan.getBalanceEnd() != null)) {
				text = Plugin.translate("part.transactions.dialog.balances.label.status.on", format(adjacentMonth.getEnd()), format(adjacentPlan.getBalanceEnd()));
				amount = adjacentPlan.getBalanceEnd();
			} else if (!doBegin && (adjacentPlan.getBalanceStart() != null)) {
				text = Plugin.translate("part.transactions.dialog.balances.label.status.on", format(adjacentMonth.getBegin()), format(adjacentPlan.getBalanceStart()));
				amount = adjacentPlan.getBalanceStart();
			}
		}
		if (text != null) {
			options.add(new OptionDescriptor(text, amount, recommended));
			text = null; recommended = false;
		}
		
		// Option: Account status records at given point in time
		BigDecimal f = getRecordedStatus(doBegin ? month.getBegin() : month.getEnd());
		if (f != null) {
			text = "Konto-Status zum "+format(doBegin ? month.getBegin() : month.getEnd())+": "+format(f);
			amount = f;
			OptionDescriptor option = new OptionDescriptor(text, amount, recommended);
			if (!options.contains(option)) options.add(option);
			text = null; recommended = false;
		}
		
		// Option: recorded status for given point in time
		if (doBegin && (adjacentPlan != null)) {
			PlanStatus status = new PlanStatus(adjacentPlan, factory);
			amount = status.getStatusEnd();
			text = Plugin.translate("part.transactions.dialog.balances.label.status.on", format(adjacentMonth.getEnd()), format(amount));
		} else if (!doBegin) {
			PlanStatus status = new PlanStatus(plan, factory);
			amount = status.getStatusEnd();
			text = Plugin.translate("part.transactions.dialog.balances.label.status.on", format(month.getEnd()), format(amount));
		}
		if (text != null) {
			OptionDescriptor option = new OptionDescriptor(text, amount, recommended);
			if (!options.contains(option)) options.add(option);
			text = null; recommended = false;
		}
		
		// Option: computed status from account status and transaction
		f = getComputedStatus(doBegin ? month.getBegin() : month.getEnd());
		if (f != null) {
			text = Plugin.translate("part.transactions.dialog.balances.label.status.computed.on", format(doBegin ? month.getBegin() : month.getEnd()), format(f));
			amount = f;
			OptionDescriptor option = new OptionDescriptor(text, amount, recommended);
			if (!options.contains(option)) options.add(option);
			text = null; recommended = false;
		}
		
		// Option: forecasted status for given point in time
		if (doBegin && (adjacentPlan != null)) {
			PlanStatus status = new PlanStatus(adjacentPlan, factory);
			amount = status.getStatusEnd().add(status.getOpenItems());
			text = Plugin.translate("part.transactions.dialog.balances.label.status.forecasted.on", format(adjacentMonth.getEnd()), format(amount));
		} else if (!doBegin) {
			PlanStatus status = new PlanStatus(plan, factory);
			amount = status.getStatusEnd().add(status.getOpenItems());
			text = Plugin.translate("part.transactions.dialog.balances.label.status.forecasted.on", format(month.getEnd()), format(amount));
		}
		if (text != null) {
			OptionDescriptor option = new OptionDescriptor(text, amount, recommended);
			if (!options.contains(option)) options.add(option);
			text = null; recommended = false;
		}
		
		factory.commit();
	}
	
	/**
	 * Returns the account status around 5 hours of point in time.
	 * @param timestamp
	 * @return null or found status
	 */
	protected BigDecimal getRecordedStatus(RsDate timestamp) {
		BigDecimal f = BigDecimal.ZERO;
		List<Account> accounts = factory.getAccountDAO().findRelevant();
		for (Account account : accounts) {
			AccountStatus status = factory.getAccountStatusDAO().findNearest(account, timestamp, 5*DateUtils.MILLIS_PER_HOUR);
			if (status != null) f = f.add(status.getBalance());
			else return null;
		}
		return f;
	};
	
	/**
	 * Returns the status as computed from account status and transactions recorded
	 * @param timestamp timestamp
	 * @return null or computed status
	 */
	protected BigDecimal getComputedStatus(RsDate timestamp) {
		int usedAccounts = 0;
		BigDecimal balance = BigDecimal.ZERO;
		List<Account> relevantAccounts = factory.getAccountDAO().findRelevant();
		for (Account account : relevantAccounts) {
			AccountStatus status = factory.getAccountStatusDAO().findLatestBy(account, timestamp);
			if (status != null) {
				usedAccounts++;
				// Base is this status
				balance = balance.add(status.getBalance());
				RsDate t = status.getTimestamp();
				
				// Add all transactions
				List<Transaction> txList = factory.getTransactionDAO().findBy(account, new DateTimePeriod(t, timestamp));
				for (Transaction tx : txList) {
					balance = balance.add(tx.getAmount());	
				}
			}
		}
		if (usedAccounts == relevantAccounts.size()) return balance;
		return null;
	}
	
	/**
	 * Format the date.
	 * @param date date to format
	 * @return formatted date
	 */
	protected String format(RsDate date) {
		return DateFormat.getDateInstance().format(date.getTime());
	}
	
	/**
	 * Format the money.
	 * @param f amount
	 * @return formatted amount
	 */
	protected String format(BigDecimal f) {
		return CurrencyLabelProvider.INSTANCE.getText(f);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return index < options.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OptionDescriptor next() {
		return options.get(index++);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("Removal not supported");
	}

	
}
