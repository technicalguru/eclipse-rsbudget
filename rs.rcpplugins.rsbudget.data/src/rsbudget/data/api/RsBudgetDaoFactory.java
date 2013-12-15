/**
 * 
 */
package rsbudget.data.api;

import rs.data.api.IDaoFactory;
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
 * Describes the DAO factory used.
 * @author ralph
 *
 */
public interface RsBudgetDaoFactory extends IDaoFactory {

	public static final String APPLICATION_KEY = "rsbudget";
	
	/************************* DAO handling *********************************/
	/**
	 * Returns the accountDAO.
	 * @return the accountDAO
	 */
	public AccountDAO getAccountDAO();

	/**
	 * Returns the accountStatusDAO.
	 * @return the accountStatusDAO
	 */
	public AccountStatusDAO getAccountStatusDAO();

	/**
	 * Returns the bankDAO.
	 * @return the bankDAO
	 */
	public BankDAO getBankDAO();

	/**
	 * Returns the bankInfoDAO.
	 * @return the bankInfoDAO
	 */
	public BankInfoDAO getBankInfoDAO();

	/**
	 * Returns the budgetDAO.
	 * @return the budgetDAO
	 */
	public BudgetDAO getBudgetDAO();

	/**
	 * Returns the budgetRecognitionDAO.
	 * @return the budgetRecognitionDAO
	 */
	public BudgetRecognitionDAO getBudgetRecognitionDAO();

	/**
	 * Returns the categoryDAO.
	 * @return the categoryDAO
	 */
	public CategoryDAO getCategoryDAO();

	/**
	 * Returns the categoryRecognitionDAO.
	 * @return the categoryRecognitionDAO
	 */
	public CategoryRecognitionDAO getCategoryRecognitionDAO();

	/**
	 * Returns the HistoricalItemDAO.
	 * @return the HistoricalItemDAO
	 */
	public HistoricalItemDAO getHistoricalItemDAO();

	/**
	 * Returns the HistoricalItemStatusDAO.
	 * @return the HistoricalItemStatusDAO
	 */
	public HistoricalItemStatusDAO getHistoricalItemStatusDAO();

	/**
	 * Returns the periodicalBudgetDAO.
	 * @return the periodicalBudgetDAO
	 */
	public PeriodicalBudgetDAO getPeriodicalBudgetDAO();

	/**
	 * Returns the periodicalTransactionDAO.
	 * @return the periodicalTransactionDAO
	 */
	public PeriodicalTransactionDAO getPeriodicalTransactionDAO();
	
	/**
	 * Returns the planDAO.
	 * @return the planDAO
	 */
	public PlanDAO getPlanDAO();

	/**
	 * Returns the plannedTransactionDAO.
	 * @return the plannedTransactionDAO
	 */
	public PlannedTransactionDAO getPlannedTransactionDAO();

	/**
	 * Returns the settingDAO.
	 * @return the settingDAO
	 */
	public SettingDAO getSettingDAO();

	/**
	 * Returns the transactionDAO.
	 * @return the transactionDAO
	 */
	public TransactionDAO getTransactionDAO();
	
}
