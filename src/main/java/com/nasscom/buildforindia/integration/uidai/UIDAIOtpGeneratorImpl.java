package com.nasscom.buildforindia.integration.uidai;

import java.net.URI;

import org.springframework.stereotype.Component;

import com.nasscom.buildforindia.integration.uidai.helper.DigitalSigner;
import com.nasscom.buildforindia.integration.uidai.helper.ErrorCodeDescriptions;
import com.nasscom.buildforindia.integration.uidai.helper.OtpClient;
import com.nasscom.buildforindia.integration.uidai.helper.OtpDataFromDeviceToAUA;
import com.nasscom.buildforindia.integration.uidai.helper.OtpRequestCreator;

import in.gov.uidai.authentication.otp._1.Otp;
import in.gov.uidai.authentication.otp._1.OtpRes;
import in.gov.uidai.authentication.otp._1.OtpResult;

@Component("UIDAI")
public class UIDAIOtpGeneratorImpl extends OTPStore implements OTPGenerator{

	@Override
	public String generateOTP(String uid) throws Exception {
		OtpClient otpClient = new OtpClient(new URI("http://developer.uidai.gov.in/otp/2.5"));
		DigitalSigner ds = new DigitalSigner("/home/satyajit/Downloads/Staging_Signature_PrivateKey.p12", "public".toCharArray(),"public");
		otpClient.setDigitalSignator(ds);
		
		otpClient.setAsaLicenseKey("MG41KIrkk5moCkcO8w-2fc01-P7I5S-6X2-X7luVcDgZyOa2LXs3ELI");
		
		String channel = "01";

		OtpDataFromDeviceToAUA auaData = new OtpDataFromDeviceToAUA("550061676674", null, channel);

		OtpRequestCreator requestCreator = new OtpRequestCreator();
		Otp otp = requestCreator.createOtpRequest("public", "public","MEaMX8fkRa6PqsqK6wGMrEXcXFl_oXHA-YuknI2uf0gKgZ80HaZgG3A", auaData);

		OtpRes res = otpClient.generateOtp(otp).getOtpRes();
		if (res.getRet().equals(OtpResult.N)) {
			System.out.println(("Failed (Reason: " + res.getErr() + " (" + ErrorCodeDescriptions.getDescription(res.getErr()) + "), "
					+ " Code: " + (res.getCode()) + ")"));
			throw new Exception(ErrorCodeDescriptions.getDescription(res.getErr()));
		} else {
			return res.getCode();
		}
	}
	
	@Override
	public boolean verifyOTP(String key, String otp) {
		return otp.equals(otpStore.get(key));
	}
}
