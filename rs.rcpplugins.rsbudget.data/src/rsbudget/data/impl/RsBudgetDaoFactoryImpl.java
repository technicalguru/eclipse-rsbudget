/**
 * 
 */
package rsbudget.data.impl;

import rs.data.impl.AbstractDaoFactory;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.dao.AccountDAO;
import rsbudget.data.api.dao.AccountStatusDAO;
import rsbudget.data.api.dao.BankDAO;
import rsbudget.data.api.dao.BankInfoDAO;
import rsbudget.data.api.dao.BudgetDAO;
import rsbudget.data.api.dao.BudgetRecognitionDAO;
import rsbudget.data.api.dao.CategoryDAO;
import rsbudget.data.api.dao.CategoryRecognitionDAO;
import rsbudget.data.api.dao.HistoricalItemDAO;
import rsbudget.data.api.dao.HistoricalItemStatusDAO;
import rsbudget.data.api.dao.PeriodicalBudgetDAO;
import rsbudget.data.api.dao.PeriodicalTransactionDAO;
import rsbudget.data.api.dao.PlanDAO;
import rsbudget.data.api.dao.PlannedTransactionDAO;
import rsbudget.data.api.dao.SettingDAO;
import rsbudget.data.api.dao.TransactionDAO;

/**
 * The implementation of the DAO factory.
 * @author ralph
 *
 */
public class RsBudgetDaoFactoryImpl extends AbstractDaoFactory implements RsBudgetDaoFactory {

	/**
	 * Constructor.
	 */
	public RsBudgetDaoFactoryImpl() {
	}

	
	/************************* DAO handling *********************************/

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountDAO getAccountDAO() {
		return getDao(AccountDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountStatusDAO getAccountStatusDAO() {
		return getDao(AccountStatusDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BankDAO getBankDAO() {
		return getDao(BankDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BankInfoDAO getBankInfoDAO() {
		return getDao(BankInfoDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BudgetDAO getBudgetDAO() {
		return getDao(BudgetDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BudgetRecognitionDAO getBudgetRecognitionDAO() {
		return getDao(BudgetRecognitionDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CategoryDAO getCategoryDAO() {
		return getDao(CategoryDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CategoryRecognitionDAO getCategoryRecognitionDAO() {
		return getDao(CategoryRecognitionDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HistoricalItemDAO getHistoricalItemDAO() {
		return getDao(HistoricalItemDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HistoricalItemStatusDAO getHistoricalItemStatusDAO() {
		return getDao(HistoricalItemStatusDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PeriodicalBudgetDAO getPeriodicalBudgetDAO() {
		return getDao(PeriodicalBudgetDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PeriodicalTransactionDAO getPeriodicalTransactionDAO() {
		return getDao(PeriodicalTransactionDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlanDAO getPlanDAO() {
		return getDao(PlanDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlannedTransactionDAO getPlannedTransactionDAO() {
		return getDao(PlannedTransactionDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SettingDAO getSettingDAO() {
		return getDao(SettingDAO.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransactionDAO getTransactionDAO() {
		return getDao(TransactionDAO.class);
	}
	
}
