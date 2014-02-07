/**
 * 
 */
package rsbudget.data.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import rs.baselib.util.ILocaleDisplayProvider;

/**
 * Stands for a period.
 * @author ralph
 *
 */
public enum PlannedPeriod implements ILocaleDisplayProvider {

	WEEKLY(0, "weekly", "w\u00f6chentlich"),
	MONTHLY(0, "monthly", "monatlich"),
	QUARTERLY(3, "quarterly", "viertelj\u00E4hrlich"),
	HALF_YEARLY(6, "half-yearly", "halbj\u00E4hrlich"),
	YEARLY(12, "yearly", "j\u00E4hrlich");
	
	private Map<Locale, String> display = new HashMap<Locale, String>();
	private int maxSequence;
	
	/**
	 * Constructor.
	 * @param en english display
	 * @param de german display
	 */
	private PlannedPeriod(int maxSequence, String en, String de) {
		display.put(Locale.GERMANY, de);
		display.put(Locale.ENGLISH, en);
		this.maxSequence = maxSequence;
	}

	/**
	 * Returns the display.
	 * @return the display
	 */
	public String getDisplay(Locale locale) {
		String rc = display.get(locale);
		if (rc == null) rc = display.get(Locale.ENGLISH);
		if (rc == null) rc = name();
		return rc;
	}

	/**
	 * Returns the maxSequence.
	 * @return the maxSequence
	 */
	public int getMaxSequence() {
		return maxSequence;
	}

	
}
