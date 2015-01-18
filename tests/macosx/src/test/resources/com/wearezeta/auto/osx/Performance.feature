Feature: Performance

  @performance
  Scenario Outline: Normal usage performance testing
    Given There are <UsersNumber> shared users with name prefix <UserNamePrefix>
    Given User <Name> is Me
    Given Myself is connected to all other users
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Login> in Contact list
    And I <Login> start testing cycle for <Time> minutes

    Examples: 
      | Login      | Password      | Name      | UsersNumber | UserNamePrefix | Time  |
      | user1Email | user1Password | user1Name | 11          | perf           | 720   |
