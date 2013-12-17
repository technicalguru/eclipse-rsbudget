/**
 * 
 */
package rsbudget.view.wizards.bootstrap;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rs.baselib.util.BeanComparator;
import rs.e4.SwtUtils;
import rs.e4.util.BeanLabelProvider;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Bank;
import rsbudget.data.api.bo.BankInfo;
import rsbudget.data.impl.bo.AccountBO;
import rsbudget.data.impl.bo.BankBO;
import rsbudget.data.impl.dao.BankInfoDAOImpl;
import rsbudget.view.wizards.AbstractWizardPage;

/**
 * Page for editing account data.
 * @author ralph
 *
 */
public class AccountPage extends AbstractWizardPage {

	private Text txtAccountOwner;
	private Text txtAccountNumber;
	private Text txtKontoname;
	private Account account;
	private Bank bank;
	private ComboViewer blzCombo;
	private Label lblBankName;
	private Label lblBankUrl;
	/**
	 * Create the wizard.
	 */
	public AccountPage() {
		super("Zweite Seite");
		setTitle(BootstrapWizardLanguage.get("account.title"));
		setDescription(BootstrapWizardLanguage.get("account.description"));
		bank = new BankBO();
		account = new AccountBO();
		account.setBank(bank);
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
		
		
		Group sctnBank = new Group(container, SWT.NONE);
		sctnBank.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnBank.setText(BootstrapWizardLanguage.get("account.bank"));
		GridLayout gl_sctnBank = new GridLayout(2, false);
		gl_sctnBank.marginRight = 5;
		gl_sctnBank.marginLeft = 5;
		gl_sctnBank.marginBottom = 5;
		sctnBank.setLayout(gl_sctnBank);
		
		Label lblBlz = new Label(sctnBank, SWT.NONE);
		lblBlz.setText(BootstrapWizardLanguage.get("account.bic"));
		
		blzCombo = new ComboViewer(sctnBank, SWT.NONE);
		blzCombo.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				bankChanged(event);
			}
		});
		blzCombo.setContentProvider(new ArrayContentProvider());
		blzCombo.setLabelProvider(new BeanLabelProvider("blz"));
		List<BankInfo> l = BankInfoDAOImpl.loadObjects();
		Collections.sort(l, new BeanComparator<BankInfo>("blz"));
		blzCombo.setInput(l.toArray());
		
		lblBankName = new Label(sctnBank, SWT.NONE);
		lblBankName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		lblBankUrl = new Label(sctnBank, SWT.NONE);
		lblBankUrl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		Group sctnAccount = new Group(container, SWT.NONE);
		sctnAccount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnAccount.setText(BootstrapWizardLanguage.get("account.account"));
		GridLayout gl_sctnAccount = new GridLayout(4, false);
		gl_sctnAccount.marginRight = 5;
		gl_sctnAccount.marginLeft = 5;
		gl_sctnAccount.marginBottom = 5;
		sctnAccount.setLayout(gl_sctnAccount);
		
		Label lblName = new Label(sctnAccount, SWT.NONE);
		lblName.setText(BootstrapWizardLanguage.get("account.name"));
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		txtKontoname = new Text(sctnAccount, SWT.BORDER);
		txtKontoname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtKontoname.addListener(SWT.KeyUp, this);
		new Label(sctnAccount, SWT.NONE);
		new Label(sctnAccount, SWT.NONE);
		
		Label lblKontoinhaber = new Label(sctnAccount, SWT.NONE);
		lblKontoinhaber.setText(BootstrapWizardLanguage.get("account.owner"));
		lblKontoinhaber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		txtAccountOwner = new Text(sctnAccount, SWT.BORDER);
		txtAccountOwner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtAccountOwner.addListener(SWT.KeyUp, this);
		
		Label lblKontonr = new Label(sctnAccount, SWT.NONE);
		lblKontonr.setText(BootstrapWizardLanguage.get("account.number"));
		lblKontonr.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		
		txtAccountNumber = new Text(sctnAccount, SWT.BORDER);
		txtAccountNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		txtAccountNumber.addListener(SWT.KeyUp, this);
	}
	
	/**
	 * Called when bank changed.
	 * @param event
	 */
	protected void bankChanged(SelectionChangedEvent event) {
		BankInfo p = (BankInfo) ((IStructuredSelection)event.getSelection()).getFirstElement();
		lblBankName.setText(p.getName()+" "+p.getLocation());
		String url = p.getUrl();
		if (url == null) url = "";
		lblBankUrl.setText(url);
		bank.setBlz(p.getBlz());
		bank.setName(p.getName()+" "+p.getLocation());
		try {
			bank.setUrl(new URL(p.getUrl()));
		} catch (Exception e) {
			bank.setUrl(null);
		}
		getWizard().getContainer().updateButtons();
	}
	
	/**
	 * Returns the bank.
	 * @return the bank
	 */
	public Bank getBank() {
		return bank;
	}
	
	/**
	 * Returns the account.
	 * @return the account
	 */
	public Account getAccount() {
		// Fill account properties
		account.setActive(true);
		account.setLogin("login");
		account.setPassword("password");
		account.setName(txtKontoname.getText().trim());
		account.setAccountNumber(txtAccountNumber.getText().trim());
		account.setOwner(txtAccountOwner.getText().trim());
		return account;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean canFlipToNextPage() {
		if (getErrorMessage() != null) return false;
		if (!SwtUtils.isTextEmpty(txtAccountNumber) && !SwtUtils.isTextEmpty(txtAccountOwner) 
				&& !SwtUtils.isTextEmpty(txtKontoname) && (bank.getBlz() != null))
			return true;
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
			factory.begin();
			
			// Check existence of bank record and create if required
			Bank bank = getBank();
			Bank dbBank = factory.getBankDAO().findByBlz(bank.getBlz());
			if (dbBank == null) {
				getLog().info("Creating bank");
				Bank newBank = factory.getBankDAO().newInstance();
				newBank.setBlz(bank.getBlz());
				newBank.setName(bank.getName());
				newBank.setUrl(bank.getUrl());
				factory.getBankDAO().create(newBank);
				bank = newBank;
			} else {
				getLog().info("Bank already in database");
				bank = dbBank;
			}
			
			// Check existence of account record and create if required
			Account account = getAccount();
			Account dbAccount = factory.getAccountDAO().findByName(account.getName());
			if (dbAccount == null) {
				getLog().info("Creating account");
				Account newAcc = factory.getAccountDAO().newInstance();
				newAcc.setAccountNumber(account.getAccountNumber());
				newAcc.setBank(bank);
				newAcc.setName(account.getName());
				newAcc.setOwner(account.getOwner());
				newAcc.setActive(true);
				newAcc.setPlanningRelevant(true);
				factory.getAccountDAO().create(newAcc);
			} else {
				getLog().info("Account already in database");
				dbAccount.setActive(true);
				dbAccount.setPlanningRelevant(true);
				factory.getAccountDAO().save(dbAccount);
			}
			
			factory.commit();
		} catch (Exception e) {
			if (factory != null) factory.rollback();
			getLog().error("Error while setting up: ", e);
			return e.getLocalizedMessage();
		}
		return null;
	}


}
