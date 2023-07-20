package com.truglobal.runner;

import org.junit.runner.RunWith;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.cucumber.junit.Cucumber;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@Test
@RunWith(Cucumber.class)
@CucumberOptions(features={
		"src/test/resources/Features/01_Login.feature",
		"src/test/resources/Features/Account.feature",
		"src/test/resources/Features/calender.feature",
		"src/test/resources/Features/Contacts.feature",
		"src/test/resources/Features/EndtoEndFlow.feature",
		"src/test/resources/Features/Opportunity.feature",
		"src/test/resources/Features/Quote.feature",
		"src/test/resources/Features/Case Management.feature",
		"src/test/resources/Features/SFDC_API.feature",
		"src/test/resources/Features/Task.feature",
		"src/test/resources/Features/Lead.feature",
		"src/test/resources/Features/CampaignManagement.feature",
		"src/test/resources/Features/Manufacturing_SalesAgreement.feature",
		"src/test/resources/Features/Manufacturing_AccountManagerTargets.feature",
		"src/test/resources/Features/QuoteManagment.feature",
		"src/test/resources/Features/Manufacturing_Rebate Program Members.feature",
		"src/test/resources/Features/Manufacturing_AdvanceAccountForcastSetPartner.feature",
		"src/test/resources/Features/Manufacturing_AdvanceAccountForcastSetUser.feature",
		"src/test/resources/Features/ContactManagement.feature",
		"src/test/resources/Features/Manufacturing_RebateProgram.feature",
		"src/test/resources/Features/Manufacturing_ManufacturingPrograms.feature"
	
},
dryRun= false,   //Only if we want to get Only Snippet without running whole program than put as true 
glue = {"com.truglobal.stepdefinitions"}, // its represent the package name in step definitions
monochrome = true,  //Display the console output in proper readable format
//plugin = { "pretty",  "com.epam.reportportal.cucumber.ScenarioReporter" }
plugin = { "pretty",  "com.epam.reportportal.cucumber.ScenarioReporter" },

tags = ""
// tags = {"@SmokeTest" , "@End2End"} => AND
// tags = {"@SmokeTest, @End2End"} => OR

)


public class Runner extends AbstractTestNGCucumberTests {
	@Override
	@DataProvider(parallel = false)
	public Object[][] scenarios() {
		return super.scenarios();
	}
}
