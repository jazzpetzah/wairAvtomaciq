Feature: Sign Out

  @id2266 @staging
  Scenario Outline: Sign out from ZClient in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap options button
    And I tap sign out button
    Then I see welcome screen

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id2251 @staging
  Scenario Outline: Sign out from ZClient in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap options button
    And I tap sign out button
    Then I see welcome screen

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
