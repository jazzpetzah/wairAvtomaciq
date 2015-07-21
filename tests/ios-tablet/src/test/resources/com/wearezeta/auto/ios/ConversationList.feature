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
    And I long swipe right to archive conversation <Contact2>
    Then I dont see conversation <Contact2> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list
    Then I see user <Contact2> in contact list

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id2755 @id2377
  Scenario Outline: Verify archive a conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I click archive button for conversation <Contact>
    Then I dont see conversation <Contact> in contact list
    And I long swipe right to archive conversation <Contact2>
    Then I dont see conversation <Contact2> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list
    Then I see user <Contact2> in contact list

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
    When I long swipe right to archive conversation <GroupChatName>
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
    And I long swipe right to archive conversation <GroupChatName>
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
    And I navigate back to conversations view
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

  @regression @id2369
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
    And I swipe right on Dialog page
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
    And I swipe right on Dialog page
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

  @regression @id2360
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

  @regression @id2368
  Scenario Outline: Verify missed call indicator appearance in conversation list [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User <Name> change accent color to <Color>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    And <Contact> verifies that call status to me is changed to inactive in 60 seconds
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
    And <Contact> verifies that call status to me is changed to inactive in 60 seconds
    Then I see missed call indicator in list for contact <Contact>
    When Contact <Contact> send number <Number> of message to user <Name>
    Then I see missed call indicator in list for contact <Contact>
    When Contact <Contact1> send number <Number> of message to user <Name>
    Then I see missed call indicator got moved down in list for contact <Contact>

    Examples: 
      | Name      | Contact   | Contact1  | Number | Color           | CallBackend |
      | user1Name | user2Name | user3Name | 2      | StrongLimeGreen | autocall    |

  @staging @id2371
  Scenario Outline: Verify unread dots have different size for 1, 5, 10 incoming messages [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User <Name> change accent color to <Color>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I swipe right on Dialog page
    And I tap on contact name <Contact1>
    And I swipe right on Dialog page
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

  @regression @id2465 
  Scenario Outline: Verify Play/pause Youtube media from conversation list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type and send long message and media link <YouTubeLink>
    And I click play video button
    And I rotate UI to portrait
    And I click play video button
    And I swipe right on Dialog page
    Then I see play/pause button next to username <Contact> in contact list
    And I tap on play/pause button in contact list
    And I see Play media button next to user <Contact>
    And I tap on play/pause button in contact list
    And I see Pause media button next to user <Contact>

    Examples: 
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @regression @id2566
  Scenario Outline: Verify muting ongoing call [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I tap on contact name <Contact>
    And I see dialog page
    And <Contact> calls me using <CallBackend>
    And I accept incoming call
    And I see mute call, end call buttons
    And I swipe right on Dialog page
    Then I see mute call button in conversation list
    And I click mute call button in conversation list
    And I swipe left in current window
    And I see mute call button on calling bar is selected

    Examples: 
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | webdriver   |

  @regression @id2364
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
    And I swipe right on Dialog page
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
