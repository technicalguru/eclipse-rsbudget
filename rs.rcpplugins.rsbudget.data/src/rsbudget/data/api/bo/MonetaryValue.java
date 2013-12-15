/**
 * 
 */
package rsbudget.data.api.bo;

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
	public float getAmount();
	
	/**
	 * Sets the amount.
	 * @param amount the value
	 */
	public void setAmount(float amount);
}
