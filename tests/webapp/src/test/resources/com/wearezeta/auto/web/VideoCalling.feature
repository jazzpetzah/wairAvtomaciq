Feature: VideoCalling

  @C12071 @videocalling
  Scenario Outline: Verify I can start a Video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I end the video call

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:48.0.2564.97 | 60      |

  @C12070 @videocalling
  Scenario Outline: Verify I can accept Video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given <Contact> starts a video call to <Name> using <CallBackend>
    When I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I see the calling bar
    And I accept the incoming video call
    Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I end the video call

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:48.0.2564.97 | 60      |

  @C12072 @videocalling
  Scenario Outline: Verify I can decline Video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given <Contact> starts a video call to me using <CallBackend>
    When I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I see the calling bar
    And I silence the incoming call
    And I do not see the calling bar

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend         |
      | user1Email | user1Password | user1Name | user2Name | chrome:48.0.2564.97 |

  @C12078 @videocalling
  Scenario Outline: Verify I cannot see blocked contact trying to make a video call to me
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    # OtherContact is needed otherwise the search will show up sometimes
    Given Myself is connected to <Contact>,<OtherContact>
    Given Myself blocked <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When <Contact> starts a video call to me using <CallBackend>
    Then <Contact> verifies that call status to Myself is changed to connecting in <Timeout> seconds
    And I do not see the calling bar

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend         | Timeout | OtherContact |
      | user1Email | user1Password | user1Name | user2Name | chrome:48.0.2564.97 | 60      | user3Name    |

  @C12079 @videocalling
  Scenario Outline: Verify I can make a Video call one after another
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I end the video call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And <Contact> accepts next incoming video call automatically
    When I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I end the video call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:48.0.2564.97 | 60      |

  @C12097 @videocalling
  Scenario Outline: Verify I can have video call more than 15 mins
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 60 seconds
    And I see end video call button
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I end the video call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend         | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:48.0.2564.97 | 60      |