Feature: Conversation List

  @C836 @rc @regression @id1333
  Scenario Outline: Unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open archived conversations
    And I tap on contact name <ArchivedUser>
    And I navigate back to conversations list
    Then I see first item in contact list named <ArchivedUser>

    Examples:
      | Name      | ArchivedUser |
      | user1Name | user2Name    |

  @C352 @rc @clumsy @regression @id1332 @id2171 @id2172
  Scenario Outline: Verify archive a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <Contact>
    And I tap Archive action button
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C12 @regression @id1334
  Scenario Outline: Verify archiving silenced conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <Contact>
    And I tap Silence action button
    When I swipe right on a <Contact>
    And I tap Archive action button
    Then I do not see conversation <Contact> in conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    And I do not see conversation <Contact> in conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    And I tap on contact name <Contact>
    And I see conversation view page

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C350 @regression @id2153
  Scenario Outline: Verify unread dots have different size for 1, 5, 10 incoming messages
    Given There are 2 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I navigate back to conversations list
    And I remember the state of <Contact> conversation item
    When User <Contact> sends 1 encrypted message to user Myself
    Then I see the state of <Contact> conversation item is changed
    When I remember the state of <Contact> conversation item
    And User <Contact> sends 4 encrypted message to user Myself
    Then I see the state of <Contact> conversation item is changed
    When I remember the state of <Contact> conversation item
    Given User <Contact> sends 5 encrypted messages to user Myself
    Then I see the state of <Contact> conversation item is changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C19 @regression @id2040
  Scenario Outline: Verify archive a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <GroupChatName>
    And I tap Archive action button
    Then I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    Then I see conversation <GroupChatName> in conversations list

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
    Given I see conversations list
    When I open archived conversations
    And I tap on contact name <GroupChatName>
    And I see conversation view page
    And I navigate back to conversations list
    Then I see first item in contact list named <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @C104 @regression @id2761
  Scenario Outline: Verify conversations are sorted according to most recent activity
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact3> sends 1 encrypted message to user Myself
    And I see first item in contact list named <Contact3>
    Given User <Contact2> securely pings conversation Myself
    And I see first item in contact list named <Contact2>
    Given User <Contact1> sends encrypted image <Picture> to single user conversation Myself
    Then I see first item in contact list named <Contact1>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Picture     |
      | user1Name | user2Name | user3name | user4name | testing.jpg |

  @C851 @regression @id3310
  Scenario Outline: Verify action menu is opened on swipe right on the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <GroupChatName>
    Then I see conversation <GroupChatName> name in action menu in Contact List
    And I see Silence action button
    And I see Archive action button
    And I see Delete action button
    And I see Leave action button
    And I see Cancel action button

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3name | ActionMenuChat |

  @C852 @regression @id3311
  Scenario Outline: Verify action menu is opened on swipe right on 1to1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <Contact>
    Then I see conversation <Contact> name in action menu in Contact List
    And I see Silence action button
    And I see Archive action button
    And I see Delete action button
    And I see Block action button
    And I see Cancel action button

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C837 @regression @id3313
  Scenario Outline: Verify archiving from the action menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <Contact>
    And I see conversation <Contact> name in action menu in Contact List
    And I see Archive action button
    And I tap Archive action button
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C848 @rc @clumsy @regression @id3314
  Scenario Outline: Verify leaving group conversation from the action menu
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <GroupChatName>
    And I see conversation <GroupChatName> name in action menu in Contact List
    And I see Leave action button
    And I tap Leave action button
    And I see leave conversation alert
    And I press leave
    Then I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    And I see conversation <GroupChatName> in conversations list
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName   |
      | user1Name | user2Name | user3Name | LeaveActionMenu |

  @C840 @rc @clumsy @regression @id3315
  Scenario Outline: Verify removing the content from the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends encrypted image <Picture> to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on contact name <GroupChatName>
    Then I see 1 photo in the dialog
    When I navigate back to conversations list
    And I swipe right on a <GroupChatName>
    And I tap Delete action button
    And I confirm delete conversation content
    And I open search UI
    And I input in People picker search field conversation name <GroupChatName>
    And I tap on conversation <GroupChatName> in search result
    Then I see group chat page with users <Contact1>,<Contact2>
    And I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | TESTCHAT      | testing.jpg |

  @C842 @rc @clumsy @regression @id3318
  Scenario Outline: Verify removing the history from 1-to1 conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends encrypted image <Picture> to single user conversation Myself
    Given User <Contact1> sends 1 encrypted message to user <Name>
    Given User Myself sends 1 encrypted message to user <Contact1>
    When I tap on contact name <Contact1>
    Then I see 1 photo in the dialog
    When I navigate back to conversations list
    And I swipe right on a <Contact1>
    And I tap Delete action button
    And I confirm delete conversation content
    And I open search UI
    And I input in People picker search field conversation name <Contact1>
    And I tap on conversation <Contact1> in search result
    And I tap Open conversation action button on People picker page
    Then I see conversation view page
    And I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | Picture     |
      | user1Name | user2Name | user3Name | testing.jpg |

  @C853 @regression @id3319
  Scenario Outline: Verify closing the action menu by clicking on cancel on out of the menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <Contact>
    Then I see conversation <Contact> name in action menu in Contact List
    And I see Cancel action button
    And I tap Cancel action button
    Then I see conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C850 @regression @id3312
  Scenario Outline: Verify silencing and notify from the action menu
    Given There are 2 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I remember the state of <Contact> conversation item
    And I tap on contact name <Contact>
    And I navigate back to conversations list
    When I swipe right on a <Contact>
    And I tap Silence action button
    Then I see the state of <Contact> conversation item is changed
    When I remember the state of <Contact> conversation item
    And I swipe right on a <Contact>
    And I tap Notify action button
    Then I see the state of <Contact> conversation item is changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C106 @regression @id3899
  Scenario Outline: Verify first conversation in the list is highlighted and opened
    Given There are 3 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I remember the state of conversation item number 1
    And I tap on conversation item number 2
    And I navigate back to conversations list
    Then I see the state of conversation item number 1 is not changed

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C843 @regression @id3954
  Scenario Outline: Verify that deleted conversation isn't going to archive
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to user <Name>
    Given User Myself sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I tap Delete action button
    And I confirm delete conversation content
    Then I do not see conversation <Contact1> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C844 @regression @id3960
  Scenario Outline: Verify deleting 1-to-1 conversation from archive
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <Contact1>
    And I tap Archive action button
    And I do not see conversation <Contact1> in conversations list
    And I open archived conversations
    And I swipe right on a <Contact1>
    And I tap Delete action button
    And I confirm delete conversation content
    Then I do not see conversation <Contact1> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C841 @regression @id3316
  Scenario Outline: Verify removing the content and leaving from the group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I tap Delete action button
    And I select Also Leave option on Delete conversation dialog
    And I confirm delete conversation content
    And I open search UI
    And I input in People picker search field conversation name <GroupChatName>
    Then I see the conversation "<GroupChatName>" does not exist in Search results
    When I click close button to dismiss people view
    And I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    Then I see conversation <GroupChatName> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C846 @rc @regression @id3968
  Scenario Outline: Verify posting in a group conversation without content
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself securely pings conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends encrypted image <Picture> to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I tap Delete action button
    And I confirm delete conversation content
    Then I do not see conversation <GroupChatName> in conversations list
    When I open search UI
    And I input in People picker search field conversation name <GroupChatName>
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
    Given I see conversations list
    When I swipe right on a <GroupChatName>
    And I see Archive action button
    And I see Delete action button
    And I see Cancel action button
    And I tap Delete action button
    And I confirm delete conversation content
    Then I do not see conversation <GroupChatName> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | KICKCHAT      |

  @C839 @regression @id4017
  Scenario Outline: Verify canceling blocking person
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <Contact1>
    And I tap Block action button
    And I tap Cancel action button
    Then I see conversation action menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C838 @regression @id3317
  Scenario Outline: Verify blocking person from action menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I swipe right on a <Contact>
    And I tap Block action button
    And I confirm blocking alert
    Then I do not see conversation <Contact> in conversations list
    And I do not see Archive button at the bottom of conversations list
    And I open search UI
    And I input in People picker search field user name <Contact>
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C366 @rc @regression @id1075
  Scenario Outline: Verify messages are marked as read with disappearing unread dot
    Given There are 2 users where <Name> is me
    Given User Myself removes his avatar picture
    Given <Contact> is connected to <Name>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends 1 encrypted messages to user Myself
    When I remember the state of <Contact> conversation item
    And I tap on contact name <Contact>
    And I see conversation view page
    And I navigate back to conversations list
    Then I see the state of <Contact> conversation item is changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C854 @regression
  Scenario Outline: Verify action menu is opened on swipe right on outgoing connection request
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I see first item in contact list named <Contact>
    When I swipe right on a <Contact>
    Then I see conversation <Contact> name in action menu in Contact List
    And I see Archive action button
    And I see Cancel action button

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C109 @noAcceptAlert @regression
  Scenario Outline: Verify share contacts dialogue is shown each time on invite more friends click
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I see sign in screen
    Given I switch to Log In tab
    Given I have entered login <Login>
    Given I have entered password <Password>
    Given I press Login button
    Given I dismiss alert
    Given I accept First Time overlay if it is visible
    Given I dismiss alert
    Given I dismiss settings warning
    When I see conversations list
    And I open search UI
    And I tap Invite more people button
    Then I see Share Contacts settings warning
    And I dismiss settings warning
    And I tap Cancel button to not Invite more people
    And I tap Invite more people button
    Then I see Share Contacts settings warning

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @C95627 @regression
  Scenario Outline: Verify deleting a conversation is synchronised to all devices
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I see conversation <Contact1> in conversations list
    When User Myself adds new device <DeviceName>
    And User <Contact1> sends 1 encrypted message to user Myself
    And User Myself deletes single user conversation <Contact1> using device <DeviceName>
    # Let the stuff to sync up
    Then I wait up to <Timeout> seconds until conversation <Contact1> disappears from the list

    Examples:
      | Name      | Contact1  | DeviceName | Timeout |
      | user1Name | user2Name | device1    | 15      |

  @C82827 @regression
  Scenario Outline: Verify archive behaviour when one archive/unarchive a conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I sign in using my email or phone number
    Given I see conversations list
    When User <ArchivedUser> sends 1 message to user Myself
    Then I see conversation <ArchivedUser> in conversations list
    And I see Contacts label at the bottom of conversations list
    And I do not see Archive button at the bottom of conversations list
    When Myself archived conversation with <ArchivedUser>
    Then I do not see conversation <ArchivedUser> in conversations list
    And I do not see Contacts label at the bottom of conversations list
    And I see Archive button at the bottom of conversations list
    And I see NO CONVERSATIONS message in conversations list
    When I open archived conversations
    Then I see conversation <ArchivedUser> in conversations list
    When I swipe right on a <ArchivedUser>
    And I tap Unarchive action button
    Then I do not see conversation <ArchivedUser> in conversations list
    When I tap close Archive page button
    Then I see conversation <ArchivedUser> in conversations list
    And I do not see Archive button at the bottom of conversations list
    When I swipe right on a <ArchivedUser>
    And I tap Archive action button
    And I do not see conversation <ArchivedUser> in conversations list
    And I see Archive button at the bottom of conversations list
    And User <ArchivedUser> sends 1 messages to user Myself
    Then I see conversation <ArchivedUser> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | ArchivedUser|
      | user1Name | user2Name   |

  @C95634 @regression
  Scenario Outline: Verify hint is not shown anymore after tapping on it once
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I see Conversations hint text
    When I tap on Conversations hint text
    Then I see People Picker page
    When I click close button to dismiss people view
    Then I see conversations list
    And I do not see Conversations hint text

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
