/**
 * 
 */
package rsbudget.hbci;

/**
 * @author ralph
 *
 */
public enum CallbackNeed {

	/** ID 0 */
	DUMMY_0,
	/** ID 1 */
	DUMMY_1,
	/** ID 2. Ursache des Callback-Aufrufes: Chipkarte benötigt (im Chipkartenterminal). Dieser Callback
    tritt auf, wenn der HBCI-Kernel auf das Einlegen der HBCI-Chipkarte in den Chipkartenleser
    wartet. Als Reaktion auf diesen Callback darf nur eine entsprechende Aufforderung o.ä.
    angezeigt werden, die Callback-Methode muss anschließend sofort beendet werden. Das eigentliche
    "Warten" auf die Chipkarte sowie das Erkennen, dass eine Chipkarte eingelegt wurde,
    wird von <em>HBCI4Java</em> übernommen. Ist das Einlegen der Chipkarte abgeschlossen, so wird ein
    weiterer Callback mit dem Code <code>HAVE_CHIPCARD</code> erzeugt.*/
	NEED_CHIPCARD,
	/** ID 3. Ursache des Callback-Aufrufes: PIN-Eingabe am Chipkartenterminal erwartet. Dieser Callback
    zeigt an, dass der Anwender jetzt die HBCI-PIN am Chipkartenterminal eingeben muss. Hier
    gilt das gleiche wie beim Code <code>NEED_CHIPCARD</code>: Die Callback-Methode darf hier
    nur eine entsprechende Meldung o.ä. anzeigen und muss dann sofort zurückkehren -- <em>HBCI4Java</em> erledigt die
    eigentliche Entgegennahme der PIN. Wurde die PIN eingegeben (oder die Eingabe abgebrochen),
    so wird ein weiterer Callback-Aufruf mit dem Code <code>HAVE_HARDPIN</code> erzeugt. */
	NEED_HARDPIN,
	/** ID 4. Ursache des Callback-Aufrufes: PIN-Eingabe über Computer-Tastatur benötigt. Alternativ zum
    Callback <code>NEED_HARDPIN</code> kann dieser Callback auftreten, wenn die direkte PIN-Eingabe
    am Chipkartenterminal nicht möglich oder deaktiviert ist. In diesem Fall muss die PIN
    "softwaremäßig" eingegeben werden, d.h. der Anwender gibt die PIN über die PC-Tastatur
    ein, welche über diesen Callback-Aufruf an den HBCI-Kernel übergeben wird. Der Kernel
    übermittelt die PIN anschließend zur Verifikation an die Chipkarte. In diesem Falle gibt es
    keinen weiteren Callback-Aufruf, wenn die PIN-Verifikation abgeschlossen ist! */
	NEED_SOFTPIN,
	/** ID 5. Ursache des Callback-Aufrufes: PIN-Eingabe über Chipkartenterminal abgeschlossen. Dieser Callback
    tritt auf, wenn die direkte PIN-Eingabe am Chipkartenleser abgeschlossen (oder abgebrochen) ist.
    Dieser Aufruf kann dazu genutzt werden, evtl. angezeigte Meldungsfenster ("Bitte jetzt PIN eingeben")
    wieder zu schließen. */ 
	HAVE_HARDPIN,
	/** ID 6. Ursache des Callback-Aufrufes: Chipkarte wurde in Chipkartenterminal eingelegt. Dieser Callback
    tritt auf, wenn das Einlegen der Chipkarte in den Chipkartenleser abgeschlossen (oder abgebrochen) ist.
    Dieser Aufruf kann dazu genutzt werden, evtl. angezeigte Meldungsfenster ("Bitte jetzt Karte einlegen einlegen")
    wieder zu schließen. */
	HAVE_CHIPCARD,
	/** ID 7. Ursache des Callback-Aufrufes: Länderkennzeichen der Bankverbindung benötigt. Der Kernel benötigt
    für ein neu zu erstellendes Passport-Medium das Länderkennzeichen der Bank, für die dieses
    Passport benutzt werden soll. Da es sich i.d.R. um deutsche Banken handelt, kann die Callback-Routine
    hier immer "DE" zurückgeben, anstatt tatsächlich auf eine Nutzereingabe zu warten. */
	NEED_COUNTRY,
	/** ID 8. Ursache des Callback-Aufrufes: Bankleitzahl der Bank benötigt. Für ein neu zu erstellendes Passport-Medium
    wird die Bankleitzahl der Bank benötigt, für die dieses Passport verwendet werden soll. */
	NEED_BLZ,
	/** Id 9. Ursache des Callback-Aufrufes: Netzwerkadresse des HBCI-Servers benötigt. Es wird die Hostadresse
    benötigt, unter welcher der HBCI-Server der Bank zu erreichen ist. Dieses Callback tritt nur auf,
    wenn der Kernel ein neues Passport-Medium erzeugt. Bei RDH- bzw. DDV-Passports wird hier eine
    IP-Adresse oder ein vollständiger Hostname erwartet. Für PIN/TAN-Passports wird hier die URL
    erwartet, unter der der HBCI-PIN/TAN-Handler auf entsprechende HTTPS-Requests reagiert. Dabei
    muss das Prefix "<code>https://</code>" weggelassen werden (also beispielsweise 
    "<code>www.hbci-kernel.de/pintan/PinTanServlet</code>").*/
	NEED_HOST,
	/** ID 10. Ursache des Callback-Aufrufes: TCP-Port, auf dem der HBCI-Server arbeitet (3000), benötigt. Dieser
    Callback tritt nur auf, wenn ein neues Passport-Medium vom Kernel erzeugt wird. Da die TCP-Portnummer
    für HBCI-Server immer "3000" ist, kann dieser Wert direkt von der Callback-Methode zurückgegeben
    werden, anstatt auf eine Nutzereingabe zu warten. */
	NEED_PORT,
	/** ID 11. Ursache des Callback-Aufrufes: Nutzerkennung für HBCI-Zugang benötigt. Wird beim Anlegen eines neuen
    Passport-Mediums und manchmal beim erstmaligen Benutzen einer DDV-Chipkarte erzeugt, wenn auf der
    Chipkarte die Benutzerkennung noch nicht gespeichert ist. */
	NEED_USERID,
	/** ID 12. Ursache des Callback-Aufrufes: Bestätigung für neue Instituts-Schlüssel benötigt (INI-Brief-Vergleich).
    Dieser Callback tritt nur bei Verwendung des RDH-Verfahrens auf. Bei einer Dialoginitialisierung
    versucht <em>HBCI4Java</em>, die öffentlichen Schlüssel des Kreditinstitutes zu aktualisieren. Werden
    tatsächlich neue Schlüsseldaten empfangen (was i.d.R. nur beim erstmaligen Initialisieren eines
    Passport-Mediums auftritt), so müssen diese Schlüsseldaten vom Anwender verifiziert werden. Dazu muss
    er die Schlüsseldaten, die <em>HBCI4Java</em> empfangen hat, mit den Daten vergleichen, die die Bank in
    einem INI-Brief mitgeteilt hat. Erst wenn dieser Vergleich positiv abläuft, wird <em>HBCI4Java</em> diese
    Schlüssel für die Kommunikation mit der Bank benutzen.
    <p>Beim Auftreten dieses Callbacks muss die Anwendung also die gerade empfangenen Schlüsseldaten der
    Bank (öffentlicher Signier-/Chiffrierschlüssel) geeignet anzeigen (Exponent, Modulus, Hash-Wert) und
    den Anwender auffordern, diese Daten mit denen aus dem INI-Brief zu vergleichen. Dieser Callback
    erwartet als Rückgabedaten einen Boolean-Wert (siehe {@link #TYPE_BOOLEAN}). Sind die Daten
    in Ordnung, so muss die Callback-Methode einen leeren String in dem Rückgabedaten-StringBuffer
    zurückgeben, ansonsten füllt sie den StringBuffer mit einem beliebigen nichtleeren String (siehe dazu
    {@link #callback(org.kapott.hbci.passport.HBCIPassport,int,String,int,StringBuffer)} und
    die Beschreibung des Rückgabe-Datentyps {@link #TYPE_BOOLEAN})).</p>
    <p>Da im Moment keine dokumentierten Methoden zur Verfügung stehen, um aus einem Passport die
    entsprechenden Schlüsseldaten zum Anzeigen zu extrahieren, wird folgendes Vorgehen empfohlen:
    die Anwendung erzeugt eine HBCICallback-Klasse, die von einer der bereits vorhandenen 
    Default-Implementationen ({@link org.kapott.hbci.callback.HBCICallbackConsole},
    {@link org.kapott.hbci.callback.HBCICallbackSwing}, ...) abgeleitet ist. Tritt dieser Callback
    auf, so kann die Anwendung mit <code>super.callback(...)</code> die bereits implementierte
    Version des entsprechenden Handlers aufrufen. In diesen Default-Implementationen werden zur Zeit
    nicht dokumentierte Passport-Funktionen benutzt, um die Schlüsseldaten zu extrahieren.</p>*/
	NEED_NEW_INST_KEYS_ACK,
	/** ID 13. Ursache des Callback-Aufrufes: neue Nutzerschlüssel generiert (INI-Brief erforderlich). Dieser Callback
    tritt nur bei Verwendung von RDH-Passports auf. Wird ein RDH-Passport neu erstellt, so werden für
    den Bankkunden neue Schlüssel für die Signierung und Verschlüsselung der HBCI-Nachrichten erzeugt.
    Die öffentlichen Teile dieser Schlüssel werden von <em>HBCI4Java</em> an die Bank gesandt. Diese schaltet
    die neuen Schlüssel aber erst dann frei, wenn ihre Authentizität durch einen INI-Brief bestätigt
    wird, den der Kunde erzeugen und ebenfalls an die Bank senden muss (per Post oder Fax).
    <p>Nach der Schlüsselerzeugung und dem erfolgreichen Versand der Schlüsseldaten erzeugt <em>HBCI4Java</em>
    also diesen Callback. Die Anwendung muss in diesem Fall den Anwender darüber informieren, dass
    seine neuen Schlüssel erst dann freigeschaltet werden, wenn er einen entsprechenden INI-Brief
    generiert und zur Bank geschickt hat (und diese die Schlüsseldaten auf Übereinstimmung verglichen
    hat). Zum Generieren eines INI-Briefes kann das Tool {@link org.kapott.hbci.tools.INILetter}
    benutzt werden, was Teil von <em>HBCI4Java</em> ist.</p>
    <p>Nachdem dieser Callback abgearbeitet wurde, wirft <em>HBCI4Java</em> eine Exception (<code>NeedKeyAckException</code>)
    und bricht damit die Ausführung des aktuellen HBCI-Dialoges ab. Ein HBCI-Dialog zum Ausführen von
    Geschäftsvorfällen kann erst dann wieder stattfinden, wenn die Bank die Schlüssel freigeschaltet hat.
    Wird ein HBCI-Dialog begonnen, obwohl die Bank die neuen Schlüssel noch nicht aktiviert hat,
    wird der HBCI-Server mit einer entsprechenden Fehlermeldung beim Initialisieren des HBCI-Dialoges
    antworten.</p>*/
	HAVE_NEW_MY_KEYS,
	/** ID 14. Ursache des Callback-Aufrufes: Institutsnachricht erhalten. Tritt dieser Callback auf, so enthält
    der <code>msg</code>-Parameter der <code>callback</code>-Methode (siehe
    {@link #callback(org.kapott.hbci.passport.HBCIPassport,int,String,int,StringBuffer)} einen
    String, den die Bank als Kreditinstitutsnachricht an den Kunden gesandt hat. Diese Nachricht sollte
    dem Anwender i.d.R. angezeigt werden. <em>HBCI4Java</em> erwartet auf diesen Callback keine Antwortdaten. */
	HAVE_INST_MSG,
	/** ID 15. Ursache des Callback-Aufrufes: Chipkarte soll aus Chipkartenterminal entfernt werden. Dieser Callback
    wird zur Zeit noch nicht benutzt. */
	NEED_REMOVE_CHIPCARD,
	/** ID 16. Ursache des Callback-Aufrufes: PIN für PIN/TAN-Verfahren benötigt. Dieser Callback tritt nur bei
    Verwendung von PIN/TAN-Passports auf. Benötigt <em>HBCI4Java</em> die PIN, um die digitale Signatur zu
    erzeugen, wird sie über diesen Callback abgefragt. */
	NEED_PT_PIN,
	/** ID 17. Ursache des Callback-Aufrufes: eine TAN für PIN/TAN-Verfahren benötigt. Dieser Callback tritt nur bei
    Verwendung von PIN/TAN-Passports auf. Benötigt <em>HBCI4Java</em> eine TAN, um eine digitale Signatur zu
    erzeugen, wird sie über diesen Callback abgefragt. */
	NEED_PT_TAN,
	/** ID 18. Ursache des Callback-Aufrufes: Kunden-ID für HBCI-Zugang benötigt. Dieser Callback tritt nur beim
    Erzeugen eines neuen Passports auf. <em>HBCI4Java</em> benötigt die Kunden-ID, die das Kreditinstitut
    dem Bankkunden zugewiesen hat (steht meist in dem Brief mit den Zugangsdaten). Hat eine Bank einem
    Kunden keine separate Kunden-ID zugewiesen, so muss an dieser Stelle die Benutzer-Kennung (User-ID)
    zurückgegeben werden. */
	NEED_CUSTOMERID,
	/** ID 19. <p>Ursache des Callback-Aufrufes: Fehler beim Verifizieren einer Kontonummer mit Hilfe
    des jeweiligen Prüfzifferverfahrens. Tritt dieser Callback auf, so hat <em>HBCI4Java</em>
    festgestellt, dass eine verwendete Kontonummer den Prüfziffercheck der dazugehörigen Bank nicht
    bestanden hat. Der Anwender soll die Möglichkeit erhalten, die Kontonummer und/oder 
    Bankleitzahl zu korrigieren. Dazu wird ein String in der Form "BLZ|KONTONUMMER" im Parameter
    <code>retData</code> der <code>callback</code>-Methode übergeben. Die Anwendung kann dem
    Anwender also BLZ und Kontonummer anzeigen und diese evtl. ändern lassen. Die neue BLZ und
    Kontonummer muss im Ergebnis wieder in der o.g. Form in die Rückgabevariable
    <code>retData</code> eingetragen werden. Wurden BLZ oder Kontonummer geändert,
    so führt <em>HBCI4Java</em> eine erneute Prüfung der Daten durch - schlägt diese
    wieder fehl, so wird der Callback erneut erzeugt, diesmal natürlich mit den neuen
    (vom Anwender eingegebenen) Daten. Werden die Daten innerhalb der Callback-Methode nicht
    geändert (bleibt also der Inhalt von <code>retData</code> unverändert), so übernimmt
    <em>HBCI4Java</em> die Kontodaten trotz des fehlgeschlagenen Prüfziffern-Checks</p>
    <p>Die automatische Überprüfung von Kontonummern findet statt, wenn HBCI-Jobs mit
    Hilfe des Highlevel-Interfaces (siehe dazu Paketbeschreibung von <code>org.kapott.hbci.GV</code>)
    erzeugt werden. Beim Hinzufügen eines so erzeugten Jobs zur Menge der auszuführenden
    Aufträge 
    ({@link org.kapott.hbci.GV.HBCIJob#addToQueue()})
    wird die Überprüfung für alle in diesem Job benutzten Kontonummern durchgeführt. Für jeden
    Prüfzifferfehler, der dabei entdeckt wird, wird dieser Callback erzeugt.<br/>
    Tritt beim Überprüfen einer IBAN ein Fehler auf, wird statt dessen
    {@link #HAVE_IBAN_ERROR} als Callback-Reason verwendet. */
	HAVE_CRC_ERROR,
	/** ID 20. <p>Ursache des Callback-Aufrufes: Es ist ein Fehler aufgetreten, der auf Wunsch 
    des Anwenders ignoriert werden kann. Durch Setzen bestimmter Kernel-Parameter 
    (siehe {@link org.kapott.hbci.manager.HBCIUtils#setParam(String,String)}) kann
    festgelegt werden, dass beim Auftreten bestimmter Fehler zur Laufzeit nicht sofort eine Exception
    geworfen wird, sondern dass statt dessen erst dieser Callback erzeugt wird, welcher als <code>msg</code>
    eine entsprechende Problembeschreibung enthält. <em>HBCI4Java</em> erwartet einen
    boolschen Rückgabewert, der beschreibt, ob der Fehler ignoriert werden soll oder ob eine
    enstprechende Exception erzeugt werden soll. Der Anwender kann den Fehler ignorieren, indem
    im <code>retData</code> Rückgabedaten-Objekt ein leerer String zurückgegeben wird, oder er kann
    erzwingen, dass <em>HBCI4Java</em> tatsächlich abbricht, indem ein nicht-leerer String im
    <code>retData</code>-Objekt zurückgegen wird. Siehe dazu auch die Beschreibung des
    Rückgabe-Datentyps {@link #TYPE_BOOLEAN}.</p>
    <p>Das Ignorieren eines Fehlers kann dazu führen, dass <em>HBCI4Java</em> später trotzdem eine
    Exception erzeugt, z.B. weil der Fehler in einem bestimmten Submodul doch nicht einfach ignoriert
    werden kann, oder es kann auch dazu führen, dass Aufträge von der Bank nicht angenommen werden usw.
    Es wird aber in jedem Fall eine entsprechende Fehlermeldung erzeugt.</p> */
	HAVE_ERROR,

	/** ID 21. Ursache des Callback-Aufrufes: Passwort für das Einlesen der Schlüsseldatei
    benötigt. Dieser Callback tritt beim Laden eines Passport-Files auf, um nach dem 
    Passwort für die Entschlüsselung zu fragen. 
    ACHTUNG: Die folgenden Zeichen duerfen NICHT im Passwort enthalten sein: ß´°§üÜöäÖÄ
	 */
	NEED_PASSPHRASE_LOAD,
	/** ID 22. Ursache des Callback-Aufrufes: Passwort für das Erzeugen der Schlüsseldatei
    benötigt. Dieser Callback tritt beim Erzeugen eines neuen Passport-Files bzw. beim
    Ändern der Passphrase für eine Schlüsseldatei auf, um nach dem 
    Passwort für die Verschlüsselung zu fragen.
    ACHTUNG: Die folgenden Zeichen duerfen NICHT im Passwort enthalten sein: ß´°§üÜöäÖÄ
	 */
	NEED_PASSPHRASE_SAVE,
	/** ID 23. <p>Ursache des Callback-Aufrufes: Auswahl eines Eintrages aus einer SIZ-RDH-Datei
    benötigt. Dieser Callback tritt nur bei Verwendung der Passport-Variante
    SIZRDHFile auf. In einer SIZ-RDH-Schlüsseldatei können mehrere HBCI-Zugangsdatensätze
    gespeichert sein. Wird eine solche Datei mit mehreren Datensätzen geladen,
    so wird dieser Callback erzeugt, um den zu benutzenden Datensatz aus der Datei
    auswählen zu können.</p>
    <p>Dazu wird beim Aufruf der Callback-Routine im Parameter <code>retData</code>
    ein String übergeben, der aus Informationen über alle in der Datei vorhandenen
    Zugangsdatensätze besteht. Das Format dieses Strings ist
    <code>&lt;ID&gt;;&lt;BLZ&gt;;&lt;USERID&gt;[|&lt;ID&gt;;&lt;BLZ&gt;;&lt;USERID&gt;...]</code>
    Es werden also die verschiedenen Datensätze durch "|" getrennt dargestellt,
    wobei jeder einzelne Datensatz durch eine ID, die Bankleitzahl und die UserID
    dieses Datensatzes repräsentiert wird.</p>
    <p>Dem Anwender müssen diese Daten in geeigneter Weise zur Auswahl angezeigt
    werden. Die Callback-Routine muss schließlich die ID des vom Anwender ausgewählten
    Eintrages im <code>retData</code>-Rückgabedatenobjekt zurückgeben.</p>
    <p>Beim Aufruf der Callback-Routine könnte <code>retData</code> also folgendes
    enthalten: <code>0;09950003;Kunde-001|1;01234567;Kunde8|4;8765432;7364634564564</code>.
    Der Anwender muss sich also zwischen den Datensätzen "09950003;Kunde-001",
    "01234567;Kunde8" und "8765432;7364634564564" entscheiden. Je nach Auswahl
    muss in <code>retData</code> dann jeweils "0", "1" oder "4" zurückgegeben werden.</p>*/
	NEED_SIZENTRY_SELECT,
	/** ID 24. <p>Ursache des Callback-Aufrufes: es wird eine Netz-Verbindung zum HBCI-Server benötigt.
    Dieser Callback wird erzeugt, bevor <em>HBCI4Java</em> eine Verbindung zum HBCI-Server
    aufbaut. Bei Client-Anwendungen, die mit einer Dialup-Verbindung zum Internet arbeiten,
    kann dieser Callback benutzt werden, um den Anwender zum Aktivieren der Internet-Verbindung
    aufzufordern. Es werden keine Rückgabedaten erwartet. Sobald die Internet-Verbindung 
    nicht mehr benötigt wird, wird ein anderer Callback ({@link #CLOSE_CONNECTION}) erzeugt.</p>
    <p>Dieses Callback-Paar wird immer dann erzeugt, wenn von der aktuellen 
    <em>HBCI4Java</em>-Verarbeitungsstufe tatsächlich eine Verbindung zum Internet benötigt 
    wird bzw. nicht mehr ({@link #CLOSE_CONNECTION}) benötigt wird. U.U. werden allerdings 
    mehrere solcher Verarbeitungsstufen direkt hintereinander ausgeführt - das kann zur Folge 
    haben, dass auch diese Callback-Paare mehrmals direkt hintereinander auftreten. Das tritt
    vor allem beim erstmaligen Initialiseren eines Passports auf. Beim Aufruf von
    <code>new&nbsp;HBCIHandler(...)</code> werden verschiedene Passport-Daten mit
    der Bank abgeglichen, dabei wird u.U. mehrmals 
    <code>NEED_CONNECTION</code>/<code>CLOSE_CONNECTION</code> aufgerufen. Evtl.
    sollte der Callback-Handler der Anwendung in diesem Fall also entsprechende
    Maßnahmen treffen.</p> */
	NEED_CONNECTION,
	/** ID 25. Ursache des Callback-Aufrufes: die Netzwerk-Verbindung zum HBCI-Server wird nicht länger
    benötigt. Dieser Callback wird aufgerufen, sobald <em>HBCI4Java</em> die Kommunikation
    mit dem HBCI-Server vorläufig beendet hat. Dieser Callback kann zusammen mit dem
    Callback {@link #NEED_CONNECTION} benutzt werden, um für Clients mit Dialup-Verbindungen
    die Online-Zeiten zu optimieren. Bei diesem Callback werden keine Rückgabedaten
    erwartet */
	CLOSE_CONNECTION,
	/** ID 26. <p>Ursache des Callback-Aufrufes: es wird die Bezeichnung des zu verwendenden
    Datenfilters benötigt. Mögliche Filterbezeichnungen sind "<code>None</code>"
    (kein Filter) und "<code>Base64</code>" (Daten BASE64-kodieren). Die
    jeweilige Filterbezeichnung ist in <code>retData</code> zurückzugeben.
    Dieser Callback tritt zur Zeit nur bei Verwendung von PIN/TAN-Passports 
    auf, weil hier nicht alle Banken einheitlich mit der gleichen Art der 
    Filterung arbeiten.</p>
    <p>Normalweise wird bei PIN/TAN der <code>Base64</code>-Filter benutzt.
    Wenn bei dessen Verwendung aber keine Antwortdaten von der Bank empfangen
    werden, dann sollte die andere Variante (<code>None</code>) ausprobiert 
    werden.</p> */
	NEED_FILTER,

	/** ID 27. <p>Ursache des Callbacks: bei Verwendung von HBCI-PIN/TAN muss eines der
	 * unterstützten Verfahren ausgewählt werden. Seit FinTS-3.0 gibt es mehrere
	 * Verfahren für PIN/TAN - das "normale" Einschrittverfahren sowie mehrere
	 * Zweischritt-Verfahren. Unterstützt eine Bank mehr als ein Verfahren, so
	 * wird dieser Callback erzeugt, damit der Anwender das zu verwendende 
	 * Verfahren auswählen kann.</p>
	 * <p>Dazu wird in <code>retData</code> ein String mit folgendem Format
	 * an die Callback-Methode übergeben: 
	 * "<code>ID1:Beschreibung1|ID2:Beschreibung2...</code>". Jedes Token 
	 * "<code>ID:Beschreibung</code>" steht dabei für ein unterstütztes
	 * PIN/TAN-Verfahren. Die Callback-Methode muss die ID des vom Anwender
	 * ausgewählten PIN/TAN-Verfahrens anschließend in <code>retData</code>
	 * zurückgeben.</p> */
	NEED_PT_SECMECH,

	/** ID 28. Ursache des Callbacks: es wird ein Nutzername für die Authentifizierung
	 * am Proxy-Server benötigt. Wird für die HTTPS-Verbindungen bei HBCI-PIN/TAN 
	 * ein Proxy-Server verwendet, und verlangt dieser Proxy-Server eine
	 * Authentifizierung, so wird über diesen Callback nach dem Nutzernamen
	 * gefragt, falls dieser nicht schon durch den Kernel-Parameter
	 * <code>client.passport.PinTan.proxyuser</code> gesetzt wurde */
	NEED_PROXY_USER,

