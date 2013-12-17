/**
 * 
 */
package rsbudget.composites;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.util.BeanComparator;
import rs.e4.util.BeanLabelProvider;
import rsbudget.Plugin;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.Bank;
import rsbudget.data.api.bo.BankInfo;
import rsbudget.data.api.dao.AccountDAO;
import rsbudget.data.api.dao.BankDAO;
import rsbudget.data.impl.bo.AccountBO;
import rsbudget.data.impl.dao.BankInfoDAOImpl;

/**
 * Composite containing account data.
 * @author ralph
 *
 */
public class AccountPanel extends Composite {

	private static Logger log = LoggerFactory.getLogger(AccountPanel.class);
	
	private Text txtAccountOwner;
	private Text txtAccountNumber;
	private Text txtKontoname;
	private Account account;
	private ComboViewer blzCombo;
	private Label lblBankName;
	private Label lblBankUrl;
	private DataBindingContext bindingContext;
	private Set<Account> changedAccounts = new HashSet<>();
	private long nextId = -1;
	
	/**
	 * Constructor.
	 */
	public AccountPanel(Composite parent, int style) {
		super(parent, style);
		createContents();
	}

	/**
	 * Creates the content of the panel.
	 */
	protected void createContents() {
		GridLayout gl_container = new GridLayout(1, false);
		setLayout(gl_container);
		account = RsBudgetModelService.INSTANCE.getFactory().getAccountDAO().newInstance();
		
		Group sctnBank = new Group(this, SWT.NONE);
		sctnBank.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnBank.setText(Plugin.translate("panels.account.bank.label"));
		GridLayout gl_sctnBank = new GridLayout(2, false);
		gl_sctnBank.marginRight = 5;
		gl_sctnBank.marginLeft = 5;
		gl_sctnBank.marginBottom = 5;
		sctnBank.setLayout(gl_sctnBank);

		Label lblBlz = new Label(sctnBank, SWT.NONE);
		lblBlz.setText(Plugin.translate("panels.account.blz.label"));

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

		Group sctnAccount = new Group(this, SWT.NONE);
		sctnAccount.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnAccount.setText(Plugin.translate("panels.account.account.label"));
		GridLayout gl_sctnAccount = new GridLayout(4, false);
		gl_sctnAccount.marginRight = 5;
		gl_sctnAccount.marginLeft = 5;
		gl_sctnAccount.marginBottom = 5;
		sctnAccount.setLayout(gl_sctnAccount);

		Label lblName = new Label(sctnAccount, SWT.NONE);
		lblName.setText(Plugin.translate("panels.account.name.label"));
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtKontoname = new Text(sctnAccount, SWT.BORDER);
		txtKontoname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(sctnAccount, SWT.NONE);
		new Label(sctnAccount, SWT.NONE);

		Label lblKontoinhaber = new Label(sctnAccount, SWT.NONE);
		lblKontoinhaber.setText(Plugin.translate("panels.account.owner.label"));
		lblKontoinhaber.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtAccountOwner = new Text(sctnAccount, SWT.BORDER);
		txtAccountOwner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		Label lblKontonr = new Label(sctnAccount, SWT.NONE);
		lblKontonr.setText(Plugin.translate("panels.account.number.label"));
		lblKontonr.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		txtAccountNumber = new Text(sctnAccount, SWT.BORDER);
		txtAccountNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		bindingContext = createDataBinding();
	}

	protected DataBindingContext createDataBinding() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeOwnerText = WidgetProperties.text(SWT.Modify).observe(txtAccountOwner);
		IObservableValue accountOwnerObserveValue = BeanProperties.value("owner").observe(account);
		bindingContext.bindValue(observeOwnerText, accountOwnerObserveValue, null, null);
		//
		IObservableValue observeNumberText = WidgetProperties.text(SWT.Modify).observe(txtAccountNumber);
		IObservableValue accountNumberObserveValue = BeanProperties.value("accountNumber").observe(account);
		bindingContext.bindValue(observeNumberText, accountNumberObserveValue, null, null);
		//
		IObservableValue observeNameText = WidgetProperties.text(SWT.Modify).observe(txtKontoname);
		IObservableValue accountNameObserveValue = BeanProperties.value("name").observe(account);
		bindingContext.bindValue(observeNameText, accountNameObserveValue, null, null);
		//
		bindingContext.updateModels();
		return bindingContext;
	}
	
	protected void bankChanged(SelectionChangedEvent evt) {
		BankInfo bankInfo = (BankInfo) ((IStructuredSelection)evt.getSelection()).getFirstElement();
		if (bankInfo != null) {
			lblBankName.setText(bankInfo.getName());
			lblBankUrl.setText(bankInfo.getUrl() != null ? bankInfo.getUrl() : "");
			// Assign the bank to the account for later usage
			RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory(); 
			BankDAO dao = factory.getBankDAO();
			try {
				factory.begin();
				Bank bank = dao.findByBlz(bankInfo.getBlz());
				if (bank == null) {
					bank = dao.newInstance(bankInfo);
				}
				this.account.setBank(bank);
				factory.commit();
			} catch (Exception e) {
				try { factory.rollback(); } catch (Exception e2) {}
			}
			
		} else {
			lblBankName.setText("");
			lblBankUrl.setText("");
		}
	}
	
	public void setAccount(Account account) {
		rememberAccountData();
		if (account != null) {
			try {
				account.copyTo(this.account);
				Long id = account.getId();
				if (id == null) id = createNewId();
				((AccountBO)this.account).setId(id);
				this.account.setChanged(false);
				blzCombo.setSelection(new StructuredSelection(RsBudgetModelService.INSTANCE.getFactory().getBankInfoDAO().findBy(account.getBank().getBlz())));
			} catch (Exception e) {
				log.error("Cannot copy properties", e);
			}
		} else {
			blzCombo.setSelection(new StructuredSelection());
			this.account.reset();
		}
		bindingContext.updateModels();
	}

	protected Long createNewId() {
		nextId--;
		return Long.valueOf(nextId+1);
	}
	
	protected void rememberAccountData() {
		if (this.account.isChanged() && (this.account.getId() != null)) {
			AccountBO chAccount = (AccountBO)RsBudgetModelService.INSTANCE.getFactory().getAccountDAO().newInstance();
			this.account.copyTo(chAccount);
			chAccount.setId(this.account.getId());
			changedAccounts.add(chAccount);
		}		
	}
	
	/** 
	 * Saves all changes.
	 */
	public void saveChanges() {
		AccountDAO dao = RsBudgetModelService.INSTANCE.getFactory().getAccountDAO();
		rememberAccountData();
		for (Account account : changedAccounts) {
			Long id = account.getId();
			Account db = dao.findBy(id);
			if (db == null) {
				// New account
				db = dao.newInstance();
				account.copyTo(db);
				saveBank(db);
				dao.create(db);
			} else {
				// existing account
				account.copyTo(db);
				saveBank(db);
				dao.save(db);
			}
		}
	}
	
	public void removeAccount(Account account) {
		changedAccounts.remove(account);
	}
	
	protected void saveBank(Account account) {
		BankDAO dao = RsBudgetModelService.INSTANCE.getFactory().getBankDAO();
		Bank bank = account.getBank();
		if (bank.getId() == null) {
			Bank dbBank = dao.findByBlz(bank.getBlz());
			if (dbBank != null) {
				account.setBank(dbBank);
			} else {
				dao.create(bank);
			}
		}
	}
	
	/**
	 * Returns the account.
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}
}
