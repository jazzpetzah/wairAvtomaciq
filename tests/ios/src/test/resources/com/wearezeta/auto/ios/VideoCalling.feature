Feature: Video Calling

  @C12102 @calling_basic
  Scenario Outline: Verify initiating Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    Then I see call status message contains "<Contact> RINGING"
    And I see Leave button on Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C12105 @calling_basic
  Scenario Outline: Verify cancelling Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    Then I see call status message contains "<Contact> RINGING"
    When I tap Leave button on Calling overlay
    Then I see missed call from contact YOU

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C12101 @staging
  Scenario Outline: Verify accepting video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to <Name> using <CallBackend>
    And I see call status message contains "<Contact> CALLING"
    And I tap Accept Video button on Calling overlay
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    Then I see Mute button on Calling overlay
    And I see Leave button on Calling overlay
    And I see Call Video button on Calling overlay
    And I tap Leave button on Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C12103 @staging
  Scenario Outline: Verify finishing video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    And I see Calling overlay
    And I see Mute button on Calling overlay
    And I see Leave button on Calling overlay
    And I see Call Video button on Calling overlay
    And I tap Leave button on Calling overlay
    Then I see dialog page

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C12104 @staging
  Scenario Outline: Verify ignoring Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to me using <CallBackend>
    And I see call status message contains "<Contact> CALLING"
    And I tap Ignore button on the Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C12107 @staging
  Scenario Outline: Verify getting missed call indication when someone called
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    And I remember the state of <Contact> conversation item
    When <Contact> starts a video call to me using <CallBackend>
    Then I see call status message contains "<Contact> CALLING"
    And <Contact> stops all calls to me
    And I do not see Calling overlay
    Then I see the state of <Contact> conversation item is changed
    And I tap on contact name <Contact>
    And I see missed call from contact <Contact>

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |

  @C12114 @staging
  Scenario Outline: Verify I can switch to another incoming audio call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    And <Contact1> starts a video call to me using <VideoCallBackend>
    And I see call status message contains "<Contact1> CALLING"
    And I tap Accept Video button on Calling overlay
    When <Contact2> calls me using <AudioCallBackend>
    And I see call status message contains "<Contact2> CALLING"
    Then I tap Accept button on Calling overlay
    And I do not see Accept Video button on Calling overlay
    And I see Leave button on Calling overlay
    And <Contact2> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact1> verifies that call status to me is changed to destroyed in <Timeout> seconds

    Examples:
      | Name      | Contact1  | Contact2  | VideoCallBackend | AudioCallBackend | Timeout |
      | user1Name | user2Name | user3Name | chrome           | autocall         | 60      |