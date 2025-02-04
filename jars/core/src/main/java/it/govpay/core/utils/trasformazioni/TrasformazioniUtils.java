package it.govpay.core.utils.trasformazioni;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

import org.openspcoop2.utils.date.DateManager;
import org.openspcoop2.utils.resources.TemplateUtils;
import org.openspcoop2.utils.service.context.IContext;
import org.slf4j.Logger;

import freemarker.core.StopException;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateModel;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Versamento;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.trasformazioni.exception.TrasformazioneException;

public class TrasformazioniUtils {

	private static void fillDynamicMap(Logger log, Map<String, Object> dynamicMap, IContext context, MultivaluedMap<String, String> queryParameters,
			MultivaluedMap<String, String> pathParameters, Map<String, String> headers, String json) {
		if(dynamicMap.containsKey(Costanti.MAP_DATE_OBJECT)==false) {
			dynamicMap.put(Costanti.MAP_DATE_OBJECT, DateManager.getDate());
		}

		if(context !=null) {
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			if(dynamicMap.containsKey(Costanti.MAP_CTX_OBJECT)==false) {
				dynamicMap.put(Costanti.MAP_CTX_OBJECT, ctx.getContext());
			}
			
			if(dynamicMap.containsKey(Costanti.MAP_TRANSACTION_ID_OBJECT)==false) {
				String idTransazione = context.getTransactionId();
				dynamicMap.put(Costanti.MAP_TRANSACTION_ID_OBJECT, idTransazione);
			}

			if(ctx !=null && ctx.getEventoCtx()!=null && ctx.getEventoCtx().getUrl() != null) {
				URLRegExpExtractor urle = new URLRegExpExtractor(ctx.getEventoCtx().getUrl(), log);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP, urle);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP.toLowerCase(), urle);
			}
		}

		if(dynamicMap.containsKey(Costanti.MAP_HEADER)==false && headers !=null) {
			dynamicMap.put(Costanti.MAP_HEADER, headers);
		}

		if(dynamicMap.containsKey(Costanti.MAP_QUERY_PARAMETER)==false && queryParameters!=null && !queryParameters.isEmpty()) {
			dynamicMap.put(Costanti.MAP_QUERY_PARAMETER, convertMultiToRegularMap(queryParameters));
		}

		if(dynamicMap.containsKey(Costanti.MAP_PATH_PARAMETER)==false && pathParameters!=null && !pathParameters.isEmpty()) {
			dynamicMap.put(Costanti.MAP_PATH_PARAMETER, convertMultiToRegularMap(pathParameters));
		}

		if(json !=null) {
			PatternExtractor pe = new PatternExtractor(json, log);
			dynamicMap.put(Costanti.MAP_ELEMENT_JSON_PATH, pe);
			dynamicMap.put(Costanti.MAP_ELEMENT_JSON_PATH.toLowerCase(), pe);
		}
	}
	
	public static void fillDynamicMap(Logger log, Map<String, Object> dynamicMap, IContext context, MultivaluedMap<String, String> queryParameters,
			MultivaluedMap<String, String> pathParameters, Map<String, String> headers, String json, String idDominio, String idTipoVersamento, String idUnitaOperativa) {
		fillDynamicMap(log, dynamicMap, context, queryParameters, pathParameters, headers, json);
		
		if(dynamicMap.containsKey(Costanti.MAP_ID_TIPO_VERSAMENTO)==false && idTipoVersamento !=null) {
			dynamicMap.put(Costanti.MAP_ID_TIPO_VERSAMENTO, idTipoVersamento);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_ID_UNITA_OPERATIVA)==false && idUnitaOperativa !=null) {
			dynamicMap.put(Costanti.MAP_ID_UNITA_OPERATIVA, idUnitaOperativa);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_ID_DOMINIO)==false && idDominio !=null) {
			dynamicMap.put(Costanti.MAP_ID_DOMINIO, idDominio);
		}
		
	}

	public static void convertFreeMarkerTemplate(String name, byte[] template, Map<String,Object> dynamicMap, OutputStream out) throws TrasformazioneException, UnprocessableEntityException {
		try {			
			OutputStreamWriter oow = new OutputStreamWriter(out);
			
			//Inserisco la mappa per i dati di ritorno
			Map<String,Object> responseMap = new HashMap<String, Object>();
			dynamicMap.put(Costanti.MAP_RESPONSE, responseMap);
			_convertFreeMarkerTemplate(name, template, dynamicMap, oow);
			oow.flush();
			oow.close();
		} catch(IOException e) {
			throw new TrasformazioneException(e.getMessage(),e);
		}
	}
	public static void convertFreeMarkerTemplate(String name, byte[] template, Map<String,Object> dynamicMap, Writer writer) throws TrasformazioneException, UnprocessableEntityException {
		_convertFreeMarkerTemplate(name, template, dynamicMap, writer);
	}
	private static void _convertFreeMarkerTemplate(String name, byte[] template, Map<String,Object> dynamicMap, Writer writer) throws TrasformazioneException, UnprocessableEntityException {
		try {
			// ** Aggiungo utility per usare metodi statici ed istanziare oggetti

			// statici
			BeansWrapper wrapper = new BeansWrapper(Configuration.VERSION_2_3_23);
			TemplateModel classModel = wrapper.getStaticModels();
			dynamicMap.put(Costanti.MAP_CLASS_LOAD_STATIC, classModel);

			// newObject
			dynamicMap.put(Costanti.MAP_CLASS_NEW_INSTANCE, new freemarker.template.utility.ObjectConstructor());

			// Configurazione
            freemarker.template.Configuration config = TemplateUtils.newTemplateEngine();
            config.setAPIBuiltinEnabled(true); // serve per modificare le mappe in freemarker
            
			// ** costruisco template
			Template templateFTL = TemplateUtils.buildTemplate(config, name, template);
			templateFTL.process(dynamicMap, writer);
			writer.flush();
			
		} catch(StopException e) {
			throw new UnprocessableEntityException(e.getMessageWithoutStackTop());
		} catch(Exception e) {
			throw new TrasformazioneException(e.getMessage(),e);
		}
	}

	private static Map<String, String> convertMultiToRegularMap(MultivaluedMap<String, String> m) {
		Map<String, String> map = new HashMap<String, String>();
		if (m == null) {
			return map;
		}
		for (Entry<String, List<String>> entry : m.entrySet()) {
			if(entry.getValue() != null) {
				StringBuilder sb = new StringBuilder();
				for (String s : entry.getValue()) {
					if (sb.length() > 0) {
						sb.append(',');
					}
					sb.append(s);
				}
				map.put(entry.getKey(), sb.toString());
			}
		}
		return map;
	}
	
	public static void fillDynamicMapPromemoriaAvviso(Logger log, Map<String, Object> dynamicMap, IContext context, Versamento versamento, Dominio dominio) {
		if(dynamicMap.containsKey(Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA)==false) {
			dynamicMap.put(Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA, Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA_DEFAULT_VALUE);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_DATE_OBJECT)==false) {
			dynamicMap.put(Costanti.MAP_DATE_OBJECT, DateManager.getDate());
		}

		if(context !=null) {
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			if(dynamicMap.containsKey(Costanti.MAP_CTX_OBJECT)==false) {
				dynamicMap.put(Costanti.MAP_CTX_OBJECT, ctx.getContext());
			}
			
			if(dynamicMap.containsKey(Costanti.MAP_TRANSACTION_ID_OBJECT)==false) {
				String idTransazione = context.getTransactionId();
				dynamicMap.put(Costanti.MAP_TRANSACTION_ID_OBJECT, idTransazione);
			}

			if(ctx !=null && ctx.getEventoCtx()!=null && ctx.getEventoCtx().getUrl() != null) {
				URLRegExpExtractor urle = new URLRegExpExtractor(ctx.getEventoCtx().getUrl(), log);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP, urle);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP.toLowerCase(), urle);
			}
		}

		if(dynamicMap.containsKey(Costanti.MAP_VERSAMENTO)==false && versamento !=null) {
			dynamicMap.put(Costanti.MAP_VERSAMENTO, versamento);
		}

		if(dynamicMap.containsKey(Costanti.MAP_DOMINIO)==false && dominio !=null) {
			dynamicMap.put(Costanti.MAP_DOMINIO, dominio);
		}
	}
	
	public static void fillDynamicMapPromemoriaRicevuta(Logger log, Map<String, Object> dynamicMap, IContext context, it.govpay.bd.model.Rpt rpt, Versamento versamento, Dominio dominio) {
		if(dynamicMap.containsKey(Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA)==false) {
			dynamicMap.put(Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA, Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA_DEFAULT_VALUE);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_DATE_OBJECT)==false) {
			dynamicMap.put(Costanti.MAP_DATE_OBJECT, DateManager.getDate());
		}

		if(context !=null) {
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			if(dynamicMap.containsKey(Costanti.MAP_CTX_OBJECT)==false) {
				dynamicMap.put(Costanti.MAP_CTX_OBJECT, ctx.getContext());
			}
			
			if(dynamicMap.containsKey(Costanti.MAP_TRANSACTION_ID_OBJECT)==false) {
				String idTransazione = context.getTransactionId();
				dynamicMap.put(Costanti.MAP_TRANSACTION_ID_OBJECT, idTransazione);
			}

			if(ctx !=null && ctx.getEventoCtx()!=null && ctx.getEventoCtx().getUrl() != null) {
				URLRegExpExtractor urle = new URLRegExpExtractor(ctx.getEventoCtx().getUrl(), log);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP, urle);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP.toLowerCase(), urle);
			}
		}

		if(dynamicMap.containsKey(Costanti.MAP_VERSAMENTO)==false && versamento !=null) {
			dynamicMap.put(Costanti.MAP_VERSAMENTO, versamento);
		}

		if(dynamicMap.containsKey(Costanti.MAP_DOMINIO)==false && dominio !=null) {
			dynamicMap.put(Costanti.MAP_DOMINIO, dominio);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_RPT)==false && rpt !=null) {
			dynamicMap.put(Costanti.MAP_RPT, rpt);
		}
	}
	
	public static void fillDynamicMapPromemoriaScadenza(Logger log, Map<String, Object> dynamicMap, IContext context, Versamento versamento, Dominio dominio) {
		if(dynamicMap.containsKey(Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA)==false) {
			dynamicMap.put(Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA, Costanti.MAP_CONTENT_TYPE_MESSAGGIO_PROMEMORIA_DEFAULT_VALUE);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_DATE_OBJECT)==false) {
			dynamicMap.put(Costanti.MAP_DATE_OBJECT, DateManager.getDate());
		}

		if(context !=null) {
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			if(dynamicMap.containsKey(Costanti.MAP_CTX_OBJECT)==false) {
				dynamicMap.put(Costanti.MAP_CTX_OBJECT, ctx.getContext());
			}
			
			if(dynamicMap.containsKey(Costanti.MAP_TRANSACTION_ID_OBJECT)==false) {
				String idTransazione = context.getTransactionId();
				dynamicMap.put(Costanti.MAP_TRANSACTION_ID_OBJECT, idTransazione);
			}

			if(ctx !=null && ctx.getEventoCtx()!=null && ctx.getEventoCtx().getUrl() != null) {
				URLRegExpExtractor urle = new URLRegExpExtractor(ctx.getEventoCtx().getUrl(), log);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP, urle);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP.toLowerCase(), urle);
			}
		}

		if(dynamicMap.containsKey(Costanti.MAP_VERSAMENTO)==false && versamento !=null) {
			dynamicMap.put(Costanti.MAP_VERSAMENTO, versamento);
		}

		if(dynamicMap.containsKey(Costanti.MAP_DOMINIO)==false && dominio !=null) {
			dynamicMap.put(Costanti.MAP_DOMINIO, dominio);
		}
	}
	
	public static void fillDynamicMapRichiestaTracciatoCSV(Logger log, Map<String, Object> dynamicMap, IContext context, String linea, String codDominio, String codTipoVersamento) {
		
		if(dynamicMap.containsKey(Costanti.MAP_DATE_OBJECT)==false) {
			dynamicMap.put(Costanti.MAP_DATE_OBJECT, DateManager.getDate());
		}

		if(context !=null) {
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			if(dynamicMap.containsKey(Costanti.MAP_CTX_OBJECT)==false) {
				dynamicMap.put(Costanti.MAP_CTX_OBJECT, ctx.getContext());
			}
			if(dynamicMap.containsKey(Costanti.MAP_TRANSACTION_ID_OBJECT)==false) {
				String idTransazione = context.getTransactionId();
				dynamicMap.put(Costanti.MAP_TRANSACTION_ID_OBJECT, idTransazione);
			}

			
			if(ctx !=null && ctx.getEventoCtx()!=null && ctx.getEventoCtx().getUrl() != null) {
				URLRegExpExtractor urle = new URLRegExpExtractor(ctx.getEventoCtx().getUrl(), log);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP, urle);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP.toLowerCase(), urle);
			}
		}

		if(dynamicMap.containsKey(Costanti.MAP_ID_TIPO_VERSAMENTO)==false && codTipoVersamento !=null) {
			dynamicMap.put(Costanti.MAP_ID_TIPO_VERSAMENTO, codTipoVersamento);
		}

		if(dynamicMap.containsKey(Costanti.MAP_ID_DOMINIO)==false && codDominio !=null) {
			dynamicMap.put(Costanti.MAP_ID_DOMINIO, codDominio);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_LINEA_CSV_RICHIESTA)==false && linea !=null) {
			dynamicMap.put(Costanti.MAP_LINEA_CSV_RICHIESTA, linea);
		}
		
	}
	
	public static void fillDynamicMapRispostaTracciatoCSV(Logger log, Map<String, Object> dynamicMap, IContext context, String headerRisposta, String json,
			String codDominio, String codTipoVersamento, Dominio dominio, Applicazione applicazione, Versamento versamento, Documento documento, 
			String esitoOperazione, String descrizioneEsitoOperazione, String tipoOperazione) {
		
		if(dynamicMap.containsKey(Costanti.MAP_DATE_OBJECT)==false) {
			dynamicMap.put(Costanti.MAP_DATE_OBJECT, DateManager.getDate());
		}

		if(context !=null) {
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			if(dynamicMap.containsKey(Costanti.MAP_CTX_OBJECT)==false) {
				dynamicMap.put(Costanti.MAP_CTX_OBJECT, ctx.getContext());
			}
			if(dynamicMap.containsKey(Costanti.MAP_TRANSACTION_ID_OBJECT)==false) {
				String idTransazione = context.getTransactionId();
				dynamicMap.put(Costanti.MAP_TRANSACTION_ID_OBJECT, idTransazione);
			}

			if(ctx !=null && ctx.getEventoCtx()!=null && ctx.getEventoCtx().getUrl() != null) {
				URLRegExpExtractor urle = new URLRegExpExtractor(ctx.getEventoCtx().getUrl(), log);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP, urle);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP.toLowerCase(), urle);
			}
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_CSV_HEADER_RISPOSTA)==false && headerRisposta !=null) {
			dynamicMap.put(Costanti.MAP_CSV_HEADER_RISPOSTA, headerRisposta);
		}

		if(dynamicMap.containsKey(Costanti.MAP_ID_TIPO_VERSAMENTO)==false && codTipoVersamento !=null) {
			dynamicMap.put(Costanti.MAP_ID_TIPO_VERSAMENTO, codTipoVersamento);
		}

		if(dynamicMap.containsKey(Costanti.MAP_ID_DOMINIO)==false && codDominio !=null) {
			dynamicMap.put(Costanti.MAP_ID_DOMINIO, codDominio);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_VERSAMENTO)==false && versamento !=null) {
			dynamicMap.put(Costanti.MAP_VERSAMENTO, versamento);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_DOCUMENTO)==false && documento !=null) {
			dynamicMap.put(Costanti.MAP_DOCUMENTO, documento);
		}

		if(dynamicMap.containsKey(Costanti.MAP_DOMINIO)==false && dominio !=null) {
			dynamicMap.put(Costanti.MAP_DOMINIO, dominio);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_APPLICAZIONE)==false && applicazione !=null) {
			dynamicMap.put(Costanti.MAP_APPLICAZIONE, applicazione);
		}
		
		if(json !=null) {
			PatternExtractor pe = new PatternExtractor(json, log);
			dynamicMap.put(Costanti.MAP_ELEMENT_JSON_PATH, pe);
			dynamicMap.put(Costanti.MAP_ELEMENT_JSON_PATH.toLowerCase(), pe);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_CSV_ESITO_OPERAZIONE)==false && esitoOperazione !=null) {
			dynamicMap.put(Costanti.MAP_CSV_ESITO_OPERAZIONE, esitoOperazione);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_CSV_DESCRIZIONE_ESITO_OPERAZIONE)==false && descrizioneEsitoOperazione !=null) {
			dynamicMap.put(Costanti.MAP_CSV_DESCRIZIONE_ESITO_OPERAZIONE, descrizioneEsitoOperazione);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_CSV_TIPO_OPERAZIONE)==false && tipoOperazione !=null) {
			dynamicMap.put(Costanti.MAP_CSV_TIPO_OPERAZIONE, tipoOperazione);
		}
	}
	
	public static void fillDynamicMapSubjectMessageAppIO(Logger log, Map<String, Object> dynamicMap, IContext context, Versamento versamento, Rpt rpt, Dominio dominio) {
		
		if(dynamicMap.containsKey(Costanti.MAP_DATE_OBJECT)==false) {
			dynamicMap.put(Costanti.MAP_DATE_OBJECT, DateManager.getDate());
		}

		if(context !=null) {
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			if(dynamicMap.containsKey(Costanti.MAP_CTX_OBJECT)==false) {
				dynamicMap.put(Costanti.MAP_CTX_OBJECT, ctx.getContext());
			}
			
			if(dynamicMap.containsKey(Costanti.MAP_TRANSACTION_ID_OBJECT)==false) {
				String idTransazione = context.getTransactionId();
				dynamicMap.put(Costanti.MAP_TRANSACTION_ID_OBJECT, idTransazione);
			}

			if(ctx !=null && ctx.getEventoCtx()!=null && ctx.getEventoCtx().getUrl() != null) {
				URLRegExpExtractor urle = new URLRegExpExtractor(ctx.getEventoCtx().getUrl(), log);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP, urle);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP.toLowerCase(), urle);
			}
		}

		if(dynamicMap.containsKey(Costanti.MAP_VERSAMENTO)==false && versamento !=null) {
			dynamicMap.put(Costanti.MAP_VERSAMENTO, versamento);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_RPT)==false && rpt !=null) {
			dynamicMap.put(Costanti.MAP_RPT, rpt);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_DOMINIO)==false && dominio !=null) {
			dynamicMap.put(Costanti.MAP_DOMINIO, dominio);
		}
		
	}
	
