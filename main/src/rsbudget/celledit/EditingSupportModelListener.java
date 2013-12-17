/**
 * 
 */
package rsbudget.celledit;

import rs.baselib.util.IWrapper;
import rs.e4.celledit.IEditingSupportModelListener;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.api.dao.RsBudgetDAO;

/**
 * Handles transactions.
 * @author ralph
 *
 */
public class EditingSupportModelListener implements IEditingSupportModelListener {

	private RsBudgetDaoFactory factory;
	
	/**
	 * Constructor.
	 */
	public EditingSupportModelListener() {
		factory = RsBudgetModelService.INSTANCE.getFactory();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeGetValue(Object element, String beanProperty) {
		factory.begin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterGetValue(Object element, String beanProperty) {
		factory.commit();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeSetValue(Object element, String beanProperty, Object value) {
		factory.begin();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterSetValue(Object element, String beanProperty, Object value) {
		if (element instanceof IWrapper) element = ((IWrapper)element).getWrapped();
		RsBudgetBO<?> mo = (RsBudgetBO<?>)element;
		RsBudgetDAO<?,?> dao = (RsBudgetDAO<?,?>)mo.getDao();
		if ((dao != null) && (mo.getId() != null)) {
			beforeSaveObject((RsBudgetBO<?>)element);
			dao.saveObject((RsBudgetBO<?>)element);
			afterSaveObject((RsBudgetBO<?>)element);
		} else {
			factory.rollback();
			throw new RuntimeException("No DAO found for: "+element);
		}
		factory.commit();
	}

	/**
	 * Hook into initialization.
	 * @param object object about to create
	 */
	protected void beforeCreateObject(RsBudgetBO<?> object) {
		
	}

	/**
	 * Hook into initialization.
	 * @param object object created
	 */
	protected void afterCreateObject(RsBudgetBO<?> object) {
		
	}

	/**
	 * Hook into initialization.
	 * @param object object about to save
	 */
	protected void beforeSaveObject(RsBudgetBO<?> object) {
		
	}
	
	/**
	 * Hook into initialization.
	 * @param object object saved
	 */
	protected void afterSaveObject(RsBudgetBO<?> object) {
		
	}
}
