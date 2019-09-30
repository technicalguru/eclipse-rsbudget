/**
 * 
 */
package rsbudget.hbci;

import org.eclipse.jface.viewers.LabelProvider;

import rsbudget.util.CurrencyLabelProvider;

/**
 * Label provider for the assignment of TX
 * @author ralph
 *
 */
public class AssigningLabelProvider extends LabelProvider {

	public static AssigningLabelProvider INSTANCE = new AssigningLabelProvider();
	
	private CurrencyLabelProvider clp = CurrencyLabelProvider.INSTANCE;
	
	/**
	 * Constructor.
	 */
	public AssigningLabelProvider() {
	}

	@Override
	public String getText(Object element) {
		if (element == null) return "";
		if (element instanceof AssignmentWrapper) {
			AssignmentWrapper wrapper = (AssignmentWrapper)element;
			switch (wrapper.getAction()) {
			case IGNORE:     return "<Ignore>";
			case NEW:        return "<New>";
			case SEPARATOR:  return "----------------";
			case BUDGET:     return clip(wrapper.getBudget().getName())+" ("+clp.getText(wrapper.getBudget().getAvailable())+")";
			case PLANNED_TX: return clip(wrapper.getPlannedTransaction().getName())+" ("+clp.getText(wrapper.getPlannedTransaction().getAmount())+")";
			case ASSIGNED:   return "*"+clip(wrapper.getTransaction().getText())+" ("+clp.getText(wrapper.getTransaction().getAmount())+")";
			case CATEGORY:   return clip(wrapper.getCategory().getName());
			}
		}
		return "$"+element.toString()+"$";
	}

	private String clip(String rc) {
		if (rc.length() > 30) rc = rc.substring(0,  30)+"...";
		return rc;
	}
}
