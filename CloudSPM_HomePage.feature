Feature: CloudSPM HomePage

  Background: 
    Given launch the CloudSPM browser for web selenoid
    When The CloudSPM application is loaded

  Scenario: Verify the following links on the Home Page - Home, Applications, Services, Products, About
    When User clicks on the Home link
    Then User Verifies the Home Page 0
    When User clicks on the About link
    Then User Verifies the About Page 0
    When User clicks on the Applications link
    Then User Verifies the Applications Page 0
    When User clicks on the Services link
    Then User Verifies the Services Page 0
    When User clicks on the Products link
    Then User Verifies the Products Page 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Contact Us Button should be navigated to Contact Us Page
    When User clicks on the Contact Us Button
    Then User Verifies the Contact Us Page 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Know more Button should be navigated to About Us Page
    When User clicks on the About link
    When User clicks on the Know more Button
    Then User Verifies the About Us Page 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on View All Services Button should be navigated to Our Services Page
    When User clicks on the View All Services Button
    Then User Verifies the Our Services Page 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on View Product Button should be navigated to SFDC Integration Page
    When User clicks on the View Product Button
    Then User Verifies the SFDC Integration Page 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Contact now Button should be navigated to Contact Us Page
    When User clicks on the Contact now Button
    Then User Verifies the Contact Us Page 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on About us link should be navigated to About Us Page
    When User clicks on the About us link
    Then User Verifies the About Us Page 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Strategic link should be navigated to Our Services Page
    When User clicks on the Strategic link
    Then User Verifies the Strategy Text 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Implementation link should be navigated to Our Services Page
    When User clicks on the Implementation link
    Then User Verifies the Implementation Text 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Managed Services link should be navigated to Our Services Page
    When User clicks on the Managed Services link
    Then User Verifies the Managed Services Text 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Staff Augmentation link should be navigated to Staff Augmentation Page
    When User clicks on the Staff Augmentation link
    Then User Verifies the Staff Augmentation Text 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Training link should be navigated to Services Page
    When User clicks on the Training link
    Then User Verifies the Training Text 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Products link should be navigated to SFDC Integration Page
    When User clicks on the Products link below
    Then User Verifies the SFDC Integration Page 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Contact Us link should be navigated to Contact Us Page
    When User clicks on the Contact Us link
    Then User Verifies the Contact Us Page 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Contact Address link should be navigated to Google Maps
    When User clicks on the Contact Address link
    Then User Verifies the Contact Address Text 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Mobile Number link should display a pop up window
    When User clicks on the Mobile Number link
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on Email Id link should display a pop up window
    When User clicks on the Email Id link
    And CloudSPM Testcase is completed

  Scenario: Verify various icon boxes under Our Applications Section
    When User clicks on the Applications link
    Then User Verifies the various icon boxes under Our Applications Section 0
    And CloudSPM Testcase is completed

  Scenario: Verify various box images under Our Featured Services Section
    When User clicks on the Services link
    Then User Verifies the various box images under Our Featured Services Section 0
    And CloudSPM Testcase is completed

  Scenario: Verify various banner layers under SPM delivered on Salesforce.com Section
    When User clicks on the Products link
    Then User Verifies the various banner layers under SPM delivered on Salesforce.com Section 0
    And CloudSPM Testcase is completed

  Scenario: Verify User clicking on TRUGlobal link should be navigated to TRUGlobal Home Page
    When User clicks on the TRUGlobal link
    Then User Verifies the TRUGlobal Home Page 0
    And CloudSPM Testcase is completed
