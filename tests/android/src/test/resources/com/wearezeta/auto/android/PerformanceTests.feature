Feature: Performance Tests

  @performance
  Scenario Outline: Normal usage
    Given There are <UsersNumber> shared users with name prefix <UserNamePrefix>
    Given User <Name> is Me
    Given I sign in using my email or phone number
    Given Myself is connected to all other users
    Given I see Contact list with contacts
    When I test conversation loading time for conversation with 300 messages and 30 images
    When I start test cycle for <Time> minutes
    Then I generate performance report for <UsersNumber> users

    Examples: 
      | Name      | UsersNumber | UserNamePrefix | Time |
      | user1Name | 11          | perf           | 5    |
