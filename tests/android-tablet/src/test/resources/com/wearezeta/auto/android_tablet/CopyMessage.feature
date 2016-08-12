Feature: Copy Message

  @C162656 @regression @rc
  Scenario Outline: Verify long tap on the message shows menu "Copy, Delete"
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact>
    When I long tap the message "<Message>" in the conversation view
    Then I see Copy button on the message bottom menu
    And I see Delete only for me button on the message bottom menu

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Wassap  |
