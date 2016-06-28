Feature: Audio Message

  @C162660 @staging
  Scenario Outline: Verify sending voice message by long tap > release the thumb > tap on icon
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact>
    When I long tap Audio message button from cursor toolbar for <TapDuration> seconds
    And I tap audio recording Send button
    Then I see cursor toolbar
    And I see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C162661 @staging
  Scenario Outline: Verify cancelling sending voice message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact>
    When I long tap Audio message button from cursor toolbar for <TapDuration> seconds
    And I tap audio recording Cancel button
    Then I see cursor toolbar
    And I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |