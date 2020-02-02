package Framework;

import com.relevantcodes.extentreports.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class Index {

    protected static Map<String, ArrayList<Map<String, String>>> testData;
    ExtentReports extentReport;
    ExtentTest extentTest;
    WebDriver webDr;

    /*
     * Method Name: setUp
     * Author: Amit Singh
     * Description: Create HTML report template, load the test data and extent-config.xml file
     * Date Created: 01-Feb-2020
     * Modified By:
    */
    @BeforeSuite
    public void setUp()  {

        Config c = new Config();        //Initialize Configurations
        testData = Data.getTestData();  //Fetch Test Data

        //Initialize Extent Report
        extentReport = new ExtentReports(Config.getProperty("reportFolderPath") +
                "/SeleniumExtentReport.html", true);
        extentReport.addSystemInfo("Project Name", Config.getProperty("projectName"))
                .addSystemInfo("Environment", Config.getProperty("environment"))
                .addSystemInfo("Tester Name", System.getProperty("user.name"));
        extentReport.loadConfig(new File(System.getProperty("user.dir") + "\\extent-config.xml"));
    }

    /*
     * Method Name: beforeClass
     * Author: Amit Singh
     * Description: initialize webDriver
     * Date Created: 01-Feb-2020
     * Modified By:
    */
    @BeforeClass
    public void beforeClass() {
        String driverPath = Config.getProperty("driverPath");
        String browser = Config.getProperty("testBrowser").trim();

        switch (browser)  {
            case "Chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("disable-popup-blocking");
                chromeOptions.addArguments("disable-infobars");
                chromeOptions.addArguments("incognito");
                System.setProperty("webdriver.chrome.driver", driverPath + "chromedriver.exe");
                webDr = new ChromeDriver(chromeOptions);
                break;
            case "Opera":
                OperaOptions operaOptions = new OperaOptions();
                operaOptions.addArguments("disable-popup-blocking");
                operaOptions.addArguments("disable-infobars");
                System.setProperty("webdriver.ie.driver", driverPath + "operadriver.exe");
                webDr = new OperaDriver();
                break;
            case "IE":
                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.ignoreZoomSettings();
                ieOptions.introduceFlakinessByIgnoringSecurityDomains();
                System.setProperty("webdriver.ie.driver", driverPath + "IEDriverServer.exe");
                webDr = new InternetExplorerDriver(ieOptions);
                break;
            default:
                System.out.println("Invalid browser specified in Config file: " + browser);
                break;
        }
        webDr.manage().window().maximize();
    }

    /*
     * Method Name: beforeMethod
     * Author: Amit Singh
     * Description: Collect the current running test case name
     * Date Created: 01-Feb-2020
     * Modified By:
    */
    @BeforeMethod
    public void beforeMethod(Method method) {
        String className = this.getClass().getSimpleName();
        extentTest = extentReport.startTest(className + "-" + method.getName());
    }

    @Test
    public void printText() {
        System.out.println("Hello World");
    }

    /*
     * Method Name: getResult
     * Author: Amit Singh
     * Description: Collect the test execution status and based on that log into HTML report
     * Date Created: 01-Feb-2020
     * Modified By:
    */
    @AfterMethod
    public void getResult(ITestResult result, Method method) throws Exception {
        if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.log(LogStatus.FAIL, "Test Case Failed is: " + result.getName());
            extentTest.log(LogStatus.FAIL, "Error Details :- \n" + result.getThrowable().getMessage());
        }
        if (result.getStatus() == ITestResult.SKIP) {
            extentTest.log(LogStatus.SKIP, "Test Case Skipped is: "  + result.getName());
        }
        if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.log(LogStatus.PASS, "Test Case Passed is: " + result.getName());
        }
    }

    /*
     * Method Name: wrapUp
     * Author: Amit Singh
     * Description: Stop the object of ExtentReports and ExtentTest
     * Date Created: 01-Feb-2020
     * Modified By:
    */
    @AfterSuite
    public void wrapUp() {
        extentReport.endTest(extentTest);
        extentReport.flush();
        webDr.quit();
    }
}