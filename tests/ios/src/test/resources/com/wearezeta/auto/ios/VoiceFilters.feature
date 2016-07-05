Feature: Voice Filters

  @C165138 @staging
  Scenario Outline: Verify you can record an audio message and apply voice filter to it
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 3 seconds
    And I tap Stop Recording button on Voice Filters overlay
    And I tap <ButtonsCount> random effect buttons on Voice Filters overlay
    And I tap Confirm button on Voice Filters overlay
    Then I see audio message placeholder
    And I do not see Confirm button on Voice Filters overlay

    Examples:
      | Name      | Contact   | ButtonsCount |
      | user1Name | user2Name | 4            |

  @C165139 @staging
  Scenario Outline: Verify you can cancel filtered voice message sending
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 2 seconds
    And I tap Stop Recording button on Voice Filters overlay
    And I tap <ButtonsCount> random effect buttons on Voice Filters overlay
    And I tap Cancel button on Voice Filters overlay
    Then I do not see audio message placeholder
    And I do not see Confirm button on Voice Filters overlay
    And I do not see Start Recording button on Voice Filters overlay

    Examples:
      | Name      | Contact   | ButtonsCount |
      | user1Name | user2Name | 2            |

  @C165140 @staging
  Scenario Outline: Verify you can retry recording of filtered voice message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 2 seconds
    And I tap Stop Recording button on Voice Filters overlay
    And I tap <ButtonsCount> random effect buttons on Voice Filters overlay
    And I tap Redo button on Voice Filters overlay
    Then I see Start Recording button on Voice Filters overlay

    Examples:
      | Name      | Contact   | ButtonsCount |
      | user1Name | user2Name | 1            |


