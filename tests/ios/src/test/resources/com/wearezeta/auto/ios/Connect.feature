Feature: Connect

  @smoke @id576
  Scenario Outline: Send invitation message to a user
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    Given I wait up to 15 seconds until <ContactEmail> exists in backend search results
    And I input in People picker search field user email <ContactEmail>
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
      | Login      | Password      | Name      | Contact   | ContactEmail | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user2Email   | user3Name |

  @smoke @id585
  Scenario Outline: Get invitation message from user
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given <Contact> has sent connection request to Me
    Given I Sign in using login <Login> and password <Password>
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
      | Login      | Password      | Name      | Contact   | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @regression @id576
  Scenario Outline: Send connection request to unconnected participant in a group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <GroupCreator>
    Given <GroupCreator> is connected to <UnconnectedUser>
    Given <GroupCreator> has group chat <GroupChatName> with <UnconnectedUser>,Myself
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on group chat with name <GroupChatName>
    And I swipe up on group chat page
    And I tap on not connected contact <UnconnectedUser>
    And I click Connect button on connect to dialog
    And I exit the group info page
    And I return to the chat list
    Then I see first item in contact list named <UnconnectedUser>

    Examples: 
      | Login      | Password      | Name      | GroupCreator | GroupChatName | UnconnectedUser |
      | user1Email | user1Password | user1Name | user2Name    | TESTCHAT      | user3Name       |

  #Muted due to relogin issue
  #@staging @id611
  #Scenario Outline: Verify 1:1 conversation is not created on the second end after you ignore connection request(UI)
  #Given I Sign in using login <Login> and password <Password>
  #And I see Contact list with my name <Name>
  #When I swipe down contact list
  #And I see People picker page
  #And I input in People picker search field user name <Contact>
  #And I see user <Contact> found on People picker page
  #And I tap on NOT connected user name on People picker page <Contact>
  #And I see connect to <Contact> dialog
  #And I input message in connect to dialog
  #Old method must change this line #And I click Send button on connect to dialog
  #And I see People picker page
  #And I click clear button
  #And I see Contact list with my name <Name>
  #And I tap on my name <Name>
  #And I see Personal page
  #And I click on Settings button on personal page
  #And I click Sign out button from personal page
  #And I Sign in using login <Contact> and password <Password>
  #And I see Personal page
  #And I swipe right on the personal page
  #And I see Pending request link in contact list
  #And I click on Pending request link in contact list
  #And I click on Ignore button on Pending requests page
  #And I dont see Pending request link in contact list
  #And I don't see conversation with not connected user <Name>
  #And I tap on my name <Contact>
  #And I click on Settings button on personal page
  #And I click Sign out button from personal page
  #And I Sign in using login <Name> and password <Password>
  #And I see Personal page
  #And I swipe right on the personal page
  #And I see conversation with not connected user <Contact>
  #Examples:
  #  | Login   | Password    | Name    | Contact  |
  #  | aqaUser | aqaPassword | aqaUser | yourUser |
  #Muted due to relogin issue
  #@staging @id611
  #Scenario Outline: Verify 1:1 conversation is not created on the second end after you ignore connection request(BE)
  #Given I send invitation to <Name> by <Contact>
  #And I Sign in using login <Name> and password <Password>
  #And I see Pending request link in contact list
  #And I click on Pending request link in contact list
  #And I click on Ignore button on Pending requests page
  #And I dont see Pending request link in contact list
  #And I don't see conversation with not connected user <Contact>
  #And I tap on my name <Name>
  #And I click on Settings button on personal page
  #And I click Sign out button from personal page
  #And I Sign in using login <Contact> and password <Password>
  #And I see Personal page
  #And I swipe right on the personal page
  #And I see conversation with not connected user <Name>
  #Examples:
  #| Login   | Password    | Name    | Contact         |
  #| aqaUser | aqaPassword | aqaUser | yourNotContact1 |
  #Muted due relogin issue and blank Personal page screen issue
  #@staging @id610
  #Scenario Outline: Verify 1:1 conversation is successfully created on the second end after you accept connection request(BE)
  #Given I send invitation to <Name> by <Contact>
  #And I Sign in using login <Name> and password <Password>
  #And I see Pending request link in contact list
  #And I click on Pending request link in contact list
  #And I click Connect button on Pending request page
  #And I dont see Pending request link in contact list
  #And I see conversation with not connected user <Contact>
  #And I tap on my name <Name>
  #And I click on Settings button on personal page
  #And I click Sign out button from personal page
  #And I Sign in using login <Contact> and password <Password>
  #And I see Personal page
  #And I swipe right on the personal page
  #And I see conversation with not connected user <Name>
  #Examples:
  #| Login   | Password    | Name    | Contact         |
  #| aqaUser | aqaPassword | aqaUser | yourNotContact1 |
  @regression @id579
  Scenario Outline: Verify transitions between connection requests (ignoring)
    Given There are 5 users where <Name> is me
    Given <Contact1> has sent connection request to me
    Given <Contact2> has sent connection request to me
    Given <Contact3> has sent connection request to me
    Given Myself is connected to <Contact4>
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I see Pending request link in contact list
    And I click on Pending request link in contact list
    And I see Pending request page
    And I click on Ignore button on Pending requests page <SentRequests> times
    And I dont see Pending request link in contact list
    And I don't see conversation with not connected user <Contact1>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for ignored user name <Contact1> and tap on it
    Then I see Pending request page

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @regression @id577
  Scenario Outline: Verify transitions between connection requests (connecting)
    Given There are 5 users where <Name> is me
    Given <Contact1> has sent connection request to me
    Given <Contact2> has sent connection request to me
    Given <Contact3> has sent connection request to me
    Given Myself is connected to <Contact4>
    Given I Sign in using login <Login> and password <Password>
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
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | SentRequests |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user5Name | 3            |

  @regression @id1404
  Scenario Outline: Verify impossibility of starting 1:1 conversation with pending  user (Search)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
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
    And I click on start conversation button on pending profile page
    And I see <Contact> user pending profile page

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @regression @id1399
  Scenario Outline: Verify you don't receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When Contact <Contact> sends image <Picture> to single user conversation <Name>
    And Contact <Contact> ping conversation <Name>
    And Contact <Contact> sends random message to user <Name>
    And I wait for 10 seconds
    Then I dont see conversation <Contact> in contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I unblock user
    Then I see User <Contact> Pinged message in the conversation
    And I see new photo in the dialog
    And I see message in the dialog
    And I navigate back to conversations view
    And Contact <Contact> sends random message to user <Name>
    When I tap on contact name <Contact>
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Picture     |
      | user1Email | user1Password | user1Name | user2Name | testing.jpg |
      
  @regression @id596
  Scenario Outline: Verify you cannot send the invitation message twice
  	Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I wait up to 15 seconds until <ContactEmail> exists in backend search results
    And I input in People picker search field user email <ContactEmail>
    And I see user <Contact> found on People picker page
    And I tap on NOT connected user name on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I click Connect button on connect to dialog
    And I see People picker page
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    And I see user <Contact> found on People picker page
    And I tap on user on pending name on People picker page <Contact>
    Then I see <Contact> user pending profile page
    
  	Examples: 
      | Login      | Password      | Name      | Contact   | ContactEmail |
      | user1Email | user1Password | user1Name | user2Name | user2Email   |
