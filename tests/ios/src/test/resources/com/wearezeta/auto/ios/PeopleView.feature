Feature: People View

  @smoke @id1393
  Scenario Outline: Start group chat with users from contact list
    Given I have 1 users and 2 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page to open other user personal page
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact2>
    And I see user <Contact2> found on People picker page
    And I tap on connected user <Contact2> on People picker page
    #And I see Add to conversation button
    And I click on Go button
    Then I see group chat page with users <Contact1> <Contact2>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @regression @id489
  Scenario Outline: Add user to a group conversation
    Given I have 1 users and 3 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I return to the chat list
    And I see <Contact1> and <Contact2> chat in contact list
    And I tap on a group chat with <Contact1> and <Contact2>
    And I swipe up on group chat page
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
      | Login   | Password    | Name    | Contact1    | Contact2    | Contact3    | Number |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 | 4      |
      
#conflicting with other contacts in contact list
  @smoke @id1389
  Scenario Outline: Leave from group chat
    Given I have 1 users and 2 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I swipe up on group chat page
    And I press leave converstation button
    And I see leave conversation alert
    Then I press leave
    And I open archived conversations
    And I see <Contact1> and <Contact2> chat in contact list
    And I tap on a group chat with <Contact1> and <Contact2>
    And I see You Left message in group chat

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @smoke @id1390
  Scenario Outline: Remove from group chat
    Given I have 1 users and 2 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I swipe up on group chat page
    And I select contact <Contact2>
    And I click Remove
    And I see warning message
    And I confirm remove
    Then I see that <Contact2> is not present on group chat page

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @regression @id1396
  Scenario Outline: Verify correct group info page information
    Given I have 1 users and 0 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I swipe up on group chat page
    Then I see that the conversation name is correct with <Contact1> and <Contact2>
    And I see the correct number of participants in the title <ParticipantNumber>
    And I see the correct participant avatars

    Examples: 
      | Login   | Password    | Name    | Contact1          | Contact2              | ParticipantNumber |
      | aqaUser | aqaPassword | aqaUser | aqaPictureContact | aqaAvatar TestContact | 3                 |

  @smoke @id1406
  Scenario Outline: I can edit the conversation name
    Given I have 1 users and 2 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I swipe up on group chat page
    And I change conversation name to <ChatName>
    Then I see correct conversation name <ChatName>
    And I exit the group info page
    And I see you renamed conversation to <ChatName> message shown in Group Chat
    And I return to the chat list
    And I see in contact list group chat named <ChatName>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | QAtest   |

  @regression @id531
  Scenario Outline: I can see the individual user profile if I select someone in participants view
    Given I have 1 users and 2 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I swipe up on group chat page
    And I select contact <Contact2>
    Then I see <Contact2> user profile page

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  #fails to check email of first user due to defect IOS-990
  @staging @id339
  Scenario Outline: Tap on participant profiles in group info page participant view
    Given I have 2 users and 0 contacts for 2 users
    Given I have group chat named <GroupChatName> with an unconnected user, made by <GroupCreator>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I swipe up on group chat page
    And I tap on all of the participants and check their emails and names

    Examples: 
      | Login   | Password    | Name    | GroupCreator      | GroupChatName |
      | aqaUser | aqaPassword | aqaUser | aqaPictureContact | TESTCHAT      |

  @staging @id393
  Scenario Outline: Verify you can start 1:1 conversation from a group conversation profile
    Given I have 1 users and 2 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    And I swipe up on group chat page
    And I select contact <Contact1>
    And I tap on start dialog button on other user profile page
    And I type the message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

  @staging @id393
  Scenario Outline: Verify you cannot start a 1:1 conversation from a group chat if the other user is not in your contacts list
    Given I have 2 users and 1 contacts for 2 users
    Given I have group chat named <ChatName> with an unconnected user, made by <Contact1>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on group chat with name <ChatName>
    And I swipe up on group chat page
    And I tap on not connected contact <Contact2>
    Then I see connect to <Contact2> dialog

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName   |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | yourUser    | QAtestChat |

	#ZIOS-2035            
  @regression @id555
  Scenario Outline: Verify you can add people from 1:1 people view (view functionality)
    Given I have 1 users and 2 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page to open other user personal page
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
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |
            
  @regression @id557
  Scenario Outline: Verify you can add people from 1:1 people view (via keyboard button)
    Given I have 1 users and 3 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page to open other user personal page
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact2>
    And I see user <Contact2> found on People picker page
    And I don't see Add to conversation button
    And I click on connected user <Contact2> avatar on People picker page
    And I click on connected user <Contact3> avatar on People picker page
    And I click on Go button
    And I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I swipe right on group chat page
    And I see Contact list with my name <Name>
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | Contact3    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 |
           
  @regression @id559
  Scenario Outline: Verify you can add people from 1:1 people view (cancel view)
    Given I have 1 users and 3 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page to open other user personal page
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
      | Login   | Password    | Name    | Contact1    | Contact2    | Contact3    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | aqaContact3 |

  #Known issue ZIOS-1711. Muted test due to crash after relogin.
  #@staging @id597 @mute
  #Scenario Outline: Verify the new conversation is created on the other end (1:1 source)
    #Given I Sign in using login <Login> and password <Password>
    #And I see Contact list with my name <Name>
    #When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    #And I change conversation name to <ChatName>
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
    #And I change conversation name to <ChatName>
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
