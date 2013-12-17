/**
 * 
 */
package rsbudget.data.impl.bo;

import rs.baselib.util.RsDate;
import rs.data.impl.bo.AbstractGeneralBO;
import rs.data.type.UrlKey;

/**
 * An abstract implementtaion for filebased objects.
 * @author ralph
 *
 */
public abstract class AbstractRsBudgetFileBO<K extends UrlKey<?>> extends AbstractGeneralBO<K> {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 1L;

	private K id;
	private RsDate creationDate = new RsDate();
	private RsDate changeDate = new RsDate();
	
	/**
	 * Constructor.
	 */
	public AbstractRsBudgetFileBO() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public AbstractRsBudgetFileBO(K id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public K getId() {
		return id;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(K id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getCreationDate() {
		return creationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationDate(RsDate creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RsDate getChangeDate() {
		return changeDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChangeDate(RsDate changeDate) {
		this.changeDate = changeDate;
	}

	
}
