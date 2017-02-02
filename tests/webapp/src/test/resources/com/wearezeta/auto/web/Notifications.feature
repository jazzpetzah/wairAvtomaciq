Feature: Notifications

  @C318637 @staging @WEBAPP-3586
  Scenario Outline: Sender name and a message are shown in notification when 'Show sender and message' item is selected in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I listen for notifications
    Then I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    Then I see notification setting is set to on
    Then I click next notification from <NotificationSender> with text <ExpectedMessage>
    When Contact <Contact1> sends message <ExpectedMessage> to user Myself
    Then I see text message <ExpectedMessage>
    Then I saw notification from <NotificationSender> with text <ExpectedMessage>
    Then I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ExpectedMessage | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DEFAULT         | user2Name          |

  @C395989 @staging @WEBAPP-3586
  Scenario Outline: No message content is written on notification when 'Show sender' item is selected in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I listen for notifications
    Then I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    When I set notification setting to sender
    Then I see notification setting is set to obfuscate-message
    Then I click next notification from <NotificationSender> with text <ExpectedMessage>
    When Contact <Contact1> sends message <OriginalMessage> to user Myself
    Then I see text message <OriginalMessage>
    Then I saw notification from <NotificationSender> with text <ExpectedMessage>
    Then I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage    | ExpectedMessage    | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | user3Name | MESSAGE_OBFUSCATED | Sent you a message | user2Name          |

  @C318638 @staging @WEBAPP-3586
  Scenario Outline: No sender name, profile image or message content is written on notification when choose 'Hide details' in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I listen for notifications
    Then I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    When I set notification setting to hide details
    Then I see notification setting is set to obfuscate
    Then I click next notification from <NotificationSender> with text <ExpectedMessage>
    When Contact <Contact1> sends message <OriginalMessage> to user Myself
    Then I see text message <OriginalMessage>
    Then I saw notification from <NotificationSender> with text <ExpectedMessage>
    Then I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage               | ExpectedMessage    | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | user3Name | MESSAGE_AND_SENDER_OBFUSCATED | Sent you a message | Someone            |