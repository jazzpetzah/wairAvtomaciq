Feature: Test

  Scenario Outline: Receive Message from contact
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    When I see Contact list with contacts
    Then All contacts send me a message <Message>
    And Contact <Contact> sends image <GifName> to single user conversation <Name>

    Examples:
      | Name      | Contact   | Message | GifName      |
      | user1Name | user2Name | Yo      | animated.gif |
