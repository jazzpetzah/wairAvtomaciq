Feature: Delivery Receipts

  @C228501 @regression @fastLogin
  Scenario Outline: Verify Delivered status isn't shown if receiver is on the old build
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
