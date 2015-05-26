Feature: Ping

  # Ping again in IE may fail because it takes quite long to locate this button after 
  # the first ping was already sent
  @smoke @id1705
  Scenario Outline: Send ping in 1on1
    Given My browser supports fast location by XPath
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I click ping button  
    Then I see ping message <PING>
    When I click ping button  
    Then I see ping message <PING_AGAIN>

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name | pinged | pinged again |

  @smoke @id1706
  Scenario Outline: Verify you cannot Ping several times in a row
    Given My browser supports fast location by XPath
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I click ping button
    Then I see ping message <PING>
    When I click ping button
    Then I see ping message <PING_AGAIN>
    Then I see only one ping message

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name | pinged | pinged again |