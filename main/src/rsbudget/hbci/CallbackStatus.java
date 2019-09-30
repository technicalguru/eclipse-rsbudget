/**
 * 
 */
package rsbudget.hbci;

/**
 * @author ralph
 *
 */
public enum CallbackStatus {
	DUMMY_0,
	/** Kernel-Status: Erzeuge Auftrag zum Versenden. Als Zusatzinformation 
    wird bei diesem Callback das <code>HBCIJob</code>-Objekt des 
    Auftrages übergeben, dessen Auftragsdaten gerade erzeugt werden. */
	STATUS_SEND_TASK,
	/** Kernel-Status: Auftrag gesendet. Tritt auf, wenn zu einem bestimmten Job
    Auftragsdaten empfangen und ausgewertet wurden. Als Zusatzinformation wird
    das <code>HBCIJob</code>-Objekt des jeweiligen Auftrages übergeben. */
	STATUS_SEND_TASK_DONE,
	/** Kernel-Status: hole BPD. Kann nur während der Passport-Initialisierung
    ({@link org.kapott.hbci.manager.HBCIHandler#HBCIHandler(String,org.kapott.hbci.passport.HBCIPassport)})
    auftreten und zeigt an, dass die BPD von der Bank abgeholt werden müssen,
    weil sie noch nicht lokal vorhanden sind. Es werden keine zusätzlichen
    Informationen übergeben. */
	STATUS_INST_BPD_INIT,
	/** Kernel-Status: BPD aktualisiert. Dieser Status-Callback tritt nach dem expliziten
    Abholen der BPD ({@link #STATUS_INST_BPD_INIT}) auf und kann auch nach einer
    Dialog-Initialisierung auftreten, wenn dabei eine neue BPD vom Kreditinstitut
    empfangen wurde. Als Zusatzinformation wird ein <code>Properties</code>-Objekt
    mit den neuen BPD übergeben.*/
	STATUS_INST_BPD_INIT_DONE,
	/** Kernel-Status: hole Institutsschlüssel. Dieser Status-Callback zeigt an, dass
    <em>HBCI4Java</em> die öffentlichen Schlüssel des Kreditinstitutes abholt.
    Dieser Callback kann nur beim Initialisieren eines Passportes (siehe 
    {@link org.kapott.hbci.manager.HBCIHandler#HBCIHandler(String,org.kapott.hbci.passport.HBCIPassport)})
    und bei Verwendung von RDH als Sicherheitsverfahren auftreten. Es werden keine
    zusätzlichen Informationen übergeben. */
	STATUS_INST_GET_KEYS,
	/** Kernel-Status: Institutsschlüssel aktualisiert. Dieser Callback tritt
    auf, wenn <em>HBCI4Java</em> neue öffentliche Schlüssel der Bank
    empfangen hat. Dieser Callback kann nach dem expliziten Anfordern der
    neuen Schlüssel ({@link #STATUS_INST_GET_KEYS}) oder nach einer Dialog-Initialisierung
    auftreten, wenn das Kreditinstitut neue Schlüssel übermittelt hat. Es
    werden keine zusätzlichen Informationen übergeben. */
	STATUS_INST_GET_KEYS_DONE,
	/** Kernel-Status: Sende Nutzerschlüssel. Wird erzeugt, wenn <em>HBCI4Java</em>
    neue Schlüssel des Anwenders an die Bank versendet. Das tritt beim erstmaligen
    Einrichten eines RDH-Passportes bzw. nach dem manuellen Erzeugen neuer
    RDH-Schlüssel auf. Es werden keine zusätzlichen Informationen übergeben. */
	STATUS_SEND_KEYS,
	/** Kernel-Status: Nutzerschlüssel gesendet. Dieser Callback zeigt an, dass die RDH-Schlüssel
    des Anwenders an die Bank versandt wurden. Der Erfolg dieser Aktion kann nicht
    allein durch das Auftreten dieses Callbacks angenommen werden! Es wird der Status
    des Nachrichtenaustauschs ({@link org.kapott.hbci.status.HBCIMsgStatus})
    als Zusatzinformation übergeben. */
	STATUS_SEND_KEYS_DONE,
	/** Kernel-Status: aktualisiere System-ID. Dieser Status-Callback wird erzeugt, wenn
    <em>HBCI4Java</em> die System-ID, die für das RDH-Verfahren benötigt
    wird, synchronisiert. Der Callback kann nur beim Initialisieren eines Passports
    (siehe {@link org.kapott.hbci.manager.HBCIHandler#HBCIHandler(String,org.kapott.hbci.passport.HBCIPassport)})
    auftreten. Es werden keine Zusatzinformationen übergeben. */
	STATUS_INIT_SYSID,
	/** Kernel-Status: System-ID aktualisiert. Dieser Callback tritt auf, wenn im Zuge der
    Synchronisierung ({@link #STATUS_INIT_SYSID}) eine System-ID empfangen wurde. Als
    Zusatzinformation wird ein Array übergeben, dessen erstes Element die Statusinformation
    zu diesem Nachrichtenaustausch darstellt ({@link org.kapott.hbci.status.HBCIMsgStatus}) 
    und dessen zweites Element die neue System-ID ist. */
	STATUS_INIT_SYSID_DONE,
	/** Kernel-Status: hole UPD. Kann nur während der Passport-Initialisierung
    ({@link org.kapott.hbci.manager.HBCIHandler#HBCIHandler(String,org.kapott.hbci.passport.HBCIPassport)})
    auftreten und zeigt an, dass die UPD von der Bank abgeholt werden müssen,
    weil sie noch nicht lokal vorhanden sind. Es werden keine zusätzlichen
    Informationen übergeben.  */
	STATUS_INIT_UPD,
	/** Kernel-Status: UPD aktualisiert. Dieser Status-Callback tritt nach dem expliziten
    Abholen der UPD ({@link #STATUS_INIT_UPD}) auf und kann auch nach einer
    Dialog-Initialisierung auftreten, wenn dabei eine neue UPD vom Kreditinstitut
    empfangen wurde. Als Zusatzinformation wird ein <code>Properties</code>-Objekt
    mit den neuen UPD übergeben. */
	STATUS_INIT_UPD_DONE,
	/** Kernel-Status: sperre Nutzerschlüssel. Dieser Status-Callback wird erzeugt, wenn
    <em>HBCI4Java</em> einen Auftrag zur Sperrung der aktuellen Nutzerschlüssel
    generiert. Es werden keine Zusatzinformationen übergeben. */
	STATUS_LOCK_KEYS,
	/** Kernel-Status: Nutzerschlüssel gesperrt. Dieser Callback tritt auf, nachdem die
    Antwort auf die Nachricht "Sperren der Nutzerschlüssel" eingetroffen ist. Ein
    Auftreten dieses Callbacks ist keine Garantie dafür, dass die Schlüsselsperrung
    erfolgreich abgelaufen ist. Es wird der Status
    des Nachrichtenaustauschs ({@link org.kapott.hbci.status.HBCIMsgStatus})
    als Zusatzinformation übergeben. */
	STATUS_LOCK_KEYS_DONE,
	/** Kernel-Status: aktualisiere Signatur-ID. Dieser Status-Callback wird erzeugt, wenn
    <em>HBCI4Java</em> die Signatur-ID, die für das RDH-Verfahren benötigt
    wird, synchronisiert. Der Callback kann nur beim Initialisieren eines Passports
    (siehe {@link org.kapott.hbci.manager.HBCIHandler#HBCIHandler(String,org.kapott.hbci.passport.HBCIPassport)})
    auftreten. Es werden keine Zusatzinformationen übergeben. */
	STATUS_INIT_SIGID,
	/** Kernel-Status: Signatur-ID aktualisiert. Dieser Callback tritt auf, wenn im Zuge der
    Synchronisierung ({@link #STATUS_INIT_SIGID}) eine Signatur-ID empfangen wurde. Als
    Zusatzinformation wird ein Array übergeben, dessen erstes Element die Statusinformation
    zu diesem Nachrichtenaustausch darstellt ({@link org.kapott.hbci.status.HBCIMsgStatus}) 
    und dessen zweites Element die neue Signatur-ID (ein Long-Objekt) ist.*/
	STATUS_INIT_SIGID_DONE,
	/** Kernel-Status: Starte Dialog-Initialisierung. Dieser Status-Callback zeigt an, dass
    <em>HBCI4Java</em> eine Dialog-Initialisierung startet. Es werden keine
    zusätzlichen Informationen übergeben. */
	STATUS_DIALOG_INIT,
	/** Kernel-Status: Dialog-Initialisierung ausgeführt. Dieser Callback tritt nach dem
    Durchführen der Dialog-Initialisierung auf. Als
    Zusatzinformation wird ein Array übergeben, dessen erstes Element die Statusinformation
    zu diesem Nachrichtenaustausch darstellt ({@link org.kapott.hbci.status.HBCIMsgStatus}) 
    und dessen zweites Element die neue Dialog-ID ist. */
	STATUS_DIALOG_INIT_DONE,
	/** Kernel-Status: Beende Dialog. Wird ausgelöst, wenn <em>HBCI4Java</em> den
    aktuellen Dialog beendet. Es werden keine zusätzlichen Daten übergeben. */
	STATUS_DIALOG_END,
	/** Kernel-Status: Dialog beendet. Wird ausgeführt, wenn der HBCI-Dialog tatsächlich
    beendet ist. Es wird der Status
    des Nachrichtenaustauschs ({@link org.kapott.hbci.status.HBCIMsgStatus})
    als Zusatzinformation übergeben.*/
	STATUS_DIALOG_END_DONE,
	/** Kernel-Status: Erzeuge HBCI-Nachricht. Dieser Callback zeigt an, dass <em>HBCI4Java</em>
    gerade eine HBCI-Nachricht erzeugt. Es wird der Name der Nachricht als zusätzliches
    Objekt übergeben. */
	STATUS_MSG_CREATE,
	/** Kernel-Status: Signiere HBCI-Nachricht. Dieser Callback wird aufgerufen, wenn
    <em>HBCI4Java</em> die ausgehende HBCI-Nachricht signiert. Es werden keine
    zusätzlichen Informationen übergeben. */
	STATUS_MSG_SIGN,
	/** Kernel-Status: Verschlüssele HBCI-Nachricht. Wird aufgerufen, wenn <em>HBCI4Java</em>
    die ausgehende HBCI-Nachricht verschlüsselt. Es werden keine zusätzlichen
    Informationen übergeben. */
	STATUS_MSG_CRYPT,
	/** Kernel-Status: Sende HBCI-Nachricht (bei diesem Callback ist das
    <code>passport</code>-Objekt immer <code>null</code>). Wird aufgerufen,
    wenn die erzeugte HBCI-Nachricht an den HBCI-Server versandt wird. Es werden
    keine zusätzlichen Informationen übergeben. */
	STATUS_MSG_SEND,
	/** Kernel-Status: Entschlüssele HBCI-Nachricht. Wird aufgerufen, wenn die empfangene
    HBCI-Nachricht von <em>HBCI4Java</em> entschlüsselt wird. Es werden keine
    zusätzlichen Informationen übergeben. */
	STATUS_MSG_DECRYPT,
	/** Kernel-Status: Überprüfe digitale Signatur der Nachricht. Wird aufgerufen, wenn
    <em>HBCI4Java</em> die digitale Signatur der empfangenen Antwortnachricht
    überprüft. Es werden keine zusätzlichen Informationen übergeben. */
	STATUS_MSG_VERIFY,
	/** Kernel-Status: Empfange HBCI-Antwort-Nachricht (bei diesem Callback ist das
    <code>passport</code>-Objekt immer <code>null</code>). Wird aufgerufen, wenn
    die Antwort-HBCI-Nachricht vom HBCI-Server empfangen wird. Es werden keine
    zusätzlichen Informationen übergeben. */
	STATUS_MSG_RECV,
	/** Kernel-Status: Parse HBCI-Antwort-Nachricht (bei diesem Callback ist das
    <code>passport</code>-Objekt immer <code>null</code>). Wird aufgerufen, wenn 
    <em>HBCI4Java</em> versucht, die empfangene Nachricht zu parsen. Es wird
    der Name der erwarteten Nachricht als zusätzliche Information übergeben. */
	STATUS_MSG_PARSE,  

