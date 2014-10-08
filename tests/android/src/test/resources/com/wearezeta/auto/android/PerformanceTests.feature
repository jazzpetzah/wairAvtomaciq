Feature: Performance Tests

  @performance
  Scenario Outline: Normal usage
    Given Generate <UsersNumber> and connect to <Login> contacts
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    When I <Login> start test cycle for <Time> minutes

    Examples: 
      | Login    | Password | UsersNumber | Time |
      | perfUser | perfPass | 10          | 60   |
