Feature: Performance Tests

  @performance @torun
  Scenario Outline: Normal usage
    Given There are <UsersNumber> shared users with name prefix <UserNamePrefix>
    Given User <Name> is Me
    Given Myself is connected to all other users
    Given I receive <MsgsCount> messages from contact <Sender>
    Given I sign in using my email
    Given I see Contact list with contacts
    When I start test cycle for <Time> minutes with messages received from <Sender>
    Then I generate performance report for <UsersNumber> users

    Examples: 
      | Name      | UsersNumber       | UserNamePrefix    | Time            | MsgsCount | Sender        |
      | user1Name | ${perfUsersCount} | ${userNamePrefix} | ${perfDuration} | 200       | user2Name     |
#      | user1Name | 11                | perf10user         | 1               | 200      | user2Name     |
      