package com.nasscom.buildforindia.integration.uidai;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component("Local")
public class LocalOtpGenerator extends OTPStore implements OTPGenerator{

	@Override
	public String generateOTP(String uid) throws Exception {
		Random random = new Random(System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb.append(random.nextInt(10));
		}
		String key = UUID.randomUUID().toString().replace("-", "");
		otpStore.put(key, sb.toString());
		System.out.println("=========Key: " + key + "===========");
		System.out.println("=========OTP: " + sb + "===========");
		return key;
	}

	@Override
	public boolean verifyOTP(String key, String otp) {
		return otp.equals(otpStore.get(key));
	}

}
