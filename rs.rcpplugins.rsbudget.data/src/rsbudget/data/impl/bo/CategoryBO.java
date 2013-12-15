/**
 * 
 */
package rsbudget.data.impl.bo;

import rsbudget.data.api.bo.Category;
import rsbudget.data.dto.CategoryDTO;

/**
 * Transaction category BO wrapper.
 * @author ralph
 *
 */
public class CategoryBO extends AbstractRsBudgetDbBO<CategoryDTO> implements Category {

	/**
	 * Serial Version UID.
	 */
	private static final long serialVersionUID = 303847806201802189L;

	/**
	 * Constructor with empty DTO.
	 */
	public CategoryBO() {
		this(new CategoryDTO());
	}

	/**
	 * Constructor.
	 * @param dto the DTO to be wrapped
	 */
	public CategoryBO(CategoryDTO dto) {
		super(dto);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return getTransferObject().getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) {
		String oldValue = getName();
		getTransferObject().setName(name);
		firePropertyChange(PROPERTY_NAME, oldValue, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDisplay() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDefault() {
		return getTransferObject().isDefault();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDefault(boolean defaultCategory) {
		boolean oldValue = isDefault();
		getTransferObject().setDefault(defaultCategory);
		firePropertyChange(PROPERTY_DEFAULT, oldValue, defaultCategory);
	}

	
}
