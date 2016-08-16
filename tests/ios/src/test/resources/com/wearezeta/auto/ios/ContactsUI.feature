Feature: ContactsUI

  @C81 @regression @fastLogin
  Scenario Outline: Verify blocked users are not displayed in the Contacts UI
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User <Name> blocks user <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I do not see conversation <Contact> in conversations list
    And I open search UI
    And I tap Send Invite button
    And I see ContactsUI page
    And I input user name <Contact> in search on ContactsUI
    Then I DONT see contact <Contact> in ContactsUI page list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C78 @regression @fastLogin
  Scenario Outline: Verify opening existing conversation from Contacts UI
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I sign in using my email or phone number
    Given I see conversations list
    And I open search UI
    And I tap Send Invite button
    And I see contact <Contact> in ContactsUI page list
    When I click on Open button next to user name <Contact> on ContactsUI
    Then I see the conversation with <Contact>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |