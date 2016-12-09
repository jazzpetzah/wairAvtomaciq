Feature: ContactsUI

  @C2499 @regression @fastLogin
  Scenario Outline: Verify blocked users are not displayed in the Contacts UI [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User <Name> blocks user <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I do not see conversation <Contact> in conversations list
    And I open search UI
    And I accept alert if visible
    And I tap Send Invite button on Search UI page
    And I see ContactsUI page
    And I input user name <Contact> in search on ContactsUI
    Then I do not see contact <Contact> in ContactsUI page list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2493 @regression @fastLogin
  Scenario Outline: Verify opening existing conversation from Contacts UI [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search UI
    And I accept alert if visible
    And I tap Send Invite button on Search UI page
    And I tap Open button next to user name <Contact> on ContactsUI
    # Wait for animation
    And I wait for 2 seconds
    Then I see the conversation with <Contact>

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |