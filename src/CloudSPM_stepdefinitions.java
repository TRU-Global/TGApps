package com.truglobal.stepdefinitions;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Listeners;

import com.epam.reportportal.testng.ReportPortalTestNGListener;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvException;
import com.truglobal.email.EmailSender;
import com.truglobal.pageobjectmodel.AboutUsPage;
import com.truglobal.pageobjectmodel.ContactUsPage;
import com.truglobal.pageobjectmodel.HomePage;
import com.truglobal.pageobjectmodel.OurServicesPage;
import com.truglobal.runner.Runner;
import com.truglobal.util.ServiceLocator;

import io.cucumber.java.AfterAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

@Listeners({ ReportPortalTestNGListener.class })
public class CloudSPM_stepdefinitions {
	private static final Logger logger = LogManager.getLogger(CloudSPM_stepdefinitions.class);

	ServiceLocator service = ServiceLocator.getInstance();
	// WebDriver driver;
	RemoteWebDriver driver;
	HomePage homepageObj;
	OurServicesPage ourservicespageObj;
	ContactUsPage contactuspageObj;
	AboutUsPage aboutuspageObj;

	@Given("^launch the CloudSPM browser for web$")
	public void launchBrowserforweb() throws IOException, CsvException {
		// chrome
		/*
		 * WebDriverManager.chromedriver().setup();
		 * logger.info("Starting ChromeDriver"); driver = new ChromeDriver();
		 * logger.info("ChromeDriver Started Successfully");
		 * logger.info("Start of Window Maximize"); driver.manage().window().maximize();
		 * logger.info("End of Window Maximize");
		 * driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		 */
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		WebDriverManager.chromedriver().setup();
		logger.info("Starting ChromeDriver");
		driver = new ChromeDriver(options);
		logger.info("ChromeDriver Started Successfully");
		logger.info("Start of Window Maximize");
		driver.manage().window().maximize();
		logger.info("End of Window Maximize");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		homepageObj = new HomePage(driver);
		ourservicespageObj = new OurServicesPage(driver);
		contactuspageObj = new ContactUsPage(driver);
		aboutuspageObj = new AboutUsPage(driver);

	}

	@Given("^launch the CloudSPM browser for web selenoid$")
	public void launchBrowserforwebselnoid() throws IOException, CsvException {

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-blink-features=AutomationControlled");
		String date = new Date().toString();
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browserName", service.getProperty("browserNameAndVersion").split("->")[0]);
		caps.setCapability("version", service.getProperty("browserNameAndVersion").split("->")[1]);
		caps.setCapability(ChromeOptions.CAPABILITY, options);
		MutableCapabilities mut = new MutableCapabilities();
		mut.setCapability(ChromeOptions.CAPABILITY, options);
		mut.setCapability("enableVNC", Boolean.parseBoolean(service.getProperty("enableVNC")));
		mut.setCapability("enableVideo", Boolean.parseBoolean(service.getProperty("enableVideo")));
		mut.setCapability("videoName", date + " Video.mp4");
		mut.setCapability("enableLog", Boolean.parseBoolean(service.getProperty("enableLog")));
		mut.setCapability("logName", date + " SeleniumLog.log");
		mut.setCapability("name", date);
		mut.setCapability("sessionTimeout", service.getProperty("selenoidSessionIdleTimeout"));
		String[] list = { "VERBOSE=VERBOSE" };
		mut.setCapability("env", list);
		caps.setCapability("selenoid:options", mut);
		URL gridUrl = new URL("http://10.60.23.210/selenoid/wd/hub");
		logger.info("Starting ChromeDriver||TestName=" + date);
		driver = new RemoteWebDriver(gridUrl, caps);
		logger.info("ChromeDriver Started Successfully");
		logger.info("Start of Window Maximize");
		driver.manage().window().maximize();
		logger.info("End of Window Maximize");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
		homepageObj = new HomePage(driver);
		ourservicespageObj = new OurServicesPage(driver);
		contactuspageObj = new ContactUsPage(driver);
		aboutuspageObj = new AboutUsPage(driver);
	}

	@AfterAll
	public static void sendEmail() throws MessagingException {
		String reportportalURL = Runner.saveReportPortalLaunchId();

		String message = "Hi Team,</br><br>Execution is Completed. Please find the below link for reportportal <br>";
		message = message + "<a href='" + reportportalURL + "'>" + "Report Portal Link</a>";

		EmailSender email = new EmailSender();
		email.setFromAddress("truglobal-alerts@truglobal.com");
		email.setToAddresses("hamza.d@truglobal.com");
		email.setSubject("Execution Report");
		email.setMessageContent(message);
		// email.addAttachment("C:/Users/admin/Downloads/k.csv");
		// email.addAttachment("C:/Users/admin/Pictures/Capture.png");
		email.sendMail();
		System.out.println("Mail Sent");
	}

	@AfterAll
	public static void sendMessageToWebhook()
			throws JsonParseException, JsonMappingException, IOException, ParseException, InterruptedException {
		String reportportalURL = Runner.saveReportPortalLaunchId();
		List<String> reportPortalStats = getReportPortalStats(reportportalURL);
		String teamsWebhookUrl = "https://truglobal0.webhook.office.com/webhookb2/aceba28f-f8a4-4054-a10a-35ca9d5cb37e@f795bab8-f723-4a7c-afbf-4a7b5369243b/IncomingWebhook/6ff7d3fdeb114d8491e8cb3d543db2bb/f952d956-6654-47b8-8c2f-286bdecdc6a0";

		InputStream in = CloudSPM_stepdefinitions.class.getClassLoader()
				.getResourceAsStream("TeamsNotificationCard.json");
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readValue(in, JsonNode.class);

		String status = reportPortalStats.get(4);
		String failed = reportPortalStats.get(7);
		if (failed != null && Integer.parseInt(failed) > 0) {
			status = "FAILED";
		} else {
			failed = "0";
			status = "PASSED";
		}

		String skipped = reportPortalStats.get(8);
		if (skipped == null) {
			skipped = "0";
		}

		String jsonString = String.format(mapper.writeValueAsString(jsonNode),
				reportPortalStats.get(0) + " #" + reportPortalStats.get(1), reportPortalStats.get(2), status,
				reportportalURL, reportPortalStats.get(5), reportPortalStats.get(6), failed, skipped, reportportalURL);

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(teamsWebhookUrl);

		StringEntity entity = new StringEntity(jsonString);
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		client.execute(httpPost);
		client.close();
	}

	public static List<String> getReportPortalStats(String reportportalURL) {
		String[] Uuid = reportportalURL.split("/");
		JsonPath jsonObj = getLaunchInfoFromReportPortal(Uuid[Uuid.length - 1]);

		String name = jsonObj.getString("name");
		String number = jsonObj.getString("number");
		String startDate = jsonObj.getString("startTime");
		String endDate = jsonObj.getString("endTime");
		String status = jsonObj.getString("status");
		String total = jsonObj.getString("statistics.executions.total");
		String passed = jsonObj.getString("statistics.executions.passed");
		String failed = jsonObj.getString("statistics.executions.failed");
		String skipped = jsonObj.getString("statistics.executions.skipped");

		Date date = new Date();
		startDate = date.toString();

		List<String> data = new ArrayList<>();
		data.add(name);
		data.add(number);
		data.add(startDate);
		data.add(endDate);
		data.add(status);
		data.add(total);
		data.add(passed);
		data.add(failed);
		data.add(skipped);

		System.out.println(data);
		return data;
	}

	public static JsonPath getLaunchInfoFromReportPortal(String launchUuid) {
		String baseUrl = "http://10.60.23.210";
		String endpoint = "/api/v1/superadmin_personal/launch/uuid/" + launchUuid;
		String authToken = "e3d92e8f-b67a-451a-b50d-984288a57727";
		Map<String, Object> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + authToken);

		RestAssured.baseURI = baseUrl;
		Response response = given()
				.headers("Authorization", "Bearer " + authToken, "Content-Type", ContentType.JSON, "Accept",
						ContentType.JSON)
				.when().get(endpoint).then().contentType(ContentType.JSON).extract().response();

