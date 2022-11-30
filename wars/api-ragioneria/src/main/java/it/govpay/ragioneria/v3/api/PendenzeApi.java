package it.govpay.ragioneria.v3.api;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.govpay.ragioneria.v3.beans.FaultBean;
import it.govpay.ragioneria.v3.beans.Pendenza;

/**
 * GovPay - API Ragioneria
 *
 * <p>No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 */
@Path("/")
public interface PendenzeApi  {

    /**
     * Allegato di una pendenza
     *
     * Fornisce l&#x27;allegato di una pendenza
     *
     */
    @GET
    @Path("/allegati/{id}")
    @Produces({ "*/*", "application/json" })
    @Operation(summary = "Allegato di una pendenza", tags={ "Pendenze" })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contenuto dell'allegato", content = @Content(mediaType = "*/*", schema = @Schema(implementation = File.class))),
        @ApiResponse(responseCode = "401", description = "Richiesta non autenticata"),
        @ApiResponse(responseCode = "403", description = "Richiesta non autorizzata"),
        @ApiResponse(responseCode = "404", description = "Risorsa inesistente"),
        @ApiResponse(responseCode = "500", description = "Servizio non disponibile", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FaultBean.class))) })
    public Response getAllegatoPendenza(@PathParam("id") Long id);

    /**
     * Dettaglio di una pendenza per riferimento avviso
     *
     * Acquisisce il dettaglio di una pendenza, comprensivo dei dati di pagamento.
     *
     */
    @GET
    @Path("/pendenze/byAvviso/{idDominio}/{numeroAvviso}")
    @Produces({ "application/json" })
    @Operation(summary = "Dettaglio di una pendenza per riferimento avviso", tags={ "Pendenze" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Informazioni dettagliate della pendenza", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pendenza.class))),
        @ApiResponse(responseCode = "401", description = "Richiesta non autenticata"),
        @ApiResponse(responseCode = "403", description = "Richiesta non autorizzata"),
        @ApiResponse(responseCode = "404", description = "Risorsa inesistente"),
        @ApiResponse(responseCode = "500", description = "Servizio non disponibile", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FaultBean.class))) })
    public Response getPendenzaByAvviso(@PathParam("idDominio") String idDominio, @PathParam("numeroAvviso") String numeroAvviso);
}
