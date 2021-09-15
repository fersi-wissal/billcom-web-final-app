package com.billcom.app.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component

public class AppUtils {
	private AppUtils() {

	}
	public static boolean isEmail(String email) {

		String regex = "^[a-z-A-Z-0-9._%+-]+@[a-z]+\\.[a-z]{2,4}$";
		return email.matches(regex);
	}

	
	public static boolean isMobile(String mobile) {
		String regex = "^[0-9]{8}$";
		return mobile.matches(regex);

	}
	
}
