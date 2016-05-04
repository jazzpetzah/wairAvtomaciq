Feature: Delete Message

  @C111638 @staging
  Scenario Outline: Verify deleting own text message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see the message "<Message>" in the conversation view
    When I long tap the message "<Message>" in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |
