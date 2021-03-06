Feature: Ephemeral Messages

  @C318642 @regression @fastLogin
  Scenario Outline: Verify the message is deleted on the sender side when it's read on the receiver side
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<DeviceName>"}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    # This is to close expiration timer popup
    Given I tap at 50%,50% of the viewport size
    Given I type the default message and send it
    Given I see 1 default message in the conversation view
    When User <Contact> reads the recent message from user Myself
    And I wait for <Timeout> seconds
    Then I see 1 conversation entry

    Examples:
      | Name      | Contact   | Timeout | DeviceName    |
      | user1Name | user2Name | 5       | ContactDevice |