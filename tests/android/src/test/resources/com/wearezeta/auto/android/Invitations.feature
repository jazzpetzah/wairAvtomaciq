Feature: Invitations

  @id4161 @staging
  Scenario Outline: Invitations (Conversations List): I can send an email notification from conversations list
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I delete all contacts from Address Book
    And I add <Contact> into Address Book
    And I tap Invite button at the bottom of conversations list
    And I see <Contact> in the invites list
    And I remember the state of <Contact> avatar in the invites list
    And I tap Invite button next to <Contact>
    And I select <ContactEmail> email on invitation sending alert
    And I confirm invitation sending alert
    Then I verify the state of <Contact> avatar in the invites list is changed
    And I verify user <Contact> has received an email invitation

    Examples:
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |


