/**
 * Access to DeuBa REST Abi (dbAPI).
 * <p>
 * REST Call is: https://simulator-api.db.com:443/gw/dbapi/v2/transactions?iban=DE10000000000000002834&sortBy=bookingDate%5BASC%5D&limit=10&offset=0
 * </p>
 * <p>Answer is <pre>
 * {
  "totalItems": 130,
  "limit": 10,
  "transactions": [
    {
      "originIban": "DE10000000000000002834",
      "amount": -19.99,
      "paymentReference": "SEPA-BASISLASTSCHRIFT  Abrechnung 0171-658811RNGNR FLAT L",
      "counterPartyName": "Telekom AG",
      "transactionCode": "123",
      "paymentIdentification": "212+ZKLE 911/696682-X-ABC",
      "mandateReference": "MX0355443",
      "externalBankTransactionDomainCode": "D001",
      "externalBankTransactionFamilyCode": "CCRD",
      "externalBankTransactionSubFamilyCode": "CWDL",
      "bookingDate": "2017-06-07",
      "e2eReference": "E2E - Reference",
      "currencyCode": "EUR",
      "creditorId": "DE0222200004544221"
    }, ...</pre>
   ]
}
 * @author ralph
 *
 */
package rsbudget.rest.deuba;