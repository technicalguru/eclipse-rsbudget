package rsbudget.parts.transactions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

import rs.baselib.util.RsMonth;
import rs.e4.SwtUtils;
import rsbudget.Plugin;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.util.PlanStatus;
import rsbudget.statusbar.FinancialStatusEntry;
import rsbudget.util.CurrencyLabelProvider;
import rsbudget.util.RsBudgetColors;

public class CurrentStatusView extends Composite {

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Plugin.translate("part.transactions.format.date.short"));

	private final FormToolkit toolkit;
	private ResourceManager resourceManager;
	
	private Label lStatusStart;
	private Label statusStart;
	private Label lOpenItems;
	private Label openItems;
	private Label lPlannedExpenses;
	private Label plannedExpenses;
	private Label lActualExpenses;
	private Label actualExpenses;
	private Label lCurrentStatus;
	private Label currentStatus;
	private Label lRelativeBalance;
	private Label relativeBalance;
	private Label lPlannedIncome;
	private Label plannedIncome;
	private Label lActualIncome;
	private Label actualIncome;
	private Label lStatusEnd;
	private Label statusEnd;
	private Label lAbsoluteBalance;
	private Label absoluteBalance;
	private Label lPlannedBalance;
	private Label plannedBalance;
	private Label lActualBalance;
	private Label actualBalance;
	private Plan plan;
	private RsMonth month;
	private PropertyChangeListener listener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			handlePropertyChange(evt);
		}
	};
	
	@Inject
	private UISynchronize uiSynchronize;
	
	@Inject
	private IEventBroker eventBroker;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CurrentStatusView(Composite parent, LocalResourceManager resourceManager, int style) {
		super(parent, style);
		toolkit = new FormToolkit(parent.getDisplay());
		this.resourceManager = resourceManager;
		
		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				toolkit.dispose();
			}
		});
		
		toolkit.adapt(this);
		toolkit.paintBordersFor(this);
		setLayout(new GridLayout(8, false));
		
		Font boldFont = resourceManager.createFont(SwtUtils.deriveBoldFont(parent.getFont()));
		
		lStatusStart = toolkit.createLabel(this, Plugin.translate("part.transactions.label.status.start"), SWT.RIGHT);
		lStatusStart.setToolTipText(Plugin.translate("part.transactions.label.status.start.tooltip"));
		lStatusStart.setFont(boldFont);
		lStatusStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		statusStart = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		statusStart.setToolTipText(Plugin.translate("part.transactions.label.status.start.tooltip"));
		statusStart.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lOpenItems = toolkit.createLabel(this, Plugin.translate("part.transactions.label.ledgerless"), SWT.RIGHT);
		lOpenItems.setToolTipText(Plugin.translate("part.transactions.label.ledgerless.tooltip"));
		lOpenItems.setFont(boldFont);
		lOpenItems.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		openItems = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		openItems.setToolTipText(Plugin.translate("part.transactions.label.ledgerless.tooltip"));
		openItems.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lPlannedExpenses = toolkit.createLabel(this, Plugin.translate("part.transactions.label.expenses.planned"), SWT.RIGHT);
		lPlannedExpenses.setToolTipText(Plugin.translate("part.transactions.label.expenses.planned.tooltip"));
		lPlannedExpenses.setFont(boldFont);
		lPlannedExpenses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		plannedExpenses = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		plannedExpenses.setToolTipText(Plugin.translate("part.transactions.label.expenses.planned.tooltip"));
		plannedExpenses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lActualExpenses = toolkit.createLabel(this, Plugin.translate("part.transactions.label.expenses.actual"), SWT.RIGHT);
		lActualExpenses.setToolTipText(Plugin.translate("part.transactions.label.expenses.actual.tooltip"));
		lActualExpenses.setFont(boldFont);
		lActualExpenses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		actualExpenses = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		actualExpenses.setToolTipText(Plugin.translate("part.transactions.label.expenses.actual.tooltip"));
		actualExpenses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lCurrentStatus = toolkit.createLabel(this, Plugin.translate("part.transactions.label.status.current"), SWT.RIGHT);
		lCurrentStatus.setToolTipText(Plugin.translate("part.transactions.label.status.current.tooltip"));
		lCurrentStatus.setFont(boldFont);
		lCurrentStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		currentStatus = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		currentStatus.setToolTipText(Plugin.translate("part.transactions.label.status.current.tooltip"));
		currentStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		lRelativeBalance = toolkit.createLabel(this, Plugin.translate("part.transactions.label.balance.relative"), SWT.RIGHT);
		lRelativeBalance.setToolTipText(Plugin.translate("part.transactions.label.balance.relative.tooltip"));
		lRelativeBalance.setFont(boldFont);
		lRelativeBalance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		relativeBalance = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		relativeBalance.setToolTipText(Plugin.translate("part.transactions.label.balance.relative.tooltip"));
		relativeBalance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lPlannedIncome = toolkit.createLabel(this, Plugin.translate("part.transactions.label.income.planned"), SWT.RIGHT);
		lPlannedIncome.setToolTipText(Plugin.translate("part.transactions.label.income.planned.tooltip"));
		lPlannedIncome.setFont(boldFont);
		lPlannedIncome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		plannedIncome = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		plannedIncome.setToolTipText(Plugin.translate("part.transactions.label.income.planned.tooltip"));
		plannedIncome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lActualIncome = toolkit.createLabel(this, Plugin.translate("part.transactions.label.income.actual"), SWT.RIGHT);
		lActualIncome.setToolTipText(Plugin.translate("part.transactions.label.income.actual.tooltip"));
		lActualIncome.setFont(boldFont);
		lActualIncome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		actualIncome = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		actualIncome.setToolTipText(Plugin.translate("part.transactions.label.income.actual.tooltip"));
		actualIncome.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lStatusEnd = toolkit.createLabel(this, Plugin.translate("part.transactions.label.status.end"), SWT.RIGHT);
		lStatusEnd.setToolTipText(Plugin.translate("part.transactions.label.status.end.tooltip"));
		lStatusEnd.setFont(boldFont);
		lStatusEnd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		statusEnd = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		statusEnd.setToolTipText(Plugin.translate("part.transactions.label.status.end.tooltip"));
		statusEnd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		lAbsoluteBalance = toolkit.createLabel(this, Plugin.translate("part.transactions.label.balance.absolute"), SWT.RIGHT);
		lAbsoluteBalance.setToolTipText(Plugin.translate("part.transactions.label.balance.absolute.tooltip"));
		lAbsoluteBalance.setFont(boldFont);
		lAbsoluteBalance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		absoluteBalance = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		absoluteBalance.setToolTipText(Plugin.translate("part.transactions.label.balance.absolute.tooltip"));
		absoluteBalance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				
		lPlannedBalance = toolkit.createLabel(this, Plugin.translate("part.transactions.label.balance.planned"), SWT.RIGHT);
		lPlannedBalance.setToolTipText(Plugin.translate("part.transactions.label.balance.planned.tooltip"));
		lPlannedBalance.setFont(boldFont);
		lPlannedBalance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		plannedBalance = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		plannedBalance.setToolTipText(Plugin.translate("part.transactions.label.balance.planned.tooltip"));
		plannedBalance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		lActualBalance = toolkit.createLabel(this, Plugin.translate("part.transactions.label.balance.actual"), SWT.RIGHT);
		lActualBalance.setToolTipText(Plugin.translate("part.transactions.label.balance.actual.tooltip"));
		lActualBalance.setFont(boldFont);
		lActualBalance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		actualBalance = toolkit.createLabel(this, Plugin.translate("part.transactions.label.amount"), SWT.RIGHT);
		actualBalance.setToolTipText(Plugin.translate("part.transactions.label.balance.actual.tooltip"));
		actualBalance.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

	}

	/**
	 * Adjust the data to the new plan.
	 * @param plan
	 */
	public void setModel(Plan plan) {
		if (this.plan != null) plan.removePropertyChangeListener(listener);
		this.plan = plan;
		if (plan != null) this.month = plan.getMonth();
		else this.month = null;
		updateValues();
		if (this.plan != null) plan.addPropertyChangeListener(listener);
	}

	/**
	 * Adjust the data to the new plan.
	 * @param plan
	 */
	public void setModel(RsMonth month) {
		if (this.plan != null) plan.removePropertyChangeListener(listener);
		this.plan = null;
		this.month = month;
		updateValues();
	}
	
	protected void handlePropertyChange(PropertyChangeEvent evt) {
		uiSynchronize.syncExec(new Runnable() {
			public void run() {
				updateValues();
			}
		});
	}
	
	public void updateValues() {		
		// Update the labels
		lStatusStart.setText(Plugin.translate("part.transactions.label.status.on", DATE_FORMAT.format(month.getBegin().getTime())));
		lStatusEnd.setText(Plugin.translate("part.transactions.label.status.on", DATE_FORMAT.format(month.getEnd().getTime())));
		
		PlanStatus status = new PlanStatus(plan, RsBudgetModelService.INSTANCE.getFactory());
		// Update the values
		updateValueField(this.statusStart, status.getStatusStart());
		updateValueField(this.currentStatus, status.getCurrentStatus());
		if (status.isCurrent()) {
			eventBroker.post(FinancialStatusEntry.TOPIC, status.getCurrentStatus());
		}
		updateValueField(this.statusEnd, status.getStatusEnd());
		updateValueField(this.openItems, status.getOpenItems());
		updateValueField(this.plannedExpenses, status.getPlannedExpenses());
		updateValueField(this.actualExpenses, status.getActualExpenses());
		updateValueField(this.plannedIncome, status.getPlannedIncome());
		updateValueField(this.actualIncome, status.getActualIncome());
		updateValueField(this.absoluteBalance, status.getAbsoluteBalance());
		updateValueField(this.relativeBalance, status.getRelativeBalance());
		updateValueField(this.plannedBalance, status.getPlannedBalance());
		updateValueField(this.actualBalance, status.getActualBalance());
	}
	
	/**
	 * Set the given field to the value and format it.
	 * @param field label field
	 * @param amount amount
	 */
	private void updateValueField(Label field, float amount) {
		if (field.isDisposed()) return;
		field.setText(CurrencyLabelProvider.INSTANCE.getText(amount));
		RGB rgb = RsBudgetColors.RGB_BLACK;
		if (amount < 0) {
			rgb = RsBudgetColors.RGB_RED;
		}
		field.setForeground(resourceManager.createColor(rgb));
	}
}
