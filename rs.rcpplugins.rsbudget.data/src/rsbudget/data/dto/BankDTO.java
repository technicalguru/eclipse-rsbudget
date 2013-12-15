/**
 * 
 */
package rsbudget.data.dto;

import java.net.URL;

/**
 * A bank's properties
 * @author ralph
 *
 */
public class BankDTO extends RsBudgetDTO {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -2768315809661235611L;

	private String name;
	private String blz;
	private URL url;
	
	/**
	 * Constructor.
	 */
	public BankDTO() {
	}

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the blz.
	 * @return the blz
	 */
	public String getBlz() {
		return blz;
	}

	/**
	 * Sets the blz.
	 * @param blz the blz to set
	 */
	public void setBlz(String blz) {
		this.blz = blz;
	}

	/**
	 * Returns the url.
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	
}
