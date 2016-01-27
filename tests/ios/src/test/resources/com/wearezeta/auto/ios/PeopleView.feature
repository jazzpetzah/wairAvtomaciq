Feature: People View

  @C985 @regression @id1393
  Scenario Outline: (ZIOS-5442) Start group chat with users from contact list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
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
    And I tap on conversation <Contact2> in search result
    #And I see Add to conversation button
    And I click on Go button
    And I wait for 2 seconds
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C973 @regression @id489
  Scenario Outline: Add user to a group conversation
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press Add button
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact3>
    And I see user <Contact3> found on People picker page
    And I tap on conversation <Contact3> in search result
    And I click on Go button
    Then I can see You Added <Contact3> message
    When I open group conversation details
    Then I see that conversation has <Number> people
    Then I see <Number> participants avatars

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Number | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | 4      | TESTCHAT      |

  @C3175 @regression @rc @id1389
  Scenario Outline: Leave from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
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

  @C3169 @regression @rc @id1390
  Scenario Outline: Remove from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select contact <Contact2>
    And I click Remove
    And I see warning message
    And I confirm remove
    And I click close user profile page button
    Then I see that <Contact2> is not present on group chat info page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @C3173 @staging @rc @id1396
  Scenario Outline: Verify correct group info page information
    Given There are 3 users where <Name> is me
    Given User <Contact1> changes avatar picture to <Picture>
    Given User <Contact1> changes name to AQAPICTURECONTACT
    Given User <Contact2> changes name to AQAAVATAR
    Given User <Contact2> changes accent color to <Color>
    Given User <Contact1> changes accent color to <Color1>
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I create group chat with <Contact1> and <Contact2>
    And I open group conversation details
    Then I see that the conversation name is correct with <Contact1> and <Contact2>
    And I see that conversation has <ParticipantNumber> people
    And I see the correct participant <Contact1> avatar
    And I see the correct participant <Contact2> avatar

    Examples:
      | Name      | Contact1  | Contact2  | ParticipantNumber | Picture                      | Color        | Color1       |
      | user1Name | user2Name | user3Name | 2                 | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow |

  @C3174 @regression @rc @id1406
  Scenario Outline: I can edit the conversation name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I change group conversation name to <ChatName>
    Then I see correct conversation name <ChatName>
    And I exit the group info page
    And I see you renamed conversation to <ChatName> message shown in Group Chat
    And I navigate back to conversations list
    And I see in contact list group chat named <ChatName>

    Examples:
      | Name      | Contact1  | Contact2  | ChatName | GroupChatName |
      | user1Name | user2Name | user3Name | QAtest   | TESTCHAT      |

  @C3172 @regression @rc @id339
  Scenario Outline: Tap on participant profiles in group info page participant view
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select contact <GroupCreator>
    And I verify username <GroupCreator> on Other User Profile page is displayed
    And I verify user email for <GroupCreator> on Other User Profile page is displayed
    And I click close user profile page button
    And I select contact <NonConnectedContact>
    Then I verify username <NonConnectedContact> on Other User Profile page is displayed

    Examples:
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |

  @C988 @regression @id2174
  Scenario Outline: Verify you can start 1:1 conversation from a group conversation profile
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select contact <Contact1>
    And I tap on start dialog button on other user profile page
    And I type the default message and send it
    Then I see 1 default message in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @C972 @regression @id393
  Scenario Outline: Verify you cannot start a 1:1 conversation from a group chat if the other user is not in your contacts list
    Given There are 3 users where <Name> is me
    Given <GroupCreator> is connected to me
    Given <GroupCreator> is connected to <NonConnectedContact>
    Given <GroupCreator> has group chat <GroupChatName> with Myself,<NonConnectedContact>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I tap on group chat with name <GroupChatName>
    #And I swipe up on group chat page
    And I open group conversation details
    And I tap on not connected contact <NonConnectedContact>
    Then I see connect to <NonConnectedContact> dialog

    Examples:
      | Name      | GroupCreator | NonConnectedContact | GroupChatName |
      | user1Name | user2Name    | user3Name           | TESTCHAT      |

  # broken functionality
  @C975 @regression @id555
  Scenario Outline: Verify you can add people from 1:1 people view (view functionality)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see dialog page
    #And I swipe up on dialog page to open other user personal page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    #And I dont see keyboard
    And I tap on conversation <Contact2> in search result
    Then I see user <Contact2> on People picker page is selected
    And I tap on conversation <Contact2> in search result
    Then I see user <Contact2> on People picker page is NOT selected
    And I tap on conversation <Contact2> in search result
    And I tap on Search input on People picker page
    #Then I see keyboard
    #And I don't see Add to conversation button
    And I press keyboard Delete button
    Then I see user <Contact2> on People picker page is NOT selected

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C986 @regression @rc @id556
  Scenario Outline: Verify you can add people from 1:1 people view (via keyboard button)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I tap on Search input on People picker page
    And I see user <Contact2> found on People picker page
    And I don't see Add to conversation button
    And I tap on conversation <Contact2> in search result
    And I see user <Contact3> found on People picker page
    And I tap on conversation <Contact3> in search result
    And I click on Go button
    And I see group chat page with users <Contact1>,<Contact2>,<Contact3>
    And I navigate back to conversations list
    And I see conversations list
    And I see in contact list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C977 @staging @id559
  Scenario Outline: Verify you can add people from 1:1 people view (cancel view)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see dialog page
    #And I swipe up on dialog page to open other user personal page
    And I open conversation details
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I scroll up page a bit
    And I dont see keyboard
    And I tap on conversation <Contact2> in search result
    And I tap on conversation <Contact3> in search result
    And I click close button to dismiss people view
    And I see <Contact1> user profile page
    And I press Add button
    And I see People picker page
    And I see user <Contact2> on People picker page is NOT selected
    And I see user <Contact3> on People picker page is NOT selected
    And I click close button to dismiss people view
    And I see <Contact1> user profile page
    And I click close user profile page button
    And I see dialog page
    And I navigate back to conversations list
    And I see conversations list
    And I don't see in contact list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C3170 @regression @rc @id1462
  Scenario Outline: Verify silence the conversation
    Given There are 2 users where <Name> is me
    Given User Myself removes his avatar picture
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I see conversations list
    When I remember the state of <Contact> conversation item
    And I tap on contact name <Contact>
    And I see dialog page
    And I open conversation details
    And I press conversation menu button
    And I press menu silence button
    And I click close user profile page button
    And I see dialog page
    And I navigate back to conversations list
    And I see conversations list
    Then I see the state of <Contact> conversation item is changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C3171 @regression @rc @id1335
  Scenario Outline: Verify unsilence the conversation
    Given There are 2 users where <Name> is me
    Given User Myself removes his avatar picture
    Given <Contact> is connected to Myself
    Given Myself silenced conversation with <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I remember the state of <Contact> conversation item
    And I tap on contact name <Contact>
    And I see dialog page
    And I open conversation details
    And I press conversation menu button
    And I press menu notify button
    And I click close user profile page button
    And I see dialog page
    And I navigate back to conversations list
    And I see conversations list
    Then I see the state of <Contact> conversation item is changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C26 @regression @id712
  Scenario Outline: Verify you can block a person from profile view
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
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

  @C29 @regression @id722
  Scenario Outline: Verify you can unblock someone from a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Name> blocks user <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select contact <Contact1>
    And I see <Contact1> user profile page
    And I unblock user
    Then I see dialog page
    And I navigate back to conversations list
    Then I see conversation <Contact1> is selected in list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Name | user2Name | user3Name | UnblockFromGroup |

  @C980 @regression @id842
  Scenario Outline: Verify displaying only connected users in the search in group chat
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
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
    And I see People picker page
    And I wait until <Contact2> exists in backend search results
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact3>
    Then I see that user <Contact3> is NOT found on People picker page

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user3Name | OnlyConnected |

  @C1829 @regression @id3957
  Scenario Outline: Verify that deleted conversation via participant view isn't going to archive
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends 1 message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I dont see conversation <GroupChatName> in contact list
    And I open archived conversations
    Then I dont see conversation <GroupChatName> in contact list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C1831 @regression @rc @id3972 @ZIOS-5247
  Scenario Outline: Verify removing the content and leaving from the group conversation via participant view
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Name> sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I select Also Leave option on Delete conversation dialog
    And I confirm delete conversation content
    And I open search by taping on it
    And I input in People picker search field conversation name <GroupChatName>
    Then I see conversation <GroupChatName> is NOT presented in Search results
    When I click close button to dismiss people view
    And I dont see conversation <GroupChatName> in contact list
    And I open archived conversations
    Then I dont see conversation <GroupChatName> in contact list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ForDeletion   |

  @C1830 @staging @id3971
  Scenario Outline: Verify removing the content from the group conversation via participant view
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself securely pings conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User Myself sends encrypted image <Picture> to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I press conversation menu button
    And I click delete menu button
    And I confirm delete conversation content
    And I open search by taping on it
    And I input in People picker search field conversation name <GroupChatName>
    Then I see conversation <GroupChatName> is presented in Search results
    When I tap on conversation <GroupChatName> in search result
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Picture     |
      | user1Name | user2Name | user3Name | ForDeletion   | testing.jpg |

  @C1832 @regression @rc @id3973
  Scenario Outline: Verify removing the content from 1-to-1 via participant view
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
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
    And I open search by taping on it
    And I input in People picker search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on conversation <Contact1> in search result
    And I click open conversation button on People picker page
    Then I see the only message in dialog is system message CONNECTED TO <Contact1>

    Examples:
      | Name      | Contact1  | Image       |
      | user1Name | user2Name | testing.jpg |

  @C849 @regression @id3320
  Scenario Outline: Verify that left conversation is shown in the Archive
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends encrypted image <Image> to group conversation <GroupChatName>
    Given User Myself sends 1 encrypted message to group conversation <GroupChatName>
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I see 3 conversation entries
    And I open group conversation details
    And I press leave converstation button
    And I see leave conversation alert
    Then I press leave
    And I open archived conversations
    And I see user <GroupChatName> in contact list
    And I tap on group chat with name <GroupChatName>
    Then I see 4 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       |
      | user1Name | user2Name | user3Name | TESTCHAT      | testing.jpg |

  @C42 @regression @id583
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending  user (People view)
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact3>,<Contact2>,<Name>
    Given <Contact1> has group chat <GroupChatName> with <Contact3>,<Contact2>,<Name>
    Given Myself sent connection request to <Contact3>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select contact <Contact3>
    Then I see <Contact3> user pending profile page
    Then I see remove from group conversation button

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | TESTCHAT      |

  @C1828 @regression @id4021
  Scenario Outline: Verify canceling blocking person from participant list
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
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

  @C984 @regression @id1246
  Scenario Outline: Verify length limit for group conversation name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
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
