Feature: Settings

  @id482 @regression
  Scenario Outline: Verify user can access settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    Then I see settings page

    Examples: 
      | Name      |
      | user1Name |

  @id729 @regression
  Scenario Outline: Attempt to open About screen in settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id862
  Scenario Outline: Verify reset password page is accessible from settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
	And I click on Settings button on personal page
	And I click on Settings button from the options menu
	And I click on Change Password button in Settings
	Then I see reset password page

    Examples: 
      | Name      |
      | user1Name |

  @id1258 @regression
  Scenario Outline: Verify default value for sound settings is all
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    Then I verify that all is the default selected value

    Examples: 
      | Name      |
      | user1Name |
      
  @regression @id2074
  Scenario Outline: Verify you can access Help site within the app
  	Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    When I click on Help button from the options menu
    Then I see Support web page
    
    Examples: 
      | Name      |
      | user1Name |
      
  @regression @id2146
  Scenario Outline: Verify switching on/off chatheads
    Given There are 3 users where <Name> is me
    Given User <Contact2> change avatar picture to <Picture>
    Given User <Contact2> change name to <NewName>
    Given Myself is connected to <Contact>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    And I switch on or off the chathead preview
    And I close the Settings
    And I close self profile
    When I tap on contact name <Contact>
    And I see dialog page
    And Contact <Contact2> sends random message to user <Name>
    Then I do not see chathead of contact <Contact2>
    And I swipe right on Dialog page
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    And I switch on or off the chathead preview
    And I close the Settings
    And I close self profile
    When I tap on contact name <Contact>
    And I see dialog page
    And Contact <Contact2> sends random message to user <Name>
    Then I see chathead of contact <Contact2> 
    And I wait for 5 seconds
    Then I do not see chathead of contact <Contact2>    
    
    Examples: 
      | Name      | Contact   | Contact2  | NewName  | Picture                      |
      | user1Name | user2Name | user3Name | CHATHEAD | aqaPictureContact600_800.jpg |
  
  @regression @id730 @id731
  Scenario Outline: Verify about screen contains all the required information
    Given There is 1 user where <Name> is me
    Given User me change accent color to <Color>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page
    And I see that the About page is colored <Color>
    And I see WireWebsiteButton
    And I see TermsButton
    And I see PrivacyPolicyButton
    And I see BuildNumberText
    And I open PrivacyPolicyPage
    And I see PrivacyPolicyPage
    And I close legal page
    Then I see About page
    And I open TermsOfUsePage
    And I see TermsOfUsePage
    And I close legal page
    Then I see About page
    And I open WireWebsite
    Then I see WireWebsitePage

    Examples: 
      | Name      | Contact   | Color  |
      | user1Name | user2Name | Violet |