Feature: Emoji

  @C162664 @regression @rc
  Scenario Outline: Verify that received message containing emoji characters only is 3 times bigger, than normal text messages
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <NormalText> to user Myself
    Given User <Contact> sends encrypted message <EmojiText> to user Myself
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    Then I see that the difference in height of "<EmojiText>" and "<NormalText>" messages is greater than <MinHeightDiff> percent

    Examples:
      | Name      | Contact   | NormalText | EmojiText | MinHeightDiff |
      | user1Name | user2Name | Yo         | 👿        | 120           |

  @C162665 @regression @rc
  Scenario Outline: Verify the height of received message stays unchanged if the string contains both emoji and normal characters
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <NormalText> to user Myself
    Given User <Contact> sends encrypted message <MixedText> to user Myself
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    Then I see that the difference in height of "<MixedText>" and "<NormalText>" messages is not greater than <MaxHeightDiff> percent

    Examples:
      | Name      | Contact   | NormalText | MixedText | MaxHeightDiff |
      | user1Name | user2Name | Yo         | 👿?       | 20            |

  @C250833 @C250834 @staging
  Scenario Outline: Verify When I switch to emoji keyboard and select one, cursor send button appears even it is disabled in settings
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap conversations list settings button
    # Disable at first
    And I select "Options" settings menu item
    And I select "Send button" settings menu item
    And I press Back button 2 times
    And I see Conversations list
    And I tap on conversation name <Contact>
    And I tap Switch to emoji button from cursor toolbar
    # C250834
    Then I see Emoji keyboard
    When I tap on 1st emoji at Emoji Keyboard
    # C250833
    Then I see Send button in cursor input
    When I tap Switch to text button from cursor toolbar
    # C250834
    Then I do not see Emoji keyboard
    And I see Android keyboard

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |