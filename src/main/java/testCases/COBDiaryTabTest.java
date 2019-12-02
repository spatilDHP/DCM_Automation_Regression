package testCases;

import java.io.IOException;
import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qtpselenium.util.Keywords;
import com.qtpselenium.util.TestUtil;
import com.qtpselenium.util.Xls_Reader;

public class COBDiaryTabTest {
	Keywords k=Keywords.getinstance();
	Xls_Reader xls=new Xls_Reader(System.getProperty("user.dir")+"//src//main//resources//TPL_SmokeSuite.xlsx");  
	
	@Test(dataProvider="getdata")
	public void doLogin(Hashtable<String,String> data)
	{
		
		if(!TestUtil.getrunmode("COBDiaryTabTest", xls))
			throw new SkipException("Skipping the test as runmode is set to no");	
			
		if(data.get("Runmode").equals("N"))
		throw new SkipException("Skipping the test as flag set to no");
		try {
			k.executekeywords("COBDiaryTabTest", xls, data);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
	
	
	
	@DataProvider
	public Object[][] getdata()
	{
		return TestUtil.getdata("COBDiaryTabTest", xls);
	
	}


	
	
	

}
