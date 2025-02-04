package it.govpay.ragioneria.v3.api.impl;

import java.text.MessageFormat;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.anagrafica.UtentiDAO;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.ragioneria.v2.controller.BaseController;
import it.govpay.ragioneria.v3.api.UtentiApi;
import it.govpay.ragioneria.v3.beans.Profilo;
import it.govpay.ragioneria.v3.beans.converter.ProfiloConverter;


/**
 * GovPay - API Ragioneria
 *
 * <p>No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 */
public class UtentiApiServiceImpl extends BaseApiServiceImpl  implements UtentiApi {

	public UtentiApiServiceImpl() {
		super("profilo", UtentiApiServiceImpl.class);
	}

	/**
	 * Elenco delle acl associate all&#x27;utenza chiamante
	 *
	 */
	@Override
	public Response getProfilo() {
		this.buildContext();
		Authentication user = this.getUser();
		String methodName = "getProfilo";
 		String transactionId = ContextThreadLocal.get().getTransactionId();
 		this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_IN_CORSO, methodName));
 		try{
 			UtentiDAO utentiDAO = new UtentiDAO();

 			LeggiProfiloDTOResponse leggiProfilo = utentiDAO.getProfilo(user);

 			Profilo profilo = ProfiloConverter.getProfilo(leggiProfilo);

 			this.log.debug(MessageFormat.format(BaseController.LOG_MSG_ESECUZIONE_METODO_COMPLETATA, methodName));
 			return this.handleResponseOk(Response.status(Status.OK).entity(profilo),transactionId).build();

 		}catch (Exception e) {
 			return this.handleException(uriInfo, httpHeaders, methodName, e, transactionId);
 		} finally {
 			this.logContext(ContextThreadLocal.get());
 		}
	}

}

