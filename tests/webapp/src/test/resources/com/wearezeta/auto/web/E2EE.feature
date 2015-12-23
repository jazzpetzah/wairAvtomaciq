Feature: E2EE

  @regression @c1846
  Scenario Outline: Remove remote device from device list
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
    Then I see a device named <Label>, <Device> in the devices section
    When I click on the device <Label>, <Device> in the devices section
    Then I see a device named <Label>, <Device> in the device details
    When I click the remove device link
    And I type password "<Password>" into the device remove form
    And I click the remove button
    Then I do not see a device named <Label>, <Device> in the devices section
    When I click close settings page button
    And I wait for 2 seconds
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I do not see a device named <Label>, <Device> in the devices section

    Examples: 
      | Email      | Password      | Name      | Device  | Label  |
      | user1Email | user1Password | user1Name | Remote1 | Label1 |

  @regression @c1847
  Scenario Outline: Login as permanent device after permanent device limit is reached
    Given There is 1 user where <Name> is me
    Given user <Name> adds a new device Device1 with label Label1
    Given user <Name> adds a new device Device2 with label Label2
    Given user <Name> adds a new device Device3 with label Label3
    Given user <Name> adds a new device Device4 with label Label4
    Given user <Name> adds a new device Device5 with label Label5
    Given user <Name> adds a new device Device6 with label Label6
    Given user <Name> adds a new device Device7 with label Label7
    Given user <Name> adds a new device Device8 with label Label8
    Given I switch to Sign In page
    Given I enter email "<Email>"
    Given I enter password "<Password>"
    Given I check option to remember me
    Given I press Sign In button
    And I wait for 60 seconds

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |
     