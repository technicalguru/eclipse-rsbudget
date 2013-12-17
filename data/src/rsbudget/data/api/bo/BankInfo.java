package rsbudget.data.api.bo;

import rsbudget.data.type.BankInfoId;

/**
 * Information about a bank.
 * @author ralph
 *
 */
public interface BankInfo extends RsBudgetBO<BankInfoId> {

	// Postbank|Berlin|PBNKDEFF100|24|10.10.10.255|https://hbci.postbank.de/banking/hbci.do|220|plus|
	
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_LOCATION = "location";
	public static final String PROPERTY_BIC = "bic";
	public static final String PROPERTY_UNKNOWN = "unknown";
	public static final String PROPERTY_IP_ADDRESS = "ipAddress";
	public static final String PROPERTY_URL = "url";
	public static final String PROPERTY_HBCI_VERSION = "hbciVersion";
	public static final String PROPERTY_PROTOCOL = "protocol";

	/**
	 * Returns the blz.
	 * @return the blz
	 */
	public String getBlz();

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName();

	/**
	 * Returns the location.
	 * @return the location
	 */
	public String getLocation();

	/**
	 * Returns the iban.
	 * @return the iban
	 */
	public String getBic();

	/**
	 * Returns the undef1.
	 * @return the undef1
	 */
	public String getUnknown();

	/**
	 * Returns the ip address.
	 * @return the ip address
	 */
	public String getIpAddress();

	/**
	 * Returns the url.
	 * @return the url
	 */
	public String getUrl();

	/**
	 * Returns the undef2.
	 * @return the undef2
	 */
	public String getHbciVersion();

	/**
	 * Returns the protocol.
	 * @return the protocol
	 */
	public String getProtocol();

}
