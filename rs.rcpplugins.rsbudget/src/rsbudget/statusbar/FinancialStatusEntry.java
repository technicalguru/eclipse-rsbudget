/**
 * 
 */
package rsbudget.statusbar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;

import rsbudget.util.CurrencyLabelProvider;

/**
 * Displays the status text.
 * @author ralph
 *
 */
public class FinancialStatusEntry {

	/** The Event broker topic */
	public static final String TOPIC = "FINANCIAL_STATUS_TOPIC";
	
	private CLabel label;
	
	@Inject
	EModelService modelService;

	/**
	 * Constructor.
	 */
	public FinancialStatusEntry() {
	}

	@PostConstruct
	void createWidget(Composite parent, MToolControl toolControl) {
		label = new CLabel(parent, SWT.BORDER);
		label.setAlignment(SWT.RIGHT);
		setStatus(999999.9f);
	}
	
	@PreDestroy
	void destroy() {
	}

	@Inject @Optional
	public void setStatus(@UIEventTopic(TOPIC) Float amount) {
		if (amount == null) amount = 0f;
		label.setText(" "+CurrencyLabelProvider.INSTANCE.getText(amount)+" ");
	}
}