	/** ID 29. Ursache des Callbacks: es wird ein Passwort für die Authentifizierung
	 * am Proxy-Server benötigt. Wird für die HTTPS-Verbindungen bei HBCI-PIN/TAN 
	 * ein Proxy-Server verwendet, und verlangt dieser Proxy-Server eine
	 * Authentifizierung, so wird über diesen Callback nach dem Passwort
	 * gefragt, falls dieses nicht schon durch den Kernel-Parameter
	 * <code>client.passport.PinTan.proxypass</code> gesetzt wurde */
	NEED_PROXY_PASS,

	/** ID 30. Ursache des Callbacks: beim Überprüfen einer IBAN ist ein Fehler aufgetreten.
	 * in <code>retData</code> wird die fehlerhafte IBAN übergeben. Der Nutzer
	 * sollte die IBAN korrieren. Die korrigierte IBAN sollte wieder in <code>retData</code>
	 * zurückgegeben werden. Wird die IBAN nicht verändert, wird diese IBAN trotz
	 * des Fehlers verwendet. Wird eine korrigierte IBAN zum Nutzer zurückgegeben,
	 * wird für diese erneut ein Prüfsummencheck ausgeführt. Schlägt der wieder fehl,
	 * wird der Callback erneut erzeugt. Das geht so lange, bis entweder der
	 * Prüfsummencheck erfolgreich war oder bis die IBAN vom Nutzer nicht verändert
	 * wird. Siehe dazu auch {@link #HAVE_CRC_ERROR}. */
	HAVE_IBAN_ERROR,

