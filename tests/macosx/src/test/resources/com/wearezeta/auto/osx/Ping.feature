Feature: Ping

  @id1290 @regression
  Scenario Outline: Ping group chat
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
  
  @torun @staging @id1291
  Scenario Outline: Verify you can see Ping on the other side (group conversation)
  	Given I have group chat with name <ChatName> with <Contact1> and <Contact2>
  	And I Sign in using login <Login> and password <Password>
  	And I see my name <Name> in Contact list
  	And I open conversation with <ChatName>
  	And User <Contact1> pings in chat <ChatName>
  	Then I see User <Contact1> Pinged message in the conversation
  	And User <Contact1> pings again in chat <ChatName>
  	Then I see User <Contact1> Pinged Again message in the conversation
  		
  	Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | ChatName      |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | PingGroupChat |
