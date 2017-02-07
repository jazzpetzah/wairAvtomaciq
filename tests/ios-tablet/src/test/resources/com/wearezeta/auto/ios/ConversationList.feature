Feature: Conversation List

  @C2530 @regression @rc @fastLogin
  Scenario Outline: Verify archive a conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I swipe right on conversation <Contact>
    And I tap Archive conversation action button
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2528 @regression @fastLogin
  Scenario Outline: Verify archive a group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I swipe right on conversation <GroupChatName>
    And I tap Archive conversation action button
    Then I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    Then I see conversation <GroupChatName> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @C2529 @regression @fastLogin
  Scenario Outline: Unarchive conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given User Myself archives single user conversation <ArchivedUser>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I open archived conversations
    And I tap on contact name <ArchivedUser>
    Then I see first item in contact list named <ArchivedUser>

    Examples:
      | Name      | ArchivedUser |
      | user1Name | user2Name    |

  @C2506 @regression @fastLogin
  Scenario Outline: Verify opening search by tapping on the search field [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    And I accept alert if visible
    Then I see Search UI

    Examples:
      | Name      |
      | user1Name |

  @C2533 @regression @fastLogin
  Scenario Outline: Verify Ping animation in the conversations list [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User Myself removes his avatar picture
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact2>
    And I navigate back to conversations list
    Given I remember the left side state of <Contact> conversation item on iPad
    When User <Contact> pings conversation Myself
    And I see first item in contact list named <Contact>
    Then I see the state of <Contact> conversation item is changed on iPad

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2508 @regression @fastLogin
  Scenario Outline: Verify conversations are sorted according to most recent activity [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given <Name> is connected to <Contact>,<Contact2>,<Contact3>
    Given Users add the following devices: {"<Contact>": [{}], "<Contact3>": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends <Number> default messages to conversation Myself
    Given User <Contact3> sends <Number> default messages to conversation Myself
    Given I see conversations list
    Given I see first item in contact list named <Contact3>
    When User <Contact2> pings conversation <Name>
    And I wait for 5 seconds
    Then I see first item in contact list named <Contact2>
    When User <Contact> sends 1 image file <Picture> to conversation Myself
    And I wait for 5 seconds
    Then I see first item in contact list named <Contact>

    Examples:
      | Name      | Contact   | Contact2  | Contact3  | Number | Picture     |
      | user1Name | user2Name | user3name | user4name | 2      | testing.jpg |

  @C2509 @regression @rc @fastLogin
  Scenario Outline: (ZIOS-6338) Verify inbox area displaying in case of new incoming connection requests [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I do not see Pending request link in conversations list
    And <Contact> sent connection request to Me
    # Workaround for ZIOS-6338
    And I tap Incoming Pending Requests item in conversations list
    Then I see Connect button on Single user Pending incoming connection page
    And I see name "<Contact>" on Single user Pending incoming connection page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2532 @regression @fastLogin
  Scenario Outline: Verify missed call indicator appearance in conversation list [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User Myself removes his avatar picture
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given Users add the following devices: {"<Contact>": [{}]}
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact1>
    And I navigate back to conversations list
    And I remember the left side state of <Contact> conversation item on iPad
    And <Contact> calls me
    And <Contact> stops outgoing call to me
    Then I see the state of <Contact> conversation item is changed on iPad
    When I remember the left side state of <Contact> conversation item on iPad
    And User <Contact> sends <Number> default messages to conversation Myself
    Then I see the state of <Contact> conversation item is not changed on iPad

    Examples:
      | Name      | Contact   | Contact1  | Number | CallBackend |
      | user1Name | user2Name | user3Name | 2      | chrome      |

  @C2535 @regression @fastLogin
  Scenario Outline: Verify unread dots have different size for 1, 5, 10 incoming messages [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to all other
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact2>
    And I navigate back to conversations list
    And I remember the left side state of <Contact> conversation item on iPad
    When User <Contact> sends 10 default messages to conversation Myself
    And I see first item in contact list named <Contact>
    # FIXME: Screenshotscomparison on Jenkins nodes works a bit differently
#    Then I see the state of <Contact> conversation item is changed on iPad
#    When I remember the left side state of <Contact> conversation item on iPad
#    And User <Contact> sends 4 encrypted message to user Myself
#    Then I see the state of <Contact> conversation item is changed on iPad
#    When I remember the left side state of <Contact> conversation item on iPad
#    And User <Contact> sends 5 encrypted messages to user Myself
    Then I see the state of <Contact> conversation item is changed on iPad

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2558 @regression @fastLogin
  Scenario Outline: Verify action menu is opened on swipe right on the group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I swipe right on conversation <GroupChatName>
    Then I see Mute conversation action button
    And I see Archive conversation action button
    And I see Delete conversation action button
    And I see Leave conversation action button
    And I see Cancel conversation action button

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3name | ActionMenuChat |

  @C2561 @regression @fastLogin
  Scenario Outline: Verify action menu is opened on swipe right on 1to1 conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I swipe right on conversation <Contact>
    Then I see Mute conversation action button
    And I see Archive conversation action button
    And I see Delete conversation action button
    And I see Block conversation action button
    And I see Cancel conversation action button

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2511 @regression @fastLogin
  Scenario Outline: Verify first conversation in the list is highlighted and opened [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> change accent color to BrightOrange
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see conversations list
    Then I see the conversation with <Contact>

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2545 @regression @fastLogin
  Scenario Outline: Verify that deleted conversation isn't going to archive [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Users add the following devices: {"<Contact1>": [{}], "Myself": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given User Myself sends 1 default message to conversation <Contact1>
    Given I see conversations list
    When I swipe right on conversation <Contact1>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <Contact1> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2547 @regression @fastLogin
  Scenario Outline: Verify deleting 1-to-1 conversation from archive [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on conversation <Contact1>
    And I tap Archive conversation action button
    And I do not see conversation <Contact1> in conversations list
    And I open archived conversations
    And I swipe right on conversation <Contact1>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <Contact1> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2553 @rc @regression @fastLogin
  Scenario Outline: ZIOS-6809 Verify posting in a group conversation without content [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{}], "Myself": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself pings conversation <GroupChatName>
    Given User Myself sends 1 default message to conversation <GroupChatName>
    Given User <Contact1> sends 1 default message to conversation <GroupChatName>
    Given User Myself sends 1 image file <Image> to conversation <GroupChatName>
    When I swipe right on conversation <GroupChatName>
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <GroupChatName> in conversations list
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<GroupChatName>" in Search UI input field
    And I tap on conversation <GroupChatName> in search result
    Then I see 0 conversation entries
    When I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C2543 @regression @fastLogin
  Scenario Outline: Verify canceling blocking person [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on conversation <Contact1>
    And I tap Block conversation action button
    And I tap Cancel conversation action button
    Then I see actions menu for <Contact1> conversation

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2388 @regression @fastLogin
  Scenario Outline: Verify archiving silenced conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on conversation <Contact>
    And I tap Mute conversation action button
    When I swipe right on conversation <Contact>
    And I tap Archive conversation action button
    Then I do not see conversation <Contact> in conversations list
    When User <Contact> sends 1 default message to conversation Myself
    And I do not see conversation <Contact> in conversations list
    And User <Contact> sends 1 image file <Picture> to conversation Myself
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list
    And I tap on contact name <Contact>
    And I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2551 @regression @ZIOS-5247 @fastLogin
  Scenario Outline: Verify removing the content and leaving from the group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User adds the following device: {"Myself": [{}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Name> sends 1 default message to conversation <GroupChatName>
    When I swipe right on conversation <GroupChatName>
    And I tap Delete conversation action button
    And I tap Also Leave checkbox on Group info page
    And I confirm Delete conversation action
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<GroupChatName>" in Search UI input field
    Then I see the conversation "<GroupChatName>" does not exist in Search results
    When I tap X button on Search UI page
    And I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    Then I see conversation <GroupChatName> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C2555 @regression @fastLogin
  Scenario Outline: Verify deleting the history from kicked out conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> removes Myself from group chat <GroupChatName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on conversation <GroupChatName>
    And I see Archive conversation action button
    And I see Delete conversation action button
    And I see Cancel conversation action button
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <GroupChatName> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | KICKCHAT      |

  @C2541 @regression @fastLogin
  Scenario Outline: Verify blocking person from action menu [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on conversation <Contact>
    And I tap Block conversation action button
    And I confirm Block conversation action
    Then I do not see conversation <Contact> in conversations list
    And I do not see Archive button at the bottom of conversations list
    And I wait until <Contact> exists in backend search results
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact>" in Search UI input field
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2536 @regression @rc @fastLogin
  Scenario Outline: Verify messages are marked read after opening a conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I remember the left side state of <Contact1> conversation item on iPad
    And I tap on contact name <Contact2>
    And User <Contact1> sends 10 default messages to conversation Myself
    And I tap on contact name <Contact1>
    Then I see the state of <Contact1> conversation item is not changed on iPad

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C95633 @regression @fastLogin
  Scenario Outline: Verify hint is not shown anymore after tapping on it once [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on Conversations hint text
    And I accept alert if visible
    Then I see Search UI
    When I tap X button on Search UI page
    Then I see conversations list
    And I do not see Conversations hint text

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |