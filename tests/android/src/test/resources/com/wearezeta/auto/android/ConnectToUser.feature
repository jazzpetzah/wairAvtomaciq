Feature: Connect to User

  @smoke @nonUnicode
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

  @218 @staging @nonUnicode
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

  @223 @staging @nonUnicode
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

  @220 @nonUnicode @staging
  Scenario Outline: I can do full name search for existing group convo(non-archive)
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field part of user name to connect to <GroupChatName>
    Then I see group <GroupChatName>  in People picker

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName          |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | PeoplePicker GroupChat |

  @225 @nonUnicode @staging
  Scenario Outline: I can do full name search for existing group convo(non-archive)
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
