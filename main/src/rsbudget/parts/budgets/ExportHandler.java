/**
 * 
 */
package rsbudget.parts.budgets;

import java.io.IOException;
import java.util.ArrayList;
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

import rs.baselib.util.IWrapper;
import rsbudget.Plugin;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.util.PlannedPeriod;
import rsbudget.data.util.SequenceNumber;
import rsbudget.util.CurrencyLabelProvider;
import rsbudget.util.ExportProducer;
import rsbudget.util.ExportUtil;

/**
 * Handles the export of the transactions.
 * @author ralph
 *
 */
public class ExportHandler implements ExportProducer {

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
		if ((customPart != null) && (customPart instanceof BudgetPart)) {
			BudgetPart txPart = (BudgetPart)customPart;
			try {
				IObservableList budgets = txPart.getBudgets();
				List<RsBudgetBO<?>> l = new ArrayList<RsBudgetBO<?>>();
				for (Object o : budgets) {
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

	/**
	 * Only active when there is a plan.
	 * @param context the context
	 * @return <code>true</code> when the handler can be executed
	 */
	@CanExecute
	public boolean canExecute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		return (customPart != null) && (customPart instanceof BudgetPart);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] getColumns(Object o) {
		if (o instanceof PeriodicalBudget) {
			PeriodicalBudget tx = (PeriodicalBudget)o;
			String text = tx.getName();
			return new Object[] {
					getPeriodicity(tx.getPlannedPeriod(), tx.getMonthSequenceNumber()),
					"N/A",
					"Budget: "+text,
					tx.getCategory() != null ? tx.getCategory().getName() : "",
					"",
					currencyFormat.getText(tx.getAmount())
			};
		} else if (o instanceof PeriodicalTransaction) {
			PeriodicalTransaction tx = (PeriodicalTransaction)o;
			String text = tx.getName();
			if ((tx.getAnnotation() != null) && !tx.getAnnotation().isEmpty()) text += " ("+tx.getAnnotation()+")";
			return new Object[] {
					getPeriodicity(tx.getPlannedPeriod(), tx.getMonthSequenceNumber()),
					tx.getAccount().getName(),
					text,
					tx.getCategory() != null ? tx.getCategory().getName() : "",
					tx.getBudget() != null ? tx.getBudget().getName() : "",
					currencyFormat.getText(tx.getAmount())
			};
		} else if (o == null) {
			return new String[] {
					Plugin.translate("%export.header.periodicity"),
					Plugin.translate("%export.header.account"),
					Plugin.translate("%part.budgets.column.text.title"),
					Plugin.translate("%part.budgets.column.category.title"),
					Plugin.translate("%part.budgets.column.budget.title"),
					Plugin.translate("%part.budgets.column.amount.title"),
			};
		}
		return null;
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

}
