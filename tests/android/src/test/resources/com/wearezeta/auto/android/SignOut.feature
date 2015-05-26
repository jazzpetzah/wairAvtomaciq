Feature: Sign Out

  @id329 @smoke
  Scenario Outline: Sign out from ZClient
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on my avatar
    And I tap options button
    And I tap sign out button
    Then I see welcome screen

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
