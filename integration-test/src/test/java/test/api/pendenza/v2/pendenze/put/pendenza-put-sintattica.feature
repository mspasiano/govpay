Feature: Validazione sintattica inserimento pendenza

Background: 

* callonce read('classpath:utils/common-utils.feature')
* callonce read('classpath:configurazione/v1/anagrafica.feature')
* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_multivoce_bollo.json')
* def pendenzeBaseurl = getGovPayApiBaseUrl({api: 'pendenze', versione: 'v2', autenticazione: 'basic'})
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400

* match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| nome | pendenzaPut.nome | loremIpsum | 'nome' |
| causale | pendenzaPut.causale | null | 'causale' |
| causale | pendenzaPut.causale | loremIpsum | 'causale' |
| causale | pendenzaPut.causale | '' | 'causale' |
| numeroAvviso | pendenzaPut.numeroAvviso | loremIpsum | 'numeroAvviso' |
| numeroAvviso | pendenzaPut.numeroAvviso | 'ABC000000000000000' | 'numeroAvviso' |
| dataValidita | pendenzaPut.dataValidita | '2030-19-40' | 'dataValidita' |
| dataValidita | pendenzaPut.dataScadenza | '2030-19-40' | 'dataScadenza' |
| annoRiferimento | pendenzaPut.annoRiferimento | 'aaaa' | 'annoRiferimento' |
| tassonomiaAvviso | pendenzaPut.tassonomiaAvviso | 'xxxx' | 'tassonomiaAvviso' |
| soggettoPagatore.tipo | pendenzaPut.soggettoPagatore | null | 'soggettoPagatore' |
| soggettoPagatore.tipo | pendenzaPut.soggettoPagatore.tipo | null | 'tipo' |
| soggettoPagatore.tipo | pendenzaPut.soggettoPagatore.tipo | 'X' | 'tipo' |
| soggettoPagatore.identificativo | pendenzaPut.soggettoPagatore.identificativo | null | 'identificativo' |
| soggettoPagatore.identificativo | pendenzaPut.soggettoPagatore.identificativo | '' | 'identificativo' |
| soggettoPagatore.identificativo | pendenzaPut.soggettoPagatore.identificativo | loremIpsum | 'identificativo' |
| soggettoPagatore.identificativo | pendenzaPut.soggettoPagatore.identificativo | 'a' | 'identificativo' |
| soggettoPagatore.identificativo | pendenzaPut.soggettoPagatore.identificativo | '12345678901234567' | 'identificativo' |
| soggettoPagatore.anagrafica | pendenzaPut.soggettoPagatore.anagrafica | null | 'anagrafica' |
| soggettoPagatore.anagrafica | pendenzaPut.soggettoPagatore.anagrafica | '' | 'anagrafica' |
| soggettoPagatore.anagrafica | pendenzaPut.soggettoPagatore.anagrafica | loremIpsum | 'anagrafica' |
| soggettoPagatore.indirizzo | pendenzaPut.soggettoPagatore.indirizzo | '' | 'indirizzo' |
| soggettoPagatore.indirizzo | pendenzaPut.soggettoPagatore.indirizzo | loremIpsum | 'indirizzo' |
| soggettoPagatore.civico | pendenzaPut.soggettoPagatore.civico | '' | 'civico' |
| soggettoPagatore.civico | pendenzaPut.soggettoPagatore.civico | loremIpsum | 'civico' |
| soggettoPagatore.cap | pendenzaPut.soggettoPagatore.cap | '' | 'cap' |
| soggettoPagatore.cap | pendenzaPut.soggettoPagatore.cap | loremIpsum | 'cap' |
| soggettoPagatore.localita | pendenzaPut.soggettoPagatore.localita | '' | 'localita' |
| soggettoPagatore.localita | pendenzaPut.soggettoPagatore.localita | loremIpsum | 'localita' |
| soggettoPagatore.provincia | pendenzaPut.soggettoPagatore.provincia | '' | 'provincia' |
| soggettoPagatore.provincia | pendenzaPut.soggettoPagatore.provincia | loremIpsum | 'provincia' |
| soggettoPagatore.nazione | pendenzaPut.soggettoPagatore.nazione | 'aaa' | 'nazione' |
| soggettoPagatore.email | pendenzaPut.soggettoPagatore.email | 'verdi@giuseppe@email' | 'email' |
| soggettoPagatore.cellulare | pendenzaPut.soggettoPagatore.cellulare | '+390000000000' | 'cellulare' |
| importo | pendenzaPut.importo | null | 'importo' |
| importo | pendenzaPut.importo | '10.001' | 'importo' |
| importo | pendenzaPut.importo | '10,000' | 'importo' |
| importo | pendenzaPut.importo | '10,00.0' | 'importo' |
| importo | pendenzaPut.importo | 'aaaa' | 'importo' |
| importo | pendenzaPut.importo | '12345678901234567,89' | 'importo' |
| importo | pendenzaPut.importo | '1000000000.00' | 'importo' |
| tassonomia | pendenzaPut.tassonomia | loremIpsum | 'tassonomia' |
| direzione | pendenzaPut.direzione | loremIpsum | 'direzione' |
| divisione | pendenzaPut.divisione | loremIpsum | 'divisione' |
| voci | pendenzaPut.voci | null | 'voci' |
| voci.idVocePendenza | pendenzaPut.voci[0].idVocePendenza | null | 'idVocePendenza' |
| voci.idVocePendenza | pendenzaPut.voci[0].idVocePendenza | loremIpsum | 'idVocePendenza' |
| voci.importo | pendenzaPut.voci[0].importo | null | 'importo' |
| voci.importo | pendenzaPut.voci[0].importo | '10.001' | 'importo' |
| voci.importo | pendenzaPut.voci[0].importo | '10,000' | 'importo' |
| voci.importo | pendenzaPut.voci[0].importo | '10,00.0' | 'importo' |
| voci.importo | pendenzaPut.voci[0].importo | 'aaaa' | 'importo' |
| voci.importo | pendenzaPut.voci[0].importo | '12345678901234567,89' | 'importo' |
| voci.descrizione | pendenzaPut.voci[0].descrizione | null | 'descrizione' |
| voci.descrizione | pendenzaPut.voci[0].descrizione | loremIpsum | 'descrizione' |
| voci.codEntrata | pendenzaPut.voci[0].codEntrata | null | 'codEntrata' |
| voci.ibanAccredito | pendenzaPut.voci[1].ibanAccredito | null | 'ibanAccredito' |
| voci.tipoContabilita | pendenzaPut.voci[1].tipoContabilita | null | 'tipoContabilita' |
| voci.tipoContabilita | pendenzaPut.voci[1].tipoContabilita | 'xxx' | 'tipoContabilita' |
| voci.codiceContabilita | pendenzaPut.voci[1].codiceContabilita | null | 'codiceContabilita' |
| voci.codiceContabilita | pendenzaPut.voci[1].codiceContabilita | '' | 'codiceContabilita' |
| voci.codiceContabilita | pendenzaPut.voci[1].codiceContabilita | 'XX' | 'codiceContabilita' |
| voci.codiceContabilita | pendenzaPut.voci[1].codiceContabilita | 'XXX X' | 'codiceContabilita' |
| voci.tipoBollo | pendenzaPut.voci[2].tipoBollo | null | 'tipoBollo' |
| voci.tipoBollo | pendenzaPut.voci[2].tipoBollo | 'xxx' | 'tipoBollo' |
| voci.hashDocumento | pendenzaPut.voci[2].hashDocumento | null | 'hashDocumento' |
| voci.hashDocumento | pendenzaPut.voci[2].hashDocumento | loremIpsum | 'hashDocumento' |
| voci.provinciaResidenza | pendenzaPut.voci[2].provinciaResidenza | null | 'provinciaResidenza' |
| voci.provinciaResidenza | pendenzaPut.voci[2].provinciaResidenza | 'xxx' | 'provinciaResidenza' |
| voci.descrizioneCausaleRPT | pendenzaPut.voci[0].descrizioneCausaleRPT | '' | 'descrizioneCausaleRPT' |
| voci.descrizioneCausaleRPT | pendenzaPut.voci[0].descrizioneCausaleRPT | loremIpsum | 'descrizioneCausaleRPT' |

Scenario: Numero voci eccessivo

* set pendenzaPut.voci[3] = pendenzaPut.voci[0]
* set pendenzaPut.voci[3].idVocePendenza = 4
* set pendenzaPut.voci[4] = pendenzaPut.voci[0]
* set pendenzaPut.voci[4].idVocePendenza = 5
* set pendenzaPut.voci[5] = pendenzaPut.voci[0]
* set pendenzaPut.voci[5].idVocePendenza = 6

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 400
And match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }
And match response.dettaglio contains 'voci'

@debug
Scenario: caricamento pendenza con payload vuoto

* def pendenzaPutVuota = 
"""
{

}
"""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPutVuota
When method put
Then status 400
And match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }


Scenario: caricamento pendenza con payload vuoto

* def pendenzaPutVuota = ""

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And headers { 'Content-Type' : 'application/json' }
And request pendenzaPutVuota
When method put
Then status 400
And match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }


Scenario: caricamento pendenza con payload contenente uno spazio bianco

* def pendenzaPutVuota = " "

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And headers { 'Content-Type' : 'application/json' }
And request pendenzaPutVuota
When method put
Then status 400
And match response contains { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida' }


Scenario Outline: Validazione importi: <fieldValue> 

* def idPendenza = getCurrentTimeMillis()
* def pendenzaPut = read('msg/pendenza-put_monovoce_riferimento.json')

* set <fieldRequest> = <fieldValue>
* set <fieldRequest2> = <fieldValue>

Given url pendenzeBaseurl
And path '/pendenze', idA2A, idPendenza
And headers idA2ABasicAutenticationHeader
And request pendenzaPut
When method put
Then status 201

Examples:
| field | fieldRequest | fieldRequest2 | fieldValue |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 10 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 10.00 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 100.00 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 1000.00 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 10000.00 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 100000.00 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 1000000.00 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 10000000.00 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 100000000.00 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 100000000.00 |
| importo | pendenzaPut.importo | pendenzaPut.voci[0].importo  | 999999999.99 |
