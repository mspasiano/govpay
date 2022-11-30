package it.govpay.pagamento.v1.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"nome",
"domini",
"entrate",
"acl",
"anagrafica",
"identityData",
})
public class Profilo extends JSONSerializable {

  @JsonProperty("nome")
  private String nome = null;

  @JsonProperty("domini")
  private List<DominioIndex> domini = new ArrayList<>();

  @JsonProperty("entrate")
  private List<TipoEntrata> entrate = new ArrayList<>();

  @JsonProperty("acl")
  private List<AclPost> acl = new ArrayList<>();

  @JsonProperty("anagrafica")
  private Soggetto anagrafica = null;

  @JsonProperty("identityData")
  private Object identityData = null;

  /**
   * Nome dell'utenza
   **/
  public Profilo nome(String nome) {
    this.nome = nome;
    return this;
  }

  @JsonProperty("nome")
  public String getNome() {
    return this.nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * domini su cui e' abilitato ad operare
   **/
  public Profilo domini(List<DominioIndex> domini) {
    this.domini = domini;
    return this;
  }

  @JsonProperty("domini")
  public List<DominioIndex> getDomini() {
    return this.domini;
  }
  public void setDomini(List<DominioIndex> domini) {
    this.domini = domini;
  }

  /**
   * entrate su cui e' abilitato ad operare
   **/
  public Profilo entrate(List<TipoEntrata> entrate) {
    this.entrate = entrate;
    return this;
  }

  @JsonProperty("entrate")
  public List<TipoEntrata> getEntrate() {
    return entrate;
  }
  public void setEntrate(List<TipoEntrata> entrate) {
    this.entrate = entrate;
  }

  /**
   **/
  public Profilo acl(List<AclPost> acl) {
    this.acl = acl;
    return this;
  }

  @JsonProperty("acl")
  public List<AclPost> getAcl() {
    return this.acl;
  }
  public void setAcl(List<AclPost> acl) {
    this.acl = acl;
  }

  /**
   **/
  public Profilo anagrafica(Soggetto anagrafica) {
    this.anagrafica = anagrafica;
    return this;
  }

  @JsonProperty("anagrafica")
  public Soggetto getAnagrafica() {
    return anagrafica;
  }
  public void setAnagrafica(Soggetto anagrafica) {
    this.anagrafica = anagrafica;
  }

  /**
   **/
  public Profilo identityData(Object identityData) {
    this.identityData = identityData;
    return this;
  }

  @JsonProperty("identityData")
  public Object getIdentityData() {
    return identityData;
  }
  public void setIdentityData(Object identityData) {
    this.identityData = identityData;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Profilo profilo = (Profilo) o;
    return Objects.equals(nome, profilo.nome) &&
        Objects.equals(domini, profilo.domini) &&
        Objects.equals(entrate, profilo.entrate) &&
        Objects.equals(acl, profilo.acl) &&
        Objects.equals(anagrafica, profilo.anagrafica) &&
        Objects.equals(identityData, profilo.identityData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, domini, entrate, acl, anagrafica, identityData);
  }

  public static Profilo parse(String json) throws it.govpay.core.exceptions.IOException {
    return parse(json, Profilo.class);
  }

  @Override
  public String getJsonIdFilter() {
    return "profilo";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Profilo {\n");

    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    domini: ").append(toIndentedString(domini)).append("\n");
    sb.append("    entrate: ").append(toIndentedString(entrate)).append("\n");
    sb.append("    acl: ").append(toIndentedString(acl)).append("\n");
    sb.append("    anagrafica: ").append(toIndentedString(anagrafica)).append("\n");
    sb.append("    identityData: ").append(toIndentedString(identityData)).append("\n");
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
}



