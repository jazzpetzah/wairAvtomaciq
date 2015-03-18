Feature: Settings

  @id482 @regression
  Scenario Outline: Verify user can access settings
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    Then I see settings page

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id729 @regression
  Scenario Outline: Attempt to open About screen in settings
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

@regression @id862
  Scenario Outline: Verify reset password page is accessible from settings
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
	And I click on Settings button on personal page
	And I click on Settings button from the options menu
	And I click on Change Password button in Settings
	Then I see reset password page

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @id1258 @regression
  Scenario Outline: Verify default value for sound settings is all
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    Then I verify that all is the default selected value

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
      
  @torun @staging @id2074
  Scenario Outline: Verify you can access Help site within the app
  	Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    When I click on Help button from the options menu
    Then I see Support web page
    
    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
  
