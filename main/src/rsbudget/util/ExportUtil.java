/**
 * 
 */
package rsbudget.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import rsbudget.Plugin;
import csv.TableWriter;
import csv.impl.CSVWriter;
import csv.impl.ExcelWriter;

/**
 * Helps exporting transactions.
 * @author ralph
 *
 */
public class ExportUtil {

	/**
	 * Exports objects.
	 * @param shell shell for communication
	 * @param transactions transactions to be exported
	 * @throws IOException when a problem occurs
	 */
	public static void export(Shell shell, Collection<? extends Object> rows, ExportProducer producer) throws IOException {
		WriterInfo info = getWriterInfo(shell);
		if (info.writer != null) {
			info.writer.printRow(producer.getColumns(null));
			for (Object o : rows) {
				Object row[] = producer.getColumns(o);
				if (row != null) info.writer.printRow(row);
			}
			info.writer.close();
			MessageDialog.openInformation(shell, Plugin.translate("export.confirm.title"), Plugin.translate("export.confirm.message", info.file.getAbsolutePath()));
		}
	}
	
	protected static WriterInfo getWriterInfo(Shell shell) throws IOException {
		WriterInfo rc = new WriterInfo();
		FileDialog dlg = new FileDialog(shell, SWT.SAVE);
		dlg.setText(Plugin.translate("%export.dialog.title"));
		dlg.setOverwrite(true);
		dlg.setFilterExtensions(new String[] { "*.*", "*.csv", "*.xlsx" });
		dlg.setFilterNames(new String[] { Plugin.translate("%export.dialog.filter.all"), Plugin.translate("%export.dialog.filter.csv"), Plugin.translate("%export.dialog.filter.xlsx") });
		dlg.setFilterIndex(1);
		String selected = dlg.open();
		if (selected != null) {
			// Is an extension given?
			rc.file = new File(selected);
			int pos = rc.file.getName().indexOf('.');
			int idx = dlg.getFilterIndex();
			if (pos < 0) {
				if (idx < 2) {
					// CSV is meant
					rc.writer = new CSVWriter(selected+".csv");
					rc.file = new File(selected+".csv");
				} else {
					// Excel is meant
					rc.writer = new ExcelWriter(selected+".xlsx");
					rc.file = new File(selected+".xlsx");
				}
			} else {
				String ext = rc.file.getName().substring(pos+1);
				if (ext.equalsIgnoreCase("txt") || ext.equalsIgnoreCase("csv")) {
					// CSV is meant
					rc.writer = new CSVWriter(selected);
				} else if (ext.equalsIgnoreCase("xlsx")) {
					// Excel is meant
					rc.writer = new ExcelWriter(selected);
				} else if (idx < 2) {
					// CSV is meant
					rc.writer = new CSVWriter(selected);
				} else {
					// Excel is meant
					rc.writer = new ExcelWriter(selected);
				}
			}
		}
		return rc;
	}

	/** Helper class */
	protected static class WriterInfo {
		TableWriter writer;
		File file;
	}
	
}
