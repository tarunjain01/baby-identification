package com.nasscom.buildforindia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.nasscom.buildforindia.integration.uidai.OTPGenerator;

@Service
public class UIDAIVerificationService {

	@Autowired
	@Qualifier("Local")
	OTPGenerator otpGenerator;

	public String sendOTPForUid(String uid) throws Exception {
		return this.otpGenerator.generateOTP(uid);
	}

	public boolean verifyOTP(String key, String otp) {
		return otpGenerator.verifyOTP(key, otp);
	}
}
