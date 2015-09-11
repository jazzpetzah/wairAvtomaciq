Feature: Conversation List

  @regression @rc @id1333
  Scenario Outline: Unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I wait for 30 seconds
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I open archived conversations
    And I tap on contact name <ArchivedUser>
    And I return to the chat list
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Name      | ArchivedUser |
      | user1Name | user2Name    |

  @regression @rc @id1332 @id2171 @id2172
  Scenario Outline: Verify archive a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I click archive button for conversation <Contact>
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id2153
  Scenario Outline: Verify unread dots have different size for 1, 5, 10 incoming messages
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User <Name> change accent color to <Color>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I return to the chat list
    And I tap on contact name <Contact1>
    And I return to the chat list
    Then I dont see unread message indicator in list for contact <Contact>
    And Contact <Contact> send number 1 of message to user <Name>
    Then I see 1 unread message indicator in list for contact <Contact>
    And Contact <Contact> send number 1 of message to user <Name>
    Then I see 5 unread message indicator in list for contact <Contact>
    And Contact <Contact> send number 8 of message to user <Name>
    Then I see 10 unread message indicator in list for contact <Contact>

    Examples: 
      | Name      | Contact   | Contact1  | Color           |
      | user1Name | user2Name | user3Name | StrongLimeGreen |

  @regression @id2040
  Scenario Outline: Verify archive a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I click archive button for conversation <GroupChatName>
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations
    Then I see user <GroupChatName> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @regression @id2041
  Scenario Outline: Verify unarchive a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Myself archived conversation having groupname <GroupChatName>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I open archived conversations
    And I tap on contact name <GroupChatName>
    And I see dialog page
    And I return to the chat list
    Then I see first item in contact list named <GroupChatName>

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @regression @rc @id1369
  Scenario Outline: Verify Ping animation in the conversations list
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When Contact <Contact> ping conversation <Name>
    And I wait for 10 seconds
    Then I see change of state for first conversation cell

    Examples: 
      | Name      | Contact   | NewName | Color        |
      | user1Name | user2Name | PING    | BrightOrange |

  @smoke @id2761
  Scenario Outline: Verify conversations are sorted according to most recent activity
    Given There are 4 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And Contact <Contact3> send number <Number> of message to user <Name>
    And I see first item in contact list named <Contact3>
    When Contact <Contact2> ping conversation <Name>
    And I see first item in contact list named <Contact2>
    When Contact <Contact1> sends image <Picture> to single user conversation <Name>
    Then I see first item in contact list named <Contact1>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Number | Picture     |
      | user1Name | user2Name | user3name | user4name | 2      | testing.jpg |

  @staging @id3310
  Scenario Outline: Verify action menu is opened on swipe right on the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    Then I see conversation <GroupChatName> name in action menu in Contact List
    And I see Silence button in action menu in Contact List
    And I see Archive button in action menu in Contact List
    And I see Delete button in action menu in Contact List
    And I see Leave button in action menu in Contact List
    And I see Cancel button in action menu in Contact List

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3name | ActionMenuChat |

  @staging @id3311
  Scenario Outline: Verify action menu is opened on swipe right on 1to1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    Then I see conversation <Contact> name in action menu in Contact List
    And I see Silence button in action menu in Contact List
    And I see Archive button in action menu in Contact List
    And I see Delete button in action menu in Contact List
    And I see Block button in action menu in Contact List
    And I see Cancel button in action menu in Contact List

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3313
  Scenario Outline: Verify archiving from the action menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I see conversation <Contact> name in action menu in Contact List
    And I see Archive button in action menu in Contact List
    And I press Archive button in action menu in Contact List
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3314
  Scenario Outline: Verify leaving group conversation from the action menu
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I see conversation <GroupChatName> name in action menu in Contact List
    And I see Leave button in action menu in Contact List
    And I press Leave button in action menu in Contact List
    And I see leave conversation alert
    And I press leave
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations
    And I see user <GroupChatName> in contact list
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName   |
      | user1Name | user2Name | user3Name | LeaveActionMenu |
      
  @staging @id3315
  Scenario Outline: Verify removing the content from the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> sent message <Message> to conversation <GroupChatName>
    Given Contact <Contact2> sends image <Image> to group conversation <GroupChatName>
    Given User <Name> sent message <Message> to conversation <GroupChatName>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    Then I see new photo in the dialog
    When I return to the chat list
    And I swipe right on a <GroupChatName>
	And I click delete menu button
	And I confirm delete conversation content
	And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <GroupChatName> and tap on it on People picker page
    Then I see group chat page with users <Contact1>,<Contact2>
    And I see only 1 message

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | Message | Image       |
      | user1Name | user2Name | user3Name | TESTCHAT      | testing | testing.jpg |
      
  @staging @id3318
  Scenario Outline: Verify removing the history from 1-to1 conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> sent message <Message> to conversation <Name>
    Given Contact <Contact1> sends image <Image> to group conversation <Name>
    Given User <Name> sent message <Message> to conversation <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    Then I see new photo in the dialog
    When I return to the chat list
    And I swipe right on a <Contact1>
	And I click delete menu button
	And I confirm delete conversation content
	And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <Contact1> and tap on it on People picker page
    And I click open conversation button on People picker page
    Then I see dialog page
    And I see only 1 message

    Examples: 
      | Name      | Contact1  | Contact2  |Message | Image       |
      | user1Name | user2Name | user3Name |testing | testing.jpg |
