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