/**
 * 
 */
package rsbudget.celledit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rs.baselib.util.CommonUtils;
import rs.e4.celledit.AbstractComboBoxEditingSupportModel;

/**
 * A combo box support from a map.
 * @author ralph
 *
 */
public class MapSelectionSupportModel extends AbstractComboBoxEditingSupportModel {
	
	private Map<?,?> map;
	
	/**
	 * Constructor.
	 */
	public MapSelectionSupportModel(Map<?,?> map, String beanProperty) {
		super(beanProperty);
		this.map = map;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public String getDisplay(Object object) {
		return CommonUtils.getDisplay(map.get(object), Locale.getDefault());
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected List<?> createOptions(Object object) {
		List<?> rc = new ArrayList<Object>(map.keySet());
		return rc;
	}

}
