Feature: Performance Tests

  @noAcceptAlert @performance
  Scenario Outline: Normal usage
    Given There are <UsersNumber> users where <Name> is me
    Given Myself is connected to all other users
    #And I start Activity Monitoring logging <Device ID>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    When I <Login> start test cycle for <Time> minutes

    Examples: 
      | Login     | Password      | UsersNumber | Time | Device ID                                |
      | user1Name | user1Password | 11          | 480  | d1cfac0e274fde4f69fe57dbf82e824458bbe1b5 |
