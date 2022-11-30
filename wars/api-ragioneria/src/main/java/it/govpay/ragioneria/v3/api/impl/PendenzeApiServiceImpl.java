package it.govpay.ragioneria.v3.api.impl;

import java.text.MessageFormat;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.bd.model.Allegato;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.AllegatiDAO;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiAllegatoDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiAllegatoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.ragioneria.v3.beans.Pendenza;
import it.govpay.ragioneria.v3.beans.PendenzaPagata;
import it.govpay.ragioneria.v3.beans.converter.PendenzeConverter;
import it.govpay.ragioneria.v3.api.PendenzeApi;


/**
 * GovPay - API Ragioneria
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
     * Allegato di una pendenza
     *
     * Fornisce l&#x27;allegato di una pendenza
     *
     */
    @Override
	public Response getAllegatoPendenza(Long id) {
    	this.buildContext();
    	Authentication user = this.getUser();
    	String methodName = "getAllegatoPendenza";
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName)); 

		try{
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));

			AllegatiDAO allegatiDAO = new AllegatiDAO();

			LeggiAllegatoDTO leggiAllegatoDTO = new LeggiAllegatoDTO(user);
			leggiAllegatoDTO.setId(id);
			leggiAllegatoDTO.setIncludiRawContenuto(false);

			LeggiAllegatoDTOResponse leggiAllegatoDTOResponse = allegatiDAO.leggiAllegato(leggiAllegatoDTO);

			Allegato allegato = leggiAllegatoDTOResponse.getAllegato();

			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, leggiAllegatoDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user,leggiAllegatoDTOResponse.getDominio().getCodDominio(), null);
			}

			String allegatoFileName = allegato.getNome();
			String mediaType = allegato.getTipo() != null? allegato.getTipo() : MediaType.APPLICATION_OCTET_STREAM;

			StreamingOutput contenutoStream = allegatiDAO.leggiBlobContenuto(allegato.getId());

			this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).type(mediaType).entity(contenutoStream).header("content-disposition", "attachment; filename=\""+allegatoFileName+"\""),transactionId).build();

		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }
    
    /**
     * Dettaglio di una pendenza per riferimento avviso
     *
     * Acquisisce il dettaglio di una pendenza, comprensivo dei dati di pagamento.
     *
     */
    public Response getPendenzaByAvviso(String idDominio, String numeroAvviso) {
    	this.buildContext();
    	Authentication user = this.getUser();
    	String methodName = "getPendenzaByAvviso";  
		String transactionId = ContextThreadLocal.get().getTransactionId();
		this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));  
		try{
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setCodDominio(idDominio);
			// autorizzazione sulla API
			this.isAuthorized(user, Arrays.asList(TIPO_UTENZA.APPLICAZIONE), Arrays.asList(Servizio.API_RAGIONERIA), Arrays.asList(Diritti.LETTURA));
			
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdDominio("idDominio", idDominio);

			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(user);

			leggiPendenzaDTO.setIdDominio(idDominio);
			leggiPendenzaDTO.setNumeroAvviso(numeroAvviso);
			leggiPendenzaDTO.setVerificaAvviso(true);

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			LeggiPendenzaDTOResponse leggiPendenzaDTOResponse = pendenzeDAO.leggiPendenzaByRiferimentoAvviso(leggiPendenzaDTO);

			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(leggiPendenzaDTOResponse.getVersamento().getCodVersamentoEnte());
			((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(leggiPendenzaDTOResponse.getApplicazione().getCodApplicazione());
			
			// controllo che il dominio sia autorizzato
			if(!AuthorizationManager.isDominioAuthorized(user, leggiPendenzaDTOResponse.getDominio().getCodDominio())) {
				throw AuthorizationManager.toNotAuthorizedException(user,leggiPendenzaDTOResponse.getDominio().getCodDominio(), null);
			}
			
			PendenzaPagata pendenza = PendenzeConverter.toPendenzaPagataRsModel(leggiPendenzaDTOResponse.getVersamento(), leggiPendenzaDTOResponse.getRpts());
			this.log.debug(MessageFormat.format(BaseApiServiceImpl.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName)); 
			return this.handleResponseOk(Response.status(Status.OK).entity(pendenza),transactionId).build();
		}catch (Exception e) {
			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
		} finally {
			this.log(ContextThreadLocal.get());
		}
    }
}

