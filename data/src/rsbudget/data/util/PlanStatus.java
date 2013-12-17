/**
 * 
 */
package rsbudget.data.util;

import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.LoggerFactory;

import rs.baselib.lang.LangUtils;
import rs.baselib.util.DateTimePeriod;
import rs.baselib.util.RsDate;
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
	private float statusStart;
	private float currentStatus;
	private float statusEnd;
	private float openItems;
	private float plannedExpenses;
	private float actualExpenses;
	private float relativeBalance;
	private float absoluteBalance;
	private float plannedBalance;
	private float actualBalance;
	private float plannedIncome;
	private float actualIncome;
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
		currentStatus   = 0;
		statusEnd       = 0;
		openItems       = 0;
		plannedExpenses = 0;
		actualExpenses  = 0;
		relativeBalance = 0;
		absoluteBalance = 0;
		plannedBalance  = 0;
		actualBalance   = 0;
		plannedIncome   = 0;
		actualIncome    = 0;
		boolean doForecast = true;

		doForecast = plan.getMonth().getEnd().after(new GregorianCalendar());

		// Add budgets to the plan
		for (Budget budget: plan.getBudgets()) {
			float amount = budget.getPlanned();
			float budgetOpen = 0;
			
			// What is planned on this budget
			float bPlanned = budget.getPlanned();
			for (PlannedTransaction ptx : budget.getPlannedTransactions()) {
				float f = ptx.getAmount();
				if (f < 0) plannedExpenses += f;
				else       plannedIncome   += f;
				if (ptx.getTransaction() == null) budgetOpen += f;
			}
			
			// If planned is more than budget then add this difference
			if (amount < 0) {
				if (bPlanned > amount) plannedExpenses += (amount-bPlanned);
				else amount = bPlanned; // for openItems computation
			} else {
				if (bPlanned < amount) plannedIncome   += (amount-bPlanned);
				else amount = bPlanned; // for openItems computation
			}
			
			// How much of this budget has been spent?
//			int spent = 0;
//			for (Transaction tx : budget.getTransactions()) {
//				spent += tx.getAmount();
//			}
			
			// Open items
			float budgetAvailable = budget.getAvailable();
			//System.out.println(budget.getName()+": planned="+bPlanned+" spent="+spent+" open="+budgetOpen+" available="+budgetAvailable);
			if (budgetAvailable != 0) {
				if (amount < 0) {
					// This was an expenses budget
					if (budgetOpen < budgetAvailable) {
						// Open transactions exceed available budget: add all open
						openItems += budgetOpen;
						//System.out.println("    adding open");
					} else {
						// Open transactions fit in available: add available
						openItems += budgetAvailable;
						//System.out.println("    adding available");
					}
				}
			} else {
				// No budget available: add open transactions
				openItems += budgetOpen;
				//System.out.println("    adding open");
			}
			//openItems += budget.getAvailable();
		}

		// Add other TX to the plan
		for (PlannedTransaction tx : plan.getPlannedTransactions()) {
			// If there is already a budget, its included
			if (tx.getBudget() == null) {
				float amount = tx.getAmount();
				if (amount < 0) plannedExpenses += amount;
				else            plannedIncome   += amount;

				// If not actual yet, add to open items
				if (tx.getTransaction() == null) {
					openItems += amount;
				}
			}
		}

		// Add all TX to the plan
		for (Transaction tx : plan.getTransactions()) {
			float amount = tx.getAmount();
			if (amount < 0) actualExpenses += amount;
			else            actualIncome   += amount;
		}

		// No open items anymore if the end status is set
		if (plan.getBalanceEnd() != null) openItems = 0;
		
		// Some totals
		plannedBalance = plannedIncome + plannedExpenses;
		actualBalance  = actualIncome  + actualExpenses;
		statusEnd      = statusStart + actualBalance;
		if (doForecast) statusEnd += openItems;
		if (plan.getBalanceEnd() != null) statusEnd = plan.getBalanceEnd();
		absoluteBalance = statusEnd - getProfitLossThreshold();
		relativeBalance = statusEnd - statusStart;
		currentStatus = doForecast ? statusStart + actualBalance : statusEnd;
		factory.commit();
	}

	/**
	 * Returns the absolute limit for balance.
	 * @return the limit
	 */
	public float getProfitLossThreshold() {
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
		if (profitLossThreshold != null) return LangUtils.getFloat(profitLossThreshold.getValue(), 0f);
		return 0f;
	}
	
	/**
	 * Compute the correct start of the month.
	 * @return account balance at begin of month
	 */
	protected float computeStatusStart() {
		float rc = 0;
		// 1. Use the status from this month
		if (plan.getBalanceStart() != null) return plan.getBalanceStart().floatValue();
		
		// 2. Use the status from last month
		Plan prev = factory.getPlanDAO().findBy(plan.getMonth().getPrevious());
		if (prev != null) {
			if (prev.getBalanceEnd() != null) return prev.getBalanceEnd();
			
			// Use account information if possible
			RsDate timestamp = plan.getMonth().getBegin();
			int usedAccounts = 0;
			float balance = 0;
			List<Account> relevantAccounts = factory.getAccountDAO().findRelevant();
			for (Account account : relevantAccounts) {
				AccountStatus status = factory.getAccountStatusDAO().findLatestBy(account, timestamp);
				// We can use this status if there was no transaction inbetween
				if (status != null) {
					RsDate t = status.getTimestamp();
					List<Transaction> tx = factory.getTransactionDAO().findBy(account, new DateTimePeriod(t, timestamp));
					if (tx.size() == 0) {
						usedAccounts++;
						balance += status.getBalance();
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
			rc += computeAccountStatus(account, plan.getMonth().getBegin());
		}
		return rc;
	}
	
	/**
	 * Calculates the account situation at given point in time.
	 * @param account account to be checked
	 * @param timestamp timestamp
	 * @return status at given timestamp
	 */
	protected float computeAccountStatus(Account account, RsDate timestamp) {
		float rc = 0;
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
			rc += tx.getAmount();
		}
		
		return rc;
	}
	
	/**
	 * Returns the statusStart.
	 * @return the statusStart
	 */
	public float getStatusStart() {
		return statusStart;
	}

	/**
	 * Returns the currentStatus.
	 * @return the currentStatus
	 */
	public float getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * Returns the statusEnd.
	 * @return the statusEnd
	 */
	public float getStatusEnd() {
		return statusEnd;
	}

	/**
	 * Returns the openItems.
	 * @return the openItems
	 */
	public float getOpenItems() {
		return openItems;
	}

	/**
	 * Returns the plannedExpenses.
	 * @return the plannedExpenses
	 */
	public float getPlannedExpenses() {
		return plannedExpenses;
	}

	/**
	 * Returns the actualExpenses.
	 * @return the actualExpenses
	 */
	public float getActualExpenses() {
		return actualExpenses;
	}

	/**
	 * Returns the relativeBalance.
	 * @return the relativeBalance
	 */
	public float getRelativeBalance() {
		return relativeBalance;
	}

	/**
	 * Returns the absoluteBalance.
	 * @return the absoluteBalance
	 */
	public float getAbsoluteBalance() {
		return absoluteBalance;
	}

	/**
	 * Returns the plannedBalance.
	 * @return the plannedBalance
	 */
	public float getPlannedBalance() {
		return plannedBalance;
	}

	/**
	 * Returns the actualBalance.
	 * @return the actualBalance
	 */
	public float getActualBalance() {
		return actualBalance;
	}

	/**
	 * Returns the plannedIncome.
	 * @return the plannedIncome
	 */
	public float getPlannedIncome() {
		return plannedIncome;
	}

	/**
	 * Returns the actualIncome.
	 * @return the actualIncome
	 */
	public float getActualIncome() {
		return actualIncome;
	}
	
	
}
