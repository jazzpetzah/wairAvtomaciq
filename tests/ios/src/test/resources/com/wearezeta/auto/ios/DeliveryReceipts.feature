Feature: Delivery Receipts

  @C226452 @staging @fastLogin
  Scenario Outline: Verify status is changed to Sent with a timestamp when message reached the server
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    And I see "<SentLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact   | SentLabel |
      | user1Name | user2Name | Sent      |
