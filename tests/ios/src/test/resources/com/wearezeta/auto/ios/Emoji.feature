Feature: Emoji

  @C164766 @regression @fastLogin
  Scenario Outline: Verify emoji is displayed bigger if it is sent alone
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 "<NormalText>" message to conversation Myself
    Given User <Contact> sends 1 "<EmojiText>" message to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see that the height of "<EmojiText>" and "<NormalText>" messages is different

    Examples:
      | Name      | Contact   | NormalText | EmojiText |
      | user1Name | user2Name | Yo👿      | 👿        |

  @C250854 @fastLogin @regression
  Scenario Outline: Verify opening emoji keyboard on tapping on smiley face
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Emoji Keyboard button in conversation view
    And I tap "<EmojiChar>" key on Emoji Keyboard
    # Search is faster if Emoji keyboard is not visible
    And I tap Text Keyboard button in conversation view
    And I tap Send Message button in conversation view
    Then I see last message in the conversation view is expected message <EmojiChar>

    Examples:
      | Name      | Contact   | EmojiChar |
      | user1Name | user2Name | 😀        |
