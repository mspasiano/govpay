.. _govpay_validazione:

Processo di validazione
#######################

Ogni nuova versione di GovPay viene sottoposta alle seguenti verifiche di sicurezza al fine di assicurarne la stabilità e l’assenza di vulnerabilità note:

- Static Code Analysis: identifica possibili vulnerabilità all’interno del codice sorgente utilizzando gli strumenti `SpotBugs <https://spotbugs.github.io/>`_ e `Sonarqube <https://www.sonarsource.com/products/sonarqube/>`_;
- Dynamic Analysis: cerca vulnerabilità del software durante l’effettiva esecuzione del prodotto. L’analisi viene eseguita attraverso l’esecuzione di estese batterie di test realizzate con `Karate <https://github.com/karatelabs/karate>`_;
- Third Party Dependency Analysis: assicura che tutte le librerie terza parte utilizzate non siano soggette a vulnerabilità di sicurezza note, utilizzando il tool `OWASP Dependency-Check </https://owasp.org/www-project-dependency-check/>`_.

Le verifiche sono eseguite automaticamente ad ogni modifica del codice di GovPay sul branch `master` dal sistema di `Continuous Integration Jenkins di GovPay <https://jenkins.link.it/govpay/blue/organizations/jenkins/govpay/activity/>`_.

Falsi positivi
**************

Di seguito le segnalazioni emerse dagli strumenti utilizzati nel processo di validazione che sono stati classificati come Falsi Positivi

CVE-2022-45688
==============

file name: json-20230618.jar

La vulnerabilità indicata viene descritta come segue: `A stack overflow in the XML.toJSONObject component of hutool-json v5.8.10 allows attackers to cause a Denial of Service (DoS) via crafted JSON or XML data.`

La versione della libreria ultilizzata risolve il bug indicato, ma il tool di validazione continua a segnalare la vulnerabilità. Risulta quindi un falso positivo.

CVE-2022-40152
==============

file name: stax2-api-4.2.1.jar

La vulnerabilità 'CVE-2022-40152' è relativa alla dipendenza transitiva 'woodstox-core'. In GovPay non viene utilizzata la versione definita nella dipendenza transitiva, ma bensì la versione woodstox-core-6.4.0.jar che non presenta la vulnerabilità.

CVE-2020-5408
=============

file name: spring-security-crypto-5.8.3.jar

La vulnerabilità indicata viene descritta come segue: `Spring Security versions 5.3.x prior to 5.3.2, 5.2.x prior to 5.2.4, 5.1.x prior to 5.1.10, 5.0.x prior to 5.0.16 and 4.2.x prior to 4.2.16 use a fixed null initialization vector with CBC Mode in the implementation of the queryable text encryptor. A malicious user with access to the data that has been encrypted using such an encryptor may be able to derive the unencrypted values using a dictionary attack.`

La versione utilizzata è superiore alla '5.3.2' quindi risulta un falso positivo ed in GovPay il metodo oggetto della vulnerabilità (Encryptors#queryableText(CharSequence, CharSequence)) non viene utilizzato.

CVE-2016-1000027
================

file name: spring-web-5.3.28.jar

La vulnerabilità indicata viene descritta come segue: `Pivotal Spring Framework through 5.3.16 suffers from a potential remote code execution (RCE) issue if used for Java deserialization of untrusted data. Depending on how the library is implemented within a product, this issue may or not occur, and authentication may be required. NOTE: the vendor's position is that untrusted data is not an intended use case. The product's behavior will not be changed because some users rely on deserialization of trusted data.`

La versione utilizzata è superiore alla '5.3.16' quindi risulta un falso positivo e la classe oggetto della vulnerabilità (https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#remoting-httpinvoker) non viene utilizzata.

CVE-2014-125070
===============

file name: pom.xml

La vulnerabilità indicata viene descritta come segue: `A vulnerability has been found in yanheven console and classified as problematic. Affected by this vulnerability is the function get_zone_hosts/AvailabilityZonesTable of the file openstack_dashboard/dashboards/admin/aggregates/tables.py. The manipulation leads to cross site scripting. The attack can be launched remotely. The name of the patch is ba908ae88d5925f4f6783eb234cc4ea95017472b. It is recommended to apply a patch to fix this issue. The associated identifier of this vulnerability is VDB-217651.`

La libreria indicata non viene utilizzata in GovPay, pertanto si puo' concludere che si tratti di un falso positivo.

Librerire installer
===================

Le seguenti segnalazioni riguardano le librerie utilizzate dall'installer di GovPay, utilizzato offline per la configurazione dell'ear, e che non fanno parte degli artefatti dispiegati. Possono pertanto essere ignorate.

- CVE-2007-1036
- CVE-2012-2312
- CVE-2013-4128
- CVE-2014-3488
- CVE-2014-3599
- CVE-2015-2156
- CVE-2016-4978
- CVE-2018-2799
- CVE-2019-16869
- CVE-2019-19343
- CVE-2019-20444
- CVE-2019-20445
- CVE-2020-1945
- CVE-2020-7238
- CVE-2020-11612
- CVE-2021-4277
- CVE-2021-20318
- CVE-2021-21290
- CVE-2021-21295
- CVE-2021-21409
- CVE-2021-37136
- CVE-2021-37137
- CVE-2021-43797
- CVE-2022-23437
- CVE-2022-24823
- CVE-2022-41881
- CVE-2022-41915 


Test di copertura funzionale
============================

Di seguito le funzionalità del prodotto ed i test che ne verificano il corretto funzionamento

Integrazione AppIO
~~~~~~~~~~~~~~~~~~

* Notifica avviso pagoPA
    * test.api.appio.avviso_pagamento
* Notifica ricevuta
    * test.api.appio.notifica_ricevuta
    * test.api.appio.notifica_ricevuta_pagamento_no_rpt

API Backoffice
~~~~~~~~~~~~~~
    
* Lettura applicazioni
    * test.api.backoffice.v1.applicazioni.get.applicazioni-find-byMetadatiPaginazione
* Modifica applicazioni
    * test.api.backoffice.v1.applicazioni.patch.applicazioni-patch-password
    * test.api.backoffice.v1.applicazioni.put.applicazioni-put-password
    * test.api.backoffice.v1.applicazioni.put.applicazioni-put-semantica
    * test.api.backoffice.v1.applicazioni.put.applicazioni-put-sintassi
    * test.api.backoffice.v1.applicazioni.put.applicazioni-put
* Modifica impostazioni sistema
    * test.api.backoffice.v1.configurazione.patch.configurazione-appIOBatch
    * test.api.backoffice.v1.configurazione.patch.configurazione-avvisaturaAppIO
    * test.api.backoffice.v1.configurazione.patch.configurazione-avvisaturaMail
    * test.api.backoffice.v1.configurazione.patch.configurazione-giornaleEventi
    * test.api.backoffice.v1.configurazione.patch.configurazione-hardening
    * test.api.backoffice.v1.configurazione.patch.configurazione-mailBatch
    * test.api.backoffice.v1.configurazione.patch.configurazione-tracciatoCSV
    * test.api.backoffice.v1.configurazione.post.configurazione-sintassi
* Ricerca e lettura Enti Creditori
    * test.api.backoffice.v1.domini.get.domini-find-byIdDominio
    * test.api.backoffice.v1.domini.get.domini-find-byMetadatiPaginazione
    * test.api.backoffice.v1.domini.get.domini-find-operatore
    * test.api.backoffice.v1.domini.get.dominio-get
* Ricerca e lettura Entrate
    * test.api.backoffice.v1.domini.get.entrate-find-byMetadatiPaginazione
    * test.api.backoffice.v1.domini.get.entrate-get
    * test.api.backoffice.v1.entrate.get.entrate-find-byMetadatiPaginazione      
* Ricerca e lettura Conti di Accredito
    * test.api.backoffice.v1.domini.get.iban-find-byMetadatiPaginazione
    * test.api.backoffice.v1.domini.get.iban-get
* Ricerca e lettura Tipologie di pendenza
    * test.api.backoffice.v1.domini.get.tipipendenza-find-byMetadatiPaginazione
    * test.api.backoffice.v1.domini.get.tipipendenza-get
* Ricerca e lettura Unità Operative
    * test.api.backoffice.v1.domini.get.unita-get
    * test.api.backoffice.v1.domini.get.uo-find-byMetadatiPaginazione
* Configurazione Enti Creditori      
    * test.api.backoffice.v1.domini.put.domini-put-connettoreGovPay
    * test.api.backoffice.v1.domini.put.domini-put-connettoreHyperSicAPKappa
    * test.api.backoffice.v1.domini.put.domini-put-connettoreMaggioliJPPA
    * test.api.backoffice.v1.domini.put.domini-put-connettoreMyPivot
    * test.api.backoffice.v1.domini.put.domini-put-connettoreSecim
    * test.api.backoffice.v1.domini.put.domini-put-intermediato
    * test.api.backoffice.v1.domini.put.domini-put-semantica
    * test.api.backoffice.v1.domini.put.domini-put-sintassi
    * test.api.backoffice.v1.domini.put.dominio-put
* Inserimento e modifica Entrate
    * test.api.backoffice.v1.domini.put.entrate-put-semantica
    * test.api.backoffice.v1.domini.put.entrate-put-sintassi
    * test.api.backoffice.v1.domini.put.entrate-put
    * test.api.backoffice.v1.entrate.put.entrate-put-sintassi
    * test.api.backoffice.v1.entrate.put.entrate-put      
* Inserimento e modifica Conti di Accredito
    * test.api.backoffice.v1.domini.put.iban-put-sintassi
    * test.api.backoffice.v1.domini.put.iban-put
* Inserimento e modifica Tipologie di pendenza
    * test.api.backoffice.v1.domini.put.tipipendenza-put-semantica
    * test.api.backoffice.v1.domini.put.tipipendenza-put-sintassi
    * test.api.backoffice.v1.domini.put.tipipendenza-put
* Inserimento e modifica Unità Operative      
    * test.api.backoffice.v1.domini.put.unita-put-sintassi
    * test.api.backoffice.v1.domini.put.unita-put
* Ricerca e consultazione Eventi del Giornale degli Eventi      
    * test.api.backoffice.v1.eventi.eventi-interfaccia-autorizzazione
    * test.api.backoffice.v1.eventi.get.eventi-find-byMetadatiPaginazione
    * test.api.backoffice.v1.eventi.get.eventi-find-sintassi
    * test.api.backoffice.v1.eventi.pagopa.eventi-interfaccia-nodoInviaCarrelloRPT
    * test.api.backoffice.v1.eventi.pagopa.eventi-interfaccia-nodoInviaRPT
    * test.api.backoffice.v1.eventi.pagopa.eventi-interfaccia-paaAttivaRPT
    * test.api.backoffice.v1.eventi.pagopa.eventi-interfaccia-paaVerificaRPT
    * test.api.backoffice.v1.eventi.pagopa.eventi-interfaccia-pagopa-modello3-nonprecaricato-verifica
    * test.api.backoffice.v1.eventi.eventi-interfaccia-apipendenze-addPendenza
    * test.api.backoffice.v1.eventi.eventi-interfaccia-get-avviso-api-v2
    * test.api.backoffice.v1.eventi.eventi-interfaccia-get-avviso
    * test.api.backoffice.v1.eventi.eventi-interfaccia-apibackoffice-addPendenza
* Ricerca e consultazione dei Flussi di Rendicontazione      
    * test.api.backoffice.v1.flussiRendicontazione.get.flussiRendicontazione-find-byIdFlusso
    * test.api.backoffice.v1.flussiRendicontazione.get.flussiRendicontazione-find-byMetadatiPaginazione
    * test.api.backoffice.v1.flussiRendicontazione.get.flussiRendicontazione-find-sintassi
    * test.api.backoffice.v1.flussiRendicontazione.get.flussiRendicontazione-auth-uo
    * test.api.backoffice.v1.flussiRendicontazione.get.flussiRendicontazione-find-auth-uo
    * test.api.backoffice.v1.flussiRendicontazione.get.flussiRendicontazione-find-byIuv
    * test.api.backoffice.v1.flussiRendicontazione.get.flussiRendicontazione-find
    * test.api.backoffice.v1.flussiRendicontazione.get.flussiRendicontazione-get
    * test.api.backoffice.v1.flussiRendicontazione.get.flussiRendicontazione-getByIdEData
* Ricerca intermediari pagoPA      
    * test.api.backoffice.v1.intermediari.get.intermediari-find-byMetadatiPaginazione
* Configurazione intermediari e stazioni pagoPA      
    * test.api.backoffice.v1.intermediari.put.intermediari-put-sintassi
    * test.api.backoffice.v1.intermediari.put.stazioni-put-semantica
    * test.api.backoffice.v1.intermediari.put.intermediari-put
    * test.api.backoffice.v1.intermediari.put.stazioni-put-sintassi
    * test.api.backoffice.v1.intermediari.put.stazioni-put
* Ricerca notifiche      
    * test.api.backoffice.v1.notifiche.get.notifiche-find-byMetadatiPaginazione
    * test.api.backoffice.v1.notifiche.get.notifiche-find-sintassi
* Ricerca operatori console      
    * test.api.backoffice.v1.operatori.get.operatori-find-byMetadatiPaginazione
* Censimento e modifica operatori      
    * test.api.backoffice.v1.operatori.patch.operatori-patch-password
    * test.api.backoffice.v1.operatori.put.operatori-put-password
    * test.api.backoffice.v1.operatori.put.operatori-put-semantica
    * test.api.backoffice.v1.operatori.put.operatori-put-sintassi
    * test.api.backoffice.v1.operatori.put.operatori-put
* Consultazioni operazioni CSV      
    * test.api.backoffice.v1.operazioni.get.operazioni-get-tracciatiGovpay-rest
* Ricerca riscossioni      
    * test.api.backoffice.v1.pagamenti.get.pagamenti-find-byIdDominio
    * test.api.backoffice.v1.pagamenti.get.pagamenti-find-byMetadatiPaginazione
    * test.api.backoffice.v1.pagamenti.get.pagamenti-find-sintassi
    * test.api.backoffice.v1.pagamenti.get.pagamento-find-applicazioni
    * test.api.backoffice.v1.pagamenti.get.pagamento-find-byStato
    * test.api.backoffice.v1.pagamenti.get.pagamento-get-applicazioni
    * test.api.backoffice.v1.pagamenti.get.pagamento-get-operatori
    * test.api.backoffice.v1.pagamenti.get.pagamento-find-operatori
* Ricerca pendenze      
    * test.api.backoffice.v1.pendenze.get.pendenze-find-applicazioni
    * test.api.backoffice.v1.pendenze.get.pendenze-find-byData
    * test.api.backoffice.v1.pendenze.get.pendenze-find-byIUV
    * test.api.backoffice.v1.pendenze.get.pendenze-find-byMetadatiPaginazione
    * test.api.backoffice.v1.pendenze.get.pendenze-find-byMostraSpontaneiNonPagati
    * test.api.backoffice.v1.pendenze.get.pendenze-find-byStato
    * test.api.backoffice.v1.pendenze.get.pendenze-find-sintassi
    * test.api.backoffice.v1.pendenze.get.pendenze-get-applicazione
    * test.api.backoffice.v1.pendenze.get.pendenze-get-multibeneficiario
    * test.api.backoffice.v1.pendenze.get.pendenze-auth-uo
    * test.api.backoffice.v1.pendenze.get.pendenze-find-operatori
    * test.api.backoffice.v1.pendenze.get.pendenze-get-operatori
* Inserimento e modifica pendenze      
    * test.api.backoffice.v1.pendenze.patch.pendenza-patch-annullamento
    * test.api.backoffice.v1.pendenze.post.pendenza-post-inoltro
    * test.api.backoffice.v1.pendenze.post.pendenza-post-trasformazione-uo
    * test.api.backoffice.v1.pendenze.post.pendenza-post-trasformazione
    * test.api.backoffice.v1.pendenze.put.pendenza-put-aggiornamento-dominio
    * test.api.backoffice.v1.pendenze.put.pendenza-put-aggiornamento-stazione
    * test.api.backoffice.v1.pendenze.put.pendenza-put-aggiornamento
    * test.api.backoffice.v1.pendenze.put.pendenza-put-allegati
    * test.api.backoffice.v1.pendenze.put.pendenza-put-autorizzazione
    * test.api.backoffice.v1.pendenze.put.pendenza-put-campiOpzionali
    * test.api.backoffice.v1.pendenze.put.pendenza-put-contabilita
    * test.api.backoffice.v1.pendenze.put.pendenza-put-datiAllegati
    * test.api.backoffice.v1.pendenze.put.pendenza-put-documento
    * test.api.backoffice.v1.pendenze.put.pendenza-put-iuv-custom
    * test.api.backoffice.v1.pendenze.put.pendenza-put-monovoce
    * test.api.backoffice.v1.pendenze.put.pendenza-put-multibeneficiario
    * test.api.backoffice.v1.pendenze.put.pendenza-put-multivoce
    * test.api.backoffice.v1.pendenze.put.pendenza-put-promemoria
    * test.api.backoffice.v1.pendenze.put.pendenza-put-proprieta
    * test.api.backoffice.v1.pendenze.put.pendenza-put-sintattica
    * test.api.backoffice.v1.pendenze.put.pendenza-put-tipoPendenza
    * test.api.backoffice.v1.pendenze.put.pendenza-put-promemoria-quietanza-pagamento
    * test.api.backoffice.v1.pendenze.put.pendenza-put-semantica
* Lettura profilo utente chiamante      
    * test.api.backoffice.v1.profilo.get.get-profilo
* Modifica password utente chiamante      
    * test.api.backoffice.v1.profilo.patch.profilo-patch-password
* Ricerca promemoria      
    * test.api.backoffice.v1.promemoria.get.promemoria-find-byMetadatiPaginazione
    * test.api.backoffice.v1.promemoria.get.promemoria-find-sintassi
* Lettura dati di quadreatura Rendicontazioni e Riscossioni      
    * test.api.backoffice.v1.quadrature.get.quadrature-rendicontazioni-find-sintassi
    * test.api.backoffice.v1.quadrature.get.quadrature-riscossioni-find-sintassi      
    * test.api.backoffice.v1.rendicontazioni.get.rendicontazioni-find-sintassi
    * test.api.backoffice.v1.reportistiche.get.reportistiche-entrate-previste-find-sintassi
* Ricerca e lettura riconciliazioni      
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazione-applicazione-get
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazione-applicazione-getbyTipoRiscossione
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazione-find
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazioni-find-byIdFlusso
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazioni-find-byIuv
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazioni-find-byMetadatiPaginazione
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazioni-find-bySct
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazioni-find-sintassi
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazione-operatore-get
    * test.api.backoffice.v1.riconciliazioni.get.riconciliazione-operatore-noauth-get
* Registrazione riconciliazioni contabili      
    * test.api.backoffice.v1.riconciliazioni.post.riconciliazione-cumulativa-ricercaFlussiCaseInsensitive
    * test.api.backoffice.v1.riconciliazioni.post.riconciliazione-semantica
    * test.api.backoffice.v1.riconciliazioni.post.riconciliazione-senza-rpt
    * test.api.backoffice.v1.riconciliazioni.post.riconciliazione-sintassi
    * test.api.backoffice.v1.riconciliazioni.post.riconciliazione-autorizzazione
    * test.api.backoffice.v1.riconciliazioni.post.riconciliazione-cumulativa
    * test.api.backoffice.v1.riconciliazioni.post.riconciliazione-singola
* Ricerca e consultazione riscossioni      
    * test.api.backoffice.v1.riscossioni.get.riscossioni-find-byDirezioneDivisione
    * test.api.backoffice.v1.riscossioni.get.riscossioni-find-byIur
    * test.api.backoffice.v1.riscossioni.get.riscossioni-find-byMetadatiPaginazione
    * test.api.backoffice.v1.riscossioni.get.riscossioni-find-byStato
    * test.api.backoffice.v1.riscossioni.get.riscossioni-find-byTipo
    * test.api.backoffice.v1.riscossioni.get.riscossioni-find-sintassi
    * test.api.backoffice.v1.riscossioni.get.riscossioni-find
    * test.api.backoffice.v1.riscossioni.get.riscossioni-get
* Ricerca e consultazione transazioni di pagamento      
    * test.api.backoffice.v1.rpp.get.rpp-find-applicazione
    * test.api.backoffice.v1.rpp.get.rpp-find-byEsito
    * test.api.backoffice.v1.rpp.get.rpp-find-byMetadatiPaginazione
    * test.api.backoffice.v1.rpp.get.rpp-find-sintassi
    * test.api.backoffice.v1.rpp.get.rpp-find
    * test.api.backoffice.v1.rpp.get.rpp_filtri
    * test.api.backoffice.v1.rpp.get.rpp-find-operatore
* Aggiornamento di una ricevuta di pagamento      
    * test.api.backoffice.v1.rpp.patch.rpp-caricamento-rt
* Lettura e configurazione dei ruoli operatore      
    * test.api.backoffice.v1.ruoli.get.ruoli-find-byMetadatiPaginazione
    * test.api.backoffice.v1.ruoli.put.ruoli-put-sintassi
    * test.api.backoffice.v1.ruoli.put.ruoli-put
* Lettura delle Tipologie di Pendenza
    * test.api.backoffice.v1.tipipendenza.get.tipipendenza-find-byMetadatiPaginazione
    * test.api.backoffice.v1.tipipendenza.get.tipipendenza-find-byNonAssociati
    * test.api.backoffice.v1.tipipendenza.get.tipipendenza-find
* Inserimento e modifica delle Tipologie di Pendenza
    * test.api.backoffice.v1.tipipendenza.put.tipipendenza-put-sintassi
    * test.api.backoffice.v1.tipipendenza.put.tipipendenza-put
* Ricerca e lettura dei tracciati CSV di alimentazione APA 
    * test.api.backoffice.v1.tracciati.get.tracciati-find-byMetadatiPaginazione
    * test.api.backoffice.v1.tracciati.get.tracciati-find-byStato
* Caricamento ed elaborazione dei tracciati CSV di alimentazione APA      
    * test.api.backoffice.v1.tracciati.post.tracciati-csv-large
    * test.api.backoffice.v1.tracciati.post.tracciati-csv-post-avvisi-300
    * test.api.backoffice.v1.tracciati.post.tracciati-csv-post-big
    * test.api.backoffice.v1.tracciati.post.tracciati-csv-post-tipopendenza
    * test.api.backoffice.v1.tracciati.post.tracciati-csv-post
    * test.api.backoffice.v1.tracciati.post.tracciati-json-post

API Ente Creditore
~~~~~~~~~~~~~~~~~~

* Test di acquisizione di una pendenza da avviso
    * test.api.ente.v1.avvisi.get.avvisi-get-semantica
    * test.api.ente.v1.avvisi.get.avvisi-get-sintattica
    * test.api.ente.v2.avvisi.get.avvisi-get-semantica
    * test.api.ente.v2.avvisi.get.avvisi-get-sintattica
* Test di notifica di un pagamento
    * test.api.ente.v2.ricevute.put.ricevute-put

API Pagamenti
~~~~~~~~~~~~~

* Ricerca e verifica avvisi di pagamento
    * test.api.pagamento.v1.avvisi.get.avvisi-find-anonimo
    * test.api.pagamento.v1.avvisi.get.verifica-avviso-anonimo
    * test.api.pagamento.v1.avvisi.get.verifica-avviso-basic
    * test.api.pagamento.v1.avvisi.get.verifica-avviso-hardening
    * test.api.pagamento.v1.avvisi.get.verifica-avviso-spid
    * test.api.pagamento.v2.avvisi.get.avvisi-find-anonimo
    * test.api.pagamento.v2.avvisi.get.pagamento-avviso-stazione-modificata
    * test.api.pagamento.v2.avvisi.get.verifica-avviso-hardening
    * test.api.pagamento.v2.avvisi.get.verifica-avviso-anonimo
    * test.api.pagamento.v2.avvisi.get.verifica-avviso-basic
    * test.api.pagamento.v2.avvisi.get.verifica-avviso-spid
      
* Ricerca riscossioni      
    * test.api.pagamento.v1.pagamenti.get.pagamenti-find-sintassi
    * test.api.pagamento.v1.pagamenti.get.pagamento-find-anonimo
    * test.api.pagamento.v1.pagamenti.get.pagamento-find-applicazione
    * test.api.pagamento.v1.pagamenti.get.pagamento-find-byMetadatiPaginazione
    * test.api.pagamento.v1.pagamenti.get.pagamento-find-byStato
    * test.api.pagamento.v1.pagamenti.get.pagamento-get-anonimo
    * test.api.pagamento.v1.pagamenti.get.pagamento-get-applicazione
    * test.api.pagamento.v1.pagamenti.get.pagamento-find-spid
    * test.api.pagamento.v1.pagamenti.get.pagamento-get-spid
    * test.api.pagamento.v2.pagamenti.get.pagamenti-find-sintassi
    * test.api.pagamento.v2.pagamenti.get.pagamento-find-anonimo
    * test.api.pagamento.v2.pagamenti.get.pagamento-find-applicazione
    * test.api.pagamento.v2.pagamenti.get.pagamento-find-byMetadatiPaginazione
    * test.api.pagamento.v2.pagamenti.get.pagamento-find-byStato
    * test.api.pagamento.v2.pagamenti.get.pagamento-get-anonimo
    * test.api.pagamento.v2.pagamenti.get.pagamento-get-applicazione-bollo
    * test.api.pagamento.v2.pagamenti.get.pagamento-get-applicazione
    * test.api.pagamento.v2.pagamenti.get.pagamento-find-spid
    * test.api.pagamento.v2.pagamenti.get.pagamento-get-spid
    * test.api.pagamento.v3.ricevute.get.ricevute-get-anonimo
    * test.api.pagamento.v3.ricevute.get.ricevute-idDominio-iuv-find-byEsito
    * test.api.pagamento.v3.ricevute.get.ricevute-get-applicazione
    * test.api.pagamento.v3.ricevute.get.ricevute-idDominio-iuv-find-anonimo
    * test.api.pagamento.v3.ricevute.get.ricevute-idDominio-iuv-find-applicazione
    * test.api.pagamento.v3.ricevute.get.ricevute-get-spid
    * test.api.pagamento.v3.ricevute.get.ricevute-idDominio-iuv-find-spid      
* Avvio di una transazione di pagamento      
    * test.api.pagamento.v1.pagamenti.post.causale-versamento
    * test.api.pagamento.v1.pagamenti.post.iban-appoggio
    * test.api.pagamento.v1.pagamenti.post.pagamento-avviso-anonimo
    * test.api.pagamento.v1.pagamenti.post.pagamento-avviso-applicazione
    * test.api.pagamento.v1.pagamenti.post.pagamento-descrizioneCausaleRPT
    * test.api.pagamento.v1.pagamenti.post.pagamento-gw
    * test.api.pagamento.v1.pagamenti.post.pagamento-hardening
    * test.api.pagamento.v1.pagamenti.post.pagamento-pendenza-anonimo
    * test.api.pagamento.v1.pagamenti.post.pagamento-pendenza-applicazione
    * test.api.pagamento.v1.pagamenti.post.pagamento-spontaneo-anonimo
    * test.api.pagamento.v1.pagamenti.post.pagamento-spontaneo-applicazione
    * test.api.pagamento.v1.pagamenti.post.pagamento-validazione-semantica
    * test.api.pagamento.v1.pagamenti.post.pagamento-validazione-sintattica
    * test.api.pagamento.v1.pagamenti.post.pagamento-errore-nodo
    * test.api.pagamento.v1.pagamenti.post.pagamento-avviso-spid
    * test.api.pagamento.v1.pagamenti.post.pagamento-pendenza-spid
    * test.api.pagamento.v1.pagamenti.post.pagamento-spontaneo-spid
    * test.api.pagamento.v2.pagamenti.post.iban-appoggio
    * test.api.pagamento.v2.pagamenti.post.pagamento-avviso-anonimo
    * test.api.pagamento.v2.pagamenti.post.pagamento-carrello
    * test.api.pagamento.v2.pagamenti.post.pagamento-descrizioneCausaleRPT
    * test.api.pagamento.v2.pagamenti.post.pagamento-gw
    * test.api.pagamento.v2.pagamenti.post.pagamento-hardening
    * test.api.pagamento.v2.pagamenti.post.pagamento-pendenza-anonimo
    * test.api.pagamento.v2.pagamenti.post.pagamento-pendenza-applicazione
    * test.api.pagamento.v2.pagamenti.post.pagamento-pendenza-modello4-applicazione-inoltro
    * test.api.pagamento.v2.pagamenti.post.pagamento-pendenza-modello4-applicazione-trasformazione
    * test.api.pagamento.v2.pagamenti.post.pagamento-spontaneo-anonimo
    * test.api.pagamento.v2.pagamenti.post.pagamento-spontaneo-applicazione-allegati
    * test.api.pagamento.v2.pagamenti.post.pagamento-spontaneo-applicazione-contabilita-pendenza
    * test.api.pagamento.v2.pagamenti.post.pagamento-spontaneo-applicazione-proprieta-pendenza
    * test.api.pagamento.v2.pagamenti.post.pagamento-spontaneo-applicazione
    * test.api.pagamento.v2.pagamenti.post.pagamento-spontaneo-codiceConvenzione
    * test.api.pagamento.v2.pagamenti.post.pagamento-validazione-semantica
    * test.api.pagamento.v2.pagamenti.post.pagamento-validazione-sintattica
    * test.api.pagamento.v2.pagamenti.post.pagamento-checkout-applicazione
    * test.api.pagamento.v2.pagamenti.post.pagamento-avviso-applicazione
    * test.api.pagamento.v2.pagamenti.post.pagamento-errore-nodo
    * test.api.pagamento.v2.pagamenti.post.pagamento-pendenza-anonimo-sessione
    * test.api.pagamento.v2.pagamenti.post.pagamento-pendenza-spid-sessione
    * test.api.pagamento.v2.pagamenti.post.pagamento-avviso-spid
    * test.api.pagamento.v2.pagamenti.post.pagamento-pendenza-spid
    * test.api.pagamento.v2.pagamenti.post.pagamento-spontaneo-spid
    * test.api.pagamento.v2.pagamenti.post.pagamento-spontaneo-psp
* Ricerca e consultazione di posizioni debitorie      
    * test.api.pagamento.v1.pendenze.get.pendenze-find-anonimo
    * test.api.pagamento.v1.pendenze.get.pendenze-find-applicazioni
    * test.api.pagamento.v1.pendenze.get.pendenze-find-byMetadatiPaginazione
    * test.api.pagamento.v1.pendenze.get.pendenze-find-byStato
    * test.api.pagamento.v1.pendenze.get.pendenze-find-sintassi
    * test.api.pagamento.v1.pendenze.get.pendenze-get-applicazione
    * test.api.pagamento.v2.pendenze.get.pendenze-find-anonimo
    * test.api.pagamento.v2.pendenze.get.pendenze-find-applicazioni-byMostraSpontaneiNonPagati
    * test.api.pagamento.v2.pendenze.get.pendenze-find-applicazioni
    * test.api.pagamento.v2.pendenze.get.pendenze-find-byMetadatiPaginazione
    * test.api.pagamento.v2.pendenze.get.pendenze-find-byStato
    * test.api.pagamento.v2.pendenze.get.pendenze-find-sintassi
    * test.api.pagamento.v2.pendenze.get.pendenze-get-applicazione-datiAllegati
    * test.api.pagamento.v2.pendenze.get.pendenze-get-applicazione
    * test.api.pagamento.v2.pendenze.get.pendenze-getByAvviso-anonimo
    * test.api.pagamento.v2.pendenze.get.pendenze-getByAvviso-applicazione
    * test.api.pagamento.v2.pendenze.get.pendenze-getByAvviso-spid
    * test.api.pagamento.v2.pendenze.get.pendenze-spid
    * test.api.pagamento.v3.pendenze.get.pendenze-find-anonimo
    * test.api.pagamento.v3.pendenze.get.pendenze-find-applicazioni-byMostraSpontaneiNonPagati
    * test.api.pagamento.v3.pendenze.get.pendenze-find-applicazioni
    * test.api.pagamento.v3.pendenze.get.pendenze-find-byMetadatiPaginazione
    * test.api.pagamento.v3.pendenze.get.pendenze-find-byStato
    * test.api.pagamento.v3.pendenze.get.pendenze-find-sintassi
    * test.api.pagamento.v3.pendenze.get.pendenze-get-applicazione-datiAllegati
    * test.api.pagamento.v3.pendenze.get.pendenze-get-applicazione
    * test.api.pagamento.v3.pendenze.get.pendenze-spid      
* Lettura del profilo utente chiamante      
    * test.api.pagamento.v1.profilo.get.get-profilo
    * test.api.pagamento.v2.profilo.get.get-profilo      
    * test.api.pagamento.v3.profilo.get.get-profilo      
* Ricerca e consultazione di transazioni di pagamento
    * test.api.pagamento.v1.rpp.get.transazioni-find-anonimo
    * test.api.pagamento.v1.rpp.get.transazioni-find-applicazione
    * test.api.pagamento.v1.rpp.get.transazioni-find-byEsito
    * test.api.pagamento.v1.rpp.get.transazioni-find-byMetadatiPaginazione
    * test.api.pagamento.v1.rpp.get.transazioni-find-sintassi
    * test.api.pagamento.v1.rpp.get.transazioni-get-anonimo
    * test.api.pagamento.v1.rpp.get.transazioni-get-applicazione
    * test.api.pagamento.v1.rpp.get.transazioni-find-spid
    * test.api.pagamento.v1.rpp.get.transazioni-get-spid
    * test.api.pagamento.v2.rpp.get.transazioni-find-anonimo
    * test.api.pagamento.v2.rpp.get.transazioni-find-applicazione
    * test.api.pagamento.v2.rpp.get.transazioni-find-byData
    * test.api.pagamento.v2.rpp.get.transazioni-find-byEsito
    * test.api.pagamento.v2.rpp.get.transazioni-find-byMetadatiPaginazione
    * test.api.pagamento.v2.rpp.get.transazioni-find-sintassi
    * test.api.pagamento.v2.rpp.get.transazioni-get-anonimo
    * test.api.pagamento.v2.rpp.get.transazioni-get-applicazione
    * test.api.pagamento.v2.rpp.get.transazioni-find-spid
    * test.api.pagamento.v2.rpp.get.transazioni-get-spid      
* Ricerca e consultazione delle tipologie di pendenza per pagamenti spontanei      
    * test.api.pagamento.v2.domini.get.tipipendenza-get
* Creazione di pendenze spontanee
    * test.api.pagamento.v2.pendenze.post.pendenze-add-anonimo
    * test.api.pagamento.v2.pendenze.post.pendenze-add-applicazione
    * test.api.pagamento.v2.pendenze.post.pendenze-add-spid

API Pendenze
~~~~~~~~~~~~

* Ricerca e consultazione pendenze
    * test.api.pendenza.v1.pendenze.get.pendenze-anonimo
    * test.api.pendenza.v1.pendenze.get.pendenze-find-applicazioni
    * test.api.pendenza.v1.pendenze.get.pendenze-find-byMetadatiPaginazione
    * test.api.pendenza.v1.pendenze.get.pendenze-find-byStato
    * test.api.pendenza.v1.pendenze.get.pendenze-find-sintassi
    * test.api.pendenza.v1.pendenze.get.pendenze-get-applicazione
    * test.api.pendenza.v1.pendenze.get.pendenze-spid
    * test.api.pendenza.v2.pendenze.get.pendenze-anonimo
    * test.api.pendenza.v2.pendenze.get.pendenze-find-applicazioni-byMostraSpontaneiNonPagati
    * test.api.pendenza.v2.pendenze.get.pendenze-find-applicazioni
    * test.api.pendenza.v2.pendenze.get.pendenze-find-byMetadatiPaginazione
    * test.api.pendenza.v2.pendenze.get.pendenze-find-byStato
    * test.api.pendenza.v2.pendenze.get.pendenze-find-sintassi
    * test.api.pendenza.v2.pendenze.get.pendenze-get-applicazione
    * test.api.pendenza.v2.pendenze.get.pendenza-get-documenti
    * test.api.pendenza.v2.pendenze.get.pendenza-get-avvisi
    * test.api.pendenza.v2.pendenze.get.pendenze-spid
* Creazione e modifica pendenze      
    * test.api.pendenza.v1.pendenze.patch.pendenza-patch-annullamento
    * test.api.pendenza.v1.pendenze.patch.pendenza-patch-sintassi
    * test.api.pendenza.v1.pendenze.put.pendenza-put-aggiornamento
    * test.api.pendenza.v1.pendenze.put.pendenza-put-autorizzazione
    * test.api.pendenza.v1.pendenze.put.pendenza-put-campiOpzionali
    * test.api.pendenza.v1.pendenze.put.pendenza-put-datiAllegati
    * test.api.pendenza.v1.pendenze.put.pendenza-put-iuv-custom
    * test.api.pendenza.v1.pendenze.put.pendenza-put-monovoce
    * test.api.pendenza.v1.pendenze.put.pendenza-put-multivoce
    * test.api.pendenza.v1.pendenze.put.pendenza-put-semantica
    * test.api.pendenza.v1.pendenze.put.pendenza-put-sintattica
    * test.api.pendenza.v2.pendenze.patch.pendenza-patch-annullamento
    * test.api.pendenza.v2.pendenze.patch.pendenza-patch-sintassi
    * test.api.pendenza.v2.pendenze.put.pendenza-put-aggiornamento
    * test.api.pendenza.v2.pendenze.put.pendenza-put-allegati
    * test.api.pendenza.v2.pendenze.put.pendenza-put-autorizzazione
    * test.api.pendenza.v2.pendenze.put.pendenza-put-campiOpzionali
    * test.api.pendenza.v2.pendenze.put.pendenza-put-datiAllegati
    * test.api.pendenza.v2.pendenze.put.pendenza-put-documento
    * test.api.pendenza.v2.pendenze.put.pendenza-put-iuv-custom
    * test.api.pendenza.v2.pendenze.put.pendenza-put-monovoce
    * test.api.pendenza.v2.pendenze.put.pendenza-put-multivoce
    * test.api.pendenza.v2.pendenze.put.pendenza-put-pagamento-pendenza-scaduta
    * test.api.pendenza.v2.pendenze.put.pendenza-put-proprieta
    * test.api.pendenza.v2.pendenze.put.pendenza-put-sintattica
    * test.api.pendenza.v2.pendenze.put.pendenza-put-stampaAvviso
    * test.api.pendenza.v2.pendenze.put.pendenza-put-tipoPendenza
    * test.api.pendenza.v2.pendenze.put.pendenza-put-pagamento-pendenza-mbt
    * test.api.pendenza.v2.pendenze.put.pendenza-put-contabilita
    * test.api.pendenza.v2.pendenze.put.pendenza-put-semantica
* Lettura profilo utente chiamante      
    * test.api.pendenza.v1.profilo.get.get-profilo
    * test.api.pendenza.v2.profilo.get.get-profilo
* Ricerca e consultazione transazioni di pagamento      
    * test.api.pendenza.v1.rpp.get.transazioni-anonimo
    * test.api.pendenza.v1.rpp.get.transazioni-find-applicazione
    * test.api.pendenza.v1.rpp.get.transazioni-find-byEsito
    * test.api.pendenza.v1.rpp.get.transazioni-find-byMetadatiPaginazione
    * test.api.pendenza.v1.rpp.get.transazioni-find-sintassi
    * test.api.pendenza.v1.rpp.get.transazioni-get-applicazione
    * test.api.pendenza.v1.rpp.get.transazioni-spid
    * test.api.pendenza.v2.rpp.get.transazioni-anonimo
    * test.api.pendenza.v2.rpp.get.transazioni-find-applicazione
    * test.api.pendenza.v2.rpp.get.transazioni-find-byData
    * test.api.pendenza.v2.rpp.get.transazioni-find-byEsito
    * test.api.pendenza.v2.rpp.get.transazioni-find-byMetadatiPaginazione
    * test.api.pendenza.v2.rpp.get.transazioni-find-sintassi
    * test.api.pendenza.v2.rpp.get.transazioni-get-applicazione
    * test.api.pendenza.v2.rpp.get.transazioni-spid
* Consultazione e stampa PDF di un Avviso di Pagamento      
    * test.api.pendenza.v2.avvisi.get.avvisi-get-byNumeroAvviso
      
API Ragioneria
~~~~~~~~~~~~~~

* Ricerca e consultazione dei Flussi di Rendicontazione 
    * test.api.ragioneria.v1.flussiRendicontazione.get.flussiRendicontazione-find-byMetadatiPaginazione
    * test.api.ragioneria.v1.flussiRendicontazione.get.flussiRendicontazione-find-sintassi
    * test.api.ragioneria.v1.flussiRendicontazione.get.flussiRendicontazione-find
    * test.api.ragioneria.v1.flussiRendicontazione.get.flussiRendicontazione-get
    * test.api.ragioneria.v1.flussiRendicontazione.get.flussiRendicontazione-getByIdEData
    * test.api.ragioneria.v2.flussiRendicontazione.get.flussiRendicontazione-find-auth-uo
    * test.api.ragioneria.v2.flussiRendicontazione.get.flussiRendicontazione-find-byIdFlusso
    * test.api.ragioneria.v2.flussiRendicontazione.get.flussiRendicontazione-find-byIuv
    * test.api.ragioneria.v2.flussiRendicontazione.get.flussiRendicontazione-find-byMetadatiPaginazione
    * test.api.ragioneria.v2.flussiRendicontazione.get.flussiRendicontazione-find-sintassi
    * test.api.ragioneria.v2.flussiRendicontazione.get.flussiRendicontazione-find
    * test.api.ragioneria.v2.flussiRendicontazione.get.flussiRendicontazione-get
    * test.api.ragioneria.v2.flussiRendicontazione.get.flussiRendicontazione-getByIdEData
    * test.api.ragioneria.v2.flussiRendicontazione.get.flussiRendicontazione-getByDominioIdEData
    * test.api.ragioneria.v3.flussiRendicontazione.get.flussiRendicontazione-find-byIdFlusso
    * test.api.ragioneria.v3.flussiRendicontazione.get.flussiRendicontazione-find-byIuv
    * test.api.ragioneria.v3.flussiRendicontazione.get.flussiRendicontazione-find-byMetadatiPaginazione
    * test.api.ragioneria.v3.flussiRendicontazione.get.flussiRendicontazione-find-sintassi
    * test.api.ragioneria.v3.flussiRendicontazione.get.flussiRendicontazione-find
    * test.api.ragioneria.v3.flussiRendicontazione.get.flussiRendicontazione-get
    * test.api.ragioneria.v3.flussiRendicontazione.get.flussiRendicontazione-getByDominioIdEData
    * test.api.ragioneria.v3.flussiRendicontazione.get.flussiRendicontazione-getByIdEData      
* Lettura profilo utente chiamante      
    * test.api.ragioneria.v1.profilo.get.get-profilo
    * test.api.ragioneria.v2.profilo.get.get-profilo
* Ricerca e consultazione delle Riconciliazioni
    * test.api.ragioneria.v1.riconciliazioni.get.riconciliazione-find
    * test.api.ragioneria.v1.riconciliazioni.get.riconciliazione-get
    * test.api.ragioneria.v1.riconciliazioni.get.riconciliazioni-find-byMetadatiPaginazione
    * test.api.ragioneria.v1.riconciliazioni.get.riconciliazioni-find-sintassi
    * test.api.ragioneria.v2.riconciliazioni.get.riconciliazione-find
    * test.api.ragioneria.v2.riconciliazioni.get.riconciliazione-get
    * test.api.ragioneria.v2.riconciliazioni.get.riconciliazione-getbyTipoRiscossione
    * test.api.ragioneria.v2.riconciliazioni.get.riconciliazioni-find-byMetadatiPaginazione
    * test.api.ragioneria.v2.riconciliazioni.get.riconciliazioni-find-sintassi
    * test.api.ragioneria.v3.riconciliazioni.get.riconciliazione-find
    * test.api.ragioneria.v3.riconciliazioni.get.riconciliazione-get
    * test.api.ragioneria.v3.riconciliazioni.get.riconciliazioni-find-byMetadatiPaginazione
    * test.api.ragioneria.v3.riconciliazioni.get.riconciliazioni-find-sintassi
* Registrazione delle Riconciliazioni      
    * test.api.ragioneria.v1.riconciliazioni.post.riconciliazione-autorizzazione
    * test.api.ragioneria.v1.riconciliazioni.post.riconciliazione-cumulativa
    * test.api.ragioneria.v1.riconciliazioni.post.riconciliazione-semantica
    * test.api.ragioneria.v1.riconciliazioni.post.riconciliazione-senza-rpt
    * test.api.ragioneria.v1.riconciliazioni.post.riconciliazione-singola
    * test.api.ragioneria.v1.riconciliazioni.post.riconciliazione-sintassi
    * test.api.ragioneria.v2.riconciliazioni.post.riconciliazione-autorizzazione
    * test.api.ragioneria.v2.riconciliazioni.post.riconciliazione-cumulativa-ricercaFlussiCaseInsensitive
    * test.api.ragioneria.v2.riconciliazioni.post.riconciliazione-cumulativa
    * test.api.ragioneria.v2.riconciliazioni.post.riconciliazione-semantica
    * test.api.ragioneria.v2.riconciliazioni.post.riconciliazione-senza-rpt
    * test.api.ragioneria.v2.riconciliazioni.post.riconciliazione-singola
    * test.api.ragioneria.v2.riconciliazioni.post.riconciliazione-sintassi
    * test.api.ragioneria.v3.riconciliazioni.put.riconciliazione-autorizzazione
    * test.api.ragioneria.v3.riconciliazioni.put.riconciliazione-cumulativa
    * test.api.ragioneria.v3.riconciliazioni.put.riconciliazione-semantica
    * test.api.ragioneria.v3.riconciliazioni.put.riconciliazione-senza-rpt
    * test.api.ragioneria.v3.riconciliazioni.put.riconciliazione-singola
    * test.api.ragioneria.v3.riconciliazioni.put.riconciliazione-sintassi      
* Ricerca e consultazione delle riscossioni      
    * test.api.ragioneria.v1.riscossioni.get.riscossioni-find-byMetadatiPaginazione
    * test.api.ragioneria.v1.riscossioni.get.riscossioni-find-byStato
    * test.api.ragioneria.v1.riscossioni.get.riscossioni-find-sintassi
    * test.api.ragioneria.v1.riscossioni.post.riscossioni-find
    * test.api.ragioneria.v1.riscossioni.post.riscossioni-get
    * test.api.ragioneria.v2.riscossioni.get.riscossioni-find-byIur
    * test.api.ragioneria.v2.riscossioni.get.riscossioni-find-byMetadatiPaginazione
    * test.api.ragioneria.v2.riscossioni.get.riscossioni-find-byStato
    * test.api.ragioneria.v2.riscossioni.get.riscossioni-find-byTipo
    * test.api.ragioneria.v2.riscossioni.get.riscossioni-find-sintassi
    * test.api.ragioneria.v2.riscossioni.post.riscossioni-find
    * test.api.ragioneria.v2.riscossioni.post.riscossioni-get
    * test.api.ragioneria.v3.riscossioni.get.riscossioni-find-byIur
    * test.api.ragioneria.v3.riscossioni.get.riscossioni-find-byMetadatiPaginazione
    * test.api.ragioneria.v3.riscossioni.get.riscossioni-find-byStato
    * test.api.ragioneria.v3.riscossioni.get.riscossioni-find-byTipo
    * test.api.ragioneria.v3.riscossioni.get.riscossioni-find-sintassi
    * test.api.ragioneria.v3.riscossioni.get.riscossioni-find
    * test.api.ragioneria.v3.riscossioni.get.riscossioni-get      
    * test.api.ragioneria.v3.ricevute.get.ricevute-find-applicazione
    * test.api.ragioneria.v3.ricevute.get.ricevute-find
    * test.api.ragioneria.v3.ricevute.get.ricevute-get
    
Workflow di pagamento
~~~~~~~~~~~~~~~~~~~~~

* Pagamenti modello 3 (Api pagoPA v1)
    * test.workflow.modello3.v1.modello3-non-autorizzato
    * test.workflow.modello3.v1.modello3-pagamento-annullato
    * test.workflow.modello3.v1.modello3-pagamento-data-validita-decorsa
    * test.workflow.modello3.v1.modello3-pagamento-duplicato
    * test.workflow.modello3.v1.modello3-pagamento-eseguito
    * test.workflow.modello3.v1.modello3-pagamento-importo-errato
    * test.workflow.modello3.v1.modello3-pagamento-non-eseguito
    * test.workflow.modello3.v1.modello3-pagamento-scaduto
    * test.workflow.modello3.v1.modello3-pagamento-sconosciuto
* Pagamenti modello unico (Api pagoPA v2)      
    * test.workflow.modello3.v2.modello3-non-autorizzato
    * test.workflow.modello3.v2.modello3-pagamento-duplicato
    * test.workflow.modello3.v2.modello3-pagamento-eseguito
    * test.workflow.modello3.v2.modello3-pagamento-non-eseguito

