package rsbudget.view.wizards.bootstrap;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import rsbudget.view.wizards.AbstractWizardPage;

/**
 * Get default category.
 * @author ralph
 *
 */
public class LanguageSelectionPage extends AbstractWizardPage {
	
	private ComboViewer languageSelection;

	/**
	 * Create the wizard.
	 */
	public LanguageSelectionPage() {
		super("Init Seite");
		setTitle("Language");
		setDescription("Please select your setup language!");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		GridLayout gl_container = new GridLayout(1, false);
		gl_container.marginRight = 5;
		gl_container.marginLeft = 5;
		gl_container.marginBottom = 5;
		container.setLayout(gl_container);
		
		languageSelection = new ComboViewer(container, SWT.BORDER);
		languageSelection.setContentProvider(new ArrayContentProvider());
		languageSelection.setInput(BootstrapWizardLanguage.LANGUAGES);
		languageSelection.setSelection(new StructuredSelection(BootstrapWizardLanguage.LANGUAGES[0]));
		languageSelection.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canFlipToNextPage() {
		BootstrapWizardLanguage.setLanguage((String)((StructuredSelection)languageSelection.getSelection()).getFirstElement());
		return true;
	}
	
	
}
