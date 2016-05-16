package com.spiss.spissclient.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputStyleValidate {
	
	public static boolean isUserNameOrPwdFormat(String userNameOrPwd){

		boolean isUserNameOrPwd=false;
		Pattern p=Pattern.compile("^[a-zA-Z0-9_-]+");
		Matcher m=p.matcher(userNameOrPwd);
		boolean b=m.matches();
		if(b && userNameOrPwd.length()>=4 && userNameOrPwd.length()<=16){
			isUserNameOrPwd=true;
		}


		return isUserNameOrPwd;

	}
	
	public static boolean isEmailAddressFormat(String email) {
		boolean isExist = false;
		Pattern pp = Pattern
				.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+$");
		Matcher mm = pp.matcher(email);
		boolean bb = mm.matches();
		if (bb) {
			isExist = true;
		}
		return isExist;

	}

	

}
