Feature: Notifications

  @C318637 @notifications @preferences @staging @WEBAPP-3586
  Scenario Outline: Sender name and a message are shown in notification when 'Show sender and message' item is selected in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I listen for notifications
    And I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    Then I see notification setting is set to on
    When I close preferences
    Then Soundfile new_message did not start playing
    When I click next notification from <NotificationSender> with text <ExpectedMessage>
    And Contact <Contact1> sends message <ExpectedMessage> to user Myself
    Then I see text message <ExpectedMessage>
    And Soundfile new_message did start playing
    And I got 1 notification
    And I saw notification from <NotificationSender> with text <ExpectedMessage>
    And I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ExpectedMessage | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DEFAULT         | user2Name          |

  @C395989 @notifications @preferences @staging @WEBAPP-3586
  Scenario Outline: No message content is written on notification when 'Show sender' item is selected in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I listen for notifications
    And I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    And I set notification setting to sender
    Then I see notification setting is set to obfuscate-message
    When I close preferences
    Then Soundfile new_message did not start playing
    When I click next notification from <NotificationSender> with text <ExpectedMessage>
    And Contact <Contact1> sends message <OriginalMessage> to user Myself
    Then I see text message <OriginalMessage>
    And Soundfile new_message did start playing
    And I got 1 notification
    And I saw notification from <NotificationSender> with text <ExpectedMessage>
    And I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage    | ExpectedMessage    | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | user3Name | MESSAGE_OBFUSCATED | Sent you a message | user2Name          |

  @C318638 @notifications @preferences @staging @WEBAPP-3586
  Scenario Outline: No sender name, profile image or message content is written on notification when choose 'Hide details' in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I listen for notifications
    And I open conversation with <Contact2>
    And I open preferences by clicking the gear button
    And I open options in preferences
    And I set notification setting to hide details
    Then I see notification setting is set to obfuscate
    When I close preferences
    Then Soundfile new_message did not start playing
    When I click next notification from <NotificationSender> with text <ExpectedMessage>
    And Contact <Contact1> sends message <OriginalMessage> to user Myself
    Then I see text message <OriginalMessage>
    And Soundfile new_message did start playing
    And I got 1 notification
    And I saw notification from <NotificationSender> with text <ExpectedMessage>
    And I see conversation with <Contact1> is selected in conversations list

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage               | ExpectedMessage    | NotificationSender |
      | user1Email | user1Password | user1Name | user2Name | user3Name | MESSAGE_AND_SENDER_OBFUSCATED | Sent you a message | Someone            |

  @C318639 @notifications @preferences @staging
  Scenario Outline: No notification shown when selecting 'Off' in preferences
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I listen for notifications
    And I open conversation with <Contact2>
    And Soundfile new_message did not start playing
    And I click next notification from <Contact1> with text <OriginalMessage1>
    And Contact <Contact1> sends message <OriginalMessage1> to user Myself
    Then I see text message <OriginalMessage1>
    And Soundfile new_message did start playing
    And I got 1 notification
    When I open preferences by clicking the gear button
    And I open options in preferences
    And I set notification setting to off
    Then I see notification setting is set to none
    When I close preferences
    And I open conversation with <Contact2>
    Then Soundfile new_message did not start playing
    And Contact <Contact1> sends message <OriginalMessage2> to user Myself
    Then Soundfile new_message did start playing
    And I see unread dot in conversation <Contact1>
    When I open conversation with <Contact1>
    Then I see text message <OriginalMessage2>
    And I got 1 notification

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage1 | OriginalMessage2 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DEFAULT          | OFF              |