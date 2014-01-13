/**
 * 
 */
package rsbudget.view.wizards.bootstrap;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rs.baselib.prefs.PreferencesService;
import rs.baselib.sql.HyperSqlFileJdbcConnectionProvider;
import rs.baselib.sql.IDataSourceProvider;
import rs.baselib.sql.IHibernateDialectProvider;
import rs.baselib.sql.IJdbcConnectionProvider;
import rs.baselib.sql.IJdbcConnectionProvider2;
import rs.baselib.sql.MySql5JdbcConnectionProvider;
import rs.baselib.util.IDisplayProvider;
import rs.e4.SwtUtils;
import rsbudget.Plugin;
import rsbudget.data.RsBudgetConfigurationService;
import rsbudget.data.util.DbConfigLocator;
import rsbudget.view.wizards.AbstractWizardPage;

/**
 * @author ralph
 *
 */
public class DbSetupPage extends AbstractWizardPage {

	private ComboViewer dbTypeCombo;
	private Text txtHost;
	private Text txtPort;
	private Text txtDbName;
	private Text txtUser;
	private Text txtPassword;
	private Button testConnectionButton;
	private Button encrypted;
	private boolean tested = false;

	/**
	 * Constructor.
	 * @param name
	 */
	public DbSetupPage() {
		super("DbSetup");
		setTitle(BootstrapWizardLanguage.get("dbsetup.title"));
		setDescription(BootstrapWizardLanguage.get("dbsetup.description"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		GridLayout gl_container = new GridLayout(1, false);
		container.setLayout(gl_container);

		// Type of database
		Group sctnDatabase = new Group(container, SWT.NONE);
		sctnDatabase.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnDatabase.setText(BootstrapWizardLanguage.get("dbsetup.database"));
		GridLayout gl_sctnDatabase = new GridLayout(4, false);
		gl_sctnDatabase.marginRight = 5;
		gl_sctnDatabase.marginLeft = 5;
		gl_sctnDatabase.marginBottom = 5;
		sctnDatabase.setLayout(gl_sctnDatabase);

		Label lblDbType = new Label(sctnDatabase, SWT.NONE);
		GridData gd_lblDbType = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		lblDbType.setLayoutData(gd_lblDbType);
		lblDbType.setText(BootstrapWizardLanguage.get("dbsetup.dbtype"));

		dbTypeCombo = new ComboViewer(sctnDatabase, SWT.READ_ONLY);
		dbTypeCombo.setContentProvider(ArrayContentProvider.getInstance());
		dbTypeCombo.setLabelProvider(new ConnectionProviderLabelProvider());
		IJdbcConnectionProvider2 providers[] = getConnectionProviders();
		dbTypeCombo.setInput(providers);
		GridData gd_dbTypeCombo = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		dbTypeCombo.getCombo().setLayoutData(gd_dbTypeCombo);

		new Label(sctnDatabase, SWT.NONE);

		// Help for database
		Label lblDbHelp = new Label(sctnDatabase, SWT.NONE);
		GridData gd_lblHelp = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		lblDbHelp.setLayoutData(gd_lblHelp);
		lblDbHelp.setText(BootstrapWizardLanguage.get("dbsetup.dbhelp"));

		// host and port
		Label lblHost = new Label(sctnDatabase, SWT.NONE);
		GridData gd_lblHost = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblHost.verticalIndent = 5;
		lblHost.setLayoutData(gd_lblHost);
		lblHost.setText(BootstrapWizardLanguage.get("dbsetup.host"));

		txtHost = new Text(sctnDatabase, SWT.BORDER);
		txtHost.setText("localhost");
		GridData gd_txtHost = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_txtHost.verticalIndent = 5;
		txtHost.setLayoutData(gd_txtHost);
		txtHost.addListener(SWT.KeyUp, this);

		Label lblPort = new Label(sctnDatabase, SWT.NONE);
		GridData gd_lblPort = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_lblPort.verticalIndent = 5;
		lblPort.setLayoutData(gd_lblPort);
		lblPort.setText(BootstrapWizardLanguage.get("dbsetup.port"));

		txtPort = new Text(sctnDatabase, SWT.BORDER);
		txtPort.setText("");
		GridData gd_txtPort = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_txtPort.verticalIndent = 5;
		gd_txtPort.widthHint = 30;
		txtPort.setLayoutData(gd_txtPort);
		txtPort.addListener(SWT.KeyUp, this);

		// dbname
		Label lbldbName = new Label(sctnDatabase, SWT.NONE);
		GridData gd_lbldbName = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		lbldbName.setLayoutData(gd_lbldbName);
		lbldbName.setText(BootstrapWizardLanguage.get("dbsetup.dbname"));

		txtDbName = new Text(sctnDatabase, SWT.BORDER);
		txtDbName.setText("rsbudget");
		GridData gd_txtDbName = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		txtDbName.setLayoutData(gd_txtDbName);
		txtDbName.addListener(SWT.KeyUp, this);

		// user
		Label lblUser = new Label(sctnDatabase, SWT.NONE);
		GridData gd_lblUser = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		lblUser.setLayoutData(gd_lblUser);
		lblUser.setText(BootstrapWizardLanguage.get("dbsetup.user"));

		txtUser = new Text(sctnDatabase, SWT.BORDER);
		txtUser.setText("rsbudget");
		GridData gd_txtUser = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		txtUser.setLayoutData(gd_txtUser);
		txtUser.addListener(SWT.KeyUp, this);

		// password
		Label lblPassword = new Label(sctnDatabase, SWT.NONE);
		GridData gd_lblPassword = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		lblPassword.setLayoutData(gd_lblPassword);
		lblPassword.setText(BootstrapWizardLanguage.get("dbsetup.passwd"));

		txtPassword = new Text(sctnDatabase, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setText("rsbudget");
		GridData gd_txtPassword = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
		txtPassword.setLayoutData(gd_txtPassword);
		txtPassword.addListener(SWT.KeyUp, this);

		// Setup encrypted or not
		Group sctnEncrypted = new Group(container, SWT.NONE);
		sctnEncrypted.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnEncrypted.setText(BootstrapWizardLanguage.get("dbsetup.encryption-title"));
		GridLayout gd_sctnEncrypted = new GridLayout(1, false);
		gd_sctnEncrypted.marginRight = 5;
		gd_sctnEncrypted.marginLeft = 5;
		gd_sctnEncrypted.marginBottom = 5;
		sctnEncrypted.setLayout(gd_sctnEncrypted);

		encrypted = new Button(sctnEncrypted, SWT.CHECK);
		encrypted.setText(BootstrapWizardLanguage.get("dbsetup.encryption-label"));
		GridData gd_encrypted = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		encrypted.setLayoutData(gd_encrypted);

		StyledText styledText = new StyledText(sctnEncrypted, SWT.WRAP | SWT.TRANSPARENT);
		styledText.setText(BootstrapWizardLanguage.get("dbsetup.encryption-help"));
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd.widthHint = 400;
		styledText.setLayoutData(gd);
		styledText.setEditable(false);
		styledText.setBackground(container.getBackground());

		testConnectionButton = new Button(container, SWT.PUSH);
		testConnectionButton.setText(BootstrapWizardLanguage.get("dbsetup.testconnection"));
		gd = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		gd.verticalIndent = 10;
		testConnectionButton.setLayoutData(gd);
		testConnectionButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				testConnection(true);
				getWizard().getContainer().updateButtons();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				testConnection(true);
				getWizard().getContainer().updateButtons();
			}
		});

		dbTypeCombo.setSelection(new StructuredSelection(providers[0]));
		updateUI(providers[0]);

		// Added here because causes NPE when added before the intial selection
		dbTypeCombo.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				tested = false;
				updateUI((IJdbcConnectionProvider2)((IStructuredSelection)event.getSelection()).getFirstElement());
				getWizard().getContainer().updateButtons();
			}
		});
	}

	/**
	 * Controls the UI according to the selected provider.
	 * @param provider the provider selected
	 */
	protected void updateUI(IJdbcConnectionProvider2 provider) {
		if (provider != null) {
			txtHost.setEnabled(provider.isHostEnabled());
			txtPort.setEnabled(provider.isPortEnabled());
			txtDbName.setEnabled(provider.isDbNameEnabled());
			txtUser.setEnabled(provider.isDbLoginEnabled());
			txtPassword.setEnabled(provider.isDbPasswordEnabled());
		} else {
			txtHost.setEnabled(false);
			txtPort.setEnabled(false);
			txtDbName.setEnabled(false);
			txtUser.setEnabled(false);
			txtPassword.setEnabled(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleEvent(Event event) {
		super.handleEvent(event);
		if (event.type == SWT.KeyUp) {
			boolean filled = true;
			for (Text control : new Text[] { txtHost, txtDbName, txtUser, txtPassword }) {
				filled &= !SwtUtils.isTextEmpty(control);
				if (!filled) break;
			}
			if (!filled) {
				setMessage(BootstrapWizardLanguage.get("password.warn.infomissing"), WARNING);
			} else {
				setMessage(null, WARNING);
				setErrorMessage(null);
			}
			tested = false;
			getWizard().getContainer().updateButtons();
		}
	}

	public String getPassword() {
		return new String(txtPassword.getTextChars());
	}

	public String getUser() {
		return txtUser.getText();
	}

	public String getHost() {
		return txtHost.getText();
	}

	public String getPort() {
		return txtPort.getText();
	}

	public String getDbName() {
		return txtDbName.getText();
	}

	public IJdbcConnectionProvider2 getDbProvider() {
		return (IJdbcConnectionProvider2)((IStructuredSelection)dbTypeCombo.getSelection()).getFirstElement();
	}

	public boolean isEncryption() {
		return encrypted.getSelection();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canFlipToNextPage() {
		IJdbcConnectionProvider2 provider = getDbProvider();
		if (provider == null) return false;

		if (provider.isHostEnabled() && SwtUtils.isTextEmpty(txtHost)) return false;
		if (provider.isDbNameEnabled() && SwtUtils.isTextEmpty(txtDbName)) return false;
		if (provider.isDbLoginEnabled() && SwtUtils.isTextEmpty(txtUser)) return false;
		if (provider.isDbPasswordEnabled() && SwtUtils.isTextEmpty(txtPassword)) return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isPageComplete() {
		return canFlipToNextPage();
	}

	protected boolean testConnection(boolean showOk) {
		IJdbcConnectionProvider2 provider = getDbProvider();
		String host     = provider.getHost(getHost());
		String port     = provider.getPort(getPort());
		String dbname   = provider.getDbName(getDbName());
		String login    = provider.getDbLogin(getUser());
		String password = provider.getDbPassword(getPassword());
		Connection con = null;
		try {
			con = provider.getConnection(host, port, dbname, login, password);
			Statement st = null;
			ResultSet rs = null;
			tested = true;

			// Check if DB was setup before
			boolean dbExists = false;
			try {
				String sql = "SELECT * from settings";
				st = con.createStatement();
				rs = st.executeQuery(sql);
				dbExists = true;
				if (rs.next()) {
					// There is a DB already
				}
			} catch (SQLException e) {
			} finally {
				if (rs != null) rs.close();
				if (st != null) st.close();
			}

			// Check if encryption matches
			boolean encryptionExists = false;
			if (dbExists) {
				try {
					st = null;
					rs = null;
					String sql = "SELECT id, name, is_default from transaction_categories";
					st = con.createStatement();
					rs = st.executeQuery(sql);
					int type = rs.getMetaData().getColumnType(3);
					encryptionExists = (type != Types.INTEGER) && (type != Types.BIT);
					if (rs.next()) {
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (rs != null) rs.close();
					if (st != null) st.close();
				}

			}

			if (dbExists) {
				if (isEncryption() == !encryptionExists) {
					// Error: not possible
					if (encryptionExists) {
						MessageDialog.openError(getShell(), BootstrapWizardLanguage.get("dbsetup.test.encrypterror.title"), BootstrapWizardLanguage.get("dbsetup.test.encrypterror.message1"));
					} else {
						MessageDialog.openError(getShell(), BootstrapWizardLanguage.get("dbsetup.test.encrypterror.title"), BootstrapWizardLanguage.get("dbsetup.test.encrypterror.message2"));
					}
					tested = false;
				} else {
					// Warn about setup
					tested = MessageDialog.openQuestion(getShell(), BootstrapWizardLanguage.get("dbsetup.test.encryptwarn.title"), BootstrapWizardLanguage.get("dbsetup.test.encryptwarn.message"));
				}
			} else if (showOk) {
				MessageDialog.openInformation(getShell(), BootstrapWizardLanguage.get("dbsetup.test.ok.title"), BootstrapWizardLanguage.get("dbsetup.test.ok.message"));
			}
		} catch (Exception e) {
			getLog().error("Test connection failed: ", e);
			MessageDialog.openError(getShell(), BootstrapWizardLanguage.get("dbsetup.test.error.title"), BootstrapWizardLanguage.get("dbsetup.test.error.message", e.getLocalizedMessage()));
			tested = false;
		} finally {
			if (con != null) try {
				con.close();
			} catch (SQLException e) {} // Ignore
		}
		return tested;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performNext() {
		if (!tested) return testConnection(false);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performFinish() {
		IJdbcConnectionProvider2 provider = getDbProvider();

		// Create the dbconfig.xml file
		try {
			File configFile = DbConfigLocator.getUserDbConfigFile();
			File f = configFile.getParentFile();
			if (!f.exists()) f.mkdirs();
			PrintWriter out = new PrintWriter(configFile);
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<dbconfig>");
			out.println("   <property name=\"hibernate.dialect\">"+((IHibernateDialectProvider)provider).getHibernateDialect()+"</property>");
			out.println("   <datasource class=\""+((IDataSourceProvider)provider).getDataSource()+"\">");
			out.println("      <property name=\"url\">"+provider.getDriverUrl(getHost(), getPort(), getDbName(), getUser(), getPassword())+"</property>");
			out.println("      <property name=\"user\">"+provider.getDbLogin(getUser())+"</property>");
			out.println("      <property name=\"password\">"+provider.getDbPassword(getPassword())+"</property>");
			out.println("   </datasource>");
			out.println("</dbconfig>");
			out.flush();
			out.close();

			RsBudgetConfigurationService.setEncrypted(isEncryption());
		} catch (Exception e) {
			getLog().error("Cannot create dbconfig.xml file:", e);
			return e.getLocalizedMessage();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void performCancel() {
		try {
			File f = DbConfigLocator.getUserDbConfigFile();
			if (f.exists()) {
				File failed = new File(f.getAbsolutePath()+".failed");
				if (failed.exists()) failed.delete();
				f.renameTo(failed);
			}
		} catch (Exception e) {
			// Ignore
		}
	}

	protected static IJdbcConnectionProvider2[] getConnectionProviders() {
		IJdbcConnectionProvider2 p1 = new HyperSqlFileJdbcConnectionProvider();
		p1.setAdditionalArgumentEnabled(0, false);
		p1.setDefaultAdditionalArgument(0, PreferencesService.INSTANCE.getUserPreferencesHome(Plugin.APPLICATION_KEY).getAbsolutePath()+File.separatorChar+"database.hsqldb");

		IJdbcConnectionProvider2 p2 = new MySql5JdbcConnectionProvider();
		p2.setDefaultDbLogin("rsbudget");
		p2.setDefaultDbPassword("rsbudget");
		//		"DB2", "",
		//		"Ingres 3", "",
		//		"Oracle", "",
		return new IJdbcConnectionProvider2[] { p1, p2 };
	}

	protected static class ConnectionProviderLabelProvider extends LabelProvider {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getText(Object element) {
			IJdbcConnectionProvider provider = (IJdbcConnectionProvider)element;
			if (provider instanceof IDisplayProvider) {
				return ((IDisplayProvider)provider).getDisplay();
			}
			return provider.toString();
		}

	}
}
