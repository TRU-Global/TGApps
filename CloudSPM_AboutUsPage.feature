Feature: CloudSPM_AboutUsPage

  Background: 
    Given launch the CloudSPM browser for web selenoid
    When The CloudSPM application is loaded

  Scenario: Verify various icon boxes under About Us page
    When User clicks on the About link
    When User clicks on the Know more Button
    Then User verifies various icon boxes under About Us page 0
    And CloudSPM Testcase is completed
