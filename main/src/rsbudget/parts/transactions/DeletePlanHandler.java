/**
 * 
 */
package rsbudget.parts.transactions;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import rs.baselib.util.RsMonth;
import rsbudget.Plugin;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;

/**
 * Handles the creation of a plan.
 * @author ralph
 *
 */
public class DeletePlanHandler {

	@Inject
	private RsBudgetDaoFactory factory;
	
	/**
	 * Constructor.
	 */
	public DeletePlanHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, Shell shell, @Active MPart part) {
		boolean doIt = MessageDialog.openConfirm(shell, Plugin.translate("part.transactions.dialog.deleteplan.title"), Plugin.translate("part.transactions.dialog.deleteplan.message"));
		if (!doIt) return;
		
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			Plan plan = txPart.getPlan();
			RsMonth month = plan.getMonth();
			
			try {
				
				
				// Start the work
				factory.begin();
				
				// Remove all transactions
				Set<Transaction> txList = new HashSet<Transaction>(plan.getTransactions());
				for (Transaction o : txList) factory.getTransactionDAO().delete(o);
				
				// Remove all planned transactions
				Set<PlannedTransaction> ptxList = new HashSet<PlannedTransaction>(plan.getPlannedTransactions());
				for (PlannedTransaction o : ptxList) factory.getPlannedTransactionDAO().delete(o);
				
				// Remove all budgets
				Set<Budget> bList = new HashSet<Budget>(plan.getBudgets());
				for (Budget o : bList) factory.getBudgetDAO().delete(o);
				
				// Remove the plan
				factory.getPlanDAO().delete(plan);
				
				// That's it
			} catch (Exception e) {
				factory.rollback();
				throw new RuntimeException("Cannot create plan", e);
			} finally {
				factory.commit();
			}
			
			// Inform the view
			txPart.setModel(month);
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
