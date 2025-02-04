Feature: Validazione sintattica intermediari

Background:

* call read('classpath:utils/common-utils.feature')
* def idIntermediario = '11111111113'
* def basicAutenticationHeader = getBasicAuthenticationHeader( { username: govpay_backoffice_user, password: govpay_backoffice_password } )
* def backofficeBaseurl = getGovPayApiBaseUrl({api: 'backoffice', versione: 'v1', autenticazione: 'basic'})
* def intermediario = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediario.json')
* def intermediarioBasicAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioBasicAuth.json')
* def intermediarioClientAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioClientAuth.json')
* def intermediarioServerAuth = read('classpath:test/api/backoffice/v1/intermediari/put/msg/intermediarioServerAuth.json')
* def loremIpsum = 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus non neque vestibulum, porta eros quis, fringilla enim. Nam sit amet justo sagittis, pretium urna et, convallis nisl. Proin fringilla consequat ex quis pharetra. Nam laoreet dignissim leo. Ut pulvinar odio et egestas placerat. Quisque tincidunt egestas orci, feugiat lobortis nisi tempor id. Donec aliquet sed massa at congue. Sed dictum, elit id molestie ornare, nibh augue facilisis ex, in molestie metus enim finibus arcu. Donec non elit dictum, dignissim dui sed, facilisis enim. Suspendisse nec cursus nisi. Ut turpis justo, fermentum vitae odio et, hendrerit sodales tortor. Aliquam varius facilisis nulla vitae hendrerit. In cursus et lacus vel consectetur.'

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediario
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| denominazione | intermediario.denominazione | null |  'denominazione' |
| principalPagoPa | intermediario.principalPagoPa | null |  'principalPagoPa' |
| urlRPT | intermediario.servizioPagoPa.urlRPT | 'htttttttp://aaa.it' |  'urlRPT' |
| abilitato | intermediario.abilitato | 'boh' |  'abilitato' |
| abilitato | intermediario.abilitato | null |  'abilitato' |
| subscriptionKey | intermediario.servizioPagoPa.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediario.servizioPagoPa.subscriptionKey | loremIpsum |  'subscriptionKey' |

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioBasicAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| username | intermediarioBasicAuth.servizioPagoPa.auth.username | null |  'username' |
| password | intermediarioBasicAuth.servizioPagoPa.auth.password | null |  'password' |
| subscriptionKey | intermediarioBasicAuth.servizioPagoPa.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediarioBasicAuth.servizioPagoPa.subscriptionKey | loremIpsum |  'subscriptionKey' |
| denominazione | intermediarioBasicAuth.denominazione | null |  'denominazione' |

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioClientAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| tipo | intermediarioClientAuth.servizioPagoPa.auth.tipo | null |  'tipo' |
| tipo | intermediarioClientAuth.servizioPagoPa.auth.tipo | 'XXXX' |  'tipo' |
| ksLocation | intermediarioClientAuth.servizioPagoPa.auth.ksLocation | null |  'ksLocation' |
| ksPassword | intermediarioClientAuth.servizioPagoPa.auth.ksPassword | null |  'ksPassword' |
| tsLocation | intermediarioClientAuth.servizioPagoPa.auth.tsLocation | null |  'tsLocation' |
| tsPassword | intermediarioClientAuth.servizioPagoPa.auth.tsPassword | null |  'tsPassword' |
| tsType | intermediarioClientAuth.servizioPagoPa.auth.tsType | null |  'tsType' |
| tsType | intermediarioClientAuth.servizioPagoPa.auth.tsType | 'XXX' |  'tsType' |
| sslType | intermediarioClientAuth.servizioPagoPa.auth.sslType | null |  'sslType' |
| sslType | intermediarioClientAuth.servizioPagoPa.auth.sslType | 'XXX' |  'sslType' |
| ksType | intermediarioClientAuth.servizioPagoPa.auth.ksType | null |  'ksType' |
| ksType | intermediarioClientAuth.servizioPagoPa.auth.ksType | 'XXX' |  'ksType' |
| ksPKeyPasswd | intermediarioClientAuth.servizioPagoPa.auth.ksPKeyPasswd | null |  'ksPKeyPasswd' |
| subscriptionKey | intermediarioClientAuth.servizioPagoPa.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediarioClientAuth.servizioPagoPa.subscriptionKey | loremIpsum |  'subscriptionKey' |

Scenario Outline: <field> non valida

* set <fieldRequest> = <fieldValue>

Given url backofficeBaseurl
And path 'intermediari', idIntermediario
And headers basicAutenticationHeader
And request intermediarioServerAuth
When method put
Then status 400

* match response == { categoria: 'RICHIESTA', codice: 'SINTASSI', descrizione: 'Richiesta non valida', dettaglio: '#notnull' }
* match response.dettaglio contains <fieldResponse>

Examples:
| field | fieldRequest | fieldValue | fieldResponse |
| tipo | intermediarioServerAuth.servizioPagoPa.auth.tipo | null |  'tipo' |
| tipo | intermediarioServerAuth.servizioPagoPa.auth.tipo | 'XXXX' |  'tipo' |
| tsLocation | intermediarioServerAuth.servizioPagoPa.auth.tsLocation | null |  'tsLocation' |
| tsPassword | intermediarioServerAuth.servizioPagoPa.auth.tsPassword | null |  'tsPassword' |
| tsType | intermediarioServerAuth.servizioPagoPa.auth.tsType | null |  'tsType' |
| tsType | intermediarioServerAuth.servizioPagoPa.auth.tsType | 'XXX' |  'tsType' |
| sslType | intermediarioServerAuth.servizioPagoPa.auth.sslType | null |  'sslType' |
| sslType | intermediarioServerAuth.servizioPagoPa.auth.sslType | 'XXX' |  'sslType' |
| subscriptionKey | intermediarioServerAuth.servizioPagoPa.subscriptionKey | '' |  'subscriptionKey' |
| subscriptionKey | intermediarioServerAuth.servizioPagoPa.subscriptionKey | loremIpsum |  'subscriptionKey' |

