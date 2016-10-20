Feature: Ephemeral

  @C261723 @ephemeral @staging
  Scenario Outline: Verify sending ephemeral text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    And I Sign in using login <Login2> and password <Password>
    And I am signed in properly
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    Given I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <TimeShort> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message>
    And I send message
    Then I see text message <Message>
    And I verify the database is containing the message <Message> from <Name> in active conversation
    #And I see timer next to the last message
    When I wait for <Time> seconds
    #Then I do not see timer next to the last message
    And I see the last message is obfuscated
    And I see 2 messages in conversation
    And I verify the database is not containing the message <Message> from <Name> in active conversation
    And I see 1 message in database from <Name> in active conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password>
    And I am signed in properly
    And I open conversation with <Name>
    Then I see text message <Message>
    #And I see timer next to the last message
    When I wait for <Time> seconds
    Then I do not see text message <Message>
    And I see 1 messages in conversation
    And I see 0 message in database from <Name> in active conversation
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
    And I see 0 message in database from <Name> in active conversation

    Examples:
      | Login1     | Password      | Name      | Contact   | Login2     | Time | TimeLong   | TimeShort | Message |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 5    | 5 seconds  | 5s        | Hello   |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 15   | 15 seconds | 15s       | Hello   |
      | user1Email | user1Password | user1Name | user2Name | user2Email | 60   | 1 minute   | 1m        | Hello   |

  @C261728 @ephemeral @staging
  Scenario Outline: Verify switching on/off ephemeral message
    Given There are 2 users where <Name> is me
    #Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <TimeShort> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message1>
    And I send message
    Then I see text message <Message1>
    When I wait for <Time> seconds
    #And I do not see text message <Message1>
    And I click on ephemeral button
    And I set the timer for ephemeral to None
    And I see placeholder of conversation input is Type a message
    And I write message <Message2>
    And I send message
    Then I see text message <Message2>
    When I wait for <Time> seconds
    Then I see text message <Message2>
    And I see the last message is not obfuscated

    Examples:
      | Login1     | Password      | Name      | Contact   | Time | TimeLong   | TimeShort | Message1 | Message2 |
      | user1Email | user1Password | user1Name | user2Name | 5    | 5 seconds  | 5s        | Hello1   | Hello2   |

  @C261727 @ephemeral @staging
  Scenario Outline: Verify ephemeral messages are turned off in a group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    Then I cannot see ephemeral button

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Ephemeral |

  @C262533 @ephemeral @staging
  Scenario Outline: Verify that messages with previous timer are deleted on start-up when the timeout passed
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I click on ephemeral button
    And I set the timer for ephemeral to <TimeLong>
    Then I see <TimeShort> on ephemeral button
    And I see placeholder of conversation input is Timed message
    When I write message <Message>
    And I send message
    Then I see text message <Message>
    When I wait for <Time> seconds
    And I see the last message is obfuscated
    Then I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login2> and password <Password>
    Given I am signed in properly
    When I open conversation with <Name>
    Then I see text message <Message>
    And I wait for <Time> seconds
    And I do not see text message <Message>
    And I see 1 message in conversation
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login2> and password <Password>
    Given I am signed in properly
    When I open conversation with <Name>
    Then I do not see text message <Message>
    And I see 1 message in conversation

    Examples:
      | Login      | Password      | Login2     | Name      | Contact   | TimeLong  | TimeShort | Time | Message |
      | user1Email | user1Password | user2Email | user1Name | user2Name | 5 seconds | 5s        | 5    | testing |