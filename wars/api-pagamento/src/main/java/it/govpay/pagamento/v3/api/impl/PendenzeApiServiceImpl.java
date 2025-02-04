package it.govpay.pagamento.v3.api.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Allegato;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IdUnitaOperativa;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.Costanti;
import it.govpay.core.dao.pagamenti.AllegatiDAO;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiAllegatoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiAllegatoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeSmartOrderDTO;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v2.controller.BaseController;
import it.govpay.pagamento.v3.api.PendenzeApi;
import it.govpay.pagamento.v3.beans.PendenzaArchivio;
import it.govpay.pagamento.v3.beans.PosizioneDebitoria;
import it.govpay.pagamento.v3.beans.StatoPendenza;
import it.govpay.pagamento.v3.beans.converter.PendenzeConverter;


/**
 * GovPay - API Pagamento
 *
 * <p>No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 */
public class PendenzeApiServiceImpl extends BaseApiServiceImpl implements PendenzeApi {

	public static final String DETTAGLIO_PATH_PATTERN = "/allegati/{0}";

	public PendenzeApiServiceImpl() {
		super("pendenze", PendenzeApiServiceImpl.class);
	}

	/**
	 * Elenco delle pendenze
	 *
	 * Fornisce la lista delle pendenze filtrata ed ordinata.
	 *
	 */
	public Response findPendenze(Integer pagina, Integer risultatiPerPagina, String ordinamento, String idDominio, String dataDa, String dataA, String iuv, String idA2A, String idPendenza, String idDebitore, String stato, String idPagamento, String direzione, String divisione, Boolean mostraSpontaneiNonPagati, Boolean metadatiPaginazione, Boolean maxRisultati) {
		this.buildContext();
		Authentication user = this.getUser();
		String transactionId = ContextThreadLocal.get().getTransactionId();
		String methodName = "findPendenze"; 
		try{
			this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreUtils.validaRisultatiPerPagina(vf, Costanti.PARAMETRO_RISULTATI_PER_PAGINA, risultatiPerPagina);

			// Parametri - > DTO Input

			ListaPendenzeDTO listaPendenzeDTO = null;

			// solo l'utente cittadino deve visualizzare l'ordinamento smart
			if(AutorizzazioneUtils.getAuthenticationDetails(user).getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				listaPendenzeDTO = new ListaPendenzeSmartOrderDTO(user);
			} else {
				listaPendenzeDTO = new ListaPendenzeDTO(user);
			}

			listaPendenzeDTO.setLimit(risultatiPerPagina);
			listaPendenzeDTO.setPagina(pagina);
			if(stato != null) {
				StatoPendenza statoPendenza = StatoPendenza.fromValue(stato);
				if(statoPendenza != null) {
					switch(statoPendenza) {
					case ANNULLATA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ANNULLATA); break;
					case ESEGUITA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ESEGUITA); break;
					case ESEGUITA_PARZIALE: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ESEGUITA_PARZIALE); break;
					case NON_ESEGUITA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.NON_ESEGUITA); break;
					case SCADUTA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.SCADUTA); break;
					case ANOMALA: listaPendenzeDTO.setStato(it.govpay.model.StatoPendenza.ANOMALA); break;
					}				
				} else {
					throw new ValidationException("Codifica inesistente per stato. Valore fornito [" + stato
							+ "] valori possibili " + ArrayUtils.toString(StatoPendenza.values()));
				}
			}

			if(idDominio != null)
				listaPendenzeDTO.setIdDominio(idDominio);
			if(idA2A != null)
				listaPendenzeDTO.setIdA2A(idA2A);
			if(idDebitore != null)
				listaPendenzeDTO.setIdDebitore(idDebitore);

			if(idPagamento != null)
				listaPendenzeDTO.setIdPagamento(idPagamento);

			if(ordinamento != null)
				listaPendenzeDTO.setOrderBy(ordinamento);

			if(dataDa!=null) {
				Date dataDaDate = SimpleDateFormatUtils.getDataDaConTimestamp(dataDa, "dataDa");
				listaPendenzeDTO.setDataDa(dataDaDate);
			}


			if(dataA!=null) {
				Date dataADate = SimpleDateFormatUtils.getDataAConTimestamp(dataA, "dataA");
				listaPendenzeDTO.setDataA(dataADate);
			}

			// Autorizzazione sulle UO
			List<IdUnitaOperativa> uoAutorizzate = AuthorizationManager.getUoAutorizzate(user);
			if(uoAutorizzate == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunaUOAutorizzata(user);
			}
			listaPendenzeDTO.setUnitaOperative(uoAutorizzate);

			// autorizzazione sui tipi pendenza
			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(user);
			if(idTipiVersamento == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(user);
			}
			listaPendenzeDTO.setIdTipiVersamento(idTipiVersamento);
			listaPendenzeDTO.setDirezione(direzione);
			listaPendenzeDTO.setDivisione(divisione);
			listaPendenzeDTO.setMostraSpontaneiNonPagati(mostraSpontaneiNonPagati);

			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaPendenzeDTO.getUser());
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				listaPendenzeDTO.setCfCittadino(userDetails.getIdentificativo()); 
			}

			listaPendenzeDTO.setEseguiCount(metadatiPaginazione);
			listaPendenzeDTO.setEseguiCountConLimit(maxRisultati);

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			// CHIAMATA AL DAO

			ListaPendenzeDTOResponse listaPendenzeDTOResponse = null; 
			if(AutorizzazioneUtils.getAuthenticationDetails(user).getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				listaPendenzeDTOResponse = pendenzeDAO.listaPendenzeSmartOrder((ListaPendenzeSmartOrderDTO) listaPendenzeDTO);
			} else {
				listaPendenzeDTOResponse = pendenzeDAO.listaPendenze(listaPendenzeDTO);
			}

			// CONVERT TO JSON DELLA RISPOSTA

			List<it.govpay.pagamento.v3.beans.PendenzaArchivio> results = new ArrayList<>();
			for(LeggiPendenzaDTOResponse ricevutaDTOResponse: listaPendenzeDTOResponse.getResults()) {
				PendenzaArchivio rsModel = PendenzeConverter.toPendenzaArchivioRsModel(ricevutaDTOResponse,user);
				results.add(rsModel);
			}

			PosizioneDebitoria response = new PosizioneDebitoria(this.getServicePath(uriInfo),	listaPendenzeDTOResponse.getTotalResults(), pagina, risultatiPerPagina);
			response.setRisultati(results);

			this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(response),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

	/**
	 * Allegato di una pendenza
	 *
	 * Fornisce l&#x27;allegato di una pendenza
	 *
	 */
	public Response getAllegatoPendenza(Long id) {
		this.buildContext();
		Authentication user = this.getUser();
		String methodName = "getAllegatoPendenza";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			AllegatiDAO allegatiDAO = new AllegatiDAO();

			LeggiAllegatoDTO leggiAllegatoDTO = new LeggiAllegatoDTO(user);
			leggiAllegatoDTO.setId(id);
			leggiAllegatoDTO.setIncludiRawContenuto(false);

			LeggiAllegatoDTOResponse leggiAllegatoDTOResponse = allegatiDAO.leggiAllegato(leggiAllegatoDTO);

			Dominio dominio = leggiAllegatoDTOResponse.getDominio();
			TipoVersamento tipoVersamento = leggiAllegatoDTOResponse.getTipoVersamento();
			UnitaOperativa unitaOperativa = leggiAllegatoDTOResponse.getUnitaOperativa();
			Allegato allegato = leggiAllegatoDTOResponse.getAllegato();

			// controllo che il dominio, uo e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoUOAuthorized(user, dominio.getCodDominio(), unitaOperativa.getCodUo(), tipoVersamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(user, dominio.getCodDominio(), unitaOperativa.getCodUo(), tipoVersamento.getCodTipoVersamento());
			}
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				if(!leggiAllegatoDTOResponse.getVersamento().getAnagraficaDebitore().getCodUnivoco().equals(userDetails.getIdentificativo())) {
					throw AuthorizationManager.toNotAuthorizedException(user, "la pendenza non appartiene al cittadino chiamante.");
				}
			}

			String allegatoFileName = allegato.getNome();
			String mediaType = allegato.getTipo() != null? allegato.getTipo() : MediaType.APPLICATION_OCTET_STREAM;

			StreamingOutput contenutoStream = allegatiDAO.leggiBlobContenuto(allegato.getId());

			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).type(mediaType).entity(contenutoStream).header("content-disposition", "attachment; filename=\""+allegatoFileName+"\""),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

	/**
	 * Dettaglio di una pendenza per identificativo
	 *
	 * Acquisisce il dettaglio di una pendenza, comprensivo dei dati di pagamento.
	 *
	 */
	public Response getPendenza(String idA2A, String idPendenza) {
		this.buildContext();
		Authentication user = this.getUser();
		String methodName = "getPendenza";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));

		try{
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(idPendenza);
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(idA2A);

			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_PAGAMENTI), Arrays.asList(Diritti.LETTURA));

			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdApplicazione("idA2A", idA2A);
			validatoreId.validaIdPendenza("idPendenza", idPendenza);

			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);

			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);

			Dominio dominio = ricevutaDTOResponse.getDominio();
			TipoVersamento tipoVersamento = ricevutaDTOResponse.getTipoVersamento();
			UnitaOperativa unitaOperativa = ricevutaDTOResponse.getUnitaOperativa();

			// controllo che il dominio, uo e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoUOAuthorized(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), unitaOperativa.getCodUo(), tipoVersamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), unitaOperativa.getCodUo(), tipoVersamento.getCodTipoVersamento());
			}
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(leggiPendenzaDTO.getUser());
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				if(!ricevutaDTOResponse.getVersamento().getAnagraficaDebitore().getCodUnivoco().equals(userDetails.getIdentificativo())) {
					throw AuthorizationManager.toNotAuthorizedException(leggiPendenzaDTO.getUser(), "la pendenza non appartiene al cittadino chiamante.");
				}
			}

			PendenzaArchivio pendenza = PendenzeConverter.toPendenzaArchivioRsModel(ricevutaDTOResponse,user);

			this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.logContext(ContextThreadLocal.get());
		}
	}

}

