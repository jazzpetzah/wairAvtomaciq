Feature: Sign Out

  @id329 @smoke
  Scenario Outline: Sign out from ZClient
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap options button
    And I tap sign out button
    Then I see welcome screen

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
