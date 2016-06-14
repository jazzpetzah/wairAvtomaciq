Feature: Settings

  @C669 @id67 @regression @rc
  Scenario Outline: Open and Close settings page
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    Then I see settings page
    When I press back button
    Then I see Contact list with no contacts

    Examples:
      | Name      |
      | user1Name |

  @C670 @id92 @regression @rc
  Scenario Outline: Check About page in settings menu
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I select "About" settings menu item
    Then I see "Wire Website" settings menu item

    Examples:
      | Name      |
      | user1Name |

