Feature: E2EE

  @C1846 @regression
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
    Then I see a device named <Device> in the devices section
    When I click on the device <Device> in the devices section
    Then I see a device named <Device> with label <Label> in the device details
    When I click the remove device link
    And I type password "<Password>" into the device remove form
    And I click the remove button
    Then I do not see a device named <Label>, <Device> in the devices section
    When I click close settings page button
    And I wait for 2 seconds
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I do not see a device named <Device> in the devices section

    Examples: 
      | Email      | Password      | Name      | Device  | Label  |
      | user1Email | user1Password | user1Name | Remote1 | Label1 |

  @C1847 @regression
  Scenario Outline: Login as permanent device after permanent device limit is reached
    Given There is 1 user where <Name> is me
    Given user <Name> adds a new device Device1 with label Label1
    Given user <Name> adds a new device Device2 with label Label2
    Given user <Name> adds a new device Device3 with label Label3
    Given user <Name> adds a new device Device4 with label Label4
    Given user <Name> adds a new device Device5 with label Label5
    Given user <Name> adds a new device Device6 with label Label6
    Given user <Name> adds a new device Device7 with label Label7
    Given I switch to Sign In page
    Given I enter email "<Email>"
    Given I enter password "<Password>"
    Given I check option to remember me
    When I press Sign In button
    Then I am informed about the device limit
    When I click on Sign Out on the device limit page
    Then I see Sign In page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I check option to remember me
    And I press Sign In button
    Then I am informed about the device limit
    When I click button to manage devices
    Then I see 7 devices under managed devices
    And I see a device named Device1 with label Label1 under managed devices
    And I see a device named Device2 with label Label2 under managed devices
    And I see a device named Device3 with label Label3 under managed devices
    And I see a device named Device4 with label Label4 under managed devices
    And I see a device named Device5 with label Label5 under managed devices
    And I see a device named Device6 with label Label6 under managed devices
    And I see a device named Device7 with label Label7 under managed devices

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C2100 @regression
  Scenario Outline: Login as temporary device after device limit is reached
    Given There is 1 user where <Name> is me
    Given user <Name> adds a new device Device1 with label Label1
    Given user <Name> adds a new device Device2 with label Label2
    Given user <Name> adds a new device Device3 with label Label3
    Given user <Name> adds a new device Device4 with label Label4
    Given user <Name> adds a new device Device5 with label Label5
    Given user <Name> adds a new device Device6 with label Label6
    Given user <Name> adds a new device Device7 with label Label7
    Given I switch to Sign In page
    Given I enter email "<Email>"
    Given I enter password "<Password>"
    When I press Sign In button
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    And I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I see a device named Device1 in the devices section
    And I see a device named Device2 in the devices section
    And I see a device named Device3 in the devices section
    And I see a device named Device4 in the devices section
    And I see a device named Device5 in the devices section
    And I see a device named Device6 in the devices section
    And I see a device named Device7 in the devices section

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C2098 @regression
  Scenario Outline: Verify current browser is set as permanent device
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I enter email "<Email>"
    Given I enter password "<Password>"
    When I check option to remember me
    And I press Sign In button
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    And I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    And I remember the device id of the current device
    And I click close settings page button
    And I wait for 2 seconds
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see Sign In page
    And I enter email "<Email>"
    And I enter password "<Password>"
    And I check option to remember me
    And I press Sign In button
    And I see Contacts Upload dialog
    And I close Contacts Upload dialog
    And I see my avatar on top of Contact list
    And I open self profile
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I verify that the device id of the current device is still the same

    Examples: 
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |