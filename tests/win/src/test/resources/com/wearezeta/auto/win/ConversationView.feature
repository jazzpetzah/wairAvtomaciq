Feature: Conversation View

  @C2344 @smoke
  Scenario Outline: Verify I can ping a conversation using the menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Ping"
    Then I see <PING> action in conversation
    And I click menu bar item "Conversation" and menu item "Ping"
    Then I see <PING> action 2 times in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   |
      | user1Email | user1Password | user1Name | user2Name | pinged |


  @C2333 @smoke
   Scenario Outline: Verify you ping in a conversation when you press Ctrl + K
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I type shortcut combination to ping
    And I wait for 30 seconds
    Then I see <PING> action in conversation
    When I type shortcut combination to ping
    And I wait for 5 seconds
    Then I see <PING> action 2 times in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | PING       |
      | user1Email | user1Password | user1Name | user2Name | you pinged |

  @C2345 @smoke
  Scenario Outline: Verify I can call a conversation using the menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Call"
    Then I see the outgoing call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2346 @smoke
  Scenario Outline: Verify I can ping a group conversation using the menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Ping"
    Then I see <PING> action in conversation
    And I click menu bar item "Conversation" and menu item "Ping"
    Then I see <PING> action 2 times in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName  | PING   |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | GroupChat | pinged |


  @C2348 @smoke
  Scenario Outline: Verify you ping in a group conversation when you press Ctrl + K
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <ChatName>
    And I type shortcut combination to ping
    Then I see <PING> action in conversation
    When I type shortcut combination to ping
    Then I see <PING> action 2 times in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName  | PING   |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | GroupChat | pinged |

  @C2347 @smoke
  Scenario Outline: Verify I can call a group conversation using the menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Call"
    Then I see the outgoing call controls for conversation <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName  |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | GroupChat |

  @C2359 @smoke @WEBAPP-2785
  Scenario Outline: Verify I can undo redo using menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message>
    Then I verify that message "<Message>" was typed
    When I click menu bar item "Edit" and menu item "Undo"
    Then I verify that message "T" was typed
    When I click menu bar item "Edit" and menu item "Undo"
    Then I verify that message "" was typed
    When I click menu bar item "Edit" and menu item "Redo"
    Then I verify that message "<RedoMessage>" was typed

    Examples: 
      | Login      | Password      | Name      | Contact   | Message | RedoMessage |
      | user1Email | user1Password | user1Name | user2Name | Test    | T           |

  @C2360 @smoke @WEBAPP-2785
  Scenario Outline: Verify I can undo redo using shortcuts Ctrl Z and Ctrl Shift Z
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message>
    Then I verify that message "<Message>" was typed
    When I type shortcut combination to undo
    And I wait for 1 seconds
    Then I verify that message "T" was typed
    When I type shortcut combination to undo
    And I wait for 1 seconds
    Then I verify that message "" was typed
    When I type shortcut combination to redo
    And I wait for 1 seconds
    Then I verify that message "<RedoMessage>" was typed

    Examples: 
      | Login      | Password      | Name      | Contact   | Message | RedoMessage |
      | user1Email | user1Password | user1Name | user2Name | Test    | T           |

  @C2367 @smoke
  Scenario Outline: Verify I can select all, cut and paste using menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I click menu bar item "Edit" and menu item "Select All"
    And I click menu bar item "Edit" and menu item "Cut"
    Then I verify that message "" was typed
# We can not paste something in automation due to security
#     When I click menu bar item "Edit" and menu item "Paste"
#     Then I verify that random message was typed

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2368 @smoke
  Scenario Outline: Verify I can select all, cut and paste using shortcuts Ctrl A, Ctrl X and Ctrl V
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I type shortcut combination to select all
    And I wait for 1 seconds
    And I type shortcut combination to cut
    And I wait for 1 seconds
    Then I verify that message "" was typed
# We can not paste something in automation due to security
#     When I type shortcut combination to paste
#     Then I verify that random message was typed

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2369 @smoke
  Scenario Outline: Verify I can select all, copy and paste using menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I click menu bar item "Edit" and menu item "Select All"
    And I click menu bar item "Edit" and menu item "Copy"
    Then I verify that random message was typed
# We can not paste something in automation due to security
#     When I click menu bar item "Edit" and menu item "Paste"
#     Then I verify that random message was typed

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2370 @smoke
  Scenario Outline: Verify I can select all, copy and paste using shortcuts Ctrl A, Ctrl C and Ctrl V
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I type shortcut combination to select all
    And I wait for 1 seconds
    And I type shortcut combination to copy
    And I wait for 1 seconds
    Then I verify that random message was typed
# We can not paste something in automation due to security
#     When I type shortcut combination to paste
#     Then I verify that random message was typed

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C183888 @smoke
  Scenario Outline: Send a long message containing new lines in 1on1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I write 10 new lines
    And I write message aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    And I write 10 new lines
    And I write message bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb
    And I write 10 new lines
    And I send message
    Then I verify the last text message equals to <ExpectedMessage>

    Examples: 
      | Login      | Password      | Name      | Contact   | ExpectedMessage                   |
      | user1Email | user1Password | user1Name | user2Name | ('a' * 100)('LF' * 10)('b' * 100) |

  @C206255 @smoke
  Scenario Outline: Verify I can delete my message everywhere (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then I open conversation with <Contact>
    And I write message <Message>
    And I send message
    Then I verify the last text message equals to <Message>
    And I see 2 messages in conversation
    When I open context menu of the last message
    And I click delete everywhere in message context menu
    And I click confirm to delete message for everyone
    Then I do not see text message <Message>
    And I see 1 messages in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | Message   |
      | user1Email | user1Password | user1Name | user2Name | delete me |

  @C206275 @smoke
  Scenario Outline: Verify I can delete my message locally (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then I open conversation with <Contact>
    And I write message <Message>
    And I send message
    Then I verify the last text message equals to <Message>
    And I see 2 messages in conversation
    When I open context menu of the last message
    And I click delete in message context menu
    And I click confirm to delete message for me
    Then I do not see text message <Message>
    And I see 1 messages in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | Message   |
      | user1Email | user1Password | user1Name | user2Name | delete me |

  @C206276 @smoke
  Scenario Outline: Verify I can only locally delete a message from other person (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given user <Contact> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then I open conversation with <Contact>
    And Contact <Contact> sends message <Message> via device Device1 to user <Name>
    Then I verify the last text message equals to <Message>
    And I see 2 messages in conversation
    When I open context menu of the last message
# as long as there is no entry for deleting everywhere this will select delete locally
    And I click delete everywhere in message context menu
    And I click confirm to delete message for me
    Then I do not see text message <Message>
    And I see 1 messages in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | Message   |
      | user1Email | user1Password | user1Name | user2Name | delete me |