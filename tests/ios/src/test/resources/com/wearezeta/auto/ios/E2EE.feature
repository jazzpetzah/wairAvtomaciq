Feature: E2EE

  @C3284 @staging
  Scenario Outline: Verify newly added people in a group conversation don't see a history
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to Myself,<Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact2> sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact3> sends 1 encrypted message to group conversation <GroupChatName>
    Given I wait for 5 seconds
    Given User <Contact1> adds user Myself to group chat <GroupChatName>
    When I tap on contact name <GroupChatName>
    Then I see group chat page with users <Contact1>,<Contact2>,<Contact3>
    And I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | EncryptedGrp  |

  @C3293 @noAcceptAlert @staging
  Scenario Outline: (ZIOS-5684) Verify system message appearance in case of using a new device by you
    Given There is 1 user where <Name> is me
    Given User Myself removes his avatar picture
    Given I sign in using my email
    Given I accept alert
    Given I accept First Time overlay if it is visible
    Given I accept alert
    Given I see conversations list
    When I remember the state of my avatar
    And User Myself adds a new device <DeviceName> with label <DeviceLabel>
    Then I wait until my avatar is changed
    When I tap my avatar
    Then I verify the alert contains text <DeviceLabel>
    When I accept alert
    And I close self profile
    Then I wait until my avatar is not changed

    Examples:
      | Name      | DeviceName | DeviceLabel  |
      | user1Name | Device1    | Device1Label |

  @C3296 @staging
  Scenario Outline: Verify opening device details by clicking on it in person's profile
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I see conversations list
    And User <Contact1> adds new devices <DeviceName1>,<DeviceName2>,<DeviceName3>
    And I tap on contact name <Contact1>
    And I open conversation details
    And I tap on Devices button
    Then I see 3 devices shown in participant devices tab

    Examples:
      | Name      | Contact1  | DeviceName1 | DeviceName2 | DeviceName3 |
      | user1Name | user2Name | Device1     | Device2     | Device3     |

  @C3290 @staging
  Scenario Outline: (ZIOS-5741) Verify new device is added to device management after sign in
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    And User Myself adds a new device <DeviceName> with label <DeviceLabel>
    When I tap my avatar
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I select settings item Privacy & Security
    And I select settings item Manage devices
    Then I see settings item <DeviceLabel>

    Examples:
      | Name      | DeviceName | DeviceLabel  |
      | user1Name | Device1    | Device1Label |

  @torun @C3510 @staging
  Scenario Outline: Verify deleting one of the devices from device management by Edit
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    And User Myself adds new devices <DeviceName>
    When I tap my avatar
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I select settings item Privacy & Security
    And I select settings item Manage devices
    And I press Edit button
    And I press Delete <DeviceName> button from devices
    And I press the Delete Button
    And I confirm with my <Password> the deletion of the device

    Examples:
      | Name      | DeviceName |Password     |
      | user1Name | Device1    |user1Password|