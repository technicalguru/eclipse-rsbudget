/**
 * 
 */
package rsbudget.preferences;

import java.util.Currency;
import java.util.Locale;

import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.RGB;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.LoggerFactory;

import rs.baselib.bean.AbstractBean;
import rs.e4.E4Utils;

/**
 * Stores preferences.
 * @author ralph
 *
 */
public class RsBudgetPreferences extends AbstractBean {

	public static final String PROPERTY_LOCALE = "locale";
	public static final String PROPERTY_CURRENCY = "currency";
	public static final String PROPERTY_POSITIVE_COLOR = "positiveColor";
	public static final String PROPERTY_NEGATIVE_COLOR = "negativeColor";
	public static final String PROPERTY_MATCH_CATEGORIES = "matchCategories";
	public static final String PROPERTY_MATCH_BUDGETS = "matchBudgets";
	public static final String PROPERTY_NO_PROXY = "noProxy";
	public static final String PROPERTY_SYSTEM_PROXY = "systemProxy";
	public static final String PROPERTY_MANUAL_PROXY = "manualProxy";
	public static final String PROPERTY_PROXY_HOST = "proxyHost";
	public static final String PROPERTY_PROXY_PORT = "proxyPort";
	public static final String PROPERTY_PROXY_AUTH_REQUIRED = "proxyAuthRequired";
	public static final String PROPERTY_PROXY_USER = "proxyUser";
	public static final String PROPERTY_PROXY_PASSWORD = "proxyPassword";
	
	private static final String HTTP_PROXY_HOST = System.getProperty("http.proxyHost", "");
	private static final String HTTP_PROXY_PORT = System.getProperty("http.proxyPort", "-1");

	private Locale locale;
	private Currency currency;
	private boolean resetLayout = false;
	private RGB positiveColor;
	private RGB negativeColor;
	private boolean matchCategories = true;
	private boolean matchBudgets = true;
	private boolean noProxy = false;
	private boolean systemProxy = true;
	private boolean manualProxy = false;
	private String proxyHost = null;
	private int proxyPort = -1;
	private boolean proxyAuthRequired = false;
	private String proxyUser = null;
	private String proxyPassword = null;
	private IProxyService proxyService = E4Utils.getTopContext().get(IProxyService.class);
	
	/**
	 * Constructor.
	 */
	public RsBudgetPreferences() {
	}

	/**
	 * Returns the locale.
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Sets the locale.
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		Locale oldValue = getLocale();
		this.locale = locale;
		firePropertyChange(PROPERTY_LOCALE, oldValue, locale);
	}

	/**
	 * Returns the currency.
	 * @return the currency
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * Sets the currency.
	 * @param currency the currency to set
	 */
	public void setCurrency(Currency currency) {
		Currency oldValue = getCurrency();
		this.currency = currency;
		firePropertyChange(PROPERTY_CURRENCY, oldValue, currency);
	}

	/**
	 * Returns the positiveColor.
	 * @return the positiveColor
	 */
	public RGB getPositiveColor() {
		return positiveColor;
	}

	/**
	 * Sets the positiveColor.
	 * @param positiveColor the positiveColor to set
	 */
	public void setPositiveColor(RGB positiveColor) {
		RGB oldValue = getPositiveColor();
		this.positiveColor = positiveColor;
		firePropertyChange(PROPERTY_POSITIVE_COLOR, oldValue, positiveColor);
	}

	/**
	 * Returns the negativeColor.
	 * @return the negativeColor
	 */
	public RGB getNegativeColor() {
		return negativeColor;
	}

	/**
	 * Sets the negativeColor.
	 * @param negativeColor the negativeColor to set
	 */
	public void setNegativeColor(RGB negativeColor) {
		RGB oldValue = getNegativeColor();
		this.negativeColor = negativeColor;
		firePropertyChange(PROPERTY_NEGATIVE_COLOR, oldValue, negativeColor);
	}

	/**
	 * Returns the matchCategories.
	 * @return the matchCategories
	 */
	public boolean isMatchCategories() {
		return matchCategories;
	}

	/**
	 * Sets the matchCategories.
	 * @param matchCategories the matchCategories to set
	 */
	public void setMatchCategories(boolean matchCategories) {
		boolean oldValue = isMatchCategories();
		this.matchCategories = matchCategories;
		firePropertyChange(PROPERTY_MATCH_CATEGORIES, oldValue, matchCategories);
	}

	/**
	 * Returns the matchBudgets.
	 * @return the matchBudgets
	 */
	public boolean isMatchBudgets() {
		return matchBudgets;
	}

