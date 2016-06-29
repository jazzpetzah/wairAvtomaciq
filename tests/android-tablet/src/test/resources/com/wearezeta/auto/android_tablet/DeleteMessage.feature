Feature: Delete Message

  @C164770 @staging
  Scenario Outline: Verify deleting received text message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact>
    When I long tap the message "<Message>" in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message           |
      | user1Name | user2Name | DeleteTextMessage |
