/**
 * 
 */
package rsbudget.celledit;

import java.util.List;

import rs.e4.celledit.AbstractComboBoxEditingSupportModel;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.api.dao.RsBudgetDAO;

/**
 * A combo box support from DAO values.
 * @author ralph
 *
 */
public class BoSelectionSupportModel<B extends RsBudgetBO<?>> extends AbstractComboBoxEditingSupportModel {
	
	private RsBudgetDAO<?, B> dao;
	
	/**
	 * Constructor.
	 */
	public BoSelectionSupportModel(RsBudgetDAO<?, B> dao, String beanProperty) {
		super(beanProperty);
		this.dao = dao;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public String getDisplay(Object object) {
		return ((RsBudgetBO<?>)object).getDisplay();
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected List<B> createOptions(Object object) {
		dao.getFactory().begin();
		List<B> rc = _getOptions(object);
		dao.getFactory().commit();
		return rc;
	}

	/**
	 * Get the options from the DAO.
	 * @param object
	 * @return
	 */
	protected List<B> _getOptions(Object object) {
		return dao.findDefaultAll();
	}

	/**
	 * Returns the DAO.
	 * @return
	 */
	protected RsBudgetDAO<?, B> getDao() {
		return dao;
	}
}
