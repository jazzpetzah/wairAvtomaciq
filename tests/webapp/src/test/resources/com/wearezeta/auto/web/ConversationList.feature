Feature: Conversation List

  @C1690 @regression
  Scenario Outline: Archive and unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    And I send picture <PictureName> to the current conversation
    And I write random message
    And I send message
    And I click ping button
    And I send 1KB sized file with name <File> to the current conversation
    And I wait until file <File> is uploaded completely
    And I archive conversation <Contact>
    Then I do not see Contact list with name <Contact>
    When I open archive
    And I unarchive conversation <Contact>
    Then I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see random message in conversation
    And I see only 1 picture in the conversation
    And I see <PING> action 1 times in conversation
    And I see file transfer for file <File> in the conversation view
    And I see 5 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | PictureName               | PING       | File        |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg | you pinged | example.txt |

  @C1757 @smoke
  Scenario Outline: Mute 1on1 conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact1>
    When I set muted state for conversation <Contact1>
    And I open conversation with <Contact2>
    Then I see that conversation <Contact1> is muted

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @C1758 @regression
  Scenario Outline: Unmute 1on1 conversation
    Given There are 2 users where <Name> is me
    Given user <Name> adds a new device SecondDevice with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    Given I muted conversation with user <Contact> on device SecondDevice
    And I see that conversation <Contact> is muted
    When I set unmuted state for conversation <Contact>
    And I open preferences by clicking the gear button
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C1721 @regression
  Scenario Outline: Verify Ping icon in the conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open preferences by clicking the gear button
    When User <Contact> pinged in the conversation with <Name>
    And I see ping icon in conversation with <Contact>
    Then I verify ping icon in conversation with <Contact> has <ColorName> color

    Examples: 
      | Login      | Password      | Name      | ColorName  | Contact   |
      | user1Email | user1Password | user1Name | StrongBlue | user2Name |

  @C1796 @regression
  Scenario Outline: Verify you mute the conversation when you press ⌥⇧⌘S (Mac) or alt + ctrl + S (Win)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <Contact>
    And I see that conversation <Contact> is not muted
    When I click on options button for conversation <Contact>
    Then I see correct tooltip for mute button in options popover
    When I type shortcut combination to mute or unmute
    Then I see that conversation <Contact> is muted
    When I type shortcut combination to mute or unmute
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C1804 @regression
  Scenario Outline: Verify I can cancel blocking a 1:1 conversation from conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <Contact>
    When I click on options button for conversation <Contact>
    And I click the option to block in the options popover
    Then I see a block warning modal
    And I click cancel button in the block warning
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C1807 @regression
  Scenario Outline: Verify I can cancel leaving a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <ChatName>
    When I click on options button for conversation <ChatName>
    And I click the option to leave in the options popover
    Then I see a leave warning modal
    And I click cancel button in the leave warning
    Then I see Contact list with name <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName       |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat |

  @C1810 @regression
  Scenario Outline: Verify I can delete a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <ChatName>
    When I click on options button for conversation <ChatName>
    And I click delete in the options popover
    Then I see a delete warning modal for group conversations
    And I click delete button in the delete warning for group conversations
    Then I do not see Contact list with name <ChatName>
    When I open search by clicking the people button
    And I type <ChatName> in search field of People Picker
    Then I see group conversation <ChatName> found in People Picker
    And I close People Picker
    When Contact <Contact1> sends message <Msg1> to group conversation <ChatName>
    Then I see Contact list with name <ChatName>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And User <Contact1> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see the history info page
    And I click confirm on history info page
    And I am signed in properly
    When I open conversation with <ChatName>
    Then I do not see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName        | Msg1    | Login2     | Password2     | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DeleteGroupChat | message | user2Email | user2Password | LEFT    |

  @C1803 @regression
  Scenario Outline: Verify I can block a 1:1 conversation from conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> changes avatar picture to default
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password2>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click Logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <Contact>
    When I click on options button for conversation <Contact>
    And I click the option to block in the options popover
    Then I see a block warning modal
    And I click block button in the block warning
    Then I do not see Contact list with name <Contact>
    When Contact <Contact> sends message <Msg1> to user <Name>
    Then I do not see Contact list with name <Contact>
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And User <Contact> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    And I open conversation with <Name>
    Then I do not see <Action> action for <Name> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | Login2     | Password2     | Msg1    | Action |
      | user1Email | user1Password | user1Name | user2Name | user2Email | user2Password | message | LEFT   |

  @C1806 @regression
  Scenario Outline: Verify I can leave a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given User <Contact1> changes avatar picture to default
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password2>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click Logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <ChatName>
    When I click on options button for conversation <ChatName>
    And I click the option to leave in the options popover
    And I see a leave warning modal
    And I click leave button in the leave warning
    Then I do not see Contact list with name <ChatName>
    And I see Archive button at the bottom of my Contact list
    When I open archive
    And I unarchive conversation <ChatName>
    Then I do not see Archive button at the bottom of my Contact list
    And I see <Action1> action in conversation
    When Contact <Contact1> sends message <Msg1> to group conversation <ChatName>
    Then I do not see text message <Msg1>
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And User <Contact1> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    And I open conversation with <ChatName>
    Then I see <Action2> action for <Name> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName        | Msg1    | Login2     | Password2     | Action1 | Action2 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DeleteGroupChat | message | user2Email | user2Password | YOU LEFT   | LEFT |

  @C1812 @regression
  Scenario Outline: Verify I see picture, ping and call after I delete a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <ChatName>
    When I click on options button for conversation <ChatName>
    And I click delete in the options popover
    And I see a delete warning modal for group conversations
    And I click delete button in the delete warning for group conversations
    And I do not see Contact list with name <ChatName>
    And Contact <Contact1> sends message <Message> to group conversation <ChatName>
    And I open conversation with <ChatName>
    Then I see <Action> action for <Contact1> in conversation
    And I see <Action> action for <Contact2> in conversation
    And I see text message <Message>
    When I click on options button for conversation <ChatName>
    And I click delete in the options popover
    And I see a delete warning modal for group conversations
    And I click delete button in the delete warning for group conversations
    And I do not see Contact list with name <ChatName>
    And User <Contact1> pinged in the conversation with <ChatName>
    And I open conversation with <ChatName>
    Then I see <Action> action for <Contact1> in conversation
    And I see <Action> action for <Contact2> in conversation
    And I see <PING> action for <Contact1> in conversation
    When I click on options button for conversation <ChatName>
    And I click delete in the options popover
    And I see a delete warning modal for group conversations
    And I click delete button in the delete warning for group conversations
    And I do not see Contact list with name <ChatName>
    And User <Contact1> sends image <Image> to group conversation <ChatName>
    And I open conversation with <ChatName>
    Then I see <Action> action for <Contact1> in conversation
    And I see <Action> action for <Contact2> in conversation
    And I see sent picture <Image> in the conversation view
    When I click on options button for conversation <ChatName>
    And I click delete in the options popover
    And I see a delete warning modal for group conversations
    And I click delete button in the delete warning for group conversations
    And I do not see Contact list with name <ChatName>
    And <Contact1> calls <ChatName>
    And I open conversation with <ChatName>
    Then I see <Action> action for <Contact1> in conversation
    And I see <Action> action for <Contact2> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName        | Message | Action | PING   | Image                     | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DeleteGroupChat | hello   | START  | pinged | userpicture_landscape.jpg | zcall       |

  @C1814 @regression
  Scenario Outline: Verify I can delete and leave a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given User <Contact1> changes avatar picture to default
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password2>
    Given I am signed in properly
    Given I open preferences by clicking the gear button
    Given I click logout in account preferences
    Given I see the clear data dialog
    Given I click Logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <ChatName>
    When I click on options button for conversation <ChatName>
    And I click delete in the options popover
    Then I see a delete warning modal for group conversations
    When I click Leave checkbox on a delete warning modal for group conversations
    And I click delete button in the delete warning for group conversations
    Then I do not see Contact list with name <ChatName>
    When I open search by clicking the people button
    And I type <ChatName> in search field of People Picker
    Then I do not see group conversation <ChatName> found in People Picker
    And I close People Picker
    When Contact <Contact1> sends message <Msg1> to group conversation <ChatName>
    Then I do not see Contact list with name <ChatName>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And User <Contact1> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    When I open conversation with <ChatName>
    Then I see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName        | Msg1    | Login2     | Password2     | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DeleteGroupChat | message | user2Email | user2Password | LEFT    |

  @C1811 @regression
  Scenario Outline: Verify I can cancel deleting a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <ChatName>
    When I click on options button for conversation <ChatName>
    And I click delete in the options popover
    Then I see a delete warning modal for group conversations
    And I click cancel button in the delete warning for group conversations
    Then I see Contact list with name <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName        |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DeleteGroupChat |

  @C1808 @regression
  Scenario Outline: Verify I can delete a 1:1 conversation from conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <Contact1>
    When Contact <Contact1> sends message <Msg1> to group conversation <Contact1>
    Then I see text message <Msg1>
    When I click on options button for conversation <Contact1>
    And I click delete in the options popover
    Then I see a delete warning modal for 1:1 conversations
    And I click delete button in the delete warning for 1:1 conversations
    Then I do not see Contact list with name <Contact1>
    When I open search by clicking the people button
    And I type <Contact1> in search field of People Picker
    Then I see user <Contact1> found in People Picker
    And I close People Picker
    When Contact <Contact1> sends message <Msg2> to group conversation <Contact1>
    Then I see Contact list with name <Contact1>
    And I open conversation with <Contact1>
    And I see text message <Msg2>
    And I do not see text message <Msg1>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And User <Contact1> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see the history info page
    And I click confirm on history info page
    And I am signed in properly
    When I open conversation with <Name>
    Then I do not see <Action> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Action | Msg1   | Msg2   |Login2     | Password2     |
      | user1Email | user1Password | user1Name | user2Name | LEFT   | hello1 | hello2 | user2Email | user2Password |

  @C1809 @regression
  Scenario Outline: Verify I can cancel deleting a 1:1 conversation from conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <Contact1>
    When I click on options button for conversation <Contact1>
    And I click delete in the options popover
    Then I see a delete warning modal for 1:1 conversations
    And I click cancel button in the delete warning for 1:1 conversations
    Then I see Contact list with name <Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Msg1    |
      | user1Email | user1Password | user1Name | user2Name | message |

  @C58607 @regression
  Scenario Outline: Verify the order of conversation list is synced between devices
    Given There are 5 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact2>
    And I write random message
    And I send message
    And I see random message in conversation
    And I open conversation with <Contact1>
    And I write random message
    And I send message
    And I see random message in conversation
    Then I see conversation <Contact1> is on the top
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    Then I see Sign In page
    When I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    And I open conversation with <Name>
    And user <Name> adds a new device Device1 with label Label1
    And Contact <Name> sends message <Message> via device Device1 to user <Contact2>
    Then I see text message <Message>
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    Then I see Sign In page
    When I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I see Contact list with name <Contact2>
    Then I see conversation <Contact2> is on the top

    Examples:
      | Login      | Password      | Login2     | Password2     | Name      | Contact1  | Contact2  | Message |
      | user1Email | user1Password | user3Email | user3Password | user1Name | user2Name | user3Name | TESTING |
