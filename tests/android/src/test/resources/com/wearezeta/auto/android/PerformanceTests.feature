Feature: Performance Tests

  @performance
  Scenario Outline: Normal usage
    Given There are <UsersNumber> users where <Name> is me
    Given Myself is connected to all other users
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I <Login> start test cycle for <Time> minutes

    Examples: 
      | Login      | Password      | Name      | UsersNumber | Time  |
      | user1Email | user1Password | user1Name | 11          | 60    |
