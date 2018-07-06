/**
 * 
 */
package rsbudget.license;

import java.net.URL;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.io.FileFinder;
import rs.baselib.licensing.DefaultLicense;
import rs.baselib.licensing.DefaultLicenseContext;
import rs.baselib.licensing.ILicense;
import rs.baselib.licensing.ILicenseContext;
import rs.baselib.licensing.ILicenseVerifier;
import rs.baselib.licensing.LicenseException;
import rs.baselib.licensing.LicensingScheme;
import rs.baselib.prefs.PreferencesService;
import rs.baselib.util.CommonUtils;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.preferences.PreferencesUtils;

/**
 * Helper method for loading, saving and checking licenses
 * @author ralph
 *
 */
public class RsBudgetLicenseManager {

	/** The logger */
	private static final Logger log = LoggerFactory.getLogger(RsBudgetLicenseManager.class);

	/** Public key for decrypting */
	private static final String PUBLIC_KEY = 
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkVXkJjdB+0nvNHDIXbf2wxnAd343wOr79w7z" + 
					"v9RuJusIFrx6N3eZW0MOUg8QWYjPTAwDivDQDlZhN5Wls4Dv3TORi4Inczc3FMl8Sz4ew+9IF+E2ZoXL" + 
					"JYhS3yAn/oVNu3/P1/1HgU1i00EqGxI5Xa9Mw4qkHJW1mqY8vwTiEdvlG9jvwekOaBrp42+ACVW6bbug" + 
					"aUciQVZuqegX/0IGpAKtsY+7VHXuiqoGRIRe+SXqTh3f1eEqhB5l4Y5iaDeEN/vyohp1nuyivm5pn5Jj"+
					"RNIGz7x5MbBCC7XlfweD7QwUhO+HMvRo6PKvS1tcdTogUFIZo0i1oUdBIqazzxjvVQIDAQAB";

	/** The verified licenses (valid) */
	private static EnumSet<Module> licensedModules = null;

	/** The owner key */
	private static String ownerKey = null;

	/**
	 * Returns the modules that are licensed.
	 * @return list of modules with valid licenses
	 */
	private static EnumSet<Module> getLicensedModules() {
		if (licensedModules == null) {
			synchronized (PUBLIC_KEY) {
				if (licensedModules == null) {
					licensedModules = loadLicenses();
				}
			}
		}
		return licensedModules;
	}

	/**
	 * Returns the modules that are licensed.
	 * @return list of modules with valid licenses
	 */
	private static EnumSet<Module> loadLicenses() {
		EnumSet<Module> rc = EnumSet.noneOf(Module.class);
		try {
			// Load the files here
			ILicenseVerifier verifier = LicensingScheme.RSA_LICENSE.getLicenseVerifier();
			PublicKey publicKey = getPublicKey();
			String ownerKey = getOwnerKey();
			if (ownerKey != null) {
				for (Module module : Module.values()) {
					URL url = FileFinder.find(PreferencesService.INSTANCE.getUserPreferencesHome(RsBudgetDaoFactory.APPLICATION_KEY)+"/"+module.getFilename()+".license");
					if (url != null) try {	
						String licenseKey = CommonUtils.loadContent(url);	
						ILicenseContext context = new DefaultLicenseContext();
						context.set(ILicense.PRODUCT_KEY, module.getFilename());
						context.set(Key.class, publicKey);
						context.set(DefaultLicense.OWNER_KEY, ownerKey);
						context.set(ILicense.EXPIRATION_DATE_KEY, System.currentTimeMillis());
						verifier.verify(licenseKey, context);
						rc.add(module);
						log.info("Valid license found for "+module.name());
					} catch (LicenseException e) {
						log.error("License "+module.name()+" is invalid: "+e.getMessage());
					} catch (Throwable t) {
						// Do not use this license
						log.error("Cannot load license "+module.name(), t);
					} else {
						log.info("No license found for "+module.name());
					}
				}
			} else {
				log.error("Owner Key not found");
			}
		} catch (Throwable t) {
			log.error("Cannot load licenses", t);
		}
		return rc;
	}

	/**
	 * Returns the public key.
	 * @return the public key.
	 */
	private static PublicKey getPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
		return EncryptionUtils.decodeBase64PublicKey(PUBLIC_KEY, "RSA");
	}

	/**
	 * Returns the owner key.
	 * @return the owner key.
	 */
	private static String getOwnerKey() {
		if (ownerKey == null) {
			synchronized(PUBLIC_KEY) {
				if (ownerKey == null) {
					ownerKey = PreferencesUtils.getPreferences("licensing").get("ownerName", "");
					log.info("Licensing owner: "+ownerKey);
				}
			}
		}
		return ownerKey;
	}
	/**
	 * Returns true when the module was licensed correctly.
	 * @param module - the module to be checked
	 * @return {@code true} when module was licensed.
	 */
	public static boolean isLicensed(Module module) {
		EnumSet<Module> licensedModules = getLicensedModules();
		return licensedModules.contains(module);
	}
}
