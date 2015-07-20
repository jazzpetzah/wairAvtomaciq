Feature: Connect

  @regression @id2355
  Scenario Outline: Verify sending a connection request to user chosen from search [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given User <UnconnectedUser> name starts with <StartLetter>
    Given User <Name> change accent color to <Color>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <UnconnectedUser> and tap on it on People picker page
    And I see connect to <UnconnectedUser> dialog
    And I delete all connect message content
    And I see that connect button is disabled
    And I input message in connect dialog with <NumOfMessageChars> characters
    And I fill in 3 characters in connect dialog
    And I see message with max number of characters
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    And I swipe down contact list on iPad
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see the user <UnconnectedUser> avatar with a clock
    And I click close button to dismiss people view
    And I see conversation with not connected user <UnconnectedUser>
    And I tap on contact name <UnconnectedUser>
    And I open pending user conversation details
    And I see <UnconnectedUser> user pending profile popover on iPad

    Examples: 
      | Name      | UnconnectedUser | ContactEmail | NumOfMessageChars | StartLetter | Color        |
      | user1Name | user2Name       | user2Email   | 140               | T           | BrightOrange |

  @regression @id3008
  Scenario Outline: Verify sending a connection request to user chosen from search [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given User <UnconnectedUser> name starts with <StartLetter>
    Given User <Name> change accent color to <Color>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <UnconnectedUser> and tap on it on People picker page
    And I see connect to <UnconnectedUser> dialog
    And I delete all connect message content
    And I see that connect button is disabled
    And I input message in connect dialog with <NumOfMessageChars> characters
    And I fill in 3 characters in connect dialog
    And I see message with max number of characters
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    And I swipe down contact list on iPad
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see the user <UnconnectedUser> avatar with a clock
    And I click close button to dismiss people view
    And I see conversation with not connected user <UnconnectedUser>
    And I tap on contact name <UnconnectedUser>
    And I open pending user conversation details
    And I see <UnconnectedUser> user pending profile popover on iPad

    Examples: 
      | Name      | UnconnectedUser | ContactEmail | NumOfMessageChars | StartLetter | Color        |
      | user1Name | user2Name       | user2Email   | 140               | T           | BrightOrange |

  @staging @id2119
  Scenario Outline: Verify sending connection request after opening profile by clicking on the name and avatar [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I Sign in on tablet using my email
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
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page from user <Name>

    Examples: 
      | Name      | Contact   | ContactEmail | Contact2  |
      | user1Name | user2Name | user2Email   | user3Name |

  @staging @id3009
  Scenario Outline: Verify sending connection request after opening profile by clicking on the name and avatar [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
    And I tap on contact name <Contact>
    And I see Pending Connect to <Contact> message on Dialog page from user <Name>

    Examples: 
      | Name      | Contact   | ContactEmail | Contact2  |
      | user1Name | user2Name | user2Email   | user3Name |

  @staging @id2610
  Scenario Outline: Send connection request to unconnected participant in a group chat [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <GroupCreator>
    Given <GroupCreator> is connected to <UnconnectedUser>
    Given <GroupCreator> has group chat <GroupChatName> with <UnconnectedUser>,Myself
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap on not connected contact <UnconnectedUser>
    And I click Connect button on connect to dialog
    And I exit the group info iPad popover
    And I swipe right on group chat page
    Then I see first item in contact list named <UnconnectedUser>

    Examples: 
      | Name      | GroupCreator | GroupChatName | UnconnectedUser |
      | user1Name | user2Name    | TESTCHAT      | user3Name       |

  @staging @id3011
  Scenario Outline: Send connection request to unconnected participant in a group chat [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <GroupCreator>
    Given <GroupCreator> is connected to <UnconnectedUser>
    Given <GroupCreator> has group chat <GroupChatName> with <UnconnectedUser>,Myself
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I open group conversation details
    And I tap on not connected contact <UnconnectedUser>
    And I click Connect button on connect to dialog
    And I exit the group info iPad popover
    Then I see first item in contact list named <UnconnectedUser>

    Examples: 
      | Name      | GroupCreator | GroupChatName | UnconnectedUser |
      | user1Name | user2Name    | TESTCHAT      | user3Name       |

  @staging @id2330
  Scenario Outline: Verify transitions between connection requests (ignoring) [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I click on Ignore button on Pending requests page <SentRequests> times
    And I swipe right on Dialog page
    And I dont see Pending request link in contact list
    And I don't see conversation with not connected user <Contact1>
    And I wait until <Contact1> exists in backend search results
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for ignored user name <Contact1> and tap on it
    Then I see incoming pending popover from user <Contact1> on iPad

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @staging @id3012
  Scenario Outline: Verify transitions between connection requests (ignoring) [LANDSCAPE]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I click on Connect button on Pending requests page <SentRequests> times
    And I dont see Pending request link in contact list
    And I don't see conversation with not connected user <Contact1>
    And I wait until <Contact1> exists in backend search results
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for ignored user name <Contact1> and tap on it
    Then I see incoming pending popover from user <Contact1> on iPad

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @staging @id2329
  Scenario Outline: Verify transitions between connection requests (accepting) [PORTRAIT]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I Sign in on tablet using my email
    When I see Contact list with my name <Name>
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I click on Connect button on Pending requests page <SentRequests> times
    And I swipe right on Dialog page
    Then I dont see Pending request link in contact list
    And I see user <Contact1> in contact list
    And I see user <Contact2> in contact list
    And I see user <Contact3> in contact list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @staging @id3013
  Scenario Outline: Verify transitions between connection requests (accepting) [LANDSCAPE]
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to me
    Given <Contact2> sent connection request to me
    Given <Contact3> sent connection request to me
    Given Myself is connected to <Contact4>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @staging @id2359
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending  user (Search) [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given Me sent connection request to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user on pending name on People picker page <Contact>
    And I see <Contact> user pending profile popover on iPad
    And I click on start conversation button on pending profile page
    Then I see text input in dialog is not allowed

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @staging @id3014
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending  user (Search) [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given Me sent connection request to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I click hide keyboard button
    And I tap on user on pending name on People picker page <Contact>
    And I see <Contact> user pending profile popover on iPad
    And I click on start conversation button on pending profile page
    Then I see text input in dialog is not allowed

    Examples: 
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @staging @id2341
  Scenario Outline: Verify you don't receive any messages from blocked person in 1to1 chat [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I Sign in on tablet using my email
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
    And I see new photo in the dialog
    And I see message in the dialog
    And I swipe right on Dialog page
    And I see People picker page
    And I click close button to dismiss people view
    And Contact <Contact> sends random message to user <Name>
    When I tap on contact name <Contact>
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @staging @id3015
  Scenario Outline: Verify you don't receive any messages from blocked person in 1to1 chat [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
    And I see new photo in the dialog
    And I see message in the dialog
    And I click close button to dismiss people view
    And Contact <Contact> sends random message to user <Name>
    When I tap on contact name <Contact>
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @staging @id2436
  Scenario Outline: Verify you cannot send the invitation message twice [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in on tablet using my email
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
    And I see <Contact> user pending profile popover on iPad

    Examples: 
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @staging @id3016
  Scenario Outline: Verify you cannot send the invitation message twice [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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
    And I see <Contact> user pending profile popover on iPad

    Examples: 
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @staging @id1492
  Scenario Outline: Verify you can send an invitation via mail [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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

  @staging @id3017
  Scenario Outline: Verify you can send an invitation via mail [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @staging @id2768 @deployAddressBook @noAcceptAlert
  Scenario Outline: Verify you can see People you may know on Wire after uploading your address book
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I dismiss alert
    And I open search by taping on it
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I accept alert
    Then I see CONNECT label
    And I see user <Contact1> found on People picker page
    And I see user <Contact2> found on People picker page

    Examples: 
      | Name      | Contact1 | Contact2 |
      | user1Name | vb003    | Dorothy  |
