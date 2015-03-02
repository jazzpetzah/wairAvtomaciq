Feature: Performance Tests

  @performance
  Scenario Outline: Normal usage
    Given There are <UsersCount> shared users with name prefix <UserNamePrefix>
    Given User <Name> is Me
    Given Myself is connected to all other users
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I start test cycle for <Time> minutes

    Examples: 
      | Login      | Name      | Password      | UsersCount        | Time            | UserNamePrefix |
      | user1Email | user1Name | user1Password | ${perfUsersCount} | ${perfDuration} | perf           |
