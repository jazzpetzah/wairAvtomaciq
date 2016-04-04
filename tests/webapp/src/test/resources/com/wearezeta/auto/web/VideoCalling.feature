Feature: VideoCalling

  @C12071 @videocalling @smoke
  Scenario Outline: Verify I can start Video call from conversation view
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    When I end the video call
    Then I do not see the call controls for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @C12070 @videocalling @smoke
  Scenario Outline: Verify I can accept Video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And <Contact> starts a video call to me
    Then I see the incoming call controls for conversation <Contact>
    And I see accept video call button for conversation <Contact>
    And I see decline call button for conversation <Contact>
    And I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    When I end the video call
    Then I do not see the call controls for conversation <Contact>
    And I do not see accept video call button for conversation <Contact>
    And I do not see decline call button for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @C12072 @videocalling @regression
  Scenario Outline: Verify I can decline Video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given <Contact> starts a video call to me
    When I am signed in properly
    Then I see the incoming call controls for conversation <Contact>
    And I see accept video call button for conversation <Contact>
    And I see decline call button for conversation <Contact>
    When I ignore the call from conversation <Contact>
    Then I do not see the call controls for conversation <Contact>
    And I do not see accept video call button for conversation <Contact>
    And I do not see decline call button for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | chrome      |

  @C12078 @videocalling @regression
  Scenario Outline: Verify I cannot see blocked contact trying to make a video call to me
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    # OtherContact is needed otherwise the search will show up sometimes
    Given Myself is connected to <Contact>,<OtherContact>
    Given Myself blocked <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When <Contact> starts a video call to me
    Then <Contact> verifies that call status to Myself is changed to connecting in <Timeout> seconds
    And I do not see accept video call button for conversation <Contact>
    And I do not see decline call button for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout | OtherContact |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      | user3Name    |

  @C12079 @videocalling @regression
  Scenario Outline: Verify I can make a Video call one after another
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
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
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
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

  @C12075 @videocalling @regression
  Scenario Outline: Verify I can cancel the outgoing video call (as a caller)
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I start a video call
    And I see my self video view
    And I see the outgoing call controls for conversation <Contact>
    And I see mute call button for conversation <Contact>
    And I see hang up button for conversation <Contact>
    Then I hang up call with conversation <Contact>
    And I do not see my self video view
    And I do not see hang up button for conversation  <Contact>
    And I do not see mute call button for conversation <Contact>
    And I do not see video button for conversation <Contact>


    Examples:
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C12073 @videocalling @regression
  Scenario Outline: Verify I can mute Video call after the call is established
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I start a video call
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

  @C49971 @videocalling @regression
  Scenario Outline: Verify I can mute Video call before the call is established
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I start a video call
    And I see mute button for conversation <Contact> is not pressed
    Then I click mute call button for conversation <Contact>
    And I see mute button for conversation <Contact> is pressed
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

  @C48229 @videocalling @regression
  Scenario Outline: Verify I can start 1:1 Video Call from Start UI
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I wait until <Contact> exists in backend search results
    And I see Contact list with name <Contact>
    When I open People Picker from Contact List
    And I type <Contact> in search field of People Picker
    And I see user <Contact> found in People Picker
    And I select <Contact> from People Picker results
    And I click Video Call button on People Picker page
    Then I see the outgoing call controls for conversation <Contact>
    When <Contact> accepts next incoming video call automatically
    Then I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I end the video call
    Then I do not see the call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @C48230 @videocalling @regression
  Scenario Outline: Verify you don't see video call button when you're creating group from Start UI
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I wait until <Contact1> exists in backend search results
    And I wait until <Contact2> exists in backend search results
    When I open People Picker from Contact List
    And I type <Contact1> in search field of People Picker
    And I see user <Contact1> found in People Picker
    And I select <Contact1> from People Picker results
    Then I see Video Call button on People Picker page
    When I type <Contact2> in search field of People Picker
    And I see user <Contact2> found in People Picker
    And I select <Contact2> from People Picker results
    Then I do not see Video Call button on People Picker page

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @C77944 @videocalling @staging
  Scenario Outline: Verify I can start Video call after declining an audio call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given <Contact> calls me
    When I am signed in properly
    Then I see the incoming call controls for conversation <Contact>
    And I see decline call button for conversation <Contact>
    When I ignore the call from conversation <Contact>
    Then I do not see the call controls for conversation <Contact>
    And I open conversation with <Contact>
    When I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    When I end the video call
    Then I do not see the call controls for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @C77975 @videocalling @regression
  Scenario Outline: Verify I see the timer/duration of the video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And <Contact> starts a video call to me
    Then I see the incoming call controls for conversation <Contact>
    And I see accept video call button for conversation <Contact>
    And I see decline call button for conversation <Contact>
    And I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And I see the video call timer
    When I end the video call
    Then I do not see the video call timer

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |

  @C78099 @videocalling @regression
  Scenario Outline: Verify I can see the incoming video call when I just login
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    When I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    Then I see Sign In page
    And <Contact> starts a video call to me
    When I Sign in using login <Login> and password <Password>
    And I am signed in properly
    Then I see the incoming call controls for conversation <Contact>
    And I accept the call from conversation <Contact>
    Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    When I end the video call
    Then I do not see the call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |