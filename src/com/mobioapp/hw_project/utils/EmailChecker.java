package com.mobioapp.hw_project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailChecker {
	public static boolean isValidEmail(String email1) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email1);
		return matcher.matches();
	}

}
