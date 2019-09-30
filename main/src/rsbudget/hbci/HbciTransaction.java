/**
 * 
 */
package rsbudget.hbci;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.slf4j.Logger;

import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.util.CommonUtils;
import rs.baselib.util.RsDate;

/**
 * Holds information about a transaction loaded by HBCI.
 * @author ralph
 *
 */
public class HbciTransaction {

	private static DateFormat DATE_FORMATTER = CommonUtils.DATE_FORMATTER();
	private static DecimalFormat VALUE_FORMATTER = new DecimalFormat("#,##0.00");

	private RsDate bookingDate;
	private BigDecimal chargeValue;
	private String chargeCurrency;
	private RsDate valutaDate;
	private String gvCode;
	private BigDecimal value;
	private String valueCurrency;
	private String text;
	private String usage;
	private String eref;
	private String mref;
	private String svwz;
	private String cred;
	private BigDecimal saldo;
	private String saldoCurrency;
	private String instRef;
	private String otherIban;
	private String otherBic;
	private String otherBlz;
	private String otherAccountNumber;
	private String otherName;
	private String customerReference;
	private String additionalKey;
	private String additional;
	private boolean sepa;
	private boolean storno;
	private String hash;
	
	/**
	 * Constructor.
	 */
	public HbciTransaction() {
	}

	/**
	 * Constructor.
	 * @param entry - the HBCI transaction
	 */
	public HbciTransaction(GVRKUms.UmsLine entry) {
		if (entry != null) {
			// entry.bdate enthält Buchungsdatum
			if (entry.bdate != null) {
				setBookingDate(new RsDate(entry.bdate));
			}
			if (entry.charge_value != null) {
				setChargeValue(entry.charge_value.getBigDecimalValue());
				setChargeCurrency(entry.charge_value.getCurr());
			}
			if (entry.valuta != null) {	
				setValutaDate(new RsDate(entry.valuta));
			}
			setGvCode(entry.gvcode);
			// entry.value enthält gebuchten Betrag
			if (entry.value != null) {
				setValue(entry.value.getBigDecimalValue());
				setValueCurrency(entry.value.getCurr());
			}
			setText(entry.text);
			// Manadatsreferenz: Zeile "MREF+<referenz>"
			// entry.usage enthält die Verwendungszweck-zeilen
			// mehr Informationen sie Dokumentation zu
			//   org.kapott.hbci.GV_Result.GVRKUms
			if (entry.usage != null) {
				setUsage(StringUtils.join(entry.usage, ','));
				setEref(parseUsage("EREF", entry.usage));
				setMref(parseUsage("MREF", entry.usage));
				setSvwz(parseUsage("SVWZ", entry.usage));
				setCred(parseUsage("CRED", entry.usage));
			}
			if (entry.saldo != null) {
				setSaldo(entry.saldo.value.getBigDecimalValue());
				setSaldoCurrency(entry.saldo.value.getCurr());
			}
			setInstRef(entry.instref);
			if (entry.other != null) {
				setOtherIban(entry.other.iban);
				setOtherBic(entry.other.bic);
				setOtherBlz(entry.other.blz);
				setOtherAccountNumber(entry.other.number);
				String s = entry.other.name != null ? entry.other.name : "";
				s += entry.other.name2 != null ? entry.other.name2 : "";
				setOtherName(s);
			}
			setCustomerReference(entry.customerref);
			setAdditionalKey(entry.addkey);
			setAdditional(entry.additional);
			setSepa(entry.isSepa);
			setStorno(entry.isSepa);
		}
	}

	/**
	 * Returns the bookingDate.
	 * @return the bookingDate
	 */
	public RsDate getBookingDate() {
		return bookingDate;
	}

	/**
	 * Sets the bookingDate.
	 * @param bookingDate the bookingDate to set
	 */
	public void setBookingDate(RsDate bookingDate) {
		this.bookingDate = bookingDate;
		hash = null;
	}

	/**
	 * Returns the chargeValue.
	 * @return the chargeValue
	 */
	public BigDecimal getChargeValue() {
		return chargeValue;
	}

	/**
	 * Sets the chargeValue.
	 * @param chargeValue the chargeValue to set
	 */
	public void setChargeValue(BigDecimal chargeValue) {
		this.chargeValue = chargeValue;
	}

	/**
	 * Returns the chargeCurrency.
	 * @return the chargeCurrency
	 */
	public String getChargeCurrency() {
		return chargeCurrency;
	}

	/**
	 * Sets the chargeCurrency.
	 * @param chargeCurrency the chargeCurrency to set
	 */
	public void setChargeCurrency(String chargeCurrency) {
		this.chargeCurrency = chargeCurrency;
	}

	/**
	 * Returns the valutaDate.
	 * @return the valutaDate
	 */
	public RsDate getValutaDate() {
		return valutaDate;
	}

	/**
	 * Sets the valutaDate.
	 * @param valutaDate the valutaDate to set
	 */
	public void setValutaDate(RsDate valutaDate) {
		this.valutaDate = valutaDate;
	}

	/**
	 * Returns the gvCode.
	 * @return the gvCode
	 */
	public String getGvCode() {
		return gvCode;
	}

	/**
	 * Sets the gvCode.
	 * @param gvCode the gvCode to set
	 */
	public void setGvCode(String gvCode) {
		this.gvCode = gvCode;
		hash = null;
	}

	/**
	 * Returns the value.
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
		hash = null;
	}

	/**
	 * Returns the valueCurrency.
	 * @return the valueCurrency
	 */
	public String getValueCurrency() {
		return valueCurrency;
	}

	/**
	 * Sets the valueCurrency.
	 * @param valueCurrency the valueCurrency to set
	 */
	public void setValueCurrency(String valueCurrency) {
		this.valueCurrency = valueCurrency;
		hash = null;
	}

	/**
	 * Returns the text.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the usage.
	 * @return the usage
	 */
	public String getUsage() {
		return usage;
	}

	/**
	 * Sets the usage.
	 * @param usage the usage to set
	 */
	public void setUsage(String usage) {
		this.usage = usage;
		hash = null;
	}

	/**
	 * Returns the eref.
	 * @return the eref
	 */
	public String getEref() {
		return eref;
	}

	/**
	 * Sets the eref.
	 * @param eref the eref to set
	 */
	public void setEref(String eref) {
		this.eref = eref;
	}

	/**
	 * Returns the mref.
	 * @return the mref
	 */
	public String getMref() {
		return mref;
	}

	/**
	 * Sets the mref.
	 * @param mref the mref to set
	 */
	public void setMref(String mref) {
		this.mref = mref;
	}

	/**
	 * Returns the svwz.
	 * @return the svwz
	 */
	public String getSvwz() {
		return svwz;
	}

	/**
	 * Sets the svwz.
	 * @param svwz the svwz to set
	 */
	public void setSvwz(String svwz) {
		this.svwz = svwz;
	}

	/**
	 * Returns the cred.
	 * @return the cred
	 */
	public String getCred() {
		return cred;
	}

	/**
	 * Sets the cred.
	 * @param cred the cred to set
	 */
	public void setCred(String cred) {
		this.cred = cred;
	}

	/**
	 * Returns the saldo.
	 * @return the saldo
	 */
	public BigDecimal getSaldo() {
		return saldo;
	}

	/**
	 * Sets the saldo.
	 * @param saldo the saldo to set
	 */
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	/**
	 * Returns the saldoCurrency.
	 * @return the saldoCurrency
	 */
	public String getSaldoCurrency() {
		return saldoCurrency;
	}

	/**
	 * Sets the saldoCurrency.
	 * @param saldoCurrency the saldoCurrency to set
	 */
	public void setSaldoCurrency(String saldoCurrency) {
		this.saldoCurrency = saldoCurrency;
	}

	/**
	 * Returns the instRef.
	 * @return the instRef
	 */
	public String getInstRef() {
		return instRef;
	}

	/**
	 * Sets the instRef.
	 * @param instRef the instRef to set
	 */
	public void setInstRef(String instRef) {
		this.instRef = instRef;
	}

	/**
	 * Returns the otherIban.
	 * @return the otherIban
	 */
	public String getOtherIban() {
		return otherIban;
	}

	/**
	 * Sets the otherIban.
	 * @param otherIban the otherIban to set
	 */
	public void setOtherIban(String otherIban) {
		this.otherIban = otherIban;
	}

	/**
	 * Returns the otherBic.
	 * @return the otherBic
	 */
	public String getOtherBic() {
		return otherBic;
	}

	/**
	 * Sets the otherBic.
	 * @param otherBic the otherBic to set
	 */
	public void setOtherBic(String otherBic) {
		this.otherBic = otherBic;
	}

	/**
	 * Returns the otherBlz.
	 * @return the otherBlz
	 */
	public String getOtherBlz() {
		return otherBlz;
	}

	/**
	 * Sets the otherBlz.
	 * @param otherBlz the otherBlz to set
	 */
	public void setOtherBlz(String otherBlz) {
		this.otherBlz = otherBlz;
	}

	/**
	 * Returns the otherAccountNumber.
	 * @return the otherAccountNumber
	 */
	public String getOtherAccountNumber() {
		return otherAccountNumber;
	}

	/**
	 * Sets the otherAccountNumber.
	 * @param otherAccountNumber the otherAccountNumber to set
	 */
	public void setOtherAccountNumber(String otherAccountNumber) {
		this.otherAccountNumber = otherAccountNumber;
	}

