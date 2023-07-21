Feature: CloudSPM_ContactUsPage

  Background: 
    Given launch the CloudSPM browser for web selenoid
    When The CloudSPM application is loaded

    Scenario: Verify User is not able to submit the Contact Us form by entering invalid details
    When User clicks on the Contact Us Button
    And User enters the invalid Contact Us form details 1
    And CloudSPM Testcase is completed