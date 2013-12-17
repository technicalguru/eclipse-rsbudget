/**
 * 
 */
package rsbudget.parts.budgets;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;

import rsbudget.Plugin;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.statusbar.StatusEntry;

/**
 * Saves all display orders.
 * @author ralph
 *
 */
public class SaveOrderJob extends Job {

	private BudgetRowWrapper list[];

	@Inject
	private IEventBroker eventBroker;
	
	/**
	 * Constructor.
	 */
	public SaveOrderJob() {
		this(null);
	}
	
	/**
	 * Constructor.
	 */
	public SaveOrderJob(IObservableList list) {
		super("Saving");
		setRows(list);
	}
	
	/**
	 * Sets the list for saving.
	 * @param list
	 */
	public void setRows(IObservableList list) {
		if (list != null) {
			this.list = (BudgetRowWrapper[])list.toArray(new BudgetRowWrapper[list.size()]);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Plugin.translate("part.budgets.save.transactions"), list.length);
		setStatus(Plugin.translate("part.budgets.save.saving"));
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		try {
			factory.begin();
			int index = 0;
			for (BudgetRowWrapper row : list) {
				row.setDisplayOrder(index++);
				if (row.isBudget()) factory.getPeriodicalBudgetDAO().save((PeriodicalBudget)row.getWrapped());
				else factory.getPeriodicalTransactionDAO().save((PeriodicalTransaction)row.getWrapped());
				monitor.worked(1);
			}
		} finally {
			factory.commit();
		}
		monitor.done();
		setStatus(Plugin.translate("status.ready"));
		return Status.OK_STATUS;
	}
	
	/**
	 * Sets the status message.
	 * @param s the message to be set.
	 */
	protected void setStatus(String s) {
		if (eventBroker != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("STATUS", s);
			eventBroker.post(StatusEntry.TOPIC, map);
		}
	}

}
