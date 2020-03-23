/**
 * 
 */
package rsbudget.hbci;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.util.CommonUtils;
import rs.baselib.util.RsMonth;
import rs.e4.util.RsMonthLabelProvider;
import rsbudget.Plugin;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.Transaction;

/**
 * The dialog for synchronizing by HBCI.
 * @author ralph
 *
 */
public class HbciSyncDialog extends TitleAreaDialog {

	private static Logger log = LoggerFactory.getLogger(HbciSyncDialog.class);
	private static DateFormat DATE_FORMATTER = CommonUtils.DATE_FORMATTER();
	private static DecimalFormat VALUE_FORMATTER = new DecimalFormat("#,##0.00");

	private LocalResourceManager resourceManager;

	//private IWorkbench workbench;

	// "Select the plan" widgets
	private Combo planCombo;
	private ComboViewer planComboViewer;
	private PlanContentProvider planContentProvider;
	private Button retrieveButton;
	// "Assign TX section"
	private Composite txParent;
	private Map<HbciTransaction, ComboViewer> assignments = new HashMap<>();
	private Map<HbciTransaction, Text>        names = new HashMap<>();

	/**
	 * Constructor.
	 * @param parentShell
	 */
	public HbciSyncDialog(Shell parentShell, IWorkbench workbench) {
		super(parentShell);
		//this.workbench = workbench;
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
		retrieveButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				retrieveHbciSelected(e);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				retrieveHbciSelected(e);
			}

		});
	}

	protected void retrieveHbciSelected(SelectionEvent e) {
		log.info("Retrieving HBCI data");
		planCombo.setEnabled(false);
		retrieveButton.setEnabled(false);
		HbciLoader loader = new HbciLoader();
		IStructuredSelection sel = (IStructuredSelection)planComboViewer.getSelection();
		RsMonth month = (RsMonth)sel.getFirstElement();
		loader.setMonth(month);
		loader.run();
		// TODO Get the HBCI transactions and build the UI for selection
		for (HbciTransaction tx : loader.getTransactions()) {
			// We need a label and a combo for each TX
			getBookingDateLabel(tx);
			getTextLabel(tx);
			getValueLabel(tx);
			getTxCombo(tx, month);
		}
		txParent.pack();
		((ScrolledComposite)txParent.getParent()).setMinSize(txParent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		txParent.getParent().layout();
		getShell().layout();
	}

	protected Label getBookingDateLabel(HbciTransaction tx) {
		return getTxLabel(DATE_FORMATTER.format(tx.getBookingDate().getTime()), false);
	}

	protected Text getTextLabel(HbciTransaction tx) {
		String s = tx.getSvwz();
		if (CommonUtils.isEmpty(s)) s = tx.getUsage();
		if (CommonUtils.isEmpty(s)) s = "Sonstige (GV"+tx.getGvCode()+")";
		if (!CommonUtils.isEmpty(tx.getOtherName())) s = tx.getOtherName()+": "+s;
		Text rc = new Text(txParent, SWT.LEFT|SWT.BORDER);
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gridData.widthHint = 600;
		rc.setLayoutData(gridData);
		rc.setText(s);
		
		names.put(tx, rc);
		return rc;
	}

	protected Label getValueLabel(HbciTransaction tx) {
		return getTxLabel(VALUE_FORMATTER.format(tx.getValue())+" "+tx.getValueCurrency(), SWT.RIGHT, false);
	}

	protected ComboViewer getTxCombo(HbciTransaction tx, RsMonth month) {
		Combo combo = new Combo(txParent, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		ComboViewer comboViewer = new ComboViewer(combo);
		AssigningContentProvider contentProvider = new AssigningContentProvider();
		comboViewer.setContentProvider(contentProvider);
		comboViewer.setLabelProvider(AssigningLabelProvider.INSTANCE);
		comboViewer.setInput(month);
		// Assigned transactions get disabled
		int selectedIndex = getPreferredSelection(comboViewer, tx);
		combo.select(selectedIndex);
		AssignmentWrapper wrapper = (AssignmentWrapper)comboViewer.getElementAt(selectedIndex);
		if (wrapper.getAction().equals(AssignmentWrapper.Action.ASSIGNED)) {
			combo.setEnabled(false);
			names.get(tx).setEnabled(false);
		}
		assignments.put(tx, comboViewer);

		return comboViewer;
	}

	protected int getPreferredSelection(ComboViewer comboViewer, HbciTransaction tx) {
		// Select accordingly (default is IGNORE)
		int selectedIndex = 0;
		int idx = 0;
		while (true) {
			AssignmentWrapper wrapper = (AssignmentWrapper)comboViewer.getElementAt(idx);
			if (wrapper == null) break;
			switch (wrapper.getAction()) {
			case ASSIGNED:
				if (wrapper.getTransaction().getHash().equals(tx.getHash())) {
					selectedIndex = idx;
					break;
				}
			}
			idx++;
		}
		return selectedIndex;
	}
	
	protected Label getTxLabel(String s, boolean excessHorizontal) {
		return getTxLabel(s, SWT.NONE, excessHorizontal);
	}

	protected Label getTxLabel(String s, int style, boolean excessHorizontal) {
		Label rc = new Label(txParent, style);
		rc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, excessHorizontal, false, 1, 1));
		rc.setText(s);
		return rc;
	}

	/**
	 * Creates the controls for selecting the plan.
	 * @param parent - the parent composite
	 */
	protected void createTransactionArea(Composite parent) {
		Group sctnTx = new Group(parent, SWT.NONE);
		sctnTx.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sctnTx.setText(Plugin.translate("dialog.hbci.transactions.title"));
		sctnTx.setLayout(new GridLayout(1, true));
		ScrolledComposite scrolled = new ScrolledComposite(sctnTx, SWT.V_SCROLL|SWT.H_SCROLL);
		scrolled.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		txParent = new Composite(scrolled, SWT.NONE);
		GridLayout gl_sctnTx = new GridLayout(4, false);
		gl_sctnTx.marginRight = 5;
		gl_sctnTx.marginLeft = 5;
		gl_sctnTx.marginBottom = 5;
		txParent.setLayout(gl_sctnTx);
		scrolled.setContent(txParent);
		scrolled.setExpandHorizontal( true );
		scrolled.setExpandVertical( true );
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
		return new Point(1200, 600);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		if (saveTransactions()) {
			super.okPressed();
		}
	}

	protected boolean saveTransactions() {
		// CHECKS:
		// 1. Make sure no Planned TX is assigned twice
		// 2. No SEPARATORS selected
		boolean error = false;
		Set<PlannedTransaction> planned = new HashSet<>();
		for (Map.Entry<HbciTransaction, ComboViewer> entry : assignments.entrySet()) {
			AssignmentWrapper assignment = (AssignmentWrapper)((IStructuredSelection)entry.getValue().getSelection()).getFirstElement();
			switch (assignment.getAction()) {
			case SEPARATOR: error = true; break;
			case PLANNED_TX:
				if (planned.contains(assignment.getPlannedTransaction())) {
					log.error("Doubled assigned: "+planned.toString());
					error = true;
				} else {
					planned.add(assignment.getPlannedTransaction());
				}
			}
			if (error) break;
		}

		// Again: Now perform actions
		if (!error) {
			RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
			try {
				factory.begin(240000);
				IStructuredSelection sel = (IStructuredSelection)planComboViewer.getSelection();
				RsMonth month = (RsMonth)sel.getFirstElement();
				Plan plan = factory.getPlanDAO().findBy(month);
				for (Map.Entry<HbciTransaction, ComboViewer> entry : assignments.entrySet()) {
					HbciTransaction tx = entry.getKey();
					String textLabel = names.get(tx).getText();
					AssignmentWrapper assignment = (AssignmentWrapper)((IStructuredSelection)entry.getValue().getSelection()).getFirstElement();
					PlannedTransaction plannedTx = null;
					Transaction transaction      = null;
					switch (assignment.getAction()) {
					case NEW:
						plannedTx = createPlannedTransaction(plan, null, tx, textLabel);
						transaction = createTransaction(plannedTx, tx);
						break;
					case CATEGORY:
						plannedTx = createPlannedTransaction(plan, null, tx, textLabel);
						plannedTx.setCategory(assignment.getCategory());
						transaction = createTransaction(plannedTx, tx);
						break;
					case BUDGET:
						plannedTx = createPlannedTransaction(plan, assignment.getBudget(), tx, assignment.getBudget().getName());
						transaction = createTransaction(plannedTx, tx);
						break;
					case PLANNED_TX:
						transaction = createTransaction(assignment.getPlannedTransaction(), tx);
						break;
					}
					if (transaction != null) {
						log.info(transaction.toString());
					}
				}
				factory.commit();
				AssigningContentProvider.invalidate(month);
			} catch (Exception e) {
				log.error("Cannot process actions", e);
				try {
					factory.rollback();
				} catch (Exception e2) {
					log.error("Cannot rollback", e2);
				}
			} finally {

			}
		}
		return !error;
	}

	/**
	 * Create a planned transaction based on this budget and transaction.
	 * @param plan - the plan
	 * @param budget - the budget to base upon (can be null for new transaction)
	 * @param transaction - the HBCI tranaction to base upon
	 * @return the planned transaction
	 */
	protected PlannedTransaction createPlannedTransaction(Plan plan, Budget budget, HbciTransaction transaction, String name) {
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		PlannedTransaction rc = factory.getPlannedTransactionDAO().newInstance();
		rc.setAccount(factory.getAccountDAO().findDefault());
		rc.setAmount(transaction.getValue());
		String s = budget != null ? transaction.getSvwz() : transaction.getMref();
		if (s.length() > 200) s = s.substring(0,200);
		rc.setAnnotation(s);
		//if (rc.getAnnotation() == null) rc.setAnnotation("");
		rc.setBudget(budget);
		rc.setCategory(budget != null ? budget.getCategory() : factory.getCategoryDAO().findDefault());
		s = name;
		if (s.length() > 100) s = s.substring(0,100);
		rc.setName(s);
		rc.setPlan(plan);
		rc.setTransaction(null);
		rc.setDisplayOrder(plan.getMaxTxDisplayOrder()+1);

		factory.getPlannedTransactionDAO().create(rc);
		return rc;
	}

	/**
	 * Create a planned transaction based on this budget and transaction.
	 * @param plannedTransaction - the planned transaction
	 * @param transaction - the HBCI tranaction to base upon
	 * @return the transaction
	 */
	protected Transaction createTransaction(PlannedTransaction planned, HbciTransaction transaction) {
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		Transaction rc = factory.getTransactionDAO().newInstance();
		rc.setAccount(planned.getAccount());
		rc.setAmount(transaction.getValue());
		rc.setAnnotation(planned.getAnnotation());
		rc.setBudget(planned.getBudget());
		rc.setCategory(planned.getCategory());
		rc.setDisplayOrder(planned.getDisplayOrder());
		rc.setPlan(planned.getPlan());
		rc.setPlannedTransaction(planned);
		rc.setText(planned.getName());
		rc.setTransactionDate(transaction.getBookingDate());
		rc.setValueDate(transaction.getValutaDate());
		String s = transaction.getOtherName() != null ? transaction.getOtherName() : "";
		if (s.length() > 50) s = s.substring(0, 50);
		rc.setPartnerName(s);
		rc.setPartnerBank(transaction.getOtherBic() != null ? transaction.getOtherBic() : "");
		s = transaction.getOtherAccountNumber() != null ? transaction.getOtherAccountNumber() : "";
		if (s.length() > 20) s = s.substring(0, 20);
		rc.setPartnerAccountNumber(s);
		rc.setHash(transaction.getHash());
		factory.getTransactionDAO().create(rc);
		planned.setTransaction(rc);
		factory.getPlannedTransactionDAO().save(planned);
		return rc;
	}
}
