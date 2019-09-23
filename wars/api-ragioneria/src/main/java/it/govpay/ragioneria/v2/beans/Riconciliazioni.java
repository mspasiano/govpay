package it.govpay.ragioneria.v2.beans;

import java.net.URI;
import java.util.List;

import it.govpay.core.beans.Lista;

public class Riconciliazioni extends Lista<RiconciliazioneIndex> {
	
	public Riconciliazioni(List<RiconciliazioneIndex> incassi, URI requestUri, long count, long offset, long limit) {
		super(incassi, requestUri, count, offset, limit);
	}
	
}
