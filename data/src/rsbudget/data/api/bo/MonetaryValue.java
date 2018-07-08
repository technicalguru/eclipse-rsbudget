/**
 * 
 */
package rsbudget.data.api.bo;

import java.math.BigDecimal;

/**
 * Defines an interface returning monetary values.
 * @author Ralph
 *
 */
public interface MonetaryValue {

	/**
	 * Returns the amount.
	 * @return the amount
	 */
	public BigDecimal getAmount();
	
	/**
	 * Sets the amount.
	 * @param amount the value
	 */
	public void setAmount(BigDecimal amount);
}
