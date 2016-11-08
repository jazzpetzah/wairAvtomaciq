Feature: Sign Out

  @C692 @regression @rc @legacy
  Scenario Outline: (AN-4605) Sign out from Wire
    Given There is 1 user where <Name> is me
#    Given I sign in using my email or phone number
    Given I sign in using my phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Log out" settings menu item
    And I confirm sign out
    Then I see welcome screen
    #email login after phone login to reproduce AN-4605
    When I sign in using my email
    And I do not see First Time overlay
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |
