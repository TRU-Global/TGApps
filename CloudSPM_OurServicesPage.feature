Feature: CloudSPM_OurServicesPage

  Background: 
    Given launch the CloudSPM browser for web selenoid
    When The CloudSPM application is loaded

  Scenario: Verify various buttons under Our Services Page
    When User clicks on the Services link
    When User clicks on the View All Services Button
    And User clicks on various buttons under Our Services Page
    Then User Verifies the Strategy Text 0
    Then User Verifies the Implementation Text 0
    Then User Verifies the Managed Services Text 0
    Then User Verifies the Staff Augmentation Text 0
    Then User Verifies the Training Text 0
    And CloudSPM Testcase is completed
