/**
 * 
 */
package rsbudget.parts.budgets;

import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ILabelProvider;

import rs.e4.util.AbstractColumnLabelProvider;
import rsbudget.data.util.PlannedPeriod;
import rsbudget.data.util.SequenceNumber;

/**
 * @author ralph
 *
 */
public class SequenceLabelProvider extends AbstractColumnLabelProvider {

	/**
	 * Constructor.
	 */
	public SequenceLabelProvider(LocalResourceManager resourceManager) {
		super(resourceManager);
	}

	/**
	 * Constructor.
	 */
	public SequenceLabelProvider(ILabelProvider valueLabelProvider, LocalResourceManager resourceManager) {
		super(valueLabelProvider, resourceManager);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getValue(Object element) {
		try {
			if (element == null) return getNullValue();
			if (((BudgetRowWrapper)element).isBudget()) return getNullValue();
			PlannedPeriod period = (PlannedPeriod)PropertyUtils.getProperty(element, "plannedPeriod");
			switch (period) {
			case MONTHLY:
			case WEEKLY:
				return getNullValue();
			default:
			}
			int value = (Integer)PropertyUtils.getProperty(element, "sequenceNumber");
			SequenceNumber rc = SequenceNumber.getSequence(value, period);
			return rc;
		} catch (Throwable t) {
			throw new RuntimeException("Cannot retrieve value:", t);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getErrorValue(Throwable t) {
		return "";
	}

}
