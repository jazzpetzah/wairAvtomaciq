Feature: Connect

  @id191 @id193 @smoke
  Scenario Outline: Send connection request from search
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    And I see People picker page
    And I navigate back to Conversations List
    Then I see contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | Message       |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Hellow friend |

  @id323 @smoke
  Scenario Outline: Accept incoming connection request from conversation list
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given <Contact> sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | WaitingMess      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | 1 person waiting |

  @id1411 @regression
  Scenario Outline: I can see a new inbox for connection when receive new connection request
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    Given I do not see contact list with name <WaitingMess>
    Given <Contact> sent connection request to <Name>
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I press Ignore connect button
    Then I see Contact list
    Then I do not see contact list with name <WaitingMess>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | WaitingMess      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | 1 person waiting |

  @id539 @id543 @regression
  Scenario Outline: I can see a inbox count increasing/decreasing correctly + I ignore someone from people picker and clear my inbox
    Given There are 5 users where <Name> is me
    Given <Contact1> sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    Given <Contact2> sent connection request to <Name>
    When I wait for 2 seconds
    Then I see contact list with name <WaitingMess2>
    When I tap on contact name <WaitingMess2>
    And I press Ignore connect button
    And I navigate back from connect page
    Then I see contact list with name <WaitingMess1>
    And <Contact3> sent connection request to <Name>
    And <Contact4> sent connection request to <Name>
    And I see contact list with name <WaitingMess3>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact3>
    And I tap on user name found on People picker page <Contact3>
    And I press Ignore connect button
    And I navigate back from connect page
    And I see contact list with name <WaitingMess2>
    And I tap on contact name <WaitingMess2>
    And I press Ignore connect button
    And I navigate back from connect page
    And I see contact list with name <WaitingMess1>
    And I tap on contact name <WaitingMess1>
    And I press Ignore connect button
    And I do not see contact list with name <WaitingMess1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | WaitingMess1     | Contact2  | WaitingMess2     | Contact3  | Contact4  | WaitingMess3     |
      | user1Email | user1Password | user1Name | user2Name | 1 person waiting | user3Name | 2 people waiting | user4Name | user5Name | 3 people waiting |

  @id544 @regression
  Scenario Outline: I accept someone from people picker and -1 from inbox as well
    Given There are 3 users where <Name> is me
    Given <Contact1> sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    Given <Contact2> sent connection request to <Name>
    When I see contact list with name <WaitingMess1>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact2>
    And I tap on user name found on People picker page <Contact2>
    And I swipe up on connect page
    And I see connect to <Contact2> dialog
    And I Connect with contact by pressing button
    And I wait for 5 seconds
    Then I navigate back from dialog page
    And I see contact list with name <WaitingMess2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | WaitingMess2     | Contact2  | WaitingMess1     |
      | user1Email | user1Password | user1Name | user2Name | 1 person waiting | user3Name | 2 people waiting |

  @id540 @regression
  Scenario Outline: I can ignore a connect request and reconnect later
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I see contact list with name <WaitingMess>
    And I tap on contact name <WaitingMess>
    And I press Ignore connect button
    And I do not see contact list with name <WaitingMess>
    And I wait until <Contact> exists in backend search results
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see Connect to <Contact> Dialog page

    Examples: 
      | Login      | Password      | Name      | Contact   | WaitingMess      |
      | user1Email | user1Password | user1Name | user2Name | 1 person waiting |

  @id542 @regression
  Scenario Outline: Accept incoming connection request from search
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I see contact list with name <WaitingMess>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see contact list with name <Contact>

    #Then I see dialog page
    #And I see Connect to <Contact> Dialog page
    Examples: 
      | Login      | Password      | Name      | Contact   | WaitingMess      |
      | user1Email | user1Password | user1Name | user2Name | 1 person waiting |

  @id547 @regression
  Scenario Outline: I can see the char counter changes when writing the first connect message
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    Then I see counter value <CounterValue1>
    And I see connect button enabled state is <FirstState>
    And I type Connect request "<Message>"
    And I see counter value <CounterValue2>
    And I see connect button enabled state is <SecondState>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | CounterValue1 | Message | CounterValue2 | FirstState | SecondState |
      | user1Email | user1Password | user1Name | user2Name | user3Name | 140           | test    | 136           | false      | true        |

  @id548 @regression
  Scenario Outline: I can not send first message with space only
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "    "
    Then I see counter value <CounterValue>
    And I see connect button enabled state is <FirstState>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | CounterValue | FirstState |
      | user1Email | user1Password | user1Name | user2Name | user3Name | 136          | false      |

  @id554 @regression
  Scenario Outline: I would not know other person has ignored my connection request
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    When <Contact> ignore all requests
    And I press Clear button
    Then I tap on contact name <Contact>
    And I see that connection is pending

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | Test    |

  @id541 @regression
  Scenario Outline: I can receive new connection request when app in background
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I minimize the application
    And <Contact> sent connection request to Me
    And I restore the application
    And I see Contact list
    And I see contact list with name <WaitingMess>
    And I tap on contact name <WaitingMess>
    Then I see connect to <Contact> dialog
    And I see Accept and Ignore buttons
    And I press Ignore connect button

    Examples: 
      | Login      | Password      | Name      | Contact   | WaitingMess      |
      | user1Email | user1Password | user1Name | user2Name | 1 person waiting |

  @id553 @regression
  Scenario Outline: I want to see that the other person has accepted the connect request in the conversation view
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    When <Contact> accept all requests
    And I wait for 2 seconds
    And I press Clear button
    Then I see contact list with name <Contact>
    And I tap on contact name <Contact>
    And I see Connect to <Contact> Dialog page

    Examples: 
      | Login      | Password      | Name      | Contact   | Message |
      | user1Email | user1Password | user1Name | user2Name | Test    |

  @id552 @regression
  Scenario Outline: I want to discard the new connect request (sending) by returning to the search results after selecting someone I’m not connected to
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    Then I close Connect To dialog
    And I see People picker page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id550 @regression
  Scenario Outline: I want to initiate a connect request by selecting someone from within a group conversation
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given <Contact1> is connected to <Contact2>
    Given <Contact1> has group chat <ChatName> with Myself,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <ChatName>
    And I swipe up on dialog page
    And I tap on group chat contact <Contact2>
    And I see connect to <Contact2> dialog
    And I tap on edit connect request field
    And I type Connect request "Message"
    And I press Connect button
    And I return to group chat page
    And I navigate back from dialog page
    And I see contact list with name <Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName         |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ContactGroupChat |

  @id676 @regression
  Scenario Outline: I want to block a person from 1:1 conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe up on dialog page
    And I press options menu button
    And I Press Block button
    And I confirm block
    Then I do not see contact list with name <Contact>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    Then User info should be shown with Block button

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id680 @regressionko
  Scenario Outline: I want to see user has been blocked within the Start UI
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    And I press Clear button
    And I see contact list with name <Contact>
    When I tap on contact name <Contact>
    And I see that connection is pending
    And I Press Block button on connect to page
    And I confirm block on connect to page
    And I wait for 5 seconds
    Then I do not see contact list with name <Contact>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    Then User info should be shown with Block button
    And I click Unblock button

    Examples: 
      | Login      | Password      | Name      | Contact   | Message      |
      | user1Email | user1Password | user1Name | user2Name | Hello friend |

  @regression_off @id720 @mute
  Scenario Outline: I do not want to be seen in the search results of someone I blocked
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When User <Contact> blocks user <Name>
    And I wait for 120 seconds
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    Then I see that no results found

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id723 @regression
  Scenario Outline: I want to unblock someone from their Profile view
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given <Contact2> is connected to <Name>
    Given User <Name> blocks user <Contact1>
    And I wait for 120 seconds
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact1>
    And I see user <Contact1> found on People picker page
    And I tap on user name found on People picker page <Contact1>
    Then User info should be shown with Block button
    And I click Unblock button
    And I see dialog page
    And I navigate back from dialog page
    And I see contact list with name <Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @id1405 @regression
  Scenario Outline: Impossibility of starting 1:1 conversation with pending user (Search)
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I press Connect button
    #And I swipe down contact list
    And I see People picker page
    #And I tap on Search input on People picker page
    #And I input in search field user name to connect to <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    Then I see that connection is pending

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact1  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @id1397 @regression
  Scenario Outline: Verify you do not receive any messages from blocked person in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message "<Message>" and send it
    And User <Name> blocks user <Contact>
    And Contact <Contact> sends image <Picture> to single user conversation <Name>
    And Contact <Contact> send message to user <Name>
    And Contact <Contact> ping conversation <Name>
    Then Last message is <Message>

    Examples: 
      | Login      | Password      | Name      | Contact   | Message          | Picture     |
      | user1Email | user1Password | user1Name | user2Name | Hello my friend! | testing.jpg |

  @id2215 @staging
  Scenario Outline: I can connect to someone from PYMK by clicking +
    Given I see sign in screen
    Given I press Join button
    Given I press Camera button twice
    Given I confirm selection
    Given I enter name <Name>
    Given I enter email <Email>
    Given I enter password <Password>
    Given I submit registration data
    Given I see confirmation page
    Given I verify registration address
    When I wait for PYMK for 30 secs
    And I hide keyboard
    And I press + button on a random Connect
    And I press Clear button
    Then I see Contact list
    And I see contact list with PeoplePicker Random Connect

    Examples: 
      | Email      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

  @id2216 @staging
  Scenario Outline: I can connect to someone from PYMK by tap and typing connect message
    Given I see sign in screen
    Given I press Join button
    Given I press Camera button twice
    Given I confirm selection
    Given I enter name <Name>
    Given I enter email <Email>
    Given I enter password <Password>
    Given I submit registration data
    Given I see confirmation page
    Given I verify registration address
    When I wait for PYMK for 30 secs
    And I hide keyboard
    And I tap on a random contact from PYMK and set it name to <Contact1>
    And I see connect to <Contact1> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    And I see People picker page
    And I navigate back to Conversations List
    Then I see contact list with name <Contact1>

    Examples: 
      | Email      | Password      | Name      | Contact1  | Message       |
      | user1Email | user1Password | user1Name | user2Name | Hellow friend |
