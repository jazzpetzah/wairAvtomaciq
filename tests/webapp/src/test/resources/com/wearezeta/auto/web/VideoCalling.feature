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
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @C12070 @videocalling
  Scenario Outline: Verify I can accept Video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given <Contact> starts a video call to <Name> using <CallBackend>
    When I see my avatar on top of Contact list
    And I see the name of user <Contact> in calling banner in conversation list
    And I see accept video call button for conversation <Contact>
    And I see decline call button for conversation <Contact>
    And I click the accept call button in conversation list
    Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I end the video call
    And I do not see accept video call button for conversation <Contact>
    And I do not see decline call button for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @C12072 @videocalling
  Scenario Outline: Verify I can decline Video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given <Contact> starts a video call to me using <CallBackend>
    When I see my avatar on top of Contact list
    Then I see the name of user <Contact> in calling banner in conversation list
    And I see accept video call button for conversation <Contact>
    And I see decline call button for conversation <Contact>
    When I click the decline call button in conversation list
    Then I do not see accept video call button for conversation <Contact>
    And I do not see decline call button for conversation <Contact>
    And I do not see my self video view

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
    And I do not see accept video call button for conversation <Contact>
    And I do not see decline call button for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout | OtherContact |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      | user3Name    |

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
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

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
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @C12075 @videocalling
  Scenario Outline: Verify I can cancel the outgoing video call (as a caller)
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I start a video call
    And I see my self video view
    And I see the name of user <Contact> in calling banner in conversation list
    And I see mute call button for conversation <Contact>
    And I see video button for conversation <Contact>
    And I see end call button for conversation <Contact>
    Then I click end call button from conversation list
    And I do not see my self video view
    And I do not see end call button for conversation <Contact>
    And I do not see mute call button for conversation <Contact>
    And I do not see video button for conversation <Contact>


    Examples:
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C12073 @videocalling
  Scenario Outline: Verify I can mute Video call after the call is established
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
    When I see mute button on video call page is not pressed
    And I click mute button on video call page
    Then I see mute button on video call page is pressed
    When I click mute button on video call page
    Then I see mute button on video call page is not pressed
    When I end the video call
    Then I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @C49971 @videocalling
  Scenario Outline: Verify I can mute Video call before the call is established
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I start a video call
    And I see mute button in conversation list is not pressed
    Then I click mute call button in conversation list
    And I see mute button in conversation list is pressed
    And <Contact> accepts next incoming video call automatically
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    When I see mute button on video call page is pressed
    And I click mute button on video call page
    Then I see mute button on video call page is not pressed
    When I end the video call
    Then I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |