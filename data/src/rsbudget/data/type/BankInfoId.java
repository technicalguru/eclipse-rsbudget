/**
 * 
 */
package rsbudget.data.type;

import java.net.URL;

import rs.data.type.UrlKey;

/**
 * The Info ID for bank information.
 * @author ralph
 *
 */
public class BankInfoId extends UrlKey<String> {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public BankInfoId(URL url, String blz) {
		super(url, blz);
	}

}
