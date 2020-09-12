/**
 * 
 */
package rsbudget.parts.transactions;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;

/**
 * Handles the up movement.
 * @author ralph
 *
 */
public class MoveUpHandler {

	private TxRowWrapper rows[];
	
	/**
	 * Constructor.
	 */
	public MoveUpHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			IObservableList<TxRowWrapper> list = txPart.getTransactions();
			// Get the first index of the selection
			int index = list.indexOf(rows[0]);
			if (index > 0) {
				int firstTxPosition = 0;
				for (int i=0; i<list.size(); i++) {
					TxRowWrapper row = (TxRowWrapper)list.get(i);
					if (row.isTransaction()) {
						firstTxPosition = i;
						break;
					}
				}
				
				// Move all selected objects at index-1
				index--;
				firstTxPosition = Math.max(index, firstTxPosition);
				for (TxRowWrapper row : rows) {
					list.move(list.indexOf(row), firstTxPosition++);
				}
				
				// start job to save
				SaveOrderJob job = ContextInjectionFactory.make(SaveOrderJob.class, context);
				job.setRows(list);
				job.setInitialAccountInfo(txPart.getPlan().getBalanceStart());
				job.schedule();
			}
		}
	}
	
	@CanExecute
	public boolean canExecute() {
		if ((rows == null) || (rows.length == 0)) {
			return false;
		}
		for (TxRowWrapper row : rows) {
			if (row.isBudget()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Get selection from service.
	 * @param budget
	 */
	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) TxRowWrapper transactions[]) {
		if (transactions != null) this.rows = transactions;
	}
}
