Feature: VideoCalling

  @C12071 @videocalling @smoke @localytics
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
    When I see video call button
    Then I see correct video call button tooltip
    When I start a video call
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I end the video call
    Then I do not see the call controls for conversation <Contact>
    And I do not see my self video view
    And I see localytics event <Event> with attributes <Attributes>

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout | Event                        | Attributes                                                                                                             |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      | media.completed_media_action | {\\"action\\":\\"video_call\\",\\"conversation_type\\":\\"one_to_one\\",\\"is_ephemeral\\":false,\\"with_bot\\":false} |

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
    And I see my self video view
    And I accept the call from conversation <Contact>
    Then I see my self video view
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I end the video call
    Then I do not see the call controls for conversation <Contact>
    And I do not see accept video call button for conversation <Contact>
    And I do not see decline call button for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

  @C12072 @videocalling @regression
  Scenario Outline: Verify I can decline Video call
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
    And I see my self video view
    When I ignore the call from conversation <Contact>
    Then I see join call button for conversation <Contact>
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
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      | user3Name    |

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
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I end the video call
    Then I do not see my self video view
    And <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    When <Contact> accepts next incoming video call automatically
    And I start a video call
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I end the video call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

  @C12097 @long-call
  Scenario Outline: Verify I can have video call more than 30 mins
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
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
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
# 5 minutes
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
# 10 minutes
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
# 15 minutes
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
# 20 minutes
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
# 25 minutes
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
# 30 minutes
    And I wait for 60 seconds
    And I see end video call button
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And I end the video call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

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
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I see mute button on video call page is not pressed
    And I click mute button on video call page
    Then I see mute button on video call page is pressed
