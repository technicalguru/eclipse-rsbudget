/**
 * 
 */
package rsbudget.parts.history;

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
	
	/**
	 * Constructor.
	 */
	public HistoryLabelProvider(int index, LocalResourceManager resourceManager) {
		super(null, resourceManager);
		this.index = index;
		CommonUtils.SIMPLE_NUMBER_FORMATTER().setMaximumFractionDigits(2);
		CommonUtils.SIMPLE_NUMBER_FORMATTER().setMinimumFractionDigits(2);
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
			else if (record.isFloat(index)) rc = CommonUtils.SIMPLE_NUMBER_FORMATTER().format(value)+" "+unit;
			else rc = CommonUtils.SIMPLE_INT_FORMATTER().format(value)+" "+unit;
			
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
