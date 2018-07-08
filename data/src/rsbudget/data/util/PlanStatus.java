/**
 * 
 */
package rsbudget.data.util;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.LoggerFactory;

import rs.baselib.util.DateTimePeriod;
import rs.baselib.util.RsDate;
import rsbaselib.util.RsCommonUtils;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.AccountStatus;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Setting;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.api.dao.SettingDAO;

/**
 * @author ralph
 *
 */
public class PlanStatus {

	private RsBudgetDaoFactory factory;
	private Plan plan;
	private BigDecimal statusStart;
	private BigDecimal currentStatus;
	private BigDecimal statusEnd;
	private BigDecimal openItems;
	private BigDecimal plannedExpenses;
	private BigDecimal actualExpenses;
	private BigDecimal relativeBalance;
	private BigDecimal absoluteBalance;
	private BigDecimal plannedBalance;
	private BigDecimal actualBalance;
	private BigDecimal plannedIncome;
	private BigDecimal actualIncome;
	private Setting profitLossThreshold = null;
	
	/**
	 * Constructor.
	 */
	public PlanStatus(Plan plan, RsBudgetDaoFactory factory) {
		this.factory = factory;
		this.plan = plan;
		if (plan != null) computeValues();
	}

	/**
	 * Returns true when this status is of current months.
	 * @return
	 */
	public boolean isCurrent() {
		if (plan == null) return false;
		long now = System.currentTimeMillis();
		long begin = plan.getMonth().getBegin().getTimeInMillis();
		long end = plan.getMonth().getEnd().getTimeInMillis();
		return (begin <= now) && (now <= end);
	}
	
	/**
	 * Calculate all relevant values.
	 */
	protected void computeValues() {
		factory.begin();
		statusStart     = computeStatusStart();
		currentStatus   = BigDecimal.ZERO;
		statusEnd       = BigDecimal.ZERO;
		openItems       = BigDecimal.ZERO;
		plannedExpenses = BigDecimal.ZERO;
		actualExpenses  = BigDecimal.ZERO;
		relativeBalance = BigDecimal.ZERO;
		absoluteBalance = BigDecimal.ZERO;
		plannedBalance  = BigDecimal.ZERO;
		actualBalance   = BigDecimal.ZERO;
		plannedIncome   = BigDecimal.ZERO;
		actualIncome    = BigDecimal.ZERO;
		boolean doForecast = true;

		doForecast = plan.getMonth().getEnd().after(new GregorianCalendar());

		// Add budgets to the plan
		for (Budget budget: plan.getBudgets()) {
			BigDecimal amount = budget.getPlanned();
			BigDecimal budgetOpen = BigDecimal.ZERO;
			
			// What is planned on this budget
			BigDecimal bPlanned = budget.getPlanned();
			for (PlannedTransaction ptx : budget.getPlannedTransactions()) {
				BigDecimal f = ptx.getAmount();
				if (f.signum() < 0) plannedExpenses = plannedExpenses.add(f);
				else       plannedIncome   = plannedIncome.add(f);
				if (ptx.getTransaction() == null) budgetOpen = budgetOpen.add(f);
			}
			
			// If planned is more than budget then add this difference
			if (amount.signum() < 0) {
				if (bPlanned.compareTo(amount) > 0) plannedExpenses = plannedExpenses.add(amount).subtract(bPlanned);
				else amount = bPlanned; // for openItems computation
			} else {
				if (bPlanned.compareTo(amount) < 0) plannedIncome   = plannedIncome.add(amount).subtract(bPlanned);
				else amount = bPlanned; // for openItems computation
			}
			
			// How much of this budget has been spent?
//			int spent = 0;
//			for (Transaction tx : budget.getTransactions()) {
//				spent += tx.getAmount();
//			}
			
			// Open items
			BigDecimal budgetAvailable = budget.getAvailable();
			//System.out.println(budget.getName()+": planned="+bPlanned+" spent="+spent+" open="+budgetOpen+" available="+budgetAvailable);
			if (budgetAvailable.signum() != 0) {
				if (amount.signum() < 0) {
					// This was an expenses budget
					if (budgetOpen.compareTo(budgetAvailable) < 0) {
						// Open transactions exceed available budget: add all open
						openItems = openItems.add(budgetOpen);
						//System.out.println("    adding open");
					} else {
						// Open transactions fit in available: add available
						openItems = openItems.add(budgetAvailable);
						//System.out.println("    adding available");
					}
				}
			} else {
				// No budget available: add open transactions
				openItems = openItems.add(budgetOpen);
				//System.out.println("    adding open");
			}
			//openItems += budget.getAvailable();
		}

		// Add other TX to the plan
		for (PlannedTransaction tx : plan.getPlannedTransactions()) {
			// If there is already a budget, its included
			if (tx.getBudget() == null) {
				BigDecimal amount = tx.getAmount();
				if (amount.signum() < 0) plannedExpenses = plannedExpenses.add(amount);
				else                     plannedIncome   = plannedIncome.add(amount);

				// If not actual yet, add to open items
				if (tx.getTransaction() == null) {
					openItems = openItems.add(amount);
				}
			}
		}

		// Add all TX to the plan
		for (Transaction tx : plan.getTransactions()) {
			BigDecimal amount = tx.getAmount();
			if (amount.signum() < 0) actualExpenses = actualExpenses.add(amount);
			else                     actualIncome   = actualIncome.add(amount);
		}

		// No open items anymore if the end status is set
		if (plan.getBalanceEnd() != null) openItems = BigDecimal.ZERO;
		
		// Some totals
		plannedBalance = plannedIncome.add(plannedExpenses);
		actualBalance  = actualIncome.add(actualExpenses);
		statusEnd      = statusStart.add(actualBalance);
		if (doForecast) statusEnd = statusEnd.add(openItems);
		if (plan.getBalanceEnd() != null) statusEnd = plan.getBalanceEnd();
		absoluteBalance = statusEnd.subtract(getProfitLossThreshold());
		relativeBalance = statusEnd.subtract(statusStart);
		currentStatus = doForecast ? statusStart.add(actualBalance) : statusEnd;
		factory.commit();
	}

