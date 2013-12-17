/**
 * 
 */
package rsbudget.data.impl.bo;

import java.net.URL;

import rsbudget.data.api.bo.BankInfo;
import rsbudget.data.type.BankInfoId;

/**
 * Implementation of Bank Information.
 * @author ralph
 *
 */
public class BankInfoImpl extends AbstractRsBudgetFileBO<BankInfoId> implements BankInfo {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String location;
	private String bic;
	private String unknown;
	private String ipAddress;
	private String url;
	private String hbciVersion;
	private String protocol;

	
	/**
	 * Constructor.
	 * @param file the URL to the file
	 * @param name
	 * @param location
	 * @param bic
	 * @param unknown
	 * @param ipAddress
	 * @param url
	 * @param hbciVersion
	 * @param protocol
	 */
	public BankInfoImpl(URL file, String blz, String name, String location, String bic, String unknown, String ipAddress, String url, String hbciVersion,	String protocol) {
		this(new BankInfoId(file, blz));
		setName(name);
		setLocation(location);
		setBic(bic);
		setUnknown(unknown);
		setIpAddress(ipAddress);
		setUrl(url);
		setHbciVersion(hbciVersion);
		setProtocol(protocol);
	}

	/**
	 * Constructor.
	 */
	public BankInfoImpl(BankInfoId id) {
		super(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay() {
		return getBlz();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBlz() {
		return getId().getKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLocation() {
		return location;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBic() {
		return bic;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUnknown() {
		return unknown;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHbciVersion() {
		return hbciVersion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProtocol() {
		return protocol;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getBlz();
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the location.
	 * @param location the location to set
	 */
	protected void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Sets the bic.
	 * @param bic the bic to set
	 */
	protected void setBic(String bic) {
		this.bic = bic;
	}

	/**
	 * Sets the unknown.
	 * @param unknown the unknown to set
	 */
	protected void setUnknown(String unknown) {
		this.unknown = unknown;
	}

	/**
	 * Sets the ipAddress.
	 * @param ipAddress the ipAddress to set
	 */
	protected void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Sets the url.
	 * @param url the url to set
	 */
	protected void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Sets the hbciVersion.
	 * @param hbciVersion the hbciVersion to set
	 */
	protected void setHbciVersion(String hbciVersion) {
		this.hbciVersion = hbciVersion;
	}

	/**
	 * Sets the protocol.
	 * @param protocol the protocol to set
	 */
	protected void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	
}
