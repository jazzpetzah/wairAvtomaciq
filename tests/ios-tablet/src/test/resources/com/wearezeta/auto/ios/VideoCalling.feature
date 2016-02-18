Feature: Video Calling

  @C28847 @calling_basic
  Scenario Outline: Verify initiating Video call [PORTRAIT]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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

  @C28848 @calling_basic
  Scenario Outline: Verify initiating Video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C28849 @calling_basic
  Scenario Outline: Verify cancelling Video call [PORTRAIT]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
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

  @C28850 @calling_basic
  Scenario Outline: Verify cancelling Video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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