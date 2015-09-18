Feature: Connect

  @smoke @id3272
  Scenario Outline: Send invitation message to a user
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    Given I wait until <ContactEmail> exists in backend search results
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on NOT connected user name on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to dialog
    And I see People picker page
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page from user <Name>

    Examples: 
      | Name      | Contact   | ContactEmail | Contact2  |
      | user1Name | user2Name | user2Email   | user3Name |

  @smoke @rc @id1475
  Scenario Outline: Get invitation message from user
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> sent connection request to Me
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I wait for 2 seconds
    And I see Hello connect message from user <Contact> on Pending request page
    And I click Connect button on Pending request page
    And I wait for 2 seconds
    Then I see first item in contact list named <Contact>

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @rc @id576
  Scenario Outline: Send connection request to unconnected participant in a group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <GroupCreator>
    Given <GroupCreator> is connected to <UnconnectedUser>
    Given <GroupCreator> has group chat <GroupChatName> with <UnconnectedUser>,Myself
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap on not connected contact <UnconnectedUser>
    And I click Connect button on connect to dialog
    And I exit the group info page
    And I return to the chat list
    Then I see first item in contact list named <UnconnectedUser>

    Examples: 
      | Name      | GroupCreator | GroupChatName | UnconnectedUser |
      | user1Name | user2Name    | TESTCHAT      | user3Name       |

  @regression @id579
  Scenario Outline: Verify transitions between connection requests (ignoring)
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I click on Ignore button on Pending requests page <SentRequests> times
    And I dont see Pending request link in contact list
    And I don't see conversation with not connected user <Contact1>
    And I wait until <Contact1> exists in backend search results
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for ignored user name <Contact1> and tap on it
    Then I see Pending request page

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @regression @id577
  Scenario Outline: Verify transitions between connection requests (connecting)
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I click on Connect button on Pending requests page <SentRequests> times
    Then I dont see Pending request link in contact list
    And I see user <Contact1> in contact list
    And I see user <Contact2> in contact list
    And I see user <Contact3> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @regression @rc @id1404
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending  user (Search)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on NOT connected user name on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to dialog
    And I see People picker page
    And I see user <Contact> found on People picker page
    And I tap on user on pending name on People picker page <Contact>
    And I see <Contact> user pending profile page
    And I see cancel request button on pending profile page

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @regression @id1399
  Scenario Outline: Verify you don't receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When Contact <Contact> sends image <Picture> to single user conversation <Name>
    And Contact <Contact> ping conversation <Name>
    And Contact <Contact> sends random message to user <Name>
    And I wait for 10 seconds
    Then I dont see conversation <Contact> in contact list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I unblock user
    And I wait for 5 seconds
    Then I see User <Contact> Pinged message in the conversation
    And I see new photo in the dialog
    And I see message in the dialog
    And I navigate back to conversations view
    And I see People picker page
    And I click close button to dismiss people view
    And Contact <Contact> sends random message to user <Name>
    When I tap on contact name <Contact>
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @regression @id596
  Scenario Outline: Verify you cannot send the invitation message twice
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I wait until <ContactEmail> exists in backend search results
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on NOT connected user name on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to dialog
    And I see People picker page
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user on pending name on People picker page <Contact>
    Then I see cancel request button on pending profile page

    Examples: 
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @regression @rc @id2536
  Scenario Outline: Verify you can send an invitation via mail
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I press the send an invite button
    And I press the copy button
    And I click clear button
    And I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I tap and hold on message input
    And I click on popup Paste item
    And I send the message
    Then I check copied content from <Name>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2294
  Scenario Outline: Verify sending connection request by clicking instant + button (with search)
    Given There are 2 users where <Name> is me
    Given User <UnconnectedUser> name starts with <StartLetter>
    Given User <Name> change accent color to <Color>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    Given I wait until <ContactEmail> exists in backend search results
    And I input in People picker search field user email <ContactEmail>
    And I see user <UnconnectedUser> found on People picker page
    And I press the instant connect button
    And I click close button to dismiss people view
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see the user <UnconnectedUser> avatar with a clock
    And I click close button to dismiss people view
    And I see first item in contact list named <UnconnectedUser>
    And I tap on contact name <UnconnectedUser>
    Then I see Pending Connect to <UnconnectedUser> message on Dialog page from user <Name>

    Examples: 
      | Name      | UnconnectedUser | ContactEmail | StartLetter | Color        |
      | user1Name | user2Name       | user2Email   | T           | BrightOrange |

  #regression
  @staging @id2768 @deployAddressBook @noAcceptAlert
  Scenario Outline: Verify you can see People you may know on Wire after uploading your address book
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I dismiss all alerts
    And I dismiss settings warning
    And I open search by taping on it
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I accept alert
    Then I see CONNECT label
    And I see user <Contact1> found on People picker page

    #And I see user <Contact2> found on People picker page
    Examples: 
      | Name      | Contact1 | Contact2 |
      | user1Name | vb003    | Dorothy  |

  @regression @rc @id3223
  Scenario Outline: Verify possibility of disconnecting from conversation list
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see plus button next to text input
    And I click plus button next to text input
    And I open conversation details
    And I click Cancel request button
    Then I see Cancel request confirmation page
    And I confirm Cancel request by click on Yes button
    And I see Details button is visible
    And I return to the chat list
    Then I dont see conversation <Contact> in contact list

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @regression @id3224
  Scenario Outline: Verify sending connection request after disconnecting
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I click plus button next to text input
    And I open conversation details
    And I click Cancel request button
    And I confirm Cancel request by click on Yes button
    And I see Details button is visible
    And I return to the chat list
    And I open search by taping on it
    And I input in People picker search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on NOT connected user name on People picker page <Contact1>
    And I see connect to <Contact1> dialog
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact1>

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @regression @id3225
  Scenario Outline: Verify possibility of disconnecting from Search UI
    Given There are 2 users where <Name> is me
    Given Me sent connection request to <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact1>
    And I see user <Contact1> found on People picker page
    And I see the user <Contact1> avatar with a clock
    And I tap on NOT connected user name on People picker page <Contact1>
    And I click Cancel request button
    And I see Cancel request confirmation page
    And I confirm Cancel request by click on Yes button
    And I see user <Contact1> found on People picker page
    Then I see the user <Contact1> avatar without the pending clock

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @staging @id586
  Scenario Outline: Verify ignoring a connection request from another person (People view)
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact3> sent connection request to me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>,<Contact3>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    And I see Pending request link in contact list
    When I tap on group chat with name <GroupChatName>
    And I see dialog page
    And I open group conversation details
    And I select contact <Contact3>
    And I see <Contact3> user pending profile page
    And I click on start conversation button on pending profile page
    And I see accept ignore request alert
    And I click on Ignore button on Pending requests page
    And I close user profile page to return to dialog page
    And I exit the group info page
    And I return to the chat list
    Then I dont see Pending request link in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | IGNORECONNECT |

  @staging @id1199
  Scenario Outline: Verify you can send text messages and images in 1to1 chat to the person who blocked you
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> blocks user <Name>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    Then I see dialog page
    And I type the message
    When I send the message
    Then I see message in the dialog
    When I swipe the text input cursor
    And I press Add Picture button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    Then I see new photo in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id1133
  Scenario Outline: Verify unblocking from users profile page
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    Then I dont see conversation <Contact> in contact list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I unblock user
    Then I see dialog page
    When I navigate back to conversations view
    And I click close button to dismiss people view
    Then I see user <Contact> in contact list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |