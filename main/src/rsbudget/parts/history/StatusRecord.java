/**
 * 
 */
package rsbudget.parts.history;

import java.util.ArrayList;
import java.util.List;

import rs.baselib.bean.AbstractBean;
import rs.baselib.util.RsDate;
import rs.baselib.util.RsMonth;

/**
 * Represents a row in the history view.
 * @author ralph
 *
 */
public class StatusRecord extends AbstractBean {
	
	public static final String PROPERTY_STATUS_DATE = "statusDate";
	
	private RsMonth timestamp;
	private List<Number> values;
	private List<String> units;
	private List<Boolean> editable;
	private List<Boolean> floats;
	private RsDate statusDate = new RsDate();
	
	public StatusRecord(RsMonth timestamp) {
		this.timestamp = timestamp;
		values = new ArrayList<Number>();
		units  = new ArrayList<String>();
		editable = new ArrayList<Boolean>();
		floats = new ArrayList<Boolean>();
	}
	
	public RsMonth getTimestamp() {
		return timestamp;
	}
	
	public void addEmpty(boolean isEditable, boolean isFloat) {
		addValue(null, null, isEditable, isFloat);
	}
	
	public void addValue(Number value, String unit, boolean isEditable, boolean isFloat) {
		values.add(value);
		units.add(value != null ? unit : null);
		editable.add(isEditable);
		floats.add(isFloat);
	}
	
	public Number getValue(int column) {
		return values.get(column);
	}
	
	public void setValue(int column, Number value) {
		values.set(column, value);
		setStatusDate(new RsDate());
	}
	
	public String getUnit(int column) {
		return units.get(column);
	}
	
	public void setUnit(int column, String unit) {
		while (units.size() <= column+1) units.add(null);
		units.set(column, unit);
		setStatusDate(new RsDate());
	}
	
	public boolean isEditable(int column) {
		return editable.get(column);
	}
	
	public boolean isFloat(int column) {
		return floats.get(column);
	}
	
	public int getColumnCount() {
		return values.size();
	}

	/**
	 * Returns the statusDate.
	 * @return the statusDate
	 */
	public RsDate getStatusDate() {
		return statusDate;
	}

	/**
	 * Sets the statusDate.
	 * @param statusDate the statusDate to set
	 */
	public void setStatusDate(RsDate statusDate) {
		RsDate oldValue = getStatusDate();
		this.statusDate = statusDate;
		firePropertyChange(PROPERTY_STATUS_DATE, oldValue, statusDate);
	}

	
}
