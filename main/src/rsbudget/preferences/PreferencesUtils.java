/**
 * 
 */
package rsbudget.preferences;

import java.io.UnsupportedEncodingException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.prefs.BackingStoreException;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.graphics.RGB;
import org.slf4j.LoggerFactory;

import rs.baselib.crypto.Decrypter;
import rs.baselib.crypto.Encrypter;
import rs.baselib.lang.LangUtils;
import rs.baselib.prefs.IPreferences;
import rs.baselib.util.CommonUtils;
import rsbudget.Plugin;
import rsbudget.util.RsBudgetColors;
import rsbudget.util.TableColumnResizeListener;

/**
 * Gets and sets some preferences.
 * @author ralph
 *
 */
public class PreferencesUtils {

	public static final String SYSTEM_PROXY = "SYSTEM";
	public static final String NO_PROXY = "NO";
	public static final String MANUAL_PROXY = "MANUAL";

	/** The preferred Locale */
	private static volatile Locale locale = null;
	/** The preferred currency */
	private static volatile Currency currency = null;
	/** Used for thread synchronization */
	private static Object SYNCH_OBJECT = new Object();
	/** Preferred colors */
	private static Map<String, RGB> colors = new HashMap<>();
	/** matching categories? */
	private static Boolean matchCategories = null;
	/** matching budgets? */
	private static Boolean matchBudgets= null;
	/** User name (is salt to our encrypting process) */
	private static byte[] userName;
	/** key to our encrypting process */
	private static byte[] key;

	/**
	 * Returns the preferred Locale object.
	 * @return the preferred Locale object
	 */
	public static Locale getLocale() {
		if (locale == null) {
			synchronized (SYNCH_OBJECT) {
				if (locale == null) {
					IPreferences langPrefs = getLanguagePreferences();
					locale = LangUtils.getLocale(langPrefs.get("userInterface", Locale.getDefault().toString()));
				}
			}
		}
		return locale;
	}

	/**
	 * Sets the preferred Locale.
	 * @param locale the new preferred locale
	 */
	public static void setLocale(Locale locale) {
		synchronized (SYNCH_OBJECT) {
			IPreferences langPrefs = getLanguagePreferences();
			if (locale == null) locale = Locale.getDefault();
			langPrefs.put("userInterface", locale.toString());
			PreferencesUtils.locale = locale;
		}
	}

	/**
	 * Returns the preferred Currency object.
	 * @return the preferred Currency object
	 */
	public static Currency getCurrency() {
		if (currency == null) {
			synchronized (SYNCH_OBJECT) {
				if (currency == null) {
					IPreferences langPrefs = getLanguagePreferences();
					try {
						String currencyCode = langPrefs.get("currency", Currency.getInstance(getLocale()).getCurrencyCode());
						currency = Currency.getInstance(currencyCode);
						if (currency == null) currency = Currency.getInstance(getLocale());
					} catch (Throwable t) {
						currency = Currency.getInstance("USD");
					}
				}
			}
		}
		return currency;
	}

	/**
	 * Sets the preferred currency.
	 * @param currency the new preferred currency
	 */
	public static void setCurrency(Currency currency) {
		synchronized (SYNCH_OBJECT) {
			IPreferences langPrefs = getLanguagePreferences();
			if (currency == null) currency = Currency.getInstance(getLocale());
			String currencyCode = currency.getCurrencyCode();
			langPrefs.put("currency", currencyCode);
			PreferencesUtils.currency = currency;
		}
	}

	/**
	 * Returns the setting for matching categories.
	 * @return true when matching shall be done
	 */
	public static boolean isMatchCategories() {
		if (matchCategories == null) {
			synchronized (SYNCH_OBJECT) {
				if (matchCategories == null) {
					IPreferences prefs = getGeneralPreferences();
					matchCategories = LangUtils.getBoolean(prefs.get("match-categories", "true"));
				}
			}
		}
		return matchCategories.booleanValue();
	}

