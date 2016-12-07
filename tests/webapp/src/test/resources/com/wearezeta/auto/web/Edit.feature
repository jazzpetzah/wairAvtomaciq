Feature: Edit

  @C206267 @edit @regression
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
    When I click context menu of the last message
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

  @C206268 @edit @regression
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
    When I click context menu of the last message
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

  @C206280 @edit @regression
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
    When I click context menu of the last message
    And I do not see edit button in context menu
    When I click context menu of the last message
    And I see text message <Message1>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  |Contact2   | ChatName  | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | message1 |

  @C206284 @edit @regression
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

  @C206287 @edit @regression
  Scenario Outline: Verify design is correct if I edit a message in between other messages from me
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write random message
    And I send message
    And I see random message in conversation
    And I write message <OriginalMessage>
    And I send message
    And I see text message <OriginalMessage>
    And I write random message
    And I send message
    And I see random message in conversation
    Then I see 4 messages in conversation
    And I do not see message header for second last message
    When I click context menu of the second last message
    And I click to edit message in context menu
    And I delete 7 characters from the conversation input
    And I write message <EditedMessage>
    And I send message
    Then I do not see text message <OriginalMessage>
    And I see text message <EditedMessage>
    And I see 4 messages in conversation
    And I see message header for second last message
    And I do not see message header for last message

    Examples:
      | Login      | Password      | Name      | Contact   | OriginalMessage | EditedMessage |
      | user1Email | user2Password | user1Name | user2Name | edit me         | edited        |

  @C206269 @edit @regression
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

  @C206270 @edit @regression
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

  @C225972 @edit @regression
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
    And Contact <Contact1> sends message <AnotherMessage> via device Device1 to user <Name>
    Then I see text message <AnotherMessage>
    And I see 3 messages in conversation
    When User <Contact1> edits the second last message to "<EditedMessage>" from user <Name> via device Device1
    Then I do not see text message <OriginalMessage>
    And I see text message <AnotherMessage>
    And I see text message <EditedMessage>
    And I see 3 messages in conversation
    And I see latest text message <AnotherMessage>
    And I do not see unread dot in conversation <Contact1>
    When I refresh page
    Then I do not see text message <OriginalMessage>
    And I see text message <AnotherMessage>
    And I see text message <EditedMessage>
    And I see 3 messages in conversation
    And I see latest text message <AnotherMessage>
    And I do not see unread dot in conversation <Contact1>

    Examples:
      | Login      | Password      | Name      | Contact1   | AnotherMessage | OriginalMessage | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name  | something      | edit me         | edited        |

  @C225973 @edit @regression
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

  @C223070 @edit @regression
  Scenario Outline: Verify position and date of edited message
    Given There are 2 users where <Name> is me
    Given <Contact> have unique usernames
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password2>
    Given I am signed in properly
    Given I see watermark
    Given I see first time experience hint
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click logout button on clear data dialog
    When I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I write message <OriginalMessage>
    And I send message
    Then I see text message <OriginalMessage>
    When I write message last message
    And I send message
    Then I see text message last message
    When I click context menu of the second last message
    And I click to edit message in context menu
    And I delete 7 characters from the conversation input
    And I write message <EditedMessage>
    And I send message
    Then I do not see text message <OriginalMessage>
    And I see text message <EditedMessage>
    And I see 3 messages in conversation
    And I see latest text message last message
    When I remember edit timestamp of second last message
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    Then I click logout button on clear data dialog
    When I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    And I open conversation with <Name>
    Then I see latest text message last message
    And I verify the edit timestamp of second last message equals the remembered timestamp

    Examples:
      | Login      | Password      | Login2     | Password2     | Name      | Contact   | OriginalMessage | EditedMessage |
      | user1Email | user1Password | user2Email | user2Password | user1Name | user2Name | edit me         | edited        |

  @C223071 @edit @regression
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

  @C206283 @edit @regression
  Scenario Outline: Verify I can edit a message multiple times in a row
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
    When I click context menu of the last message
    And I click to edit message in context menu
    And I delete 7 characters from the conversation input
    And I write message <EditedMessage1>
    And I send message
    Then I do not see text message <OriginalMessage>
    And I see text message <EditedMessage1>
    And I see 2 messages in conversation
    When I click context menu of the last message
    And I click to edit message in context menu
    And I delete 7 characters from the conversation input
    And I write message <EditedMessage2>
    And I send message
    Then I do not see text message <EditedMessage1>
    And I see text message <EditedMessage2>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | OriginalMessage | EditedMessage1 | EditedMessage2 |
      | user1Email | user1Password | user1Name | user2Name | edit me         | edited1        | edited2        |

  @C206281 @edit @regression
  Scenario Outline: Verify I can cancel editing a message by button
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
    When I click context menu of the last message
    And I click to edit message in context menu
    And I delete 7 characters from the conversation input
    And I write message <EditedMessage>
    And I click x button to close edit mode
    Then I see text message <OriginalMessage>
    And I do not see text message <EditedMessage>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | OriginalMessage | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name | edit me         | edited1       |

  @C206285 @edit @regression
  Scenario Outline: Verify I can switch to edit another message while editing a message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <OriginalMessage>
    Then I send message
    When I write message <OriginalMessage2>
    Then I send message
    And I see text message <OriginalMessage>
    And I see text message <OriginalMessage2>
    And I see 3 messages in conversation
    When I click context menu of the last message
    And I click to edit message in context menu
    And I delete 9 characters from the conversation input
    And I write message <IntermediateMessage>
    When I click context menu of the second last message
    And I click to edit message in context menu
    And I delete 7 characters from the conversation input
    And I write message <EditedMessage>
    When I send message
    Then I see text message <OriginalMessage2>
    And I do not see text message <OriginalMessage>
    And I do not see text message <IntermediateMessage>
    And I see 3 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | OriginalMessage | OriginalMessage2 | IntermediateMessage | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name | edit me         | edit me 2        | abort edit          | edited1       |

  @WEBAPP-3422 @edit @staging
  Scenario Outline: Verify I do not loose messages when someone edits a message from a long time ago
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And Contact <Contact1> sends 10 messages with prefix <Prefix1> via device Device1 to group conversation <ChatName>
    And I remember the message <Prefix1>1
    And Contact <Contact2> sends 10 messages with prefix <Prefix2> via device Device1 to group conversation <ChatName>
    And Contact <Contact1> sends 10 messages with prefix <Prefix3> via device Device1 to group conversation <ChatName>
    And Contact <Contact1> sends 10 messages with prefix <Prefix4> via device Device1 to group conversation <ChatName>
    When I open conversation with <Contact1>
    And User <Contact1> edits the remembered message to "<EditedMessage>" on device Device1
    And I open conversation with <ChatName>
    And I scroll up in the conversation
    Then I see text message <Prefix1>0
    And I see text message <EditedMessage>
    And I do not see text message <Prefix1>1
    And I see text message <Prefix1>2
    And I see text message <Prefix1>9
    And I see text message <Prefix2>0
    And I see text message <Prefix2>9
    And I see text message <Prefix3>0
    And I see text message <Prefix3>9
    And I see text message <Prefix4>0
    And I see text message <Prefix4>9

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName | Prefix1 | Prefix2 | Prefix3 | Prefix4 | EditedMessage |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Editing  | first   | second  | third   | fourth  | edited        |