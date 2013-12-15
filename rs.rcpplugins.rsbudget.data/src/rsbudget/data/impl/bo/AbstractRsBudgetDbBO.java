/**
 * 
 */
package rsbudget.data.impl.bo;

import rs.data.hibernate.bo.AbstractHibernateLongBO;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.dto.RsBudgetDTO;

/**
 * Base class for all BO implementations in database.
 * @author ralph
 *
 */
public abstract class AbstractRsBudgetDbBO<T extends RsBudgetDTO> extends AbstractHibernateLongBO<T> implements RsBudgetBO<Long> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 4972016548179874542L;

	/**
	 * Constructor.
	 */
	public AbstractRsBudgetDbBO() {
	}

	/**
	 * Constructor.
	 */
	public AbstractRsBudgetDbBO(T transferObject) {
		super(transferObject);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay() {
		return toString();
	}

}
