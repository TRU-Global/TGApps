Feature: CloudSPM_ContactUsPage

  Background: 
    Given launch the CloudSPM browser for web selenoid
    When The CloudSPM application is loaded

  Scenario: Verify User is able to submit the Contact Us form by entering valid details
    When User clicks on the Contact Us Button
    And User enters the valid Contact Us form details 0
    Then User verifies the submitted message 0
    And CloudSPM Testcase is completed

  Scenario: Verify User is not able to submit the Contact Us form by entering invalid details
    When User clicks on the Contact Us Button
    And User enters the invalid Contact Us form details 1
    And CloudSPM Testcase is completed
