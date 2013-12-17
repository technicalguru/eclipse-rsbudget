/**
 * 
 */
package rsbudget.parts.categories;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import rsbudget.Plugin;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Category;

/**
 * Deletes a category.
 * @author ralph
 *
 */
public class DeleteCategoryHandler {

	@Inject
	private RsBudgetDaoFactory factory;
	/** The selected category */
	private Category category;
	
	/**
	 * Constructor.
	 */
	public DeleteCategoryHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute(Shell shell) {
		try {
			factory.begin();
			// Check that category has no transactions or budgets 
			if ((factory.getTransactionDAO().findBy(category).size() > 0)) {
				MessageDialog.openError(shell, Plugin.translate("part.categories.dialog.deleterow.error.title"), Plugin.translate("part.categories.dialog.deleterow.error.message.tx", category.getName()));
			} else if (factory.getBudgetDAO().findBy(category).size() > 0) {
				MessageDialog.openError(shell, Plugin.translate("part.categories.dialog.deleterow.error.title"), Plugin.translate("part.categories.dialog.deleterow.error.message.budgets", category.getName()));
			} else if (factory.getPeriodicalBudgetDAO().findBy(category).size() > 0) {
				MessageDialog.openError(shell, Plugin.translate("part.categories.dialog.deleterow.error.title"), Plugin.translate("part.categories.dialog.deleterow.error.message.budgets2", category.getName()));
			} else if (factory.getPeriodicalTransactionDAO().findBy(category).size() > 0) {
				MessageDialog.openError(shell, Plugin.translate("part.categories.dialog.deleterow.error.title"), Plugin.translate("part.categories.dialog.deleterow.error.message.tx2", category.getName()));
			} else if (factory.getPlannedTransactionDAO().findBy(category).size() > 0) {
				MessageDialog.openError(shell, Plugin.translate("part.categories.dialog.deleterow.error.title"), Plugin.translate("part.categories.dialog.deleterow.error.message.ptx", category.getName()));
			} else {
				boolean rc = MessageDialog.openConfirm(shell, Plugin.translate("part.categories.dialog.deleterow.title"), Plugin.translate("part.categories.dialog.deleterow.message", category.getName()));
				if (rc) {
					factory.getCategoryDAO().delete(category);
					this.category = null;
				}
			}
		} finally {
			factory.commit();
		}
	}
	
	/**
	 * Can we execute?
	 * @return
	 */
	@CanExecute
	public boolean canExecute() {
		return category != null;
	}
	
	/**
	 * Get selection from service.
	 * @param category
	 */
	@Inject
	public void setSelection(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Category category) {
		if (category == null) {
			// No selection
		} else {
			this.category = category;
		}
	}

}
