Feature: Log Out

  @id2266 @regression @rc
  Scenario Outline: Sign out from Wire in portrait mode
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with no conversations
    And I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    And I tap Options button on Self Profile page
    And I select "SETTINGS" menu item on Self Profile page
    And I select "Account" menu item on Settings page
    And I select "Log out" menu item on Settings page
    And I confirm logout on Settings page
    Then I see welcome screen

    Examples:
      | Name      |
      | user1Name |

  @id2251 @regression @rc @rc44
  Scenario Outline: Sign out from Wire in landscape mode
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with no conversations
    And I see my name on Self Profile page
    And I tap Options button on Self Profile page
    And I select "SETTINGS" menu item on Self Profile page
    And I select "Account" menu item on Settings page
    And I select "Log out" menu item on Settings page
    And I confirm logout on Settings page
    Then I see welcome screen

    Examples:
      | Name      |
      | user1Name |