	/** 
	 * ID 31. 
	 * @deprecated
	 **/
	NEED_INFOPOINT_ACK,

	/** ID 32. <p>Ursache des Callbacks: bei Verwendung von HBCI-PIN/TAN muss
	 * die Bezeichnung des TAN-Mediums eingegeben werden. Bei smsTAN ist
	 * das z.Bsp. der Alias-Name des Mobiltelefons, wie er bei der Bank
	 * hinterlegt wurde. Dieser Name wird verwendet, damit die SMS mit
	 * der TAN an mehrere Mobiltelefone schicken kann. */
	NEED_PT_TANMEDIA,

	/**
	 * ID 33. Ursache des Callback-Aufrufes: eine Photo-TAN für PIN/TAN-Verfahren benötigt. Dieser
	 * Callback tritt nur bei Verwendung von PIN/TAN-Passports mit dem photoTAN-Verfahren auf.
	 * Im Callback wird im StringBuffer der Wert aus dem HHDuc uebergeben. Das sind die Roh-Daten
	 * des Bildes inclusive Angaben zum Bildformat. HBCI4Java enthaelt eine Klasse "MatrixCode",
	 * mit dem diese Daten dann gelesen werden koennen.
	 **/
	NEED_PT_PHOTOTAN,
	DUMMY_34,
	DUMMY_35,
	DUMMY_36,
	DUMMY_37,
	DUMMY_38,
	DUMMY_39,

    /** <p>Ursache des Callbacks: falsche PIN eingegeben */
    WRONG_PIN,
    
    /** <p>Ursache des Callbacks: Dialogantwort 3072 der GAD - UserID und CustomerID werden ausgetauscht */
    /** <p>im Parameter retData stehen die neuen Daten im Format UserID|CustomerID drin */
    USERID_CHANGED;

}
