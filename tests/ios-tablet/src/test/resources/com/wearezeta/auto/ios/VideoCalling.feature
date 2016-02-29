Feature: Video Calling

  @C28850 @rc @calling_basic
  Scenario Outline: Verify cancelling Video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    Then I see call status message contains "<Contact> RINGING"
    When I tap Leave button on Calling overlay
    And I do not see Calling overlay
    Then I see missed call from contact YOU

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C28852 @calling_basic
  Scenario Outline: Verify accepting video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When <Contact> starts a video call to <Name> using <CallBackend>
    And I see call status message contains "<Contact> CALLING"
    And I tap Accept Video button on Calling overlay
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    And I tap Leave button on Calling overlay
    And I do not see Calling overlay
    Then I see dialog page

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @28856 @calling_basic
  Scenario Outline: Verify finishing video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    And I see Calling overlay
    And I tap Leave button on Calling overlay
    And I do not see Calling overlay
    Then I see dialog page

    Examples:
      | Name      | Contact   | CallBackend  |
      | user1Name | user2Name | chrome		 |

  @C28855 @calling_basic
  Scenario Outline: Verify ignoring Video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When <Contact> starts a video call to me using <CallBackend>
    And I see call status message contains "<Contact> CALLING"
    And I tap Ignore button on the Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | chrome      |