/**
 * 
 */
package rsbudget.parts.transactions;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import rsbudget.Plugin;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;

/**
 * Creates a new category.
 * @author ralph
 *
 */
public class NewTransactionHandler {

	@Inject
	private RsBudgetDaoFactory factory;

	/**
	 * Constructor.
	 */
	public NewTransactionHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			Plan plan = txPart.getPlan();
			PlannedTransaction newRow = null;
			try {
				factory.begin();
				newRow = factory.getPlannedTransactionDAO().newInstance();
				newRow.setAccount(factory.getAccountDAO().findDefault());
				newRow.setAmount(0);
				newRow.setAnnotation("");
				newRow.setBudget(null);
				newRow.setCategory(factory.getCategoryDAO().findDefault());
				newRow.setName(Plugin.translate("part.transactions.label.newtx"));
				newRow.setPlan(plan);
				newRow.setTransaction(null);
				newRow.setDisplayOrder(plan.getMaxTxDisplayOrder()+1);
				factory.getPlannedTransactionDAO().create(newRow);
			} finally {
				factory.commit();
			}
			txPart.edit(newRow);
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
