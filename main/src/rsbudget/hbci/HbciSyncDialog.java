/**
 * 
 */
package rsbudget.hbci;

import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.util.RsMonth;
import rs.e4.util.RsMonthLabelProvider;
import rsbudget.Plugin;

/**
 * The dialog for synchronizing by HBCI.
 * @author ralph
 *
 */
public class HbciSyncDialog extends TitleAreaDialog {

	private static Logger log = LoggerFactory.getLogger(HbciSyncDialog.class);

	private LocalResourceManager resourceManager;
	
	private IWorkbench workbench;

	// "Select the plan" widgets
	private Combo planCombo;
	private ComboViewer planComboViewer;
	private PlanContentProvider planContentProvider;
	private Button retrieveButton;
	
	/**
	 * Constructor.
	 * @param parentShell
	 */
	public HbciSyncDialog(Shell parentShell, IWorkbench workbench) {
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

		setTitle(Plugin.translate("dialog.hbci.title"));
		setTitleImage(resourceManager.createImage(ImageDescriptor.createFromFile(getClass(), "/icons/Synchronize_32x32.png")));

		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(1, true));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		try {
			createPlanSelectionArea(container);
			createTransactionArea(container);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return area;
	}
	
	/**
	 * Creates the controls for selecting the plan.
	 * @param parent - the parent composite
	 */
	protected void createPlanSelectionArea(Composite parent) {
		Group sctnPlanSelect = new Group(parent, SWT.NONE);
		sctnPlanSelect.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		sctnPlanSelect.setText(Plugin.translate("dialog.hbci.planselect.title"));
		GridLayout gl_sctnPlanSelect = new GridLayout(5, false);
		gl_sctnPlanSelect.marginRight = 5;
		gl_sctnPlanSelect.marginLeft = 5;
		gl_sctnPlanSelect.marginBottom = 5;
		sctnPlanSelect.setLayout(gl_sctnPlanSelect);

		// Label
		createLabel(sctnPlanSelect, Plugin.translate("dialog.hbci.planselect.label"));
		
		// Select
		planCombo = new Combo(sctnPlanSelect, SWT.READ_ONLY);
		planCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		planComboViewer = new ComboViewer(planCombo);
		planContentProvider = new PlanContentProvider();
		planComboViewer.setContentProvider(planContentProvider);
		planComboViewer.setLabelProvider(RsMonthLabelProvider.INSTANCE);
		planComboViewer.setInput(new RsMonth());
		planCombo.select(0);
		
		// Button
		retrieveButton = new Button(sctnPlanSelect, SWT.PUSH);
		retrieveButton.setText(Plugin.translate("dialog.hbci.planselect.button"));
	}
	
	
	/**
	 * Creates the controls for selecting the plan.
	 * @param parent - the parent composite
	 */
	protected void createTransactionArea(Composite parent) {
		
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
	 * {@inheritDoc}
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(700, 500);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		// TODO savePreferences();
		super.okPressed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void cancelPressed() {
		// TODO
		super.cancelPressed();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean close() {
		// TODO dispose any
		return super.close();
	}
	
}
