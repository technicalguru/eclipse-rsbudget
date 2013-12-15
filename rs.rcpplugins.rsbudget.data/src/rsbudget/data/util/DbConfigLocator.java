/**
 * 
 */
package rsbudget.data.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;
import rs.baselib.prefs.PreferencesService;
import rs.baselib.util.IUrlProvider;
import rsbudget.data.api.RsBudgetDaoFactory;

/**
 * Searches for the dbconfig.xml file.
 * The class will first check the user home directory, then system home dir,
 * and finally the classpath.
 * @author ralph
 *
 */
public class DbConfigLocator implements IUrlProvider {

	public static final String FILENAME = "dbconfig.xml";

	/**
	 * Constructor.
	 */
	public DbConfigLocator() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getUrl() {

		try {
			URL rc = null;

			// Try user home
			File f = getUserDbConfigFile();
			if (f.exists()) rc = f.toURI().toURL();

			if (rc == null) {
				// Try system home
				f = getSystemDbConfigFile();
				if (f.exists()) rc = f.toURI().toURL();
			}

			if (rc == null) {
				// The classic classpath way
				rc = getAppDbConfigFile();
			}

			// Return the file:/// link for this URL
			if ((rc != null) && !rc.getProtocol().equals("file")) {
				rc = FileLocator.toFileURL(rc);
			}
			
			LoggerFactory.getLogger(DbConfigLocator.class).info("DB Configuration: "+rc.toString());
			return rc;
		} catch (IOException e) {
			throw new RuntimeException("Cannot create a file URL: ", e);
		}
	}

	/**
	 * Returns the user DB Config File.
	 * @return the file which shall be used first.
	 */
	public static File getUserDbConfigFile() {
		// Try user home
		File parentDir = PreferencesService.INSTANCE.getUserPreferencesHome(RsBudgetDaoFactory.APPLICATION_KEY);
		return new File(parentDir, FILENAME);
	}

	/**
	 * Returns the system DB Config File.
	 * @return the file which shall be used second.
	 */
	public static File getSystemDbConfigFile() {
		// Try system home
		File parentDir = PreferencesService.INSTANCE.getSystemPreferencesHome(RsBudgetDaoFactory.APPLICATION_KEY);
		return new File(parentDir, FILENAME);
	}

	/**
	 * Returns the classpath DB Config URL.
	 * @return the file which shall be used last.
	 */
	public static URL getAppDbConfigFile() {
		return FileFinder.find(FILENAME);
	}
}
