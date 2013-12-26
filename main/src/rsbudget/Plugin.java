/**
 * 
 */
package rsbudget;

import java.util.prefs.BackingStoreException;

import org.eclipse.e4.core.services.translation.TranslationService;

import rs.baselib.prefs.IPreferences;
import rs.baselib.prefs.PreferencesService;
import rs.e4.E4Utils;
import rsbudget.data.api.RsBudgetDaoFactory;

/**
 * Some main methods for the plugin
 * @author ralph
 *
 */
public class Plugin {

	public static final String SYMBOLIC_NAME = "rs.rcpplugins.rsbudget.main";
	public static final String CONTRIBUTOR_URI = "platform:/plugin/"+SYMBOLIC_NAME;
	public static final String APPLICATION_KEY = RsBudgetDaoFactory.APPLICATION_KEY;
	public static final String APPLICATION_NAME = "RsBudget";
	public static final String APPLICATION_SPLASH_VERSION = "2.0";
	public static final String BRANDING_TITLE = "Beta2";
	public static final String APPLICATION_SUBTITLE = "Track your expenses";
	public static final String NEW_ROW_COMMAND_ID = "rs.rcpplugins.rsbudget.command.newrow";
	public static final String DELETE_ROW_COMMAND_ID = "rs.rcpplugins.rsbudget.command.deleterow";
	
	/** The release site */
	public static final String RELEASE_CHANNEL = "http://download.ralph-schuster.eu/rsbudget/releases";
	/** The snapshot site */
	public static final String SNAPSHOT_CHANNEL = "http://download.ralph-schuster.eu/rsbudget/snapshots/";
	
	public static final TranslationService TRANSLATIONS = E4Utils.getTopContext().get(TranslationService.class);
	
	/**
	 * Translate the given key.
	 * @param key key to translate
	 * @return translated value
	 */
	public static String translate(String key, Object... args) {
		return E4Utils.translate(key, CONTRIBUTOR_URI, args);
	}
	
	/**
	 * Returns the user preferences.
	 * @return the preferences
	 */
	public static IPreferences getUserPreferences() throws BackingStoreException {
		return PreferencesService.INSTANCE.getUserPreferences(APPLICATION_KEY);
	}
	
	/**
	 * Returns the system preferences.
	 * @return the preferences
	 */
	public static IPreferences getSystemPreferences() throws BackingStoreException {
		return PreferencesService.INSTANCE.getSystemPreferences(APPLICATION_KEY);
	}
}
