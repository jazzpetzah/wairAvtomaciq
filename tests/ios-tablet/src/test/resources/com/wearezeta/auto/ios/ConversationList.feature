Feature: Conversation List

  @regression @id2378 @id2568
  Scenario Outline: Verify archive a conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I click archive button for conversation <Contact>
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @rc @id2755 @id2377
  Scenario Outline: Verify archive a conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I click archive button for conversation <Contact>
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id2674
  Scenario Outline: Verify archive a group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I swipe right on a <GroupChatName>
    And I click archive button for conversation <GroupChatName>
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations on iPad
    Then I see user <GroupChatName> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @regression @id2750
  Scenario Outline: Verify archive a group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I swipe right on a <GroupChatName>
    And I click archive button for conversation <GroupChatName>
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations on iPad
    Then I see user <GroupChatName> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @regression @id2675
  Scenario Outline: Unarchive conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I wait for 30 seconds
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I open archived conversations on iPad
    And I tap on contact name <ArchivedUser>
    And I navigate back to conversations view
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Name      | ArchivedUser |
      | user1Name | user2Name    |

  @regression @id2751
  Scenario Outline: Unarchive conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    And I wait for 30 seconds
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I open archived conversations on iPad
    And I tap on contact name <ArchivedUser>
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Name      | ArchivedUser |
      | user1Name | user2Name    |

  @regression @id2753
  Scenario Outline: Verify opening search by tapping on the search field [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page

    Examples: 
      | Name      |
      | user1Name |

  @regression @id2754
  Scenario Outline: Verify opening search by tapping on the search field [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page

    Examples: 
      | Name      |
      | user1Name |

  @regression @rc @id2369
  Scenario Outline: Verify Ping animation in the conversations list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When Contact <Contact> ping conversation <Name>
    And I wait for 10 seconds
    Then I see change of state for first conversation cell

    Examples: 
      | Name      | Contact   | NewName | Color        |
      | user1Name | user2Name | PING    | BrightOrange |

  @regression @id2752
  Scenario Outline: Verify Ping animation in the conversations list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When Contact <Contact> ping conversation <Name>
    And I wait for 10 seconds
    Then I see change of state for first conversation cell

    Examples: 
      | Name      | Contact   | NewName | Color        |
      | user1Name | user2Name | PING    | BrightOrange |

  @regression @id2367
  Scenario Outline: Verify messages are marked as read with disappearing unread dot [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given Contact <Contact> send number <Number> of message to user <Name>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When I tap on contact name <Contact>
    And I see dialog page
    And I return to the chat list
    Then I see change of state for first conversation cell

    Examples: 
      | Name      | Contact   | NewName    | Color        | Number |
      | user1Name | user2Name | UNREAD DOT | BrightYellow | 2      |

  @regression @id2711
  Scenario Outline: Verify messages are marked as read with disappearing unread dot [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given Contact <Contact> send number <Number> of message to user <Name>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see change of state for first conversation cell

    Examples: 
      | Name      | Contact   | NewName    | Color        | Number |
      | user1Name | user2Name | UNREAD DOT | BrightYellow | 2      |

  @regression @id2756
  Scenario Outline: Verify conversations are sorted according to most recent activity [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given <Name> is connected to <Contact>,<Contact2>,<Contact3>
    Given Contact <Contact> send number <Number> of message to user <Name>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And Contact <Contact3> send number <Number> of message to user <Name>
    And I see first item in contact list named <Contact3>
    When Contact <Contact2> ping conversation <Name>
    And I see first item in contact list named <Contact2>
    When Contact <Contact> sends image <Picture> to single user conversation <Name>
    Then I see first item in contact list named <Contact>

    Examples: 
      | Name      | Contact   | Contact2  | Contact3  | Number | Picture     |
      | user1Name | user2Name | user3name | user4name | 2      | testing.jpg |

  @regression @id2757
  Scenario Outline: Verify conversations are sorted according to most recent activity [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given <Name> is connected to <Contact>,<Contact2>,<Contact3>
    Given Contact <Contact> send number <Number> of message to user <Name>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And Contact <Contact3> send number <Number> of message to user <Name>
    And I see first item in contact list named <Contact3>
    When Contact <Contact2> ping conversation <Name>
    And I see first item in contact list named <Contact2>
    When Contact <Contact> sends image <Picture> to single user conversation <Name>
    Then I see first item in contact list named <Contact>

    Examples: 
      | Name      | Contact   | Contact2  | Contact3  | Number | Picture     |
      | user1Name | user2Name | user3name | user4name | 2      | testing.jpg |

  @regression @rc @id2360
  Scenario Outline: Get invitation message from user [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I see Pending request link in contact list

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id3010
  Scenario Outline: Get invitation message from user [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I see Pending request link in contact list

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @rc @id2368
  Scenario Outline: Verify missed call indicator appearance in conversation list [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User <Name> change accent color to <Color>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    And <Contact> verifies that call status to me is changed to destroyed in 60 seconds
    Then I see missed call indicator in list for contact <Contact>
    When Contact <Contact> send number <Number> of message to user <Name>
    Then I see missed call indicator in list for contact <Contact>
    When Contact <Contact1> send number <Number> of message to user <Name>
    Then I see missed call indicator got moved down in list for contact <Contact>

    Examples: 
      | Name      | Contact   | Contact1  | Number | Color           | CallBackend |
      | user1Name | user2Name | user3Name | 2      | StrongLimeGreen | autocall    |

  @regression @id2995
  Scenario Outline: Verify missed call indicator appearance in conversation list [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User <Name> change accent color to <Color>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    And <Contact> verifies that call status to me is changed to DESTROYED in 60 seconds
    Then I see missed call indicator in list for contact <Contact>
    When Contact <Contact> send number <Number> of message to user <Name>
    Then I see missed call indicator in list for contact <Contact>
    When Contact <Contact1> send number <Number> of message to user <Name>
    Then I see missed call indicator got moved down in list for contact <Contact>

    Examples: 
      | Name      | Contact   | Contact1  | Number | Color           | CallBackend |
      | user1Name | user2Name | user3Name | 2      | StrongLimeGreen | autocall    |

  @regression @rc @id2371
  Scenario Outline: Verify unread dots have different size for 1, 5, 10 incoming messages [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User <Name> change accent color to <Color>
    Given I Sign in on tablet using my email
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

  @regression @id2942
  Scenario Outline: Verify unread dots have different size for 1, 5, 10 incoming messages [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User <Name> change accent color to <Color>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I tap on contact name <Contact1>
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

  @regression @rc @id2566
  Scenario Outline: Verify muting ongoing call [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> change accent color to BrightOrange
    Given <Contact> starts waiting instance using <CallBackend>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And <Contact> calls me using <CallBackend>
    And I see incoming calling message for contact <Contact>
    And I accept incoming call
    And I see mute call, end call buttons
    And I swipe right on Dialog page
    Then I see mute call button in conversation list
    And I click mute call button in conversation list
    And I swipe left in current window
    And I see mute call button on calling bar is selected

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |

  @regression @rc @id2364
  Scenario Outline: Verify play/pause controls can change playing media state - SoundCloud [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sent message <SoundCloudLink> to conversation <Name>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap media link
    And I rotate UI to portrait
    And I return to the chat list
    Then I see Pause media button next to user <Contact>
    And I tap on play/pause button in contact list
    And I see Play media button next to user <Contact>
    And I see playing media is paused
    And I tap on play/pause button in contact list
    And I see Pause media button next to user <Contact>
    And I see media is playing

    Examples: 
      | Name      | Contact   | SoundCloudLink                                                                       |
      | user1Name | user2Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @regression @id3828
  Scenario Outline: Verify action menu is opened on swipe right on the group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
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

  @regression @id3827
  Scenario Outline: Verify action menu is opened on swipe right on the group conversation [LANDSCAPE]
    Given I rotate UI to landscape
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
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

  @regression @id3831
  Scenario Outline: Verify action menu is opened on swipe right on 1to1 conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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

  @regression @id3832
  Scenario Outline: Verify action menu is opened on swipe right on 1to1 conversation [LANDSCAPE]
    Given I rotate UI to landscape
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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

  @regression @id3900
  Scenario Outline: Verify first conversation in the list is highlighted and opened [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> change accent color to BrightOrange
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    Then I see conversation <Contact> is selected in list
    And I see dialog page with contact <Contact>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id3901
  Scenario Outline: Verify first conversation in the list is highlighted and opened [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> change accent color to BrightOrange
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    Then I see conversation <Contact> is selected in list
    And I see dialog page with contact <Contact>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3955
  Scenario Outline: Verify that deleted conversation isn't going to archive [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> sent message <Message> to conversation <Name>
    Given User <Name> sent message <Message> to conversation <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    And I dont see conversation <Contact1> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <Contact1> in contact list

    Examples: 
      | Name      | Contact1  | Message |
      | user1Name | user2Name | testing |

  @regression @id3956
  Scenario Outline: Verify that deleted conversation isn't going to archive [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> sent message <Message> to conversation <Name>
    Given User <Name> sent message <Message> to conversation <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    And I dont see conversation <Contact1> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <Contact1> in contact list

    Examples: 
      | Name      | Contact1  | Message |
      | user1Name | user2Name | testing |

  @staging @id3961
  Scenario Outline: Verify deleting 1-to-1 conversation from archive [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I press Archive button in action menu in Contact List
    And I dont see conversation <Contact1> in contact list
    And I open archived conversations on iPad
    And I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <Contact1> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <Contact1> in contact list

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @staging @id3962
  Scenario Outline: Verify deleting 1-to-1 conversation from archive [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I press Archive button in action menu in Contact List
    And I dont see conversation <Contact1> in contact list
    And I open archived conversations on iPad
    And I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <Contact1> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <Contact1> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Message | GroupChatName |
      | user1Name | user2Name | user3Name | testing | ForDeletion   |

  @staging @id3969
  Scenario Outline: Verify posting in a group conversation without content [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> sent message <Message> to conversation <GroupChatName>
    Given Contact <Name> sends image <Image> to group conversation <GroupChatName>
    Given Contact <Name> ping conversation <GroupChatName>
    Given User <Contact1> sent message <Message> to conversation <GroupChatName>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    When I open search by taping on it
    And I input conversation name <GroupChatName> in Search input
    And I see conversation <GroupChatName> is presented in Search results
    And I tap on conversation <GroupChatName> in search result
    Then I see empty group chat page with users <Contact1>,<Contact2> with only system message
    When I type the message and send it
    Then I see message in the dialog
    And I see only 2 messages

    Examples: 
      | Name      | Contact1  | Contact2  | Message | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | testing | ForDeletion   | testing.jpg |

  @regression @id3970
  Scenario Outline: Verify posting in a group conversation without content [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> sent message <Message> to conversation <GroupChatName>
    Given Contact <Name> sends image <Image> to group conversation <GroupChatName>
    Given Contact <Name> ping conversation <GroupChatName>
    Given User <Contact1> sent message <Message> to conversation <GroupChatName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    When I open search by taping on it
    And I input conversation name <GroupChatName> in Search input
    And I see conversation <GroupChatName> is presented in Search results
    And I tap on conversation <GroupChatName> in search result
    Then I see empty group chat page with users <Contact1>,<Contact2> with only system message
    When I type the message and send it
    Then I see message in the dialog
    And I see only 2 messages

    Examples: 
      | Name      | Contact1  | Contact2  | Message | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | testing | ForDeletion   | testing.jpg |

  @regression @id4018
  Scenario Outline: Verify canceling blocking person [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I press menu Block button
    And I click Cancel button
    Then I see conversation action menu

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @regression @id4019
  Scenario Outline: Verify canceling blocking person [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I press menu Block button
    And I click Cancel button
    Then I see conversation action menu

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @regression @id2324
  Scenario Outline: Verify archiving silenced conversation [PORTAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I press menu silence button
    When I swipe right on a <Contact>
    And I click archive button for conversation <Contact>
    Then I dont see conversation <Contact> in contact list
    And Contact <Contact> send number 1 of message to user <Name>
    And I dont see conversation <Contact> in contact list
    And Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list
    And I tap on contact name <Contact>
    And I see new photo in the dialog

    Examples: 
      | Name      | Contact   | Picture     | ConversationType |
      | user1Name | user2Name | testing.jpg | single user      |

  @regression @id3985
  Scenario Outline: Verify archiving silenced conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I press menu silence button
    When I swipe right on a <Contact>
    And I click archive button for conversation <Contact>
    Then I dont see conversation <Contact> in contact list
    And Contact <Contact> send number 1 of message to user <Name>
    And I dont see conversation <Contact> in contact list
    And Contact <Contact> sends image <Picture> to <ConversationType> conversation <Name>
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list
    And I tap on contact name <Contact>
    And I see new photo in the dialog

    Examples: 
      | Name      | Contact   | Picture     | ConversationType |
      | user1Name | user2Name | testing.jpg | single user      |

  @regression @id3966 @ZIOS-5247
  Scenario Outline: Verify removing the content and leaving from the group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> sent message <Message> to conversation <GroupChatName>
    Given I Sign in on tablet using my email
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
    And I open archived conversations on iPad
    Then I dont see conversation <GroupChatName> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Message | GroupChatName |
      | user1Name | user2Name | user3Name | testing | ForDeletion   |

  @regression @id3967 @ZIOS-5247
  Scenario Outline: Verify removing the content and leaving from the group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> sent message <Message> to conversation <GroupChatName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
    And I open archived conversations on iPad
    Then I dont see conversation <GroupChatName> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Message | GroupChatName |
      | user1Name | user2Name | user3Name | testing | ForDeletion   |

  @regression @id4006
  Scenario Outline: Verify deleting the history from kicked out conversation [POTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> removed <Name> from group chat <GroupChatName>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I see Archive button in action menu in Contact List
    And I see Delete button in action menu in Contact List
    And I see Cancel button in action menu in Contact List
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <GroupChatName> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | KICKCHAT      |

  @regression @id4007
  Scenario Outline: Verify deleting the history from kicked out conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> removed <Name> from group chat <GroupChatName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I see Archive button in action menu in Contact List
    And I see Delete button in action menu in Contact List
    And I see Cancel button in action menu in Contact List
    And I click delete menu button
    And I confirm delete conversation content
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <GroupChatName> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | KICKCHAT      |

  @staging @id4015
  Scenario Outline: Verify blocking person from action menu [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I press menu Block button
    And I confirm blocking alert
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    And I dont see conversation <Contact> in contact list
    And I open search by taping on it
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page
    
    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id4016
  Scenario Outline: Verify blocking person from action menu [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I press menu Block button
    And I confirm blocking alert
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    And I dont see conversation <Contact> in contact list
    And I open search by taping on it
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id4104
  Scenario Outline: Verify 'Invite more people' is hidden after 6 connections [PORTRAIT]
    Given There are <Number> users where <Name> is me
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I see Invite more people button
    And Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>
    And I see Invite more people button
    And Myself is connected to <Contact7>
    Then I DONT see Invite more people button

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | Contact7  | Number |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | user8Name | 8      |

  @staging @id4105
  Scenario Outline: Verify 'Invite more people' is hidden after 6 connections [LANDSCAPE]
    Given There are <Number> users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I see Invite more people button
    And Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>
    And I see Invite more people button
    And Myself is connected to <Contact7>
    Then I DONT see Invite more people button

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | Contact7  | Number |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | user8Name | 8      |