	/**
	 * Sets the setting for matching categories.
	 * @param b true when matching shall be done
	 */
	public static void setMatchCategories(boolean b) {
		synchronized (SYNCH_OBJECT) {
			IPreferences prefs = getGeneralPreferences();
			prefs.put("match-categories", b ? "true" : "false");
			PreferencesUtils.matchCategories = b;
		}
	}

	/**
	 * Returns the setting for matching budgets.
	 * @return true when matching shall be done
	 */
	public static boolean isMatchBudgets() {
		if (matchBudgets == null) {
			synchronized (SYNCH_OBJECT) {
				if (matchBudgets == null) {
					IPreferences prefs = getGeneralPreferences();
					matchBudgets = LangUtils.getBoolean(prefs.get("match-budgets", "true"));
				}
			}
		}
		return matchBudgets.booleanValue();
	}

	/**
	 * Sets the setting for matching categories.
	 * @param b true when matching shall be done
	 */
	public static void setMatchBudgets(boolean b) {
		synchronized (SYNCH_OBJECT) {
			IPreferences prefs = getGeneralPreferences();
			prefs.put("match-budgets", b ? "true" : "false");
			PreferencesUtils.matchBudgets = b;
		}
	}

	/**
	 * Returns the Eclipse general preferences node.
	 * @return
	 */
	public static IPreferences getGeneralPreferences() {
		return getPreferences("general");
	}

	/**
	 * Returns the Eclipse language preferences node.
	 * @return
	 */
	public static IPreferences getLanguagePreferences() {
		return getPreferences("languages");
	}

	/**
	 * Returns the Eclipse color preferences node.
	 * @return
	 */
	public static IPreferences getColorPreferences(String key) {
		return getPreferences("colors", key);
	}

	/**
	 * Returns the Eclipse proxy preferences node.
	 * @return the proxy preferences
	 */
	public static IPreferences getProxyPreferences() {
		return getPreferences("proxy");
	}

	/**
	 * Returns the Eclipse proxy preferences node.
	 * @return the proxy preferences
	 */
	public static IPreferences getTableColumnPreferences(String table, String column, boolean create) {
		if (!create) {
			IPreferences tablesPrefs = getPreferences(false, "tables");
			if (tablesPrefs != null) {
				IPreferences tablePrefs = getPreferences(false, "tables", table);
				if ((tablePrefs != null) && tablePrefs.getBoolean(TableColumnResizeListener.KEY_CHANGED, false)) {
					try {
						tablePrefs.removeNode();
						tablesPrefs.flush();
					} catch (BackingStoreException e) {
						LoggerFactory.getLogger(PreferencesUtils.class).error("Cannot remove node tables/"+table, e);
					}
				}
			}
		}
		return getPreferences(create, "tables", table, column);
	}

	/**
	 * Returns the node with given path
	 * @param keys the keys for the path
	 * @return the node
	 */
	public static IPreferences getPreferences(String... keys) {
		return getPreferences(true, keys);
	}

	/**
	 * Returns the node with given path
	 * @param create whether to create the node or return null instead
	 * @param keys the keys for the path
	 * @return the node
	 */
	public static IPreferences getPreferences(boolean create, String... keys) {
		try {
			IPreferences rc = Plugin.getUserPreferences();
			for (String key : keys) {
				if (!create && !rc.nodeExists(key)) return null;
				rc = rc.node(key);
			}
			return rc;
		} catch (BackingStoreException e) {
			LoggerFactory.getLogger(PreferencesUtils.class).error("Cannot retrieve node "+CommonUtils.join("/", keys), e);
		}
		return null;
	}

