/**
 * 
 */
package rsbudget.data.impl.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import rs.baselib.crypto.Decrypter;
import rs.baselib.crypto.DecryptionException;
import rs.baselib.crypto.Encrypter;
import rs.baselib.crypto.EncryptionException;
import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.crypto.ICryptingDelegate;
import rs.baselib.crypto.ICryptingDelegateFactory;
import rsbudget.data.api.bo.Setting;
import rsbudget.data.api.dao.SettingDAO;
import rsbudget.data.dto.SettingDTO;
import rsbudget.data.impl.bo.SettingBO;

/**
 * DAO for setting.
 * @author ralph
 *
 */
public class SettingDAOImpl extends AbstractRsBudgetDbDAO<SettingDTO, SettingBO, Setting> implements SettingDAO {

	private char userPassword[];
	private byte salt[] = new byte[] { 
			(byte)172, (byte)33, (byte)110, (byte)239, (byte)134, (byte)155, (byte)128, (byte)249
	};
	private SettingCryptoDelegate cryptingDelegate;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserPassword(char[] password) {
		this.userPassword = password;
		cryptingDelegate = null; // Invalidate the crypter
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Setting findByKey(String key) {
		return findSingleByCriteria(buildCriteria(Restrictions.eq("key", encrypt(key))));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasApplicationKey() {
		return getObjectCount() > 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkUserPassword() {
		Setting setting = findByKey(Setting.KEY_APPLICATION_ID);
		if (setting == null) return false;
		return setting.getValue().startsWith("APPL_ID:");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean checkUserPassword(char[] password) {
		return new String(password).equals(new String(userPassword));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean changePassword(char[] password) {
		// Support only when not full encryption!
		try {
			getFactory().begin();
			List<Setting> l = findAll();
			ICryptingDelegate oldDelegate = getCryptingDelegate();
			userPassword = password;
			SettingCryptoDelegate newDelegate = new SettingCryptoDelegate();

			for (Setting setting : l) {
				SettingBO o = (SettingBO)setting;
				o.convert(oldDelegate, newDelegate);
			}

			// Now change the crypto delegate and password
			cryptingDelegate = newDelegate;

			for (Setting setting : l) {
				save(setting);
			}
			getFactory().commit();
			return true;
		} catch (Throwable t) {
			try {
				getFactory().rollback();
			} catch (Exception e) {}
			throw new RuntimeException("Cannot change password", t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getApplicationKey() {
		Setting setting = findByKey(Setting.KEY_APPLICATION_ID);
		if (setting == null) return null;
		String value = setting.getValue();
		if (value.startsWith("APPL_ID:")) {
			return value.substring(8);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationKey(String applicationKey) {
		Setting pwdSetting = newInstance();
		pwdSetting.setKey(Setting.KEY_APPLICATION_ID);
		String pwdValue = "APPL_ID:"+applicationKey;
		pwdSetting.setValue(pwdValue);
		create(pwdSetting);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getApplicationSalt() {
		Setting setting = findByKey(Setting.KEY_APPLICATION_SALT);
		if (setting == null) return salt;
		String value = setting.getValue();
		if (value.startsWith("APPL_SALT:")) {
			return EncryptionUtils.decodeBase64(value.substring(10));
		}
		return salt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setApplicationSalt(byte[] salt) {
		Setting saltSetting = newInstance();
		saltSetting.setKey(Setting.KEY_APPLICATION_SALT);
		String pwdValue = "APPL_SALT:"+EncryptionUtils.encodeBase64(salt);
		saltSetting.setValue(pwdValue);
		create(saltSetting);
	}

	/**
	 * {@inheritDoc}
	 */
//	@Override
//	protected void afterNewInstance(Setting object, boolean persisted) {
//		super.afterNewInstance(object, persisted);
//		object.setDAO(this); // TODO Is this required?
//	}

	/**
	 * Returns the crypting delegate.
	 * This method will create the delegate if not done yet.
	 * @return the crypting delegate
	 * @see #createCryptingDelegate()
	 */
	protected ICryptingDelegate getCryptingDelegate() {
		if (cryptingDelegate == null) createCryptingDelegate();
		return cryptingDelegate;
	}

	/**
	 * Creates the crypting delegate.
	 */
	protected synchronized void createCryptingDelegate() {
		if (cryptingDelegate != null) return;
		cryptingDelegate = new SettingCryptoDelegate();
	}

	/**
	 * {@inheritDoc}
	 */
	public String encrypt(String s) {
		ICryptingDelegate cryptingDelegate = getCryptingDelegate();
		if (cryptingDelegate == null) return s;
		if ((s == null) || (s.length() == 0)) return s;
		try {
			String rc = EncryptionUtils.encodeBase64(cryptingDelegate.encrypt(s.getBytes("UTF-8")));
			return rc;
		} catch (Exception e) {
			throw new RuntimeException("Cannot encrypt string:", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String decrypt(String s) {
		ICryptingDelegate cryptingDelegate = getCryptingDelegate();
		if (cryptingDelegate == null) return s;
		if ((s == null) || (s.length() == 0)) return s;
		try {
			String rc = new String(cryptingDelegate.decrypt(EncryptionUtils.decodeBase64(s)), "UTF-8");
			return rc;
		} catch (Exception e) {
			throw new RuntimeException("Cannot decrypt string:", e);
		}
	}

	/**
	 * The crypting delegate.
	 * @author ralph
	 *
	 */
	protected class SettingCryptoDelegate implements ICryptingDelegate {

		private Encrypter encrypter;
		private Decrypter decrypter;

		/**
		 * Constructor.
		 */
		public SettingCryptoDelegate() {
			if (userPassword == null) throw new RuntimeException("No user password available!");
			try {
				encrypter = new Encrypter(userPassword, salt);
				decrypter = new Decrypter(userPassword, salt);
			} catch (DecryptionException e) {
				throw new RuntimeException("Cannot create decrypter", e);
			} catch (EncryptionException e) {
				throw new RuntimeException("Cannot create encrypter", e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void init(ICryptingDelegateFactory factory) {
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public byte[] encrypt(byte[] bytes) throws Exception {
			return encrypter.encrypt(bytes);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public byte[] decrypt(byte[] bytes) throws Exception {
			return decrypter.decrypt(bytes);
		}

	}
}
