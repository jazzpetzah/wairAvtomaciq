Feature: ContactsUI

  @C2498 @regression @id4131
  Scenario Outline: Verify blocked users are not displayed in the Contacts UI [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User <Name> blocks user <Contact>
    Given I Sign in on tablet using my email
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

  @C2499 @regression @id4132
  Scenario Outline: Verify blocked users are not displayed in the Contacts UI [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User <Name> blocks user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
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

  @C2492 @regression @id4122
  Scenario Outline: Verify opening existing conversation from Contacts UI [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I open search by taping on it
    And I press the send an invite button
    And I see contact <Contact> in ContactsUI page list
    And I click on Open button next to user name <Contact> on ContactsUI
    Then I see dialog page with contact <Contact>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2493 @regression @id4123
  Scenario Outline: Verify opening existing conversation from Contacts UI [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I open search by taping on it
    And I press the send an invite button
    And I see contact <Contact> in ContactsUI page list
    And I click on Open button next to user name <Contact> on ContactsUI
    Then I see dialog page with contact <Contact>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |