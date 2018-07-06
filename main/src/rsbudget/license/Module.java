/**
 * 
 */
package rsbudget.license;

/**
 * Modules that require a license.
 * @author ralph
 *
 */
public enum Module {

	/** The HBCI synchronization */
	HBCI("rsbudget-hbci");
	
	/** The filename to use */
	private String filename;
	
	/**
	 * Constructor.
	 * @param filename - filename for the license
	 */
	private Module(String filename) {
		this.filename = filename;
	}

	/**
	 * Returns the filename.
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	
	
}
