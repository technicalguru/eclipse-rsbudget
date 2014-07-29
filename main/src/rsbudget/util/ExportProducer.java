/**
 * 
 */
package rsbudget.util;

/**
 * Interface that will produce the rows for export.
 * @author ralph
 *
 */
public interface ExportProducer {


	/**
	 * Returns the columns to be exported for the given object
	 * @param o the object to be exported (<code>null</code> when the header row shall be produced)
	 * @return the columns for the object or the header row when object was <code>null</code>
	 */
	public Object[] getColumns(Object o);
}
