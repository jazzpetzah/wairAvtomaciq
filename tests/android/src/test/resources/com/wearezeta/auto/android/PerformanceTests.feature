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
      | Name      | UsersNumber       | UserNamePrefix    | Time            | MsgsCount | Sender        |
      | user1Name | ${perfUsersCount} | ${userNamePrefix} | ${perfDuration} | 101       | user2Name     |
#      | user1Name | 11                | perf10user         | 2               | 101      | user2Name     |

  @battery_performance
  Scenario Outline: Battery usage while in a call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    And I see Contact list with contacts
    And <Contact> calls me using <CallBackend>
    And I answer the call from the overlay bar
    Then I see calling overlay Big bar
    When I lock the device
    When I initialize battery performance report
    And I verify the call from <Contact> is in progress for <Time> minutes
    Then I generate battery performance report for <Time> minutes

    Examples:
      | Name      | Contact   | Time            | CallBackend |
      | user1Name | user2Name | ${perfDuration} | chrome      |
#      | user1Name | user2Name | 2               | chrome      |
