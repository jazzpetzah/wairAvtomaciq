Feature: Log Out

  @C745 @regression @rc
  Scenario Outline: Sign out from Wire in portrait mode
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    And I tap conversations list settings button
    And I select "Account" menu item on Settings page
    And I select "Log out" menu item on Settings page
    And I confirm logout on Settings page
    Then I see welcome screen

    Examples:
      | Name      |
      | user1Name |

  @C735 @regression @rc @rc44
  Scenario Outline: Sign out from Wire in landscape mode
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    And I tap conversations list settings button
    And I select "Account" menu item on Settings page
    And I select "Log out" menu item on Settings page
    And I confirm logout on Settings page
    Then I see welcome screen

    Examples:
      | Name      |
      | user1Name |