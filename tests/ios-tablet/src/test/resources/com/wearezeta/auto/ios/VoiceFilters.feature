Feature: Voice Filters

  @C169215 @staging
  Scenario Outline: Verify you can record an audio message and apply voice filter to it [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C169216 @staging
  Scenario Outline: Verify sending original audio without any filters [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 3 seconds
    And I tap Stop Recording button on Voice Filters overlay
    And I tap Confirm button on Voice Filters overlay
    Then I see audio message placeholder
    And I do not see Confirm button on Voice Filters overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |