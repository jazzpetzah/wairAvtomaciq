Feature: E2EE

  @C145965 @staging
  Scenario Outline: Verify device verification [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new devices <DeviceName1>,<DeviceName2>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I do not see shield icon next to conversation input field
    When I open conversation details
    And I switch to Devices tab
    And I open details page of device number 1 on iPad
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page on iPad
    And I open details page of device number 2 on iPad
    And I tap Verify switcher on Device Details page
    And I dismiss popover on iPad
    Then I see shield icon next to conversation input field
    And I see 2 conversation entries

    Examples:
      | Name      | Contact1  | DeviceName1 | DeviceName2 |
      | user1Name | user2Name | Device1     | Device2     |
