Feature: Emoji

  @C162662 @staging @torun
  Scenario Outline: Verify that your message containing emoji characters only is 3 times bigger, than normal text messages
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<NormalText>" and send it
    And I type the message "<EmojiText>" and send it
    Then I see that the message "<EmojiText>" is higher than "<NormalText>"

    Examples:
      | Name      | Contact   | NormalText | EmojiText |
      | user1Name | user2Name | Yo         | 👿        |