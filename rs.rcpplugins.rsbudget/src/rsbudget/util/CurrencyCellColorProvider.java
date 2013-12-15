/**
 * 
 */
package rsbudget.util;

import org.eclipse.swt.graphics.RGB;

import rs.e4.util.ICellColorProvider;
import rsbudget.preferences.PreferencesUtils;

/**
 * Provides appropriate colors (red and black) for currencies.
 * @author ralph
 *
 */
public class CurrencyCellColorProvider implements ICellColorProvider {

	/**
	 * 
	 */
	public CurrencyCellColorProvider() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RGB getForeground(Object element, Object cellValue) {
		if (cellValue != null) {
			if (cellValue instanceof Number) {
				if (((Number)cellValue).doubleValue() < 0) {
					return PreferencesUtils.getNegativeColor();
				}
			}
		}
		return PreferencesUtils.getPositiveColor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RGB getBackground(Object element, Object cellValue) {
		return null;
	}

}
