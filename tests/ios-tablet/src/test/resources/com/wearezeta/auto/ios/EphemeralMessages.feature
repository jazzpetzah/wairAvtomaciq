Feature: Ephemeral Messages

  @C318642 @staging @fastLogin
  Scenario Outline: Verify the message is deleted on the sender side when it's read on the receiver side
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    Given I click at 0.5,0.5 of Simulator window
    Given I type the default message and send it
    Given I see 1 default message in the conversation view
    When User <Contact> reads the recent message from user Myself
    And I wait for <Timeout> seconds
    Then I see 1 conversation entry

    Examples:
      | Name      | Contact   | Timeout | DeviceName    |
      | user1Name | user2Name | 5       | ContactDevice |