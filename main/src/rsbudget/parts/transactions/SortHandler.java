/**
 * 
 */
package rsbudget.parts.transactions;

import java.util.Collections;
import java.util.Comparator;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import rs.baselib.util.RsDate;

/**
 * Sorts the transactions.
 * @author ralph
 *
 */
public class SortHandler {

	private static Comparator<Object> DEFAULT_SORTER = new DefaultSorter(); 
	/**
	 * Constructor.
	 */
	public SortHandler() {
	}

	@SuppressWarnings("unchecked")
	@Execute
	public void execute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			IObservableList list = txPart.getTransactions();
			Collections.sort(list, DEFAULT_SORTER);
			// start job to save
			SaveOrderJob job = ContextInjectionFactory.make(SaveOrderJob.class, context);
			job.setRows(list);
			job.schedule();
		}
	}
	
	@CanExecute
	public boolean canExecute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			return txPart.getPlan() != null;
		}
		return false;
	}
	
	/**
	 * Sort the transactions.
	 * @author ralph
	 *
	 */
	protected static class DefaultSorter implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			TxRowWrapper row1 = (TxRowWrapper)o1;
			TxRowWrapper row2 = (TxRowWrapper)o2;
			
			int rc = 0;
			if (row1.isPlannedTransaction() && row2.isTransaction()) rc = 1;
			else if (row1.isTransaction() && row2.isPlannedTransaction()) rc = -1;
			else if (row1.isPlannedTransaction() && row2.isPlannedTransaction()) {
				rc = row1.getDisplayOrder()-row2.getDisplayOrder();
			} else if (row1.isTransaction() && row2.isTransaction()) {
				RsDate d1 = row1.getValueDate();
				RsDate d2 = row2.getValueDate();
				rc = d1.compareTo(d2);
				if (rc == 0) {
					rc = row1.getDisplayOrder()-row2.getDisplayOrder();
				}
				//System.out.println(o1+"="+CommonUtils.toString(d1)+"  vs "+o2+"="+CommonUtils.toString(d2)+" rc="+rc);
			}
			return rc;
		}
		
		
	}

}
