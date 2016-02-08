Feature: E2EE

  @C3284 @rc @regression
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

  @C3296 @regression
  Scenario Outline: Verify opening device details by clicking on it in person's profile
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I see conversations list
    And User <Contact1> adds new devices <DeviceName1>,<DeviceName2>,<DeviceName3>
    And I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab
    Then I see 3 devices shown in participant devices tab

    Examples:
      | Name      | Contact1  | DeviceName1 | DeviceName2 | DeviceName3 |
      | user1Name | user2Name | Device1     | Device2     | Device3     |

  @C3290 @noAcceptAlert @regression
  Scenario Outline: (ZIOS-5741) Verify new device is added to device management after sign in
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
    When I tap my avatar
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I select settings item Privacy & Security
    And I select settings item Manage devices
    Then I see settings item <DeviceLabel>

    Examples:
      | Name      | DeviceName | DeviceLabel  |
      | user1Name | Device1    | Device1Label |

  @C3295 @staging
  Scenario Outline: Verify shield appearance on the person's profile after verifying all the clients
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new devices <DeviceName1>,<DeviceName2>
    Given I sign in using my email
    Given I see conversations list
    And I tap on contact name <Contact1>
    And I open conversation details
    And I do not see shield icon on conversation details page
    And I switch to Devices tab
    When I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I open details page of device number 2
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I switch to Details tab
    Then I see shield icon on conversation details page

    Examples:
      | Name      | Contact1  | DeviceName1 | DeviceName2 |
      | user1Name | user2Name | Device1     | Device2     |

  @C3287 @staging
  Scenario Outline: Verify the group conversation is marked as verified after verifying clients of each other
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> adds a new device <DeviceName1> with label <DeviceLabel1>
    Given User <Contact2> adds a new device <DeviceName2> with label <DeviceLabel2>
    Given I sign in using my email
    Given I see conversations list
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    When I tap on contact name <GroupChatName>
    Then I do not see shield icon next to conversation input field
    And I open conversation details
    When I select participant <Contact1>
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I close group participant details page
    And I select participant <Contact2>
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I close group participant details page
    And I close group info page
    And I click Close input options button
    Then I see shield icon next to conversation input field
    And I see last message in dialog is expected message <VerificationMsg>

    Examples:
      | Name      | Contact1  | Contact2  | DeviceName1 | DeviceLabel1 | DeviceName2 | DeviceLabel2 | GroupChatName | VerificationMsg               |
      | user1Name | user2Name | user3Name | Device1     | Label1       | Device2     | Label2       | VerifiedGroup | ALL FINGERPRINTS ARE VERIFIED |