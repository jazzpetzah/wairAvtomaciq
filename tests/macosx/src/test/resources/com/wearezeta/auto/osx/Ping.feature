Feature: Ping

  @id1290 @regression
  Scenario Outline: Ping group chat
    Given I have 1 users and 2 contacts for 1 users
    Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    When I ping user
    Then I see message YOU PINGED in conversation
    And I ping again user
    Then I see message YOU PINGED AGAIN in conversation

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName      |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | PingGroupChat |
