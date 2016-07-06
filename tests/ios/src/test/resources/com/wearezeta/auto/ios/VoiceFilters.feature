Feature: Voice Filters

  @C165138 @regression
  Scenario Outline: Verify you can record an audio message and apply voice filter to it
    Given There are 2 users where <Name> is me
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

  @C165139 @regression
  Scenario Outline: Verify you can cancel filtered voice message sending
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
    And I tap Cancel button on Voice Filters overlay
    Then I do not see audio message placeholder
    And I do not see Confirm button on Voice Filters overlay
    And I do not see Start Recording button on Voice Filters overlay

    Examples:
      | Name      | Contact   | ButtonsCount |
      | user1Name | user2Name | 2            |

  @C165141 @regression
  Scenario Outline: Verify voice filter control disappears if you switch current conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 2 seconds
    And I tap Stop Recording button on Voice Filters overlay
    And I navigate back to conversations list
    And I tap on contact name <Contact2>
    And I navigate back to conversations list
    And I tap on contact name <Contact1>
    Then I do not see audio message placeholder
    And I do not see Confirm button on Voice Filters overlay
    And I do not see Start Recording button on Voice Filters overlay

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C165140 @regression
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