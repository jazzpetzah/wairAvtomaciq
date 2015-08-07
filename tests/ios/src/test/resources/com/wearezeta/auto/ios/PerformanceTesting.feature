Feature: Performance Tests

  @performance
  Scenario Outline: Normal usage
    Given There are <UsersNumber> shared users with name prefix <UserNamePrefix>
    Given User <Name> is Me
    Given Myself is connected to all other users
    Given I receive <MsgsCount> messages from contact <Sender>
    Given I sign in using my email
    When I start test cycle for <Time> minutes with messages received from <Sender>
    Then I generate performance report for <UsersNumber> users

    Examples: 
      | Name      | UsersNumber       | UserNamePrefix    | Time            | MsgsCount | Sender    |
      | user1Name | ${perfUsersCount} | ${userNamePrefix} | ${perfDuration} | 51        | user3Name |
