package it.govpay.core.beans.checkout;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;

@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"returnOkUrl",
"returnCancelUrl",
"returnErrorUrl",
})
public class ReturnUrls extends JSONSerializable{

	@JsonProperty("returnOkUrl")
	private String returnOkUrl;

	@JsonProperty("returnCancelUrl")
	private String returnCancelUrl;

	@JsonProperty("returnErrorUrl")
	private String returnErrorUrl;

	public ReturnUrls returnOkUrl(String returnOkUrl) {
		this.returnOkUrl = returnOkUrl;
		return this;
	}

	@JsonProperty("returnOkUrl")
	public String getReturnOkUrl() {
		return returnOkUrl;
	}

	public void setReturnOkUrl(String returnOkUrl) {
		this.returnOkUrl = returnOkUrl;
	}

	public ReturnUrls returnCancelUrl(String returnCancelUrl) {
		this.returnCancelUrl = returnCancelUrl;
		return this;
	}

	@JsonProperty("returnCancelUrl")
	public String getReturnCancelUrl() {
		return returnCancelUrl;
	}

	public void setReturnCancelUrl(String returnCancelUrl) {
		this.returnCancelUrl = returnCancelUrl;
	}

	public ReturnUrls returnErrorUrl(String returnErrorUrl) {
		this.returnErrorUrl = returnErrorUrl;
		return this;
	}

	@JsonProperty("returnErrorUrl")
	public String getReturnErrorUrl() {
		return returnErrorUrl;
	}

	public void setReturnErrorUrl(String returnErrorUrl) {
		this.returnErrorUrl = returnErrorUrl;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ReturnUrls returnUrls = (ReturnUrls) o;
		return Objects.equals(returnOkUrl, returnUrls.returnOkUrl) &&
				Objects.equals(returnCancelUrl, returnUrls.returnCancelUrl)&&
				Objects.equals(returnErrorUrl, returnUrls.returnErrorUrl);
	}

	@Override
	public int hashCode() {
		return Objects.hash(returnOkUrl, returnCancelUrl, returnErrorUrl);
	}

	public static ReturnUrls parse(String json) throws IOException {
		return (ReturnUrls) parse(json, ReturnUrls.class);
	}

	@Override
	public String getJsonIdFilter() {
		return "returnUrls";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class ReturnUrls {\n");

		sb.append("    returnOkUrl: ").append(toIndentedString(returnOkUrl)).append("\n");
		sb.append("    returnCancelUrl: ").append(toIndentedString(returnCancelUrl)).append("\n");
		sb.append("    returnErrorUrl: ").append(toIndentedString(returnErrorUrl)).append("\n");
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
