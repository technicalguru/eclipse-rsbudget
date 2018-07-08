package rsbudget.preferences;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.lang.LangUtils;
import rs.baselib.prefs.IPreferences;
import rs.baselib.util.BeanComparator;
import rs.e4.celledit.BeanEditingSupportModel;
import rs.e4.celledit.CellEditorActivationStrategy;
import rs.e4.celledit.CheckboxEditingSupport;
import rs.e4.celledit.ComboBoxEditingSupport;
import rs.e4.celledit.IComboBoxEditingSupportModel;
import rs.e4.celledit.IEditingSupportModel;
import rs.e4.celledit.TextEditingSupport;
import rs.e4.jface.databinding.FloatToStringConverter;
import rs.e4.jface.databinding.FloatValidator;
import rs.e4.jface.databinding.IntegerToStringConverter;
import rs.e4.jface.databinding.StringToBigDecimalConverter;
import rs.e4.jface.databinding.StringToIntegerConverter;
import rs.e4.swt.ColorButtonViewer;
import rs.e4.swt.action.AddDaoRowAction;
import rs.e4.swt.action.MoveDownAction;
import rs.e4.swt.action.MoveUpAction;
import rs.e4.swt.action.RemoveRowAction;
import rs.e4.swt.events.IntegerVerifyListener;
import rs.e4.util.AbstractColumnLabelProvider;
import rs.e4.util.BeanColumnLabelProvider;
import rs.e4.util.BeanLabelProvider;
import rs.e4.util.CheckboxImageProvider;
import rs.e4.util.EmptyLabelProvider;
import rs.e4.util.MapLabelProvider;
import rsbudget.Plugin;
import rsbudget.celledit.BoSelectionSupportModel;
import rsbudget.celledit.MapSelectionSupportModel;
import rsbudget.composites.AccountPanel;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.BudgetRecognition;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.CategoryRecognition;
import rsbudget.data.api.bo.HistoricalItem;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.Setting;
import rsbudget.data.api.dao.AccountDAO;
import rsbudget.data.api.dao.BudgetRecognitionDAO;
import rsbudget.data.api.dao.CategoryRecognitionDAO;
import rsbudget.data.api.dao.HistoricalItemDAO;
import rsbudget.data.api.dao.SettingDAO;
import rsbudget.data.impl.bo.AccountBO;
import rsbudget.util.DaoContentProvider;
import rsbudget.util.TableColumnResizeListener;

public class PreferencesDialog extends TitleAreaDialog {

	private static Logger log = LoggerFactory.getLogger(PreferencesDialog.class);

	private LocalResourceManager resourceManager;

	// GENERAL tab
	private TabItem tabGeneral;
	private ComboViewer userLanguageViewer;
	private Combo userLanguageCombo;
	private ComboViewer currencyViewer;
	private Combo currencyCombo;
	private Button resetWindowButton;
	private Link restartLink;

	// NETWORK tab
	private TabItem tabNetwork;
	private Button noProxyButton;
	private Button systemProxyButton;
	private Button manualProxyButton;
	private Text proxyHostText;
	private Text proxyPortText;
	private Button proxyAuthButton;
	private Text proxyUserText;
	private Text proxyPasswdText;

	// ACCOUNTS tab
	private TabItem tabAccounts;
	private ComboViewer accountCombo;
	private Link newAccountLink;
	private Link deleteAccountLink;
	private Button defaultAccountButton;
	private Button planningAccountButton;
	private AccountPanel accountPanel;
	private DaoContentProvider<Account> accountContentProvider;
	private long nextAccountId = -1;

	// COLOR tab
	private TabItem tabColors;
	private ColorButtonViewer positiveColorButtonViewer;
	private ColorButtonViewer negativeColorButtonViewer;

	// RULES tab
	private Button matchCategoriesButton;
	private Button matchBudgetsButton;
	private TabItem tabRules;
	private TabFolder rulesFolder;

	// CATEGORY RULES TAB
	private TabItem tabCategoryRules;
	private Table categoryTable;
	private TableViewer categoryTableViewer;
	private TableColumn categoryTblclmn1;
	private TableColumn categoryTblclmn2;
	/** The list of rules currently displayed */
	private IObservableList<CategoryRecognition> categoryExpressions;
	private List<CategoryRecognition> initialCategoryExpressions;

	// BUDGET RULES TAB
	private TabItem tabBudgetRules;
	private Table budgetTable;
	private TableViewer budgetTableViewer;
	private TableColumn budgetTblclmn1;
	private TableColumn budgetTblclmn2;
	/** The list of rules currently displayed */
	private IObservableList<BudgetRecognition> budgetExpressions;
	private List<BudgetRecognition> initialBudgetExpressions;

	// HISTORY tab
	private TabItem tabHistory;
	private Text absoluteLimitText;
	private float absoluteLimit;

	private Table historyTable;
	private CheckboxTableViewer historyTableViewer;
	private TableColumn historyTblclmn1;
	private TableColumn historyTblclmn2;
	private TableColumn historyTblclmn3;
	private TableColumn historyTblclmn4;
	private TableColumn historyTblclmn5;
	/** The list of history items currently displayed */
	private IObservableList<HistoricalItem> historyItems;
	private List<HistoricalItem> initialHistoryItems;
	private int initialActiveHistoryItems = 0;

	private RsBudgetPreferences preferences;
	private DataBindingContext bindingContext;
	private IWorkbench workbench;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public PreferencesDialog(Shell parentShell, IWorkbench workbench) {
		super(parentShell);
		this.workbench = workbench;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		Composite area = (Composite) super.createDialogArea(parent);

		setTitle(Plugin.translate("dialog.preferences.title"));
		setTitleImage(resourceManager.createImage(ImageDescriptor.createFromFile(getClass(), "/icons/Settings_32x32.png")));

		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		TabFolder tabFolder = new TabFolder(container, SWT.NONE);

		try {
			createGeneralTab(tabFolder);
			createNetworkTab(tabFolder);
			createAccountsTab(tabFolder);
			createRulesTab(tabFolder);
			createHistoryTab(tabFolder);
			createColorsTab(tabFolder);

			loadPreferences();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return area;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 500);
	}

	/**
	 * Create the general tab.
	 * @param tabFolder the parent folder
	 */
	protected void createGeneralTab(TabFolder tabFolder) {
		tabGeneral = createTab(tabFolder, Plugin.translate("dialog.preferences.tab.general"));
		Composite composite = (Composite)tabGeneral.getControl();
		composite.setLayout(new GridLayout(2, false));

		Group sctnLocales = new Group(composite, SWT.NONE);
		sctnLocales.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		sctnLocales.setText(Plugin.translate("dialog.preferences.tab.localization"));
		GridLayout gl_sctnLocales = new GridLayout(2, false);
		gl_sctnLocales.marginRight = 5;
		gl_sctnLocales.marginLeft = 5;
		gl_sctnLocales.marginBottom = 5;
		sctnLocales.setLayout(gl_sctnLocales);

		// User Language
		createLabel(sctnLocales, Plugin.translate("dialog.preferences.label.userinterface"));
		userLanguageCombo = new Combo(sctnLocales, SWT.NONE);
		userLanguageCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		userLanguageViewer = new ComboViewer(userLanguageCombo);
		userLanguageViewer.setContentProvider(new ArrayContentProvider());
		userLanguageViewer.setLabelProvider(new BeanLabelProvider("displayName"));
		// Filter out empty countries!
		List<Locale> l1 = Arrays.asList(Locale.getAvailableLocales()); 
		List<Locale> l2 = new ArrayList<>();
		for (Locale l : l1) if (l.getCountry().length() == 2) l2.add(l);
		Collections.sort(l2, new BeanComparator<Locale>("displayName"));
		userLanguageViewer.setInput(l2);

		// Currency
		createLabel(sctnLocales, Plugin.translate("dialog.preferences.label.currency"));
		currencyCombo = new Combo(sctnLocales, SWT.NONE);
		currencyCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		currencyViewer = new ComboViewer(currencyCombo);
		currencyViewer.setContentProvider(new ArrayContentProvider());
		currencyViewer.setLabelProvider(new BeanLabelProvider("displayName"));
		Currency cl[] = Currency.getAvailableCurrencies().toArray(new Currency[0]);
		Arrays.sort(cl, new BeanComparator<Currency>("displayName"));
		currencyViewer.setInput(cl);

		Group sctnRestart = new Group(composite, SWT.NONE);
		sctnRestart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		sctnRestart.setText(Plugin.translate("dialog.preferences.tab.general.restart"));
		GridLayout gl_sctnRestart = new GridLayout(2, false);
		gl_sctnRestart.marginRight = 10;
		gl_sctnRestart.marginLeft = 10;
		gl_sctnRestart.marginTop = 5;
		gl_sctnRestart.marginBottom = 5;
		sctnRestart.setLayout(gl_sctnRestart);

		// Reset window
		resetWindowButton = new Button(sctnRestart, SWT.CHECK);
		resetWindowButton.setText(Plugin.translate("dialog.preferences.label.resetwindow"));
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		resetWindowButton.setLayoutData(gd);

		// The Restart link
		Composite linkComposite = new Composite(composite, SWT.NONE);
		linkComposite.setLayout(new GridLayout(2, false));
		linkComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Label l = new Label(linkComposite, SWT.NONE);
		l.setText(Plugin.translate("dialog.preferences.label.restartrequired"));

		restartLink = new Link(linkComposite, SWT.NONE);
		restartLink.setText("<a>"+Plugin.translate("dialog.preferences.link.restart")+"</a>");
		restartLink.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (MessageDialog.openConfirm(getParentShell(), Plugin.translate("dialog.preferences.restart.title"), Plugin.translate("dialog.preferences.restart.message"))) {
					// Save and restart
					savePreferences();
					Plugin.doRestart(workbench);
				}
			}
		});
		//
	}

	protected void createNetworkTab(TabFolder tabFolder) {
		tabNetwork = createTab(tabFolder, Plugin.translate("dialog.preferences.tab.network"));
		Composite composite = (Composite)tabNetwork.getControl();
		composite.setLayout(new GridLayout(1, false));

		Group sctnProxy = new Group(composite, SWT.NONE);
		sctnProxy.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		sctnProxy.setText(Plugin.translate("dialog.preferences.tab.network.label.proxy"));
		GridLayout gl_sctnProxy = new GridLayout(4, false);
		gl_sctnProxy.marginRight = 150;
		gl_sctnProxy.marginLeft = 5;
		gl_sctnProxy.marginBottom = 5;
		gl_sctnProxy.marginHeight = 5;
		sctnProxy.setLayout(gl_sctnProxy);

		noProxyButton = new Button(sctnProxy, SWT.RADIO);
		noProxyButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 4, 1));
		noProxyButton.setText(Plugin.translate("dialog.preferences.tab.network.button.noproxy"));

		systemProxyButton = new Button(sctnProxy, SWT.RADIO);
		systemProxyButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 4, 1));
		systemProxyButton.setText(Plugin.translate("dialog.preferences.tab.network.button.systemproxy"));

		manualProxyButton = new Button(sctnProxy, SWT.RADIO);
		manualProxyButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 4, 1));
		manualProxyButton.setText(Plugin.translate("dialog.preferences.tab.network.button.manualproxy"));
		manualProxyButton.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent evt) {
				manualProxySelected(manualProxyButton.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent evt) {
				manualProxySelected(manualProxyButton.getSelection());
			}
		});

		Label l = new Label(sctnProxy, SWT.NONE);
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));;
		l.setText(Plugin.translate("dialog.preferences.tab.network.label.host"));

		proxyHostText = new Text(sctnProxy, SWT.BORDER);
		proxyHostText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		l = new Label(sctnProxy, SWT.NONE);
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));;
		l.setText(Plugin.translate("dialog.preferences.tab.network.label.port"));

		proxyPortText = new Text(sctnProxy, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd.widthHint = 80;
		proxyPortText.setLayoutData(gd);
		proxyPortText.addVerifyListener(new IntegerVerifyListener());
		l = new Label(sctnProxy, SWT.NONE); // Dummy

		proxyAuthButton = new Button(sctnProxy, SWT.CHECK);
		proxyAuthButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 3, 1));
		proxyAuthButton.setText(Plugin.translate("dialog.preferences.tab.network.button.authrequired"));
		proxyAuthButton.addSelectionListener(new SelectionListener() {			
			@Override
			public void widgetSelected(SelectionEvent evt) {
				proxyAuthRequiredSelected(manualProxyButton.getSelection() && proxyAuthButton.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent evt) {
				proxyAuthRequiredSelected(manualProxyButton.getSelection() && proxyAuthButton.getSelection());
			}
		});

		l = new Label(sctnProxy, SWT.NONE);
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));;
		l.setText(Plugin.translate("dialog.preferences.tab.network.label.user"));

		proxyUserText = new Text(sctnProxy, SWT.BORDER);
		proxyUserText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		l = new Label(sctnProxy, SWT.NONE);
		l.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));;
		l.setText(Plugin.translate("dialog.preferences.tab.network.label.passwd"));

		proxyPasswdText = new Text(sctnProxy, SWT.BORDER|SWT.PASSWORD);
		proxyPasswdText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 3, 1));

		// Initialize
		manualProxySelected(false);
		systemProxyButton.setSelection(true);
	}

	/**
	 * Create the accounts tab.
	 * @param tabFolder the parent folder
	 */
	protected void createAccountsTab(TabFolder tabFolder) {
		tabAccounts = createTab(tabFolder, Plugin.translate("dialog.preferences.tab.accounts"));
		Composite composite = (Composite)tabAccounts.getControl();

		Composite c = new Composite(composite, SWT.NONE);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		c.setLayout(new GridLayout(2, false));
		Group sctnAccount = new Group(c, SWT.NONE);
		sctnAccount.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		sctnAccount.setText(Plugin.translate("dialog.preferences.tab.accounts.label.selection"));
		GridLayout gl_sctnAccount = new GridLayout(4, false);
		gl_sctnAccount.marginRight = 5;
		gl_sctnAccount.marginLeft = 5;
		gl_sctnAccount.marginBottom = 5;
		sctnAccount.setLayout(gl_sctnAccount);

		Label lblBlz = new Label(sctnAccount, SWT.NONE);
		lblBlz.setText(Plugin.translate("dialog.preferences.tab.accounts.label.account"));

		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		AccountDAO dao = factory.getAccountDAO();
		accountCombo = new ComboViewer(sctnAccount, SWT.NONE);
		accountContentProvider = new DaoContentProvider<Account>(dao, new BeanComparator<Account>("name"));
		accountCombo.setContentProvider(accountContentProvider);
		accountCombo.setLabelProvider(new BeanLabelProvider("name"));
		accountCombo.addSelectionChangedListener(new ISelectionChangedListener() {		
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				accountChanged((Account)((IStructuredSelection)event.getSelection()).getFirstElement());
			}
		});

		// Default button
		defaultAccountButton = new Button(sctnAccount, SWT.CHECK);
		defaultAccountButton.setText(Plugin.translate("dialog.preferences.tab.accounts.button.default"));
		defaultAccountButton.setSelection(true);

		// Planning relevant button
		planningAccountButton = new Button(sctnAccount, SWT.CHECK);
		planningAccountButton.setText(Plugin.translate("dialog.preferences.tab.accounts.button.planning"));
		planningAccountButton.setSelection(true);

		// Two links for new and delete and button for default
		c = new Composite(sctnAccount, SWT.NONE);
		c.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.spacing = 10;
		c.setLayout(layout);
		newAccountLink = new Link(c, SWT.NONE);
		newAccountLink.setText("<a>"+Plugin.translate("dialog.preferences.tab.accounts.link.new")+"</a>");
		newAccountLink.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createAccount();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				createAccount();
			}
		});
		deleteAccountLink = new Link(c, SWT.NONE);
		deleteAccountLink.setText("<a>"+Plugin.translate("dialog.preferences.tab.accounts.link.delete")+"</a>");
		deleteAccountLink.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteAccount();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				deleteAccount();
			}
		});

		// Account panel
		accountPanel = new AccountPanel(composite, SWT.NONE);
		accountPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		try {
			factory.begin();
			accountCombo.setInput("");
			accountCombo.setSelection(new StructuredSelection(dao.findDefault()));
			factory.commit();
		} catch (Exception e) {
			log.error("Cannot execute query: ", e);
			try {
				factory.rollback();
			} catch (Exception e2) {
				log.error("Cannot rollback TX: ", e2);
			}
		}
	}

	/**
	 * Called by selection listener.
	 * @param account the account to be set
	 */
	protected void accountChanged(Account account) {
		try {
			accountPanel.setAccount(account);
			defaultAccountButton.setSelection(getDefaultAccount().equals(account));
			planningAccountButton.setSelection((account != null) && account.isPlanningRelevant());
		} catch (Throwable t) { 
			t.printStackTrace();
		}
	}

	protected Account getDefaultAccount() {
		Account rc = null;
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		try {
			factory.begin();
			rc = factory.getAccountDAO().findDefault();
			factory.commit();
		} catch (Exception e) {
			try {
				factory.rollback();
			} catch (Exception e2) {

			}
		}
		return rc;
	}

	/**
	 * Creates a new Account.
	 */
	protected void createAccount() {
		AccountBO acct = (AccountBO)RsBudgetModelService.INSTANCE.getFactory().getAccountDAO().newInstance();
		acct.setName(Plugin.translate("dialog.preferences.tab.account.new.name"));
		acct.setAccountNumber("000000000");
		acct.setActive(true);
		acct.setOwner(Plugin.translate("dialog.preferences.tab.account.new.owner"));
		acct.setLogin(Plugin.translate("dialog.preferences.tab.account.new.login"));
		acct.setPassword(Plugin.translate("dialog.preferences.tab.account.new.password"));
		acct.setBank(getAccount().getBank());
		acct.setPlanningRelevant(true);
		acct.setId(createNewAccountId());
		accountContentProvider.getAdditional().add(acct);
		accountCombo.refresh();
		accountCombo.setSelection(new StructuredSelection(acct));
	}

	/**
	 * Creates a new account ID.
	 * @return
	 */
	protected Long createNewAccountId() {
		nextAccountId--;
		return Long.valueOf(nextAccountId+1);
	}

	/**
	 * Removes the account.
	 * Actually the account is set inactive when saved.
	 */
	protected void deleteAccount() {
		if (accountCombo.getCombo().getItemCount() == 1) {
			MessageDialog.openError(getShell(), Plugin.translate("dialog.preferences.tab.account.deleteerror1.title"), Plugin.translate("dialog.preferences.tab.account.deleteerror1.message"));
		} else {
			Account acct = getAccount();
			if (MessageDialog.openConfirm(getShell(), Plugin.translate("dialog.preferences.tab.account.deletedlg.title"), Plugin.translate("dialog.preferences.tab.account.deletedlg.message", acct.getName()))) {
				if (acct.getId() < 0) {
					accountContentProvider.getAdditional().remove(acct);
				} else {
					accountContentProvider.getFiltered().add(acct);
				}
				accountCombo.refresh();
				accountCombo.setSelection(new StructuredSelection(accountContentProvider.getElements("")[0]));
				accountPanel.removeAccount(acct);
			}
		}
	}

	/**
	 * Returns the account currently selected.
	 * @return the account
	 */
	protected Account getAccount() {
		return (Account)((IStructuredSelection)accountCombo.getSelection()).getFirstElement();
	}

	/**
	 * Creates the history tab.
	 * @param tabFolder the parent folder
	 */
	protected void createHistoryTab(TabFolder tabFolder) {
		tabHistory = createTab(tabFolder, Plugin.translate("dialog.preferences.tab.history"));
		Composite composite = (Composite)tabHistory.getControl();
		composite.setLayout(new GridLayout(2, false));

		Label lblAbsoluteLimit = new Label(composite, SWT.NONE);
		lblAbsoluteLimit.setText(Plugin.translate("dialog.preferences.tab.history.label.limit"));
		GridData gd = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
		gd.verticalIndent = 10;
		gd.horizontalIndent = 10;
		lblAbsoluteLimit.setLayoutData(gd);

		absoluteLimitText = new Text(composite, SWT.BORDER|SWT.RIGHT);
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.widthHint = 50;
		gd.verticalIndent = 10;
		gd.horizontalIndent = 5;
		absoluteLimitText.setLayoutData(gd);
		absoluteLimitText.addModifyListener(new FloatValidator(absoluteLimitText, Plugin.translate("dialog.preferences.tab.history.label.error")));

		historyTable = new Table(composite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.CHECK);
		TableLayout tableLayout = new TableLayout();
		historyTable.setLayout(tableLayout);
		historyTableViewer = new CheckboxTableViewer(historyTable);
		TableViewerEditor.create(
				historyTableViewer, 
				new CellEditorActivationStrategy(historyTableViewer), 
				ColumnViewerEditor.TABBING_HORIZONTAL 
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION);
		historyTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		historyTable.setHeaderVisible(true);
		historyTable.setLinesVisible(true);
		historyTableViewer.setCheckStateProvider(new ICheckStateProvider() {
			@Override
			public boolean isGrayed(Object element) {
				return false;
			}

			@Override
			public boolean isChecked(Object element) {
				return ((HistoricalItem)element).isShowHistory();
			}
		});
		historyTableViewer.addCheckStateListener(new ICheckStateListener() {
			@Override
			public void checkStateChanged(CheckStateChangedEvent event) {
				((HistoricalItem)event.getElement()).setShowHistory(event.getChecked());
			}
		});

		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		{
			// Make it a boolean
			TableViewerColumn column1 = new TableViewerColumn(historyTableViewer, SWT.NONE);
			historyTblclmn1 = column1.getColumn();
			tableLayout.addColumnData(new ColumnWeightData(20, 20, true));
			historyTblclmn1.setText(Plugin.translate("dialog.preferences.tab.history.column.show.title"));
			AbstractColumnLabelProvider lp = new BeanColumnLabelProvider("showHistory", EmptyLabelProvider.getInstance(), resourceManager);
			column1.setLabelProvider(lp);

			TableViewerColumn column2 = new TableViewerColumn(historyTableViewer, SWT.NONE);
			historyTblclmn2 = column2.getColumn();
			tableLayout.addColumnData(new ColumnWeightData(70, 20, true));
			historyTblclmn2.setText(Plugin.translate("dialog.preferences.tab.history.column.name.title"));
			lp = new BeanColumnLabelProvider("name", resourceManager); 
			column2.setLabelProvider(lp);
			column2.setEditingSupport(new TextEditingSupport(historyTableViewer, new BeanEditingSupportModel("name"), false));

			TableViewerColumn column3 = new TableViewerColumn(historyTableViewer, SWT.NONE);
			historyTblclmn3 = column3.getColumn();
			tableLayout.addColumnData(new ColumnWeightData(30, 20, true));
			historyTblclmn3.setText(Plugin.translate("dialog.preferences.tab.history.column.unit.title"));
			lp = new BeanColumnLabelProvider("unit", resourceManager); 
			column3.setLabelProvider(lp);
			column3.setEditingSupport(new TextEditingSupport(historyTableViewer, new BeanEditingSupportModel("unit"), false));

			TableViewerColumn column4 = new TableViewerColumn(historyTableViewer, SWT.CENTER);
			historyTblclmn4 = column4.getColumn();
			historyTblclmn4.setAlignment(SWT.CENTER);
			tableLayout.addColumnData(new ColumnWeightData(20, 20, true));
			historyTblclmn4.setText(Plugin.translate("dialog.preferences.tab.history.column.changes.title"));
			lp = new BeanColumnLabelProvider("showChanges", EmptyLabelProvider.getInstance(), resourceManager);
			lp.setImageProvider(new CheckboxImageProvider(getShell()));
			column4.setLabelProvider(lp);
			column4.setEditingSupport(new CheckboxEditingSupport(historyTableViewer, new BeanEditingSupportModel("showChanges")));

			// TODO: allow scale precisions
			Map<Boolean, String> map = new LinkedHashMap<Boolean,String>();
			map.put(Boolean.FALSE, "0");
			map.put(Boolean.TRUE, "0"+((DecimalFormat)DecimalFormat.getInstance()).getDecimalFormatSymbols().getDecimalSeparator()+"00");
			TableViewerColumn column5 = new TableViewerColumn(historyTableViewer, SWT.NONE);
			historyTblclmn5 = column5.getColumn();
			tableLayout.addColumnData(new ColumnWeightData(20, 20, true));
			historyTblclmn5.setText(Plugin.translate("dialog.preferences.tab.history.column.float.title"));
			lp = new BeanColumnLabelProvider("floatValue", new MapLabelProvider(map), resourceManager); 
			column5.setLabelProvider(lp);
			IComboBoxEditingSupportModel floatModel = new MapSelectionSupportModel(map, "floatValue");
			column5.setEditingSupport(new ComboBoxEditingSupport(historyTableViewer, floatModel, false));
		}

		// Content provider
		historyTableViewer.setContentProvider(new ObservableListContentProvider());
		historyTableViewer.setInput(createHistoryModel());

		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(historyTable);
		menuMgr.add(new AddDaoRowAction(historyTableViewer, historyItems, factory.getHistoricalItemDAO(), 1));
		menuMgr.add(new RemoveRowAction(historyTableViewer, historyItems));
		historyTable.setMenu(menu);
	}


	/**
	 * Returns the absoluteLimit.
	 * @return the absoluteLimit
	 */
	public float getAbsoluteLimit() {
		return absoluteLimit;
	}

	/**
	 * Sets the absoluteLimit.
	 * @param absoluteLimit the absoluteLimit to set
	 */
	public void setAbsoluteLimit(float absoluteLimit) {
		this.absoluteLimit = absoluteLimit;
	}

	/**
	 * Creates the initial model for history items.
	 * @return the model
	 */
	protected IObservableList<HistoricalItem> createHistoryModel() {
		if (historyItems == null) {
			RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
			List<HistoricalItem> l = new ArrayList<>();
			factory.begin();
			try {
				l = factory.getHistoricalItemDAO().findDefaultAll();
				factory.commit();
			} catch (Exception e) {
				try {
					log.error("Cannot get category expression list", e);
					factory.rollback();
				} catch (Exception e2) {
					log.error("Cannot rollback transaction", e2);
				}
			}
			initialHistoryItems = new ArrayList<>(l);
			initialActiveHistoryItems = 0;
			for (HistoricalItem o : initialHistoryItems) {
				if (o.isShowHistory()) initialActiveHistoryItems++;
			}
			Collections.sort(l);
			IListProperty<List<HistoricalItem>, HistoricalItem> property = Properties.selfList(HistoricalItem.class);
			historyItems = property.observe(l);
			BeanProperties.value(HistoricalItem.class, HistoricalItem.PROPERTY_SHOW_HISTORY).observeDetail(historyItems);
			BeanProperties.value(HistoricalItem.class, HistoricalItem.PROPERTY_NAME).observeDetail(historyItems);
			BeanProperties.value(HistoricalItem.class, HistoricalItem.PROPERTY_UNIT).observeDetail(historyItems);
			BeanProperties.value(HistoricalItem.class, HistoricalItem.PROPERTY_FLOAT_VALUE).observeDetail(historyItems);
			BeanProperties.value(HistoricalItem.class, HistoricalItem.PROPERTY_SHOW_CHANGES).observeDetail(historyItems);
		}
		return historyItems;
	}


	/**
	 * Create the colors tab.
	 * @param tabFolder the parent folder
	 */
	protected void createColorsTab(TabFolder tabFolder) {
		tabColors = createTab(tabFolder, Plugin.translate("dialog.preferences.tab.colors"));
		Composite composite = (Composite)tabColors.getControl();

		createLabel(composite, Plugin.translate("dialog.preferences.label.color.positive"));	
		positiveColorButtonViewer = createColorButton(composite);

		createLabel(composite, Plugin.translate("dialog.preferences.label.color.negative"));
		negativeColorButtonViewer = createColorButton(composite);
	}

	/**
	 * Create the rules tab.
	 * @param tabFolder the parent folder
	 */
	protected void createRulesTab(TabFolder tabFolder) {
		tabRules = createTab(tabFolder, Plugin.translate("dialog.preferences.tab.rules"));
		Composite composite = (Composite)tabRules.getControl();
		composite.setLayout(new GridLayout(1, true));

		Group sctnRules = new Group(composite, SWT.NONE);
		sctnRules.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		sctnRules.setText(Plugin.translate("dialog.preferences.tab.general.label.rules"));
		GridLayout gl_sctnAccount = new GridLayout(1, false);
		gl_sctnAccount.marginRight = 5;
		gl_sctnAccount.marginLeft = 5;
		gl_sctnAccount.marginBottom = 5;
		sctnRules.setLayout(gl_sctnAccount);

		// categories button
		matchCategoriesButton = new Button(sctnRules, SWT.CHECK);
		matchCategoriesButton.setText(Plugin.translate("dialog.preferences.tab.general.button.match-categories"));
		matchCategoriesButton.setSelection(true);

		// categories button
		matchBudgetsButton = new Button(sctnRules, SWT.CHECK);
		matchBudgetsButton.setText(Plugin.translate("dialog.preferences.tab.general.button.match-budgets"));
		matchBudgetsButton.setSelection(true);

		rulesFolder = new TabFolder(composite, SWT.NONE);
		rulesFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		createCategoryRulesTab(rulesFolder);
		createBudgetRulesTab(rulesFolder);
	}

	/**
	 * Create the category rules tab.
	 * @param tabFolder the parent folder
	 */
	protected void createCategoryRulesTab(TabFolder tabFolder) {
		tabCategoryRules = createTab(tabFolder, Plugin.translate("dialog.preferences.tab.category-rules"));
		Composite composite = (Composite)tabCategoryRules.getControl();
		composite.setLayout(new GridLayout(1, true));
		FormToolkit toolkit = new FormToolkit(composite.getDisplay());
		categoryTable = toolkit.createTable(composite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		TableLayout tableLayout = new TableLayout();
		categoryTable.setLayout(tableLayout);
		categoryTableViewer = new TableViewer(categoryTable);
		TableViewerEditor.create(
				categoryTableViewer, 
				new CellEditorActivationStrategy(categoryTableViewer), 
				ColumnViewerEditor.TABBING_HORIZONTAL 
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION);
		categoryTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		categoryTable.setHeaderVisible(true);
		categoryTable.setLinesVisible(true);
		
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		{
			TableViewerColumn column1 = new TableViewerColumn(categoryTableViewer, SWT.NONE);
			categoryTblclmn1 = column1.getColumn();
			tableLayout.addColumnData(new ColumnWeightData(80, 30, true));
			categoryTblclmn1.setText(Plugin.translate("dialog.preferences.tab.category-rules.column.expression.title"));
			AbstractColumnLabelProvider lp = new BeanColumnLabelProvider("expression", resourceManager); 
			column1.setLabelProvider(lp);
			IEditingSupportModel textModel = new BeanEditingSupportModel("expression");
			column1.setEditingSupport(new TextEditingSupport(categoryTableViewer, textModel, false));

			TableViewerColumn column2 = new TableViewerColumn(categoryTableViewer, SWT.NONE);
			categoryTblclmn2 = column2.getColumn();
			tableLayout.addColumnData(new ColumnWeightData(70, 20, true));
			categoryTblclmn2.setText(Plugin.translate("dialog.preferences.tab.category-rules.column.expression.category"));
			lp = new BeanColumnLabelProvider("category.name", resourceManager); 
			column2.setLabelProvider(lp);
			IComboBoxEditingSupportModel categoryModel = new BoSelectionSupportModel<Category>(factory.getCategoryDAO(), "category");
			column2.setEditingSupport(new ComboBoxEditingSupport(categoryTableViewer, categoryModel, false));
		}

		// Content provider
		categoryTableViewer.setContentProvider(new ObservableListContentProvider());
		categoryTableViewer.setInput(createCategoryRulesModel());

		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(categoryTable);
		menuMgr.add(new AddDaoRowAction(categoryTableViewer, categoryExpressions, factory.getCategoryRecognitionDAO(), 0));
		menuMgr.add(new RemoveRowAction(categoryTableViewer, categoryExpressions));
		menuMgr.add(new Separator());
		menuMgr.add(new MoveUpAction(categoryTableViewer, categoryExpressions));
		menuMgr.add(new MoveDownAction(categoryTableViewer, categoryExpressions));
		categoryTable.setMenu(menu);

	}

	/**
	 * Creates the initial model for category expressions.
	 * @return the model
	 */
	protected IObservableList<CategoryRecognition> createCategoryRulesModel() {
		if (categoryExpressions == null) {
			RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
			List<CategoryRecognition> l = new ArrayList<>();
			factory.begin();
			try {
				l = factory.getCategoryRecognitionDAO().findDefaultAll();
				factory.commit();
			} catch (Exception e) {
				try {
					log.error("Cannot get category expression list", e);
					factory.rollback();
				} catch (Exception e2) {
					log.error("Cannot rollback transaction", e2);
				}
			}
			initialCategoryExpressions = new ArrayList<>(l);
			Collections.sort(l);
			IListProperty<List<CategoryRecognition>, CategoryRecognition> property = Properties.selfList(CategoryRecognition.class);
			categoryExpressions = property.observe(l);
			BeanProperties.value(CategoryRecognition.class, CategoryRecognition.PROPERTY_CATEGORY).observeDetail(categoryExpressions);
			BeanProperties.value(CategoryRecognition.class, CategoryRecognition.PROPERTY_EXPRESSION).observeDetail(categoryExpressions);
		}
		return categoryExpressions;
	}

	/**
	 * Create the budget rules tab.
	 * @param tabFolder the parent folder
	 */
	protected void createBudgetRulesTab(TabFolder tabFolder) {
		tabBudgetRules = createTab(tabFolder, Plugin.translate("dialog.preferences.tab.budget-rules"));
		Composite composite = (Composite)tabBudgetRules.getControl();
		composite.setLayout(new GridLayout(1, true));

		budgetTable = new Table(composite, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		TableLayout tableLayout = new TableLayout();
		budgetTable.setLayout(tableLayout);
		budgetTableViewer = new TableViewer(budgetTable);
		TableViewerEditor.create(
				budgetTableViewer, 
				new CellEditorActivationStrategy(budgetTableViewer), 
				ColumnViewerEditor.TABBING_HORIZONTAL 
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION);
		budgetTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		budgetTable.setHeaderVisible(true);
		budgetTable.setLinesVisible(true);

		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		{
			TableViewerColumn column1 = new TableViewerColumn(budgetTableViewer, SWT.NONE);
			budgetTblclmn1 = column1.getColumn();
			tableLayout.addColumnData(new ColumnWeightData(80, 30, true));
			budgetTblclmn1.setText(Plugin.translate("dialog.preferences.tab.budget-rules.column.expression.title"));
			AbstractColumnLabelProvider lp = new BeanColumnLabelProvider("expression", resourceManager); 
			column1.setLabelProvider(lp);
			IEditingSupportModel textModel = new BeanEditingSupportModel("expression");
			column1.setEditingSupport(new TextEditingSupport(budgetTableViewer, textModel, false));

			TableViewerColumn column2 = new TableViewerColumn(budgetTableViewer, SWT.NONE);
			budgetTblclmn2 = column2.getColumn();
			tableLayout.addColumnData(new ColumnWeightData(70, 20, true));
			budgetTblclmn2.setText(Plugin.translate("dialog.preferences.tab.budget-rules.column.expression.budget"));
			lp = new BeanColumnLabelProvider("budget.name", resourceManager); 
			column2.setLabelProvider(lp);
			IComboBoxEditingSupportModel categoryModel = new BoSelectionSupportModel<PeriodicalBudget>(factory.getPeriodicalBudgetDAO(), "budget");
			column2.setEditingSupport(new ComboBoxEditingSupport(budgetTableViewer, categoryModel, false));
		}

		// Content provider
		budgetTableViewer.setContentProvider(new ObservableListContentProvider());
		budgetTableViewer.setInput(createBudgetRulesModel());

		// Context menu
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(budgetTable);
		menuMgr.add(new AddDaoRowAction(budgetTableViewer, budgetExpressions, factory.getBudgetRecognitionDAO(), 0));
		menuMgr.add(new RemoveRowAction(budgetTableViewer, budgetExpressions));
		menuMgr.add(new Separator());
		menuMgr.add(new MoveUpAction(budgetTableViewer, budgetExpressions));
		menuMgr.add(new MoveDownAction(budgetTableViewer, budgetExpressions));
		budgetTable.setMenu(menu);

	}

	/**
	 * Creates the initial model for budget expressions.
	 * @return the model
	 */
	protected IObservableList createBudgetRulesModel() {
		if (budgetExpressions == null) {
			RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
			List<BudgetRecognition> l = new ArrayList<>();
			factory.begin();
			try {
				l = factory.getBudgetRecognitionDAO().findDefaultAll();
				factory.commit();
			} catch (Exception e) {
				try {
					log.error("Cannot get budget expression list", e);
					factory.rollback();
				} catch (Exception e2) {
					log.error("Cannot rollback transaction", e2);
				}
			}
			initialBudgetExpressions = new ArrayList<>(l);
			Collections.sort(l);
			IListProperty<List<BudgetRecognition>, BudgetRecognition> property = Properties.selfList(BudgetRecognition.class);
			budgetExpressions = property.observe(l);
			BeanProperties.value(BudgetRecognition.class, BudgetRecognition.PROPERTY_BUDGET).observeDetail(budgetExpressions);
			BeanProperties.value(BudgetRecognition.class, BudgetRecognition.PROPERTY_EXPRESSION).observeDetail(budgetExpressions);
		}
		return budgetExpressions;
	}


	/**
	 * Create a settings label.
	 * @param parent
	 * @param s
	 * @return
	 */
	protected Label createLabel(Composite parent, String s) {
		Label rc = new Label(parent, SWT.NONE);
		rc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		rc.setText(s);
		return rc;
	}

	/**
	 * Create a tab item.
	 * @param tabFolder
	 * @param title
	 * @return
	 */
	protected TabItem createTab(TabFolder tabFolder, String title) {
		TabItem rc = new TabItem(tabFolder, SWT.NONE);
		rc.setText(title);

		Composite composite = new Composite(tabFolder, SWT.NONE);
		rc.setControl(composite);
		composite.setLayout(new GridLayout(2, true));
		return rc;
	}

	/**
	 * Create a color button.
	 * @param parent the parent composite
	 * @return the viewer for the button
	 */
	protected ColorButtonViewer createColorButton(Composite parent) {
		Button rc = new Button(parent, SWT.PUSH);
		rc.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
		return new ColorButtonViewer(rc);
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		//createButton(parent, IDialogConstants.OK_ID, Plugin.translate("buttons.ok.label"), true);
		//createButton(parent, IDialogConstants.CANCEL_ID,Plugin.translate("buttons.cancel.label"), false);
		bindingContext = initDataBindings();
	}

	/**
	 * Load the preferences.
	 */
	protected void loadPreferences() {
		preferences = RsBudgetPreferences.load();
		noProxyButton.setSelection(preferences.isNoProxy());
		systemProxyButton.setSelection(preferences.isSystemProxy());
		manualProxyButton.setSelection(preferences.isManualProxy());
		proxyAuthButton.setSelection(preferences.isProxyAuthRequired());
		resetWindowButton.setSelection(preferences.isResetLayout());
		manualProxySelected(preferences.isManualProxy());
		setAbsoluteLimit(getProfitLossThreshold());
	}

	/**
	 * Returns the absolute limit for balance.
	 * @return the limit
	 */
	public float getProfitLossThreshold() {
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		Setting profitLossThreshold = null;
		try {
			factory.begin();
			SettingDAO dao = factory.getSettingDAO();
			profitLossThreshold = dao.findByKey(SettingDAO.KEY_PROFIT_LOSS_THRESHOLD);
			factory.commit();
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error("Cannot get threshold:", e);
			try {
				factory.rollback();
			} catch (Exception e2) {}
		}
		if (profitLossThreshold != null) return LangUtils.getFloat(profitLossThreshold.getValue(), 0f);
		return 0f;
	}

	/**
	 * Sets the absolute balance limit.
	 * @param f the new limit
	 */
	public void setProfitLossThreshold(float f) {
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		try {
			factory.begin();
			SettingDAO dao = factory.getSettingDAO();
			Setting profitLossThreshold = dao.findByKey(SettingDAO.KEY_PROFIT_LOSS_THRESHOLD);
			if (profitLossThreshold == null) {
				profitLossThreshold = dao.newInstance();
				profitLossThreshold.setKey(SettingDAO.KEY_PROFIT_LOSS_THRESHOLD);
				profitLossThreshold.setValue(Float.toString(f));
				dao.create(profitLossThreshold);
			} else {
				profitLossThreshold.setValue(Float.toString(f));
				dao.save(profitLossThreshold);
			}
			factory.commit();
		} catch (Exception e) {
			LoggerFactory.getLogger(getClass()).error("Cannot get threshold:", e);
			try {
				factory.rollback();
			} catch (Exception e2) {}
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		savePreferences();
		super.okPressed();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void cancelPressed() {
		for (Object o : budgetExpressions) {
			BudgetRecognition b = (BudgetRecognition)o;
			if (!b.isNew()) {
				b.reset();
			}
		}
		for (Object o : categoryExpressions) {
			CategoryRecognition b = (CategoryRecognition)o;
			if (!b.isNew()) {
				b.reset();
			}
		}
		super.cancelPressed();
	}

	/**
	 * Saves the preferences.
	 */
	protected void savePreferences() {
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		try {
			factory.begin();
			bindingContext.updateModels();
			preferences.setNoProxy(noProxyButton.getSelection());
			preferences.setManualProxy(manualProxyButton.getSelection());
			preferences.setSystemProxy(systemProxyButton.getSelection());
			preferences.setProxyAuthRequired(proxyAuthButton.getSelection());
			preferences.setResetLayout(resetWindowButton.getSelection());
			preferences.save();
			// New and changed accounts
			accountPanel.saveChanges();
			// Inactive accounts
			for (Account account : accountContentProvider.getFiltered()) {
				account.setActive(false);
				factory.getAccountDAO().save(account);
			}
			BudgetRecognitionDAO dao1 = factory.getBudgetRecognitionDAO();
			for (Object o : budgetExpressions) {
				BudgetRecognition b = (BudgetRecognition)o;
				if (b.isChanged()) {
					if (b.isNew()) dao1.create(b);
					dao1.save(b);
				}
			}
			for (BudgetRecognition o : initialBudgetExpressions) {
				if (!budgetExpressions.contains(o)) {
					dao1.delete(o);
				}
			}
			CategoryRecognitionDAO dao2 = factory.getCategoryRecognitionDAO();
			for (Object o : categoryExpressions) {
				CategoryRecognition b = (CategoryRecognition)o;
				if (b.isChanged()) {
					if (b.isNew()) dao2.create(b);
					dao2.save(b);
				}
			}
			for (CategoryRecognition o : initialCategoryExpressions) {
				if (!categoryExpressions.contains(o)) {
					factory.getCategoryRecognitionDAO().delete(o);
				}
			}
			HistoricalItemDAO dao3 = factory.getHistoricalItemDAO();
			int histActive = 0;
			for (Object o : historyItems) {
				HistoricalItem b = (HistoricalItem)o;
				if (b.isChanged()) {
					if (b.isNew()) dao3.create(b);
					dao3.saveObject(b);
				}
				if (b.isShowHistory()) histActive++;
			}
			for (HistoricalItem o : initialHistoryItems) {
				if (!historyItems.contains(o)) {
					o.setShowHistory(false);
					dao3.save(o);
				}
			}

			setProfitLossThreshold(getAbsoluteLimit());
			factory.commit();
			if (histActive > initialActiveHistoryItems) {
				// Delete the width preference to give space for new columns
				IPreferences prefs = PreferencesUtils.getPreferences(false, "tables", "history");
				if (prefs != null) {
					prefs.putBoolean(TableColumnResizeListener.KEY_CHANGED, true);
					prefs.parent().flush();
				}
			}
		} catch (Throwable t) {
			log.error("Cannot save preferences", t);
			try {
				factory.rollback();
			} catch (Exception e2) {
				log.error("Cannot rollback", e2);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeSingleSelectionUserLanguageViewer = ViewerProperties.singleSelection().observe(userLanguageViewer);
		IObservableValue localePreferencesObserveValue = BeanProperties.value("locale").observe(preferences);
		bindingContext.bindValue(observeSingleSelectionUserLanguageViewer, localePreferencesObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCurrencyViewer = ViewerProperties.singleSelection().observe(currencyViewer);
		IObservableValue currencyPreferencesObserveValue = BeanProperties.value("currency").observe(preferences);
		bindingContext.bindValue(observeSingleSelectionCurrencyViewer, currencyPreferencesObserveValue, null, null);
		//
		IObservableValue observePositiveColorButtonViewerValue = PojoProperties.value("color").observe(positiveColorButtonViewer);
		IObservableValue positiveColorPreferencesObserveValue = BeanProperties.value("positiveColor").observe(preferences);
		bindingContext.bindValue(observePositiveColorButtonViewerValue, positiveColorPreferencesObserveValue, null, null);
		//
		IObservableValue observeNegativeColorButtonViewerValue = PojoProperties.value("color").observe(negativeColorButtonViewer);
		IObservableValue negativeColorPreferencesObserveValue = BeanProperties.value("negativeColor").observe(preferences);
		bindingContext.bindValue(observeNegativeColorButtonViewerValue, negativeColorPreferencesObserveValue, null, null);
		//
		IObservableValue observeMatchCategoriesButtonValue = WidgetProperties.selection().observe(matchCategoriesButton);
		IObservableValue matchCategoriesPreferencesObserveValue = BeanProperties.value("matchCategories").observe(preferences);
		bindingContext.bindValue(observeMatchCategoriesButtonValue, matchCategoriesPreferencesObserveValue, null, null);
		//
		IObservableValue observeMatchBudgetsButtonValue = WidgetProperties.selection().observe(matchBudgetsButton);
		IObservableValue matchBudgetsPreferencesObserveValue = BeanProperties.value("matchBudgets").observe(preferences);
		bindingContext.bindValue(observeMatchBudgetsButtonValue, matchBudgetsPreferencesObserveValue, null, null);
		//
		IObservableValue observeProxyHostTextValue = WidgetProperties.text().observe(proxyHostText);
		IObservableValue proxyHostPreferencesObserveValue = BeanProperties.value("proxyHost").observe(preferences);
		bindingContext.bindValue(observeProxyHostTextValue, proxyHostPreferencesObserveValue, null, null);
		//
		IObservableValue observeProxyPortTextValue = WidgetProperties.text().observe(proxyPortText);
		IObservableValue proxyPortPreferencesObserveValue = BeanProperties.value("proxyPort").observe(preferences);
		UpdateValueStrategy strategy1 = new UpdateValueStrategy();
		strategy1.setConverter(new StringToIntegerConverter());
		UpdateValueStrategy strategy2 = new UpdateValueStrategy();
		strategy2.setConverter(IntegerToStringConverter.POSITIVE_INSTANCE);
		Binding bindValue = bindingContext.bindValue(observeProxyPortTextValue, proxyPortPreferencesObserveValue, strategy1, strategy2);
		ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.RIGHT);
		//
		IObservableValue observeProxyUserTextValue = WidgetProperties.text().observe(proxyUserText);
		IObservableValue proxyUserPreferencesObserveValue = BeanProperties.value("proxyUser").observe(preferences);
		bindingContext.bindValue(observeProxyUserTextValue, proxyUserPreferencesObserveValue, null, null);
		//
		IObservableValue observeProxyPasswordTextValue = WidgetProperties.text().observe(proxyPasswdText);
		IObservableValue proxyPasswordPreferencesObserveValue = BeanProperties.value("proxyPassword").observe(preferences);
		bindingContext.bindValue(observeProxyPasswordTextValue, proxyPasswordPreferencesObserveValue, null, null);
		//
		IObservableValue observeAbsLimitTextValue = WidgetProperties.text().observe(absoluteLimitText);
		IObservableValue absLimitPreferencesObserveValue = PojoProperties.value("absoluteLimit").observe(this);
		UpdateValueStrategy strategy3 = new UpdateValueStrategy();
		strategy3.setConverter(new StringToBigDecimalConverter());
		UpdateValueStrategy strategy4 = new UpdateValueStrategy();
		strategy4.setConverter(FloatToStringConverter.DEFAULT_INSTANCE);
		//strategy3.setBeforeSetValidator(new FloatValidator());
		Binding bindValue2 = bindingContext.bindValue(observeAbsLimitTextValue, absLimitPreferencesObserveValue, strategy3, strategy4);
		ControlDecorationSupport.create(bindValue2, SWT.TOP | SWT.RIGHT);
		//

		return bindingContext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean close() {
		if (categoryExpressions != null) categoryExpressions.dispose();
		if (budgetExpressions != null) budgetExpressions.dispose();
		if (historyItems != null) historyItems.dispose();
		return super.close();
	}

	protected void manualProxySelected(boolean selected) {
		proxyHostText.setEnabled(selected);
		proxyPortText.setEnabled(selected);
		proxyAuthButton.setEnabled(selected);
		proxyAuthRequiredSelected(selected && proxyAuthButton.getSelection());
	}

	protected void proxyAuthRequiredSelected(boolean selected) {
		proxyUserText.setEnabled(selected);
		proxyPasswdText.setEnabled(selected);
	}

}