	/**
	 * Sets the matchBudgets.
	 * @param matchBudgets the matchBudgets to set
	 */
	public void setMatchBudgets(boolean matchBudgets) {
		boolean oldValue = isMatchBudgets();
		this.matchBudgets = matchBudgets;
		firePropertyChange(PROPERTY_MATCH_BUDGETS, oldValue, matchBudgets);
	}

	/**
	 * Returns the {@link #noProxy}.
	 * @return the noProxy
	 */
	public boolean isNoProxy() {
		return noProxy;
	}

	/**
	 * Sets the {@link #noProxy}.
	 * @param noProxy the noProxy to set
	 */
	public void setNoProxy(boolean noProxy) {
		boolean oldValue = isNoProxy();
		this.noProxy = noProxy;
		firePropertyChange(PROPERTY_NO_PROXY, oldValue, noProxy);
	}

	/**
	 * Returns the {@link #systemProxy}.
	 * @return the systemProxy
	 */
	public boolean isSystemProxy() {
		return systemProxy;
	}

	/**
	 * Sets the {@link #systemProxy}.
	 * @param systemProxy the systemProxy to set
	 */
	public void setSystemProxy(boolean systemProxy) {
		boolean oldValue = isSystemProxy();
		this.systemProxy = systemProxy;
		firePropertyChange(PROPERTY_SYSTEM_PROXY, oldValue, systemProxy);
	}

	/**
	 * Returns the {@link #manualProxy}.
	 * @return the manualProxy
	 */
	public boolean isManualProxy() {
		return manualProxy;
	}

	/**
	 * Sets the {@link #manualProxy}.
	 * @param manualProxy the manualProxy to set
	 */
	public void setManualProxy(boolean manualProxy) {
		boolean oldValue = isManualProxy();
		this.manualProxy = manualProxy;
		firePropertyChange(PROPERTY_MANUAL_PROXY, oldValue, manualProxy);
	}

	/**
	 * Returns the {@link #proxyHost}.
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * Sets the {@link #proxyHost}.
	 * @param proxyHost the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		String oldValue = getProxyHost();
		this.proxyHost = proxyHost;
		firePropertyChange(PROPERTY_PROXY_HOST, oldValue, proxyHost);
	}

	/**
	 * Returns the {@link #proxyPort}.
	 * @return the proxyPort
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * Sets the {@link #proxyPort}.
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		int oldValue = getProxyPort();
		this.proxyPort = proxyPort;
		firePropertyChange(PROPERTY_PROXY_PORT, oldValue, proxyPort);
	}

	/**
	 * Returns the {@link #proxyAuthRequired}.
	 * @return the proxyAuthRequired
	 */
	public boolean isProxyAuthRequired() {
		return proxyAuthRequired;
	}

	/**
	 * Sets the {@link #proxyAuthRequired}.
	 * @param proxyAuthRequired the proxyAuthRequired to set
	 */
	public void setProxyAuthRequired(boolean proxyAuthRequired) {
		boolean oldValue = isProxyAuthRequired();
		this.proxyAuthRequired = proxyAuthRequired;
		firePropertyChange(PROPERTY_PROXY_AUTH_REQUIRED, oldValue, proxyAuthRequired);
	}

	/**
	 * Returns the {@link #proxyUser}.
	 * @return the proxyUser
	 */
	public String getProxyUser() {
		return proxyUser;
	}

	/**
	 * Sets the {@link #proxyUser}.
	 * @param proxyUser the proxyUser to set
	 */
	public void setProxyUser(String proxyUser) {
		String oldValue = getProxyUser();
		this.proxyUser = proxyUser;
		firePropertyChange(PROPERTY_PROXY_USER, oldValue, proxyUser);
	}

	/**
	 * Returns the {@link #proxyPassword}.
	 * @return the proxyPassword
	 */
	public String getProxyPassword() {
		return proxyPassword;
	}

	/**
	 * Sets the {@link #proxyPassword}.
	 * @param proxyPassword the proxyPassword to set
	 */
	public void setProxyPassword(String proxyPassword) {
		String oldValue = getProxyPassword();
		this.proxyPassword = proxyPassword;
		firePropertyChange(PROPERTY_PROXY_PASSWORD, oldValue, proxyPassword);
	}

	
	/**
	 * Returns the {@link #resetLayout}.
	 * @return the resetLayout
	 */
	public boolean isResetLayout() {
		return resetLayout;
	}

	/**
	 * Sets the {@link #resetLayout}.
	 * @param resetLayout the resetLayout to set
	 */
	public void setResetLayout(boolean resetLayout) {
		this.resetLayout = resetLayout;
	}

