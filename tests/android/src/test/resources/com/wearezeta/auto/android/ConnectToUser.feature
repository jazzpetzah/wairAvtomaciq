Feature: Connect to User

  @id191 @id193 @smoke @mute
  Scenario Outline: Send invitation message to a user
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    Then I see contact list loaded with User name <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact  | Message       |
      | aqaUser | aqaPassword | aqaUser | yourUser | Hellow friend |

  @id218 @regression
  Scenario Outline: I can do full name search for existing 1:1(non-archive)
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    Then I see user <Contact>  in People picker

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

  @id223 @regression
  Scenario Outline: I can do partial name search for existing 1:1
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field part <Size> of user name to connect to <Contact>
    Then I see user <Contact>  in People picker

    Examples: 
      | Login   | Password    | Name    | Contact     | Size |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | 4    |

  @id220 @regression
  Scenario Outline: I can do full name search for existing group convo(non-archive)
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <GroupChatName>
    Then I see group <GroupChatName>  in People picker

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName          |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | PeoplePicker GroupChat |

  @id225 @regression
  Scenario Outline: I can do partial name search for existing group convo(non-archive)
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field part <Size> of user name to connect to <GroupChatName>
    Then I see group <GroupChatName>  in People picker

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName          | Size |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | PeoplePicker GroupChat | 5    |

  @id319 @regression
  Scenario Outline: I can create group chat from People picker
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact1>
    And I  long tap on user name found on People picker page <Contact1>
    And I add in search field user name to connect to <Contact2>
    And I  long tap on user name found on People picker page <Contact2>
    And I tap on create conversation
    Then I see group chat page with users <Contact1> <Contact2>

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName          |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | PeoplePicker GroupChat |

  @id536 @regression
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
    Then I see Connect to <Contact2> Dialog page
    And I navigate back from dialog page
    And I see contact list loaded with User name <WaitingMess2>

    Examples: 
      | Login       | Password    | Contact1        | Contact2        | WaitingMess1     | WaitingMess2     |
      | yourContact | aqaPassword | yourNotContact1 | yourNotContact2 | 2 people waiting | 1 person waiting |

  @id540 @regression @mute 
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

  @id542 @regression @mute 
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

  @id547 @regression @mute 
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

  @id548 @regression @mute
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

  @id554 @regression @mute
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
