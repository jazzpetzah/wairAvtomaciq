Feature: Search

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
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | 12   |

  @id225 @regression
  Scenario Outline: I can do partial name search for existing group convo(non-archive)
    Given I have 1 users and 2 contacts for 1 users
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field part <Size> of user name to connect to <GroupChatName>
    Then I see group <GroupChatName>  in People picker

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName           | Size |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | PeoplePicker GroupChat1 | 5    |

  @id327 @smoke
  Scenario Outline: Open/Close People picker
    Given I have 1 users and 0 contacts for 0 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I press Clear button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
