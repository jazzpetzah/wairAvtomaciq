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

  @C3290 @noAcceptAlert @rc @regression
  Scenario Outline: (ZIOS-5741) Verify new device is added to device management after sign in
    Given There is 1 user where <Name> is me
    Given User Myself removes his avatar picture
    Given I sign in using my email
    Given I see conversations list
    When I remember the state of settings gear
    And User Myself adds a new device <DeviceName> with label <DeviceLabel>
    Then I wait until settings gear is changed
    When I tap settings gear button
    Then I verify the alert contains text <DeviceName>
    When I accept alert
    And I close self profile
    Then I wait until settings gear is not changed
    When I tap settings gear button
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I select settings item Privacy & Security
    And I select settings item Manage devices
    Then I see settings item <DeviceName>

    Examples:
      | Name      | DeviceName | DeviceLabel  |
      | user1Name | Device1    | Device1Label |

  @C3295 @regression
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

  @C3287 @regression
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
    Then I see shield icon next to conversation input field
    #BUG Labels can not be located right now in appium
    #And I see last message in dialog is expected message <VerificationMsg>
    Then I see 2 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | DeviceName1 | DeviceLabel1 | DeviceName2 | DeviceLabel2 | GroupChatName | VerificationMsg               |
      | user1Name | user2Name | user3Name | Device1     | Label1       | Device2     | Label2       | VerifiedGroup | ALL FINGERPRINTS ARE VERIFIED |

  @C3294 @rc @regression
  Scenario Outline: (ZIOS-5787) Verify system message appearance in case of using a new device by friend
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to user Myself
    And I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    When User <Contact1> adds a new device <DeviceName2> with label <DeviceLabel2>
    And User <Contact1> sends 1 encrypted message using device <DeviceName2> to user Myself
    Then I do not see shield icon next to conversation input field
    # TODO: Check the device label in the system message
    #BUG system message labels can not be located on appium
    #Then I see the conversation view contains message <ExpectedMsg>
    Then I see 5 conversation entries

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 | ExpectedMsg                |
      | user1Name | user2Name | Device2     | Label2       | STARTED USING A NEW DEVICE |

  @C3293 @rc @regression
  Scenario Outline: Verify system message appearance in case of using a new device by you
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to user Myself
    And I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    When User <Contact1> adds a new device <DeviceName2> with label <DeviceLabel2>
    And I type the default message and send it
    Then I see the label "<Contact1> <ExpectedSuffix>" on New Device overlay

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 | ExpectedSuffix             |
      | user1Name | user2Name | Device2     | Label2       | started using a new device |

  @C14310 @noAcceptAlert @regression
  Scenario Outline: On first login on 2nd device there should be an explanation that user will not see previous messages
    Given There are 1 user where <Name> is me
    Given User Myself adds a new device <DeviceName> with label <DeviceLabel>
    Given I see sign in screen
    When I switch to Log In tab
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Login button
    And I accept alert
    Then I see First Time overlay

    Examples:
      | Login      | Password      | Name      | DeviceName | DeviceLabel  |
      | user1Email | user1Password | user1Name | Device1    | Device1Label |

  @C3510 @noAcceptAlert @regression
  Scenario Outline: Verify deleting one of the devices from device management by Edit
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    And User Myself adds new devices <DeviceName>
    When I tap settings gear button
    And I accept alert
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I select settings item Privacy & Security
    And I select settings item Manage devices
    And I tap Edit button
    And I tap Delete <DeviceName> button from devices
    And I confirm with my <Password> the deletion of the device
    Then I do not see device <DeviceName> in devices list

    Examples:
      | Name      | DeviceName | Password      |
      | user1Name | Device1    | user1Password |

  @C3509 @regression
  Scenario Outline: (ZIOS-5741) Verify verifying/unverifying one of the devices
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    And User Myself adds a new device <DeviceName> with label <DeviceLabel>
    When I tap settings gear button
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I select settings item Privacy & Security
    And I select settings item Manage devices
    When I open details page of device number 2
    And I tap Verify switcher on Device Details page
    And I switch to the previous settings page
    Then I see the label Verified is shown for the device <DeviceName>
    When I open details page of device number 2
    And I tap Verify switcher on Device Details page
    And I switch to the previous settings page
    Then I see the label Not Verified is shown for the device <DeviceName>

    Examples:
      | Name      | DeviceName | DeviceLabel  |
      | user1Name | Device1    | Device1Label |

  @C3492 @regression
  Scenario Outline: Verify link is active for your own device and leads you to device's fingerprint
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I see conversations list
    And I tap on contact name <Contact1>
    #BUG system msg e2ee labels not be located by appium
    #And I see the conversation view contains message <ExpectedMsg>
    And I see 1 conversation entries
    #BUG link can not be located
    And I tap on THIS DEVICE link
    And I open details page of device number 1
    Then I see fingerprint is not empty

    Examples:
      | Name      | Contact1  | ExpectedMsg               |
      | user1Name | user2Name | STARTED USING THIS DEVICE |

  @C14317 @rc @regression
  Scenario Outline: First time when 1:1 conversation is degraded - I can ignore alert screen and send messages with resend button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to user Myself
    And I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    When User <Contact1> adds a new device <DeviceName2> with label <DeviceLabel2>
    And I type the default message and send it
    And I close New Device overlay
    And I resend the last message in the conversation with Resend button
    Then I see 2 default messages in the dialog

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 |
      | user1Name | user2Name | Device2     | Label2       |

  @C3288 @regression
  Scenario Outline: Verify conversation is downgraded after adding a new device
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to user Myself
    And I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    When User Myself adds a new device <DeviceName2> with label <DeviceLabel2>
    Then I do not see shield icon next to conversation input field
    # FIXME: Make it possible in the app to detect labels text with Appium
    # Then I see the conversation view contains message <ExpectedMsg>
    #At least checking that all system messages are there, check all conv entries
    Then I see 4 conversation entries

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 | ExpectedMsg                    |
      | user1Name | user2Name | Device2     | Label2       | YOU STARTED USING A NEW DEVICE |

  @C3286 @regression @rc
  Scenario Outline: Verify conversation is marked as verified after approving all friend's clients in 1-to-1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new devices <DeviceName1>,<DeviceName2>
    Given I sign in using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I do not see shield icon next to conversation input field
    When I open conversation details
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I open details page of device number 2
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    Then I see shield icon next to conversation input field
    # FIXME: Make it possible in the app to detect labels text with Appium
    # And I see last message in dialog is expected message <VerificationMsg>
    Then I see 2 conversation entries

    Examples:
      | Name      | Contact1  | DeviceName1 | DeviceName2 | VerificationMsg               |
      | user1Name | user2Name | Device1     | Device2     | ALL FINGERPRINTS ARE VERIFIED |

  @C3291 @rc @regression
  Scenario Outline: Verify device management appearance after 7 sign ins
    Given There is 1 user where <Name> is me
    Given User Myself adds new devices <DeviceName1>,<DeviceName2>,<DeviceName3>,<DeviceName4>,<DeviceName5>,<DeviceName6>,<DeviceName7>
    When I sign in using my email
    Then I see Manage Devices overlay

    Examples:
      | Name      | DeviceName1 | DeviceName2 | DeviceName3 | DeviceName4 | DeviceName5 | DeviceName6 | DeviceName7 |
      | user1Name | Device1     | Device2     | Device3     | Device4     | Device5     | Device6     | Device7     |

  @C14314 @regression
  Scenario Outline: Verify you can see device ids of the other conversation participant in participant details view inside a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> adds new devices <DeviceName1>,<DeviceName2>
    Given User <Contact2> adds new devices <DeviceName3>,<DeviceName4>
    Given I sign in using my email
    Given I see conversations list
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    When I tap on contact name <GroupChatName>
    And I open conversation details
    When I select participant <Contact1>
    And I switch to Devices tab
    Then I see user <Contact1> device IDs are presented on participant devices tab
    And I click close user profile page button
    When I select participant <Contact2>
    And I switch to Devices tab
    Then I see user <Contact2> device IDs are presented on participant devices tab

    Examples:
      | Name      | Contact1  | Contact2  | DeviceName1 | DeviceName2 | GroupChatName | DeviceName3 | DeviceName4 |
      | user1Name | user2Name | user3Name | Device1     | Device2     | VerifiedGroup | Device3     | Device4     |

  @C14318 @regression
  Scenario Outline: First time when group conversation is degraded - I can ignore alert screen and send messages with resend button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to group conversation <GroupChatName>
    Given User <Contact2> sends 1 encrypted message to group conversation <GroupChatName>
    And I tap on group chat with name <GroupChatName>
    And I open conversation details
    And I select participant <Contact1>
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    And I select participant <Contact2>
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    And I close group info page
    When User <Contact1> adds a new device <DeviceName2> with label <DeviceLabel2>
    And I type the default message and send it
    And I close New Device overlay
    And I resend the last message in the conversation with Resend button
    Then I see 3 default messages in the dialog

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 | Contact2  | GroupChatName |
      | user1Name | user2Name | Device2     | Label2       | user3Name | ThisGroup     |

  @C3507 @regression
  Scenario Outline: Verify remove, verify and reset session are absent for current device
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    And I tap settings gear button
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I select settings item Privacy & Security
    And I select settings item Manage devices
    When I tap on current device
    Then I see settings item Key Fingerprint
    And I do not see settings item Verified
    And I do not see settings item Reset session
    And I do not see settings item Remove device

    Examples:
      | Name      |
      | user1Name |

  @C3511 @noAcceptAlert @regression
  Scenario Outline: Verify deleting one of the devices from device information screen
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    And User Myself adds new device <DeviceName>
    When I tap settings gear button
    And I accept alert
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    And I select settings item Privacy & Security
    And I select settings item Manage devices
    And I open details page of device number 2
    And I tap Remove Device on device detail page
    And I confirm with my <Password> the deletion of the device
    Then I do not see device <DeviceName> in devices list

    Examples:
      | Name      | DeviceName | Password      |
      | user1Name | Device1    | user1Password |

  @C3289 @regression
  Scenario Outline: Verify conversation is not verified after checking only one device out of many
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new devices <DeviceName1>,<DeviceName2>
    Given I sign in using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I do not see shield icon next to conversation input field
    When I open conversation details
    And I switch to Devices tab
    And I open details page of device number 2
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    And I do not see shield icon next to conversation input field
    # FIXME: Make it possible in the app to detect labels text with Appium
    # Then I do not see the conversation view contains message <ExpectedMessage>
    Then I see 1 conversation entry

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceName1 | ExpectedMessage               |
      | user1Name | user2Name | Device2     | Device1     | ALL FINGERPRINTS ARE VERIFIED |

  @C3498 @rc @regression
  Scenario Outline: Verify "learn more" leads to the proper page
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to user Myself
    And I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab
    When I tap "Why verify conversations?" link in user details
    And I wait for 3 seconds
    Then I see "https://support.wire.com" web page opened
    When I tap Back To Wire button
    And I wait for 7 seconds
    And I open details page of device number 1
    And I tap "How do I do that?" link in user details
    And I wait for 7 seconds
    Then I see "https://support.wire.com" web page opened

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C3494 @regression
  Scenario Outline: Verify unverifying of the device in verified conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given I sign in using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    Then I see shield icon next to conversation input field
    # FIXME: Make it possible in the app to detect labels text with Appium
    # Then I see last message in dialog is expected message <VerificationMsg>
    Then I see 2 conversation entries
    When I open conversation details
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    Then I do not see shield icon next to conversation input field
    # FIXME: Make it possible in the app to detect labels text with Appium
    # Then I see last message in dialog contains expected message <UnverificationMsg>
    Then I see 3 conversation entries

    Examples:
      | Name      | Contact1  | VerificationMsg               | UnverificationMsg     |
      | user1Name | user2Name | ALL FINGERPRINTS ARE VERIFIED | YOU UNVERIFIED ONE OF |

  @C3500 @regression
  Scenario Outline: Verify shield is not shown when any text presents into the input field
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given I sign in using my email
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab
    And I open details page of device number 1
    And I tap Verify switcher on Device Details page
    And I navigate back from Device Details page
    And I click close user profile page button
    Then I see shield icon next to conversation input field
    When I type the default message
    Then I do not see shield icon next to conversation input field

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C14311 @regression
  Scenario Outline: Verify the appropriate device is signed out if you remove it from settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    When User Myself removes all his registered OTR clients
    Then I see sign in screen

    Examples:
      | Name      |
      | user1Name |
