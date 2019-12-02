package com.qtpselenium.util;

import java.util.Hashtable;

public class TestUtil {
	
	public static Object[][] getdata(String testcase,Xls_Reader xls){
		int teststartrownum=1;
		while(!xls.getCellData("Data", 0, teststartrownum).equals(testcase))
		{
			teststartrownum++;
		}
		System.out.println("Test case" + testcase+"starts from row"+ teststartrownum);
		
		int datastartrownum=teststartrownum+2;
		int rows=0;
		while(!xls.getCellData("Data", 0, datastartrownum+rows).equals(""))
		{
			rows++;
		}
		System.out.println("Total no of rows in the test case" + testcase+"are"+ rows);
		
		int colstartrownum=teststartrownum+1;
		int cols=0;
		while(!xls.getCellData("Data", cols, colstartrownum).equals(""))
		{
			cols++;
		}
		System.out.println("Total no of columns in the test case" + testcase+"are"+ cols);
		
		Object[][] testdata=new Object[rows][1];
		int index=0;
		Hashtable<String,String> table=null;
		
		for(int rnum=datastartrownum;rnum<datastartrownum+rows;rnum++){
		table=new Hashtable<String,String>();
			for(int cnum=0;cnum<cols;cnum++){
				String key=xls.getCellData("Data",cnum,colstartrownum);
				String value=xls.getCellData("Data",cnum,rnum);
				System.out.print(value+"--");
				table.put(key,value);
			}
			System.out.println("");
			testdata[index][0]=table;
			index++;
		}		
		System.out.println("**********");
		return testdata;
	}

	   public static boolean getrunmode(String testname, Xls_Reader xls)
	   {
		   for(int rnum=2;rnum<=xls.getRowCount("Test Cases");rnum++){
			   String testcasename=xls.getCellData("Test Cases","TCID", rnum);
		   if(testcasename.equals(testname))
		   {
			   if(xls.getCellData("Test Cases","Runmode" ,rnum ).equals("Y"))
			   {
				   return true;
			   }
			   else
			   {
				   return false;
			   }
		   }
		   }
		   return false;
	   }
}
