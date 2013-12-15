/**
 * 
 */
package rsbudget.data.impl.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.dao.PeriodicalBudgetDAO;
import rsbudget.data.dto.PeriodicalBudgetDTO;
import rsbudget.data.impl.bo.PeriodicalBudgetBO;

/**
 * DAO for periodical budgets. (general budget plan)
 * @author ralph
 *
 */
public class PeriodicalBudgetDAOImpl extends AbstractRsBudgetDbDAO<PeriodicalBudgetDTO, PeriodicalBudgetBO, PeriodicalBudget> implements PeriodicalBudgetDAO {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PeriodicalBudget> findBy(Category category) {
		return findByCriteria(buildCriteria(Restrictions.eq("category.id", category.getId())));
	}


}
