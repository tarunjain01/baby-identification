package com.nasscom.buildforindia.integration.uidai;

import java.util.HashMap;
import java.util.Map;

public abstract class OTPStore implements OTPGenerator{
	protected static Map<String, String> otpStore = new HashMap<>();
	
}
