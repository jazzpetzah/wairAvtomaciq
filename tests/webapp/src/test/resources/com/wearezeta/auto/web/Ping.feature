Feature: Ping

  @smoke @id1705
  Scenario Outline: Send ping in 1on1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
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

  @regression @id1706
  Scenario Outline: Verify you cannot Ping several times in a row
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
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

  @smoke @id1709
  Scenario Outline: Verify you can see Ping on the other side (group conversation)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When I click ping button
    Then I see ping message <PING>
    When I open self profile
    And I click gear button on self profile page
      And I select log out menu item on self profile page
    And User <Name> sent message <Message> to conversation <Contact1>
    And I see Sign In page
    And User <Contact1> is me
    And I Sign in using login <Login1> and password <Password1>
    Then I see my avatar on top of Contact list
    And I see ping icon in conversation with <ChatName>
    And I open conversation with <ChatName>
    Then I see ping message <PING>

    Examples: 
      | Login      | Password      | Name      | Contact1  |Login1      | Password1     | Contact2  | ChatName             | PING   | Message |
      | user1Email | user1Password | user1Name | user2Name | user2Email | user2Password | user3Name | SendMessageGroupChat | pinged | Message |