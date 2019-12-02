package com.qtpselenium.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestResult;
import org.testng.Reporter;

public class ErrorUtil {
	@SuppressWarnings("rawtypes")
	private static Map<ITestResult,List> verificationFailuresMap = new HashMap<ITestResult,List>();
	
	     public static void addVerificationFailure(Throwable e) {
			System.out.println("*************addVerificationFailure******************");
				List<Throwable> verificationFailures = getVerificationFailures();
				verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
				verificationFailures.add(e);
			}
		  
		  @SuppressWarnings("unchecked")
		public static List<Throwable> getVerificationFailures() {
				System.out.println("*************getVerificationFailures******************");
				List<?> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
				return (List<Throwable>) (verificationFailures == null ? new ArrayList<Object>() : verificationFailures);
			}

}
