package rsbudget.view.wizards.bootstrap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rs.baselib.crypto.EncryptionUtils;
import rs.e4.SwtUtils;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.dao.SettingDAO;
import rsbudget.view.wizards.AbstractWizardPage;

/**
 * Sets the password.
 * @author ralph
 *
 */
public class PasswordPage extends AbstractWizardPage {

	private Text txtPassword;
	private Text txtRepeatPassword;

	/**
	 * Create the wizard.
	 */
	public PasswordPage() {
		super("Passwort setzen");
		setTitle(BootstrapWizardLanguage.get("password.title"));
		setDescription(BootstrapWizardLanguage.get("password.description"));
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		GridLayout gl_container = new GridLayout(3, false);
		gl_container.marginRight = 10;
		gl_container.marginLeft = 10;
		gl_container.marginBottom = 10;
		gl_container.marginTop = 10;
		container.setLayout(gl_container);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		lblNewLabel.setText(BootstrapWizardLanguage.get("password.not-saved-on-system"));
		new Label(container, SWT.NONE);

		Label lblNeuesPasswort = new Label(container, SWT.NONE);
		GridData gd_lblNeuesPasswort = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblNeuesPasswort.verticalIndent = 30;
		lblNeuesPasswort.setLayoutData(gd_lblNeuesPasswort);
		lblNeuesPasswort.setText(BootstrapWizardLanguage.get("password.passwd"));

		txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setText("");
		GridData gd_txtPassword = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtPassword.verticalIndent = 30;
		txtPassword.setLayoutData(gd_txtPassword);
		txtPassword.addListener(SWT.KeyUp, this);

		Label label = new Label(container, SWT.NONE);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNeuesPasswortWiederholen = new Label(container, SWT.NONE);
		lblNeuesPasswortWiederholen.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNeuesPasswortWiederholen.setText(BootstrapWizardLanguage.get("password.repeat"));

		txtRepeatPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		txtRepeatPassword.setText("");
		txtRepeatPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtRepeatPassword.addListener(SWT.KeyUp, this);
		new Label(container, SWT.NONE);

	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean canFlipToNextPage() {
		if (getErrorMessage() != null) return false;
		if (!SwtUtils.isTextEmpty(txtPassword) && !SwtUtils.isTextEmpty(txtRepeatPassword)) return true;
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPageComplete() {
		return canFlipToNextPage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performFinish() {
		RsBudgetDaoFactory factory = null;
		try {
			factory = RsBudgetModelService.INSTANCE.getFactory();
			if (factory == null) return BootstrapWizardLanguage.get("password.error.nofactory");
			factory.begin();
			char passwd[] = getPassword();
			// Check if settings were made earlier!
			SettingDAO dao = factory.getSettingDAO();
			if (!dao.hasApplicationKey()) {
				getLog().info("Applying new password settings");
				dao.setUserPassword(passwd);
				dao.setApplicationKey(EncryptionUtils.generatePassword(35));
				dao.setApplicationSalt(EncryptionUtils.generateSalt());
			} else {
				getLog().info("Password settings already exist, checking password");
				dao.setUserPassword(getPassword());
				if (!dao.checkUserPassword()) {
					return "The given user password does not match. Change the password settings!"; 
				}
			}
			factory.commit();
			return null;
		} catch (Exception e) {
			if (factory != null) factory.rollback();
			getLog().error("Error while setting up: ", e);
			return e.getLocalizedMessage();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleEvent(Event event) {
		super.handleEvent(event);
		if (event.type == SWT.KeyUp) {
			boolean p1Filled = !SwtUtils.isTextEmpty(txtPassword);
			boolean p2Filled = !SwtUtils.isTextEmpty(txtRepeatPassword);
			if (p1Filled && !p2Filled) setMessage(BootstrapWizardLanguage.get("password.warn.repeat"), WARNING);
			if (!p1Filled && p2Filled) setMessage(BootstrapWizardLanguage.get("password.warn.fill"), WARNING);
			if (!p1Filled && !p2Filled) {
				setMessage(null, WARNING);
				setErrorMessage(null);
			}
			if (p1Filled && p2Filled) {
				String p1 = txtPassword.getText().trim();
				String p2 = txtRepeatPassword.getText().trim();
				if (p1.equals(p2)) {
					setMessage(null, WARNING);
					setErrorMessage(null);
				} else {
					setErrorMessage(BootstrapWizardLanguage.get("password.error.notequal"));
				}
			}
			getWizard().getContainer().updateButtons();
		}
	}

	public char[] getPassword() {
		return txtPassword.getTextChars();
	}
}
