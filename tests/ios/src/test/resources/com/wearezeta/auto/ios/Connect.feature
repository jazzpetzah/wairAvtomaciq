Feature: Connect

  @C1034 @rc @regression @id2541
  Scenario Outline: Send invitation message to a user
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I tap on Search input on People picker page
    Given I wait until <ContactEmail> exists in backend search results
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I see connect to <Contact> dialog
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page

    Examples: 
      | Name      | Contact   | ContactEmail | Contact2  |
      | user1Name | user2Name | user2Email   | user3Name |

  @C102 @rc @regression @id1475
  Scenario Outline: (ZIOS-5508 Simulator issue)Get invitation message from user
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I wait for 2 seconds
    And I see Hello connect message from user <Contact> on Pending request page
    And I click Connect button on Pending request page
    And I wait for 2 seconds
    And I navigate back to conversations list
    Then I see first item in contact list named <Contact>

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C987 @rc @regression @id576
  Scenario Outline: Send connection request to unconnected participant in a group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <GroupCreator>
    Given <GroupCreator> is connected to <UnconnectedUser>
    Given <GroupCreator> has group chat <GroupChatName> with <UnconnectedUser>,Myself
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select participant <UnconnectedUser>
    And I click Connect button on connect to dialog
    And I see Connect dialog is closed
    And I close group info page
    And I navigate back to conversations list
    Then I see first item in contact list named <UnconnectedUser>

    Examples: 
      | Name      | GroupCreator | GroupChatName | UnconnectedUser |
      | user1Name | user2Name    | TESTCHAT      | user3Name       |

  @C22 @regression @id579
  Scenario Outline: Verify transitions between connection requests (ignoring)
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I click on Ignore button on Pending requests page <SentRequests> times
    And I dont see Pending request link in contact list
    And I wait until <Contact1> exists in backend search results
    And I open search by taping on it
    And I tap on Search input on People picker page
    And I search for user name <Contact1> and tap on it on People picker page
    Then I see Pending request page

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C21 @regression @id577
  Scenario Outline: Verify transitions between connection requests (connecting)
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I click on Connect button on Pending requests page <SentRequests> times
    And I navigate back to conversations list
    Then I dont see Pending request link in contact list
    And I see user <Contact1> in contact list
    And I see user <Contact2> in contact list
    And I see user <Contact3> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @C45 @rc @regression @id1404
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending  user (Search)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I wait until <Contact> exists in backend search results
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I click Connect button on connect to dialog
    And I see user <Contact> found on People picker page
    And I tap on conversation <Contact> in search result
    And I see <Contact> user pending profile page
    And I see cancel request button on pending profile page

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C34 @rc @regression @id1399
  Scenario Outline: Verify you don't receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation Myself
    Given User <Contact> securely pings conversation <Name>
    Given User <Contact> sends 1 encrypted message to user Myself
    Then I dont see conversation <Contact> in contact list
    When I open search by taping on it
    And I wait until <Contact> exists in backend search results
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I unblock user
    Then I see 1 message in the dialog
    And I navigate back to conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact>
    Then I see 1 default messages in the dialog

    Examples: 
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C39 @regression @id596
  Scenario Outline: Verify you cannot send the invitation message twice
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I tap on Search input on People picker page
    And I wait until <ContactEmail> exists in backend search results
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I see connect to <Contact> dialog
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    When I open search by taping on it
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    Then I see cancel request button on pending profile page

    Examples: 
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @C1029 @rc @regression @id2536
  Scenario Outline: Verify you can send an invitation via mail
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I press the send an invite button
    And I press invite others button
    And I press the copy button
    And I click close Invite list button
    And I click clear button
    And I tap on contact name <Contact>
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I press Enter key in Simulator window
    Then I check copied content from <Name>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C41 @regression @id2294
  Scenario Outline: Verify sending connection request by clicking instant + button (with search)
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I tap on Search input on People picker page
    Given I wait until <ContactEmail> exists in backend search results
    And I input in People picker search field user email <ContactEmail>
    And I press the instant connect button
    And I click close button to dismiss people view
    And I see first item in contact list named <UnconnectedUser>
    And I tap on contact name <UnconnectedUser>
    Then I see Pending Connect to <UnconnectedUser> message on Dialog page

    Examples: 
      | Name      | UnconnectedUser | ContactEmail |
      | user1Name | user2Name       | user2Email   |

  @C38 @rc @regression @id3227
  Scenario Outline: Verify possibility of disconnecting from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given Me sent connection request to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I see plus button next to text input
    And I click plus button next to text input
    And I open conversation details
    And I click Cancel request button
    Then I see Cancel request confirmation page
    And I confirm Cancel request by click on Yes button
    Then I dont see conversation <Contact> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C35 @regression @id3224
  Scenario Outline: Verify sending connection request after disconnecting
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given Me sent connection request to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I click plus button next to text input
    And I open conversation details
    And I click Cancel request button
    And I confirm Cancel request by click on Yes button
    And I navigate back to conversations list
    And I open search by taping on it
    And I input in People picker search field user name <Contact1>
    And I tap on conversation <Contact1> in search result
    And I see connect to <Contact1> dialog
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact1>

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C36 @regression @id3225
  Scenario Outline: Verify possibility of disconnecting from Search UI
    Given There are 2 users where <Name> is me
    Given I sent connection request to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search by taping on it
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact1>
    And I tap on conversation <Contact1> in search result
    And I click Cancel request button
    And I confirm Cancel request by click on Yes button
    Then I see user <Contact1> found on People picker page

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C24 @regression @id586 @ZIOS-5335
  Scenario Outline: Verify ignoring a connection request from another person (People view)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact3> sent connection request to me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with Myself,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I see Pending request link in contact list
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I select participant <Contact3>
    And I see <Contact3> user pending profile page
    And I click on start conversation button on pending profile page
    And I click on Ignore button on Pending requests page
    And I close group info page
    And I navigate back to conversations list
    Then I dont see Pending request link in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | IGNORECONNECT |

  @C46 @regression @id3794
  Scenario Outline: Verify common connections are not tappable
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact2> sent connection request to me
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I see YOU BOTH KNOW people section
    And I click person in YOU BOTH KNOW section
    Then I see Pending request page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C31 @regression @id1199
  Scenario Outline: Verify you can send text messages and images in 1to1 chat to the person who blocked you
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> blocks user <Name>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the dialog
    When I click plus button next to text input
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    Then I see 1 photo in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C30 @regression @id1133
  Scenario Outline: Verify unblocking from users profile page
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Then I dont see conversation <Contact> in contact list
    When I open search by taping on it
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I unblock user
    Then I see dialog page
    When I navigate back to conversations list
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C107 @regression @id3902
  Scenario Outline: Verify inbox is highlighted and opened in the list
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact3>
    Given <Contact> sent connection request to Me
    Given <Contact2> sent connection request to Me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    Then I see Pending request page

    Examples: 
      | Name      | Contact   | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C47 @regression @id3996
  Scenario Outline: Verify displaying first and last names for the incoming connection request
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to Me
    Given User <Contact> changes name to <NewName>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    Then I see Hello connect message from user <NewName> on Pending request page

    Examples: 
      | Name      | Contact   | NewName  |
      | user1Name | user2Name | New Name |

  @C37 @regression @id3226
  Scenario Outline: Verify connection request is deleted from the inbox of the addresser
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I sign in using my email or phone number
    Given I see conversations list
    When I see Pending request link in contact list
    And <Contact> cancel all outgoing connection requests
    Then I dont see Pending request link in contact list

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |
