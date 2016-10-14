Feature: Ephemeral

  @C261723 @staging
  Scenario Outline: Verify sending ephemeral text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral icon to set the timer
    And I set the timer for ephemeral to <TimeLong>
    Then I see <TimeShort> on ephemeral icon
    And I see placeholder of conversation input is Timed message
    When I write message <Message>
    And I send message
    Then I see text message <Message>
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I do not see timer next to the last message
    And I see the last message is obfuscated
    And I see 2 messages in conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password>
    And I am signed in properly
    And I open conversation with <Name>
    Then I see text message <Message>
    And I see timer next to the last message
    When I wait for <Time> seconds
    Then I do not see text message <Message>
    And I see 1 messages in conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    Then I do not see text message <Message>
    And I see 1 messages in conversation

    Examples:
      | Login1     | Password      | Name      | Contact   | Login2     | Time | TimeLong   | TimeShort | Message |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 5    | 5 seconds  | 5s        | Hello   |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 15   | 15 seconds | 15s       | Hello   |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 60   | 1 minute   | 1m        | Hello   |