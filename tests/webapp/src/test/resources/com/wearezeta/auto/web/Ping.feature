Feature: Ping

  @staging @id1705
  Scenario Outline: Send ping in 1on1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name in Contact list
    And I open conversation with <Contact>
    When I click ping button  
    Then I see ping message <PING>
    When I click ping button  
    Then I see ping message <PING_AGAIN>

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name | pinged | pinged again |