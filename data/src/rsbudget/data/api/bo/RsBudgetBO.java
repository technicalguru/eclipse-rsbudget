/**
 * 
 */
package rsbudget.data.api.bo;

import java.io.Serializable;

import rs.baselib.util.IDisplayProvider;
import rs.data.api.bo.IGeneralBO;

/**
 * The base interface for all model classes.
 * @author ralph
 *
 */
public interface RsBudgetBO<K extends Serializable> extends IGeneralBO<K>, IDisplayProvider {

}
