/**
 * 
 */
package rsbudget.data.impl.bo;

import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.crypto.ICryptingDelegate;
import rsbudget.data.api.bo.Setting;
import rsbudget.data.api.dao.SettingDAO;
import rsbudget.data.dto.SettingDTO;

/**
 * Setting BO wrapper.
 * @author ralph
 *
 */
public class SettingBO extends AbstractRsBudgetDbBO<SettingDTO> implements Setting {

	/**
	 * Serial UID. 
	 */
	private static final long serialVersionUID = 3315494945330337350L;

	/**
	 * Constructor with empty DTO.
	 */
	public SettingBO() {
		this(new SettingDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public SettingBO(SettingDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey() {
		return decrypt(getTransferObject().getKey());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setKey(String key) {
		String oldValue = getKey();
		getTransferObject().setKey(encrypt(key));
		firePropertyChange(PROPERTY_KEY, oldValue, key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return decrypt(getTransferObject().getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValue(String value) {
		String oldValue = getValue();
		getTransferObject().setValue(encrypt(value));
		firePropertyChange(PROPERTY_VALUE, oldValue, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay() {
		return getKey();
	}

	/**
	 * Encrypt the given string.
	 * @param s string to be encrypted.
	 * @return encrypted version of string
	 */
	protected String encrypt(String s) {
		if (getDao() == null) return s;
		return ((SettingDAO)getDao()).encrypt(s);
	}
	
	/**
	 * Encrypt the given string.
	 * @param s string to be encrypted.
	 * @return encrypted version of string
	 */
	protected String encrypt(ICryptingDelegate crypto, String s) {
		if (crypto == null) return s;
		if ((s == null) || (s.length() == 0)) return s;
		try {
			String rc = EncryptionUtils.encodeBase64(crypto.encrypt(s.getBytes("UTF-8")));
			return rc;
		} catch (Exception e) {
			throw new RuntimeException("Cannot encrypt string:", e);
		}
	}
	
	/**
	 * Decrypt the given string.
	 * @param s string to be decrypted.
	 * @return decrypted version of string
	 */
	protected String decrypt(String s) {
		if (getDao() == null) return s;
		return ((SettingDAO)getDao()).decrypt(s);
	}
	
	/**
	 * Decrypt the given string.
	 * @param s string to be decrypted.
	 * @return decrypted version of string
	 */
	protected String decrypt(ICryptingDelegate crypto, String s) {
		if (crypto == null) return s;
		if ((s == null) || (s.length() == 0)) return s;
		try {
			String rc = new String(crypto.decrypt(EncryptionUtils.decodeBase64(s)), "UTF-8");
			return rc;
		} catch (Exception e) {
			throw new RuntimeException("Cannot decrypt string:", e);
		}
	}
	
	public void convert(ICryptingDelegate getterCrypto, ICryptingDelegate setterCrypto) {
		SettingDTO dto = getTransferObject();
		dto.setKey(encrypt(setterCrypto, decrypt(getterCrypto, dto.getKey())));
		dto.setValue(encrypt(setterCrypto, decrypt(getterCrypto, dto.getValue())));
	}
}
