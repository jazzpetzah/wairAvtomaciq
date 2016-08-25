Feature: Edit

  @C206269 @staging
  Scenario Outline: Verify I see changed message if message was edited from another device (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given user Myself adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <Contact1>
    And I write message <OriginalMessage>
    And I send message
    Then I see text message <OriginalMessage>
    And I see 2 messages in conversation
    When User Myself edits the recent message to "<EditedMessage>" from user <Contact1> via device Device1
    Then I do not see text message <OriginalMessage>
    And I see text message <EditedMessage>
    And I see 2 messages in conversation
    And I do not see unread dot in conversation <Contact1>

    Examples:
      | Login      | Password      | Name      | Contact1   | OriginalMessage | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name  | edit me         | edited        |


  @C206270 @staging
  Scenario Outline: Verify I see changed message if message was edited from another device (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given user Myself adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I write message <OriginalMessage>
    And I send message
    Then I see text message <OriginalMessage>
    And I see 2 messages in conversation
    When User Myself edits the recent message to "<EditedMessage>" from group conversation <ChatName> via device Device1
    Then I do not see text message <OriginalMessage>
    And I see text message <EditedMessage>
    And I see 2 messages in conversation
    And I do not see unread dot in conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName | OriginalMessage | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | GC1      | edit me         | edited        |

  @C225972 @staging
  Scenario Outline: Verify I see changed message if message was edited from another user (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact1>
    And Contact <Contact1> sends message <OriginalMessage> via device Device1 to user <Name>
    Then I see text message <OriginalMessage>
    And I see 2 messages in conversation
    When User <Contact1> edits the recent message to "<EditedMessage>" from user <Name> via device Device1
    Then I do not see text message <OriginalMessage>
    And I see text message <EditedMessage>
    And I see 2 messages in conversation
    And I do not see unread dot in conversation <Contact1>

    Examples:
      | Login      | Password      | Name      | Contact1   | OriginalMessage | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name  | edit me         | edited        |


  @C225973 @staging
  Scenario Outline: Verify I see changed message if message was edited from another user (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And Contact <Contact1> sends message <OriginalMessage> via device Device1 to group conversation <ChatName>
    Then I see text message <OriginalMessage>
    And I see 2 messages in conversation
    When User <Contact1> edits the recent message to "<EditedMessage>" from group conversation <ChatName> via device Device1
    Then I do not see text message <OriginalMessage>
    And I see text message <EditedMessage>
    And I see 2 messages in conversation
    And I do not see unread dot in conversation <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName | OriginalMessage | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | GC1      | edit me         | edited        |

  @C223071 @staging
  Scenario Outline: Verify editing a message does not create unread dot on receiver side
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact1>
    And Contact <Contact1> sends message <OriginalMessage> via device Device1 to user <Name>
    Then I see text message <OriginalMessage>
    When I open conversation with <Contact2>
    When User <Contact1> edits the recent message to "<EditedMessage>" from user <Name> via device Device1
    And I wait for 5 seconds
    Then I do not see unread dot in conversation <Contact1>
    When I open conversation with <Contact1>
    Then I see text message <EditedMessage>
    And I do not see text message <OriginalMessage>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | OriginalMessage | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name | user3Name | edit me         | edited        |
