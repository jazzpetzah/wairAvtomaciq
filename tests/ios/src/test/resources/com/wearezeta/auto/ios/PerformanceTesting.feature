Feature: Performance Tests

  @performance
  Scenario Outline: Normal usage
    Given There are <UsersCount> shared users with name prefix <UserNamePrefix>
    Given User <Name> is Me
    Given Myself is connected to all other users
    When I start performance monitoring for the connected iPhone
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I test conversation loading time for conversation with 300 messages and 30 images
    And I start test cycle for <Time> minutes
    Then I finish performance monitoring for the connected iPhone
    And I generate performance report for <UsersNumber> users

    Examples: 
      | Login      | Name      | Password      | UsersCount        | Time            | UserNamePrefix |
      | user1Email | user1Name | user1Password | ${perfUsersCount} | ${perfDuration} | perf           |
