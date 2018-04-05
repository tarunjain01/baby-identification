package com.nasscom.buildforindia.integration.uidai;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component("MockUIDAI")
public class TwillioOTPGenerator extends OTPStore implements OTPGenerator {

	public static final String ACCOUNT_SID = "AC14559483e51ac678eb82f9151e42f45c";
	public static final String AUTH_TOKEN = "30fcd67a3ee7b43f1de5af6f28980f9d";

	
	@Override
	public String generateOTP(String uid) throws Exception {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		Random random = new Random(System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb.append(random.nextInt(10));
		}
		Message.creator(new PhoneNumber("+918007013582"), new PhoneNumber("+14243243768"), sb.toString())
				.create();
		String key = UUID.randomUUID().toString().replace("-", "");
		otpStore.put(key, sb.toString());
		return key;
	}


	@Override
	public boolean verifyOTP(String key, String otp) {
		return otp.equals(otpStore.get(key));
	}

}
