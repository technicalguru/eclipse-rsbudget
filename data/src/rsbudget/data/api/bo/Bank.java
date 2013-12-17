/**
 * 
 */
package rsbudget.data.api.bo;

import java.net.URL;

/**
 * A bank's properties
 * @author ralph
 *
 */
public interface Bank extends RsBudgetBO<Long> {

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_URL  = "url";
	public static final String PROPERTY_BLZ  = "blz";

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName();

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name);

	/**
	 * Returns the blz.
	 * @return the blz
	 */
	public String getBlz();

	/**
	 * Sets the blz.
	 * @param blz the blz to set
	 */
	public void setBlz(String blz);

	/**
	 * Returns the url.
	 * @return the url
	 */
	public URL getUrl();

	/**
	 * Sets the url.
	 * @param url the url to set
	 */
	public void setUrl(URL url);

	
}
