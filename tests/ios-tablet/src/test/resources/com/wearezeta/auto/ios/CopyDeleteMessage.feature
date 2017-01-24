Feature: Copy/Delete Message

  @C119753 @regression @fastLogin
  Scenario Outline: Verify copy/delete menu disappears on the rotation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends 1 default message to conversation Myself
    When I tap on contact name <Contact>
    And I long tap default message in conversation view
    Then I see Copy badge item
    When I rotate UI to portrait
    Then I do not see Copy badge item

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C145958 @rc @regression @fastLogin
  Scenario Outline: Delete Message. Verify deleting a picture [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given User <Contact> sends <MessagesCount> default messages to conversation Myself
    When I tap on contact name <Contact>
    Then I see 1 photo in the conversation view
    And I see <MessagesCount> default messages in the conversation view
    When I long tap on image in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I see 0 photos in the conversation view
    And I see <MessagesCount> default messages in the conversation view

    Examples:
      | Name      | Contact   | Picture     | MessagesCount |
      | user1Name | user2Name | testing.jpg | 2             |

  @C145957 @regression @rc @fastLogin
  Scenario Outline: Verify deleting sent text message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I see 0 default messages in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |