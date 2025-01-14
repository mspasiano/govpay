package it.govpay.backoffice.v1.beans;


import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"idTipoPendenza",
"descrizione",
})
public class TipoPendenzaProfiloIndex extends JSONSerializable implements IValidable {

  @JsonProperty("idTipoPendenza")
  private String idTipoPendenza = null;

  @JsonProperty("descrizione")
  private String descrizione = null;

  /**
   **/
  public TipoPendenzaProfiloIndex idTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
    return this;
  }

  @JsonProperty("idTipoPendenza")
  public String getIdTipoPendenza() {
    return idTipoPendenza;
  }
  public void setIdTipoPendenza(String idTipoPendenza) {
    this.idTipoPendenza = idTipoPendenza;
  }

  /**
   **/
  public TipoPendenzaProfiloIndex descrizione(String descrizione) {
    this.descrizione = descrizione;
    return this;
  }

  @JsonProperty("descrizione")
  public String getDescrizione() {
    return descrizione;
  }
  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TipoPendenzaProfiloIndex tipoPendenzaProfiloIndex = (TipoPendenzaProfiloIndex) o;
    return Objects.equals(idTipoPendenza, tipoPendenzaProfiloIndex.idTipoPendenza) &&
        Objects.equals(descrizione, tipoPendenzaProfiloIndex.descrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idTipoPendenza, descrizione);
  }

  public static TipoPendenzaProfiloIndex parse(String json) throws IOException {
    return parse(json, TipoPendenzaProfiloIndex.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaProfiloIndex";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaProfiloIndex {\n");

    sb.append("    idTipoPendenza: ").append(toIndentedString(idTipoPendenza)).append("\n");
    sb.append("    descrizione: ").append(toIndentedString(descrizione)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  @Override
 	public void validate() throws it.govpay.core.exceptions.ValidationException {
		ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
		validatoreId.validaIdTipoVersamento("idTipoPendenza", this.idTipoPendenza);
  }
}



