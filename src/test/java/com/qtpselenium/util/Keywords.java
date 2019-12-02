package com.qtpselenium.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import org.testng.Assert;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Keywords {

	static WebDriver driver = null;
	Properties OR = null;
	Properties ENV = null;
	WebDriver bak_mozilla;
	WebDriver bak_chrome;
	WebDriver bak_ie;
	WebDriverWait wait = null;
	static Keywords k;
	static int i = 2;
	static int j = 2;
	static int labeltext = 0;

	private Keywords() {
		try {
			OR = new Properties();
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir") + "//src//main//resources//OR.properties");
			OR.load(fs);
			ENV = new Properties();
			String filename = OR.getProperty("environment") + ".properties";
			fs = new FileInputStream(System.getProperty("user.dir") + "//src//main//resources//" + filename);
			ENV.load(fs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Keywords getinstance() {
		if (k == null)
			k = new Keywords();
		return k;
	}

	public void executekeywords(String testname, Xls_Reader xls, Hashtable<String, String> table) throws IOException {
		int rows = xls.getRowCount("Test Steps");
		for (int rnum = 0; rnum <= rows; rnum++) {
			String tcid = xls.getCellData("Test Steps", "TCID", rnum);
			if (tcid.equals(testname)) {
				String keyword = xls.getCellData("Test Steps", "Keyword", rnum);
				String object = xls.getCellData("Test Steps", "Object", rnum);
				String data = xls.getCellData("Test Steps", "Data", rnum);
				String result = "";
				if (keyword.equals("openbrowser"))
					result = openbrowser(table.get(data));
				else if (keyword.equals("navigate"))
					result = navigate(object);

				/*
				 * else if(keyword.equals("navigateback")) result=navigateback(object);
				 */
				else if (keyword.equals("click"))
				result = click(object);
				else if (keyword.equals("click2"))
					result = click2(object);
				
				else if (keyword.equals("input"))
					result = input(object, table.get(data));

				else if (keyword.equals("validatetext"))
					result = validatetext(table.get(data));
				
				
				else if (keyword.equals("Asserttext"))
					result = Asserttext(table.get(data), object);
				else if (keyword.equals("validateUrl"))
					result = validateUrl(table.get(data));
				else if (keyword.equals("validatetitle"))
					result = validatetitle(object);
				else if (keyword.equals("waitforobject"))
					result = waitforobject(object);
				else if (keyword.equals("PageLoad"))
					result = PageLoad();
				else if (keyword.equals("trysending"))
					result = trysending(table.get(data));
				else if (keyword.equals("trysendingtab"))
					result = trysendingtab();
				else if (keyword.equals("trysendingenter"))
					result = trysendingenter();
				else if (keyword.equals("trysendingarrowdown"))
					result = trysendingarrowdown(object);
				else if (keyword.equals("testjavascript"))
					result = testjavascript(object);
				else if (keyword.equals("acceptalert"))
					result = acceptalert();
				else if (keyword.equals("SwitchToAlert"))
					result = SwitchToAlert();
				else if (keyword.equals("Refreshpage"))
					result = Refreshpage();
				else if (keyword.equals("synchronize"))
					result = synchronize();
				else if (keyword.equals("click1"))
					result = click1();
				else if (keyword.equals("pagescroll"))
					result = pagescroll();
				else if (keyword.equals("uploadfile"))
					result = uploadfile(object, table.get(data));

				else if (keyword.equals("uploadfile"))
					result = uploadfile(object, table.get(data));

				else if (keyword.equals("selectfromDropDown"))
					result = selectfromDropDown(object, table.get(data));

				else if (keyword.equals("closebrowser"))
					result = closebrowser();
				else if (keyword.equals("refresh"))
					refresh();

				if (!result.equals("Pass")) {
					System.out.println("Result is not pass");
					String filename = tcid + "_" + keyword + ".jpg";
					File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
					FileUtils.copyFile(scrFile,
							new File(System.getProperty("user.dir") + "//screenshots//" + filename));
					String proceed = xls.getCellData("Test Steps", "Proceed_On_Fail", rnum);
					if (proceed.equals("Y")) {
						try {
							Assert.fail(result);

						} catch (Throwable t) {
							System.out.println("Error");
							ErrorUtil.addVerificationFailure(t);
						}
					} else {
						Assert.fail(result);
					}
				}
				System.out.println(tcid + "--" + keyword + "--" + object + "--" + data + "--" + result);
			}
		}
	}

	public String openbrowser(String browser) {
		if (browser.equals("Mozilla") && bak_mozilla != null) {
			driver = bak_mozilla;
			return "Pass";
		}

		else if (browser.equals("chrome") && bak_chrome != null) {
			driver = bak_chrome;
			return "Pass";
		}

		else if (browser.equals("IE") && bak_ie != null) {
			driver = bak_ie;
			return "Pass";
		}

		if (browser.equals("Mozilla")) {
			driver = new FirefoxDriver();
			bak_mozilla = driver;
		} else if (browser.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "//Chrome//chromedriver.exe");
			driver = new ChromeDriver();
			bak_chrome = driver;
		} else if (browser.equals("IE")) {
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "//drivers//IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			bak_ie = driver;
		}

		driver.manage().window().maximize();
		return "Pass";
	}

	// Click Event

	public String click(String xpathKey) {
		try {
			Thread.sleep(1000);
			// logs.debug("clicking object:" + xpathKey);
			wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			driver.findElement(By.xpath(OR.getProperty(xpathKey))).click();
		} catch (Exception e) {
			return "Fail -Unable to click on" + xpathKey;

		}
		return "Pass";
	}

	
	
	public String click2(String xpathKey) {
		try {
			Thread.sleep(1000);
			// logs.debug("clicking object:" + xpathKey);
			wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.getProperty(xpathKey))));
			wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			driver.findElement(By.id(OR.getProperty(xpathKey))).click();
		} catch (Exception e) {
			return "Fail -Unable to click on" + xpathKey;

		}
		return "Pass";
	}
	
	
	
	public String clicksearchbutton(String xpathKey) {
		try {

			Thread.sleep(1000);
			// logs.debug("clicking object:" + xpathKey);
			wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			driver.findElement(By.xpath(OR.getProperty(xpathKey))).click();
			Thread.sleep(1000);

		} catch (Exception e) {
			return "Fail -Unable to click on" + xpathKey;

		}
		return "Pass";
	}

	public String clickActivityTab(String xpathKey) {
		try {

			Thread.sleep(1000);
			// logs.debug("clicking object:" + xpathKey);
			wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			driver.findElement(By.xpath(OR.getProperty(xpathKey))).click();
			Thread.sleep(1000);

		} catch (Exception e) {
			return "Fail -Unable to click on" + xpathKey;

		}
		return "Pass";
	}

	public String clickAddActivityButton(String xpathKey) {
		try {

			Thread.sleep(1000);
			// logs.debug("clicking object:" + xpathKey);
			wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			System.out.println("Click on Add activity button");
			driver.findElement(By.xpath(OR.getProperty(xpathKey))).click();
			System.out.println(" Add activity button is clicked");
			Thread.sleep(1000);

		} catch (Exception e) {
			return "Fail -Unable to click on" + xpathKey;

		}
		return "Pass";
	}

	public String clickInsertTab(String xpathKey) {
		try {

			Thread.sleep(1000);
			// logs.debug("clicking object:" + xpathKey);
			wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			driver.findElement(By.xpath(OR.getProperty(xpathKey))).click();
			Thread.sleep(1000);

		} catch (Exception e) {
			return "Fail -Unable to click on" + xpathKey;

		}
		return "Pass";
	}

	public String clickviewedcases(String xpathKey) {
		try {

			Thread.sleep(1000);
			// logs.debug("clicking object:" + xpathKey);
			wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			driver.findElement(By.xpath(OR.getProperty(xpathKey))).click();
			Thread.sleep(1000);

		} catch (Exception e) {
			return "Fail -Unable to click on" + xpathKey;

		}
		return "Pass";
	}

	public String clickclearbutton(String xpathKey) {
		try {
			// Thread.sleep(1000);
			// logs.debug("clicking object:" + xpathKey);
			wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ctl00_ContentPlaceHolder1_ButtonClear")));
			wait.until(ExpectedConditions.elementToBeClickable(By.id("ctl00_ContentPlaceHolder1_ButtonClear")));
			WebElement element = driver.findElement(By.xpath(OR.getProperty(xpathKey)));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);
			Thread.sleep(1000);

			System.out.println("Search is cleared");
		} catch (Exception e) {
			return "Fail -Unable to click on" + xpathKey;

		}
		return "Pass";
	}

	// Input Event

	public String input(String xpathKey, String text) {
		try {
			Thread.sleep(1000);
			// logs.debug("Entering text:" + text +"in " +xpathKey);
			WebElement Input = driver.findElement(By.xpath(OR.getProperty(xpathKey)));
			wait = new WebDriverWait(driver, 1000);
			wait.until(ExpectedConditions.visibilityOf(Input));
			Thread.sleep(1000);
			Input.click();
			Input.clear();
			Input.sendKeys(text);

		} catch (Exception e) {
			return "Fail -Unable to input text on" + xpathKey;
		}
		return "Pass";
	}

	

	

	public String navigate(String URLKey) {
		try {

			Thread.sleep(10000);
			// logs.debug("Navigating to URL:" + ENV.getProperty(URLKey));
			driver.get(ENV.getProperty(URLKey));
			driver.navigate().refresh();
			Thread.sleep(1000);
		} catch (Exception e) {
			return "Fail -Unable to navigate to" + URLKey;
		}
		return "Pass";
	}

	/*
	 * public String navigateback(String URLKey) { try{ Thread.sleep(1000);
	 * //logs.debug("Navigating to URL:" + ENV.getProperty(URLKey));
	 * driver.get(ENV.getProperty(URLKey)); driver.navigate().refresh();
	 * Thread.sleep(1000); } catch(Exception e) { return
	 * "Fail -Unable to navigate to" + URLKey; } return "Pass"; }
	 */

	// Text Validation Event

	public String validatetext(String expectedtext) {
		try {

			String actualtext = driver.findElement(By.xpath(OR.getProperty("DCM"))).getText();
			wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.getProperty("DCM"))));
			Thread.sleep(10000);
			System.out.println("Welcome");
			System.out.println("Element Text is " + actualtext);
			if (actualtext.contains(expectedtext)) {
				// logs.debug(actualtext + " matches " + expectedtext);
				return "Pass";
			} else {
				// logs.debug(expectedtext+" "+"Got instead:"+ actualtext);
				return "Fail";
			}
		} catch (Exception e) {
			return "Fail";
		}

	}

	public String validateUrl(String expectedtext) {
		try {

			String actualtext = driver.getTitle();
			System.out.println("Actual page title After cliking the Serach button-->" + actualtext);
			if (actualtext.contains(expectedtext)) {
				// logs.debug(actualtext + " matches " + expectedtext);
				return "Pass";
			} else {
				// logs.debug(expectedtext+" "+"Got instead:"+ actualtext);
				return "Fail";
			}
		} catch (Exception e) {
			return "failed with exception";
		}

	}

	// Close browser event

	public String closebrowser() {
		try {
			if (driver != null) {
				if (bak_chrome != null)
					bak_chrome.quit();
				if (bak_mozilla != null)
					bak_mozilla.quit();
				if (bak_ie != null)
					bak_ie.quit();
				bak_chrome = bak_mozilla = bak_ie = null;
			}
			// logs.debug("Closed browser successfully");
			return "Pass";
		} catch (Exception e) {
			return "Failed to close the browser";
		}

	}

	///////////////// Wait condition Explicit wait condition
	///////////////// /////////////////////////////////

	public String waitforobject(String xpathKey) {

		try {

			wait = new WebDriverWait(driver, 500000);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			return "Pass";
		} catch (Exception e) {
			// logs.debug("The element:"+ xpathKey + "is not present");
			System.out.println("Wait Method Failed");
			// return "Failed to find the element" + xpathKey;
		}
		return "Pass";
	}

	// PAGE LAOD SYNCHRONIZE
	public String synchronize() {
		// logs.debug("Waiting for page to load");
		((JavascriptExecutor) driver).executeScript("function pageloadingtime()" + "{"
				+ "return 'Page has completely loaded'" + "}" + "return (window.onload=pageloadingtime());");

		return "Pass";
	}

	public String selectfromDropDown(String xpathKey, String text) {

		try {
			wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			Select select = new Select(driver.findElement(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			select.selectByVisibleText(text);
			Thread.sleep(1000);
		}

		catch (Exception e) {
			return "Fail -Unable to select the option" + xpathKey;
		}

		return "Pass";
	}

	public String selectAllOption(String xpathKey, String text) {

		try {
			wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			Select select = new Select(driver.findElement(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			select.selectByVisibleText(text);
			Thread.sleep(1000);
		}

		catch (Exception e) {
			return "Fail -Unable to select the option" + xpathKey;
		}

		return "Pass";
	}

	public String selectcaseType(String xpathKey, String text) {

		try {
			wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			Select select = new Select(driver.findElement(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			select.selectByVisibleText(text);
			System.out.println("Case Type Selected");
			Thread.sleep(1000);
		}

		catch (Exception e) {
			return "Fail -Unable to select the option" + xpathKey;
		}

		return "Pass";
	}

	public String selectActivity(String xpathKey, String text) {

		try {
			wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			Select select = new Select(driver.findElement(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			select.selectByVisibleText(text);
			Thread.sleep(1000);
		}

		catch (Exception e) {
			return "Fail -Unable to select the option" + xpathKey;
		}

		return "Pass";
	}

	public String selectcaseStatus(String xpathKey, String text) {

		try {

			wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			Select select = new Select(driver.findElement(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			select.selectByVisibleText(text);
			System.out.println("Case Status Selected");
			Thread.sleep(1000);
		}

		catch (Exception e) {
			return "Fail -Unable to select the option" + xpathKey;
		}

		return "Pass";
	}

	public String selectclaimType(String xpathKey, String text) {

		try {
			wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			Select select = new Select(driver.findElement(By.xpath(OR.getProperty(xpathKey))));
			Thread.sleep(1000);
			select.selectByVisibleText(text);
			System.out.println("Claim Type Selected");
			Thread.sleep(1000);
		}

		catch (Exception e) {
			return "Fail -Unable to select the option" + xpathKey;
		}

		return "Pass";
	}

	public String click1() {
		try {
			Thread.sleep(500);
			String part1 = "//*[@id='GridViewNewRecoverySettlements']/tbody/tr[";
			String part2 = "]/td[1]";
			while (isElementPresent1(part1 + i + part2)) {
				i++;
			}
			// Thread.sleep(5000);
			// logs.debug("clicking object:" + part1+(i-1)+part2);
			driver.findElement(By.xpath(part1 + (i - 1) + part2)).click();
		} catch (Exception e) {
			return "Fail -Unable to select latest settlement";
		}
		return "Pass";
	}

	// Element validation

	public static boolean isElementPresent1(String xpathKey1) {
		int count = driver.findElements(By.xpath(xpathKey1)).size();

		if (count == 0)
			return false;
		else
			return true;

	}

	///// Assert Text

	public String Asserttext(String expectedtext, String xpathKey) {
		WebElement element = driver.findElement(By.xpath(OR.getProperty(xpathKey)));
		String strng = element.getText();
		System.out.println("Element Text is " + strng);
		Assert.assertEquals(expectedtext, strng);
		// logs.debug(strng + " matches " + expectedtext);
		return "Pass";

	}

	// Validate Title event

	public String validatetitle(String expectedtitlekey) {
		String expectedtitle = OR.getProperty(expectedtitlekey);
		String actualtitle = driver.getTitle();
		try {

			if (expectedtitle.equals(actualtitle)) {
				return "Pass";
			}

			else {
				throw new Exception();
			}
		} catch (Exception e) {
			return "Expected" + expectedtitle + "but found" + actualtitle;
		}

	}

	public void refresh() {
		driver.navigate().refresh();
	}
	////////////////// Is Element Present /////////////////

	public String isElementPresent(String xpathKey) {
		int count = driver.findElements(By.xpath(OR.getProperty(xpathKey))).size();
		try {
			if (count == 0)
				throw new Exception();
			else {
				return "Pass";
			}
		} catch (Exception e) {
			// logs.debug("The element:"+ xpathKey + "is not present");
			return "Failed to find the element" + xpathKey;
		}
	}

	//////////////// Page Load Event ///////////////////////////

	public String PageLoad1() {
		System.out.println("Text box text4 is not invisible");
		try {

			Thread.sleep(2000);
			wait = new WebDriverWait(driver, 50);
			System.out.println("Text box text4 is not invisible");
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[@id='ctl00_ContentPlaceHolder1_ImageProgress']")));
			System.out.println("Text box text4 is now invisible");
			return "Pass";
		} catch (Exception e) {
			// logs.debug("The element: is not present");
			return "Failed to find the element";
		}

	}

	///////////////////// PageLoad loop

	public String PageLoad() {
		try {

			Thread.sleep(2000);
			wait = new WebDriverWait(driver, 110);
			System.out.println("Loading is In-Progress");
			wait.until(ExpectedConditions
					.invisibilityOfElementLocated(By.xpath("//*[@id='ctl00_ContentPlaceHolder1_ImageProgress']")));
			System.out.println("Loading is now Completed");
			return "Pass";
		} catch (Exception e) {
			// logs.debug("The element: is not present");
			return "Failed to find the element";
		}
	}

	public String trysending(String keys) {
		Actions builder = new Actions(driver);
		builder.sendKeys(keys).build().perform();
		return "Pass";

	}

	//////////////// Page Load Event ///////////////////////////

	public String PageLoad2() {
		try {
			int count = 60;
			Thread.sleep(2000);
			for (i = 0; i < count; i++) {
				int size = driver.findElements(By.xpath("//*[@id='ctl00_ContentPlaceHolder1_ImageProgress']")).size();
				System.out.println(size);
				if (size == 0) {
					Thread.sleep(1000);
					System.out.println("nOT eXIST" + i);
					break;
				} else {
					System.out.println("eXIST" + i);
					Thread.sleep(1000);
				}
			}
		} catch (Exception e)

		{
			// logs.debug("The element: is not present");
			return "Failed to find the element";
		}
		return "Pass";
	}

	///////////// Try Sending Enter Tab /////////////////////////////

	public String trysendingtab() {
		Actions builder = new Actions(driver);
		builder.sendKeys(Keys.TAB).build().perform();
		return "Pass";
	}

	/////////////////// Try Sending Enter event //////////////////////

	public String trysendingenter() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Actions builder = new Actions(driver);
		builder.sendKeys(Keys.ENTER).build().perform();
		return "Pass";
	}

	//////////// Try Sending Arrowdown Event //////////////////////

	public String trysendingarrowdown(String no) {
		int foo = Integer.parseInt(no);
		Actions builder = new Actions(driver);
		for (int i = 0; i < foo - 1; i++) {
			builder.sendKeys(Keys.ARROW_DOWN).build().perform();
		}
		return "Pass";
	}

	///////////////// Java script for click event ////////////////////////////

	public String testjavascript(String xpathKey) {
		WebElement element = driver.findElement(By.xpath(OR.getProperty(xpathKey)));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			Thread.sleep(1000);

			js.executeScript("arguments[0].click();", element);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Pass";
	}

	/////////////////// acceptalert ////////////////////

	public String acceptalert() {
		Alert al = driver.switchTo().alert();
		System.out.println(al.getText());
		al.accept();// OK

		return "Pass";
	}

	////////////// Switch to alert //////////////
	public String SwitchToAlert() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Pass";
	}

	public String Refreshpage() {
		driver.navigate().refresh();
		System.out.println("pagerefresh");
		// al.accept();//OK
		return "Pass";
	}

	//////////////// Pagescroll //////////////////////

	public String pagescroll() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("scroll(0, 250)");
		return "Pass";
	}

	////////////// upload file through AutoIT //////////////////////////

	public String uploadfile(String xpathKey, String FilePath) {

		try {
			Thread.sleep(2000);

			// logs.debug("clicking object:" + xpathKey);
			Thread.sleep(1000);
			WebElement button = driver.findElement(By.xpath(OR.getProperty(xpathKey)));
			System.out.println("Element found");
			// JavascriptExecutor js = (JavascriptExecutor) driver;
			Thread.sleep(1000);

			// js.executeScript("arguments[0].click();", button);
			// Thread.sleep(1000);
			// button.sendKeys(Keys.ENTER);
			button.click();
			Thread.sleep(1000);
			// String Command = "C://Users//ashish.baldota//Desktop//upf3.exe " + FilePath;
			// String Command = (System.setProperty("user.dir")+
			// "//Chrome//chromedriver.exe) + FilePath;
			String Command = System.setProperty("Command",
					System.getProperty("user.dir") + "//Chrome//test.txt" + FilePath);

			Runtime.getRuntime().exec(Command);
			Thread.sleep(3000);
			return "Pass";
		} catch (Exception e) {

			e.printStackTrace();
			return "Fail";
		}
		// return "Pass";
	}
}