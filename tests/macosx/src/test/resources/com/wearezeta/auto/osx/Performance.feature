Feature: Performance

  @performance
  Scenario Outline: Normal usage performance testing
    Given There are <UsersCount> shared users with name prefix <UserNamePrefix>
    Given User <Name> is Me
    Given Myself is connected to all other users
    When I start performance monitoring for OS X
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I start test cycle for <Time> minutes
    Then I finish performance monitoring for OS X

    Examples: 
      | Login      | Name      | Password      | UsersCount        | Time            | UserNamePrefix |
      | user1Email | user1Name | user1Password | ${perfUsersCount} | ${perfDuration} | perf           |
