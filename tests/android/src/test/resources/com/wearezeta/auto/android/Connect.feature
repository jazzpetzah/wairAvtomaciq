Feature: Connect

  @id191 @id193 @smoke
  Scenario Outline: Send invitation message to a user
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    Then I see contact list loaded with User name <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact  | Message       |
      | aqaUser | aqaPassword | aqaUser | yourUser | Hellow friend |

  @id323 @smoke
  Scenario Outline: Accept connection request
    Given <Contact> connection request is sended to me <Login>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see Connect to <Contact> Dialog page

    Examples: 
      | Login   | Password    | Name    | Contact     | WaitingMess      |
      | aqaUser | aqaPassword | aqaUser | yourContact | 1 person waiting |

  @id1411 @regression
  Scenario Outline: I can see a new inbox for connection when receive new connection request
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I do not see Contact list with name <WaitingMess>
    And <Contact> connection request is sended to me <Login>
    When I tap on contact name <WaitingMess>
    And I see connect to <Contact> dialog
    And I press Ignore connect button
    Then I see contact list loaded with User name <Name>
    Then Contact name <WaitingMess> is not in list

    Examples: 
      | Login   | Password    | Name    | Contact         | WaitingMess      |
      | aqaUser | aqaPassword | aqaUser | yourNotContact1 | 1 person waiting |

  @id539 @id543 @regression @mute
  Scenario Outline: I can see a inbox count increasing/decreasing correctly + I ignore someone from people picker and clear my inbox
    Given <Contact1> connection request is sended to me <Login>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    And <Contact2> connection request is sended to me <Login>
    When I tap on contact name <WaitingMess2>
    And I press Ignore connect button
    Then I see contact list loaded with User name <WaitingMess1>
    And <Contact3> connection request is sended to me <Login>
    And <Contact4> connection request is sended to me <Login>
    And I see contact list loaded with User name <WaitingMess3>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact3>
    And I tap on user name found on People picker page <Contact3>
    And I press Ignore connect button
    And I see contact list loaded with User name <WaitingMess2>
    And I tap on contact name <WaitingMess2>
    And I press Ignore connect button
    And I see contact list loaded with User name <WaitingMess1>
    And I tap on contact name <WaitingMess1>
    And I press Ignore connect button
    And Contact name <WaitingMess1> is not in list

    Examples: 
      | Login    | Password    | Contact1        | WaitingMess1     | Contact2        | WaitingMess2     | Contact3        | Contact4        | WaitingMess3     |
      | yourUser | aqaPassword | yourNotContact1 | 1 person waiting | yourNotContact2 | 2 people waiting | yourNotContact3 | yourNotContact4 | 3 people waiting |

  @id544 @regression
  Scenario Outline: I accept someone from people picker and -1 from inbox as well
    Given <Contact1> connection request is sended to me <Login>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    And <Contact2> connection request is sended to me <Login>
    When I see contact list loaded with User name <WaitingMess1>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact2>
    And I tap on user name found on People picker page <Contact2>
    And I see connect to <Contact2> dialog
    And I Connect with contact by pressing button
    Then I navigate back from dialog page
    And I see contact list loaded with User name <WaitingMess2>

    Examples: 
      | Login       | Password    | Contact1        | Contact2        | WaitingMess1     | WaitingMess2     |
      | yourContact | aqaPassword | yourNotContact1 | yourNotContact2 | 2 people waiting | 1 person waiting |

  @id540 @regression
  Scenario Outline: I can ignore a connect request and reconnect later
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    And Contact name <WaitingMess> is not in list
    And <Contact> connection request is sended to me <Login>
    When I see contact list loaded with User name <WaitingMess>
    And I tap on contact name <WaitingMess>
    And I press Ignore connect button
    And Contact name <WaitingMess> is not in list
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see Connect to <Contact> Dialog page
    And I navigate back from dialog page
    And Contact name <WaitingMess> is not in list

    Examples: 
      | Login   | Password    | Contact         | WaitingMess      |
      | aqaUser | aqaPassword | yourNotContact1 | 1 person waiting |

  @id542 @regression
  Scenario Outline: I want to be taken to the connect inbox right away if the person I select already sent me a connect request
    Given <Contact> connection request is sended to me <Login>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    When I see contact list loaded with User name <WaitingMess>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I Connect with contact by pressing button
    Then I see Connect to <Contact> Dialog page

    Examples: 
      | Login   | Password    | Contact         | WaitingMess      |
      | aqaUser | aqaPassword | yourNotContact2 | 1 person waiting |

  @id547 @regression
  Scenario Outline: I can see the char counter changes when writing the first connect message
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    And I swipe down contact list
    And I see People picker page
    When I tap on Search input on People picker page
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
      | Login   | Password    | Contact         | CounterValue1 | Message | CounterValue2 | FirstState | SecondState |
      | aqaUser | aqaPassword | yourNotContact3 | 140           | test    | 136           | false      | true        |

  @id548 @regression
  Scenario Outline: I can not send first message with space only
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    And I swipe down contact list
    And I see People picker page
    When I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "    "
    Then I see counter value <CounterValue>
    And I see connect button enabled state is <FirstState>

    Examples: 
      | Login   | Password    | Contact         | CounterValue | FirstState |
      | aqaUser | aqaPassword | yourNotContact3 | 136          | false      |

  @id554 @regression
  Scenario Outline: I would not know other person has ignored my connection request
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    When <Contact> ignore all requests
    Then I tap on contact name <Contact>
    And I see that connection is pending

    Examples: 
      | Login   | Password    | Contact    | Message |
      | aqaUser | aqaPassword | yourIgnore | Test    |

  @id541 @regression @mute
  Scenario Outline: I can receive new connection request when app in background
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    When I minimize the application
    And <Contact> connection request is sended to me <Login>
    And I restore the application
    And I see contact list loaded with User name <WaitingMess>
    And I tap on contact name <WaitingMess>
    Then I see connect to <Contact> dialog
    And I see Accept and Ignore buttons
    And I press Ignore connect button

    Examples: 
      | Login   | Password    | Name    | Contact    | WaitingMess      |
      | aqaUser | aqaPassword | aqaUser | yourIgnore | 1 person waiting |

  @id553 @regression
  Scenario Outline: I want to see that the other person has accepted the connect request in the conversation view
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    When <Contact> accept all requests
    Then I tap on contact name <Contact>
    And I see Connect to <Contact> Dialog page

    Examples: 
      | Login   | Password    | Contact    | Message |
      | aqaUser | aqaPassword | yourAccept | Test    |

  @id552 @regression @mute
  Scenario Outline: I want to discard the new connect request (sending) by returning to the search results after selecting someone I’m not connected to
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    And I swipe down contact list
    And I see People picker page
    When I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    Then I close Connect To dialog
    And I see People picker page

    Examples: 
      | Login   | Password    | Contact         |
      | aqaUser | aqaPassword | yourNotContact3 |

  @id550 @regression @mute
  Scenario Outline: I want to initiate a connect request by selecting someone from within a group conversation
    Given User <Contact1> is connected with <Contact2>
    And My Contact <Contact1> has group chat with me <Login> and his Contact <Contact2> with name <ChatName>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Login>
    When I tap on contact name <ChatName>
    And I swipe up on group dialog page
    And I tap on group chat contact <Contact2>
    And I see connect to <Contact2> dialog
    And I tap on edit connect request field
    And I type Connect request "Message"
    And I press Connect button
    And I press back on group chat info page
    And I navigate back from group chat page
    And I see contact list loaded with User name <Contact2>

    Examples: 
      | Login   | Password    | Contact1    | Contact2      | ChatName         |
      | aqaUser | aqaPassword | aqaContact3 | yourGroupChat | ContactGroupChat |

  @id676 @regression
  Scenario Outline: I want to block a person from 1:1 conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe up on dialog page
    #And I see <Contact> user name and email
    And I Press Block button
    And I confirm block
    Then I do not see Contact list with name <Contact>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    Then User info should be shown with Block button

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @id680 @regression
  Scenario Outline: I want to see user has been blocked within the Start UI
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    Then I see contact list loaded with User name <Contact>
    When I tap on contact name <Contact>
    And I see that connection is pending
    And I Press Block button on connect to page
    And I confirm block on connect to page
    And I wait for 5 seconds
    Then I do not see Contact list with name <Contact>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    Then User info should be shown with Block button
    And I click Unblock button

    Examples: 
      | Login   | Password    | Name    | Contact         | Message      |
      | aqaUser | aqaPassword | aqaUser | yourNotContact3 | Hello friend |

  @regression @id720
  Scenario Outline: I do not want to be seen in the search results of someone I blocked
    Given User <Contact> blocks user <Login>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I wait for 60 seconds
    Then I see that no results found

    Examples: 
      | Login   | Password    | Name    | Contact  |
      | aqaUser | aqaPassword | aqaUser | aqaBlock |

  @id723 @regression
  Scenario Outline: I want to unblock someone from their Profile view
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I see user <Contact> found on People picker page
    And I tap on user name found on People picker page <Contact>
    Then User info should be shown with Block button
    And I click Unblock button
    And I see dialog page
    And I navigate back from dialog page
    And I see contact list loaded with User name <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