#    And <Contact> verifies to not get audio data from me
    When I click mute button on video call page
    Then I see mute button on video call page is not pressed
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    When I end the video call
    Then I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

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
    When <Contact> accepts next incoming video call automatically
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
#    And <Contact> verifies to not get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I see mute button on video call page is pressed
    And I click mute button on video call page
    Then I see mute button on video call page is not pressed
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    When I end the video call
    Then I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

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
    When I open search by clicking the people button
    And I type <Contact> in search field of People Picker
    And I see user <Contact> found in People Picker
    And I select <Contact> from People Picker results
    And I click Video Call button on People Picker page
    Then I see the outgoing call controls for conversation <Contact>
    When <Contact> accepts next incoming video call automatically
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    Then I see the ongoing call controls for conversation <Contact>
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I end the video call
    Then I do not see the call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

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
    When I open search by clicking the people button
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

  @C77944 @videocalling @regression
  Scenario Outline: Verify I can start Video call after declining an audio call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    Then <Contact> calls me
    And I see the incoming call controls for conversation <Contact>
    And I see decline call button for conversation <Contact>
    When I ignore the call from conversation <Contact>
    And I see join call button for conversation <Contact>
    And I open conversation with <Contact>
    When I start a video call
    Then I see my self video view
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    When I wait for 5 seconds
    Then I see video call is minimized
    And I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    When I hang up call with conversation <Contact>
    Then I do not see the call controls for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

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
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

  @C78099 @videocalling @regression
  Scenario Outline: Verify I can see the incoming video call when I just login
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    Then I see Sign In page
    And <Contact> starts a video call to me
    When I Sign in using login <Login> and password <Password>
    And I am signed in properly
    Then I see the incoming call controls for conversation <Contact>
    And I accept the call from conversation <Contact>
    Then I see my self video view
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And I see video call is maximized
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I end the video call
    Then I do not see the call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

  @C12076 @videocalling @regression
  Scenario Outline: Verify I get missed call indication when someone called (video)
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I open conversation with <Contact2>
    And <Contact1> starts a video call to me
    Then I see the incoming call controls for conversation <Contact1>
    And I see accept video call button for conversation <Contact1>
    When <Contact1> stops calling me
    Then I do not see the call controls for conversation <Contact1>
    And <Contact1> verifies that call status to <Name> is changed to DESTROYED in <Timeout> seconds
    And I do not see accept video call button for conversation <Contact1>
    And I see missed call notification in the conversation list for conversation <Contact1>
    When I open conversation with <Contact1>
    Then I do not see missed call notification in the conversation list for conversation <Contact1>
    And I see <Action> action for <Contact1> in conversation
    
    Examples:
      | Login      | Password      | Name      | Contact1   | Contact2   | CallBackend | Timeout | Action |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | chrome      | 30      | called |

  @C87624 @videocalling @regression
  Scenario Outline: Verify I see notification when I start a second video call
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I open conversation with <Contact1>
    And I start a video call
    Then I see the outgoing call controls for conversation <Contact1>
    When I open conversation with <Contact2>
    And I start a video call
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    And I see the outgoing call controls for conversation <Contact1>
    When I start a video call
    Then I see another call warning modal
    When I click on "Hang Up" button in another call warning modal
    Then I do not see another call warning modal
    And I see the outgoing call controls for conversation <Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome      |

  @C77946 @videocalling @regression
  Scenario Outline: Verify I can start an audio call back after declining a video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And <Contact> starts a video call to me
    Then I see the incoming call controls for conversation <Contact>
    And I see decline call button for conversation <Contact>
    When I ignore the call from conversation <Contact>
    Then I see join call button for conversation <Contact>
    And I do not see my self video view
    When I open conversation with <Contact>
    And I call
    Then I see the ongoing call controls for conversation <Contact>
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    When I hang up call with conversation <Contact>
    Then I do not see the call controls for conversation <Contact>

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

  @C165108 @videocalling @calling @regression
  Scenario Outline: Verify you can multitask while video call is minimized
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given <Contact1> accepts next incoming video call automatically
    Given <Contact1> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact1>
    And I start a video call
    Then <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    And <Contact1> verifies that all audio flows have greater than 0 bytes
    And <Contact1> verifies that all video flows have greater than 0 bytes
    Then I see my self video view
    And I see video call is maximized
    When I minimize video call
    Then I see video call is minimized
    Then I do not see my self video view
    And I see broadcast indicator is shown for video
    When I write random message
    And I send message
    Then I see random message in conversation
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    When I open conversation with <Contact2>
    And I write random message
    And I send message
    Then I see random message in conversation
    When I send picture <PictureName> to the current conversation
    Then I see sent picture <PictureName> in the conversation view
    When I open conversation with <Contact1>
    Then I see broadcast indicator is shown for video
    When I maximize video call via button on remote video
    Then I see video call is maximized
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    When I end the video call
    Then I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend | Timeout | PictureName               |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome      | 30      | userpicture_landscape.jpg |

  @C165129 @regression @videocalling @WEBAPP-3259
  Scenario Outline: Verify that current video call is terminated if you want to call someone else
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>    
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I open conversation with <Contact1>
    Then <Contact1> accept next incoming video call automatically
    And <Contact2> accept next incoming call automatically
    And <Contact1>,<Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    When I start a video call
    Then <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    And <Contact1> verifies that all audio flows have greater than 0 bytes
    And <Contact1> verifies that all video flows have greater than 0 bytes
    And I see my self video view
    And I see video call is maximized
    When I minimize video call
    Then I see video call is minimized
    And I see broadcast indicator is shown for video
    When I open conversation with <Contact2>
    And I call
    Then I see another call warning modal
    When I close the another call warning modal
    Then I do not see another call warning modal
    And I see video call is minimized
    And I see the ongoing call controls for conversation <Contact1>
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    When I call
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    And I see video call is minimized
    And I see the ongoing call controls for conversation <Contact1>
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    When I call
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    And I see video call is minimized
    And I see the ongoing call controls for conversation <Contact1>
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    When I call
    Then I see another call warning modal
    When I click on "Hang Up" button in another call warning modal
    Then I do not see another call warning modal
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I do not see the ongoing call controls for conversation <Contact1>
    And I see the ongoing call controls for conversation <Contact2>
    And <Contact2> verifies to have 1 flow
    And <Contact2> verifies to get audio data from me
    And <Contact2> verifies that all audio flows have greater than 0 bytes
    And I hang up call with conversation <Contact2>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome      | 20      |

  @C165142 @regression @videocalling
  Scenario Outline: Verify that current video call is terminated if you want to videocall someone else
    Given My browser supports calling
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>    
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    When I am signed in properly
    And I open conversation with <Contact1>
    Then <Contact1> accept next incoming video call automatically
    And <Contact2> accept next incoming video call automatically
    And <Contact1>,<Contact2> verify that waiting instance status is changed to waiting in <Timeout> seconds
    When I start a video call
    Then <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    And <Contact1> verifies that all audio flows have greater than 0 bytes
    And <Contact1> verifies that all video flows have greater than 0 bytes
    And I see my self video view
    And I see video call is maximized
    When I minimize video call
    Then I see video call is minimized
    And I see broadcast indicator is shown for video
    When I open conversation with <Contact2>
    When I start a video call
    Then I see another call warning modal
    When I close the another call warning modal
    Then I do not see another call warning modal
    And I see video call is minimized
    And I see the ongoing call controls for conversation <Contact1>
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    When I start a video call
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    And I see video call is minimized
    And I see the ongoing call controls for conversation <Contact1>
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    When I start a video call
    Then I see another call warning modal
    When I click on "Cancel" button in another call warning modal
    Then I do not see another call warning modal
    And I see video call is minimized
    And I see the ongoing call controls for conversation <Contact1>
    And <Contact1> verifies to have 1 flow
    And <Contact1> verifies to get audio data from me
    And <Contact1> verifies to get video data from me
    When I start a video call
    Then I see another call warning modal
    When I click on "Hang Up" button in another call warning modal
    Then I do not see another call warning modal
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see my self video view
    And I see video call is maximized
    And <Contact2> verifies to have 1 flow
    And <Contact2> verifies to get audio data from me
    And <Contact2> verifies to get video data from me
    And <Contact2> verifies that all audio flows have greater than 0 bytes
    And <Contact2> verify that all video flows have greater than 0 bytes
    When I minimize video call
    Then I see video call is minimized
    And I do not see the ongoing call controls for conversation <Contact1>
    And I see the ongoing call controls for conversation <Contact2>
    And I see broadcast indicator is shown for video

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | user3Name | chrome      | 20      |

  @C12074 @videocalling @regression
  Scenario Outline: Verify I can disable video in Video call and enable it back
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
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see video call is maximized
    And I see video button pressed
    And <Contact> verifies to have 1 flows
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I click on video button
    And I see video button unpressed
    And <Contact> verify that all audio flows have greater than 0 bytes
    And <Contact> verifies to get audio data from me
