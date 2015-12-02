Feature: Invitations

  @id4177 @staging
  Scenario Outline: Verify impossibility of sending invite to the person with a wrong email or phone (landscape)
    Given I delete all contacts from Address Book
    Given There is 1 user where <Name> is me
    Given I add <Contact> into Address Book excluding phone,email
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with no conversations
    When I tap Invite button at the bottom of conversations list
    Then I do not see <Contact> in the invites list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
