/**
 * 
 */
package rsbudget.data.api.dao;

import java.io.Serializable;

import rs.data.api.bo.IGeneralBO;
import rs.data.api.dao.IGeneralDAO;

/**
 * Implements the Hibernate specific functions.
 * @param <K> type of Key Object
 * @param <B> type of Business Object
 * @author ralph
 *
 */
public interface RsBudgetDAO<K extends Serializable, B extends IGeneralBO<K>> extends IGeneralDAO<K, B> {

}
