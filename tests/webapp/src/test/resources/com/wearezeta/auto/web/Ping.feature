Feature: Ping

  @C1717 @regression @localytics
  Scenario Outline: Send ping in 1on1
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <Contact>
    Then Soundfile ping_from_me did not start playing
    When I click ping button
    Then I see <PING> action in conversation
    Then Soundfile ping_from_me did start playing
    When I click ping button
    Then I see <PING> action 2 times in conversation
    Then Soundfile ping_from_me did start playing
    And I see localytics event <Event> with attributes <Attributes>

    Examples: 
      | Login      | Password      | Name      | Contact   | PING       | Event                        | Attributes                                                                                            |
      | user1Email | user1Password | user1Name | user2Name | you pinged | media.completed_media_action | {\"action\":\"ping\",\"conversation_type\":\"one_to_one\",\"is_ephemeral\":false,\"with_bot\":false}" |

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

  @C1719 @regression
  Scenario Outline: Verify I can receive ping (group conversation)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given user <Contact2> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    And I open conversation with <ChatName>
    Then Soundfile ping_from_them did not start playing
    And User <Contact2> pinged in the conversation with <ChatName>
    Then I see <PING> action in conversation
    Then Soundfile ping_from_them did start playing

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName             | PING   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessageGroupChat | pinged |
