Feature: ContactsUI

  @staging @id4130
  Scenario Outline: Verify blocked users are not displayed in the Contacts UI
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    And I see Contact list with my name <Name>
    When I dont see conversation <Contact> in contact list
    And I open search by taping on it
    And I press the send an invite button
    And I see ContactsUI page
    And I input user name <Contact> in search on ContactsUI
    Then I DONT see contact <Contact> in ContactsUI page list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |
