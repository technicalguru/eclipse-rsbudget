/**
 * 
 */
package rsbudget.parts.transactions;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import rs.baselib.util.DefaultComparator;
import rs.baselib.util.RsMonth;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.util.PlannedPeriod;

/**
 * Handles the creation of a plan.
 * @author ralph
 *
 */
public class CreatePlanHandler {

	protected static BudgetListSorter SORTER = new BudgetListSorter();
	
	@Inject
	private RsBudgetDaoFactory factory;
	
	/**
	 * Constructor.
	 */
	public CreatePlanHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			RsMonth month = txPart.getMonth();
			Plan plan = null;
			
			try {
				factory.begin();
				// Create the plan
				plan = factory.getPlanDAO().newInstance();
				plan.setMonth(month);
				factory.getPlanDAO().create(plan);
				
				// Create and remember the budgets
				List<PeriodicalBudget> l1 = factory.getPeriodicalBudgetDAO().findDefaultAll();
				Collections.sort(l1, SORTER);
				Map<PeriodicalBudget, Budget> map = new HashMap<PeriodicalBudget, Budget>();
				for (PeriodicalBudget b : l1) {
					if (b.getPlannedPeriod() == PlannedPeriod.MONTHLY) {
						Budget budget = factory.getBudgetDAO().newInstance();
						budget.setPlan(plan);
						budget.setName(b.getName());
						budget.setCategory(b.getCategory());
						budget.setAmount(b.getAmount());
						budget.setDisplayOrder(b.getDisplayOrder());
						factory.getBudgetDAO().create(budget);
						map.put(b, budget);
					}
				}
				
				// Create the planned transactions
				List<PeriodicalTransaction> l2 = factory.getPeriodicalTransactionDAO().findDefaultAll();
				Collections.sort(l2, SORTER);
				int displayOrder = 1;
				for (PeriodicalTransaction t : l2) {
					int create = 0;
					int sequenceNumber = t.getMonthSequenceNumber();
					switch (t.getPlannedPeriod()) {
					case WEEKLY:
						// Create the transaction 4th times
						create = 4;
						break;
					case MONTHLY:
						// Create the transaction once
						create = 1;
						break;
					case QUARTERLY:
						// Create the TX only when the month is correct
						if (sequenceNumber -1 == month.get(Calendar.MONTH) % 3) create = 1;
						break;
					case HALF_YEARLY:
						// Create the TX only when the month is correct
						if (sequenceNumber -1 == month.get(Calendar.MONTH) % 6) create = 1;
						break;
					case YEARLY:
						// Create the TX only when the month is correct
						if (sequenceNumber -1 == month.get(Calendar.MONTH) % 12) create = 1;
						break;
					}
					
					for (int i=0; i<create; i++) {
						PlannedTransaction tx = factory.getPlannedTransactionDAO().newInstance();
						tx.setPlan(plan);
						String name = t.getName();
						if (create > 1) name += " ("+(i+1)+")";
						tx.setName(name);
						tx.setCategory(t.getCategory());
						tx.setAccount(t.getAccount());
						PeriodicalBudget budget = t.getBudget();
						if (budget != null) tx.setBudget(map.get(budget));
						tx.setAnnotation(t.getAnnotation());
						tx.setAmount(t.getAmount());
						tx.setDisplayOrder(displayOrder);
						factory.getPlannedTransactionDAO().create(tx);
						displayOrder++;
					}
				}
			} catch (Exception e) {
				factory.rollback();
				throw new RuntimeException("Cannot create plan", e);
			} finally {
				factory.commit();
			}
			
			// Inform the view
			txPart.setModel(plan);
		}
	}

	/**
	 * Only active when there is no plan yet.
	 * @param context
	 * @return
	 */
	@CanExecute
	public boolean canExecute(IEclipseContext context, @Active MPart part) {
		Object customPart = part.getObject();
		if ((customPart != null) && (customPart instanceof TransactionsPart)) {
			TransactionsPart txPart = (TransactionsPart)customPart;
			return txPart.getPlan() == null;
		}
		return false;
	}
	
	public static class BudgetListSorter implements Comparator<RsBudgetBO<?>> {

		@Override
		public int compare(RsBudgetBO<?> o1, RsBudgetBO<?> o2) {
			if (!(o1 instanceof PeriodicalBudget) && (o2 instanceof PeriodicalBudget)) return 1;
			if ((o1 instanceof PeriodicalBudget) && !(o2 instanceof PeriodicalBudget)) return -1;
			if ((o1 instanceof PeriodicalBudget) && (o2 instanceof PeriodicalBudget)) {
				int cmp = ((PeriodicalBudget)o1).getDisplayOrder() - ((PeriodicalBudget)o2).getDisplayOrder();
				if (cmp != 0) return cmp;
			}
			if ((o1 instanceof PeriodicalTransaction) && (o2 instanceof PeriodicalTransaction)) {
				int cmp = ((PeriodicalTransaction)o1).getDisplayOrder() - ((PeriodicalTransaction)o2).getDisplayOrder();
				if (cmp != 0) return cmp;
			}
			return DefaultComparator.INSTANCE.compare(o1.getId(), o2.getId());
		}
		
	}

}
