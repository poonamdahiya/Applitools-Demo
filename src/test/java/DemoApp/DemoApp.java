package DemoApp;


import com.applitools.eyes.*;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Runs Applitools test for the demo app https://demo.applitools.com
 */
@RunWith(JUnit4.class)
public class DemoApp {

    private EyesRunner runner;
    private Eyes eyes;
    private static BatchInfo batch;
    private WebDriver driver;

    @BeforeClass
    public static void setBatch() {
        // Must be before ALL tests (at Class-level)
        batch = new BatchInfo("Demo_App");
        String batchId = System.getenv("APPLITOOLS_BATCH_ID");
        if(batchId != null) {
            batch.setId(batchId);
            System.out.println("Applitools Batch ID is " + batchId);
        }
    }

    @Before
    public void beforeEach() {
        // Initialize the Runner for your test.
        runner = new ClassicRunner();
        Configuration sconf = new Configuration();
        sconf.setIgnoreDisplacements(true);
        sconf.setIgnoreCaret(true);
        sconf.setMatchTimeout(10000);
        sconf.setStitchMode(StitchMode.CSS);
        sconf.setMatchLevel(MatchLevel.STRICT);
        sconf.setWaitBeforeScreenshots(2000);
        sconf.setAppName("Demo_App");
        sconf.setBatch(batch);
        //sconf.setForceFullPageScreenshot(true);


        // Initialize the eyes SDK

        eyes = new Eyes();
        eyes.setLogHandler(new FileLogger("/Users/nikhil/Documents/demos/Java/logs/DemoApp.log",true,true));


        // Raise an error if no API Key has been found.
        if(isNullOrEmpty(System.getenv("APPLITOOLS_API_KEY"))) {
            throw new RuntimeException("No API Key found; Please set environment variable 'APPLITOOLS_API_KEY'.");
        }
        else
        {
            System.out.println("Applitools key is " + System.getenv("APPLITOOLS_API_KEY"));
        }

        // Set your personal Applitols API Key from your environment variables.
        eyes.setApiKey(System.getenv("APPLITOOLS_API_KEY"));

        // set configuration
        eyes.setConfiguration(sconf);

        // Use Chrome browser
        driver = new ChromeDriver();
        //driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS) ;
    }

    @Test
    public void DemoApp_Diff_Test_Strict() throws Exception {
        try {
            var flag = false;
            var tName = "Basic Diff Test (STRICT mode)";
            JavascriptExecutor js = (JavascriptExecutor)driver;

            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1200, 800));

            //Check point
            printConsoleOutput(tName,eyes);

            // Navigate the browser to the  app.
            driver.get("https://demo.applitools.com");

            // To see visual bugs after the first run, use the commented line below instead.
            //driver.get("https://demo.applitools.com/index_v2.html");

            // Induce Error 1 -  Remove logo via Java script
            BreakSite(flag, js, "remove","document.getElementsByClassName(\"btn btn-primary\")[0]");

            // Visual checkpoint #1 - Check the login page.
            // eyes.checkWindow("Home Page");
            eyes.check("Login Page",Target.window().fully());

            // navigate to catalogue page
            driver.get("https://demo.applitools.com/app.html");

            // Induce Error 2- Remove logo and change background color of text box
            //if(flag)
            //js.executeScript("document.getElementById(\"ext-input-2\").style.backgroundColor='red';");
            BreakSite(flag, js, "remove","document.getElementsByClassName(\"element-box-tp\")[0]");

            // Visual checkpoint #2 - Check the app page.
            eyes.check("Main Page", Target.window().fully());

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_Diff_Test_Layout() throws Exception {
        try {
            var flag = false;
            var tName = "Basic Diff Test (LAYOUT mode)";
            JavascriptExecutor js = (JavascriptExecutor)driver;

            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1200, 800));

            //Check point
            printConsoleOutput(tName,eyes);

            // navigate to catalogue page
            driver.get("https://demo.applitools.com/app.html");

            // Induce Error - Remove logo and change background color of text box
            BreakSite(flag, js, "css-font","document.getElementsByClassName(\"element-box-tp\")[0]");

            // Visual checkpoint #2 - Check the app page.
            eyes.check("Main Page", Target.window().layout().fully());

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_CrossBrowser_Test_Strict() throws Exception {
        try {
            var flag = false;

            if(flag) {
                driver.close();
                SafariOptions sfo= new SafariOptions();
                sfo.setUseTechnologyPreview(true);
                driver = new SafariDriver();
            }

            var tName = "Cross browser test (STRICT Mode)";
            JavascriptExecutor js = (JavascriptExecutor)driver;

            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.

            eyes.setBaselineEnvName("Chrome1600");
            eyes.open(driver, "Demo App", tName, new RectangleSize(1600, 800));

            //Check point
            printConsoleOutput(tName,eyes);

            // Navigate the browser to the  app.
            driver.get("https://demo.applitools.com");


            // Visual checkpoint #1 - Check the login page.
            // eyes.checkWindow("Home Page");
            eyes.check("Login Page",Target.window().fully());

            // navigate to catalogue page
            driver.get("https://demo.applitools.com/app.html");


            // Visual checkpoint #2 - Check the app page.
            eyes.check("Main Page", Target.window().fully());

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_IgnoreRegion_Test_Strict() throws Exception {
        try {
            var flag = false;
            var tName = "Ignore region test (STRICT mode)";
            JavascriptExecutor js = (JavascriptExecutor)driver;

            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1600, 800));

            // set batch name
            eyes.setBatch(batch);

            printConsoleOutput(tName, eyes);

            // Navigate the browser to the  app.
            driver.get("https://demo.applitools.com/app.html");

            //change style first table
            BreakSite(flag, js, "css-border","document.getElementsByClassName(\"element-box-tp\")[0]");
            // delete a value from the table
            BreakSite(flag,js, "remove","document.getElementsByClassName(\"balance-value\")[0]");
            //change style second table
            BreakSite(flag, js, "css-border","document.getElementsByClassName(\"element-box-tp\")[1]");
            List<WebElement> elements = driver.findElements(By.className("element-box-tp"));
            eyes.check("Main Page", Target.window().fully().ignore(elements.get(0), elements.get(1)));

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_CheckRegion_Test_Strict() throws Exception {
        try {
            var flag = false;
            var tName = "Check region test (STRICT mode)";
            JavascriptExecutor js = (JavascriptExecutor)driver;
            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1600, 800));

            // set batch name
            eyes.setBatch(batch);

            printConsoleOutput(tName, eyes);

            // Navigate the browser to the  app.
            driver.get("https://demo.applitools.com/app.html");
            BreakSite(flag, js, "css", "document.getElementsByClassName(\"table-responsive\")[0]");

            WebElement ele = driver.findElement(By.className("table-responsive"));
            eyes.check("Transaction Grid", Target.region(ele).fully());

            WebElement ele2 = driver.findElement(By.xpath("/html/body/div/div[3]/div[1]"));
            eyes.check("Left Menu", Target.region(ele2).fully().ignore(driver.findElement(By.className("logged-user-i"))));

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_CheckFloatingRegion_Test_Strict() throws Exception {
        try {
            var flag = false;
            var tName = "Check floating region test";
            JavascriptExecutor js = (JavascriptExecutor)driver;
            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1600, 800));

            // set batch name
            eyes.setBatch(batch);

            printConsoleOutput(tName, eyes);

            // Navigate the browser to the  app.
            driver.get("https://demo.applitools.com/app.html");

            BreakSite(flag,js,"css-position","document.getElementsByClassName(\"element-box-tp\")[0]");

            WebElement ele = driver.findElement(By.className("element-box-tp"));
            eyes.check("Stats bar", Target.window().floating(ele,50,50,50,50).fully());

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_CheckStrictRegion_Test_Layout() throws Exception {
        try {
            var flag = false;
            var tName = "Check strict region test (Layout mode)";
            JavascriptExecutor js = (JavascriptExecutor)driver;

            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1600, 800));
            eyes.setMatchLevel(MatchLevel.LAYOUT);

            printConsoleOutput(tName, eyes);

            // Navigate the browser to the  app.
            driver.get("https://demo.applitools.com/app.html");

            // delete a value from the first table
            BreakSite(flag,js, "remove","document.getElementsByClassName(\"balance-value\")[0]");
            // delete a value from the second table
            BreakSite(flag,js, "table","document.getElementsByClassName(\"table-responsive\")[0]");

            WebElement ele = driver.findElement(By.className("element-box-tp"));
            eyes.check("Stats bar", Target.window().strict(ele).fully());
            //eyes.check("Special", Target.region(By.id("categoryHeader-menu")).ignore(ele));

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_CheckLayoutRegion_Test_Strict() throws Exception {
        try {
            var flag = false;
            var tName = "Check Layout region test (STRICT mode)";
            JavascriptExecutor js = (JavascriptExecutor)driver;
            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1600, 800));
            eyes.setMatchLevel(MatchLevel.STRICT);

            printConsoleOutput(tName, eyes);

            // Navigate the browser to the  app.
            driver.get("https://demo.applitools.com/app.html");

            // delete a value from the table
            BreakSite(flag,js, "remove","document.getElementsByClassName(\"balance-value\")[0]");

            List<WebElement> elements = driver.findElements(By.className("element-box-tp"));
            eyes.check("Main Page", Target.window().layout(elements.get(1)).fully());

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_CheckElement_Test_Strict() throws Exception {
        try {
            var flag = false;
            var tName = "Check element test (STRICT mode)";
            JavascriptExecutor js = (JavascriptExecutor)driver;
            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1600, 800));

            printConsoleOutput(tName, eyes);

            // Navigate the browser to the  app.
            driver.get("https://demo.applitools.com/app.html");

            BreakSite(flag, js, "css", "document.getElementsByClassName(\"table-responsive\")[0]");

            WebElement ele = driver.findElement(By.className("table-responsive"));
            eyes.check("Stats bar", Target.region(ele).fully());

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_CompareBranch_Test_Strict () throws Exception
    {
        // to do
        try {
            var flag = false;
            var tName = "Compare branch test";
            JavascriptExecutor js = (JavascriptExecutor)driver;
            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1600, 800));

            printConsoleOutput(tName, eyes);

            //eyes.setBaselineBranchName("DemoApp_Branch");
            eyes.setBranchName("DemoApp_Branch");

            //Check point
            printConsoleOutput(tName, eyes);

            if (flag) {
                // To see visual bugs after the first run, use the commented line below instead.
                driver.get("https://demo.applitools.com/index_v2.html");
            } else {
                // Navigate the browser to the  app.
                driver.get("https://demo.applitools.com");
            }

            // Visual checkpoint #1 - Check the login page.
            // eyes.checkWindow("Home Page");
            eyes.check("Login Page", Target.window().fully().strict());

            driver.findElement(By.id("log-in")).click();

            // Visual checkpoint #2 - Check the app page.
            eyes.check("Main Page", Target.window().fully().strict());

            // End the test.
            eyes.closeAsync();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @Test
    public void DemoApp_SlackIntegration_Test ()
    {
        try {
            var flag = false;
            var tName = flag ? "Slack integration test - Flag result" : "Slack integration test - Pass result";
            JavascriptExecutor js = (JavascriptExecutor) driver;

            //System.setProperty("webdriver.chrome.driver","/Users/Nikhil/Documents/chromedriver/v81/chromedriver.exe");
            // Set AUT's name, test name and viewport size (width X height)
            // We have set it to 800 x 600 to accommodate various screens. Feel free to
            // change it.
            eyes.open(driver, "Demo App", tName, new RectangleSize(1200, 800));

            //Check point
            printConsoleOutput(tName, eyes);

            if (flag) {
                // To see visual bugs after the first run, use the commented line below instead.
                driver.get("https://demo.applitools.com/index_v2.html");
            } else {
                // Navigate the browser to the  app.
                driver.get("https://demo.applitools.com");
            }

            // Visual checkpoint #1 - Check the login page.
            // eyes.checkWindow("Home Page");
            eyes.check("Login Page", Target.window().fully().strict());

            driver.findElement(By.id("log-in")).click();

            // Visual checkpoint #2 - Check the app page.
            eyes.check("Main Page", Target.window().fully().strict());

            // End the test.
            eyes.closeAsync();

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    @After
    public void afterEach() {
        // Close the browser.
        driver.quit();

        // If the test was aborted before eyes.close was called, ends the test as
        // aborted.
        eyes.abortIfNotClosed();

        // Wait and collect all test results
        TestResultsSummary allTestResults = runner.getAllTestResultsImpl(false);
        // Print results
        System.out.println(allTestResults);
    }

    public static void printConsoleOutput( String tname, Eyes obj)
    {
        System.out.println("Executing test ["+tname+"] with configurations:");
        System.out.println(obj.getConfiguration().toString());

    }

    private static void BreakSite(Boolean flag, JavascriptExecutor js,String changeType, WebElement selectedElement ) {

        if(flag) {
            switch (changeType) {
                case "css":
                    js.executeScript("arguments[0].setAttribute('style','color: red')", selectedElement);
                    //js.executeScript("arguments[0].setAttribute('style','color: pink')", driver.findElement(By.cssSelector("#login > form > div.auth-form-body.mt-3 > label:nth-child(3)")));
                    js.executeScript("arguments[0].setAttribute('style','fill: green')", selectedElement);
                    js.executeScript("arguments[0].setAttribute('style','font-size: 8px')", selectedElement);
                    break;

                case "remove":
                    js.executeScript("arguments[0].remove();", selectedElement);
                    break;

                case "content":
                default:
                    //js.executeScript("arguments[0].setAttribute("innerHtml",;", selectedElement);
                    break;

            }
        }

    }

    private static void BreakSite(Boolean flag, JavascriptExecutor js,String changeType, String domElement ) {

        if(flag) {
            switch (changeType) {
                case "css":
                    js.executeScript(domElement +".setAttribute('style','position:relative; bottom:30px; font-size:20px; border: 3px solid #73AD21');");
                    break;
                case "css-position":
                    js.executeScript(domElement+ ".setAttribute('style','position:relative; bottom:40px; border: 1px solid red');");
                    break;
                case "css-font":
                    js.executeScript(domElement+ ".setAttribute('style','font-size: 25px');");
                    break;
                case "css-border":
                    js.executeScript(domElement+ ".setAttribute('style','border: 3px solid #73AD21');");
                    break;
                case "bg":
                    js.executeScript(domElement +"style.backgroundColor='red';");
                    break;

                case "remove":
                    js.executeScript(domElement+ ".remove();");
                    break;

                case "table":
                    js.executeScript(domElement + ".deleteRow(1);");
                    break;

                case "content":
                default:
                    //js.executeScript("arguments[0].setAttribute("innerHtml",;", selectedElement);
                    break;

            }
        }

    }

}