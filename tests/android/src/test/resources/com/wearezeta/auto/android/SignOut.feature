Feature: Sign Out

  @C692 @regression @rc @rc42
  Scenario Outline: Sign out from Wire
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Log out" settings menu item
    And I confirm sign out
    Then I see welcome screen
    When I sign in using my email
    # FIXME: Email credentials should not be asked after Phone number login - should be fixed by SE
    # When I sign in using my email or phone number
    And I do not see First Time overlay
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |