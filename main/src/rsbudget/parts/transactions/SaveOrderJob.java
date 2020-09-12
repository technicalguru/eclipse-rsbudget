/**
 * 
 */
package rsbudget.parts.transactions;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.slf4j.LoggerFactory;

import rsbudget.Plugin;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;
import rsbudget.statusbar.StatusEntry;

/**
 * Saves all display orders.
 * @author ralph
 *
 */
public class SaveOrderJob extends Job {

	private TxRowWrapper list[];
	private BigDecimal initialAccountInfo;
	
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
			this.list = (TxRowWrapper[])list.toArray(new TxRowWrapper[list.size()]);
		}
	}
	
	/**
	 * Sets the initial account Info for calculation
	 * @param amount amount to be set
	 */
	public void setInitialAccountInfo(BigDecimal amount) {
		initialAccountInfo = amount != null ? amount : BigDecimal.ZERO;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask(Plugin.translate("part.transactions.save.transactions"), list.length);
		setStatus(Plugin.translate("part.transactions.save.saving"));
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		try {
			factory.begin(300000L);
			int index = 0;
			BigDecimal status = initialAccountInfo != null ? initialAccountInfo : BigDecimal.ZERO;
			for (TxRowWrapper row : list) {
				row.setDisplayOrder(index++);
				if (row.isBudget()) factory.getBudgetDAO().save((Budget)row.getWrapped());
				else if (row.isTransaction()) {
					// set the accountStatusInfo
					status = status.add(row.getActualAmount());
					row.setAccountStatusInfo(status);
					Transaction tx = (Transaction)row.getWrapped();
					factory.getTransactionDAO().save(tx);
					if (tx.getPlannedTransaction() != null) {
						factory.getPlannedTransactionDAO().save(tx.getPlannedTransaction());
					}
				} else factory.getPlannedTransactionDAO().save((PlannedTransaction)row.getWrapped());
				monitor.worked(1);
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error("Cannot save: ", e);
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
