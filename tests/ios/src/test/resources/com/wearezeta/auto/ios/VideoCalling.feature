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
    And I tap Accept button on Calling overlay
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    Then I see Mute button on Calling overlay
    And I see Leave button on Calling overlay
    And I see Call Video button on Calling overlay
    And I tap Leave button on Calling overlay

    Examples:
      | Name      | Contact   | CallBackend         | Timeout |
      | user1Name | user2Name | chrome:48.0.2564.97 | 60      |