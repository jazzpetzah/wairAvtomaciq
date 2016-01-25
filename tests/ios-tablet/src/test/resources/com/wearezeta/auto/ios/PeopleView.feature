Feature: People View

  @C2779 @regression @rc @id600
  Scenario Outline: Start group chat with users from contact list [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I fill in Search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on connected user <Contact1> on People picker page
    And I tap on Search input on People picker page
    And I fill in Search field user name <Contact2>
    And I see user <Contact2> found on People picker page
    And I tap on connected user <Contact2> on People picker page
    And I click on Go button
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2896 @regression @id2594
  Scenario Outline: Start group chat with users from contact list [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I fill in Search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on connected user <Contact1> on People picker page
    And I tap on Search input on People picker page
    And I fill in Search field user name <Contact2>
    And I see user <Contact2> found on People picker page
    And I tap on connected user <Contact2> on People picker page
    And I click on Go button
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2704 @regression @rc @id2434
  Scenario Outline: Start group chat from 1:1 conversation [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details
    And I see <Contact1> user profile page in iPad popover
    And I press Add button
    And I see People picker page on iPad popover
    And I click on connected user <Contact2> on People picker on iPad popover
    And I click on connected user <Contact3> on People picker on iPad popover
    And I click on Add to Conversation button on iPad popover
    Then I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I swipe right on group chat page
    Then I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2715 @regression @rc @id2653
  Scenario Outline: Start group chat from 1:1 conversation [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details
    And I see <Contact1> user profile page in iPad popover
    And I press Add button
    And I see People picker page on iPad popover
    And I click on connected user <Contact2> on People picker on iPad popover
    And I click on connected user <Contact3> on People picker on iPad popover
    And I click on Add to Conversation button on iPad popover
    And I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    Then I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2743 @regression @rc @id2445
  Scenario Outline: Verify leaving group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press leave converstation button on iPad
    Then I press leave on iPad
    And I open archived conversations on iPad
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveGroup    |

  @C2744 @regression @regression @id2708
  Scenario Outline: Verify leaving group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press leave converstation button on iPad
    Then I press leave on iPad
    And I open archived conversations on iPad
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveGroup    |

  @C2710 @regression @rc @id2441
  Scenario Outline: Verify removing from group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I click Remove on iPad
    And I see remove warning message on iPad
    And I confirm remove on iPad
    And I click Go back button on user profile popover
    Then I see that <Contact2> is not present on group chat info page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveGroup   |

  @C2670 @regression @id2981
  Scenario Outline: Verify removing from group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I click Remove on iPad
    And I see remove warning message on iPad
    And I confirm remove on iPad
    And I click Go back button on user profile popover
    Then I see that <Contact2> is not present on group chat info page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveGroup   |

  @C2727 @regression @rc @id2446
  Scenario Outline: Verify editing the conversation name [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press conversation menu button on iPad
    And I press RENAME on the menu on iPad
    And I change group conversation name to <ChatName>
    And I exit the group info iPad popover
    Then I see you renamed conversation to <ChatName> message shown in Group Chat
    And I swipe right on group chat page
    Then I see in contact list group chat named <ChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ChatName |
      | user1Name | user2Name | user3Name | RenameGroup   | NewName  |

  @C2728 @regression @id2922
  Scenario Outline: Verify editing the conversation name [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press conversation menu button on iPad
    And I press RENAME on the menu on iPad
    And I change group conversation name to <ChatName>
    And I exit the group info iPad popover
    Then I see you renamed conversation to <ChatName> message shown in Group Chat
    Then I see in contact list group chat named <ChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ChatName |
      | user1Name | user2Name | user3Name | RenameGroup   | NewName  |

  @C2711 @regression @rc @id2442
  Scenario Outline: Verify correct group info page information [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given User <Contact1> change avatar picture to <Picture>
    Given User <Contact1> change name to AQAPICTURECONTACT
    Given User <Contact2> change name to QAAVATAR TestContact
    Given User <Contact2> change accent color to <Color>
    Given User <Contact1> change accent color to <Color2>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    Then I can read the group name <GroupChatName> on the iPad popover
    Then I see that number of participants <ParticipantsNumber> is correct on iPad popover
    Then I see the correct avatar picture for user <Contact1> on iPad
    Then I see the correct avatar picture for user <Contact2> on iPad

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture                      | Color        | Color2       | ParticipantsNumber |
      | user1Name | user2Name | user3Name | GroupInfo     | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow | 3                  |

  @C2717 @regression @id2989
  Scenario Outline: Verify correct group info page information [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given User <Contact1> change avatar picture to <Picture>
    Given User <Contact1> change name to AQAPICTURECONTACT
    Given User <Contact2> change name to QAAVATAR TestContact
    Given User <Contact2> change accent color to <Color>
    Given User <Contact1> change accent color to <Color2>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    Then I can read the group name <GroupChatName> on the iPad popover
    Then I see that number of participants <ParticipantsNumber> is correct on iPad popover
    Then I see the correct avatar picture for user <Contact1> on iPad
    Then I see the correct avatar picture for user <Contact2> on iPad

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture                      | Color        | Color2       | ParticipantsNumber |
      | user1Name | user2Name | user3Name | GroupInfo     | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow | 3                  |

  @C2702 @regression @rc @id2432
  Scenario Outline: Check any users personal info in group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<ConnectedContact>
    Given Myself has group chat <GroupChatName> with <Contact2>,<ConnectedContact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    Then I see email and name of user <Contact2> on iPad popover

    Examples:
      | Name      | Contact2  | ConnectedContact | GroupChatName   |
      | user1Name | user2Name | user3Name        | SingleInfoGroup |

  @C2718 @regression @id3007
  Scenario Outline: Check any users personal info in group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<ConnectedContact>
    Given Myself has group chat <GroupChatName> with <Contact2>,<ConnectedContact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    Then I see email and name of user <Contact2> on iPad popover

    Examples:
      | Name      | Contact2  | ConnectedContact | GroupChatName   |
      | user1Name | user2Name | user3Name        | SingleInfoGroup |

  @C2719 @regression @id3085
  Scenario Outline: Verify you cant start 1:1 with unconnected user in group [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <NonConnectedContact>
    Then I see Connect label on Other user profile popover
    And I see Connect Button on Other user profile popover

    Examples:
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |

  @C2720 @regression @id3086
  Scenario Outline: Verify you cant start 1:1 with unconnected user in group [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <NonConnectedContact>
    Then I see Connect label on Other user profile popover
    And I see Connect Button on Other user profile popover

    Examples:
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |

  @C2714 @regression @rc @id2612
  Scenario Outline: Verify opening 1-to-1 conversation from group conversation details [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I tap on start dialog button on other user profile page
    #And I dismiss popover on iPad
    Then I see dialog page
    And I type the default message
    And I send the message
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | 1on1FromGroup |

  @C2721 @regression @id3087
  Scenario Outline: Verify opening 1-to-1 conversation from group conversation details [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I tap on start dialog button on other user profile page
    #And I dismiss popover on iPad
    Then I see dialog page
    And I type the default message
    And I send the message
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | 1on1FromGroup |

  @C2745 @regression @rc @id2455
  Scenario Outline: Verify unsilince the conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Name> silenced group conversation with <GroupChatName>
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I see conversation <GroupChatName> got silenced before
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press conversation menu button on iPad
    And I click NOTIFY button on iPad ellipsis menu
    And I exit the group info iPad popover
    And I see dialog page
    And I return to the chat list
    Then I see conversation <GroupChatName> is unsilenced

    Examples:
      | Name      | Contact1  | Contact2  | Color  | GroupChatName |
      | user1Name | user2Name | user3Name | Violet | SILENCE       |

  @C2747 @regression @id3208
  Scenario Outline: Verify unsilince the conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Name> silenced group conversation with <GroupChatName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I see conversation <GroupChatName> got silenced before
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press conversation menu button on iPad
    And I click NOTIFY button on iPad ellipsis menu
    And I exit the group info iPad popover
    And I see dialog page
    Then I see conversation <GroupChatName> is unsilenced

    Examples:
      | Name      | Contact1  | Contact2  | Color  | GroupChatName |
      | user1Name | user2Name | user3Name | Violet | SILENCE       |

  @C2746 @regression @rc @id2456
  Scenario Outline: Verify silence the conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press conversation menu button on iPad
    And I click SILENCE button on iPad ellipsis menu
    And I exit the group info iPad popover
    And I see dialog page
    And I return to the chat list
    Then I see conversation <GroupChatName> is silenced

    Examples:
      | Name      | Contact1  | Contact2  | Color  | GroupChatName |
      | user1Name | user2Name | user3Name | Violet | SILENCE       |

  @C2748 @regression @id3209
  Scenario Outline: Verify silence the conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press conversation menu button on iPad
    And I click SILENCE button on iPad ellipsis menu
    And I exit the group info iPad popover
    And I see dialog page
    Then I see conversation <GroupChatName> is silenced

    Examples:
      | Name      | Contact1  | Contact2  | Color  | GroupChatName |
      | user1Name | user2Name | user3Name | Violet | SILENCE       |

  @C2722 @regression @id3220
  Scenario Outline: Add someone to a group conversation [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press Add button
    And I see People picker page on iPad popover
    And I click on connected user <Contact3> on People picker on iPad popover
    And I click on Add to Conversation button on iPad popover
    And I open conversation details
    Then I see that number of participants <ParticipantsNumber> is correct on iPad popover

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | ParticipantsNumber |
      | user1Name | user2Name | user3Name | user4Name | AddContact    | 4                  |

  @C2723 @regression @id3221
  Scenario Outline: Add someone to a group conversation [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press Add button
    And I see People picker page on iPad popover
    And I click on connected user <Contact3> on People picker on iPad popover
    And I click on Add to Conversation button on iPad popover
    And I open conversation details
    Then I see that number of participants <ParticipantsNumber> is correct on iPad popover

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | ParticipantsNumber |
      | user1Name | user2Name | user3Name | user4Name | AddContact    | 4                  |

  @C2447 @regression @id2333
  Scenario Outline: Verify you can block a person from profile view [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press conversation menu button
    And I press menu Block button
    And I confirm blocking alert
    Then I dont see conversation <Contact1> in contact list
    Then I see conversation <Contact2> is selected in list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2458 @regression @id3834
  Scenario Outline: Verify you can block a person from profile view [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press conversation menu button
    And I press menu Block button
    And I confirm blocking alert
    Then I dont see conversation <Contact1> in contact list
    Then I see conversation <Contact2> is selected in list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2450 @staging @id2336
  Scenario Outline: Verify you can unblock someone from a group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> blocks user <Contact1>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select contact <Contact1>
    And I see <Contact1> user profile page
    And I unblock user
    Then I see dialog page
    And I return to the chat list
    Then I see conversation <Contact1> is selected in list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | UnblockFromGroup |

  @C2459 @regression @id3835
  Scenario Outline: Verify you can unblock someone from a group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> blocks user <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select contact <Contact1>
    And I see <Contact1> user profile page
    And I unblock user
    Then I see dialog page
    Then I see conversation <Contact1> is selected in list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | UnblockFromGroup |

  @C2708 @staging @id2439
  Scenario Outline: Verify displaying only connected users in the search in group chat [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact3>
    And I see user <Contact3> found on People picker page
    And I click close button to dismiss people view
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press Add button
    And I see People picker page on iPad popover
    And I wait until <Contact2> exists in backend search results
    And I tap on Search input on People picker page
    And I fill in Search field user name <Contact3>
    Then I see that user <Contact3> is NOT found on People picker page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user3Name | OnlyConnected |

  @C2724 @staging @id3949
  Scenario Outline: Verify displaying only connected users in the search in group chat [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact3>
    And I see user <Contact3> found on People picker page
    And I click close button to dismiss people view
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press Add button
    And I see People picker page on iPad popover
    And I wait until <Contact2> exists in backend search results
    And I tap on Search input on People picker page
    And I fill in Search field user name <Contact3>
    Then I see that user <Contact3> is NOT found on People picker page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user3Name | OnlyConnected |

  @C2738 @regression @id3958
  Scenario Outline: Verify that deleted conversation via participant view isn't going to archive [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I wait until popover is closed
    And I dont see conversation <GroupChatName> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <GroupChatName> in contact list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C2739 @regression @id3959
  Scenario Outline: Verify that deleted conversation via participant view isn't going to archive [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I wait until popover is closed
    And I dont see conversation <GroupChatName> in contact list
    And I open archived conversations on iPad
    Then I dont see conversation <GroupChatName> in contact list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C2740 @regression @id3977 @ZIOS-5247
  Scenario Outline: Verify removing the content and leaving from the group conversation via participant view [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I select Also Leave option on Delete conversation dialog
    And I confirm delete conversation content
    And I wait until popover is closed
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

  @C2741 @regression @id3978 @ZIOS-5247
  Scenario Outline: Verify removing the content and leaving from the group conversation via participant view [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I select Also Leave option on Delete conversation dialog
    And I confirm delete conversation content
    And I wait until popover is closed
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

  @C1833 @regression @id3975 @ZIOS-5063
  Scenario Outline: Verify removing the content from the group conversation via participant view [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself securely pings conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends encrypted image <Image> to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I wait until popover is closed
    And I open search by taping on it
    And I input conversation name <GroupChatName> in Search input
    Then I see conversation <GroupChatName> is presented in Search results
    When I tap on conversation <GroupChatName> in search result
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C1834 @staging @id3976 @ZIOS-5063
  Scenario Outline: Verify removing the content from the group conversation via participant view [LANDSCAPE]
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
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I wait until popover is closed
    And I open search by taping on it
    And I input conversation name <GroupChatName> in Search input
    Then I see conversation <GroupChatName> is presented in Search results
    When I tap on conversation <GroupChatName> in search result
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | Message | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | testing | ForDeletion   | testing.jpg |

  @C1835 @regression @id3979
  Scenario Outline: Verify removing the content from 1-to-1 via participant view [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself securely pings conversation <Contact1>
    Given User Myself sends 1 encrypted message to user <Contact1>
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given User <Contact1> sends encrypted image <Image> to single user conversation Myself
    When I tap on contact name <Contact1>
    And I see dialog page
    And I see 5 conversation entries
    And I open conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I wait until popover is closed
    And I open search by taping on it
    And I fill in Search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on connected user <Contact1> on People picker page
    And I click open conversation button on People picker page
    Then I see the only message in dialog is system message CONNECTED TO <Contact1>

    Examples:
      | Name      | Contact1  | Image       |
      | user1Name | user2Name | testing.jpg |

  @C1836 @regression @id3980
  Scenario Outline: Verify removing the content from 1-to-1 via participant view [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself securely pings conversation <Contact1>
    Given User Myself sends 1 encrypted message to user <Contact1>
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given User <Contact1> sends encrypted image <Image> to single user conversation Myself
    When I tap on contact name <Contact1>
    And I see dialog page
    And I see 5 conversation entries
    And I open conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I wait until popover is closed
    And I open search by taping on it
    And I fill in Search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on connected user <Contact1> on People picker page
    And I click open conversation button on People picker page
    Then I see the only message in dialog is system message CONNECTED TO <Contact1>

    Examples:
      | Name      | Contact1  | Message | GroupChatName | Image       |
      | user1Name | user2Name | testing | ForDeletion   | testing.jpg |

  @C2556 @staging @id4003
  Scenario Outline: Verify that left conversation is shown in the Archive [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends encrypted image <Image> to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I see 3 conversation entries
    And I open group conversation details
    And I press leave converstation button
    And I see leave conversation alert
    And I press leave
    And I wait until popover is closed
    And I open archived conversations on iPad
    And I see user <GroupChatName> in contact list
    And I tap on group chat with name <GroupChatName>
    Then I see 4 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message | Image       |
      | user1Name | user2Name | user3Name | TESTCHAT      | hello   | testing.jpg |

  @C2557 @regression @id4004
  Scenario Outline: Verify that left conversation is shown in the Archive [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends encrypted image <Image> to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I see 3 conversation entries
    And I open group conversation details
    And I press leave converstation button
    And I see leave conversation alert
    And I press leave
    And I wait until popover is closed
    And I open archived conversations on iPad
    And I see user <GroupChatName> in contact list
    And I tap on group chat with name <GroupChatName>
    Then I see 4 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | TESTCHAT      | testing.jpg |

  @C2432 @regression @id3999
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending  user (People view) [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact3>,<Contact2>,<Name>
    Given <Contact1> has group chat <GroupChatName> with <Contact3>,<Contact2>,<Name>
    Given Myself sent connection request to <Contact3>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select contact <Contact3>
    Then I see <Contact3> user pending profile popover on iPad
    Then I see remove from group conversation button

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | TESTCHAT      |

  @C2433 @regression @id4000
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending  user (People view) [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact3>,<Contact2>,<Name>
    Given <Contact1> has group chat <GroupChatName> with <Contact3>,<Contact2>,<Name>
    Given Myself sent connection request to <Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select contact <Contact3>
    Then I see <Contact3> user pending profile popover on iPad
    Then I see remove from group conversation button

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | TESTCHAT      |

  @C2736 @regression @id4022
  Scenario Outline: Verify canceling blocking person from participant list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details
    And I press conversation menu button
    And I press menu Block button
    And I click Cancel button
    Then I see conversation action menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2737 @regression @id4023
  Scenario Outline: Verify canceling blocking person from participant list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details
    And I press conversation menu button
    And I press menu Block button
    And I click Cancel button
    Then I see conversation action menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2709 @staging @id2440
  Scenario Outline: Verify length limit for group conversation name [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I try to change group conversation name to empty
    Then I see correct conversation name <GroupChatName>
    When I try to change group conversation name to random with length <MaxGroupChatNameLenght>
    Then I see correct conversation name <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | MaxGroupChatNameLenght |
      | user1Name | user2Name | user3Name | TESTCHAT      | 65                     |

  @C2725 @staging @id3950
  Scenario Outline: Verify length limit for group conversation name [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I try to change group conversation name to empty
    Then I see correct conversation name <GroupChatName>
    When I try to change group conversation name to random with length <MaxGroupChatNameLenght>
    Then I see correct conversation name <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | MaxGroupChatNameLenght |
      | user1Name | user2Name | user3Name | TESTCHAT      | 65                     |