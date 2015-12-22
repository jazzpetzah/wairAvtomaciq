Feature: E2EE

  @e2ee
  Scenario Outline: Remove device from device list
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I enter email "<Email>"
    Given I enter password "<Password>"
    Given I check option to remember me
    Given I press Sign In button
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    When user <Name> adds a new device <Device> with label <Label>
    And I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I wait for 30 seconds

    Examples: 
      | Email      | Password      | Name      | Device  | Label  |
      | user1Email | user1Password | user1Name | Remote1 | Label1 |