	/**
	 * Returns the preferred color with given key.
	 * @param key key of the preferred color
	 * @param defaultColor default color
	 * @return the preferred color
	 */
	public static RGB getColor(String key, RGB defaultColor) {
		RGB rc = colors.get(key);
		if (rc == null) {
			synchronized(SYNCH_OBJECT) {
				rc = colors.get(key);
				if (rc == null) {
					IPreferences colorDef = getColorPreferences(key);
					int red   = colorDef.getInt("red", defaultColor.red);
					int green = colorDef.getInt("green", defaultColor.green);
					int blue  = colorDef.getInt("blue", defaultColor.blue);
					rc = new RGB(red, green, blue);
					colors.put(key, rc);
				}
			}
		}
		return rc;
	}

	/**
	 * Sets a preferred color.
	 * @param key key of preferred color.
	 * @param color new color or null if it shall be removed
	 */
	public static void setColor(String key, RGB color) {
		synchronized(SYNCH_OBJECT) {
			IPreferences colorDef = getColorPreferences(key);
			if (color != null) {
				colorDef.putInt("red", color.red);
				colorDef.putInt("green", color.green);
				colorDef.putInt("blue", color.blue);
				colors.put(key, color);
			} else {
				colorDef.remove("red");
				colorDef.remove("green");
				colorDef.remove("blue");
				colors.remove(key);
			}
		}
	}

	/**
	 * Returns the color used for positive amounts.
	 * @return the color (usually black)
	 */
	public static RGB getPositiveColor() {
		return getColor("positiveAmount", RsBudgetColors.RGB_BLACK);
	}

	/**
	 * Sets the color used for positive amounts
	 * @param color the new color
	 */
	public static void setPositiveColor(RGB color) {
		setColor("positiveAmount", color);
	}

	/**
	 * Returns the color used for negative amounts.
	 * @return the color (usually red)
	 */
	public static RGB getNegativeColor() {
		return getColor("negativeAmount", RsBudgetColors.RGB_RED);
	}

	/**
	 * Sets the color used for negative amounts
	 * @param color the new color
	 */
	public static void setNegativeColor(RGB color) {
		setColor("negativeAmount", color);
	}

	/**
	 * Returns the proxy type.
	 * @return {@link #SYSTEM_PROXY}, {@link #NO_PROXY} or {@link #MANUAL_PROXY}.
	 */
	public static String getProxyType() {
		return getProxyPreferences().get("proxyType", SYSTEM_PROXY);
	}

	/**
	 * Sets the proxy type.
	 * @param type {@link #SYSTEM_PROXY}, {@link #NO_PROXY} or {@link #MANUAL_PROXY}.
	 */
	public static void setProxyType(String type) {
		getProxyPreferences().put("proxyType", type);
	}

	/**
	 * Returns the proxy host.
	 * @return the proxy host
	 */
	public static String getProxyHost() {
		return getProxyPreferences().get("host", null);
	}

	/**
	 * Sets the proxy host.
	 * @param host the proxy host
	 */
	public static void setProxyHost(String host) {
		getProxyPreferences().put("host", host);
	}

	/**
	 * Returns the proxy port.
	 * @return the proxy port
	 */
	public static int getProxyPort() {
		return getProxyPreferences().getInt("port", -1);
	}

	/**
	 * Sets the proxy port.
	 * @param port the proxy port
	 */
	public static void setProxyPort(int port) {
		getProxyPreferences().putInt("port", port);
	}

	/**
	 * Returns whether authorization is required on proxy
	 * @return true or false
	 */
	public static boolean isProxyAuthRequired() {
		return getProxyPreferences().getBoolean("authRequired", false);
	}

	/**
	 * Sets whether proxy authorization is required or not.
	 * @param b true or false
	 */
	public static void setProxyAuthRequired(boolean b) {
		getProxyPreferences().putBoolean("authRequired", b);
	}

	/**
	 * Returns whether window shall be reset on restart.
	 * @return true or false
	 */
	public static boolean isResetLayout() {
		return getPreferences().getBoolean("resetApplication", false);
	}

	/**
	 * Sets whether window shall be reset on restart.
	 * @param b true or false
	 */
	public static void setResetLayout(boolean b) {
		getPreferences().putBoolean("resetApplication", b);
	}

