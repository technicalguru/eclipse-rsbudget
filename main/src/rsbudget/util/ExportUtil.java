/**
 * 
 */
package rsbudget.util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import rs.baselib.util.CommonUtils;
import rsbudget.Plugin;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.util.PlannedPeriod;
import rsbudget.data.util.SequenceNumber;
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
	 * Exports {@link PlannedTransaction}s and {@link Transaction}s.
	 * @param shell shell for communication
	 * @param transactions transactions to be exported
	 * @throws IOException when a problem occurs
	 */
	public static void exportTransactions(Shell shell, Collection<RsBudgetBO<?>> transactions) throws IOException {
		WriterInfo info = getWriterInfo(shell);
		if (info.writer != null) {
			info.writer.printRow(getTxHeaderRow());
			DateFormat format = CommonUtils.DATE_FORMATTER();
			CurrencyLabelProvider nFormat = new CurrencyLabelProvider();
			for (RsBudgetBO<?> o : transactions) {
				if (o instanceof PlannedTransaction) {
					PlannedTransaction tx = (PlannedTransaction)o;
					String text = tx.getName();
					if ((tx.getAnnotation() != null) && !tx.getAnnotation().isEmpty()) text += " ("+tx.getAnnotation()+")";
					info.writer.printRow(new Object[] {
							tx.getPlan().getMonth().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())+" "+tx.getPlan().getMonth().get(Calendar.YEAR),
							tx.getAccount().getName(),
							"",
							"",
							text,
							tx.getCategory() != null ? tx.getCategory().getName() : "",
									tx.getBudget() != null ? tx.getBudget().getName() : "",
											nFormat.getText(tx.getAmount()),
											""
					});
				} else if (o instanceof Transaction) {
					Transaction tx = (Transaction)o;
					String text = tx.getText();
					if ((tx.getAnnotation() != null) && !tx.getAnnotation().isEmpty()) text += " ("+tx.getAnnotation()+")";
					info.writer.printRow(new Object[] {
							tx.getPlan().getMonth().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())+" "+tx.getPlan().getMonth().get(Calendar.YEAR),
							tx.getAccount().getName(),
							format.format(tx.getTransactionDate().getTime()),
							format.format(tx.getValueDate().getTime()),
							text,
							tx.getCategory() != null ? tx.getCategory().getName() : "",
							tx.getBudget() != null ? tx.getBudget().getName() : "",
							nFormat.getText(tx.getPlannedTransaction() != null ? tx.getPlannedTransaction().getAmount() : 0),
							nFormat.getText(tx.getAmount())
					});
				}
			}
			info.writer.close();
			MessageDialog.openInformation(shell, Plugin.translate("export.confirm.title"), Plugin.translate("export.confirm.message", info.file.getAbsolutePath()));
		}
	}

	/**
	 * Exports {@link PlannedTransaction}s and {@link Transaction}s.
	 * @param shell shell for communication
	 * @param transactions transactions to be exported
	 * @throws IOException when a problem occurs
	 */
	public static void exportPlanning(Shell shell, Collection<RsBudgetBO<?>> transactions) throws IOException {
		WriterInfo info = getWriterInfo(shell);
		if (info.writer != null) {
			info.writer.printRow(getPlanningHeaderRow());
			CurrencyLabelProvider nFormat = new CurrencyLabelProvider();
			for (RsBudgetBO<?> o : transactions) {
				if (o instanceof PeriodicalBudget) {
					PeriodicalBudget tx = (PeriodicalBudget)o;
					String text = tx.getName();
					info.writer.printRow(new Object[] {
						getPeriodicity(tx.getPlannedPeriod(), tx.getMonthSequenceNumber()),
						"N/A",
						"Budget: "+text,
						tx.getCategory() != null ? tx.getCategory().getName() : "",
						"",
						nFormat.getText(tx.getAmount())
					});
				} else if (o instanceof PeriodicalTransaction) {
					PeriodicalTransaction tx = (PeriodicalTransaction)o;
					String text = tx.getName();
					if ((tx.getAnnotation() != null) && !tx.getAnnotation().isEmpty()) text += " ("+tx.getAnnotation()+")";
					info.writer.printRow(new Object[] {
						getPeriodicity(tx.getPlannedPeriod(), tx.getMonthSequenceNumber()),
						tx.getAccount().getName(),
						text,
						tx.getCategory() != null ? tx.getCategory().getName() : "",
						tx.getBudget() != null ? tx.getBudget().getName() : "",
						nFormat.getText(tx.getAmount())
					});
				}
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

	/** Returns the header row for TX export */
	protected static String[] getTxHeaderRow() {
		return new String[] {
				Plugin.translate("%export.header.month"),
				Plugin.translate("%export.header.account"),
				Plugin.translate("%export.header.transactiondate"),
				Plugin.translate("%export.header.valuedate"),
				Plugin.translate("%part.transactions.column.text.title"),
				Plugin.translate("%part.transactions.column.category.title"),
				Plugin.translate("%part.transactions.column.budget.title"),
				Plugin.translate("%part.transactions.column.planned.title"),
				Plugin.translate("%part.transactions.column.actual.title")
		};
	}
	
	/** Returns the header row for TX export */
	protected static String[] getPlanningHeaderRow() {
		return new String[] {
				Plugin.translate("%export.header.periodicity"),
				Plugin.translate("%export.header.account"),
				Plugin.translate("%part.budgets.column.text.title"),
				Plugin.translate("%part.budgets.column.category.title"),
				Plugin.translate("%part.budgets.column.budget.title"),
				Plugin.translate("%part.budgets.column.amount.title"),
		};
	}
	
	protected static String getPeriodicity(PlannedPeriod period, int sequence) {
		StringBuilder rc = new StringBuilder();
		rc.append(period.getDisplay(Locale.getDefault()));
		if (period.getMaxSequence() > 0) {
			rc.append(" (");
			rc.append(SequenceNumber.getSequence(sequence, period).toString());
			rc.append(")");
		}
		return rc.toString();
	}
	
	/** Helper class */
	protected static class WriterInfo {
		TableWriter writer;
		File file;
	}
	
}
