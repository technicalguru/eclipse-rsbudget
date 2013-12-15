package rsbudget.view.wizards.bootstrap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import rsbudget.view.wizards.AbstractWizardPage;

/**
 * Summarizes the input before completion of the wizard.
 * @author ralph
 *
 */
public class WelcomePage extends AbstractWizardPage {
		
	/**
	 * Create the wizard.
	 */
	public WelcomePage() {
		super("Willkommen");
		setTitle(BootstrapWizardLanguage.get("welcome.title"));
		setDescription(BootstrapWizardLanguage.get("welcome.description"));
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		GridLayout gl_container = new GridLayout(1, false);
		container.setLayout(gl_container);
		
		StyledText styledText = new StyledText(container, SWT.WRAP | SWT.TRANSPARENT);
		styledText.setText(BootstrapWizardLanguage.get("welcome.text"));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.widthHint = 400;
		styledText.setLayoutData(gd);
		styledText.setEditable(false);
		styledText.setBackground(container.getBackground());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPageComplete() {
		return true;
	}

	
}
