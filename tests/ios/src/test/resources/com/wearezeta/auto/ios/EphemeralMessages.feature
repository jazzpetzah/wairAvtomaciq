Feature: Ephemeral Messages

  @torun @C259584 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral message (5s/15s/1m)
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to 15 seconds
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    And I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    And I wait for 10 seconds

    Examples:
      | Name      | Contact   | EphemeralTimeLabel |
      | user1Name | user2Name | seconds            |

  @C259586 @staging @fastLogin
  Scenario Outline: Verify switching on/off ephemeral message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to 15 seconds
    Then I see Ephemeral text input field
    And I see Time Indicator button in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |