/**
 * 
 */
package rsbudget.parts.categories;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;

import rsbudget.Plugin;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Category;

/**
 * Creates a new category.
 * @author ralph
 *
 */
public class NewCategoryHandler {

	@Inject
	private RsBudgetDaoFactory factory;
	
	/**
	 * Constructor.
	 */
	public NewCategoryHandler() {
	}

	/**
	 * The run method.
	 */
	@Execute
	public void execute() {
		factory.begin();
		Category newRow = factory.getCategoryDAO().newInstance();
		String name = Plugin.translate("part.categories.label.newcategory"); int index = 0;
		while (factory.getCategoryDAO().findBy(name) != null) {
			index++;
			name = Plugin.translate("part.categories.label.newcategory.indexed", index);
		}
		newRow.setName(name);
		factory.getCategoryDAO().create(newRow);
		factory.commit();
	}
	
}