#    And <Contact> verifies to not get video data from me
    Then I see my self video is off
    And I see video call is maximized
    When I click on video button
    And I see video button pressed
    And <Contact> verify that all audio flows have greater than 0 bytes
    And <Contact> verify that all video flows have greater than 0 bytes
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    Then I see my self video is on
    And I see my self video is not black
    And I see video call is maximized
    When I minimize video call
    Then I see broadcast indicator is shown for video
    When I click on video button
    And I see video button unpressed
    Then I see broadcast indicator is not shown for video
    And <Contact> verifies to get audio data from me
    When I click on video button
    Then I see video button pressed
    And I see broadcast indicator is shown for video
    When <Contact> switches video off
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    When I maximize video call via titlebar
    When <Contact> switches video on
    And I see video call is maximized
    And <Contact> verify that all audio flows have greater than 0 bytes
    And <Contact> verify that all video flows have greater than 0 bytes
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    When I end the video call
    Then I do not see the call controls for conversation <Contact>
    And I do not see my self video view

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |

  @C183895 @videocalling @regression
  Scenario Outline: Verify my video is not shown if my audio call is declined but I got called back via video
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I call
    Then <Contact> declines call from conversation <Contact>
    And <Contact> starts a video call to me
    And I see video call is minimized
    And I see video button unpressed
    And <Contact> verifies to have 1 flows
    And <Contact> verifies to get audio data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    When I click on video button
    And I see video button pressed
    And <Contact> verify that all audio flows have greater than 0 bytes
    And <Contact> verify that all video flows have greater than 0 bytes
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    When I maximize video call via button on remote video
    Then I see my self video is on
    And I see my self video is not black
    Then I see broadcast indicator is shown for video

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 20      |
