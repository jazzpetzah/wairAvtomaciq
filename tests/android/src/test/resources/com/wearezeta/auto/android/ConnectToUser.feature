Feature: Connect to User

  @id191 @id193 @smoke @nonUnicode
  Scenario Outline: Send invitation message to a user
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I tap on edit connect request field
    And I type Connect request <Message>
    And I press Connect button
    Then I see contact list loaded with User name <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact  | Message       |
      | aqaUser | aqaPassword | aqaUser | yourUser | Hellow friend |

  @id218 @regression @nonUnicode
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

  @id223 @regression @nonUnicode
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

  @id220 @nonUnicode @regression
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

  @id225 @nonUnicode @regression
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

  @id319 @nonUnicode @regression
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

  @id536 @nonUnicode @regression
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

  @id539 @id543 @nonUnicode @regression @mute
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

  @id544 @nonUnicode @regression
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
