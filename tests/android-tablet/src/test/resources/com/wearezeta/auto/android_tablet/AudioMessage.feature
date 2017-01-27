Feature: Audio Message

  @C162660 @regression @rc
  Scenario Outline: Verify sending voice message by long tap > release the thumb > tap on icon
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap audio recording Send button
    Then I see cursor toolbar
    And I see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C162661 @regression @rc
  Scenario Outline: Verify cancelling sending voice message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap audio recording Cancel button
    Then I see cursor toolbar
    And I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C162659 @regression @rc
  Scenario Outline: Verify sending voice message by long tap > swipe up
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message cursor button <TapDuration> seconds and swipe up
    Then I see cursor toolbar
    And I see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |
