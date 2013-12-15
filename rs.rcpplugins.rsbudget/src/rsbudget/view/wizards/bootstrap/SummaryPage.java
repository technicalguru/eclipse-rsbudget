package rsbudget.view.wizards.bootstrap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import rs.baselib.util.IDisplayProvider;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Bank;
import rsbudget.view.wizards.AbstractWizardPage;

/**
 * Summarizes the input before completion of the wizard.
 * @author ralph
 *
 */
public class SummaryPage extends AbstractWizardPage {
	
	private DbSetupPage dbSetupPage;
	private AccountPage accountPage;
	private CategoryPage categoryPage;
	private Label lblDbType;
	private Label lblEncryption;
	private Label lblAccountName;
	private Label lblAccountOwner;
	private Label lblAccountNumber;
	private Label lblBankname;
	private Label lblBlz;
	private Label lblUrl;
	private Label lblCategoryName;
	
	/**
	 * Create the wizard.
	 */
	public SummaryPage(DbSetupPage dbSetupPage, AccountPage accountPage, CategoryPage categoryPage) {
		super("Dritte Seite");
		this.dbSetupPage = dbSetupPage;
		this.accountPage = accountPage;
		this.categoryPage = categoryPage;
		setTitle(BootstrapWizardLanguage.get("summary.title"));
		setDescription(BootstrapWizardLanguage.get("summary.description"));
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

		Group sctnDbType = new Group(container, SWT.NONE);
		sctnDbType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnDbType.setText(BootstrapWizardLanguage.get("dbsetup.database"));
		GridLayout gl_composite_0 = new GridLayout(4, false);
		gl_composite_0.marginRight = 5;
		gl_composite_0.marginLeft = 5;
		gl_composite_0.marginBottom = 5;
		gl_composite_0.verticalSpacing = 7;
		gl_composite_0.horizontalSpacing = 10;
		sctnDbType.setLayout(gl_composite_0);
		
		Label lblName_0 = new Label(sctnDbType, SWT.NONE);
		lblName_0.setText(BootstrapWizardLanguage.get("dbsetup.dbtype"));
		lblName_0.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		lblDbType = new Label(sctnDbType, SWT.NONE);
		lblDbType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		lblName_0 = new Label(sctnDbType, SWT.NONE);
		lblName_0.setText(BootstrapWizardLanguage.get("dbsetup.encryption-label")+":");
		lblName_0.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		lblEncryption = new Label(sctnDbType, SWT.NONE);
		lblEncryption.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		
		Group sctnBank = new Group(container, SWT.NONE);
		sctnBank.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnBank.setText(BootstrapWizardLanguage.get("account.account"));
		GridLayout gl_composite_1 = new GridLayout(4, false);
		gl_composite_1.marginRight = 5;
		gl_composite_1.marginLeft = 5;
		gl_composite_1.marginBottom = 5;
		gl_composite_1.verticalSpacing = 7;
		gl_composite_1.horizontalSpacing = 10;
		sctnBank.setLayout(gl_composite_1);
		
		Label lblName_1 = new Label(sctnBank, SWT.NONE);
		lblName_1.setText(BootstrapWizardLanguage.get("account.name"));
		lblName_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		lblAccountName = new Label(sctnBank, SWT.NONE);
		lblAccountName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblBlzLabel = new Label(sctnBank, SWT.NONE);
		lblBlzLabel.setText(BootstrapWizardLanguage.get("account.bic"));
		lblBlzLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		lblBlz = new Label(sctnBank, SWT.NONE);
		lblBlz.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		Label lblKontoinhaber = new Label(sctnBank, SWT.NONE);
		lblKontoinhaber.setText(BootstrapWizardLanguage.get("account.owner"));
		lblKontoinhaber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		lblAccountOwner = new Label(sctnBank, SWT.NONE);
		lblAccountOwner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblKontonummer = new Label(sctnBank, SWT.NONE);
		lblKontonummer.setText(BootstrapWizardLanguage.get("account.number"));
		lblKontonummer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		lblAccountNumber = new Label(sctnBank, SWT.NONE);
		lblAccountNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblName = new Label(sctnBank, SWT.NONE);
		lblName.setText(BootstrapWizardLanguage.get("account.bank")+":");
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		lblBankname = new Label(sctnBank, SWT.NONE);
		GridData gd_lblBankname = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lblBankname.horizontalSpan = 3;
		lblBankname.setLayoutData(gd_lblBankname);
		
		Label lblFintanUrl = new Label(sctnBank, SWT.NONE);
		lblFintanUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		lblUrl = new Label(sctnBank, SWT.NONE);
		GridData gd_lblUrl = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_lblUrl.horizontalSpan = 3;
		lblUrl.setLayoutData(gd_lblUrl);
		
		Group sctnKategorien = new Group(container, SWT.NONE);
		sctnKategorien.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnKategorien.setText(BootstrapWizardLanguage.get("summary.category"));		
		GridLayout gl_sctnKategorien = new GridLayout(2, false);
		gl_sctnKategorien.marginRight = 5;
		gl_sctnKategorien.marginLeft = 5;
		gl_sctnKategorien.marginBottom = 5;
		sctnKategorien.setLayout(gl_sctnKategorien);
		
		Label lblName_2 = new Label(sctnKategorien, SWT.NONE);
		lblName_2.setText(BootstrapWizardLanguage.get("category.name"));
		
		lblCategoryName = new Label(sctnKategorien, SWT.NONE);
		lblCategoryName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible) updateLabels();
		super.setVisible(visible);
	}
	
	public void updateLabels() {
		IDisplayProvider dbProvider = (IDisplayProvider)dbSetupPage.getDbProvider();
		lblDbType.setText(dbProvider.getDisplay());
		lblEncryption.setText(BootstrapWizardLanguage.get(dbSetupPage.isEncryption() ? "label.on" : "label.off"));
		
		Bank bank = accountPage.getBank();
		lblBankname.setText(bank.getName());
		lblBlz.setText(bank.getBlz());
		lblUrl.setText(bank.getUrl() != null ? bank.getUrl().toExternalForm() : "");
		
		Account account = accountPage.getAccount();
		lblAccountName.setText(account.getName());
		lblAccountNumber.setText(account.getAccountNumber());
		lblAccountOwner.setText(account.getOwner());
		
		lblCategoryName.setText(categoryPage.getCategoryName());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPageComplete() {
		return true;
	}


}
