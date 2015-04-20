Feature: Calling

  @staging @id1860
  Scenario Outline: Send text, image and knock while in the call with same user
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    When I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I write random message
    And I send message
    And I click ping button
    And I send picture <PictureName> to the current conversation
    Then I see random message in conversation
    And I see ping message <PING>
    And I see sent picture <PictureName> in the conversation view
    Then <Contact> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PictureName               | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | pinged | userpicture_landscape.jpg | webdriver   | 120     |

  @staging @id2237
  Scenario Outline: Call a user twice in a row
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    And I open conversation with <Contact>
    When I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I end the call
    Then <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    And <Contact> accepts next incoming call automatically
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | webdriver   | 120     |

  @staging @id2014
  Scenario Outline: Missed call notification (adressee)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see my name on top of Contact list
    When <Contact> calls me using <CallBackend>
    And I wait for 5 seconds
    And <Contact> stops all calls to me
    When I open conversation with <Contact>
    Then I see conversation with missed call from <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |
