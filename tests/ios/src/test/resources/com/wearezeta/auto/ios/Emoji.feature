Feature: Emoji

  @C164766 @staging
  Scenario Outline: Verify preview is opened after tapping on GIF button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted message "<NormalText>" to user Myself
    Given User <Contact> sends encrypted message "<EmojiText>" to user Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see that the difference in height of "<EmojiText>" and "<NormalText>" messages is greater than <MinHeightDiff> percent

    Examples:
      | Name      | Contact   | NormalText | EmojiText | MinHeightDiff |
      | user1Name | user2Name | Yo         | 👿        | 120           |
