Feature: Settings

  @id67 @regression @rc
  Scenario Outline: Open and Close settings page
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I tap on my avatar
    And I tap options button
    And I tap settings button
    Then I see settings page
    When I press back button
    Then I see personal info page

    Examples:
      | Name      |
      | user1Name |

  @id71 @regression
  Scenario Outline: Can not open Settings page when editing user name
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I tap on my avatar
    And I tap on my name
    And I see edit name field with my name
    And I tap options button
    And I tap about button
    Then I do not see About page

    Examples:
      | Name      |
      | user1Name |

  @id92 @regression @rc
  Scenario Outline: Check About page in settings menu
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I tap on my avatar
    And I tap options button
    And I tap about button
    Then I see About page
    When I tap on About page
    Then I see personal info page

    Examples:
      | Name      |
      | user1Name |

  @id4062 @regression
  Scenario Outline: Verify I can switch dark/white theme from settings
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    Then I remember the conversation view
    When I navigate back from dialog page
    And I tap on my avatar
    And I see personal info page
    And I tap options button
    And I tap settings button
    And I select "Account" settings menu item
    And I switch color theme in settings
    And I press back button
    And I press back button
    And I see personal info page
    And I close Personal Info Page
    When I tap on contact name <Contact>
    And I see dialog page
    And I scroll to the bottom of conversation view
    Then I see the conversation view is changed
    When I navigate back from dialog page
    And I tap on my avatar
    And I see personal info page
    And I tap options button
    And I tap settings button
    And I select "Account" settings menu item
    And I switch color theme in settings
    And I press back button
    And I press back button
    And I see personal info page
    And I close Personal Info Page
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see the conversation view is not changed

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