		System.out.println(response.getStatusCode());
		JsonPath jsonObj = response.jsonPath();
		System.out.println(jsonObj);
		return jsonObj;
	}

	@Given("^The CloudSPM application is loaded$")
	public void loadapplication() {
		logger.info("Start of Open URL");
		driver.get(service.getProperty("AUT"));
		logger.info("End of Open URL");
	}

	@When("CloudSPM Testcase is completed")
	public void cloudspm_testcase_is_completed() throws InterruptedException {
		Thread.sleep(5000);
		driver.quit();
	}

	@When("User clicks on the Home link")
	public void user_clicks_on_the_home_link() {
		homepageObj.clickhomeLink();
	}

	@Then("User Verifies the Home Page {int}")
	public void user_verifies_the_home_page(Integer i) {
		homepageObj.assertTextverifyHomePage(i);
	}

	@When("User clicks on the Applications link")
	public void user_clicks_on_the_applications_link() {
		homepageObj.clickapplicationsLink();
	}

	@Then("User Verifies the Applications Page {int}")
	public void user_verifies_the_applications_page(Integer i) {
		homepageObj.assertTextverifyApplications(i);
	}

	@When("User clicks on the Services link")
	public void user_clicks_on_the_services_link() {
		homepageObj.clickservicesLink();
	}

	@Then("User Verifies the Services Page {int}")
	public void user_verifies_the_services_page(Integer i) {
		homepageObj.assertTextverifyServices(i);
	}

	@When("User clicks on the Products link")
	public void user_clicks_on_the_products_link() {
		homepageObj.clickproductsLink();
	}

	@Then("User Verifies the Products Page {int}")
	public void user_verifies_the_products_page(Integer i) {
		homepageObj.assertTextverifyProducts(i);
	}

	@When("User clicks on the About link")
	public void user_clicks_on_the_about_link() {
		homepageObj.clickaboutLink();
	}

	@Then("User Verifies the About Page {int}")
	public void user_verifies_the_about_page(Integer i) {
		homepageObj.assertTextverifyAbout(i);
	}

	@When("User clicks on the Contact Us Button")
	public void user_clicks_on_the_contact_us_button() {
		homepageObj.clickcontactUsButton();
	}

	@Then("User Verifies the Contact Us Page {int}")
	public void user_verifies_the_contact_us_page(Integer i) {
		homepageObj.assertTextverifyContactUs(i);
	}

	@When("User clicks on the Know more Button")
	public void user_clicks_on_the_know_more_button() {
		homepageObj.clickknowMoreButton();
	}

	@Then("User Verifies the About Us Page {int}")
	public void user_verifies_the_about_us_page(Integer i) {
		homepageObj.assertTextverifyAboutUs(i);
	}

	@When("User clicks on the View All Services Button")
	public void user_clicks_on_the_view_all_services_button() {
		homepageObj.clickviewallServicesButton();
	}

	@Then("User Verifies the Our Services Page {int}")
	public void user_verifies_the_our_services_page(Integer i) {
		homepageObj.assertTextverifyOurServices(i);
	}

	@When("User clicks on the View Product Button")
	public void user_clicks_on_the_view_product_button() {
		homepageObj.clickviewproductButton();
	}

	@Then("User Verifies the SFDC Integration Page {int}")
	public void user_verifies_the_sfdc_integration_page(Integer i) {
		homepageObj.assertTextverifysfdcIntegration(i);
	}

	@When("User clicks on the Contact now Button")
	public void user_clicks_on_the_contact_now_button() {
		homepageObj.clickcontactNowButton();
	}

	@When("User clicks on the About us link")
	public void user_clicks_on_the_about_us_link() {
		homepageObj.clickaboutUslink();
	}

	@When("User clicks on the Strategic link")
	public void user_clicks_on_the_strategic_link() {
		homepageObj.clickstrategicLink();
	}

	@Then("User Verifies the Strategy Text {int}")
	public void user_verifies_the_strategy_text(Integer i) {
		homepageObj.assertTextverifyStrategy(i);
	}

	@When("User clicks on the Implementation link")
	public void user_clicks_on_the_implementation_link() {
		homepageObj.clickimplementationLink();
	}

	@Then("User Verifies the Implementation Text {int}")
	public void user_verifies_the_implementation_text(Integer i) {
		homepageObj.assertTextverifyImplementation(i);
	}

	@When("User clicks on the Managed Services link")
	public void user_clicks_on_the_managed_services_link() {
		homepageObj.clickmanagedServicesLink();
	}

	@Then("User Verifies the Managed Services Text {int}")
	public void user_verifies_the_managed_services_text(Integer i) {
		homepageObj.assertTextverifyManagedServices(i);
	}

	@When("User clicks on the Staff Augmentation link")
	public void user_clicks_on_the_staff_augmentation_link() {
		homepageObj.clickstaffAugmentationLink();
	}

	@Then("User Verifies the Staff Augmentation Text {int}")
	public void user_verifies_the_staff_augmentation_text(Integer i) {
		homepageObj.assertTextverifyStaffAugmentation(i);
	}

	@When("User clicks on the Training link")
	public void user_clicks_on_the_training_link() {
		homepageObj.clickTrainingLink();
	}

	@Then("User Verifies the Training Text {int}")
	public void user_verifies_the_training_text(Integer i) {
		homepageObj.assertTextverifyTraining(i);
	}

	@When("User clicks on the Products link below")
	public void user_clicks_on_the_products_link_below() {
		homepageObj.clickProductsLinkBelow();
	}

	@When("User clicks on the Contact Us link")
	public void user_clicks_on_the_contact_us_link() {
		homepageObj.clickContactUsLink();
	}

	@When("User clicks on the Contact Address link")
	public void user_clicks_on_the_contact_address_link() {
		homepageObj.clickContactAddress();
	}

	@Then("User Verifies the Contact Address Text {int}")
	public void user_verifies_the_contact_address_text(Integer i) {
		homepageObj.clickSearchBox();
		homepageObj.assertValueverifyContactAddress(i);
	}

	@When("User clicks on the Mobile Number link")
	public void user_clicks_on_the_mobile_number_link() {
		homepageObj.clickmobileNumberLink();
	}

	@When("User clicks on the Email Id link")
	public void user_clicks_on_the_email_id_link() {
		homepageObj.clickemailIdLink();
	}

	@Then("User Verifies the various icon boxes under Our Applications Section {int}")
	public void user_verifies_the_various_icon_boxes_under_our_applications_section(Integer i) {
		homepageObj.assertTextverifyOracleSpm(i);
		homepageObj.assertTextverifyIbm(i);
		homepageObj.assertTextverifySap(i);
		homepageObj.assertTextverifyXactly(i);
	}

	@Then("User Verifies the various box images under Our Featured Services Section {int}")
	public void user_verifies_the_various_box_images_under_our_featured_services_section(Integer i) {
		homepageObj.assertTextverifyStrategyText(i);
		homepageObj.assertTextverifyImplementationText(i);
		homepageObj.assertTextverifyManagedServicesText(i);
		homepageObj.assertTextverifyStaffAugmentationText(i);
		homepageObj.assertTextverifyTrainingText(i);
	}

	@Then("User Verifies the various banner layers under SPM delivered on Salesforce.com Section {int}")
	public void user_verifies_the_various_banner_layers_under_spm_delivered_on_salesforce_com_section(Integer i) {
		homepageObj.assertTextverifyCompensationDashboards(i);
		homepageObj.assertTextverifyIncentiveEstimator(i);
		homepageObj.assertTextverifyPlanAcceptance(i);
	}

	@When("User clicks on various buttons under Our Services Page")
	public void user_clicks_on_various_buttons_under_our_services_page() {
		ourservicespageObj.clickstrategyButton();
		ourservicespageObj.clickimplementationButton();
		ourservicespageObj.clickmanagedServicesButton();
		ourservicespageObj.clickstaffAugmentationButton();
		ourservicespageObj.clicktrainingButton();
	}

	@When("ContactUsPage Step {int}")
	public void ContactUsPage(Integer i) throws Throwable {

	}

	@When("User enters the valid Contact Us form details {int}")
	public void user_enters_the_valid_contact_us_form_details(Integer i) {
		contactuspageObj.clickfirstName();
		contactuspageObj.typefirstName(i);
		contactuspageObj.clicklastName();
		contactuspageObj.typelastName(i);
		contactuspageObj.clickemailId();
		contactuspageObj.typeemailId(i);
		contactuspageObj.clickcontactNumber();
		contactuspageObj.typecontactNumber(i);
		contactuspageObj.clickcompany();
		contactuspageObj.typecompany(i);
		contactuspageObj.clicksubject();
		contactuspageObj.typesubject(i);
		contactuspageObj.selectFrameindex();
		contactuspageObj.clickcheckBox();
		contactuspageObj.selectFrameframeParent();
		contactuspageObj.clicksubmitNow();
	}

	@Then("User verifies the submitted message {int}")
	public void user_verifies_the_submitted_message(Integer i) {
		contactuspageObj.assertTextverifySubmittedMessage(i);
	}

	@When("User enters the invalid Contact Us form details {int}")
	public void user_enters_the_invalid_contact_us_form_details(Integer i) {
		contactuspageObj.clickfirstName();
		contactuspageObj.typefirstName(i);
		contactuspageObj.clicklastName();
		contactuspageObj.typelastName(i);
		contactuspageObj.clickemailId();
		contactuspageObj.typeemailId(i);
		contactuspageObj.clickcontactNumber();
		contactuspageObj.typecontactNumber(i);
		contactuspageObj.clickcompany();
		contactuspageObj.typecompany(i);
		contactuspageObj.clicksubject();
		contactuspageObj.typesubject(i);
		contactuspageObj.selectFrameindex();
		contactuspageObj.clickcheckBox();
		contactuspageObj.selectFrameframeParent();
		contactuspageObj.clicksubmitNow();
	}

	@Then("User verifies various icon boxes under About Us page {int}")
	public void user_verifies_various_icon_boxes_under_about_us_page(Integer i) {
		aboutuspageObj.assertTextverifyCommunications(i);
		aboutuspageObj.assertTextverifyHighTech(i);
		aboutuspageObj.assertTextverifySemiconductor(i);
		aboutuspageObj.assertTextverifyFinancialServices(i);
		aboutuspageObj.assertTextverifyManufacturing(i);
		aboutuspageObj.assertTextverifyMedical(i);
		aboutuspageObj.assertTextverifyTelecom(i);
		aboutuspageObj.assertTextverifyAndMore(i);
	}

	@When("User clicks on the TRUGlobal link")
	public void user_clicks_on_the_tru_global_link() {
		homepageObj.clickTruglobalLink();
	}

	@Then("User Verifies the TRUGlobal Home Page {int}")
	public void user_verifies_the_tru_global_home_page(Integer i) {
		homepageObj.assertTextverifyTruglobalHome(i);
	}
}
