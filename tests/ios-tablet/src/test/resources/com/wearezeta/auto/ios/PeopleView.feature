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

  @regression @id2653
  Scenario Outline: Start group chat from 1:1 conversation [LANDSCAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
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

  @smoke @regression @id2445
  Scenario Outline: Verify leaving group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
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
    And I open group conversation details
    And I press leave converstation button on iPad
    Then I press leave on iPad
    And I open archived conversations on iPad
    And I tap on group chat with name <GroupChatName>
    Then I see You Left message in group chat

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | LeaveGroup    |

  @regression @id2441
  Scenario Outline: Verify removing from group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I click Remove on iPad
    And I see remove warning message on iPad
    And I confirm remove on iPad
    Then I see that contact <Contact2> is not present on group popover on iPad

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveGroup   |

  @regression @id2981
  Scenario Outline: Verify removing from group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I click Remove on iPad
    And I see remove warning message on iPad
    And I confirm remove on iPad
    Then I see that contact <Contact2> is not present on group popover on iPad

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveGroup   |

  @regression @id2446
  Scenario Outline: Verify editing the conversation name [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press conversation menu button on iPad
    And I press RENAME on the menu on iPad
    And I change group conversation name on iPad popover to <ChatName>
    And I exit the group info iPad popover
    Then I see you renamed conversation to <ChatName> message shown in Group Chat
    And I swipe right on group chat page
    Then I see in contact list group chat named <ChatName>

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | ChatName |
      | user1Name | user2Name | user3Name | RenameGroup   | NewName  |

  @regression @id2922
  Scenario Outline: Verify editing the conversation name [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
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
    And I open group conversation details
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
    And I open group conversation details
    Then I can read the group name <GroupChatName> on the iPad popover
    Then I see that number of participants <ParticipantsNumber> is correct on iPad popover
    Then I see the correct avatar picture for user <Contact1> on iPad
    Then I see the correct avatar picture for user <Contact2> on iPad

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | Picture                      | Color        | Color2       |ParticipantsNumber |
      | user1Name | user2Name | user3Name | GroupInfo     | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow | 3				   |

  @staging @id2432
  Scenario Outline: Check any users personal info in group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<ConnectedContact>
    Given Myself has group chat <GroupChatName> with <Contact2>,<ConnectedContact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    Then I see email and name of user <Contact2> on iPad popover

    Examples: 
      | Name      | Contact2  | ConnectedContact | GroupChatName   |
      | user1Name | user2Name | user3Name        | SingleInfoGroup |

  @staging @id3007
  Scenario Outline: Check any users personal info in group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<ConnectedContact>
    Given Myself has group chat <GroupChatName> with <Contact2>,<ConnectedContact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    Then I see email and name of user <Contact2> on iPad popover

    Examples: 
      | Name      | Contact2  | ConnectedContact | GroupChatName   |
      | user1Name | user2Name | user3Name        | SingleInfoGroup |

  @staging @id3085
  Scenario Outline: Verify you cant start 1:1 with unconnected user in group [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <NonConnectedContact>
    Then I see connect to <NonConnectedContact> dialog

    Examples: 
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |

  @staging @id3086
  Scenario Outline: Verify you cant start 1:1 with unconnected user in group [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <NonConnectedContact>
    Then I see connect to <NonConnectedContact> dialog

    Examples: 
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |
     
  @staging @id2612
  Scenario Outline: Verify opening 1-to-1 conversation from group conversation details [PORTRAIT]
  	Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I tap on start dialog button on other user profile page
    Then I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact2  | Contact3   | GroupChatName   |
      | user1Name | user2Name | user3Name  | 1on1FromGroup   |
  
  @staging @id3087
  Scenario Outline: Verify opening 1-to-1 conversation from group conversation details [LANDSCAPE]
  	Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>,<Contact3>
    Given Myself has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select user on iPad group popover <Contact2>
    And I tap on start dialog button on other user profile page
    Then I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact2  | Contact3   | GroupChatName   |
      | user1Name | user2Name | user3Name  | 1on1FromGroup   |
  
  @torun @staging @id2455
  Scenario Outline: Verify unsilince the conversation [PORTRAIT] 
  	Given There are 3 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Name> silenced group conversation with <GroupChatName>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>  
    And I see conversation <GroupChatName> got silenced before
    
    Examples: 
      | Name      | Contact1   | Contact2  | Color    | GroupChatName |
      | user1Name | user2Name  | user3Name | Violet   | SILENCE   	  |
   
  @staging @id3208
  Scenario Outline: Verify unsilince the conversation [LANDSCAPE]
  	Given There are 3 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Name> silenced group conversation with <GroupChatName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name> 
    And I see conversation <GroupChatName> got silenced before
    
    Examples: 
      | Name      | Contact1   | Contact2  | Color    | GroupChatName |
      | user1Name | user2Name  | user3Name | Violet   | SILENCE   	  |
  
  @staging @id2456
  Scenario Outline: Verify silence the conversation [PORTRAIT]
  	Given There are 3 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press conversation menu button on iPad
    And I click SILENCE button on iPad ellipsis menu
    And I exit the group info iPad popover
    And I see dialog page
    And I swipe right on Dialog page
    And I see Contact list with my name <Name>
    Then I see conversation <GroupChatName> is silenced

    Examples: 
      | Name      | Contact1   | Contact2  | Color    | GroupChatName |
      | user1Name | user2Name  | user3Name | Violet   | SILENCE   	  |
  
  @staging @id3209
  Scenario Outline: Verify silence the conversation [LANDSCAPE]
  	Given There are 3 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I press conversation menu button on iPad
    And I click SILENCE button on iPad ellipsis menu
    And I exit the group info iPad popover
    And I see dialog page
    And I see Contact list with my name <Name>
    Then I see conversation <GroupChatName> is silenced

    Examples: 
      | Name      | Contact1   | Contact2  | Color    | GroupChatName |
      | user1Name | user2Name  | user3Name | Violet   | SILENCE   	  |
