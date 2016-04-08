Feature: ContactsUI

  @C2499 @regression @id4132
  Scenario Outline: Verify blocked users are not displayed in the Contacts UI [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User <Name> blocks user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I do not see conversation <Contact> in conversations list
    And I open search UI
    And I press the send an invite button
    And I see ContactsUI page
    And I input user name <Contact> in search on ContactsUI
    Then I DONT see contact <Contact> in ContactsUI page list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2493 @regression @id4123
  Scenario Outline: Verify opening existing conversation from Contacts UI [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I open search UI
    And I press the send an invite button
    And I see contact <Contact> in ContactsUI page list
    And I click on Open button next to user name <Contact> on ContactsUI
    Then I see dialog page with contact <Contact>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |