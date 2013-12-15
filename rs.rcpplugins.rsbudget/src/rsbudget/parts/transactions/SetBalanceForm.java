/**
 * 
 */
package rsbudget.parts.transactions;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rsbudget.Plugin;

/**
 * Setting the status of a plan.
 * @author ralph
 *
 */
public class SetBalanceForm extends Composite {

	private Text text;
	private List<Button> buttons = new ArrayList<Button>();
	private ControlDecoration decoration;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SetBalanceForm(Composite parent, int style, Float value,  Iterator<OptionDescriptor> optionIterator) {
		super(parent, style);

		setLayout(new GridLayout(3, false));

		// Create all options
		boolean hadSelection = false;
		
		// NULL value 
		Button button = new Button(this, SWT.RADIO);
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
		gd.horizontalIndent = 10;
		button.setLayoutData(gd);
		button.setText(Plugin.translate("part.transactions.dialog.balances.label.notset"));
		if (value == null) {
			button.setSelection(true);
			hadSelection = true;
		}
		button.setData(null);
		buttons.add(button);
		
		// Individual options
		while (optionIterator.hasNext()) {
			OptionDescriptor desc = optionIterator.next();
			button = new Button(this, SWT.RADIO);
			gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1);
			gd.horizontalIndent = 10;
			button.setLayoutData(gd);
			String s = desc.getText();
			if (desc.isRecommended()) s += " "+Plugin.translate("part.transactions.dialog.balances.label.balance.recommended"); // Highlight!
			button.setText(s);
			float f = Float.valueOf(desc.getAmount());
			if (!hadSelection && (value != null) && (value.floatValue() == f)) {
				button.setSelection(true);
				hadSelection = true;
			}
			button.setData(f);
			buttons.add(button);
		}

		// Manual option
		button = new Button(this, SWT.RADIO);
		button.setText(Plugin.translate("part.transactions.dialog.balances.label.manual"));
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd.horizontalIndent = 10;
		button.setLayoutData(gd);
		button.setSelection(!hadSelection);
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				recheckManual(e); 
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				recheckManual(e);
			}

			protected void recheckManual(SelectionEvent e) {
				text.setEnabled(((Button)e.getSource()).getSelection());
			}
		});

		if (value == null) value = 0f;
		text = new Text(this, SWT.BORDER | SWT.RIGHT);
		text.setText(NumberFormat.getNumberInstance().format(value.floatValue()));
		text.setEnabled(!hadSelection);
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd.widthHint = 70;
		gd.horizontalIndent = 10;
		text.setLayoutData(gd);
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		Label label = new Label(this, SWT.NONE);
		label.setText(Currency.getInstance(Locale.getDefault()).getSymbol());
		Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR).getImage();
		decoration = new ControlDecoration(text, SWT.LEFT | SWT.CENTER);
		decoration.setMarginWidth(4);
		decoration.setImage(image);
		decoration.setDescriptionText(Plugin.translate("part.transactions.dialog.balances.error.invalidnumber"));
		decoration.hide();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean validate() {
		Float rc = parse();
		if (rc == null) decoration.show();
		else decoration.hide();
		return (rc == null);
	}

	/**
	 * Returns the selected value.
	 * @return selected value
	 */
	public Float getValue() {
		Float rc = null;
		for (Button b : buttons) {
			if (b.getSelection()) {
				rc = (Float)b.getData();
				return rc;
			}
		}

		rc = parse();
		if (rc == null) throw new RuntimeException("No value available!");
		return rc;
	}

	/**
	 * Parses the input.
	 * @return parsed value or null in case of problems
	 */
	protected Float parse() {
		Float rc = null;
		// 1st try
		if (rc == null) try {
			rc = NumberFormat.getCurrencyInstance().parse(text.getText()).floatValue();
		} catch (ParseException e) {
			rc = null;
		}

		// 2nd try
		if (rc == null) try {
			rc = NumberFormat.getNumberInstance().parse(text.getText()).floatValue();
		} catch (ParseException e) {
			rc = null;
		}
		return rc;
	}

	/**
	 * Describes an option.
	 * @author ralph
	 *
	 */
	public static class OptionDescriptor {

		private String text;
		private float amount;
		private boolean recommended;

		/**
		 * Constructor.
		 * @param text text to be displayed
		 * @param amount amount to be set
		 */
		public OptionDescriptor(String text, float amount) {
			this(text, amount, false);
		}

		/**
		 * Constructor.
		 * @param text text to be displayed
		 * @param amount amount to be set
		 * @param recommended true if its the recommended option
		 */
		public OptionDescriptor(String text, float amount, boolean recommended) {
			this.text = text;
			this.amount = amount;
			this.recommended = recommended;
		}

		/**
		 * Returns the text.
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * Returns the amount.
		 * @return the amount
		 */
		public float getAmount() {
			return amount;
		}

		/**
		 * Returns the recommended.
		 * @return the recommended
		 */
		public boolean isRecommended() {
			return recommended;
		}

		/**
		 * {@inheritDoc}
		 * Hash code is based on {@link #amount}.
		 */
		public int hashCode() {
			return Float.valueOf(amount).hashCode();
		}

		/**
		 * {@inheritDoc}
		 * Equals on amount.
		 */
		public boolean equals(Object o) {
			if (o == null) return false;
			if (!(o instanceof OptionDescriptor)) return false;
			return getAmount() == ((OptionDescriptor)o).getAmount();
		}
	}


}
