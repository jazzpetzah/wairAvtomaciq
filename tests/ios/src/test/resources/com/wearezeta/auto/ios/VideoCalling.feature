Feature: Video Calling

  @C12102 @calling_basic @video_calling
  Scenario Outline: Verify initiating Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Call button
    Then I see call status message contains "<Contact> ringing"
    And I see Leave button on Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C12105 @calling_basic @video_calling
  Scenario Outline: Verify cancelling Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Call button
    Then I see call status message contains "<Contact> ringing"
    When I tap Leave button on Calling overlay
    Then I see missed call from contact YOU

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C12101 @calling_basic @video_calling @AUDIO-868 @rc
  Scenario Outline: Verify accepting video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to <Name>
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    Then I see Mute button on Video Calling overlay
    And I see Switch Camera button on Video Calling overlay
    And I see Leave button on Video Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C12103 @calling_basic @video_calling @AUDIO-868
  Scenario Outline: Verify finishing video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Call button
    And I see Video Calling overlay
    And <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I see Mute button on Video Calling overlay
    And I tap Leave button on Video Calling overlay
    Then I see conversation view page

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 30      |

  @C12104 @calling_basic @video_calling
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

  @C12107 @calling_basic @video_calling
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

  @C12114 @calling_basic @video_calling
  Scenario Outline: (AUDIO-830) Verify I can switch to another incoming audio call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> starts instance using <VideoCallBackend>
    Given <Contact2> starts instance using <AudioCallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    And <Contact1> starts a video call to me
    And I see call status message contains "<Contact1> calling"
    And I tap Accept Video button on Calling overlay
    When <Contact2> calls me
    And I see call status message contains "<Contact2> calling"
    Then I tap Accept button on Calling overlay
    And I do not see Accept Video button on Calling overlay
    And I see Leave button on Calling overlay
    And <Contact2> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact1> verifies that call status to me is changed to destroyed in <Timeout> seconds

    Examples:
      | Name      | Contact1  | Contact2  | VideoCallBackend | AudioCallBackend | Timeout |
      | user1Name | user2Name | user3Name | chrome           | autocall         | 60      |

  @C12110 @calling_basic @video_calling
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

  @C28851 @calling_basic @video_calling
  Scenario Outline: Verify starting video call with action button in Search
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I open search UI
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I tap Video call action button on People picker page
    Then I see call status message contains "<Contact> ringing"
    And I see Leave button on Video Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C12115 @calling_basic @video_calling @AUDIO-830
  Scenario Outline: Verify I can switch to another video call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given <Contact>,<Contact2> start instance using <VideoCallBackend>
    Given I sign in using my email or phone number
    And <Contact>,<Contact2> start a video call to me
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    Then I see Switch Camera button on Video Calling overlay
    When <Contact2> starts a video call to me
    And I tap Accept Video button on Calling overlay
    Then I see Mute button on Video Calling overlay
    And <Contact> verifies that call status to Myself is changed to destroyed in <Timeout> seconds
    And <Contact2> verifies that call status to Myself is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact    | Contact2 | VideoCallBackend | Timeout |
      | user1Name | user2Name  | user3Name| chrome           | 60      |

  @C12106 @calling_basic @video_calling @AUDIO-868
  Scenario Outline: Verify muting ongoing Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to <Name>
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I see Mute button on Video Calling overlay
    And I remember state of Mute button on Video Calling overlay
    And I tap Mute button on Video Calling overlay
    Then I see state of Mute button has changed on Video Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 30      |

  @C28861 @calling_basic @video_calling @AUDIO-868 @rc
  Scenario Outline: Verify video call continues after rejecting 2nd incoming video call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given <Contact>,<Contact2> starts instance using <VideoCallBackend>
    Given I sign in using my email or phone number
    And <Contact> starts a video call to me
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    Then I see Switch Camera button on Video Calling overlay
    When <Contact2> starts a video call to me
    And I see call status message contains "<Contact2> calling"
    And I tap Ignore button on the Calling overlay
    Then I see Mute button on Video Calling overlay
    And <Contact> verifies that call status to me is changed to active in <TimeoutAlreadyInCall> seconds
    And <Contact2> verifies that call status to me is changed to connecting in <Timeout> seconds

    Examples:
      | Name      | Contact    | Contact2 | VideoCallBackend | Timeout | TimeoutAlreadyInCall |
      | user1Name | user2Name  | user3Name| chrome           | 60      | 4                    |

  @C12108 @real
  Scenario Outline: Verify disabling video in Video call and enabling it back
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to <Name> 
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And <Contact> verifies that call status to Myself is changed to active in <Timeout> seconds
    And I see Switch Camera button on Video Calling overlay
    And I remember state of Video button on Video Calling overlay
    And I tap Call Video button on Video Calling overlay
    Then I do not see Switch Camera button on Video Calling overlay
    And I see state of Video button has changed on Video Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |