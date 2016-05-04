Feature: Delete Message

  @C111638 @staging
  Scenario Outline: Verify deleting own text message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message1>" and send it
    And I type the message "<Message2>" and send it
    When I long tap the message "<Message1>" in the conversation view
    And I tap the message "<Message2>" in the conversation view
    And I tap Delete button on the action mode bar
    And I see alert message containing "<AlertText>" in the title
    And I tap Delete button on the alert
    Then I do not see the message "<Message1>" in the conversation view
    And I do not see the message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1 | Message2 | AlertText  |
      | user1Name | user2Name | Yo1      | Yo2      | 2 messages |
