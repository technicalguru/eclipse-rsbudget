/**
 * 
 */
package rsbudget.util;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.TableColumn;

import rsbudget.preferences.PreferencesUtils;

/**
 * Listens for column resizes and sets the preferences accordingly.
 * @author ralph
 *
 */
public class TableColumnResizeListener implements ControlListener {

	public static final String KEY_CHANGED = "__columnsChanged__";
	
	private String table;
	private String column;
	
	/**
	 * Constructor.
	 */
	public TableColumnResizeListener(String table, String column) {
		this.table = table;
		this.column = column;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void controlMoved(ControlEvent e) {
		// Ignore
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void controlResized(ControlEvent e) {
		TableColumn col = (TableColumn)e.widget;
		PreferencesUtils.setTableColumnWidth(table, column, col.getWidth());
	}

}
