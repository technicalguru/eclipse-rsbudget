/**
 * 
 */
package rsbudget.data.api.dao;

import rsbudget.data.api.bo.Setting;

/**
 * DAO for settings.
 * @author ralph
 *
 */
public interface SettingDAO extends RsBudgetDAO<Long, Setting> {

	/** The key for the Profit/Loss threshold */
	public static final String KEY_PROFIT_LOSS_THRESHOLD = "profitLossThreshold";
	
	/**
	 * Sets the user password for the application.
	 * The password will not be validated.
	 * @param password
	 */
	public void setUserPassword(char password[]);
	
	/**
	 * Find a specific setting.
	 * @param key key of setting
	 * @return Setting object
	 */
	public Setting findByKey(String key);
	
	/**
	 * Returns true when the password is correct.
	 * @return true when password is correct.
	 */
	public boolean checkUserPassword();
	
	/**
	 * Returns true when the given password is correct.
	 * This will only check if the given password matches the new password
	 * @return true when password is correct.
	 */
	public boolean checkUserPassword(char password[]);
	
	/**
	 * Sets a new password.
	 * @return true when password was changed successfully.
	 */
	public boolean changePassword(char password[]);
	
	/**
	 * Returns whether an application key is available in DB.
	 * @return <code>true</code> if the app key is setin DB
	 */
	public boolean hasApplicationKey();
	
	/**
	 * Returns the application ID or null
	 * if the password set is invalid.
	 * @return
	 */
	public String getApplicationKey();
	
	/**
	 * Sets the application ID.
	 * @param applicationKey the key to be set
	 */
	public void setApplicationKey(String applicationKey);
	
	/**
	 * Returns the application salt or null
	 * if the password set or the salt is not set.
	 * @return the application salt.
	 */
	public byte[] getApplicationSalt();
	
	/**
	 * Sets the application salt.
	 * @param the application salt to be set.
	 */
	public void setApplicationSalt(byte salt[]);
	
	/**
	 * Decrypt the given string.
	 * @param s string to be decrypted.
	 * @return decrypted version of string
	 */
	public String decrypt(String s);
	
	/**
	 * Encrypt the given string.
	 * @param s string to be encrypted.
	 * @return encrypted version of string
	 */
	public String encrypt(String s);

}
