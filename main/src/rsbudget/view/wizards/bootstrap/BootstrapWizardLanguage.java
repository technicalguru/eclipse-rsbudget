/**
 * 
 */
package rsbudget.view.wizards.bootstrap;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains all language settings for bootstrap wizard.
 * The class is required as the E4 Translation service cannot be used at this point in time.
 * @author ralph
 *
 */
public class BootstrapWizardLanguage {

	// Languages (Add a constant here for new language)
	private static final String ENGLISH = "English";
	private static final String GERMAN  = "Deutsch";
	
	/** All languages availabel at setup time */
	// Add your language at appropriate index
	public static final String LANGUAGES[] = new String[] {
		ENGLISH,
		GERMAN,
	};
	
	// Add your language at appropriate index
	private static final String LANGUAGE_KEYS[] = new String[] {
		"en_US",
		"de_DE",
	};
	
	/** Actual translations */
	// Add your translations at same index as done for variable LANGUAGES
	private static final String TRANSLATIONS[][] = {
		// ENGLISH
		{ 
			"initialization", "Initialization",
			"welcome.title", "Welcome!",
			"welcome.description", "Thank you for using RsBudget.",
			"welcome.text", "This appears to be the first time you are using RsBudget. You need to enter "+
				    "some settings before you can use RsBudget.\n\n"+
					"Press Next to start the initial setup!",
			"dbsetup.title", "Step 1/5: Database Setup",
			"dbsetup.description", "Please enter your database information!",
			"dbsetup.database", "Database",
			"dbsetup.dbtype", "DB Type:",
			"dbsetup.dbhelp", "In case of doubt, please select \"HyperSQL (File Mode)\".",
			"dbsetup.host", "Host:",
			"dbsetup.port", "Port:",
			"dbsetup.dbname", "Datenbank-Name:",
			"dbsetup.user", "DB Login User:",
			"dbsetup.passwd", "DB Password:",
			"dbsetup.testconnection", "Test Connection!",
			"dbsetup.encryption-title", "Encryption Settings",
			"dbsetup.encryption-label", "Use full encryption",
			"dbsetup.encryption-help", "Full encryption will ensure that your data cannot be read by anyone else than you. However, "+
			      "you will not be able to recover your data when you forgot your password. Full encryption is recommended only "+
				  "when you are not the only user of your computer and fear that your data could be available to other users of "+
			      "your computer.\n\nThe current version does not support the change of this setting.",
			"dbsetup.warn.infomissing", "Please, fill in all fields!",
			"dbsetup.test.ok.title", "Test successful",
			"dbsetup.test.ok.message", "The database connection test was successful.",
			"dbsetup.test.error.title", "Test failed",
			"dbsetup.test.error.message", "The database connection test failed:\n\n{0}",
			"dbsetup.test.encrypterror.title", "Full Encryption Problem",
			"dbsetup.test.encrypterror.message1", "The database already exists but uses full encryption. Invalid setup!",
			"dbsetup.test.encrypterror.message2", "The database already exists but does not use full encryption. Invalid setup!",
			"dbsetup.test.encryptwarn.title", "Existing database detected",
			"dbsetup.test.encryptwarn.message", "The database already exists. The user password of this installation cannot be changed! Do you really want to continue?\n",
			"password.title", "Step 2/5: Set Password",
			"password.description", "Please enter a password for this installation!",
			"password.not-saved-on-system", "The password will not be saved anywhere.",
			"password.passwd", "Password:",
			"password.repeat", "Please re-enter:",
			"password.warn.repeat", "Please re-enter your password !",
			"password.warn.fill", "Please fill in both fields!",
			"password.error.notequal", "The passwords are not equal.",
			"password.error.nofactory", "The database could not be created.",
			"account.title", "Step 3/5: Create Account",
			"account.description", "Please enter the data for the managed bank account!",
			"account.bank", "Company",
			"account.bic", "BIC:",
			"account.account", "Account",
			"account.name", "Name:",
			"account.owner", "Owner:",
			"account.number", "Account Number:",
			"category.title", "Step 4/5: Create Standard Category",
			"category.description", "Please enter the name for a standard category!",
			"category.name", "Name:",
			"category.default", "Misc",
			"summary.title", "Step 5/5: Summary",
			"summary.description", "Please re-check the data you entered and confirm by pressing Finish!",
			"summary.category", "Standard Category:",
			"error.title", "Installation Error",
			"error.message", "An error occurred. Please re-start the application and try again!\n{0}",
			"label.on", "Yes",
			"label.off", "No",
			"finishing", "Applying settings",
		},
		// German
		{
			"initialization", "Initialisierung",
			"welcome.title", "Willkommen!",
			"welcome.description", "Vielen Dank, dass Sie sich f\u00FCr RsBudget entschieden haben.",
			"welcome.text", "Dies ist das erste Mal, dass Sie RsBudget starten. Bevor Sie RsBudget nutzen "+
				    "k\u00F6nnen, m\u00FCssen wir jedoch noch einige Einstellungen vornehmen.\n\n"+
					"Klicken Sie auf Weiter, um die initiale Einrichtung f\u00FCr Ihre Bed\u00FCrfnisse zu starten!",
			"dbsetup.title", "Schritt 1/5: Einrichten der Datenbank",
			"dbsetup.description", "Bitte geben Sie Ihre Zugangsinformationen zur Datenbank ein!",
			"dbsetup.database", "Datenbank",
			"dbsetup.dbtype", "Datenbank-Typ:",
			"dbsetup.dbhelp", "W\u00e4hlen Sie \"HyperSQL (File Mode)\" aus, wenn Sie unsicher sind.",
			"dbsetup.host", "Datenbank-Host:",
			"dbsetup.port", "Port:",
			"dbsetup.dbname", "Datenbank-Name:",
			"dbsetup.user", "Datenbank-Benutzer:",
			"dbsetup.passwd", "Datenbank-Passwort:",
			"dbsetup.testconnection", "Verbindung testen!",
			"dbsetup.encryption-title", "Verschl\u00fcsselung",
			"dbsetup.encryption-label", "Komplettverschl\u00fcsselung anwenden",
			"dbsetup.encryption-help", "Komplettverschl\u00fcsselung stellt sicher, dass Ihre Daten nicht von anderen gelesen werden k\u00f6nnen. "+
			      "Sie hat aber auch den Nachteil, dass Ihre Daten nicht wiederhergestellt werden k\u00f6nnen, falls Sie Ihr Passwort "+
				  "vergessen sollten. Die Komplettverschl\u00fcsselung sollten Sie nur w\u00e4hlen, wenn Sie nicht alleiniger Nutzer "+
			      "Ihres Rechners sind und bef\u00fcrchten, dass ein andere Benutzer Ihre Daten ausliest.\n\n"+
				  "Die aktuelle Version kann diese Einstellung nicht nachtr\u00e4glich \u00e4ndern.",
			"dbsetup.warn.infomissing", "Bitte f\u00fcllen Sie alle Felder aus!",
			"dbsetup.test.ok.title", "Erfolg",
			"dbsetup.test.ok.message", "Der Datenbank-Test war erfolgreich.",
			"dbsetup.test.error.title", "Fehler",
			"dbsetup.test.error.message", "Der Datenbank-Test schlug fehl:\n\n{0}",
			"dbsetup.test.encrypterror.title", "Datenbank-Fehler",
			"dbsetup.test.encrypterror.message1", "Es existiert bereits eine Datenbank, die jedoch Komplettverschl\u00fcsselung\neinsetzt. Die Einstellungen sind inkorrekt.",
			"dbsetup.test.encrypterror.message2", "Es existiert bereits eine Datenbank, die jedoch kein Komplettverschl\u00fcsselung\neinsetzt. Die Einstellungen sind inkorrekt.",
			"dbsetup.test.encryptwarn.title", "Warnung!",
			"dbsetup.test.encryptwarn.message", "Die Datenbank existiert bereits. Das benutzte Passwort kann aber\nnicht ge\u00e4ndert werden! M\u00f6chten Sie trotzdem fortfahren?\n",
			"password.title", "Schritt 2/5: Passwort setzen",
			"password.description", "Bitte vergeben Sie ein Passwort f\u00FCr diese Anwendung!",
			"password.not-saved-on-system", "Das Passwort wird nicht auf Ihrem System gespeichert.",
			"password.passwd", "Neues Passwort:",
			"password.repeat", "Neues Passwort wiederholen:",
			"password.warn.repeat", "Bitte wiederholen Sie das Passwort!",
			"password.warn.fill", "Bitte f\u00fcllen Sie beide Felder aus!",
			"password.error.notequal", "Die Passw\u00f6rter sind nicht identisch.",
			"password.error.nofactory", "Die Datenbank konnte nicht erzeugt werden.",
			"account.title", "Schritt 3/5: Konto erstellen",
			"account.description", "Bitte geben Sie die Daten f\u00f6r ein Konto ein, das verwaltet werden soll.",
			"account.bank", "Institut",
			"account.bic", "BLZ:",
			"account.account", "Konto",
			"account.name", "Name:",
			"account.owner", "Kontoinhaber:",
			"account.number", "Kontonummer:",
			"category.title", "Schritt 4/5: Standard-Kategorie erstellen",
			"category.description", "Bitte geben Sie eine Standard-Kategorie f\u00FCr Ihre Haushaltsf\u00FChrung ein.",
			"category.name", "Name:",
			"category.default", "Sonstiges",
			"summary.title", "Schritt 5/5: Zusammenfassung",
			"summary.description", "Bitte kontrollieren Sie noch einmal die erfassten Daten und best\u00e4tigen Sie deren Korrektheit.",
			"summary.category", "Standard-Kategorie:",
			"error.title", "Installations-Fehler",
			"error.message", "Es ist ein Fehler aufgetreten. Bitte schlie\u00dfen Sie die Anwendung und starten Sie erneut!\n{0}",
			"label.on", "Ja",
			"label.off", "Nein",
			"finishing", "Konfiguration speichern",
		}
	};
	
	private static Map<String, String> currentTranslations = null;
	private static String languageKey = null;
	
	public static void setLanguage(String language) {
		currentTranslations = new HashMap<String, String>();
		
		// Get the index of the language
		int index = 0;
		for (int i=0; i<LANGUAGES.length; i++) {
			if (LANGUAGES[i].equals(language)) {
				index = i;
				break;
			}
		}
		
		languageKey = LANGUAGE_KEYS[index];
		for (int i=0; i<TRANSLATIONS[index].length; i+=2) {
			currentTranslations.put(TRANSLATIONS[index][i], TRANSLATIONS[index][i+1]);
		}
	}
	
	public static String get(String key, Object...args) {
		if (currentTranslations == null) setLanguage(ENGLISH);
		String rc = currentTranslations.get(key);
		if ((args == null) || (args.length == 0)) return rc;
		return MessageFormat.format(rc, args);
	}
	
	public static String getLanguageKey() {
		if (currentTranslations == null) setLanguage(ENGLISH);
		return languageKey;
	}

}
