package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;

@JsonPropertyOrder({
"descrizione",
})
public class TipoPendenzaPost extends JSONSerializable implements IValidable {
  
  @JsonProperty("descrizione")
  private String descrizione = null;
  
  /**
   **/
  public TipoPendenzaPost descrizione(String descrizione) {
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
    TipoPendenzaPost tipoPendenzaPost = (TipoPendenzaPost) o;
    return Objects.equals(descrizione, tipoPendenzaPost.descrizione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(descrizione);
  }

  public static TipoPendenzaPost parse(String json) throws ServiceException, ValidationException  {
    return (TipoPendenzaPost) parse(json, TipoPendenzaPost.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "tipoPendenzaPost";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TipoPendenzaPost {\n");
    
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
  public void validate() throws ValidationException {
	ValidatorFactory vf = ValidatorFactory.newInstance();
	vf.getValidator("descrizione", this.descrizione).notNull().minLength(1).maxLength(255);
//	vf.getValidator("tipoContabilita", this.tipoContabilita).notNull();
//	vf.getValidator("codiceContabilita", this.codiceContabilita).notNull().minLength(1).maxLength(255);
//	vf.getValidator("codificaIUV", this.codificaIUV).minLength(1).maxLength(4).pattern("(^[0-9]{1,4}$)");
//	vf.getValidator("online", this.online).notNull();
//	vf.getValidator("pagaTerzi", this.pagaTerzi).notNull();
  }
}


