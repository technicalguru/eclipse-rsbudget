/**
 * 
 */
package rsbudget.view.wizards.bootstrap;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rsbudget.view.wizards.AbstractWizardPage;

/**
 * The initial wizard when database is empty and needs to be filled.
 * @author ralph
 *
 */
public class BootstrapWizard extends Wizard {

	private static Logger log = LoggerFactory.getLogger(BootstrapWizard.class);

	private WelcomePage page1;
	private DbSetupPage page2;
	private PasswordPage page3;
	private AccountPage page4;
	private CategoryPage page5;
	private SummaryPage page6;

	/**
	 * Constructor.
	 */
	public BootstrapWizard() {
		setWindowTitle(BootstrapWizardLanguage.get("initialization"));
		setNeedsProgressMonitor(true);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void addPages() {
		page1 = new WelcomePage();
		addPage(page1);
		page2 = new DbSetupPage();
		addPage(page2);
		page3 = new PasswordPage();
		addPage(page3);
		page4 = new AccountPage();
		addPage(page4);
		page5 = new CategoryPage();
		addPage(page5);
		page6 = new SummaryPage(page2, page4, page5);
		addPage(page6);

		((WizardDialog)getContainer()).addPageChangingListener(new IPageChangingListener() {
			@Override
			public void handlePageChanging(PageChangingEvent event) {
				if (compare(event.getCurrentPage(),event.getTargetPage()) < 0) { 
					event.doit = ((AbstractWizardPage)event.getCurrentPage()).performNext();
					if (event.doit) ((AbstractWizardPage)event.getTargetPage()).performInit();
				}
			}
		});
	}

	/**
	 * Compares both pages regarding their sequence.
	 * @param page1 page 1
	 * @param page2 page 2
	 * @return -1 if page1 is defined to be before page2, 1 is vice versa
	 */
	protected int compare(Object page1, Object page2)  {
		for (IWizardPage page : getPages()) {
			if (page == page1) return -1;
			if (page == page2) return 1;
		}
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performCancel() {
		for (IWizardPage page : getPages()) {
			((AbstractWizardPage)page).performCancel();
		}
		return super.performCancel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performFinish() {
		try {
			Finisher finisher = new Finisher();
			getContainer().run(false, false, finisher);
			return finisher.result;
		} catch (Exception e) {
		}
		return false;
	}

	protected class Finisher implements IRunnableWithProgress {

		public boolean result = true;

		@Override
		public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
			int amount = getPages().length;
			monitor.beginTask("Applying settings", amount);
			try {
				for (IWizardPage page : getPages()) {
					monitor.worked(1);
					String s = ((AbstractWizardPage)page).performFinish(); 
					if (s != null) {
						MessageDialog.openError(getShell(), BootstrapWizardLanguage.get("error.title"), BootstrapWizardLanguage.get("error.message", s));
						result = false;
						break;
					}
				}
			} catch (Exception e) {
				MessageDialog.openError(getShell(), BootstrapWizardLanguage.get("error.title"), BootstrapWizardLanguage.get("error.message", e.getLocalizedMessage()));
				log.error("Error while setting up: ", e);
				result = false;
			}
			if (!result) {
				for (IWizardPage page : getPages()) {
					((AbstractWizardPage)page).performCancel();
				}
			}
			monitor.done();
			return;
		}
	}
}
