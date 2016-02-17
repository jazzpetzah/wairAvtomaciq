Feature: Video Calling

  @C12102 @staging
  Scenario Outline: Verify initiating Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    Then I see ringing user <Contact> label on Video Call page
    And I see CallMute button and it is disabled on Video Call page
    And I see LeaveCall button and it is enabled on Video Call page
    And I see Accept button and it is disabled on Video Call page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C12105 @staging
  Scenario Outline: Verify cancelling Video call
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    Then I see ringing user <Contact> label on Video Call page
    When I click Hang Up button on Video Call page
    Then I see missed call from contact YOU

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |