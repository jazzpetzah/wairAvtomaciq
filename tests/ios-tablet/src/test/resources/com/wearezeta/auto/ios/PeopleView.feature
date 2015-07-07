Feature: People View

  @regression @id600
  Scenario Outline: Start group chat with users from contact list [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact1>
    And I tap on connected user <Contact1> on People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact2>
    And I tap on connected user <Contact2> on People picker page
    And I click on Go button
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id2594
  Scenario Outline: Start group chat with users from contact list [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact1>
    And I tap on connected user <Contact1> on People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact2>
    And I tap on connected user <Contact2> on People picker page
    And I click on Go button
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id2434
  Scenario Outline: Start group chat from 1:1 conversation [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details on iPad
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

  @regression @id2653
  Scenario Outline: Start group chat from 1:1 conversation [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details on iPad
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

  @smoke @regression @id2445
  Scenario Outline: Verify leaving group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details on iPad
    And I press leave converstation button on iPad
    Then I press leave on iPad
    And I open archived conversations on iPad
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveGroup    |

  @smoke @regression @id2708
  Scenario Outline: Verify leaving group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details on iPad
    And I press leave converstation button on iPad
    Then I press leave on iPad
    And I open archived conversations on iPad
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveGroup    |

  @staging @id2441
  Scenario Outline: Verify removing from group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details on iPad
    And I select user on iPad group popover <Contact2>
    And I click Remove on iPad
    And I see remove warning message on iPad
    And I confirm remove on iPad
    Then I see that contact <Contact2> is not present on group popover on iPad

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveGroup   |

  @staging @id2981
  Scenario Outline: Verify removing from group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details on iPad
    And I select user on iPad group popover <Contact2>
    And I click Remove on iPad
    And I see remove warning message on iPad
    And I confirm remove on iPad
    Then I see that contact <Contact2> is not present on group popover on iPad

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveGroup   |

  @staging @id2446
  Scenario Outline: Verify editing the conversation name [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details on iPad
    And I press conversation menu button on iPad
    And I press RENAME on the menu on iPad
    And I change group conversation name on iPad popover to <ChatName>
    And I exit the group info iPad popover
    Then I see you renamed conversation to <ChatName> message shown in Group Chat
    And I return to the chat list
    Then I see in contact list group chat named <ChatName>

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | ChatName |
      | user1Name | user2Name | user3Name | RenameGroup   | NewName  |

  @staging @id2922
  Scenario Outline: Verify editing the conversation name [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details on iPad
    And I press conversation menu button on iPad
    And I press RENAME on the menu on iPad
    And I change group conversation name on iPad popover to <ChatName>
    And I exit the group info iPad popover
    Then I see you renamed conversation to <ChatName> message shown in Group Chat
    Then I see in contact list group chat named <ChatName>

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | ChatName |
      | user1Name | user2Name | user3Name | RenameGroup   | NewName  |

  @staging @id2442
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
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details on iPad
    Then I can read the group name <GroupChatName> on the iPad popover
    Then I see that number of participants <ParticipantsNumber> is correct on iPad popover
    Then I see the correct avatar picture for user <Contact1> on iPad
    Then I see the correct avatar picture for user <Contact2> on iPad

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | Picture                      | Color        | Color2       | ParticipantsNumber |
      | user1Name | user2Name | user3Name | GroupInfo     | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow | 3                  |

  @staging @id2989
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
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details on iPad
    Then I can read the group name <GroupChatName> on the iPad popover
    Then I see that number of participants <ParticipantsNumber> is correct on iPad popover
    Then I see the correct avatar picture for user <Contact1> on iPad
    Then I see the correct avatar picture for user <Contact2> on iPad

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | Picture                      | Color        | Color2       |ParticipantsNumber |
      | user1Name | user2Name | user3Name | GroupInfo     | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow | 3				   |
