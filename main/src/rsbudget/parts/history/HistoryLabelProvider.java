/**
 * 
 */
package rsbudget.parts.history;

import java.text.DecimalFormat;

import org.eclipse.jface.resource.LocalResourceManager;

import rs.baselib.util.CommonUtils;
import rs.e4.util.AbstractColumnLabelProvider;

/**
 * Provides the label for the history.
 * @author ralph
 *
 */
public class HistoryLabelProvider extends AbstractColumnLabelProvider {

	private int index;
	private DecimalFormat formatter = new DecimalFormat();
	
	/**
	 * Constructor.
	 */
	public HistoryLabelProvider(int index, int precision, LocalResourceManager resourceManager) {
		super(null, resourceManager);
		this.index = index;
		formatter.setMaximumFractionDigits(precision);
		formatter.setMinimumFractionDigits(precision);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(Object element) {
		String rc = "";
		StatusRecord record = (StatusRecord)element;
		try {
			// Retrieve the value
			Number value = record.getValue(index);
			String unit = record.getUnit(index);
			
			if (value == null) rc = "";
			else rc = formatter.format(value)+" "+unit;
			
			return rc;
		} catch (Throwable t) {
			return getErrorValue(t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(Object element) {
		return ((StatusRecord)element).getValue(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getErrorValue(Throwable t) {
		return "$ERROR$";
	}
	
	
}
