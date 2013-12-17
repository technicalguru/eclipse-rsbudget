/**
 * 
 */
package rsbudget.data.util;

import rs.baselib.security.IPasswordCallback;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;

/**
 * The password callback returns the key of the application.
 * @author ralph
 *
 */
public class RsBudgetPasswordCallback implements IPasswordCallback {

	private char password[] = null;
	private byte salt[] = null;

	/**
	 * Constructor.
	 */
	public RsBudgetPasswordCallback() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public char[] getPassword() {
		RsBudgetDaoFactory factory = getFactory();
		if (password == null) {
			try {
				factory.begin();
				password = factory.getSettingDAO().getApplicationKey().toCharArray();
				factory.commit();
			} catch (Exception e) {
				try { factory.rollback(); } catch (Exception ex) {}
			}
		}
		return password;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] getSalt() {
		RsBudgetDaoFactory factory = getFactory();
		if (salt == null) {
			try {
				factory.begin();
				salt = factory.getSettingDAO().getApplicationSalt();
				factory.commit();
			} catch (Exception e) {
				try { factory.rollback(); } catch (Exception ex) {}
			}
		}
		return salt;
	}

	/**
	 * Returns the factory.
	 * @return the factory
	 */
	public RsBudgetDaoFactory getFactory() {
		return RsBudgetModelService.INSTANCE.getFactory();
	}
}
