Feature: Copy Message

  @C119435 @staging
  Scenario Outline: Verify could copy text message by copy button in top toolbar
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message∂>" and send it
    When I long tap the message "<Message>" in the conversation view
    And I tap Copy button on the action mode bar
    Then I verify that Android clipboard content equals to "<Message>"

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |
