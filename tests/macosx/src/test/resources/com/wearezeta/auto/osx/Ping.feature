Feature: Ping

  @id1290 @regression
  Scenario Outline: Ping group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    When I ping user
    Then I see message YOU PINGED in conversation
    And I ping again user
    Then I see message YOU PINGED AGAIN in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | PingGroupChat |

  @regression @id1291
  Scenario Outline: Verify you can see Ping on the other side (group conversation)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    And User <Contact1> pings in chat <ChatName>
    And I see message <Contact1> PINGED in conversation
    And User <Contact1> pings again in chat <ChatName>
    And I see message <Contact1> PINGED AGAIN in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | PingGroupChat |
