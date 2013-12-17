/**
 * 
 */
package rsbudget.parts.history;

import org.apache.commons.lang.time.DateUtils;

import rs.baselib.util.RsDate;
import rs.e4.celledit.BeanEditingSupportModel;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.AccountStatus;
import rsbudget.data.api.bo.HistoricalItem;
import rsbudget.data.api.bo.HistoricalItemStatus;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.preferences.PreferencesUtils;

/**
 * A slightly modified version of {@link BeanEditingSupportModel}
 * that takes care of creation of status records if required.
 * @author ralph
 *
 */
public class HistoryEditingSupportModel extends BeanEditingSupportModel {

	private int columnIndex;
	private RsBudgetBO<?> businessObject;
	private RsBudgetDaoFactory factory;
	
	/**
	 * Constructor.
	 * @param property the property to support.
	 */
	public HistoryEditingSupportModel(RsBudgetBO<?> businessObject, int columnIndex) {
		super("nothing");
		this.businessObject = businessObject;
		this.columnIndex = columnIndex;
		this.factory = (RsBudgetDaoFactory)businessObject.getDao().getFactory();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canEdit(Object element) {
		if (element instanceof StatusRecord) return ((StatusRecord)element).isEditable(columnIndex);
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object _getValue(Object element) throws Exception  {
		if (element instanceof StatusRecord) return ((StatusRecord)element).getValue(columnIndex);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void _setValue(Object element, Object value) throws Exception {
		if (element instanceof StatusRecord) {
			StatusRecord row = (StatusRecord)element;
			RsDate timestamp = row.getTimestamp().getBegin();
			RsDate previousTimestamp = row.getTimestamp().getPrevious().getBegin();
			String unit = row.getUnit(columnIndex);
			factory.begin();
			if (businessObject instanceof Account) {
				Account account = (Account)businessObject;
				AccountStatus status = factory.getAccountStatusDAO().findNearest(account, timestamp, DateUtils.MILLIS_PER_DAY);
				AccountStatus previous = factory.getAccountStatusDAO().findNearest(account, previousTimestamp, DateUtils.MILLIS_PER_DAY);			
				if (status != null) {
					// Update the value
					if (value != null) {
						status.setBalance((Float)value);
						factory.getAccountStatusDAO().save(status);
					} else {
						factory.getAccountStatusDAO().delete(status);
					}
				} else {
					// Create the value
					if (value != null) {
						status = factory.getAccountStatusDAO().newInstance();
						status.setTimestamp(timestamp);
						status.setAccount(account);
						status.setBalance((Float)value);
						factory.getAccountStatusDAO().create(status);
					}
				}
				unit = PreferencesUtils.getCurrency().getSymbol();
				// The difference
				if (previous != null) {
					row.setValue(columnIndex+1, status.getBalance() - previous.getBalance());
					row.setUnit(columnIndex+1, unit);
				}
			} else if (businessObject instanceof HistoricalItem) {
				HistoricalItem item = (HistoricalItem)businessObject;
				HistoricalItemStatus status = factory.getHistoricalItemStatusDAO().findNearest(item, timestamp, DateUtils.MILLIS_PER_DAY);
				HistoricalItemStatus previous = factory.getHistoricalItemStatusDAO().findNearest(item, previousTimestamp, DateUtils.MILLIS_PER_DAY);
				if (status != null) {
					// Update the value
					if (value != null) {
						status.setValue((Float)value);
						factory.getHistoricalItemStatusDAO().save(status);
					} else {
						factory.getHistoricalItemStatusDAO().delete(status);
					}
				} else {
					// Create the value
					if (value != null) {
						status = factory.getHistoricalItemStatusDAO().newInstance();
						status.setTimestamp(timestamp);
						status.setItem(item);
						status.setValue((Float)value);
						factory.getHistoricalItemStatusDAO().create(status);
					}
				}
				unit = item.getUnit();
				if (item.isShowChanges() && (previous != null)) {
					row.setValue(columnIndex+1, status.getValue()-previous.getValue());
					row.setUnit(columnIndex+1, unit);
				}
			}
			factory.commit();
			row.setValue(columnIndex, (Number)value);
			row.setUnit(columnIndex, unit);
		}
	}


}
