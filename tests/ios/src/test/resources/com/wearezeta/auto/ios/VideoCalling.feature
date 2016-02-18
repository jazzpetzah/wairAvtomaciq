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
    Then I see user <Contact> ringing label on Video Call page
    And I see CallMute button and it is disabled on Video Call page
    And I see LeaveCall button and it is enabled on Video Call page
    And I see Accept button and it is disabled on Video Call page

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
    Then I see user <Contact> ringing label on Video Call page
    When I click Hang Up button on Video Call page
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
    #And I tap on contact name <Contact>
    #And <Contact> starts a video call to <Name> using <CallBackend>
    And I see user <Contact> calling label on Video Call page
    And I click Accept video call button
    #Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    #And <Contact> verify to have 1 flows
    #And <Contact> verify that all flows have greater than 0 bytes
    And I see CallMute button and it is enabled on Video Call page
    And I see LeaveCall button and it is enabled on Video Call page
    And I see Accept button and it is disabled on Video Call page
    And I click Hang Up button on Video Call page

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | firefox     | 60      |