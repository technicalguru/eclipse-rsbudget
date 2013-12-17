/**
 * 
 */
package rsbudget.data.util;

import java.util.Comparator;

import rsbudget.data.api.bo.RsBudgetBO;

/**
 * Default comparator for model objects.
 * @author ralph
 *
 */
public class DefaultModelComparator<I extends RsBudgetBO<?>> implements Comparator<I> {

	/**
	 * Constructor.
	 */
	public DefaultModelComparator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(I o1, I o2) {
		String s1 = o1.getDisplay();
		String s2 = o2.getDisplay();
		return s1.compareToIgnoreCase(s2);
	}

	
}
