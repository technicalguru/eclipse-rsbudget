/**
 * 
 */
package rsbudget.data.impl.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.api.dao.PeriodicalTransactionDAO;
import rsbudget.data.dto.PeriodicalTransactionDTO;
import rsbudget.data.impl.bo.PeriodicalTransactionBO;
import rsbudget.data.util.PlannedPeriod;

/**
 * DAO for periodical transactions (general plan).
 * @author ralph
 *
 */
public class PeriodicalTransactionDAOImpl extends AbstractRsBudgetDbDAO<PeriodicalTransactionDTO, PeriodicalTransactionBO, PeriodicalTransaction>implements PeriodicalTransactionDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void beforeCreate(PeriodicalTransaction object) {
		super.beforeCreate(object);
		if (object.getName() == null) object.setName("");
		if (object.getPlannedPeriod() == null) object.setPlannedPeriod(PlannedPeriod.MONTHLY);
		if (object.getAccount() == null) object.setAccount(getFactory().getAccountDAO().findDefault());
		if (object.getCategory() == null) object.setCategory(getFactory().getCategoryDAO().findDefault());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PeriodicalTransaction> findBy(Category category) {
		return findByCriteria(buildCriteria(Restrictions.eq("category.id", category.getId())));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PeriodicalTransaction findBy(String name) {
		Criteria crit = buildCriteria(Restrictions.eq("name", name));
		return findSingleByCriteria(crit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxDisplayOrder() {
		Criteria crit = buildCriteria();
		crit.addOrder(Order.desc("displayOrder"));
		PeriodicalTransaction tx = findSingleByCriteria(crit);
		return tx != null ? tx.getDisplayOrder() : 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PeriodicalTransaction> findBy(PeriodicalBudget budget) {
		return findByCriteria(buildCriteria(Restrictions.eq("budget.id", budget.getId())));
	}


}
