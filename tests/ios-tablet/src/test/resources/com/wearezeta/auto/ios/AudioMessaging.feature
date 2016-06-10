Feature: Audio Messaging

  @C145953 @staging
  Scenario Outline: Verify recording and sending an audio message [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button for <Duration> seconds from input tools
    And I tap Send record control button
    Then I see audio message placeholder
    When I tap Play audio message button
    Then I see state of button on audio message placeholder is pause
    And I see the audio message in placeholder gets played

    Examples:
      | Name      | Contact   | Duration |
      | user1Name | user2Name | 50       |
