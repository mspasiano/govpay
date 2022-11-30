-- 04/08/2022 Aggiunta colonna versione alla tabella stazioni
ALTER TABLE stazioni ADD versione VARCHAR2(35 CHAR);
UPDATE stazioni SET versione = 'V1' WHERE versione IS NULL;
ALTER TABLE stazioni MODIFY (versione NOT NULL);


-- 31/08/2022 Correzione vista eventi riconciliazione
DROP VIEW IF EXISTS v_eventi_vers;
DROP VIEW IF EXISTS v_eventi_vers_base;
DROP VIEW IF EXISTS v_eventi_vers_pagamenti;
DROP VIEW IF EXISTS v_eventi_vers_rendicontazioni;
DROP VIEW IF EXISTS v_eventi_vers_riconciliazioni;
DROP VIEW IF EXISTS v_eventi_vers_tracciati;

CREATE VIEW v_eventi_vers_rendicontazioni AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi 
        JOIN rendicontazioni ON rendicontazioni.id_fr = eventi.id_fr
        JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_pagamenti AS (
	SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM versamenti
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
        JOIN pag_port_versamenti ON versamenti.id = pag_port_versamenti.id_versamento
        JOIN pagamenti_portale ON pag_port_versamenti.id_pagamento_portale = pagamenti_portale.id
        JOIN eventi ON eventi.id_sessione = pagamenti_portale.id_sessione
);

CREATE VIEW v_eventi_vers_riconciliazioni AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi
        JOIN pagamenti ON pagamenti.id_incasso = eventi.id_incasso
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_tracciati AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi
        JOIN operazioni ON operazioni.id_tracciato = eventi.id_tracciato
        JOIN versamenti ON operazioni.id_applicazione = versamenti.id_applicazione AND operazioni.cod_versamento_ente = versamenti.cod_versamento_ente
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);


CREATE VIEW v_eventi_vers_base AS (
        SELECT DISTINCT 
               cod_versamento_ente,
               cod_applicazione,
               id
        FROM eventi
        UNION SELECT * FROM v_eventi_vers_pagamenti
        UNION SELECT * FROM v_eventi_vers_rendicontazioni
        UNION SELECT * FROM v_eventi_vers_riconciliazioni
        UNION SELECT * FROM v_eventi_vers_tracciati
        );


CREATE VIEW v_eventi_vers AS (
        SELECT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               v_eventi_vers_base.cod_versamento_ente,
               v_eventi_vers_base.cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.id
               FROM v_eventi_vers_base JOIN eventi ON v_eventi_vers_base.id = eventi.id
         ); 


-- 30/09/2022 indice sulla colonna id_fr della tabella eventi
CREATE INDEX idx_evt_fk_fr ON eventi (id_fr);


-- 30/09/2022 indice sulla colonna cod_flusso della tabella fr
CREATE INDEX idx_fr_cod_flusso ON fr (cod_flusso);


-- 26/10/2022 indice sulla colonna iuv della tabella rendicontazioni
CREATE INDEX idx_rnd_iuv ON rendicontazioni (iuv);


-- 26/10/2022 indice sulla colonna data_msg_richiesta della tabella rpt
CREATE INDEX idx_rpt_data_msg_richiesta ON rpt (data_msg_richiesta);


-- 15/11/2022 nuove colonne cluster_id e transaction_id nella tabella eventi
DROP VIEW IF EXISTS v_eventi_vers;
DROP VIEW IF EXISTS v_eventi_vers_base;
DROP VIEW IF EXISTS v_eventi_vers_pagamenti;
DROP VIEW IF EXISTS v_eventi_vers_rendicontazioni;
DROP VIEW IF EXISTS v_eventi_vers_riconciliazioni;
DROP VIEW IF EXISTS v_eventi_vers_tracciati;

ALTER TABLE eventi ADD cluster_id VARCHAR2(255 CHAR);
ALTER TABLE eventi ADD transaction_id VARCHAR2(255 CHAR);

CREATE VIEW v_eventi_vers_rendicontazioni AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi 
        JOIN rendicontazioni ON rendicontazioni.id_fr = eventi.id_fr
        JOIN singoli_versamenti ON rendicontazioni.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_pagamenti AS (
	SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM versamenti
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
        JOIN pag_port_versamenti ON versamenti.id = pag_port_versamenti.id_versamento
        JOIN pagamenti_portale ON pag_port_versamenti.id_pagamento_portale = pagamenti_portale.id
        JOIN eventi ON eventi.id_sessione = pagamenti_portale.id_sessione
);

CREATE VIEW v_eventi_vers_riconciliazioni AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi
        JOIN pagamenti ON pagamenti.id_incasso = eventi.id_incasso
        JOIN singoli_versamenti ON pagamenti.id_singolo_versamento=singoli_versamenti.id
        JOIN versamenti ON singoli_versamenti.id_versamento=versamenti.id
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);

CREATE VIEW v_eventi_vers_tracciati AS (
        SELECT DISTINCT 
               versamenti.cod_versamento_ente as cod_versamento_ente,
               applicazioni.cod_applicazione as cod_applicazione,
               eventi.id
        FROM eventi
        JOIN operazioni ON operazioni.id_tracciato = eventi.id_tracciato
        JOIN versamenti ON operazioni.id_applicazione = versamenti.id_applicazione AND operazioni.cod_versamento_ente = versamenti.cod_versamento_ente
        JOIN applicazioni ON versamenti.id_applicazione = applicazioni.id
);


CREATE VIEW v_eventi_vers_base AS (
        SELECT DISTINCT 
               cod_versamento_ente,
               cod_applicazione,
               id
        FROM eventi
        UNION SELECT * FROM v_eventi_vers_pagamenti
        UNION SELECT * FROM v_eventi_vers_rendicontazioni
        UNION SELECT * FROM v_eventi_vers_riconciliazioni
        UNION SELECT * FROM v_eventi_vers_tracciati
        );


CREATE VIEW v_eventi_vers AS (
        SELECT eventi.componente,
               eventi.ruolo,
               eventi.categoria_evento,
               eventi.tipo_evento,
               eventi.sottotipo_evento,
               eventi.data,
               eventi.intervallo,
               eventi.esito,
               eventi.sottotipo_esito,
               eventi.dettaglio_esito,
               eventi.parametri_richiesta,
               eventi.parametri_risposta,
               eventi.dati_pago_pa,
               v_eventi_vers_base.cod_versamento_ente,
               v_eventi_vers_base.cod_applicazione,
               eventi.iuv,
               eventi.cod_dominio,
               eventi.ccp,
               eventi.id_sessione,
	       eventi.severita,
               eventi.cluster_id,
               eventi.transaction_id,
               eventi.id
               FROM v_eventi_vers_base JOIN eventi ON v_eventi_vers_base.id = eventi.id
         ); 

