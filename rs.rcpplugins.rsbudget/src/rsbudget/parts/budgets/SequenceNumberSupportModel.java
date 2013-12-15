/**
 * 
 */
package rsbudget.parts.budgets;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import rs.e4.celledit.AbstractComboBoxEditingSupportModel;
import rsbudget.data.util.PlannedPeriod;
import rsbudget.data.util.SequenceNumber;

/**
 * Support model for the sequence number.
 * @author ralph
 *
 */
public class SequenceNumberSupportModel extends AbstractComboBoxEditingSupportModel {

	/**
	 * Constructor.
	 */
	public SequenceNumberSupportModel() {
		super("sequenceNumber");
		setSorter(null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay(Object object) {
		return object.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<?> createOptions(Object element) {
		List<SequenceNumber> rc = new ArrayList<>();
		PlannedPeriod period = ((BudgetRowWrapper)element).getPlannedPeriod();
		switch (period) {
		case QUARTERLY:
		case HALF_YEARLY:
		case YEARLY:
			return SequenceNumber.getList(period);
		default:
		}
		return rc;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Object _getValue(Object element) throws Exception {
		PlannedPeriod period = ((BudgetRowWrapper)element).getPlannedPeriod();
		int rc = (Integer)PropertyUtils.getProperty(element, getBeanProperty());
		switch (period) {
		case QUARTERLY: 
		case HALF_YEARLY:
		case YEARLY: 
			return SequenceNumber.getSequence(rc, period);
		default:
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _setValue(Object element, Object value) throws Exception {
		super._setValue(element, ((SequenceNumber)value).getNo());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canEdit(Object element) {
		PlannedPeriod period = ((BudgetRowWrapper)element).getPlannedPeriod();
		switch (period) {
		case QUARTERLY: 
		case HALF_YEARLY: 
		case YEARLY: 
			return true;
		default:
		}
		return false;
	}


}