	/**
	 * Returns the otherName.
	 * @return the otherName
	 */
	public String getOtherName() {
		return otherName;
	}

	/**
	 * Sets the otherName.
	 * @param otherName the otherName to set
	 */
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	/**
	 * Returns the customerReference.
	 * @return the customerReference
	 */
	public String getCustomerReference() {
		return customerReference;
	}

	/**
	 * Sets the customerReference.
	 * @param customerReference the customerReference to set
	 */
	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	/**
	 * Returns the additionalKey.
	 * @return the additionalKey
	 */
	public String getAdditionalKey() {
		return additionalKey;
	}

	/**
	 * Sets the additionalKey.
	 * @param additionalKey the additionalKey to set
	 */
	public void setAdditionalKey(String additionalKey) {
		this.additionalKey = additionalKey;
	}

	/**
	 * Returns the additional.
	 * @return the additional
	 */
	public String getAdditional() {
		return additional;
	}

	/**
	 * Sets the additional.
	 * @param additional the additional to set
	 */
	public void setAdditional(String additional) {
		this.additional = additional;
	}

	/**
	 * Returns the sepa.
	 * @return the sepa
	 */
	public boolean isSepa() {
		return sepa;
	}

	/**
	 * Sets the sepa.
	 * @param sepa the sepa to set
	 */
	public void setSepa(boolean sepa) {
		this.sepa = sepa;
	}

	/**
	 * Returns the storno.
	 * @return the storno
	 */
	public boolean isStorno() {
		return storno;
	}

	/**
	 * Sets the storno.
	 * @param storno the storno to set
	 */
	public void setStorno(boolean storno) {
		this.storno = storno;
	}

	private static String parseUsage(String type, Collection<String> usage) {
		StringBuilder rc = new StringBuilder();
		String prefix = type+'+';
		for (String s : usage) {
			if (rc.length() == 0) {	
				if (s.startsWith(prefix)) rc.append(s.substring(prefix.length()));
			} else if (!s.matches("[A-Z]+\\+.*")) rc.append(s);
			else break;
		}
		return rc.toString();
	}

	public String getHash() {
		if (hash == null) {
			StringBuilder s = new StringBuilder();
			s.append(toString(getBookingDate()));
			s.append(toString(getValue(), getValueCurrency()));
			s.append(toString(getSaldo(), getSaldoCurrency()));
			s.append(getUsage());
			s.append(getGvCode());
			hash = EncryptionUtils.encodeBase64(EncryptionUtils.createMD5(s.toString()));
			if (hash.length() > 255) hash = hash.substring(0, 255);
		}
		return hash;
	}
	
	private static String toString(BigDecimal value, String currency) {
		if (value != null) {
			return VALUE_FORMATTER.format(value)+" "+currency;
		}
		return null;
	}
	
	private static String toString(RsDate date) {
		if (date != null) {
			return DATE_FORMATTER.format(date.getTime());
		}
		return null;
	}

	public void debug(Logger log) {
		// entry.bdate enthält Buchungsdatum
		log.info("    bookingDate(bdate)             = "+toString(getBookingDate()));
		log.info("    chargeValue(charge_value)      = "+toString(getChargeValue(), getChargeCurrency()));
		log.info("    valutaDate(valuta)             = "+toString(getValutaDate()));
		log.info("    gvCode(gvcode)                 = "+getGvCode());
		// entry.value enthält gebuchten Betrag
		log.info("    value                          = "+toString(getValue(), getValueCurrency()));
		log.info("    text                           = "+getText());
		// Manadatsreferenz: Zeile "MREF+<referenz>"
		// entry.usage enthält die Verwendungszweck-zeilen
		// mehr Informationen sie Dokumentation zu
		//   org.kapott.hbci.GV_Result.GVRKUms
		log.info("    usage                          "); //= "+getUsage());
		log.info("       EREF                        = "+getMref());
		log.info("       MREF                        = "+getEref());
		log.info("       SVWZ                        = "+getSvwz());
		log.info("       CRED                        = "+getCred());

		log.info("    saldo                          = "+toString(getSaldo(), getSaldoCurrency()));
		log.info("    instref                        = "+getInstRef());
		log.info("    otherIBAN                      = "+getOtherIban());
		log.info("    otherBIC                       = "+getOtherBic());
		log.info("    otherBLZ                       = "+getOtherBlz());
		log.info("    otherNumber                    = "+getOtherAccountNumber());
		log.info("    otherName                      = "+getOtherName());
		log.info("    customerReference(customerref) = "+getCustomerReference());
		log.info("    additionalKey(addkey)          = "+getAdditionalKey());
		log.info("    additional                     = "+getAdditional());
		log.info("    isSepa                         = "+isSepa());
		log.info("    isStorno                       = "+isStorno());
		log.info("    uniqueHash                     = "+getHash());
	}
	
}
