Feature: E2EE

  @e2ee
  Scenario Outline: Remove device from device list
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    When user <Name> adds a new device <Device>
    And I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I wait for 30 seconds

    Examples: 
      | Email      | Password      | Name      | Device |
      | user1Email | user1Password | user1Name | Remote |