	/**
	 * Create an instance of the preferences and load it properly.
	 * @param resourceManager resource manager for creating fonts and colors.
	 * @return a preferences object
	 */
	public static RsBudgetPreferences load() {
		RsBudgetPreferences rc = new RsBudgetPreferences();
		rc.setLocale(PreferencesUtils.getLocale());
		rc.setCurrency(PreferencesUtils.getCurrency());
		rc.setPositiveColor(PreferencesUtils.getPositiveColor());
		rc.setNegativeColor(PreferencesUtils.getNegativeColor());
		rc.setMatchCategories(PreferencesUtils.isMatchCategories());
		rc.setMatchBudgets(PreferencesUtils.isMatchBudgets());
		rc.setNoProxy(PreferencesUtils.NO_PROXY.equals(PreferencesUtils.getProxyType()));
		rc.setSystemProxy(PreferencesUtils.SYSTEM_PROXY.equals(PreferencesUtils.getProxyType()));
		rc.setManualProxy(PreferencesUtils.MANUAL_PROXY.equals(PreferencesUtils.getProxyType()));
		rc.setProxyHost(PreferencesUtils.getProxyHost());
		rc.setProxyPort(PreferencesUtils.getProxyPort());
		rc.setProxyAuthRequired(PreferencesUtils.isProxyAuthRequired());
		rc.setProxyUser(PreferencesUtils.getProxyUser());
		rc.setProxyPassword(PreferencesUtils.getProxyPassword());
		rc.applyProxySettings();
		rc.setResetLayout(PreferencesUtils.isResetLayout());
		return rc;
	}
	
	/**
	 * Save the object into the preferences.
	 */
	public void save() throws BackingStoreException {
		PreferencesUtils.setLocale(getLocale());
		PreferencesUtils.setCurrency(getCurrency());	
		PreferencesUtils.setPositiveColor(getPositiveColor());
		PreferencesUtils.setNegativeColor(getNegativeColor());
		PreferencesUtils.setMatchCategories(isMatchCategories());
		PreferencesUtils.setMatchBudgets(isMatchBudgets());
		PreferencesUtils.setResetLayout(isResetLayout());
		String type = null;
		if (isNoProxy()) type = PreferencesUtils.NO_PROXY;
		else if (isSystemProxy()) type = PreferencesUtils.SYSTEM_PROXY;
		else type = PreferencesUtils.MANUAL_PROXY;
		PreferencesUtils.setProxyType(type);
		PreferencesUtils.setProxyHost(getProxyHost());
		PreferencesUtils.setProxyPort(getProxyPort());
		PreferencesUtils.setProxyAuthRequired(isProxyAuthRequired());
		PreferencesUtils.setProxyUser(getProxyUser());
		PreferencesUtils.setProxyPassword(getProxyPassword());
		PreferencesUtils.flush();
		applyProxySettings();
	}
	
	/**
	 * Applies the proxy settings.
	 */
	protected void applyProxySettings() {
		if (proxyService != null) {
			// Compute the correct settings
			String host = null;
			int port = -1;
			String user = null;
			String passwd = null;
			
			if (isNoProxy()) {
				// Do nothing
			} else if (isSystemProxy()) {
				host = HTTP_PROXY_HOST;
				port = Integer.valueOf(HTTP_PROXY_PORT);
				// User and password?
			} else {
				host = getProxyHost();
				port = getProxyPort();
				if (isProxyAuthRequired()) {
					user = getProxyUser();
					passwd = getProxyPassword();
				}
			}

			// Now apply it
			IProxyData[] proxyData = proxyService.getProxyData();
			for (IProxyData data : proxyData) {
				if (IProxyData.HTTP_PROXY_TYPE.equals(data.getType()) || IProxyData.HTTPS_PROXY_TYPE.equals(data.getType())) {
					data.setHost(host);
					data.setPort(port);
					data.setUserid(user);
					data.setPassword(passwd);
				} else if ( IProxyData.SOCKS_PROXY_TYPE.equals(data.getType())) {
					if (!isSystemProxy()) {
						data.setHost(null);
						data.setPort(-1);
						data.setUserid(null);
						data.setPassword(null);
					}
				}
			}
			proxyService.setSystemProxiesEnabled(isSystemProxy());
			proxyService.setProxiesEnabled(!isNoProxy());
			if (!isSystemProxy()) {
				System.setProperty("http.proxyHost", host != null ? host : "");
				System.setProperty("http.proxyPort", port > 0 ? ""+port : "-1");
				System.setProperty("https.proxyHost", host != null ? host : "");
				System.setProperty("https.proxyPort", port > 0 ? ""+port : "-1");
			}
			try {
				proxyService.setProxyData(proxyData);
			} catch (CoreException e) {
				LoggerFactory.getLogger(getClass()).error("Cannot apply proxy settings: ", e);
			};
		} else {
			LoggerFactory.getLogger(getClass()).error("Cannot apply proxy settings. ProxyService is not available");
		}
	}
}
