Feature: Edit

  @C206267 @regression
  Scenario Outline: Verify I can edit my message in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message1>
    Then I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    When I click context menu of the latest message
    And I click to edit message in context menu
    And I delete 8 characters from the conversation input
    And I write message <Message2>
    And I send message
    Then I do not see text message <Message1>
    And I see text message <Message2>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 | Message2       |
      | user1Email | user1Password | user1Name | user2Name | message1 | edited message |

  @C206268 @regression
  Scenario Outline: Verify I can edit my message in group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I write message <Message1>
    Then I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    When I click context menu of the latest message
    And I click to edit message in context menu
    And I delete 8 characters from the conversation input
    And I write message <Message2>
    And I send message
    Then I do not see text message <Message1>
    And I see text message <Message2>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  |Contact2   | ChatName  | Message1 | Message2       |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | message1 | edited message |

  @C206280 @regression
  Scenario Outline: Verify I cannot edit another users message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given Contact <Contact1> sends message <Message1> via device Device1 to group conversation <ChatName>
    And I see text message <Message1>
    And I see 2 messages in conversation
    When I click context menu of the latest message
    And I do not see edit button in context menu for latest message
    When I click context menu of the latest message
    And I see text message <Message1>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  |Contact2   | ChatName  | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | message1 |

  @C206284 @staging
  Scenario Outline: Verify I can edit my last message by pressing the up arrow key
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <OriginalMessage>
    Then I send message
    And I see text message <OriginalMessage>
    And I see 2 messages in conversation
    When I press Up Arrow to edit message
    And I delete 7 characters from the conversation input
    And I write message <EditedMessage>
    And I send message
    Then I do not see text message <OriginalMessage>
    And I see text message <EditedMessage>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | OriginalMessage | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name | edit me         | edited        |

  @C206269 @regression
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

  @C206270 @regression
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

  @C225972 @regression
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

  @C225973 @regression
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

  @C223071 @regression
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
