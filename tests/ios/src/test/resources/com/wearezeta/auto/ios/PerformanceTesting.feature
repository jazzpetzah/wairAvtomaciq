Feature: Performance Tests

  @performance
  Scenario Outline: Normal usage
    Given There are <UsersNumber> shared users with name prefix <UserNamePrefix>
    Given User <Name> is Me
    Given Myself is connected to all other users
    # And I start Activity Monitoring logging <Device ID>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I <Name> start test cycle for <Time> minutes

    Examples: 
      | Login      | Name      | Password      | UsersNumber | Time | UserNamePrefix | Device ID                                |
      | user1Email | user1Name | user1Password | 11          | 5    | perf           | d1cfac0e274fde4f69fe57dbf82e824458bbe1b5 |
