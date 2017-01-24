Feature: Forward Message

  @C162655 @regression @rc
  Scenario Outline: Text message forwarding into other conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given I see the conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap the Text message "<Message>" in the conversation view
    And I tap Forward button on the message bottom menu
    Then I see the Wire app is not in foreground

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Wassap  |
