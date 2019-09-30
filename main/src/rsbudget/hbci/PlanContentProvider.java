/**
 * 
 */
package rsbudget.hbci;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jface.viewers.IStructuredContentProvider;

import rs.baselib.util.RsMonth;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.dao.PlanDAO;

/**
 * Provides the plans for selection in the HBCI dialog.
 * @author ralph
 *
 */
public class PlanContentProvider implements IStructuredContentProvider {

	private RsBudgetDaoFactory factory;
	private PlanDAO planDao;
	
	/**
	 * Constructor.
	 */
	public PlanContentProvider() {
		factory = RsBudgetModelService.INSTANCE.getFactory();
		planDao = factory.getPlanDAO();

	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<RsMonth> plans = new ArrayList<>();
		try {
			factory.begin();
			for (Plan plan : planDao.findLatest(new RsMonth(System.currentTimeMillis()-182*DateUtils.MILLIS_PER_DAY))) {
				if (plan.getBalanceEnd() == null) {
					plans.add(plan.getMonth());
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
		return plans.toArray();
	}

}
