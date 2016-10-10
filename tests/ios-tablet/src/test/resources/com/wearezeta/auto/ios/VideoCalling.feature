Feature: Video Calling

  @C28850 @rc @calling_basic @fastLogin
  Scenario Outline: Verify cancelling Video call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Call button
    And I accept alert
    Then I see call status message contains "<Contact> ringing"
    When I tap Leave button on Calling overlay
    And I do not see Calling overlay
    Then I see missed call from contact YOU

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C28852 @calling_basic @fastLogin
  Scenario Outline: Verify accepting video call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When <Contact> starts a video call to me
    And I see call status message contains "<Contact> calling"
    And I tap Accept Video button on Calling overlay
    And I accept alert
    Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C28855 @rc @calling_basic @fastLogin
  Scenario Outline: Verify ignoring Video call [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When <Contact> starts a video call to me
    And I see call status message contains "<Contact> calling"
    And I tap Ignore button on the Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |