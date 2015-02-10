Feature: Search

  @regression @id299 @noAcceptAlert
  Scenario Outline: Verify denying address book uploading
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    And I dismiss alert
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I dismiss alert
    And I dont see PEOPLE YOU MAY KNOW label
    And I click clear button
    And I swipe down contact list
    And I scroll up page a bit
    And I dont see Upload contacts dialog

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id311 @noAcceptAlert
  Scenario Outline: Verify uploading address book to the server
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    When I add contacts list users to Mac contacts
    And I dismiss alert
    And I swipe down contact list
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I accept alert
    Then I see PEOPLE YOU MAY KNOW label
    And I see user <Contact1> found on People picker page
    And I see user <Contact2> found on People picker page
    And I remove contacts list users from Mac contacts

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  #Muted due to sync engine troubles(group chat is not created and app is closed after logout)
  #@mute @smoke @id600
  #Scenario Outline: Verify the new conversation is created on the other end (Search UI source)
  #Given I Sign in using login <Login> and password <Password>
  #And I see Contact list with my name <Name>
  #When I swipe down contact list
  #And I see People picker page
  #And I click clear button
  #And I swipe down contact list
  #And I see top people list on People picker page
  #And I tap on connected user <Contact1> on People picker page
  #And I tap on connected user <Contact2> on People picker page
  #And I see Create Conversation button on People picker page
  #And I click Create Conversation button  on People picker page
  #And I see group chat page with users <Contact1>,<Contact2>
  #And I swipe right on group chat page
  #And I tap on my name <Name>
  #And I see Personal page
  #And I click on Settings button on personal page
  #And I click Sign out button from personal page
  #And I Sign in using login <Contact1> and password <Password>
  #And I see Personal page
  #And I swipe right on the personal page
  #And I see <Contact1> and <Contact2> chat in contact list
  #And I tap on my name <Name>
  #And I see Personal page
  #And I click on Settings button on personal page
  #And I click Sign out button from personal page
  #And I Sign in using login <Contact2> and password <Password>
  #And I see Personal page
  #And I swipe right on the personal page
  #And I see <Contact1> and <Contact2> chat in contact list
  #Examples:
  #| Login   | Password    | Name    | Contact1    | Contact2    |
  #| aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |
  
  
  @regression @id1394
  Scenario Outline: Start 1:1 chat with users from Top Connections
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I swipe down contact list
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on 1 top connections
    And I click Go button to create 1:1 conversation
    And I wait for 2 seconds
    And I see dialog page

    Examples: 
      | Login      | Password      | Name      | UserCount | Contact   |
      | user1Email | user1Password | user1Name | 10        | user2Name |

  @id1150 @regression
  Scenario Outline: Start group chat with users from Top Connections
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I swipe down contact list
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on 2 top connections
    And I click on Go button
    And I wait for 2 seconds
    And I swipe up on group chat page
    And I change group conversation name to <ConvoName>
    And I exit the group info page
    And I return to the chat list
    And I see first item in contact list named <ConvoName>

    Examples: 
      | Login      | Password      | Name      | ConvoName    | UserCount | Contact   |
      | user1Email | user1Password | user1Name | TopGroupTest | 10        | user2Name |

  @id1454 @staging
  Scenario Outline: Verify sending a connection request to user chosen from search
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <UnconnectedUser> and tap on it on People picker page
    Then I see connect to <UnconnectedUser> dialog
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    And I see conversation with not connected user <UnconnectedUser>
    
    Examples: 
      | Login      | Password      | Name      | UnconnectedUserEmail | UnconnectedUser |
      | user1Email | user1Password | user1Name | user2Email           | user2Name       |
      
  @staging @id763
  Scenario Outline: I can still search for other people using the search field, regardless of whether I already added people from Top conversations
  	Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I swipe down contact list
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on 3 top connections
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    Then I see that <Number> contacts are selected
     
  	Examples: 
      | Login      | Password      | Name      | UserCount | Contact   | Number |
      | user1Email | user1Password | user1Name | 10        | user2Name |  4     |
