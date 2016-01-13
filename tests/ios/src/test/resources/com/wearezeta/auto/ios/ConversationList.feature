Feature: Conversation List

  @C836 @regression @rc @id1333
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

  @C352 @regression @rc @id1332 @id2171 @id2172
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

  @C12 @regression @id1334
  Scenario Outline: Verify archiving silenced conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I press menu silence button
    When I swipe right on a <Contact>
    And I press Archive button in action menu in Contact List
    Then I dont see conversation <Contact> in contact list
    Given User <Contact> sends 1 encrypted message to user Myself
    And I dont see conversation <Contact> in contact list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations
    And I tap on contact name <Contact>
    And I see dialog page

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C350 @regression @id2153
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
    Given User <Contact> sends 1 encrypted message to user Myself
    Then I see 1 unread message indicator in list for contact <Contact>
    Given User <Contact> sends 1 encrypted message to user Myself
    Then I see 5 unread message indicator in list for contact <Contact>
    Given User <Contact> sends 8 encrypted messages to user Myself
    Then I see 10 unread message indicator in list for contact <Contact>

    Examples:
      | Name      | Contact   | Contact1  | Color           |
      | user1Name | user2Name | user3Name | StrongLimeGreen |

  @C19 @regression @id2040
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

  @C20 @regression @id2041
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

  @C3177 @regression @rc @id1369
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

  @C104 @regression @id2761
  Scenario Outline: Verify conversations are sorted according to most recent activity
    Given There are 4 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    Given User <Contact3> sends <Number> encrypted messages to user Myself
    And I see first item in contact list named <Contact3>
    When Contact <Contact2> ping conversation <Name>
    And I see first item in contact list named <Contact2>
    Given User <Contact1> sends encrypted image <Picture> to single user conversation Myself
    Then I see first item in contact list named <Contact1>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Number | Picture     |
      | user1Name | user2Name | user3name | user4name | 2      | testing.jpg |

  @C851 @regression @id3310
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

  @C852 @regression @id3311
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

  @C837 @regression @id3313
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

  @C848 @regression @id3314
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

  @C840 @regression @rc @id3315
  Scenario Outline: Verify removing the content from the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with my name <Name>
    Given User <Contact1> sends encrypted image <Picture> to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
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
    Then I see group chat page after deletion with users <Contact1>,<Contact2>
    And I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | TESTCHAT      | testing.jpg |

  @C842 @regression @rc @id3318
  Scenario Outline: Verify removing the history from 1-to1 conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with my name <Name>
    Given User <Contact1> sends encrypted image <Picture> to single user conversation Myself
    Given User <Contact1> sends 1 encrypted message to user <Name>
    Given User Myself sends 1 encrypted message to user <Contact1>
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
    And I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | Message | Picture     |
      | user1Name | user2Name | user3Name | testing | testing.jpg |

  @C853 @regression @id3319
  Scenario Outline: Verify closing the action menu by clicking on cancel on out of the menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    Then I see conversation <Contact> name in action menu in Contact List
    And I see Cancel button in action menu in Contact List
    And I press Cancel button in action menu in Contact List
    Then I see Contact list with my name <Name>

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C850 @staging @id3312
  Scenario Outline: Verify silencing and notify from the action menu
    Given There are 2 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given Myself is connected to <Contact>
    Given User <Contact> change accent color to <Color>
    Given User <Contact> change name to <NewName>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I return to the chat list
    When I swipe right on a <Contact>
    And I press menu silence button
    Then I see conversation <Contact> is silenced
    When I swipe right on a <Contact>
    And I press menu notify button
    Then I see conversation <Contact> is unsilenced

    Examples:
      | Name      | Contact   | Color  | NewName |
      | user1Name | user2Name | Violet | SILENCE |

  @C106 @staging @id3899 @ZIOS-5279
  Scenario Outline: Verify first conversation in the list is highlighted and opened
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> change accent color to BrightOrange
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    Then I see conversation <Contact> is selected in list
    When I swipe left in current window
    Then I see dialog page with contact <Contact>

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C843 @regression @id3954
  Scenario Outline: Verify that deleted conversation isn't going to archive
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with my name <Name>
    Given User <Contact1> sends 1 encrypted message to user <Name>
    Given User Myself sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    And I dont see conversation <Contact1> in contact list
    And I open archived conversations
    Then I dont see conversation <Contact1> in contact list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C844 @staging @id3960
  Scenario Outline: Verify deleting 1-to-1 conversation from archive
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I press Archive button in action menu in Contact List
    And I dont see conversation <Contact1> in contact list
    And I open archived conversations
    And I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <Contact1> in contact list
    And I open archived conversations
    Then I dont see conversation <Contact1> in contact list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C18 @staging @id1481 @ZIOS-5247
  Scenario Outline: Verify removing the content and leaving from the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> sends 1 encrypted message to group conversation <GroupChatName>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I click delete menu button
    And I select Also Leave option on Delete conversation dialog
    And I confirm delete conversation content
    And I open search by taping on it
    And I input conversation name <GroupChatName> in Search input
    Then I see conversation <GroupChatName> is NOT presented in Search results
    When I click close button to dismiss people view
    And I dont see conversation <GroupChatName> in contact list
    And I open archived conversations
    Then I dont see conversation <GroupChatName> in contact list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C846 @rc @regression @id3968
  Scenario Outline: Verify posting in a group conversation without content
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Contact <Name> ping conversation <GroupChatName>
    Given I sign in using my email or phone number
    Given I see Contact list with my name <Name>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends encrypted image <Picture> to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    When I open search by taping on it
    And I input conversation name <GroupChatName> in Search input
    And I see conversation <GroupChatName> is presented in Search results
    And I tap on conversation <GroupChatName> in search result
    Then I see empty group chat page with users <Contact1>,<Contact2> with only system message
    When I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C847 @regression @id4005
  Scenario Outline: Verify deleting the history from kicked out conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> removed <Name> from group chat <GroupChatName>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I see Archive button in action menu in Contact List
    And I see Delete button in action menu in Contact List
    And I see Cancel button in action menu in Contact List
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations
    Then I dont see conversation <GroupChatName> in contact list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | KICKCHAT      |

  @C839 @regression @id4017
  Scenario Outline: Verify canceling blocking person
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I press menu Block button
    And I click Cancel button
    Then I see conversation action menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C838 @regression @id3317
  Scenario Outline: Verify blocking person from action menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I press menu Block button
    And I confirm blocking alert
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations
    And I dont see conversation <Contact> in contact list
    And I open search by taping on it
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C366 @regression @rc @id1075
  Scenario Outline: Verify messages are marked as read with disappearing unread dot
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> change accent color to <Color>
    Given I sign in using my email or phone number
    Given I see Contact list with my name <Name>
    Given User <Contact> sends <Number> encrypted messages to user Myself
    And I see 1 unread message indicator in list for contact <Contact>
    And I tap on contact name <Contact>
    And I see dialog page
    And I return to the chat list
    Then I dont see unread message indicator in list for contact <Contact>

    Examples:
      | Name      | Contact   | Color           | Number |
      | user1Name | user2Name | StrongLimeGreen | 1      |

  @C108 @regression @id4103
  Scenario Outline: Verify 'Invite more people' is hidden after 6 connections
    Given There are <Number> users where <Name> is me
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I see Invite more people button
    And Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>
    And I see Invite more people button
    And Myself is connected to <Contact7>
    Then I DONT see Invite more people button

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | Contact7  | Number |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | user8Name | 8      |