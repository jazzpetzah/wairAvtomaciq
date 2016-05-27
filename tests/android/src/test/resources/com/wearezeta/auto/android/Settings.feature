Feature: Settings

  @C669 @id67 @regression @rc
  Scenario Outline: Open and Close settings page
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I tap options button
    And I tap settings button
    Then I see settings page
    When I press back button
    Then I see personal info page

    Examples:
      | Name      |
      | user1Name |

  @C376 @id71 @regression
  Scenario Outline: Can not open Settings page when editing user name
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    And I tap conversations list settings button
    And I tap on my name
    And I see edit name field with my name
    When I tap options button
    Then I do not see ABOUT item in Options menu

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
    And I tap options button
    And I tap about button
    Then I see About page
    When I tap on About page
    Then I see personal info page

    Examples:
      | Name      |
      | user1Name |