	/**
	 * Returns the absolute limit for balance.
	 * @return the limit
	 */
	public BigDecimal getProfitLossThreshold() {
		if (profitLossThreshold == null) {
			try {
				factory.begin();
				SettingDAO dao = factory.getSettingDAO();
				profitLossThreshold = dao.findByKey(SettingDAO.KEY_PROFIT_LOSS_THRESHOLD);
				if (profitLossThreshold == null) {
					profitLossThreshold = dao.newInstance();
					profitLossThreshold.setKey(SettingDAO.KEY_PROFIT_LOSS_THRESHOLD);
					profitLossThreshold.setValue("0");
					dao.create(profitLossThreshold);
				}
				factory.commit();
				// TODO react on changes!
			} catch (Exception e) {
				LoggerFactory.getLogger(getClass()).error("Cannot get threshold:", e);
				try {
					factory.rollback();
				} catch (Exception e2) {}
			}
		}
		if (profitLossThreshold != null) return RsCommonUtils.getBigDecimal(profitLossThreshold.getValue(), BigDecimal.ZERO);
		return BigDecimal.ZERO;
	}
	
	/**
	 * Compute the correct start of the month.
	 * @return account balance at begin of month
	 */
	protected BigDecimal computeStatusStart() {
		BigDecimal rc = BigDecimal.ZERO;
		// 1. Use the status from this month
		if (plan.getBalanceStart() != null) return plan.getBalanceStart();
		
		// 2. Use the status from last month
		Plan prev = factory.getPlanDAO().findBy(plan.getMonth().getPrevious());
		if (prev != null) {
			if (prev.getBalanceEnd() != null) return prev.getBalanceEnd();
			
			// Use account information if possible
			RsDate timestamp = plan.getMonth().getBegin();
			int usedAccounts = 0;
			BigDecimal balance = BigDecimal.ZERO;
			List<Account> relevantAccounts = factory.getAccountDAO().findRelevant();
			for (Account account : relevantAccounts) {
				AccountStatus status = factory.getAccountStatusDAO().findLatestBy(account, timestamp);
				// We can use this status if there was no transaction inbetween
				if (status != null) {
					RsDate t = status.getTimestamp();
					List<Transaction> tx = factory.getTransactionDAO().findBy(account, new DateTimePeriod(t, timestamp));
					if (tx.size() == 0) {
						usedAccounts++;
						balance = balance.add(status.getBalance());
					}
				}
			}
			if (usedAccounts == relevantAccounts.size()) return balance;
			
			// Let the previous month calculate here
			PlanStatus status = new PlanStatus(prev, factory);
			return status.getStatusEnd();
		}
		
		// 3. Use account status information
		for (Account account : factory.getAccountDAO().findRelevant()) {
			// What is status at given point in time?
			rc = rc.add(computeAccountStatus(account, plan.getMonth().getBegin()));
		}
		return rc;
	}
	
