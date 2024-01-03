package com.edenhub.gateway.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Luong Nguyen
 *
 * @date 2020-07-15
 */
public class ValidatorUtils {
	
	//matches basic email only
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	//matches basic phonenumber in Viet Nam only
	private static final String PHONE_VN_PATTERN = "(09|01[2|6|8|9])+([0-9]{8})";

	//matches basic phonenumber
	private static final String PHONE_PATTERN = "^[0-9]{10,15}$";
	
	//matches numbers only
	private static final String regexStr1 = "^[0-9]*$";

	//matches 10-digit numbers only
	private static final String regexStr2 = "^[0-9]{10}$";

	//matches numbers and dashes, any order really.
	private static final String regexStr3 = "^[0-9\\-]*$";

	//matches 9999999999, 1-999-999-9999 and 999-999-9999
	private static final String regexStr4 = "^(1\\-)?[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$" ;
 
    /*******************************************************************
     * Validate format of the email
     * 
     * @param email		the email.
     * @return true if the email is valid, or else.
     * 
     * @author: Luong Nguyen (pro7991@gmail.com)
     * @date: Jul 15, 2020
     *******************************************************************/
    public static boolean validateEmail(final String email) {
    	Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    /*******************************************************************
     * Validate format of the phone number
     * 
     * @param phone
     * @return true if the phone is valid, or else.
     * 
     * @author: Luong Nguyen (pro7991@gmail.com)
     * @date: Jul 15, 2020
     *******************************************************************/
    public static boolean validatePhone(final String phone) {
    	Pattern pattern = Pattern.compile(PHONE_PATTERN);
    	Matcher matcher = pattern.matcher(phone);
    	return matcher.matches();
    }
    
	// Comment this code for developing
	public static String generateOtp(boolean fakeOTP) {
		if (fakeOTP) {
			return "111111";
		}
		return String.format("%06d", new Random().nextInt(999999));
	}
	
    /*
     * Function to test.
     * 
    public static void main(String[] args) {
        String email = "nguyendangkhiemit@gmail.com1";
        String phone = "01649369431";
        String phone1 = "01255515151";
        String phone3 = "0125551515132232323";
        String phone4 = "0125551";
        ValidateUtil validator = new ValidateUtil();
        if (validator.validateEmail(email)) {
            System.out.println("Email hợp lệ");
        } else {
            System.out.println("Email không hợp lệ");
        }
        if (validator.validatePhone(phone1)) {
        	System.out.println(phone1 + " hợp lệ");
        } else {
        	System.out.println(phone1 + " không hợp lệ");
        }
        if (validator.validatePhone(phone3)) {
        	System.out.println(phone3 + " hợp lệ");
        } else {
        	System.out.println(phone3 + " không hợp lệ");
        }
        if (validator.validatePhone(phone4)) {
        	System.out.println(phone4 + " hợp lệ");
        } else {
        	System.out.println(phone4 + " không hợp lệ");
        }
    }
    */
}
