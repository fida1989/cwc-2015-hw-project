package com.mobioapp.hw_project.utils;

public class PasswordChecker {
	public static boolean isValidPassword(String pass1) {
		if (pass1 != null && pass1.length() > 5) {
			return true;
		}
		return false;
	}
}
