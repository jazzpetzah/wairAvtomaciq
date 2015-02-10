Feature: People View

  @smoke @id1393
  Scenario Outline: Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    #And I swipe up on dialog page to open other user personal page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact2>
    And I see user <Contact2> found on People picker page
    And I tap on connected user <Contact2> on People picker page
    #And I see Add to conversation button
    And I click on Go button
    And I wait for 2 seconds
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2    |
      | user1Email | user1Password | user1Name | user2Name   | user3Name   |

  @regression @id489
  Scenario Outline: Add user to a group conversation
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I return to the chat list
    And I see <Contact1> and <Contact2> chat in contact list
    And I tap on a group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    And I open group conversation details
    And I press Add button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact3>
    And I see user <Contact3> found on People picker page
    And I tap on connected user <Contact3> on People picker page
    And I click on Go button
    Then I see that conversation has <Number> people
    And I see <Number> participants avatars
    When I exit the group info page
    And I can see You Added <Contact3> message

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2    | Contact3  | Number |
      | user1Email | user1Password | user1Name | user2Name   | user3Name   | user4Name | 4      |
      
  @smoke @id1389
  Scenario Outline: Leave from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    And I open group conversation details
    And I press leave converstation button
    And I see leave conversation alert
    Then I press leave
    And I open archived conversations
    And I see <Contact1> and <Contact2> chat in contact list
    And I tap on a group chat with <Contact1> and <Contact2>
    And I see You Left message in group chat

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  |

  @smoke @id1390
  Scenario Outline: Remove from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    And I open group conversation details
    And I select contact <Contact2>
    And I click Remove
    And I see warning message
    And I confirm remove
    Then I see that <Contact2> is not present on group chat page

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  |

  @regression @id1396
  Scenario Outline: Verify correct group info page information
    Given There are 3 users where <Name> is me
    Given User <Contact1> change avatar picture to <Picture>
    Given User <Contact1> change  name to AQAPICTURECONTACT
    Given User <Contact2> change  name to AQAAVATAR TestContact
    Given User <Contact2> change  accent color to <Color>
    Given User <Contact1> change  accent color to <Color1>
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    And I open group conversation details
    Then I see that the conversation name is correct with <Contact1> and <Contact2>
    And I see that conversation has <ParticipantNumber> people
    And I see the correct participant avatars

    Examples:
      | Login      | Password      | Name      | Contact1    | Contact2   | ParticipantNumber | Picture                      | Color        | Color1       |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  | 3                 | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow |

  @smoke @id1406
  Scenario Outline: I can edit the conversation name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    And I open group conversation details
    And I change group conversation name to <ChatName>
    Then I see correct conversation name <ChatName>
    And I exit the group info page
    And I see you renamed conversation to <ChatName> message shown in Group Chat
    And I return to the chat list
    And I see in contact list group chat named <ChatName>

    Examples:
      | Login      | Password      | Name      | Contact1    | Contact2   | ChatName |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  | QAtest   |

  @regression @id531
  Scenario Outline: I can see the individual user profile if I select someone in participants view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    And I open group conversation details
    And I select contact <Contact2>
    Then I see <Contact2> user profile page

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  |

  @regression @id339
  Scenario Outline: Tap on participant profiles in group info page participant view
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    #And I swipe up on group chat page
    And I open group conversation details
    And I tap on <GroupCreator> and check email visible and name
    And I tap on <NonConnectedContact> and check email invisible and name

    Examples:
      | Login      | Password      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Email | user1Password | user1Name | user2Name    | user3Name           | TESTCHAT      |

  @regression @id393
  Scenario Outline: Verify you can start 1:1 conversation from a group conversation profile
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    And I open group conversation details
    And I select contact <Contact1>
    And I tap on start dialog button on other user profile page
    And I type the message and send it
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  |

  @regression @id393
  Scenario Outline: Verify you cannot start a 1:1 conversation from a group chat if the other user is not in your contacts list
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on group chat with name <GroupChatName>
    #And I swipe up on group chat page
    And I open group conversation details
    And I tap on not connected contact <NonConnectedContact>
    Then I see connect to <NonConnectedContact> dialog

    Examples: 
      | Login      | Password      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Email | user1Password | user1Name | user2Name    | user3Name           | TESTCHAT      |

	# broken functionality           
  @regression @id555
  Scenario Outline: Verify you can add people from 1:1 people view (view functionality)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    #And I swipe up on dialog page to open other user personal page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I dont see keyboard
    And I tap on connected user <Contact2> on People picker page
    And I see user <Contact2> on People picker page is selected
    And I tap on connected user <Contact2> on People picker page
    And I see user <Contact2> on People picker page is NOT selected
    And I tap on connected user <Contact2> on People picker page
    And I tap on Search input on People picker page
    And I see keyboard
    And I don't see Add to conversation button
    And I press keyboard Delete button
    And I see user <Contact2> on People picker page is NOT selected

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  |
            
  @regression @id557
  Scenario Outline: Verify you can add people from 1:1 people view (via keyboard button)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    #And I swipe up on dialog page to open other user personal page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I tap on Search input on People picker page
    #And I input in People picker search field user name <Contact2>
    And I see user <Contact2> found on People picker page
    And I don't see Add to conversation button
    And I click on connected user <Contact2> avatar on People picker page
    And I see user <Contact3> found on People picker page
    And I click on connected user <Contact3> avatar on People picker page
    And I click on Go button
    And I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I swipe right on group chat page
    And I see Contact list with my name <Name>
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   | Contact3  |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  | user4Name |
           
  @regression @id559
  Scenario Outline: Verify you can add people from 1:1 people view (cancel view)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    #And I swipe up on dialog page to open other user personal page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I scroll up page a bit
    And I dont see keyboard
    And I tap on connected user <Contact2> on People picker page
    And I tap on connected user <Contact3> on People picker page
    And I click close button to dismiss people view
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I see user <Contact2> on People picker page is NOT selected
    And I see user <Contact3> on People picker page is NOT selected
    And I click close button to dismiss people view
    And I see <Contact1> user profile page
    And I swipe down on other user profile page
    And I see dialog page
    And I swipe right on Dialog page
    And I see Contact list with my name <Name>
    And I don't see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   | Contact3  |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  | user4Name |

  #Known issue ZIOS-1711. Muted test due to crash after relogin.
  #@staging @id597 @mute
  #Scenario Outline: Verify the new conversation is created on the other end (1:1 source)
    #Given I Sign in using login <Login> and password <Password>
    #And I see Contact list with my name <Name>
    #When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    #And I change group conversation name to <ChatName>
    #And I swipe down on group chat info page
    #And I swipe right on Dialog page
    #And I tap on my name <Name>
    #And I click on Settings button on personal page
    #And I click Sign out button from personal page
    #And I Sign in using login <Contact1> and password <Password>
    #And I see Personal page
    #And I swipe right on the personal page
    #And I see in contact list group chat named <ChatName>
    #And I tap on my name <Contact1>
    #And I click on Settings button on personal page
    #And I click Sign out button from personal page
    #And I Sign in using login <Contact2> and password <Password>
    #And I see Personal page
    #And I swipe right on the personal page
    #Then I see in contact list group chat named <ChatName>

    #Examples: 
      #| Login   | Password    | Name    | Contact1    | Contact2    | ChatName   |
      #| aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | QAtestChat |

  #Muted due to app quit on logout workaround
  #@staging @id602 @mute
  #Scenario Outline: Verify new users are added to a group conversation on the other end
    #Given I Sign in using login <Login> and password <Password>
    #And I see Contact list with my name <Name>
    #When I create group chat with <Contact1> and <Contact2>
    #And I send predefined message <message>
    #And I see message in group chat <message>
    #And I swipe down on group chat page
    #And I swipe up on group chat page in simulator
    #And I change group conversation name to <ChatName>
    #And I add to existing group chat contact <Contact3>
    #And I swipe down on group chat info page
    #And I swipe right on Dialog page
    #And I tap on my name <Name>
    #And I click on Settings button on personal page
    #And I click Sign out button from personal page
    #And I Sign in using login <Contact3> and password <Password>
    #And I see Personal page
    #And I swipe right on the personal page
    #And I see in contact list group chat named <ChatName>
    #And I tap on group chat with name <ChatName>
    #And I see message in group chat <message>

    #Examples: 
      #| Login   | Password    | Name    | Contact1    | Contact2    | Contact3    | ChatName   | message      |
      #| aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 | QAtestChat | Test Message |
