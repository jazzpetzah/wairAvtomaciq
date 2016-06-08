Feature: Sign Out

  @C692 @id329 @regression @rc @rc42
  Scenario Outline: Sign out from Wire
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I tap options button
    And I tap settings button
    And I select "Account" settings menu item
    And I select "Log out" settings menu item
    And I confirm sign out
    Then I see welcome screen
    When I sign in using my email or phone number
    And I do not see First Time overlay
    Then I see Contact list with no contacts

    Examples:
      | Name      |
      | user1Name |