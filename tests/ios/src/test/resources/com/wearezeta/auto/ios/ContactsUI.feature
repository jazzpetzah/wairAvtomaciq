Feature: ContactsUI

  @C81 @regression @id4130
  Scenario Outline: Verify blocked users are not displayed in the Contacts UI
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I dont see conversation <Contact> in contact list
    And I open search by taping on it
    And I press the send an invite button
    And I see ContactsUI page
    And I input user name <Contact> in search on ContactsUI
    Then I DONT see contact <Contact> in ContactsUI page list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C78 @regression @id4121
  Scenario Outline: Verify opening existing conversation from Contacts UI
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    And I open search by taping on it
    And I press the send an invite button
    And I see contact <Contact> in ContactsUI page list
    And I click on Open button next to user name <Contact> on ContactsUI
    Then I see dialog page with contact <Contact>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |