Feature: Test

  Scenario Outline: Receive Message from contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And All contacts send me a message <Message>

    Examples:
      | Name      | Message |
      | user1Name | Yo      |
