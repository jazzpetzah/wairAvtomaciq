Feature: Calling

  @regression @id1860
  Scenario Outline: Send text, image and knock while in the call with same user
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in using login <Login> and password <Password>
    When I see my name on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    And I end the call
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

  @regression @id2237
  Scenario Outline: Call a user twice in a row
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in using login <Login> and password <Password>
    When I see my name on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    And I end the call
    Then <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    And <Contact> accepts next incoming call automatically
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the calling bar
    And <Contact> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | webdriver   | 120     |

  @regression @id1866
   Scenario Outline: Verify I can call a user for more than 15 mins
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I Sign in using login <Login> and password <Password>
    When I see my name on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 900 seconds
    And I see the calling bar
    And I end the call
    And I verify browser log is empty
    And <Contact> stops all waiting instances

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
	 | user1Email | user1Password | user1Name | user2Name | webdriver   | 120     |

@staging @id1839
   Scenario Outline: Verify calling not supported in webapp (no calling support)
      Given My browser does not support calling
      Given There are 2 users where <Name> is me
      Given Myself is connected to <Contact>
      Given I Sign in using login <Login> and password <Password>
      When I see my name on top of Contact list
      And I open conversation with <Contact>
      And <Contact> calls me using <CallBackend>
      Then I do not see the calling bar
      And I wait for 3 seconds
      And I see a warning
      And I see "Learn more" link in warning
      When I close the warning
      Then I do not see a warning
      And I see calling button
      When I call
      Then I see a warning
      And I see "Learn more" link in warning
      And I verify browser log is empty

      Examples: 
	 | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
	 | user1Email | user1Password | user1Name | user2Name | autocall    | 120     |

  @regression @id2013
  Scenario Outline: Missed call notification (caller)
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    When I see my name on top of Contact list
    And I open conversation with <Contact>
    And I call
    Then I wait for 2 seconds
    And I end the call
    When I open conversation with <Contact>
    Then I see conversation with my missed call

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  # This has to work even in browsers, which don't support calling
  @regression @id2014
  Scenario Outline: Missed call notification (adressee)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Me
    Given I Sign in using login <Login> and password <Password>
    When I see my name on top of Contact list
    And I open self profile
    And <Contact> calls me using <CallBackend>
    Then I wait for 1 seconds
    When <Contact> stops all calls to me
    And I wait for 1 seconds
    Then I see missed call notification for conversation <Contact>
    When I open conversation with <Contact>
    Then I do not see missed call notification for conversation <Contact>
    Then I see conversation with missed call from <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | autocall    |
