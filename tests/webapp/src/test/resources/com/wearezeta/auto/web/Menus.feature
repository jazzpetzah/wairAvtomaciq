Feature: Menus

  @smoke @id1648
  Scenario Outline: Check Preferences opening
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Email> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    When I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I see Settings dialog

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @smoke @id2190
  Scenario Outline: Verify sound settings are saved after re-login
    Given There is 1 user where <Name> is me
    Given I Sign in using login <Email> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    When I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    And I see Settings dialog
    When I select Sound Alerts setting to be None
    Then I see Sound Alerts setting is set to None
    And I click close settings page button
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    Given I Sign in using login <Email> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    And I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    And I see Settings dialog
    Then I see Sound Alerts setting is set to None
    When I select Sound Alerts setting to be Some
    Then I see Sound Alerts setting is set to Some
    And I click close settings page button
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And I switch to sign in page
    Given I Sign in using login <Email> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    And I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    When I see Settings dialog
    Then I see Sound Alerts setting is set to Some

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