	/** 
	 * @deprecated
	 **/
	STATUS_SEND_INFOPOINT_DATA,

	/**
	 * Wird aufgerufen unmittelbar bevor die HBCI-Nachricht an den Server gesendet wird.
	 * Als zusaetzliche Information wird die zu sendende Nachricht als String uebergeben.
	 * Sie kann dann z.Bsp. in einem Log gesammelt werden, welches ausschliesslich
	 * (zusammen mit {@link HBCICallback#STATUS_MSG_RAW_RECV}) die gesendeten und
	 * empfangenen rohen HBCI-Nachrichten enthaelt. Sinnvoll zum Debuggen der Kommunikation
	 * mit der Bank.
	 */
	STATUS_MSG_RAW_SEND,
	 /**
     * Wird aufgerufen unmittelbar nachdem die HBCI-Nachricht vom Server empfangen wurde.
     * Als zusaetzliche Information wird die empfangene Nachricht als String uebergeben.
     * Sie kann dann z.Bsp. in einem Log gesammelt werden, welches ausschliesslich
     * (zusammen mit {@link HBCICallback#STATUS_MSG_RAW_SEND}) die gesendeten und
     * empfangenen rohen HBCI-Nachrichten enthaelt. Sinnvoll zum Debuggen der Kommunikation
     * mit der Bank.
     */
    STATUS_MSG_RAW_RECV,
    
    /**
     * Wie STATUS_MSG_RAW_RECV - jedoch noch vor der Entschluesselung der Daten.
     * Abhaengig vom HBCI-Verfahren kann die Nachricht aber auch hier bereits entschluesselt
     * sein. Naemlich bei HBCI-Verfahren, bei denen die Verschluesselung nicht auf
     * im HBCI-Protokoll selbst stattfindet sondern auf dem Transport-Protokoll.
     * Konkret ist das PIN/TAN. Bei Schluesseldatei und Chipkarte hingegen ist die
     * Message zu diesem Zeitpunkt hier noch verschluesselt.
     */
    STATUS_MSG_RAW_RECV_ENCRYPTED;

}