	/**
	 * Calculates the account situation at given point in time.
	 * @param account account to be checked
	 * @param timestamp timestamp
	 * @return status at given timestamp
	 */
	protected BigDecimal computeAccountStatus(Account account, RsDate timestamp) {
		BigDecimal rc = BigDecimal.ZERO;
		RsDate lastTimestamp = new RsDate(0);
		
		// 1. Last status information before timestamp
		AccountStatus status = factory.getAccountStatusDAO().findLatestBy(account, timestamp);
		if (status != null) {
			rc = status.getBalance();
			lastTimestamp = status.getTimestamp();
		}
		
		// 2. Add all transactions from remaining period
		List<Transaction> txList = factory.getTransactionDAO().findBy(account, new DateTimePeriod(lastTimestamp, timestamp));
		for (Transaction tx : txList) {
			rc = rc.add(tx.getAmount());
		}
		
		return rc;
	}
	
	/**
	 * Returns the statusStart.
	 * @return the statusStart
	 */
	public BigDecimal getStatusStart() {
		return statusStart;
	}

	/**
	 * Returns the currentStatus.
	 * @return the currentStatus
	 */
	public BigDecimal getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * Returns the statusEnd.
	 * @return the statusEnd
	 */
	public BigDecimal getStatusEnd() {
		return statusEnd;
	}

	/**
	 * Returns the openItems.
	 * @return the openItems
	 */
	public BigDecimal getOpenItems() {
		return openItems;
	}

	/**
	 * Returns the plannedExpenses.
	 * @return the plannedExpenses
	 */
	public BigDecimal getPlannedExpenses() {
		return plannedExpenses;
	}

	/**
	 * Returns the actualExpenses.
	 * @return the actualExpenses
	 */
	public BigDecimal getActualExpenses() {
		return actualExpenses;
	}

	/**
	 * Returns the relativeBalance.
	 * @return the relativeBalance
	 */
	public BigDecimal getRelativeBalance() {
		return relativeBalance;
	}

	/**
	 * Returns the absoluteBalance.
	 * @return the absoluteBalance
	 */
	public BigDecimal getAbsoluteBalance() {
		return absoluteBalance;
	}

	/**
	 * Returns the plannedBalance.
	 * @return the plannedBalance
	 */
	public BigDecimal getPlannedBalance() {
		return plannedBalance;
	}

	/**
	 * Returns the actualBalance.
	 * @return the actualBalance
	 */
	public BigDecimal getActualBalance() {
		return actualBalance;
	}

	/**
	 * Returns the plannedIncome.
	 * @return the plannedIncome
	 */
	public BigDecimal getPlannedIncome() {
		return plannedIncome;
	}

	/**
	 * Returns the actualIncome.
	 * @return the actualIncome
	 */
	public BigDecimal getActualIncome() {
		return actualIncome;
	}
	
	
}
