Feature: Conversation List

  @C2530 @regression @rc @id2755 @id2377
  Scenario Outline: Verify archive a conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I swipe right on a <Contact>
    And I click archive button for conversation
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2528 @regression @id2750
  Scenario Outline: Verify archive a group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I swipe right on a <GroupChatName>
    And I click archive button for conversation
    Then I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    Then I see conversation <GroupChatName> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @C2529 @regression @id2751
  Scenario Outline: Unarchive conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I open archived conversations
    And I tap on contact name <ArchivedUser>
    Then I see first item in contact list named <ArchivedUser>

    Examples:
      | Name      | ArchivedUser |
      | user1Name | user2Name    |

  @C2506 @regression @id2754
  Scenario Outline: Verify opening search by tapping on the search field [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    Then I see People picker page

    Examples:
      | Name      |
      | user1Name |

  @C2533 @regression @id2369
  Scenario Outline: Verify Ping animation in the conversations list [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User Myself removes his avatar picture
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact2>
    And I navigate back to conversations list
    Given I remember the left side state of <Contact> conversation item on iPad
    When User <Contact> securely pings conversation Myself
    And I see first item in contact list named <Contact>
    Then I see the state of <Contact> conversation item is changed on iPad

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2508 @regression @id2757
  Scenario Outline: Verify conversations are sorted according to most recent activity [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given <Name> is connected to <Contact>,<Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends <Number> encrypted messages to user Myself
    Given User <Contact3> sends <Number> encrypted messages to user Myself
    Given I see first item in contact list named <Contact3>
    Given User <Contact2> securely pings conversation <Name>
    Then I see first item in contact list named <Contact2>
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Then I see first item in contact list named <Contact>

    Examples:
      | Name      | Contact   | Contact2  | Contact3  | Number | Picture     |
      | user1Name | user2Name | user3name | user4name | 2      | testing.jpg |

  @C2502 @regression @id2360
  Scenario Outline: Get invitation message from user [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I Sign in on tablet using my email
    When I see conversations list
    And I see Pending request link in conversations list

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2532 @regression @id2368
  Scenario Outline: Verify missed call indicator appearance in conversation list [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given User Myself removes his avatar picture
    Given <Contact> starts instance using <CallBackend>
    Given I Sign in on tablet using my email
    And I see conversations list
    When I remember the left side state of <Contact> conversation item on iPad
    When <Contact> calls me
    And <Contact> stops calling me
    Then I see the state of <Contact> conversation item is changed on iPad
    When I remember the left side state of <Contact> conversation item on iPad
    And User <Contact> sends <Number> encrypted messages to user Myself
    Then I see the state of <Contact> conversation item is not changed on iPad

    Examples:
      | Name      | Contact   | Contact1  | Number | CallBackend |
      | user1Name | user2Name | user3Name | 2      | autocall    |

  @C2535 @regression @id2371
  Scenario Outline: Verify unread dots have different size for 1, 5, 10 incoming messages [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to all other
    Given I Sign in on tablet using my email
    And I see conversations list
    When I tap on contact name <Contact2>
    And I navigate back to conversations list
    And I remember the left side state of <Contact> conversation item on iPad
    When User <Contact> sends 10 encrypted message to user Myself
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

  @C2558 @regression @id3827
  Scenario Outline: Verify action menu is opened on swipe right on the group conversation [LANDSCAPE]
    Given I rotate UI to landscape
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see conversations list
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

  @C2561 @regression @id3832
  Scenario Outline: Verify action menu is opened on swipe right on 1to1 conversation [LANDSCAPE]
    Given I rotate UI to landscape
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see conversations list
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

  @C2511 @regression @id3901 @ZIOS-5279
  Scenario Outline: Verify first conversation in the list is highlighted and opened [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Name> change accent color to BrightOrange
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see conversations list
    Then I see conversation <Contact> is selected in list
    And I see dialog page with contact <Contact>

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2545 @regression @id3956
  Scenario Outline: Verify that deleted conversation isn't going to archive [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given User Myself sends 1 encrypted message to user <Contact1>
    When I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    And I do not see conversation <Contact1> in conversations list
    And I open archived conversations
    Then I do not see conversation <Contact1> in conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2547 @regression @id3962
  Scenario Outline: Verify deleting 1-to-1 conversation from archive [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on a <Contact1>
    And I press Archive button in action menu in Contact List
    And I do not see conversation <Contact1> in conversations list
    And I open archived conversations
    And I swipe right on a <Contact1>
    And I click delete menu button
    And I confirm delete conversation content
    Then I do not see conversation <Contact1> in conversations list
    And I open archived conversations
    Then I do not see conversation <Contact1> in conversations list

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2553 @rc @regression @id3970
  Scenario Outline: Verify posting in a group conversation without content [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself securely pings conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends encrypted image <Image> to group conversation <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I click delete menu button
    And I confirm delete conversation content
    Then I do not see conversation <GroupChatName> in conversations list
    When I open search UI
    And I input in People picker search field conversation name <GroupChatName>
    And I tap on conversation <GroupChatName> in search result
    Then I see empty group chat page with users <Contact1>,<Contact2> with only system message
    When I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C2543 @regression @id4019
  Scenario Outline: Verify canceling blocking person [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on a <Contact1>
    And I press menu Block button
    And I click Cancel button
    Then I see conversation action menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2388 @regression @id3985
  Scenario Outline: Verify archiving silenced conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on a <Contact>
    And I press menu silence button
    When I swipe right on a <Contact>
    And I click archive button for conversation
    Then I do not see conversation <Contact> in conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    And I do not see conversation <Contact> in conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    Then I see conversation <Contact> in conversations list
    And I tap on contact name <Contact>
    And I see 1 photo in the dialog

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C2551 @regression @id3967 @ZIOS-5247
  Scenario Outline: Verify removing the content and leaving from the group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Name> sends 1 encrypted message to group conversation <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I click delete menu button
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

  @C2555 @regression @id4007
  Scenario Outline: Verify deleting the history from kicked out conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> removed <Name> from group chat <GroupChatName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on a <GroupChatName>
    And I see Archive button in action menu in Contact List
    And I see Delete button in action menu in Contact List
    And I see Cancel button in action menu in Contact List
    And I click delete menu button
    And I confirm delete conversation content
    Then I do not see conversation <GroupChatName> in conversations list
    And I open archived conversations
    Then I do not see conversation <GroupChatName> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | KICKCHAT      |

  @C2541 @regression @id4016
  Scenario Outline: Verify blocking person from action menu [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see conversations list
    When I swipe right on a <Contact>
    And I press menu Block button
    And I confirm blocking alert
    Then I do not see conversation <Contact> in conversations list
    And I open archived conversations
    And I do not see conversation <Contact> in conversations list
    And I open search UI
    And I input in People picker search field user name <Contact>
    Then I see the conversation "<Contact>" exists in Search results

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2515 @regression @id4105
  Scenario Outline: Verify 'Invite more people' is hidden after 6 connections [LANDSCAPE]
    Given There are <Number> users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see conversations list
    And I see Invite more people button
    And Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>,<Contact6>
    And I see Invite more people button
    And Myself is connected to <Contact7>
    Then I do not see Invite more people button

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  | Contact7  | Number |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name | user8Name | 8      |
