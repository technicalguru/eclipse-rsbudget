package rsbudget.parts.transactions;

import java.math.BigDecimal;
import java.text.DateFormat;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import rs.baselib.util.RsDate;
import rs.e4.SwtUtils;
import rsbudget.Plugin;
import rsbudget.data.api.bo.Plan;

public class SetBalanceDialog extends TitleAreaDialog {

	private Plan plan;
	private SetBalanceForm compositeBegin;
	private SetBalanceForm compositeEnd;
	private BigDecimal beginValue = null;
	private BigDecimal endValue = null;
	
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SetBalanceDialog(Shell parentShell, Plan plan) {
		super(parentShell);
		this.plan = plan;
	}

	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE | SWT.MAX);
	}
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		LocalResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		
		setMessage(Plugin.translate("part.transactions.dialog.balances.message", TransactionsPart.HEADER_FORMATTER.format(plan.getMonth().getTime())));
		setTitle(Plugin.translate("part.transactions.dialog.balances.title"));
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label lblBalanceBegin = new Label(container, SWT.NONE);
		lblBalanceBegin.setFont(resourceManager.createFont(SwtUtils.deriveBoldFont(SwtUtils.deriveBoldFont(lblBalanceBegin.getFont()))));
		lblBalanceBegin.setText(Plugin.translate("part.transactions.dialog.balances.label.balance.on", format(plan.getMonth().getBegin())));
		
		Label lblBalanceEnd = new Label(container, SWT.NONE);
		lblBalanceEnd.setFont(resourceManager.createFont(SwtUtils.deriveBoldFont(SwtUtils.deriveBoldFont(lblBalanceEnd.getFont()))));
		lblBalanceEnd.setText(Plugin.translate("part.transactions.dialog.balances.label.balance.on", format(plan.getMonth().getEnd())));
		
		compositeBegin = new SetBalanceForm(container, SWT.NONE, plan.getBalanceStart(), new StatusOptionIterator(plan, true));
		compositeBegin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		compositeEnd = new SetBalanceForm(container, SWT.NONE, plan.getBalanceEnd(), new StatusOptionIterator(plan, false));
		compositeEnd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return area;
	}

	/**
	 * Format the date accordingly.
	 * @param date the date
	 * @return
	 */
	protected String format(RsDate date) {
		return DateFormat.getDateInstance().format(date.getTime());
	}
	
	/**
	 * Returns the entered saldo for the begin of the month.
	 * @return
	 */
	public BigDecimal getBegin() {
		return beginValue;
	}
	
	/**
	 * Returns the entered saldo for the end of the month.
	 * @return
	 */
	public BigDecimal getEnd() {
		return endValue;
	}
	
	
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void okPressed() {
		beginValue = compositeBegin.getValue();
		endValue = compositeEnd.getValue();
		super.okPressed();
	}

	
}
