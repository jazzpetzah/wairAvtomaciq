Feature: Copy/Delete Message

  @C119753 @regression
  Scenario Outline: Verify copy/delete menu disappears on the rotation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact>
    And I long tap default message in conversation view
    Then I see Copy badge item
    When I rotate UI to portrait
    Then I do not see Copy badge item

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C145958 @staging
  Scenario Outline: Delete Message. Verify deleting a picture [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given User <Contact> sends <MessagesCount> encrypted message to user Myself
    When I tap on contact name <Contact>
    Then I see 1 photo in the conversation view
    And I see <MessagesCount> default messages in the conversation view
    When I long tap on image in conversation view
    And I tap on Delete badge item
    # FIXME: Sometimes autoaccept fails
    And I accept alert
    Then I see 0 photos in the conversation view
    And I see <MessagesCount> default messages in the conversation view

    Examples:
      | Name      | Contact   | Picture     | MessagesCount |
      | user1Name | user2Name | testing.jpg | 2             |
