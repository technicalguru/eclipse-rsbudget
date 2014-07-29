/**
 * 
 */
package rsbudget.parts.transactions;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
import rs.baselib.util.IWrapper;
import rsbudget.Plugin;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.api.bo.Transaction;
import rsbudget.util.CurrencyLabelProvider;
import rsbudget.util.ExportProducer;
import rsbudget.util.ExportUtil;

/**
 * Handles the export of the transactions.
 * @author ralph
 *
 */
public class ExportHandler implements ExportProducer {

	private DateFormat dateFormat = CommonUtils.DATE_FORMATTER();
	private CurrencyLabelProvider currencyFormat = new CurrencyLabelProvider();

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
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			Plan plan = txPart.getPlan();
			if (plan != null) {
				try {
					IObservableList tx = txPart.getTransactions();
					List<RsBudgetBO<?>> l = new ArrayList<RsBudgetBO<?>>();
					for (Object o : tx) {
						IWrapper row = (IWrapper)o;
						l.add((RsBudgetBO<?>)row.getWrapped());
					}
					ExportUtil.export(shell, l, this);
				} catch (IOException e) {
					LoggerFactory.getLogger(getClass()).error("Error while exporting transactions", e);
					MessageDialog.openError(shell, Plugin.translate("%export.error.title"), Plugin.translate("export.error.io.message"));
				} catch (Exception e) {
					LoggerFactory.getLogger(getClass()).error("Error while exporting transactions", e);
					MessageDialog.openError(shell, Plugin.translate("%export.error.title"), Plugin.translate("export.error.unknown.message"));
				}
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
		if  ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			return txPart.getPlan() != null;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getColumns(Object o) {
		if (o instanceof PlannedTransaction) {
			PlannedTransaction tx = (PlannedTransaction)o;
			String text = tx.getName();
			if ((tx.getAnnotation() != null) && !tx.getAnnotation().isEmpty()) text += " ("+tx.getAnnotation()+")";
			return new Object[] {
					tx.getPlan().getMonth().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())+" "+tx.getPlan().getMonth().get(Calendar.YEAR),
					tx.getAccount().getName(),
					"",
					"",
					text,
					tx.getCategory() != null ? tx.getCategory().getName() : "",
					tx.getBudget() != null ? tx.getBudget().getName() : "",
					currencyFormat.getText(tx.getAmount()),
					""
			};
		} else if (o instanceof Transaction) {
			Transaction tx = (Transaction)o;
			String text = tx.getText();
			if ((tx.getAnnotation() != null) && !tx.getAnnotation().isEmpty()) text += " ("+tx.getAnnotation()+")";
			return new Object[] {
					tx.getPlan().getMonth().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())+" "+tx.getPlan().getMonth().get(Calendar.YEAR),
					tx.getAccount().getName(),
					dateFormat.format(tx.getTransactionDate().getTime()),
					dateFormat.format(tx.getValueDate().getTime()),
					text,
					tx.getCategory() != null ? tx.getCategory().getName() : "",
					tx.getBudget() != null ? tx.getBudget().getName() : "",
					currencyFormat.getText(tx.getPlannedTransaction() != null ? tx.getPlannedTransaction().getAmount() : 0),
					currencyFormat.getText(tx.getAmount())
			};
		} else if (o == null) {
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

		return null;
	}
	
	
}
