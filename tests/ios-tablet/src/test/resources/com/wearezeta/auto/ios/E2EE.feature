Feature: E2EE

  @C145965 @rc @regression @fastLogin
  Scenario Outline: Verify device verification [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Users add the following devices: {"<Contact1>": [{"name": "<DeviceName1>"}, {"name": "<DeviceName2>"}]}
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I do not see shield icon in the conversation view
    When I open conversation details
    And I switch to Devices tab on Single user profile page
    And I open details page of device number 1 on Devices tab
    And I tap Verify switcher on Device Details page
    And I tap Back button on Device Details page
    And I open details page of device number 2 on Devices tab
    And I tap Verify switcher on Device Details page
    And I dismiss popover on iPad
    Then I see shield icon in the conversation view
    And I see 3 conversation entries

    Examples:
      | Name      | Contact1  | DeviceName1 | DeviceName2 |
      | user1Name | user2Name | Device1     | Device2     |

  @C145964 @rc @regression @fastLogin
  Scenario Outline: Verify system message appearance in case of using a new device by you [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to user Myself
    And I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab on Single user profile page
    And I open details page of device number 1 on Devices tab
    And I tap Verify switcher on Device Details page
    And I dismiss popover on iPad
    When Users add the following devices: {"Myself": [{"name": "<DeviceName2>", "label": "<DeviceLabel2>"}]]}
    Then I do not see shield icon in the conversation view
    And I see "<StartedUsingMsg>" system message in the conversation view

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 | StartedUsingMsg                |
      | user1Name | user2Name | Device2     | Label2       | YOU STARTED USING A NEW DEVICE |
