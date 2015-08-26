Feature: Search

  @smoke @id2147
  Scenario Outline: Verify search by email
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see user <ContactName> found on People picker page

    Examples: 
      | Name      | ContactEmail | ContactName |
      | user1Name | user2Email   | user2Name   |

  @smoke @id2148
  Scenario Outline: Verify search by name
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id299 @noAcceptAlert
  Scenario Outline: Verify denying address book uploading
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    And I dismiss alert
    And I dismiss settings warning
    And I open search by taping on it
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I dismiss alert
    And I dont see CONNECT label
    And I press maybe later button
    And I click clear button
    And I open search by taping on it
    And I scroll up page a bit
    And I dont see Upload contacts dialog

    Examples: 
      | Name      |
      | user1Name |

  #regression
  @staging @id311 @deployAddressBook @noAcceptAlert
  Scenario Outline: Verify uploading address book to the server
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    And I dismiss alert
    And I dismiss settings warning
    And I open search by taping on it
    And I see Upload contacts dialog
    And I click Continue button on Upload dialog
    And I accept alert
    Then I see CONNECT label
    And I see user <Contact1> found on People picker page
    And I see user <Contact2> found on People picker page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |


  @regression @id1394
  Scenario Outline: Start 1:1 chat with users from Top Connections
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 1 top connections
    And I click open conversation button on People picker page
    And I wait for 2 seconds
    And I see dialog page

    Examples: 
      | Name      | UserCount | Contact   |
      | user1Name | 4         | user2Name |

  @id1150 @regression
  Scenario Outline: Start group chat with users from Top Connections
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given Contact <Contact> send message to user <Name>
    Given Contact <Name> send message to user <Contact>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 2 top connections
    And I click Create Conversation button on People picker page
    And I wait for 2 seconds
    And I swipe up on group chat page
    And I change group conversation name to <ConvoName>
    And I exit the group info page
    And I return to the chat list
    And I see first item in contact list named <ConvoName>

    Examples: 
      | Name      | ConvoName    | UserCount | Contact   |
      | user1Name | TopGroupTest | 4         | user2Name |

  @id1454 @regression
  Scenario Outline: Verify sending a connection request to user chosen from search
    Given There are 2 users where <Name> is me
    Given User <UnconnectedUser> name starts with <StartLetter>
    Given User <Name> change accent color to <Color>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I search for user name <UnconnectedUser> and tap on it on People picker page
    Then I see connect to <UnconnectedUser> dialog
    And I delete all connect message content
    And I see that connect button is disabled
    And I type message in connect dialog with <NumOfMessageChars> characters
    And I see message with max number of characters
    And I click Connect button on connect to dialog
    And I click close button to dismiss people view
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user email <ContactEmail>
    Then I see the user <UnconnectedUser> avatar with a clock
    And I click close button to dismiss people view
    And I see conversation with not connected user <UnconnectedUser>
    And I tap on contact name <UnconnectedUser>
    And I swipe up on pending dialog page to open other user pending personal page
    And I see <UnconnectedUser> user pending profile page

    Examples: 
      | Name      | UnconnectedUser | ContactEmail | NumOfMessageChars | StartLetter | Color        |
      | user1Name | user2Name       | user2Email   | 140               | T           | BrightOrange |

  @regression @id763
  Scenario Outline: I can still search for other people using the search field, regardless of whether I already added people from Top conversations
    Given There are <UserCount> users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I wait for 30 seconds
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 3 top connections but not <Contact>
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    Then I see that <Number> contacts are selected

    Examples: 
      | Name      | UserCount | Contact   | Number |
      | user1Name | 7         | user2Name | 4      |

  @regression @id1456
  Scenario Outline: Verify you can unblock someone from search list
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I dont see conversation <Contact> in contact list
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I unblock user
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id2117
  Scenario Outline: Verify dismissing with clicking on Hide
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I swipe to reveal hide button for suggested contact <Friend1>
    And I tap hide for suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Name | user2Name          | user3Name | user4Name | user5Name |

  @regression @id2116
  Scenario Outline: Verify dismissing with one single gesture
    Given There are 5 users where <Name> is me
    Given <ContactWithFriends> is connected to <Name>
    Given <ContactWithFriends> is connected to <Friend1>
    Given <ContactWithFriends> is connected to <Friend2>
    Given <ContactWithFriends> is connected to <Friend3>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    #And I hide peoplepicker keyboard
    And I swipe completely to dismiss suggested contact <Friend1>
    Then I do not see suggested contact <Friend1>

    Examples: 
      | Name      | ContactWithFriends | Friend1   | Friend2   | Friend3   |
      | user1Name | user2Name          | user3Name | user4Name | user5Name |

  @regression @id2149
  Scenario Outline: Verify search by second name (something after space)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I wait until <LastName> exists in backend search results
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <LastName>
    Then I see user <NewName> found on People picker page

    Examples: 
      | Name      | Contact   | NewName  | LastName |
      | user1Name | user2Name | NEW NAME | NAME     |

  @regression @id2118
  Scenario Outline: Verify sending connection request from PYMK
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to <Contact2>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if CONNECT label is not there
    And I see CONNECT label
    And I press the instant connect button
    And I click close button to dismiss people view
    Then I see first item in contact list named <Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @staging @id3282
  Scenario Outline: Verify starting a call with action button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    When I see Contact list with my name <Name>
    And I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page
    When I tap on connected user <Contact> on People picker page
    And I see call action button on People picker page
    And I click call action button on People picker page
    Then I see mute call, end call and speakers buttons
    And I see calling to contact <Contact> message

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |
