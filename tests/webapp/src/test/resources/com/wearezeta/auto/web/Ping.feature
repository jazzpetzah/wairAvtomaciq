Feature: Ping

  @C1717 @smoke
  Scenario Outline: Send ping in 1on1
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <Contact>
    When I click ping button
    Then I see <PING> action in conversation
    When I click ping button
    Then I see <PING> action 2 times in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | PING       |
      | user1Email | user1Password | user1Name | user2Name | you pinged |

  @C1718 @regression
  Scenario Outline: Verify you can Ping several times in a row
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <Contact>
    When I click ping button
    Then I see <PING> action in conversation
    When I click ping button
    Then I see <PING> action 2 times in conversation
    When I click ping button
    Then I see <PING> action 3 times in conversation

    Examples: 
      | Login      | Password      | Name      | Contact   | PING       |
      | user1Email | user1Password | user1Name | user2Name | you pinged |

  @C1719 @smoke
  Scenario Outline: Verify you can see Ping on the other side (group conversation)
    Given There are 3 users where <Name> is me
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given User <Contact1> changes avatar picture to default
    Given I switch to Sign In page
    Given I Sign in using login <Login1> and password <Password1>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    Given I open self profile
    Given I click gear button on self profile page
    Given I select Log out menu item on self profile page
    Given I see the clear data dialog
    Given I click Logout button on clear data dialog
    Given I see Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <ChatName>
    When I click ping button
    Then I see <PING> action in conversation
    And I open conversation with <Contact1>
    When I click ping button
    Then I see <PING> action in conversation
    When I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And User <Name> sends message <Message> to conversation <Contact1>
    And I see Sign In page
    And User <Contact1> is me
    And I Sign in using login <Login1> and password <Password1>
    Then I see my avatar on top of Contact list
    And I see ping icon in conversation with <ChatName>
    And I open conversation with <ChatName>
    Then I see <PING> action for <Name> in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Login1     | Password1     | Contact2  | ChatName             | PING   | Message |
      | user1Email | user1Password | user1Name | user2Name | user2Email | user2Password | user3Name | SendMessageGroupChat | pinged | Message |
