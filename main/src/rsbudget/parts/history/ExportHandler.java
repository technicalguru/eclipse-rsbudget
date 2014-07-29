/**
 * 
 */
package rsbudget.parts.history;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.LoggerFactory;

import rs.baselib.util.CommonUtils;
import rsbudget.Plugin;
import rsbudget.util.ExportProducer;
import rsbudget.util.ExportUtil;

/**
 * Handles the export of the history.
 * @author ralph
 *
 */
public class ExportHandler implements ExportProducer {

	private DateFormat dateFormat = CommonUtils.DATE_FORMATTER();
	private NumberFormat numberFormat = CommonUtils.SIMPLE_NUMBER_FORMATTER();
	private NumberFormat intFormat = CommonUtils.SIMPLE_INT_FORMATTER();
	private HistoryPart historyPart;
	
	/**
	 * Constructor.
	 */
	public ExportHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, @Active MPart part, Shell shell) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof HistoryPart)) {
			HistoryPart txPart = (HistoryPart)customPart;
			try {
				IObservableList tx = txPart.createModel();
				List<StatusRecord> l = new ArrayList<StatusRecord>();
				for (Object o : tx) {
					l.add((StatusRecord)o);
				}
				historyPart = txPart;
				ExportUtil.export(shell, l, this);
			} catch (IOException e) {
				LoggerFactory.getLogger(getClass()).error("Error while exporting history", e);
				MessageDialog.openError(shell, Plugin.translate("%export.error.title"), Plugin.translate("export.error.io.message"));
			} catch (Exception e) {
				LoggerFactory.getLogger(getClass()).error("Error while exporting history", e);
				MessageDialog.openError(shell, Plugin.translate("%export.error.title"), Plugin.translate("export.error.unknown.message"));
			}
		}
	}

	/**
	 * Only active when there is a plan.
	 * @param context the context
	 * @return <code>true</code> when the handler can be executed
	 */
	@CanExecute
	public boolean canExecute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		return (customPart != null) && (customPart instanceof HistoryPart);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getColumns(Object o) {
		if (o instanceof StatusRecord) {
			StatusRecord r = (StatusRecord)o;
			List<Object> rc = new ArrayList<Object>();
			rc.add(dateFormat.format(r.getTimestamp().getBegin().getTime()));
			for (int i=0; i<r.getColumnCount(); i++) {
				Number value = r.getValue(i);
				String unit = r.getUnit(i);
				String s = "";
				if (value == null) s = "";
				else if (r.isFloat(i)) s = numberFormat.format(value)+" "+unit;
				else s = intFormat.format(value)+" "+unit;
				rc.add(s);
			}
			return rc.toArray();
		} else if (o == null) {
			return historyPart.getTableHeaders();			
		}
		return null;
	}
	
	
}
