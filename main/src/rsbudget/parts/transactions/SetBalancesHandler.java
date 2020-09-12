/**
 * 
 */
package rsbudget.parts.transactions;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.Transaction;

/**
 * Handles the creation of a plan.
 * @author ralph
 *
 */
public class SetBalancesHandler {

	@Inject
	private RsBudgetDaoFactory factory;
	
	/**
	 * Constructor.
	 */
	public SetBalancesHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, Shell shell, @Active MPart part) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			Plan plan = txPart.getPlan();
			
			SetBalanceDialog dlg = new SetBalanceDialog(shell, plan);
			dlg.create();
			if (dlg.open() == Dialog.OK) {
				// Do something!
				BigDecimal begin = dlg.getBegin();
				BigDecimal end   = dlg.getEnd();
				try {
					factory.begin();
					plan.setBalanceStart(begin);
					if (begin != null) {
						// Recalculate the account status info
						BigDecimal status = begin;
						IObservableList<TxRowWrapper> list = txPart.getTransactions();
						TxRowWrapper rows[] = list.toArray(new TxRowWrapper[list.size()]);
						for (TxRowWrapper row : rows) {
							if (row.isTransaction()) {
								// set the accountStatusInfo
								status = status.add(row.getActualAmount());
								row.setAccountStatusInfo(status);
								Transaction tx = (Transaction)row.getWrapped();
								factory.getTransactionDAO().save(tx);
							}
						}
					}
					plan.setBalanceEnd(end);
					factory.getPlanDAO().save(plan);
					factory.commit();
				} catch (Exception e) {
					throw new RuntimeException("Cannot commit transaction", e);
				}
			}
			
		}
	}

	/**
	 * Only active when there is a plan yet.
	 * @param context
	 * @return
	 */
	@CanExecute
	public boolean canExecute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			return txPart.getPlan() != null;
		}
		return false;
	}
	
}
