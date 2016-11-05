Feature: People View

  @C2779 @regression @fastLogin
  Scenario Outline: Start group chat with users from contact list [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap input field on Search UI page
    And I type "<Contact2>" in Search UI input field
    And I tap on conversation <Contact2> in search result
    And I tap Create conversation action button on Search UI page
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2896 @regression @fastLogin
  Scenario Outline: Start group chat with users from contact list [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    When I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap input field on Search UI page
    And I type "<Contact2>" in Search UI input field
    And I tap on conversation <Contact2> in search result
    And I tap Create conversation action button on Search UI page
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2704 @regression @fastLogin
  Scenario Outline: Start group chat from 1:1 conversation [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Create Group button on Single user profile page
    And I tap connected user <Contact2> in Search UI on iPad popover
    And I tap connected user <Contact3> in Search UI on iPad popover
    And I tap Create button on iPad popover
    Then I see group chat page with users <Contact1>,<Contact2>,<Contact3>
    And I navigate back to conversations list
    Then I see in conversations list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2715 @regression @rc @fastLogin
  Scenario Outline: Start group chat from 1:1 conversation [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Create Group button on Single user profile page
    And I tap connected user <Contact2> in Search UI on iPad popover
    And I tap connected user <Contact3> in Search UI on iPad popover
    And I tap Create button on iPad popover
    And I see group chat page with users <Contact1>,<Contact2>,<Contact3>
    Then I see in conversations list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2743 @regression @fastLogin
  Scenario Outline: Verify leaving group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I open group conversation details
    Given I tap Open Menu button on Group info page
    When I tap Leave conversation action button
    And I confirm Leave conversation action
    And I open archived conversations
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveGroup    |

  @C2744 @rc @regression @regression @fastLogin
  Scenario Outline: Verify leaving group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I open group conversation details
    Given I tap Open Menu button on Group info page
    When I tap Leave conversation action button
    And I confirm Leave conversation action
    And I open archived conversations
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveGroup    |

  @C2710 @regression @fastLogin
  Scenario Outline: Verify removing from group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I tap Remove From Conversation button on Group participant profile page
    And I confirm Remove conversation action
    And I tap X button on Group participant profile page
    Then I do not see participant name <Contact2> on Group info page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveGroup   |

  @C2716 @rc @regression @fastLogin
  Scenario Outline: Verify removing from group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I tap Remove From Conversation button on Group participant profile page
    And I confirm Remove conversation action
    And I tap X button on Group participant profile page
    Then I do not see participant name <Contact2> on Group info page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveGroup   |

  @C2727 @regression @fastLogin
  Scenario Outline: Verify editing the conversation name [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Rename conversation action button
    And I change group conversation name to "<ChatName>" on Group info page
    And I dismiss popover on iPad
    Then I see You Renamed Conversation message shown in conversation view
    When I navigate back to conversations list
    Then I see conversation <ChatName> in conversations list


    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ChatName |
      | user1Name | user2Name | user3Name | RenameGroup   | NewName  |

  @C2728 @rc @regression @fastLogin
  Scenario Outline: Verify editing the conversation name [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Rename conversation action button
    And I change group conversation name to "<ChatName>" on Group info page
    And I dismiss popover on iPad
    Then I see You Renamed Conversation message shown in conversation view
    And I see conversation <ChatName> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ChatName |
      | user1Name | user2Name | user3Name | RenameGroup   | NewName  |

  @C2711 @regression @fastLogin
  Scenario Outline: Verify correct group info page information [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact1>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    Then I see conversation name "<GroupChatName>" on Group info page
    And I see that number of participants <ParticipantsNumber> is correct on iPad popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ParticipantsNumber |
      | user1Name | user2Name | user3Name | GroupInfo     | 2                  |

  @C2717 @rc @regression @fastLogin
  Scenario Outline: Verify correct group info page information [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    Then I see conversation name "<GroupChatName>" on Group info page
    And I see that number of participants <ParticipantsNumber> is correct on iPad popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ParticipantsNumber |
      | user1Name | user2Name | user3Name | GroupInfo     | 2                  |

  @C2702 @regression @fastLogin
  Scenario Outline: Check any users personal info in group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<ConnectedContact>
    Given Myself has group chat <GroupChatName> with <Contact2>,<ConnectedContact>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    Then I see <Contact2> name on Group participant profile page
    And I see <Contact2Email> email on Group participant profile page

    Examples:
      | Name      | Contact2  | Contact2Email | ConnectedContact | GroupChatName   |
      | user1Name | user2Name | user2Email    | user3Name        | SingleInfoGroup |

  @C2718 @rc @regression @fastLogin
  Scenario Outline: Check any users personal info in group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<ConnectedContact>
    Given Myself has group chat <GroupChatName> with <Contact2>,<ConnectedContact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    Then I see <Contact2> name on Group participant profile page
    And I see <Contact2Email> email on Group participant profile page

    Examples:
      | Name      | Contact2  | Contact2Email | ConnectedContact | GroupChatName   |
      | user1Name | user2Name | user2Email    | user3Name        | SingleInfoGroup |

  @C2719 @regression @fastLogin
  Scenario Outline: Verify you cant start 1:1 with unconnected user in group [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <NonConnectedContact>
    And I see Connect button on Group participant Pending outgoing connection page


    Examples:
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |

  @C2720 @regression @fastLogin
  Scenario Outline: Verify you cant start 1:1 with unconnected user in group [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <NonConnectedContact>
    Then I see Connect button on Group participant Pending outgoing connection page

    Examples:
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |

  @C2714 @regression @fastLogin
  Scenario Outline: Verify opening 1-to-1 conversation from group conversation details [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I tap Open Conversation button on Group participant profile page
    #And I dismiss popover on iPad
    And I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | 1on1FromGroup |

  @C2721 @rc @regression @fastLogin
  Scenario Outline: Verify opening 1-to-1 conversation from group conversation details [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I tap Open Conversation button on Group participant profile page
    And I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | 1on1FromGroup |

  @C2745 @regression @fastLogin
  Scenario Outline: Verify unsilince the conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User Myself silences group conversation <GroupChatName>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I remember the right side state of <GroupChatName> conversation item on iPad
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Unmute conversation action button
    And I dismiss popover on iPad
    And I navigate back to conversations list
    Then I see the state of <GroupChatName> conversation item is changed on iPad

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SILENCE       |

  @C2747 @rc @regression @fastLogin
  Scenario Outline: Verify unsilince the conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User Myself silences group conversation <GroupChatName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I remember the right side state of <GroupChatName> conversation item on iPad
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Unmute conversation action button
    And I dismiss popover on iPad
    Then I see the state of <GroupChatName> conversation item is changed on iPad

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SILENCE       |

  @C2746 @regression @fastLogin
  Scenario Outline: Verify silence the conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I remember the right side state of <GroupChatName> conversation item on iPad
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Mute conversation action button
    And I dismiss popover on iPad
    And I navigate back to conversations list
    Then I see the state of <GroupChatName> conversation item is changed on iPad

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SILENCE       |

  @C2748 @rc @regression @fastLogin
  Scenario Outline: Verify silence the conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given User Myself removes his avatar picture
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I remember the right side state of <GroupChatName> conversation item on iPad
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Mute conversation action button
    And I dismiss popover on iPad
    Then I see the state of <GroupChatName> conversation item is changed on iPad

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | SILENCE       |

  @C2722 @regression @fastLogin
  Scenario Outline: Add someone to a group conversation [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Add People button on Group info page
    And I tap connected user <Contact3> in Search UI on iPad popover
    And I tap Add to Conversation button on iPad popover
    And I open conversation details
    Then I see that number of participants <ParticipantsNumber> is correct on iPad popover

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | ParticipantsNumber |
      | user1Name | user2Name | user3Name | user4Name | AddContact    | 3                  |

  @C2723 @regression @fastLogin
  Scenario Outline: Add someone to a group conversation [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Add People button on Group info page
    And I tap connected user <Contact3> in Search UI on iPad popover
    And I tap Add to Conversation button on iPad popover
    And I open conversation details
    Then I see that number of participants <ParticipantsNumber> is correct on iPad popover

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | ParticipantsNumber |
      | user1Name | user2Name | user3Name | user4Name | AddContact    | 3                  |

  @C2447 @regression @fastLogin
  Scenario Outline: ZIOS-6762 Verify you can block a person from profile view [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Open Menu button on Single user profile page
    And I tap Block conversation action button
    And I confirm Block conversation action
    Then I do not see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2458 @regression @fastLogin
  Scenario Outline: Verify you can block a person from profile view [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Open Menu button on Single user profile page
    And I tap Block conversation action button
    And I confirm Block conversation action
    Then I do not see conversation <Contact1> in conversations list
    And I see conversation <Contact2> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C2450 @regression @fastLogin
  Scenario Outline: Verify you can unblock someone from a group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> blocks user <Contact1>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select participant <Contact1> on Group info page
    And I tap Unblock button on Search UI page
    And I navigate back to conversations list
    Then I see conversation <Contact1> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | UnblockFromGroup |

  @C2459 @regression @fastLogin
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
    And I select participant <Contact1> on Group info page
    And I tap Unblock button on Search UI page
    Then I see conversation <Contact1> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | UnblockFromGroup |

  @C2708 @regression @fastLogin
  Scenario Outline: Verify displaying only connected users in the search in group chat [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Add People button on Group info page
    And I tap input field on Search UI page
    And I type "<Contact3>" in Search UI input field
    Then I see No Results label on Search UI page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user3Name | OnlyConnected |

  @C2724 @regression @fastLogin
  Scenario Outline: Verify displaying only connected users in the search in group chat [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Add People button on Group info page
    And I tap input field on Search UI page
    And I type "<Contact3>" in Search UI input field
    Then I see No Results label on Search UI page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user3Name | OnlyConnected |

  @C2738 @regression @fastLogin
  Scenario Outline: Verify that deleted conversation via participant view is not going to archive [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <GroupChatName> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C2739 @rc @regression @fastLogin
  Scenario Outline: Verify that deleted conversation via participant view is not going to archive [LANDSCAPE]
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
    And I tap Open Menu button on Group info page
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    Then I do not see conversation <GroupChatName> in conversations list
    And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C2740 @regression @fastLogin
  Scenario Outline: (ZIOS-6195) Verify removing the content and leaving from the group conversation via participant view [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Delete conversation action button
    And I tap Also Leave checkbox on Group info page
    And I confirm Delete conversation action
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<GroupChatName>" in Search UI input field
    Then I see the conversation "<GroupChatName>" does not exist in Search results
    When I tap X button on Search UI page
    Then I do not see conversation <GroupChatName> in conversations list
    # Workaround for ZIOS-6195
    # And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C2741 @rc @regression @fastLogin
  Scenario Outline: (ZIOS-6195) Verify removing the content and leaving from the group conversation via participant view [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Delete conversation action button
    And I tap Also Leave checkbox on Group info page
    And I confirm Delete conversation action
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<GroupChatName>" in Search UI input field
    Then I see the conversation "<GroupChatName>" does not exist in Search results
    When I tap X button on Search UI page
    Then I do not see conversation <GroupChatName> in conversations list
    # Workaround for ZIOS-6195
    # And I do not see Archive button at the bottom of conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C1833 @regression @fastLogin
  Scenario Outline: ZIOS-6809 Verify removing the content from the group conversation via participant view [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User Myself securely pings conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends encrypted image <Image> to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<GroupChatName>" in Search UI input field
    When I tap on conversation <GroupChatName> in search result
    Then I see conversation view page
    # TODO: There should be a system message there
    And I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C1834 @regression @fastLogin
  Scenario Outline: ZIOS-6809 Verify removing the content from the group conversation via participant view [LANDSCAPE]
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
    And I tap Open Menu button on Group info page
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<GroupChatName>" in Search UI input field
    When I tap on conversation <GroupChatName> in search result
    Then I see conversation view page
    # TODO: There should be a system message there
    And I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C1835 @regression @fastLogin
  Scenario Outline: Verify removing the content from 1-to-1 via participant view [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given User Myself securely pings conversation <Contact1>
    Given User Myself sends 1 encrypted message to user <Contact1>
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given User <Contact1> sends encrypted image <Image> to single user conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Open Menu button on Single user profile page
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap Open conversation action button on Search UI page
    Then I see 0 default messages in the conversation view
    And I see 0 photos in the conversation view

    Examples:
      | Name      | Contact1  | Image       |
      | user1Name | user2Name | testing.jpg |

  @C1836 @regression @fastLogin
  Scenario Outline: Verify removing the content from 1-to-1 via participant view [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User Myself securely pings conversation <Contact1>
    Given User Myself sends 1 encrypted message to user <Contact1>
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given User <Contact1> sends encrypted image <Image> to single user conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Open Menu button on Single user profile page
    And I tap Delete conversation action button
    And I confirm Delete conversation action
    And I open search UI
    And I accept alert if visible
    And I tap input field on Search UI page
    And I type "<Contact1>" in Search UI input field
    And I tap on conversation <Contact1> in search result
    And I tap Open conversation action button on Search UI page
    Then I see 0 default messages in the conversation view
    And I see 0 photos in the conversation view

    Examples:
      | Name      | Contact1  | Image       |
      | user1Name | user2Name | testing.jpg |

  @C2556 @regression @fastLogin
  Scenario Outline: Verify that left conversation is shown in the Archive [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends encrypted image <Image> to group conversation <GroupChatName>
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Leave conversation action button
    And I confirm Leave conversation action
    And I open archived conversations
    And I see conversation <GroupChatName> in conversations list
    And I tap on group chat with name <GroupChatName>
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | TESTCHAT      | testing.jpg |

  @C2557 @regression @fastLogin
  Scenario Outline: Verify that left conversation is shown in the Archive [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends encrypted image <Image> to group conversation <GroupChatName>
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap Open Menu button on Group info page
    And I tap Leave conversation action button
    And I confirm Leave conversation action
    And I open archived conversations
    And I see conversation <GroupChatName> in conversations list
    And I tap on group chat with name <GroupChatName>
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | TESTCHAT      | testing.jpg |

  @C2432 @regression @fastLogin
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending user (People view) [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact3>,<Contact2>,<Name>
    Given <Contact1> has group chat <GroupChatName> with <Contact3>,<Contact2>,<Name>
    Given Myself sent connection request to <Contact3>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select participant <Contact3> on Group info page
    Then I see <Contact3> name on Group participant Pending outgoing connection page
    And I see Remove From Group button on Group participant Pending outgoing connection page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | TESTCHAT      |

  @C2433 @regression @fastLogin
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending user (People view) [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact3>,<Contact2>,<Name>
    Given <Contact1> has group chat <GroupChatName> with <Contact3>,<Contact2>,<Name>
    Given Myself sent connection request to <Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select participant <Contact3> on Group info page
    Then I see <Contact3> name on Group participant Pending outgoing connection page
    And I see Remove From Group button on Group participant Pending outgoing connection page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | TESTCHAT      |

  @C2736 @regression @fastLogin
  Scenario Outline: Verify canceling blocking person from participant list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Open Menu button on Single user profile page
    And I tap Block conversation action button
    And I tap Cancel conversation action button
    Then I see conversation actions menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2737 @regression @fastLogin
  Scenario Outline: Verify canceling blocking person from participant list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I tap Open Menu button on Single user profile page
    And I tap Block conversation action button
    And I tap Cancel conversation action button
    Then I see conversation actions menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C2709 @regression @fastLogin
  Scenario Outline: Verify length limit for group conversation name [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I change group conversation name to "" on Group info page
    Then I see conversation name "<GroupChatName>" on Group info page
    When I try to change group conversation name to random with length <ActualLength> on Group info page
    Then I see the length of group conversation name equals to <ExpectedLength> on Group info page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ActualLength | ExpectedLength |
      | user1Name | user2Name | user3Name | TESTCHAT      | 70           | 64             |

  @C2725 @regression @fastLogin
  Scenario Outline: Verify length limit for group conversation name [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I change group conversation name to "" on Group info page
    Then I see conversation name "<GroupChatName>" on Group info page
    When I try to change group conversation name to random with length <ActualLength> on Group info page
    Then I see the length of group conversation name equals to <ExpectedLength> on Group info page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ActualLength | ExpectedLength |
      | user1Name | user2Name | user3Name | TESTCHAT      | 70           | 64             |