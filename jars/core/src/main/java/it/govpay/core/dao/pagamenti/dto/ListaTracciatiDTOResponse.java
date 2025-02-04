package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.Tracciato;
import it.govpay.core.dao.anagrafica.dto.BasicFindResponseDTO;

public class ListaTracciatiDTOResponse extends BasicFindResponseDTO<Tracciato> {

	public ListaTracciatiDTOResponse(Long totalResults, List<Tracciato> results) {
		super(totalResults, results);
	}

}
