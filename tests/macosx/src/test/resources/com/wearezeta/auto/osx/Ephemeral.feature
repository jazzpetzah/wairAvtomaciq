Feature: Ephemeral

  @C264663 @smoke @ephemeral
  Scenario Outline: Verify I can select and unselect all types of ephemeral timers
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password>
    When I am signed in properly
    And I open conversation with <Contact>
    Then I see placeholder of conversation input is Type a message
    When I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <Time> with unit <TimeShortUnit> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I click on ephemeral button
    And I set the timer for ephemeral to OFF
    Then I see placeholder of conversation input is Type a message

    Examples:
      | Login1     | Password      | Name      | Contact   | Time | TimeLong   | TimeShortUnit |
      | user1Email | user1Password | user1Name | user2Name | 5    | 5 seconds  | s             |
      | user1Email | user1Password | user1Name | user2Name | 15   | 15 seconds | s             |
      | user1Email | user1Password | user1Name | user2Name | 30   | 30 seconds | s             |
      | user1Email | user1Password | user1Name | user2Name | 1    | 1 minute   | m             |
      | user1Email | user1Password | user1Name | user2Name | 5    | 5 minutes  | m             |
      | user1Email | user1Password | user1Name | user2Name | 1    | 1 day      | d             |
