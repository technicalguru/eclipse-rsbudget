/**
 * 
 */
package rsbudget.util;

import org.eclipse.jface.resource.LocalResourceManager;

import rs.e4.util.BeanColumnLabelProvider;

/**
 * Formats currencies.
 * @author ralph
 *
 */
public class CurrencyColumnLabelProvider extends BeanColumnLabelProvider {

	/**
	 * @param beanProperty
	 * @param valueLabelProvider
	 */
	public CurrencyColumnLabelProvider(String beanProperty, LocalResourceManager resourceManager) {
		super(beanProperty, new CurrencyLabelProvider(), resourceManager);
		addColorProvider(new CurrencyCellColorProvider());
	}

	
}
