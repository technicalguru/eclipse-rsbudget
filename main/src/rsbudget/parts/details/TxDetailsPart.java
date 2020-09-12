/**
 * 
 */
package rsbudget.parts.details;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rs.baselib.bean.IBean;
import rs.baselib.util.IWrapper;
import rs.baselib.util.RsDate;
import rs.e4.SwtUtils;
import rs.e4.util.DateLabelProvider;
import rsbudget.Plugin;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.Transaction;
import rsbudget.parts.budgets.BudgetRowWrapper;
import rsbudget.parts.transactions.TransactionsPart;
import rsbudget.parts.transactions.TxRowWrapper;
import rsbudget.util.CurrencyLabelProvider;
import rsbudget.util.RsBudgetColors;

/**
 * The transaction details information.
 * @author ralph
 *
 */
public class TxDetailsPart {

	private Composite container;
	private LocalResourceManager resourceManager;
	private IWrapper rows[] = null;

	private PropertyChangeListener listener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			setTransaction(rows);
		}
	};

	@Inject
	UISynchronize uiSynchronize;

	/**
	 * Constructor.
	 */
	public TxDetailsPart() {
	}

	/**
	 * Creates the controls.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		setTransaction(null);
	}

	public void setTransaction(final IWrapper rows[]) {
		uiSynchronize.asyncExec(new Runnable() {
			public void run() {
				_setTransaction(rows);
			}
		});
	}

	protected void _setTransaction(IWrapper rows[]) {
		if (this.rows != null) {
			for (IWrapper row : this.rows) {
				((IBean)row.getWrapped()).removePropertyChangeListener(listener);
			}
		}
		if ((container == null) || container.isDisposed()) return;
		if (container.getChildren() != null) {
			for (Control c : container.getChildren()) {
				c.dispose();
			}
		}

		if (rows == null) {
			Text text = new Text(container, SWT.CENTER|SWT.MULTI|SWT.WRAP);
			text.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 2, 1));
			text.setText(Plugin.translate("part.txdetails.message.select"));
			text.setEditable(false);
			text.setBackground(container.getBackground());
		} else if (rows.length == 1) {
			IWrapper wrapper = rows[0];
			if (wrapper instanceof TxRowWrapper) {
				TxRowWrapper row = (TxRowWrapper)wrapper;
				if (row.isBudget()) {
					Budget budget = (Budget)row.getWrapped();
					addTitle(Plugin.translate("part.txdetails.addon.budget", row.getText()));
					BigDecimal planned = row.getPlannedAmount();
					BigDecimal actual = row.getActualAmount();
					addAmount(Plugin.translate("part.txdetails.label.amount"), row.getPlannedAmount());
					BigDecimal ratio = BigDecimal.ONE;
					if (budget.getPlanned().signum() != 0) ratio = actual.divide(planned, RoundingMode.HALF_UP);
					addDiffAmount(Plugin.translate("part.txdetails.label.exhausted"), ratio, actual);
					addDiffAmount(Plugin.translate("part.txdetails.label.difference"), ratio, actual.subtract(planned));
					addBudgetCategory((Budget)null, row.getCategory());
					addPlan(row.getPlan());
				} else if (row.isTransaction()) {
					addTitle(Plugin.translate("part.txdetails.addon.tx", row.getText()));
					addDetails(row.getDetails());
					addAmount(Plugin.translate("part.txdetails.label.amount"), row.getActualAmount());
					if (!row.getActualAmount().equals(row.getPlannedAmount())) {
						addAmount(Plugin.translate("part.txdetails.label.planned"), row.getPlannedAmount());
						addAmount(Plugin.translate("part.txdetails.label.difference"), row.getActualAmount().subtract(row.getPlannedAmount()));
					}
					addDate(Plugin.translate("part.txdetails.label.accountingdate"), ((Transaction)row.getWrapped()).getTransactionDate());
					addDate(Plugin.translate("part.txdetails.label.valuedate"), row.getValueDate());
					addInformation(Plugin.translate("part.txdetails.label.account"), row.getAccount().getName());
					addAmount(Plugin.translate("part.txdetails.label.accountStatus"), row.getAccountStatusInfo());
					addBudgetCategory(row.getTxBudget(), row.getCategory());
					addPlan(row.getPlan());
				} else {
					addTitle(Plugin.translate("part.txdetails.addon.ptx", row.getText()));
					addDetails(row.getDetails());
					addAmount(Plugin.translate("part.txdetails.label.amount"), row.getPlannedAmount());
					addInformation(Plugin.translate("part.txdetails.label.account"), row.getAccount().getName());
					addBudgetCategory(row.getTxBudget(), row.getCategory());
					addPlan(row.getPlan());
				}
			} else if (wrapper instanceof BudgetRowWrapper) {
				BudgetRowWrapper row = (BudgetRowWrapper)wrapper;
				if (row.isBudget()) {
					addTitle(Plugin.translate("part.txdetails.addon.periodicalbudget", row.getText()));
					addAmount(Plugin.translate("part.txdetails.label.amount"), row.getAmount());
					addBudgetCategory((PeriodicalBudget)null, row.getCategory());
				} else {
					PeriodicalTransaction ptx = (PeriodicalTransaction)row.getWrapped();
					addTitle(Plugin.translate("part.txdetails.addon.periodicaltx", row.getText()));
					addDetails(row.getDetails());
					addAmount(Plugin.translate("part.txdetails.label.amount"), row.getAmount());
					addInformation(Plugin.translate("part.txdetails.label.account"), ptx.getAccount().getName());
					addBudgetCategory(row.getTxBudget(), row.getCategory());
				}
			}
		} else {
			// Multiple selection
			addTitle(Plugin.translate("part.txdetails.label.multiple", rows.length));
			BigDecimal planned = BigDecimal.ZERO;
			BigDecimal actual = BigDecimal.ZERO;
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal budgetPlanned = null;
			BigDecimal budgetActual = null;
			BigDecimal lastAccountStatus = null;
			for (IWrapper wrapper : rows) {
				if (wrapper instanceof TxRowWrapper) {
					TxRowWrapper row = (TxRowWrapper)wrapper;
					if (row.isBudget()) {
						if (budgetPlanned == null) {
							budgetPlanned = BigDecimal.ZERO;
							budgetActual = BigDecimal.ZERO;
						}
						budgetPlanned = budgetPlanned.add(row.getPlannedAmount());
						budgetActual  = budgetActual.add(row.getActualAmount());
					} else if (row.isTransaction()) {
						actual  = actual.add(row.getActualAmount());
						planned = planned.add(row.getPlannedAmount());
						total   = total.add(row.getActualAmount());
						lastAccountStatus = row.getAccountStatusInfo();
					} else {
						planned = planned.add(row.getPlannedAmount());
						total   = total.add(row.getPlannedAmount());
					}
				} else if (wrapper instanceof BudgetRowWrapper) {
					BudgetRowWrapper row = (BudgetRowWrapper)wrapper;
					if (row.isBudget()) {
						if (budgetPlanned == null) {
							budgetPlanned = BigDecimal.ZERO;
						}
						budgetPlanned = budgetPlanned.add(row.getAmount());
					} else {
						planned = planned.add(row.getAmount());
						total   = total.add(row.getAmount());
					}
				}
			}
			addAmount(Plugin.translate("part.txdetails.label.multiple.planned"), planned);
			addAmount(Plugin.translate("part.txdetails.label.multiple.actual"), actual);
			addAmount(Plugin.translate("part.txdetails.label.multiple.total"), total);
			if (budgetPlanned != null) {
				addAmount(Plugin.translate("part.txdetails.label.multiple.budget.planned"), budgetPlanned);
			}
			if (budgetActual != null) {
				addAmount(Plugin.translate("part.txdetails.label.multiple.budget.actual"), budgetActual);
			}
			if (lastAccountStatus != null) {
				addAmount(Plugin.translate("part.txdetails.label.multiple.lastAccountStatus"), lastAccountStatus);
			}
		}
		this.rows = rows;
		if (this.rows != null) {
			for (IWrapper row : this.rows) {
				((IBean)row.getWrapped()).addPropertyChangeListener(listener);
			}
		}
		container.layout(true);
	}

	/**
	 * Adds the title to the container.
	 * @param s the title
	 */
	protected void addTitle(String s) {
		Text label = new Text(container, SWT.LEFT|SWT.MULTI|SWT.WRAP);
		label.setBackground(container.getBackground());
		label.setFont(resourceManager.createFont(SwtUtils.deriveBoldFont(SwtUtils.deriveFont(label.getFont(), 11))));
		label.setText(s);
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1);
		label.setLayoutData(gd);
	}

	/**
	 * Adds a details text to the container.
	 * @param s the text
	 */
	protected void addDetails(String s) {
		if ((s != null) && (s.trim().length() > 0)) {
			Text label = new Text(container, SWT.LEFT|SWT.MULTI|SWT.WRAP);
			label.setBackground(container.getBackground());
			label.setText(s);
			GridData gd = new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1);
			label.setLayoutData(gd);
		}
	}

	/**
	 * Adds a label to the container.
	 * @param s text
	 */
	protected void addLabel(String s) {
		Label l = new Label(container, SWT.LEFT);
		l.setText(s.trim());
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, true, false);
		l.setLayoutData(gd);
	}

	/**
	 * Adds a label to the container.
	 * @param s text
	 */
	protected void addValue(String s, RGB color) {
		if (s == null) s = "";
		Label label = new Label(container, SWT.LEFT);
		label.setText(s.trim());
		if (color != null) label.setForeground(resourceManager.createColor(color));
		GridData gd = new GridData(SWT.RIGHT, SWT.TOP, true, false);
		label.setLayoutData(gd);
	}

	/**
	 * Adds an amount to the container
	 * @param label label for the amount
	 * @param amount amount
	 */
	protected void addAmount(String label, BigDecimal amount) {
		addLabel(label);

		RGB color = null;
		if (amount.signum() < 0) color = RsBudgetColors.RGB_RED;
		addValue(CurrencyLabelProvider.INSTANCE.getText(amount), color);
	}

	protected void addDiffAmount(String label, BigDecimal ratio,  BigDecimal amount) {
		addLabel(label);

		RGB color = null;
		if (amount.signum() < 0) color = RsBudgetColors.RGB_RED;
		else if ((ratio.signum() > 0)  && (ratio.compareTo(BigDecimal.valueOf(0.1)) <= 0)) color = RsBudgetColors.RGB_YELLOW;
		addValue(CurrencyLabelProvider.INSTANCE.getText(amount), color);
	}

	/**
	 * Adds a date information to the container.
	 * @param label label
	 * @param date date
	 */
	protected void addDate(String label, RsDate date) {
		addLabel(label);
		addValue(DateLabelProvider.INSTANCE.getText(date), null);
	}

	/**
	 * Adds a text information to the container.
	 * @param label label
	 * @param s information
	 */
	protected void addInformation(String label, String s) {
		addLabel(label);
		addValue(s, null);
	}

	protected void addBudgetCategory(Budget budget, Category category) {
		String s1 = budget != null ? budget.getName() : null;
		String s2 = category != null ? category.getName() : null;
		addBudgetCategory(s1, s2);
	}

	protected void addBudgetCategory(PeriodicalBudget budget, Category category) {
		String s1 = budget != null ? budget.getName() : null;
		String s2 = category != null ? category.getName() : null;
		addBudgetCategory(s1, s2);
	}

	protected void addBudgetCategory(String budget, String category) {
		Label label = new Label(container, SWT.NONE);
		StringBuffer s = new StringBuffer();
		if (budget != null) {
			s.append(budget);
		}
		if (category != null) {
			if (!s.toString().equals(category)) {
				if (s.length() > 0) s.append(" / ");
				s.append(category);
			}
		}
		label.setText(s.toString());
		GridData gd = new GridData(SWT.LEFT, SWT.BOTTOM, true, true, 2, 1);
		label.setLayoutData(gd);
	}

	/**
	 * Adds plan information at the bottom of the container.
	 * @param plan plan
	 */
	protected void addPlan(Plan plan) {
		Label label = new Label(container, SWT.NONE);
		label.setText(TransactionsPart.HEADER_FORMATTER.format(plan.getMonth().getTime()));
		GridData gd = new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1);
		label.setLayoutData(gd);
	}

	/**
	 * Get selection from service.
	 * @param budget
	 */
	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) TxRowWrapper transactions[]) {
		if ((transactions != null) && (transactions.length == 0)) transactions = null;
		if (transactions != null) setTransaction(transactions);
	}

	/**
	 * Get selection from service.
	 * @param budget
	 */
	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) BudgetRowWrapper transactions[]) {
		if ((transactions != null) && (transactions.length == 0)) transactions = null;
		if (transactions != null) setTransaction(transactions);
	}

}