	/**
	 * Returns the proxy user.
	 * @return the proxy user
	 */
	public static String getProxyUser() {
		return getProxyPreferences().get("user", null);
	}

	/**
	 * Sets the proxy user.
	 * @param user the proxy user
	 */
	public static void setProxyUser(String user) {
		getProxyPreferences().put("user", user);
	}

	/**
	 * Returns the proxy password.
	 * @return the proxy password
	 */
	public static String getProxyPassword() {
		String rc = getProxyPreferences().get("password", null);
		if (!CommonUtils.isEmpty(rc)) rc = decrypt(rc);
		return rc;
	}

	/**
	 * Sets the proxy password.
	 * @param password the proxy password
	 */
	public static void setProxyPassword(String password) {
		getProxyPreferences().put("password", encrypt(password));
	}

	/**
	 * Return the salt for this user.
	 * @return the salt
	 */
	protected synchronized static byte[] getSalt() {
		if (userName == null) {
			try {
				userName = System.getProperty("user.name").getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				userName = "default".getBytes();
			}
			if (userName.length % 8 != 0) {
				byte b[] = new byte[userName.length+(8-(userName.length % 8))];
				for (int i=0; i<b.length; i++) {
					b[i] = userName[i % userName.length];
				}
				userName = b;
			}
		}
		return userName;
	}

	/**
	 * Return the key for this user.
	 * @return the key
	 */
	protected synchronized static byte[] getKey() {
		if (key == null) {
			key = Plugin.SYMBOLIC_NAME.getBytes();
		}
		return key;
	}

	/**
	 * Encrypt the given string.
	 * @param s string to be encrypted
	 * @return encrypted string
	 */
	protected static String encrypt(String s) {
		if (CommonUtils.isEmpty(s)) return s;
		try {
			Encrypter enc = new Encrypter(getKey(), getSalt(), 0);
			return enc.encrypt(s);
		} catch (Exception e) {
			LoggerFactory.getLogger(PreferencesUtils.class).error("Cannot encrypt string", e);
			return s;
		}
	}

	/**
	 * Decrypt the given string.
	 * @param s string to be decrypted
	 * @return decrypted string
	 */
	protected static String decrypt(String s) {
		if (CommonUtils.isEmpty(s)) return s;
		try {
			Decrypter dec = new Decrypter(getKey(), getSalt(), 0);
			return dec.decrypt(s);
		} catch (Exception e) {
			LoggerFactory.getLogger(PreferencesUtils.class).error("Cannot decrypt string", e);
			return s;
		}
	}

	/**
	 * Returns an appropriate table column layout data.
	 * @param table table key
	 * @param column column key
	 * @param defaultWeight default width to be used
	 * @param minWidth min width to be used
	 * @param isResizable whether it is resizable
	 * @return the layout data
	 */
	public static ColumnLayoutData getTableColumnData(String table, String column, int defaultWeight, int minWidth, boolean isResizable) {
		IPreferences colPrefs = getTableColumnPreferences(table, column, false);
		if (colPrefs != null) {
			return new ColumnPixelData(colPrefs.getInt("width", defaultWeight), isResizable);
		}
		return new ColumnWeightData(defaultWeight, minWidth, isResizable);
	}

	/**
	 * Sets the width preference for the given column.
	 * @param table table key
	 * @param column column key
	 * @param width width preference
	 */
	public static void setTableColumnWidth(String table, String column, int width) {
		IPreferences colPrefs = getTableColumnPreferences(table, column, true);
		colPrefs.putInt("width", width);
	}

	/**
	 * Flushes the preferences.
	 * @throws BackingStoreException
	 */
	public static void flush() {
		try {
			Plugin.getUserPreferences().flush();
		} catch (BackingStoreException e) {
			LoggerFactory.getLogger(PreferencesUtils.class).error("Cannot flush preferences", e);
		}
	}
}
