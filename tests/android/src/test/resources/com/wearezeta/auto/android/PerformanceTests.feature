Feature: Performance Tests

  @performance
  Scenario Outline: Normal usage
    Given There are <UsersNumber> shared users with name prefix <UserNamePrefix>
    Given User <Name> is Me
    Given Myself is connected to all other users
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list with my name <Name>
    When I start test cycle for <Time> minutes
    Then I generate performance report

    Examples: 
      | Login      | Name          | Password      | Name      | UsersNumber | UserNamePrefix | Time  |
      | user1Email | user1Name     | user1Password | user1Name | 11          | perf           | 5     |
