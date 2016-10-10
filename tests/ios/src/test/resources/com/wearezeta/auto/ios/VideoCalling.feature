Feature: Video Calling

  @C12102 @calling_basic @video_calling @fastLogin
  Scenario Outline: Verify initiating Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Call button
    And I accept alert
    Then I see call status message contains "<Contact> ringing"
    And I see Leave button on Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C12105 @calling_basic @video_calling @fastLogin
  Scenario Outline: Verify cancelling Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Call button
    And I accept alert
    Then I see call status message contains "<Contact> ringing"
    When I tap Leave button on Calling overlay
    Then I see missed call from contact YOU

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C12101 @calling_basic @video_calling @rc @fastLogin
  Scenario Outline: Verify accepting video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to <Name>
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And I accept alert
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flows
    And <Contact> verifies that all flows have greater than 0 bytes
    # These steps are unstable because of Appium slowness
    # Then I see Mute button on Video Calling overlay
    # And I see Switch Camera button on Video Calling overlay
    # And I see Leave button on Video Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C12104 @calling_basic @video_calling @fastLogin
  Scenario Outline: Verify ignoring Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to me
    And I see call status message contains "<Contact> calling"
    And I tap Ignore button on the Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C12107 @calling_basic @video_calling @fastLogin
  Scenario Outline: Verify getting missed call indication when someone called
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I remember the state of <Contact> conversation item
    When <Contact> starts a video call to me
    Then I see call status message contains "<Contact> calling"
    And <Contact> stops calling me
    And I do not see Calling overlay
    Then I see the state of <Contact> conversation item is changed
    And I tap on contact name <Contact>
    And I see missed call from contact <Contact>

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C12114 @calling_basic @video_calling @fastLogin
  Scenario Outline: Verify I can switch to another incoming audio call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> starts instance using <VideoCallBackend>
    Given <Contact2> starts instance using <AudioCallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given <Contact1> starts a video call to me
    Given I see call status message contains "<Contact1> calling"
    Given I tap Accept Video button on Calling overlay
    Given I accept alert
    When <Contact2> calls me
    And I see call status message contains "<Contact2> calling"
    Then I tap Accept button on Calling overlay
    And I do not see Accept Video button on Calling overlay
    And <Contact2> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact1> verifies that call status to me is changed to destroyed in <Timeout> seconds

    Examples:
      | Name      | Contact1  | Contact2  | VideoCallBackend | AudioCallBackend | Timeout |
      | user1Name | user2Name | user3Name | chrome           | chrome           | 60      |

  @C12110 @calling_basic @video_calling @fastLogin
  Scenario Outline: Verify blocked contact could not get through with a Video call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself blocks user <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    And I do not see conversation <Contact> in conversations list
    When <Contact> starts a video call to me
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C28851 @calling_basic @video_calling @fastLogin
  Scenario Outline: Verify starting video call with action button in Search
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I accept alert
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I tap Video call action button on People picker page
    And I accept alert
    Then I see call status message contains "<Contact> ringing"
    And I see Leave button on Video Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C12115 @calling_basic @video_calling @fastLogin
  Scenario Outline: Verify I can switch to another video call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given <Contact>,<Contact2> start instance using <VideoCallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> start a video call to me
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And I accept alert
    And <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    Then I see Switch Camera button on Video Calling overlay
    When <Contact2> starts a video call to me
    And I tap Accept Video button on Calling overlay
    Then I see Mute button on Video Calling overlay
    And <Contact> verifies that call status to Myself is changed to destroyed in <Timeout> seconds
    And <Contact2> verifies that call status to Myself is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | Contact2  | VideoCallBackend | Timeout |
      | user1Name | user2Name | user3Name | chrome           | 60      |

  @C12106 @calling_basic @video_calling @fastLogin
  Scenario Outline: Verify muting ongoing Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to <Name>
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And I accept alert
    And <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I remember state of Mute button on Video Calling overlay
    And I tap Mute button on Video Calling overlay
    Then I see state of Mute button has changed on Video Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 30      |

  @C28861 @calling_basic @video_calling @rc @fastLogin
  Scenario Outline: Verify video call continues after rejecting 2nd incoming video call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given <Contact>,<Contact2> starts instance using <VideoCallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to me
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And I accept alert
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    Then I see Switch Camera button on Video Calling overlay
    When <Contact2> starts a video call to me
    And I see call status message contains "<Contact2> calling"
    And I tap Ignore button on the Calling overlay
    # Then I see Mute button on Video Calling overlay
    Then <Contact> verifies that call status to me is changed to active in <TimeoutAlreadyInCall> seconds
    And <Contact2> verifies that call status to me is changed to connecting in <Timeout> seconds

    Examples:
      | Name      | Contact   | Contact2  | VideoCallBackend | Timeout | TimeoutAlreadyInCall |
      | user1Name | user2Name | user3Name | chrome           | 60      | 4                    |

  @C48232 @calling_basic @video_calling @rc @fastLogin
  Scenario Outline: Verify starting two video calls in a row
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Call button
    And I accept alert
    Then I see call status message contains "<Contact> ringing"
    When I tap Leave button on Video Calling overlay
    # Wait for animation
    And I wait for 2 seconds
    And I see missed call from contact YOU
    And I tap Video Call button
    Then I see call status message contains "<Contact> ringing"

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C48235 @calling_basic @video_calling @rc @fastLogin
  Scenario Outline: Verify making audio call after cancelled video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Call button
    And I accept alert
    Then I see call status message contains "<Contact> ringing"
    When I tap Leave button on Video Calling overlay
    And I tap Audio Call button
    Then I see Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |