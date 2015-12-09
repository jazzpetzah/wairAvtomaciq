Feature: People View

  @regression @id1393
  Scenario Outline: Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    #And I swipe up on dialog page to open other user personal page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I wait until <Contact2> exists in backend search results
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact2>
    And I see user <Contact2> found on People picker page
    And I tap on connected user <Contact2> on People picker page
    #And I see Add to conversation button
    And I click on Go button
    And I wait for 2 seconds
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id489
  Scenario Outline: Add user to a group conversation
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press Add button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact3>
    And I see user <Contact3> found on People picker page
    And I tap on connected user <Contact3> on People picker page
    And I click on Go button
    Then I can see You Added <Contact3> message
    When I open group conversation details
    Then I see that conversation has <Number> people
    Then I see <Number> participants avatars

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Number | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | 4      | TESTCHAT      |

  @regression @rc @id1389
  Scenario Outline: Leave from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press leave converstation button
    And I see leave conversation alert
    Then I press leave
    And I open archived conversations
    And I see user <GroupChatName> in contact list
    And I tap on group chat with name <GroupChatName>
    And I see You Left message in group chat

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @regression @rc @id1390
  Scenario Outline: Remove from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select contact <Contact2>
    And I click Remove
    And I see warning message
    And I confirm remove
    Then I see that <Contact2> is not present on group chat page

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @staging @rc @id1396
  Scenario Outline: Verify correct group info page information
    Given There are 3 users where <Name> is me
    Given User <Contact1> change avatar picture to <Picture>
    Given User <Contact1> change name to AQAPICTURECONTACT
    Given User <Contact2> change name to AQAAVATAR TestContact
    Given User <Contact2> change accent color to <Color>
    Given User <Contact1> change accent color to <Color1>
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    And I open group conversation details
    Then I see that the conversation name is correct with <Contact1> and <Contact2>
    And I see that conversation has <ParticipantNumber> people
    And I see the correct participant <Contact1> avatar
    And I see the correct participant <Contact2> avatar

    Examples: 
      | Name      | Contact1  | Contact2  | ParticipantNumber | Picture                      | Color        | Color1       |
      | user1Name | user2Name | user3Name | 3                 | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow |

  @regression @rc @id1406
  Scenario Outline: I can edit the conversation name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I change group conversation name to <ChatName>
    Then I see correct conversation name <ChatName>
    And I exit the group info page
    And I see you renamed conversation to <ChatName> message shown in Group Chat
    And I return to the chat list
    And I see in contact list group chat named <ChatName>

    Examples: 
      | Name      | Contact1  | Contact2  | ChatName | GroupChatName |
      | user1Name | user2Name | user3Name | QAtest   | TESTCHAT      |

  @regression @id531
  Scenario Outline: I can see the individual user profile if I select someone in participants view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I create group chat with <Contact1> and <Contact2>
    #And I swipe up on group chat page
    And I open group conversation details
    And I select contact <Contact2>
    Then I see <Contact2> user profile page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @rc @id339
  Scenario Outline: Tap on participant profiles in group info page participant view
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    #And I swipe up on group chat page
    And I open group conversation details
    And I tap on <GroupCreator> and check email visible and name
    And I tap on <NonConnectedContact> and check email invisible and name

    Examples: 
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |

  @regression @id393 @id2174
  Scenario Outline: Verify you can start 1:1 conversation from a group conversation profile
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select contact <Contact1>
    And I tap on start dialog button on other user profile page
    And I type the message and send it
    Then I see message in the dialog

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @regression @id393
  Scenario Outline: Verify you cannot start a 1:1 conversation from a group chat if the other user is not in your contacts list
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I tap on group chat with name <GroupChatName>
    #And I swipe up on group chat page
    And I open group conversation details
    And I tap on not connected contact <NonConnectedContact>
    Then I see connect to <NonConnectedContact> dialog

    Examples: 
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |

  # broken functionality
  @regression @id555
  Scenario Outline: Verify you can add people from 1:1 people view (view functionality)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    #And I swipe up on dialog page to open other user personal page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    #And I dont see keyboard
    And I tap on connected user <Contact2> on People picker page
    Then I see user <Contact2> on People picker page is selected
    And I tap on connected user <Contact2> on People picker page
    Then I see user <Contact2> on People picker page is NOT selected
    And I tap on connected user <Contact2> on People picker page
    And I tap on Search input on People picker page
    Then I see keyboard
    #And I don't see Add to conversation button
    And I press keyboard Delete button
    Then I see user <Contact2> on People picker page is NOT selected

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @rc @id556
  Scenario Outline: Verify you can add people from 1:1 people view (via keyboard button)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
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
    And I return to the chat list
    And I see Contact list with my name <Name>
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @staging @id559
  Scenario Outline: Verify you can add people from 1:1 people view (cancel view)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
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
    And I close user profile page to return to dialog page
    And I see dialog page
    And I return to the chat list
    And I see Contact list with my name <Name>
    And I don't see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @regression @rc @id1462
  Scenario Outline: Verify silence the conversation
    Given There are 2 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given <Contact> is connected to <Name>
    Given User <Contact> change accent color to <Color>
    Given User <Contact> change name to <NewName>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I open conversation details
    And I press conversation menu button
    And I press menu silence button
    And I close user profile page to return to dialog page
    And I see dialog page
    And I return to the chat list
    And I see Contact list with my name <Name>
    Then I see conversation <Contact> is silenced

    Examples: 
      | Name      | Contact   | Color  | NewName |
      | user1Name | user2Name | Violet | SILENCE |

  @regression @rc @id1335
  Scenario Outline: Verify unsilence the conversation
    Given There are 2 users where <Name> is me
    Given User <Name> change accent color to <Color>
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given <Name> silenced conversation with <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I see conversation <Contact> got silenced before
    When I tap on contact name <Contact>
    And I see dialog page
    And I open conversation details
    And I press conversation menu button
    And I press menu notify button
    And I close user profile page to return to dialog page
    And I see dialog page
    And I return to the chat list
    And I see Contact list with my name <Name>
    Then I see conversation <Contact> is unsilenced

    Examples: 
      | Name      | Contact   | Color  | NewName |
      | user1Name | user2Name | Violet | SILENCE |

  @staging @id712
  Scenario Outline: Verify you can block a person from profile view
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
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

  @regression @id722
  Scenario Outline: Verify you can unblock someone from a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> blocks user <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
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

  @staging @id842
  Scenario Outline: Verify displaying only connected users in the search in group chat
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact3>
    And I see user <Contact3> found on People picker page
    And I click close button to dismiss people view
    And I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press Add button
    And I see People picker page
    And I wait until <Contact2> exists in backend search results
    And I tap on Search input on People picker page
    And I fill in Search field user name <Contact3>
    Then I see that user <Contact3> is NOT found on People picker page

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user3Name | OnlyConnected |

  @regression @id3957
  Scenario Outline: Verify that deleted conversation via participant view isn't going to archive
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> sent message <Message> to conversation <GroupChatName>
    Given User <Name> sent message <Message> to conversation <GroupChatName>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I dont see conversation <GroupChatName> in contact list
    And I open archived conversations
    Then I dont see conversation <GroupChatName> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Message | GroupChatName |
      | user1Name | user2Name | user3Name | testing | ForDeletion   |

  @regression @id3972
  Scenario Outline: Verify removing the content and leaving from the group conversation via participant view
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> sent message <Message> to conversation <GroupChatName>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I select Also Leave option on Delete conversation dialog
    And I confirm delete conversation content
    And I return to the chat list
    And I open search by taping on it
    And I input conversation name <GroupChatName> in Search input
    Then I see conversation <GroupChatName> is NOT presented in Search results
    When I click close button to dismiss people view
    And I dont see conversation <GroupChatName> in contact list
    And I open archived conversations
    Then I dont see conversation <GroupChatName> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Message | GroupChatName |
      | user1Name | user2Name | user3Name | testing | ForDeletion   |

  @staging @id3971
  Scenario Outline: Verify removing the content from the group conversation via participant view
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> sent message <Message> to conversation <GroupChatName>
    Given Contact <Name> sends image <Image> to group conversation <GroupChatName>
    Given Contact <Name> ping conversation <GroupChatName>
    Given User <Contact1> sent message <Message> to conversation <GroupChatName>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I return to the chat list
    And I open search by taping on it
    And I input conversation name <GroupChatName> in Search input
    Then I see conversation <GroupChatName> is presented in Search results
    When I tap on conversation <GroupChatName> in search result
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  | Message | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | testing | ForDeletion   | testing.jpg |

  @staging @id3973
  Scenario Outline: Verify removing the content from 1-to-1 via participant view
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User <Name> sent message <Message> to conversation <Contact1>
    Given Contact <Contact1> sends image <Image> to single user conversation <Name>
    Given Contact <Name> ping conversation <Contact1>
    Given User <Contact1> sent message <Message> to conversation <Name>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I see only 5 messages
    And I open conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I return to the chat list
    And I open search by taping on it
    And I fill in Search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on connected user <Contact1> on People picker page
    And I click open conversation button on People picker page
    Then I see the only message in dialog is system message CONNECTED TO <Contact1>

    Examples: 
      | Name      | Contact1  | Message | GroupChatName | Image       |
      | user1Name | user2Name | testing | ForDeletion   | testing.jpg |

  @staging @id3320
  Scenario Outline: Verify that left conversation is shown in the Archive
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> sent message <Message> to conversation <GroupChatName>
    Given Contact <Contact1> sends image <Image> to group conversation <GroupChatName>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I see only 3 messages
    And I open group conversation details
    And I press leave converstation button
    And I see leave conversation alert
    Then I press leave
    And I open archived conversations
    And I see user <GroupChatName> in contact list
    And I tap on group chat with name <GroupChatName>
    Then I see only 4 messages

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | Message |   Image     |
      | user1Name | user2Name | user3Name | TESTCHAT      | testing | testing.jpg | 

  @staging @id583
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending  user (People view)
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact3>,<Contact2>,<Name>
    Given <Contact1> has group chat <GroupChatName> with <Contact3>,<Contact2>,<Name>
    Given Myself sent connection request to <Contact3>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select contact <Contact3>
    Then I see <Contact3> user pending profile page
    Then I see remove from group conversation button

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | TESTCHAT      |

  @staging @id4021
  Scenario Outline: Verify canceling blocking person from participant list
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
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
