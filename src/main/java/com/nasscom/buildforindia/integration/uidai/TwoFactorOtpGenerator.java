package com.nasscom.buildforindia.integration.uidai;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component("TwoFactor")
public class TwoFactorOtpGenerator extends OTPStore implements OTPGenerator {

	@Value("${api.key.twofactor}")
	private String apiKey;
	@Value("${test.mobile.otp}")
	private String phoneNumber;

	@Override
	public String generateOTP(String uid) throws Exception {

		String url = "https://2factor.in/API/V1/{api_key}/SMS/{phoneNumber}/AUTOGEN";

		// URI (URL) parameters
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("api_key", apiKey);
		uriParams.put("phoneNumber", phoneNumber);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

		TwoFactorSMSGENResponse response = new RestTemplate()
				.getForObject(builder.buildAndExpand(uriParams).toUri(), TwoFactorSMSGENResponse.class);
		return response.getDetails();
	}

	@Override
	public boolean verifyOTP(String key, String otp) {

		String url = "https://2factor.in/API/V1/{api_key}/SMS/VERIFY/{session_id}/{otp_input}";

		// URI (URL) parameters
		Map<String, String> uriParams = new HashMap<String, String>();
		uriParams.put("api_key", apiKey);
		uriParams.put("session_id", key);
		uriParams.put("otp_input", otp);

		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

		TwoFactorSMSGENResponse response = new RestTemplate()
				.getForObject(builder.buildAndExpand(uriParams).toUri(), TwoFactorSMSGENResponse.class);

		System.out.println(response);

		return "OTP Matched".equals(response.getDetails());
	}

	private static class TwoFactorSMSGENResponse implements Serializable {

		private static final long serialVersionUID = 1L;
		@JsonProperty("Status")
		private String status;
		@JsonProperty("Details")
		private String details;

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		@Override
		public String toString() {
			return "TwoFactorSMSGENResponse [status=" + status + ", details=" + details + "]";
		}

	}
}
