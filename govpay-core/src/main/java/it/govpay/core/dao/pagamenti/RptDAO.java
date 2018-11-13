package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRicevutaDTO.FormatoRicevuta;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.dao.pagamenti.exception.RicevutaNonTrovataException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Versamento.Causale;
import it.govpay.stampe.pdf.rt.RicevutaTelematicaPdf;

public class RptDAO extends BaseDAO{

	public RptDAO() {
	}

	public LeggiRptDTOResponse leggiRpt(LeggiRptDTO leggiRptDTO) throws ServiceException,RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRptDTOResponse response = new LeggiRptDTOResponse();

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiRptDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, leggiRptDTO.getIdDominio(), null, bd);

			RptBD rptBD = new RptBD(bd);
			Rpt	rpt = rptBD.getRpt(leggiRptDTO.getIdDominio(), leggiRptDTO.getIuv(), leggiRptDTO.getCcp());

			response.setRpt(rpt);
			response.setVersamento(rpt.getVersamento(bd));
			response.setApplicazione(rpt.getVersamento(bd).getApplicazione(bd)); 
			response.setDominio(rpt.getVersamento(bd).getDominio(bd));
			response.setUnitaOperativa(rpt.getVersamento(bd).getUo(bd));
			List<SingoloVersamento> singoliVersamenti = rpt.getVersamento(bd).getSingoliVersamenti(bd);
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				singoloVersamento.getCodContabilita(bd);
				singoloVersamento.getIbanAccredito(bd);
				singoloVersamento.getTipoContabilita(bd);
				singoloVersamento.getTributo(bd);
			}
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public LeggiRicevutaDTOResponse leggiRt(LeggiRicevutaDTO leggiRicevutaDTO) throws ServiceException,RicevutaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiRicevutaDTOResponse response = new LeggiRicevutaDTOResponse();

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiRicevutaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, leggiRicevutaDTO.getIdDominio(), null, bd);

			RptBD rptBD = new RptBD(bd);
			Rpt rpt = rptBD.getRpt(leggiRicevutaDTO.getIdDominio(), leggiRicevutaDTO.getIuv(), leggiRicevutaDTO.getCcp());

			if(rpt.getXmlRt() == null)
				throw new RicevutaNonTrovataException(null);
			
			if(leggiRicevutaDTO.getFormato().equals(FormatoRicevuta.PDF)) {
				it.govpay.core.business.RicevutaTelematica avvisoBD = new it.govpay.core.business.RicevutaTelematica(bd);
				response = avvisoBD.creaPdfRicevuta(leggiRicevutaDTO,rpt);
			}
			
			response.setRpt(rpt);
		} catch (NotFoundException e) {
			throw new RicevutaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}

	public ListaRptDTOResponse listaRpt(ListaRptDTO listaRptDTO) throws ServiceException,PagamentoPortaleNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			return this.listaRpt(listaRptDTO, bd);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaRptDTOResponse listaRpt(ListaRptDTO listaRptDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		List<String> listaDominiFiltro;
		this.autorizzaRichiesta(listaRptDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

		// Autorizzazione sui domini
		listaDominiFiltro = AclEngine.getDominiAutorizzati((Utenza) listaRptDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
		if(listaDominiFiltro == null) {
			throw new NotAuthorizedException("L'utenza autenticata ["+listaRptDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
		}

		RptBD rptBD = new RptBD(bd);
		RptFilter filter = rptBD.newFilter();

		filter.setOffset(listaRptDTO.getOffset());
		filter.setLimit(listaRptDTO.getLimit());
		filter.setDataInizio(listaRptDTO.getDataDa());
		filter.setDataFine(listaRptDTO.getDataA());
		filter.setStato(listaRptDTO.getStato());
		filter.setCcp(listaRptDTO.getCcp());
		filter.setIuv(listaRptDTO.getIuv());
		if(listaRptDTO.getIdDominio() != null) {
			listaDominiFiltro.add(listaRptDTO.getIdDominio());
		}

		if(listaDominiFiltro != null && listaDominiFiltro.size() > 0) {
			filter.setIdDomini(listaDominiFiltro);
		}

		filter.setCodPagamentoPortale(listaRptDTO.getIdPagamento());
		filter.setIdPendenza(listaRptDTO.getIdPendenza());
		filter.setCodApplicazione(listaRptDTO.getIdA2A());
		filter.setFilterSortList(listaRptDTO.getFieldSortList());

		long count = rptBD.count(filter);

		List<LeggiRptDTOResponse> resList = new ArrayList<>();
		if(count > 0) {
			List<Rpt> findAll = rptBD.findAll(filter);

			for (Rpt rpt : findAll) {
				LeggiRptDTOResponse elem = new LeggiRptDTOResponse();
				elem.setRpt(rpt);
				rpt.getVersamento(bd).getDominio(bd);
				rpt.getVersamento(bd).getUo(bd);
				elem.setVersamento(rpt.getVersamento(bd));
				elem.setApplicazione(rpt.getVersamento(bd).getApplicazione(bd)); 
				resList.add(elem);
			}
		} 

		return new ListaRptDTOResponse(count, resList);
	}
}
