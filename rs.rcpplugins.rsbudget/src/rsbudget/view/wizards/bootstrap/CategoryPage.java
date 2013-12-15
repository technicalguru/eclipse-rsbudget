package rsbudget.view.wizards.bootstrap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rs.e4.SwtUtils;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Category;
import rsbudget.view.wizards.AbstractWizardPage;

/**
 * Get default category.
 * @author ralph
 *
 */
public class CategoryPage extends AbstractWizardPage {

	private Text text;

	/**
	 * Create the wizard.
	 */
	public CategoryPage() {
		super("Dritte Seite");
		setTitle(BootstrapWizardLanguage.get("category.title"));
		setDescription("category.description");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		GridLayout gl_container = new GridLayout(2, false);
		gl_container.marginRight = 5;
		gl_container.marginLeft = 5;
		gl_container.marginBottom = 5;
		container.setLayout(gl_container);

		Label lblName = new Label(container, SWT.NONE);
		lblName.setText(BootstrapWizardLanguage.get("category.name"));
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		text = new Text(container, SWT.BORDER);
		text.setText(BootstrapWizardLanguage.get("category.default"));
		text.selectAll();
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		text.addListener(SWT.KeyUp, this);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public boolean canFlipToNextPage() {
		if (getErrorMessage() != null) return false;
		if (!SwtUtils.isTextEmpty(text)) return true;
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
	 * Returns the entered text.
	 * @return category name
	 */
	public String getCategoryName() {
		return text.getText().trim();
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

			// Check if category was created earlier
			Category defaultCat = factory.getCategoryDAO().findDefault();
			if (defaultCat != null) {
				getLog().info("Removing default flag from existing category");
				defaultCat.setDefault(false);
				factory.getCategoryDAO().save(defaultCat);
			}
			Category dbCategory = factory.getCategoryDAO().findBy(getCategoryName());
			if (dbCategory == null) {
				getLog().info("Creating new default category");
				Category category = factory.getCategoryDAO().newInstance();
				category.setDefault(true);
				category.setName(getCategoryName());
				factory.getCategoryDAO().create(category);
			} else {
				getLog().info("Category already exists");
				dbCategory.setDefault(true);
				factory.getCategoryDAO().save(dbCategory);
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
