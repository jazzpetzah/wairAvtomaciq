Feature: Settings

  @id482 @regression
  Scenario Outline: Verify user can access settings
    Given I have 1 users and 0 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    Then I see settings page

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  @id729 @regression
  Scenario Outline: Attempt to open About screen in settings
    Given I have 1 users and 0 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  @id1258 @regression
  Scenario Outline: Verify default value for sound settings is all
    Given I have 1 users and 0 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    Then I verify that all is the default selected value

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
