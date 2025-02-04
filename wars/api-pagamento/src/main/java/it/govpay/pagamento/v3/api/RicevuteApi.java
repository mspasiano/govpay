package it.govpay.pagamento.v3.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.govpay.pagamento.v3.beans.FaultBean;
import it.govpay.pagamento.v3.beans.Ricevuta;
import it.govpay.pagamento.v3.beans.Ricevute;

/**
 * GovPay - API Pagamento
 *
 * <p>No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 */
@Path("/")
public interface RicevuteApi  {

    /**
     * Ricerca delle ricevute di pagamento per identificativo transazione
     *
     */
    @GET
    @Path("/ricevute/{idDominio}/{iuv}")
    @Produces({ "application/json" })
    @Operation(summary = "Ricerca delle ricevute di pagamento per identificativo transazione", tags={ "Ricevute" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Lista delle ricevute di pagamento", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ricevute.class))),
        @ApiResponse(responseCode = "400", description = "Richiesta non correttamente formata", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FaultBean.class))),
        @ApiResponse(responseCode = "401", description = "Richiesta non autenticata"),
        @ApiResponse(responseCode = "403", description = "Richiesta non autorizzata"),
        @ApiResponse(responseCode = "500", description = "Servizio non disponibile", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FaultBean.class))) })
    public Response findRicevute(@PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @QueryParam("esito") String esito);

    /**
     * Acquisizione di una ricevuta di avvenuto pagamento pagoPA
     *
     * Ricevuta pagoPA, sia questa veicolata nella forma di &#x60;RT&#x60; o di &#x60;recepit&#x60;, di esito positivo. 
     *
     */
    @GET
    @Path("/ricevute/{idDominio}/{iuv}/{idRicevuta}")
    @Produces({ "application/json", "application/pdf" })
    @Operation(summary = "Acquisizione di una ricevuta di avvenuto pagamento pagoPA", tags={ "Ricevute" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "201", description = "ricevuta di pagamento acquisita", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ricevuta.class))),
        @ApiResponse(responseCode = "400", description = "Richiesta non correttamente formata", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FaultBean.class))),
        @ApiResponse(responseCode = "401", description = "Richiesta non autenticata"),
        @ApiResponse(responseCode = "403", description = "Richiesta non autorizzata"),
        @ApiResponse(responseCode = "500", description = "Servizio non disponibile", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FaultBean.class))) })
    public Response getRicevuta(@PathParam("idDominio") String idDominio, @PathParam("iuv") String iuv, @PathParam("idRicevuta") String idRicevuta);
}
