/**
 * 
 */
package rsbudget.data;

import java.net.URL;
import java.util.prefs.BackingStoreException;

import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;
import rs.baselib.prefs.PreferencesService;
import rsbudget.data.api.RsBudgetDaoFactory;

/**
 * Provides the plugin configuration.
 * @author ralph
 *
 */
public class RsBudgetConfigurationService {

	private static Logger log = LoggerFactory.getLogger(RsBudgetConfigurationService.class);
	private static XMLConfiguration pluginConfig;

	static {
		try {
			boolean isEncrypted = isEncrypted();
			String configName = isEncrypted ? "config/plugin-encrypted-config.xml" : "config/plugin-config.xml";
			
			// Loading config
			URL url = FileFinder.find(RsBudgetConfigurationService.class, configName);
			log.info("Plugin configuration: "+url);
			pluginConfig = new XMLConfiguration(url);
		} catch (Exception e) {
			log.error("Cannot create application", e);
		}
	}
	
	/**
	 * Returns whether the DB is encrypted.
	 * @return <code>true</code> when encryption is on
	 * @throws BackingStoreException
	 */
	public static boolean isEncrypted() throws BackingStoreException {
		return PreferencesService.INSTANCE.getUserPreferences(RsBudgetDaoFactory.APPLICATION_KEY).getBoolean("encryption", false);
	}
	
	/**
	 * Sets whether the DB is encrypted.
	 * <p><b>Attention:</b> This must be set before database is installed.</p>
	 * @param b <code>true</code> when encryption is on
	 * @throws BackingStoreException
	 */
	public static void setEncrypted(boolean b) throws BackingStoreException {
		PreferencesService.INSTANCE.getUserPreferences(RsBudgetDaoFactory.APPLICATION_KEY).putBoolean("encryption", b);
	}
	
	/**
	 * Returns the plugin configuration.
	 * @return
	 */
	public static XMLConfiguration getConfiguration() {
		return pluginConfig;
	}

}
