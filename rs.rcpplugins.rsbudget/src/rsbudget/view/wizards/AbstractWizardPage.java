/**
 * 
 */
package rsbudget.view.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;

/**
 * Basic methods for wizards.
 * @author ralph
 *
 */
public abstract class AbstractWizardPage extends WizardPage implements Listener {

	private RsBudgetDaoFactory factory;
	
	/**
	 * Constructor.
	 */
	public AbstractWizardPage(String name) {
		super(name);
	}

	/**
	 * Returns the factory.
	 * @return the factory
	 */
	public RsBudgetDaoFactory getFactory() {
		if (factory == null) {
			factory = RsBudgetModelService.INSTANCE.getFactory();
		}
		return factory;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Updates buttons.
	 */
	@Override
	public void handleEvent(Event event) {
		getWizard().getContainer().updateButtons();
	}

	/**
	 * Pages can initialize themselves here right before they become visible.
	 * Default implementation does nothing.
	 */
	public void performInit() {
	}

	/**
	 * Pages can perform some tasks here right before the next page opens.
	 * Default implementation does nothing.
	 * @return <code>true</code> when next page can be opened, <code>false</code> otherwise
	 */
	public boolean performNext() {
		return true;
	}

	/**
	 * Pages can perform some tasks here when the wizard was finished.
	 * Default implementation does nothing.
	 * @return <code>null</code> when finish request was accepted, error message otherwise
	 */
	public String performFinish() {
		return null;
	}

	/**
	 * Pages can perform some tasks here when the wizard was cancelled.
	 * Default implementation does nothing.
	 */
	public void performCancel() {
	}
	
	/**
	 * Returns the logger.
	 * @return the logger
	 */
	protected Logger getLog() {
		return LoggerFactory.getLogger(getClass());
	}
}
