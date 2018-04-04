package com.nasscom.buildforindia.integration.uidai;

public interface OTPGenerator {
	String generateOTP(String uid) throws Exception;
	boolean verifyOTP(String key, String otp);
}
