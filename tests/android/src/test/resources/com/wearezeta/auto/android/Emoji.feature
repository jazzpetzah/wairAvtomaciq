Feature: Emoji

  @C162664 @staging
  Scenario Outline: Verify that received message containing emoji characters only is 3 times bigger, than normal text messages
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <NormalText> to user Myself
    Given User <Contact> sends encrypted message <EmojiText> to user Myself
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    Then I see that the message "<EmojiText>" is at least 2 times higher than "<NormalText>" in the conversation view

    Examples:
      | Name      | Contact   | NormalText | EmojiText |
      | user1Name | user2Name | Yo         | 👿        |

  @C162665 @staging
  Scenario Outline: Verify the height of received message stays unchanged if the string contains both emoji and normal characters
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <NormalText> to user Myself
    Given User <Contact> sends encrypted message <MixedText> to user Myself
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    Then I see that messages "<MixedText>" and "<NormalText>" have equal height in the conversation view

    Examples:
      | Name      | Contact   | NormalText | MixedText |
      | user1Name | user2Name | Yo         | 👿?       |
