/**
 * 
 */
package rsbudget.util;

import java.text.NumberFormat;

import org.eclipse.jface.viewers.LabelProvider;

import rsbudget.preferences.PreferencesUtils;

/**
 * Formats with currency.
 * @author ralph
 *
 */
public class CurrencyLabelProvider extends LabelProvider {

	public static final CurrencyLabelProvider INSTANCE = new CurrencyLabelProvider();
	
	private NumberFormat formatter;
	
	/**
	 * Constructor.
	 */
	public CurrencyLabelProvider() {
		update();
	}
	
	/**
	 * Creates the formatter.
	 */
	public void update() {
		formatter = NumberFormat.getCurrencyInstance(PreferencesUtils.getLocale());
		formatter.setCurrency(PreferencesUtils.getCurrency());
	}

	@Override
	public String getText(Object element) {
		if (element == null) return "";
		if (element instanceof Number) {
			return formatter.format(((Number)element).doubleValue());
		}
		return "$"+element.toString()+"$";
	}

}
