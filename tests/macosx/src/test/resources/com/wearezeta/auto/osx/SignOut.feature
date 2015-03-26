Feature: Sign Out

  #not supported functionality - Sign Out
  @smoke @id691
  Scenario Outline: Sign out from ZClient
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I sign out
    Then I have returned to Sign In screen

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