public static void fillDynamicMapMarkdownMessageAppIO(Logger log, Map<String, Object> dynamicMap, IContext context, Versamento versamento, Rpt rpt, Dominio dominio) {
		
		if(dynamicMap.containsKey(Costanti.MAP_DATE_OBJECT)==false) {
			dynamicMap.put(Costanti.MAP_DATE_OBJECT, DateManager.getDate());
		}

		if(context !=null) {
			GpContext ctx = (GpContext) ((org.openspcoop2.utils.service.context.Context)context).getApplicationContext();
			if(dynamicMap.containsKey(Costanti.MAP_CTX_OBJECT)==false) {
				dynamicMap.put(Costanti.MAP_CTX_OBJECT, ctx.getContext());
			}
			
			if(dynamicMap.containsKey(Costanti.MAP_TRANSACTION_ID_OBJECT)==false) {
				String idTransazione = context.getTransactionId();
				dynamicMap.put(Costanti.MAP_TRANSACTION_ID_OBJECT, idTransazione);
			}

			if(ctx !=null && ctx.getEventoCtx()!=null && ctx.getEventoCtx().getUrl() != null) {
				URLRegExpExtractor urle = new URLRegExpExtractor(ctx.getEventoCtx().getUrl(), log);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP, urle);
				dynamicMap.put(Costanti.MAP_ELEMENT_URL_REGEXP.toLowerCase(), urle);
			}
		}

		if(dynamicMap.containsKey(Costanti.MAP_VERSAMENTO)==false && versamento !=null) {
			dynamicMap.put(Costanti.MAP_VERSAMENTO, versamento);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_RPT)==false && rpt !=null) {
			dynamicMap.put(Costanti.MAP_RPT, rpt);
		}
		
		if(dynamicMap.containsKey(Costanti.MAP_DOMINIO)==false && dominio !=null) {
			dynamicMap.put(Costanti.MAP_DOMINIO, dominio);
		}
		
	}
}
