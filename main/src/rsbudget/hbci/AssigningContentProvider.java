/**
 * 
 */
package rsbudget.hbci;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import rs.baselib.util.RsMonth;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;
import rsbudget.data.api.dao.BudgetDAO;
import rsbudget.data.api.dao.CategoryDAO;
import rsbudget.data.api.dao.PlannedTransactionDAO;
import rsbudget.hbci.AssignmentWrapper.Action;

/**
 * Content Provider for assigning TX in synchronization
 * @author ralph
 *
 */
public class AssigningContentProvider implements IStructuredContentProvider {

	private RsBudgetDaoFactory factory;
	private PlannedTransactionDAO plannedTxDao;
	private BudgetDAO budgetDao;
	private CategoryDAO categoryDao;

	private static Map<RsMonth, Object[]> monthlyObjects = new HashMap<>();
	/**
	 * Constructor.
	 */
	public AssigningContentProvider() {
		factory      = RsBudgetModelService.INSTANCE.getFactory();
		plannedTxDao = factory.getPlannedTransactionDAO();
		budgetDao    = factory.getBudgetDAO();
		categoryDao  = factory.getCategoryDAO();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		RsMonth month = (RsMonth)inputElement;
		Object rc[] = monthlyObjects.get(month);
		if (rc == null) {
			List<AssignmentWrapper> list = new ArrayList<>();
			// Input is the month
			try {
				factory.begin(120000);
				list.add(new AssignmentWrapper(Action.IGNORE));
				list.add(new AssignmentWrapper(Action.NEW));
				list.add(new AssignmentWrapper(Action.SEPARATOR));
				for (Budget budget : budgetDao.findBy(month)) {
					list.add(new AssignmentWrapper(budget));
				}
				list.add(new AssignmentWrapper(Action.SEPARATOR));
				for (Category category : categoryDao.findAll()) {
					list.add(new AssignmentWrapper(category));
				}
				list.add(new AssignmentWrapper(Action.SEPARATOR));
				for (PlannedTransaction ptx : plannedTxDao.findBy(month)) {
					Transaction tx = ptx.getTransaction();
					if (tx != null) {
						list.add(new AssignmentWrapper(tx));
					} else {
						list.add(new AssignmentWrapper(ptx));					
					}
				}
				factory.commit();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					factory.rollback();
				} catch (Throwable t2) {
					t2.printStackTrace();
				}
			}
			rc =  list.toArray();
			monthlyObjects.put(month, rc);
		}
		return rc;
	}

	public static void invalidate(RsMonth month) {
		monthlyObjects.remove(month);
	}
}
