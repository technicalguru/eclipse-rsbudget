/**
 * 
 */
package rsbudget.parts.transactions;

import rs.e4.celledit.BeanEditingSupportModel;

/**
 * Controls the fields to be edited.
 * @author ralph
 *
 */
public class TransactionSupportModel extends BeanEditingSupportModel {

	/**
	 * Constructor.
	 */
	public TransactionSupportModel(String beanProperty) {
		super(beanProperty);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canEdit(Object element) {
		String s = getBeanProperty();
		if (s.equals("valueDate") || s.equals("actualAmount") || s.equals("details") || s.equals("txBudget")) {
			if (element instanceof TxRowWrapper) {
				return  !((TxRowWrapper)element).isBudget();
			}
		}
		return super.canEdit(element);
	}

	
}
