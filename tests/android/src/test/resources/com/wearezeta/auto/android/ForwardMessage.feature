Feature: Forward Message

  @C150008 @staging
  Scenario Outline: Check text message forwarding into other conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap the Text message "<Message>" in the conversation view
    And I tap Forward button on the action mode bar
    Then I see the Wire app is not in foreground

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Wassap  |
