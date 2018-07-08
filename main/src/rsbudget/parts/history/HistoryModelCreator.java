/**
 * 
 */
package rsbudget.parts.history;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableColumn;

import rs.baselib.util.RsDay;
import rs.baselib.util.RsMonth;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.AccountStatus;
import rsbudget.data.api.bo.HistoricalItem;
import rsbudget.data.api.bo.HistoricalItemStatus;
import rsbudget.data.api.bo.Plan;
import rsbudget.preferences.PreferencesUtils;

/**
 * Creates the history model.
 * @author ralph
 *
 */
public class HistoryModelCreator {

	private RsBudgetDaoFactory factory;
	private TableViewer viewer;

	/**
	 * Constructor.
	 */
	public HistoryModelCreator(RsBudgetDaoFactory factory, TableViewer viewer) {
		this.factory = factory;
		this.viewer = viewer;
	}

	public List<StatusRecord> retrieveRecords() {
		try {
			factory.begin();
			Map<RsDay, Map<Account,AccountStatus>> acctStatus = getAccountStatus();
			Map<RsDay, Map<HistoricalItem,HistoricalItemStatus>> itemStatus = getHistoricalItemStatus();
			Map<RsDay, Plan> plans = getPlans();
			
			List<StatusRecord> rc = new ArrayList<StatusRecord>();
			TableColumn columns[] = viewer.getTable().getColumns();
			
			// Start with lowest month available
			RsMonth month = getFirstMonth(acctStatus, itemStatus, plans);
			RsMonth lastMonth = new RsMonth(System.currentTimeMillis()).getNext();
			while (!month.after(lastMonth)) {
				StatusRecord row = makeRow(month, columns, acctStatus, itemStatus, plans);
				rc.add(row);
				month = month.getNext();
			}
			factory.commit();
			return rc;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * Retrieve all status information from accounts.
	 * @return status information
	 */
	protected Map<RsDay, Map<Account,AccountStatus>> getAccountStatus() {
		Map<RsDay, Map<Account,AccountStatus>> rc = new HashMap<RsDay, Map<Account,AccountStatus>>();
		for (AccountStatus status : factory.getAccountStatusDAO().findDefaultAll()) {
			RsDay day = status.getTimestamp().getDay();
			Map<Account,AccountStatus> acctStatus = rc.get(day);
			if (acctStatus == null) {
				acctStatus = new HashMap<Account, AccountStatus>();
				rc.put(day, acctStatus);
			}
			acctStatus.put(status.getAccount(), status);
		}
		return rc;
	}
	
	/**
	 * Retrieve all status information from historical information.
	 * @return status information
	 */
	protected Map<RsDay, Map<HistoricalItem,HistoricalItemStatus>> getHistoricalItemStatus() {
		Map<RsDay, Map<HistoricalItem,HistoricalItemStatus>> rc = new HashMap<RsDay, Map<HistoricalItem,HistoricalItemStatus>>();
		for (HistoricalItemStatus status : factory.getHistoricalItemStatusDAO().findDefaultAll()) {
			RsDay day = status.getTimestamp().getDay();
			Map<HistoricalItem,HistoricalItemStatus> itemStatus = rc.get(day);
			if (itemStatus == null) {
				itemStatus = new HashMap<HistoricalItem, HistoricalItemStatus>();
				rc.put(day, itemStatus);
			}
			itemStatus.put(status.getItem(), status);
		}
		return rc;
	}
	
	/**
	 * Retrieve all status information from historical information.
	 * @return status information
	 */
	protected Map<RsDay, Plan> getPlans() {
		Map<RsDay, Plan> rc = new HashMap<RsDay, Plan>();
		for (Plan plan : factory.getPlanDAO().findDefaultAll()) {
			RsDay day = plan.getMonth().getBegin().getDay();
			rc.put(day, plan);
		}
		return rc;
	}

	/**
	 * Create the row for the given month
	 * @param month
	 * @param columns
	 * @return
	 */
	protected StatusRecord makeRow(RsMonth month, TableColumn columns[], 
			Map<RsDay, Map<Account,AccountStatus>> acctStatus,
			Map<RsDay, Map<HistoricalItem,HistoricalItemStatus>> itemStatus,
			Map<RsDay, Plan> plans) {
		
		StatusRecord row = new StatusRecord(month);
		for (int i=1; i<columns.length; i++) {
			String purpose = (String)columns[i].getData(HistoryPart.KEY_PURPOSE);
			Object o = columns[i].getData(HistoryPart.KEY_OBJECT);
			if (purpose.equals(HistoryPart.VALUE_VALUE)) {
				if (o == null) {
					// The plan status
					Plan plan = plans.get(month.getBegin().getDay());
					if (plan != null) row.addValue(plan.getBalanceStart(), "\u20ac", false, true);
					else row.addEmpty(false, true);
				} else if (o instanceof Account) {
					AccountStatus status = get(acctStatus, (Account)o, month);
					if (status != null) row.addValue(status.getBalance(), "\u20ac", true, true);
					else row.addEmpty(true, true);
				} else if (o instanceof HistoricalItem) {
					HistoricalItemStatus status = get(itemStatus, (HistoricalItem)o, month);
					if (status != null) {
						BigDecimal value = status.getValue();
						if (((HistoricalItem)o).isFloatValue()) row.addValue(value, ((HistoricalItem)o).getUnit(), true, true);
						else row.addValue(value.longValue(), ((HistoricalItem)o).getUnit(), true, false);
					} else row.addEmpty(true, ((HistoricalItem)o).isFloatValue());
				}
			} else if (purpose.equals(HistoryPart.VALUE_CHANGE)) {
				if (o == null) {
					// The plan change
					BigDecimal status1 = null;
					Plan plan1 = plans.get(month.getPrevious().getBegin().getDay());
					if (plan1 != null) status1 = plan1.getBalanceStart();
					BigDecimal status2 = null;
					Plan plan2 = plans.get(month.getBegin().getDay());
					if (plan2 != null) status2 = plan2.getBalanceStart();
					row.addValue(getChange(status1, status2), PreferencesUtils.getCurrency().getSymbol(), false, true);
				} else if (o instanceof Account) {
					BigDecimal status1 = null;
					AccountStatus statusRow1 = get(acctStatus, (Account)o, month.getPrevious());
					if (statusRow1 != null) status1 = statusRow1.getBalance();
					BigDecimal status2 = null;
					AccountStatus statusRow2 = get(acctStatus, (Account)o, month);
					if (statusRow2 != null) status2 = statusRow2.getBalance();
					row.addValue(getChange(status1, status2), PreferencesUtils.getCurrency().getSymbol(), false, true);
				} else if (o instanceof HistoricalItem) {
					BigDecimal status1 = null;
					HistoricalItemStatus statusRow1 = get(itemStatus, (HistoricalItem)o, month.getPrevious());
					if (statusRow1 != null) status1 = statusRow1.getValue();
					BigDecimal status2 = null;
					HistoricalItemStatus statusRow2 = get(itemStatus, (HistoricalItem)o, month);
					if (statusRow2 != null) status2 = statusRow2.getValue();
					row.addValue(getChange(status1, status2, ((HistoricalItem)o).isFloatValue()), ((HistoricalItem)o).getUnit(), false, ((HistoricalItem)o).isFloatValue());
				}
			}
		}
		return row;
	}
	
	/**
	 * Returns the status for the given account and month from the map.
	 * @param acctStatus status map
	 * @param account account
	 * @param month month
	 * @return status of that account in that month
	 */
	protected AccountStatus get(Map<RsDay, Map<Account,AccountStatus>> acctStatus, Account account, RsMonth month) {
		Map<Account,AccountStatus> subMap = acctStatus.get(month.getBegin().getDay());
		if (subMap != null) return subMap.get(account);
		return null;
	}
	
	/**
	 * Returns the status for the given item and month from the map.
	 * @param acctStatus status map
	 * @param item item
	 * @param month month
	 * @return status of that item in that month
	 */
	protected HistoricalItemStatus get(Map<RsDay, Map<HistoricalItem,HistoricalItemStatus>> itemStatus, HistoricalItem item, RsMonth month) {
		Map<HistoricalItem,HistoricalItemStatus> subMap = itemStatus.get(month.getBegin().getDay());
		if (subMap != null) return subMap.get(item);
		return null;
	}

	/**
	 * Returns the first available month with a status.
	 * @return
	 */
	protected RsMonth getFirstMonth(Map<RsDay, ?> map1, Map<RsDay, ?> map2, Map<RsDay, ?> map3) {
		RsMonth rc = new RsMonth();
		for (RsDay day : map1.keySet()) {
			if (day.getMonth().before(rc)) rc = day.getMonth();
		}
		for (RsDay day : map2.keySet()) {
			if (day.getMonth().before(rc)) rc = day.getMonth();
		}
		for (RsDay day : map3.keySet()) {
			if (day.getMonth().before(rc)) rc = day.getMonth();
		}
		
		return rc;
	}
	
	/**
	 * Returns the difference of two values that can be potentially null.
	 * @param f1 first value
	 * @param f2 second value
	 * @return change
	 */
	public Number getChange(BigDecimal f1, BigDecimal f2) {
		return getChange(f1, f2, true);
	}
	
	/**
	 * Returns the difference of two values that can be potentially null.
	 * @param f1 first value
	 * @param f2 second value
	 * @return change
	 */
	public Number getChange(BigDecimal f1, BigDecimal f2, boolean isFloat) {
		if (f1 == null) return makeNumber(f2, isFloat);
		if (f2 == null) return null;
		return makeNumber(f2.subtract(f1), isFloat);
	}
	
	/**
	 * Makes a long out of the float if required.
	 * @param f float
	 * @param isFloat shall it be a float?
	 * @return long or float (or null)
	 */
	protected Number makeNumber(BigDecimal f, boolean isFloat) {
		if (f == null) return null;
		if (isFloat) return f;
		return Long.valueOf(f.longValue());
	}
	

}
