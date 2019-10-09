Feature: Caricamento tracciato JSON

Background:

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* configure retry = { count: 20, interval: 5000 }

Scenario: Pagamento pendenza precaricata anonimo

* def idPendenza = getCurrentTimeMillis()
* def tracciato = read('classpath:test/api/backoffice/v1/tracciati/post/msg/tracciato-pendenze.json')

Given url backofficeBaseurl
And path 'pendenze', 'tracciati'
And headers basicAutenticationHeader
And request tracciato
When method post
Then status 201

* def idTracciato = response.id

Given url backofficeBaseurl
And path 'pendenze', 'tracciati', idTracciato
And headers basicAutenticationHeader
And retry until response.stato == 'ESEGUITO'
When method get