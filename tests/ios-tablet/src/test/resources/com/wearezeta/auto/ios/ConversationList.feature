Feature: Conversation List

  @C2523 @C2524 @regression @id2378 @id2568
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

  @C2530 @regression @rc @id2755 @id2377
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

  @C2525 @regression @id2674
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

  @C2528 @regression @id2750
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

  @C2526 @regression @id2675
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

  @C2529 @regression @id2751
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

  @C2505 @regression @id2753
  Scenario Outline: Verify opening search by tapping on the search field [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page

    Examples:
      | Name      |
      | user1Name |

  @C2506 @regression @id2754
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

  @C2533 @regression @rc @id2369
  Scenario Outline: Verify Ping animation in the conversations list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given I remember the state of the first conversation cell
    Given User <Contact> pings conversation <Name>
    When I wait for 10 seconds
    Then I see change of state for first conversation cell

    Examples:
      | Name      | Contact   | NewName | Color        |
      | user1Name | user2Name | PING    | BrightOrange |

  @C2537 @regression @id2752
  Scenario Outline: Verify Ping animation in the conversations list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given I remember the state of the first conversation cell
    Given User <Contact> pings conversation <Name>
    When I wait for 10 seconds
    Then I see change of state for first conversation cell

    Examples:
      | Name      | Contact   | NewName | Color        |
      | user1Name | user2Name | PING    | BrightOrange |

  @C2531 @regression @id2367
  Scenario Outline: Verify messages are marked as read with disappearing unread dot [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Contact> sends <Number> encrypted messages to user Myself
    And I remember the state of the first conversation cell
    When I tap on contact name <Contact>
    And I see dialog page
    And I return to the chat list
    Then I see change of state for first conversation cell

    Examples:
      | Name      | Contact   | NewName    | Color        | Number |
      | user1Name | user2Name | UNREAD DOT | BrightYellow | 2      |

  @C2536 @regression @id2711
  Scenario Outline: Verify messages are marked as read with disappearing unread dot [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Contact> sends <Number> encrypted messages to user Myself
    And I remember the state of the first conversation cell
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see change of state for first conversation cell

    Examples:
      | Name      | Contact   | NewName    | Color        | Number |
      | user1Name | user2Name | UNREAD DOT | BrightYellow | 2      |

  @C2507 @regression @id2756
  Scenario Outline: Verify conversations are sorted according to most recent activity [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given <Name> is connected to <Contact>,<Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Contact> sends <Number> encrypted messages to user Myself
    Given User <Contact3> sends <Number> encrypted messages to user Myself
    And I see first item in contact list named <Contact3>
    Given User <Contact2> pings conversation <Name>
    And I see first item in contact list named <Contact2>
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Then I see first item in contact list named <Contact>

    Examples:
      | Name      | Contact   | Contact2  | Contact3  | Number | Picture     |
      | user1Name | user2Name | user3name | user4name | 2      | testing.jpg |

  @C2508 @regression @id2757
  Scenario Outline: Verify conversations are sorted according to most recent activity [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given <Name> is connected to <Contact>,<Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Contact> sends <Number> encrypted messages to user Myself
    Given User <Contact3> sends <Number> encrypted messages to user Myself
    Given I see first item in contact list named <Contact3>
    Given User <Contact2> pings conversation <Name>
    And I see first item in contact list named <Contact2>
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Then I see first item in contact list named <Contact>

    Examples:
      | Name      | Contact   | Contact2  | Contact3  | Number | Picture     |
      | user1Name | user2Name | user3name | user4name | 2      | testing.jpg |

  @C2502 @regression @rc @id2360
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

  @C2509 @regression @id3010
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

  @C2532 @regression @rc @id2368
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
    Given User <Contact> sends <Number> encrypted messages to user Myself
    Then I see missed call indicator in list for contact <Contact>
    Given User <Contact1> sends <Number> encrypted messages to user Myself
    Then I see missed call indicator got moved down in list for contact <Contact>

    Examples:
      | Name      | Contact   | Contact1  | Number | Color           | CallBackend |
      | user1Name | user2Name | user3Name | 2      | StrongLimeGreen | autocall    |

  @C2539 @regression @id2995
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
    Given User <Contact> sends <Number> encrypted messages to user Myself
    Then I see missed call indicator in list for contact <Contact>
    Given User <Contact1> sends <Number> encrypted messages to user Myself
    Then I see missed call indicator got moved down in list for contact <Contact>

    Examples:
      | Name      | Contact   | Contact1  | Number | Color           | CallBackend |
      | user1Name | user2Name | user3Name | 2      | StrongLimeGreen | autocall    |

  @C2535 @regression @rc @id2371
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
    Given User <Contact> sends 1 encrypted message to user Myself
    Then I see 1 unread message indicator in list for contact <Contact>
    Given User <Contact> sends 1 encrypted message to user Myself
    Then I see 5 unread message indicator in list for contact <Contact>
    Given User <Contact> sends 8 encrypted messages to user Myself
    Then I see 10 unread message indicator in list for contact <Contact>

    Examples:
      | Name      | Contact   | Contact1  | Color           |
      | user1Name | user2Name | user3Name | StrongLimeGreen |

  @C2538 @regression @id2942
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
    Given User <Contact> sends 1 encrypted message to user Myself
    Then I see 1 unread message indicator in list for contact <Contact>
    Given User <Contact> sends 1 encrypted message to user Myself
    Then I see 5 unread message indicator in list for contact <Contact>
    Given User <Contact> sends 8 encrypted message to user Myself
    Then I see 10 unread message indicator in list for contact <Contact>

    Examples:
      | Name      | Contact   | Contact1  | Color           |
      | user1Name | user2Name | user3Name | StrongLimeGreen |

  @C2504 @regression @rc @id2566
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

  @C2522 @regression @rc @id2364
  Scenario Outline: Verify play/pause controls can change playing media state - SoundCloud [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Contact> sends encrypted message "<SoundCloudLink>" to user Myself
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

  @C2559 @regression @id3828
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

  @C2558 @regression @id3827
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

  @C2560 @regression @id3831
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

  @C2561 @regression @id3832
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

  @C2510 @regression @id3900 @ZIOS-5279
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

  @C2511 @staging @id3901 @ZIOS-5279
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

  @C2544 @regression @id3955
  Scenario Outline: Verify that deleted conversation isn't going to archive [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given User Myself sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    And I dont see conversation <Contact1> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <Contact1> in contact list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2545 @regression @id3956
  Scenario Outline: Verify that deleted conversation isn't going to archive [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given User Myself sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    And I dont see conversation <Contact1> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <Contact1> in contact list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2546 @staging @id3961
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

  @C2547 @staging @id3962
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
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2552 @staging @id3969
  Scenario Outline: Verify posting in a group conversation without content [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User Myself pings conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends encrypted image <Image> to group conversation <GroupChatName>
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
    And I see 2 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C2553 @regression @id3970
  Scenario Outline: Verify posting in a group conversation without content [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User Myself pings conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends encrypted image <Image> to group conversation <GroupChatName>
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
    And I see 2 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C2542 @regression @id4018
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

  @C2543 @regression @id4019
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

  @C2383 @regression @id2324
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
    Given User <Contact> sends 1 encrypted message to user Myself
    And I dont see conversation <Contact> in contact list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list
    And I tap on contact name <Contact>
    And I see new photo in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2388 @regression @id3985
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
    Given User <Contact> sends 1 encrypted message to user Myself
    And I dont see conversation <Contact> in contact list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Then I dont see conversation <Contact> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list
    And I tap on contact name <Contact>
    And I see new photo in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2550 @regression @id3966 @ZIOS-5247
  Scenario Outline: Verify removing the content and leaving from the group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
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
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C2551 @regression @id3967 @ZIOS-5247
  Scenario Outline: Verify removing the content and leaving from the group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see Contact list with my name <Name>
    Given User <Name> sends 1 encrypted message to group conversation <GroupChatName>
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
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C2554 @regression @id4006
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

  @C2555 @regression @id4007
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

  @C2540 @staging @id4015
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

  @C2541 @staging @id4016
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

  @C2514 @staging @id4104
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

  @C2515 @staging @id4105
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