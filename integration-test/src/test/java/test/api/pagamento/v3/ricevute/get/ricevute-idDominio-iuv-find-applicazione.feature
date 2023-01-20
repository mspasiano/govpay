Feature: Ricerca pagamenti per idDominio/IUV utenza applicazione

Background:

* callonce read('classpath:utils/workflow/modello1/v2/modello1-bunch-pagamenti-v3.feature')

@test1
Scenario Outline: Ricerca pagamenti per idDominio/IUV utenza applicazione

* def pagamentiBaseurl = getGovPayApiBaseUrl({api: 'pagamento', versione: 'v3', autenticazione: 'basic'})
* def risposta = read('msg/<risposta>')

Given url pagamentiBaseurl
And path '/ricevute', <rpt>.dominio.identificativoDominio, <rpt>.datiVersamento.identificativoUnivocoVersamento
And headers idA2ABasicAutenticationHeader
When method get
Then status <httpStatus>
And match response == risposta

Examples:
| rpt | httpStatus | risposta |
| rpt_Anonimo_INCORSO_DOM1_SEGRETERIA | 404 | notFound.json |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA  | 404 | notFound.json |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| rpt_Verdi_ESEGUITO_DOM1_SEGRETERIA_A2A2 | 404 | notFound.json |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA | 404 | notFound.json |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A | 200 | lista_ricevute.json |
| rpt_Verdi_NONESEGUITO_DOM1_SEGRETERIA_A2A2 | 404 | notFound.json |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| rpt_Verdi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 404 | notFound.json |
| rpt_Verdi_NONESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| rpt_Verdi_RIFIUTATO_DOM1_LIBERO | 404 | notFound.json |
| rpt_Verdi_INCORSO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| rpt_Rossi_ESEGUITO_DOM1_SEGRETERIA | 404 | notFound.json |
| rpt_Rossi_NONESEGUITO_DOM1_SEGRETERIA | 404 | notFound.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE | 404 | notFound.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| rpt_Rossi_ESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 404 | notFound.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A | 200 | lista_ricevute.json |
| rpt_Rossi_NONESEGUITO_DOM2_ENTRATASIOPE_A2A2 | 404 | notFound.json |
