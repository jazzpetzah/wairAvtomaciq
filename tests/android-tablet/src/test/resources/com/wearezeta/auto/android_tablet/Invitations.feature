Feature: Invitations

  @C574 @regression
  Scenario Outline: Verify impossibility of sending invite to the person with a wrong email or phone (landscape)
    Given I delete all contacts from Address Book
    Given There is 1 user where <Name> is me
    Given I add <Contact> into Address Book excluding phone,email
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with no conversations
    When I open Search UI
    Then I do not see "<Contact>" avatar in Contact list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |