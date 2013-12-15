/**
 * 
 */
package rsbudget.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import rs.baselib.util.BeanComparator;
import rs.baselib.util.IValueProvider;
import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;

/**
 * A general purpose content provider which retrieves data from DAOs.
 * @author ralph
 *
 */
public class DaoContentProvider<T extends IGeneralBO<?>> implements IStructuredContentProvider {

	private IGeneralDAO<?, T> dao;
	private Method retrievalMethod;
	private Comparator<T> comparator;
	private Set<T> filtered;
	private Set<T> additional;
	private FilterPredicate filterPredicate;
	
	/**
	 * Constructor.
	 */
	public DaoContentProvider(IGeneralDAO<?, T> dao) {
		this(dao, null, null);
	}

	/**
	 * Constructor.
	 */
	public DaoContentProvider(IGeneralDAO<?, T> dao, IValueProvider valueProvider) {
		this(dao, new BeanComparator<T>(valueProvider));
	}

	/**
	 * Constructor.
	 */
	public DaoContentProvider(IGeneralDAO<?, T> dao, Comparator<T> comparator) {
		this(dao, comparator, null);
	}

	/**
	 * Constructor.
	 */
	public DaoContentProvider(IGeneralDAO<?, T> dao, Method retrievalMethod) {
		this(dao, null, retrievalMethod);
	}

	/**
	 * Constructor.
	 */
	public DaoContentProvider(IGeneralDAO<?, T> dao, Comparator<T> comparator, Method retrievalMethod) {
		this.dao = dao;
		this.filtered = new HashSet<>();
		this.additional = new HashSet<>();
		filterPredicate = new FilterPredicate();
		setComparator(comparator);
		setRetrievalMethod(retrievalMethod);
	}

	/**
	 * Returns the dao.
	 * @return the dao
	 */
	public IGeneralDAO<?, ?> getDao() {
		return dao;
	}

	/**
	 * Sets the dao.
	 * @param dao the dao to set
	 */
	protected void setDao(IGeneralDAO<?, T> dao) {
		this.dao = dao;
	}

	/**
	 * Returns the comparator.
	 * @return the comparator
	 */
	public Comparator<T> getComparator() {
		return comparator;
	}

	/**
	 * Sets the comparator.
	 * @param comparator the comparator to set
	 */
	protected void setComparator(Comparator<T> comparator) {
		this.comparator = comparator;
	}

	/**
	 * Returns the retrievalMethod.
	 * @return the retrievalMethod
	 */
	public Method getRetrievalMethod() {
		return retrievalMethod;
	}

	/**
	 * Returns the {@link #filtered}.
	 * @return the filtered
	 */
	public Set<T> getFiltered() {
		return filtered;
	}

	/**
	 * Returns the {@link #additional}.
	 * @return the additional
	 */
	public Set<T> getAdditional() {
		return additional;
	}

	/**
	 * Sets the retrievalMethod.
	 * @param retrievalMethod the retrievalMethod to set
	 */
	protected void setRetrievalMethod(Method retrievalMethod) {
		// Assure health
		if (retrievalMethod != null) {
			if (retrievalMethod.getParameterTypes().length > 0) {
				throw new RuntimeException(retrievalMethod.getName()+" must not take arguments");
			}
			if (!retrievalMethod.getReturnType().isAssignableFrom(List.class)) {
				throw new RuntimeException(retrievalMethod.getName()+" does not return java.util.List");
			}
		}
		this.retrievalMethod = retrievalMethod;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] getElements(Object inputElement) {
		List<T> rc = new ArrayList<T>();
		
		// Start transaction and retrieve objects
		try {
			dao.getFactory().begin();
			Method m = getRetrievalMethod();
			if (m != null) {
				rc.addAll((List<T>)m.invoke(dao));
			} else {
				rc.addAll(dao.findDefaultAll());
			}
			dao.getFactory().commit();
		} catch (Exception e) {
			dao.getFactory().rollback();
			throw new RuntimeException("Cannot get Objects", e);
		}
		
		// Filter any objects
		CollectionUtils.filter(rc, filterPredicate);
		// Add any others
		rc.addAll(additional);
		
		// Sort objects if required
		Comparator<T> c = getComparator();
		if (c != null) Collections.sort(rc, c);
		
		return rc.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dispose() {
	}

	/**
	 * Filters the objects collected by those that shall be hidden.
	 * @author ralph
	 *
	 */
	protected class FilterPredicate implements Predicate {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean evaluate(Object object) {
			return !filtered.contains(object);
		}
		
	}
}
