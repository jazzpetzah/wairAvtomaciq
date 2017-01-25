Feature: E2EE

  @C3284 @rc @regression @fastLogin
  Scenario Outline: Verify newly added people in a group conversation don't see a history
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to Myself,<Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given Users add the following devices: {"<Contact1>": [{}], "<Contact2>": [{}], "<Contact3>": [{}]}
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 default message to conversation <GroupChatName>
    Given User <Contact2> sends 1 default message to conversation <GroupChatName>
    Given User <Contact3> sends 1 default message to conversation <GroupChatName>
    Given I wait for 5 seconds
    Given User <Contact1> adds Myself to group chat <GroupChatName>
    When I tap on contact name <GroupChatName>
    Then I see the conversation with <GroupChatName>
    And I see 0 conversation entries

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName |
      | user1Name | user2Name | user3Name | user4Name | EncryptedGrp  |

  @C3296 @regression @fastLogin
  Scenario Outline: Verify opening device details by clicking on it in person's profile
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Users add the following devices: {"<Contact1>": [{"name": "<DeviceName1>"}, {"name": "<DeviceName2>"}, {"name": "<DeviceName3>"}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I open conversation details
    When I switch to Devices tab on Single user profile page
    Then I see 3 items are shown on Devices tab

    Examples:
      | Name      | Contact1  | DeviceName1 | DeviceName2 | DeviceName3 |
      | user1Name | user2Name | Device1     | Device2     | Device3     |

  @C3290 @rc @regression @fastLogin
  Scenario Outline: Verify new device is added to device management after sign in
    Given There is 1 user where <Name> is me
    Given User Myself removes his avatar picture
    Given I sign in using my email
    Given I see conversations list
    When I remember the state of settings gear
    And Users add the following devices: {"Myself": [{"name": "<DeviceName>", "label": "<DeviceLabel>"}]}
    Then I wait until settings gear is changed
    When I tap settings gear button
    Then I see alert contains text <DeviceName>
    When I accept alert
    And I tap Done navigation button on Settings page
    Then I wait until settings gear is not changed
    When I tap settings gear button
    And I select settings item Devices
    Then I see settings item <DeviceName>

    Examples:
      | Name      | DeviceName | DeviceLabel  |
      | user1Name | Device1    | Device1Label |

  @C3295 @regression @fastLogin
  Scenario Outline: Verify shield appearance on the person's profile after verifying all the clients
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Users add the following devices: {"<Contact1>": [{"name": "<DeviceName1>"}, {"name": "<DeviceName2>"}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    And I tap on contact name <Contact1>
    And I open conversation details
    And I do not see shield icon on Single user profile page
    And I switch to Devices tab on Single user profile page
    When I open details page of device number 1 on Devices tab
    And I tap Verify switcher on Device Details page
    And I tap Back button on Device Details page
    And I open details page of device number 2 on Devices tab
    And I tap Verify switcher on Device Details page
    And I tap Back button on Device Details page
    And I switch to Details tab on Single user profile page
    Then I see shield icon on Single user profile page

    Examples:
      | Name      | Contact1  | DeviceName1 | DeviceName2 |
      | user1Name | user2Name | Device1     | Device2     |

  @C3287 @regression @fastLogin
  Scenario Outline: Verify the group conversation is marked as verified after verifying clients of each other
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{"name": "<DeviceName1>", "label": "<DeviceLabel1>"}], "<Contact2>": [{"name": "<DeviceName2>", "label": "<DeviceLabel2>"}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given User <Contact2> sends 1 default message to conversation Myself
    Given I see conversations list
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    When I tap on contact name <GroupChatName>
    Then I do not see shield icon in the conversation view
    And I open conversation details
    When I select participant <Contact1> on Group info page
    And I switch to Devices tab on Group participant profile page
    And I open details page of device number 1 on Devices tab
    And I tap Verify switcher on Device Details page
    And I tap Back button on Device Details page
    And I tap X button on Group participant profile page
    And I select participant <Contact2> on Group info page
    And I switch to Devices tab on Group participant profile page
    And I open details page of device number 1 on Devices tab
    And I tap Verify switcher on Device Details page
    And I tap Back button on Device Details page
    And I tap X button on Group participant profile page
    And I tap X button on Group info page
    Then I see shield icon in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | DeviceName1 | DeviceLabel1 | DeviceName2 | DeviceLabel2 | GroupChatName |
      | user1Name | user2Name | user3Name | Device1     | Label1       | Device2     | Label2       | VerifiedGroup |

  @C3294 @rc @regression @fastLogin
  Scenario Outline: Verify system message appearance in case of using a new device by friend
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{}]}
    Given I sign in using my email
    Given I see conversations list
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I tap on contact name <Contact1>
    Given I open conversation details
    Given I switch to Devices tab on Single user profile page
    Given I open details page of device number 1 on Devices tab
    Given I tap Verify switcher on Device Details page
    Given I tap Back button on Device Details page
    Given I tap X button on Single user profile page
    When Users add the following devices: {"<Contact1>": [{"name": "<DeviceName2>", "label": "<DeviceLabel2>"}]}
    And User <Contact1> sends 1 message using device <DeviceName2> to user Myself
    # Wait for sync
    And I wait for 4 seconds
    Then I do not see shield icon in the conversation view
    And I see "<Contact1>  <StartedUsingMsg>" system message in the conversation view

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 | StartedUsingMsg            |
      | user1Name | user2Name | Device2     | Label2       | STARTED USING A NEW DEVICE |

  @C3293 @rc @regression @fastLogin
  Scenario Outline: Verify system message appearance in case of using a new device by you
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I open conversation details
    Given I switch to Devices tab on Single user profile page
    Given I open details page of device number 1 on Devices tab
    Given I tap Verify switcher on Device Details page
    Given I tap Back button on Device Details page
    Given I tap X button on Single user profile page
    When Users add the following devices: {"<Contact1>": [{"name": "<DeviceName2>", "label": "<DeviceLabel2>"}]}
    And I type the default message and send it
    Then I see the label "<Contact1> <ExpectedSuffix>" on New Device overlay

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 | ExpectedSuffix             |
      | user1Name | user2Name | Device2     | Label2       | started using a new device |

  @C14310 @regression
  Scenario Outline: On first login on 2nd device there should be an explanation that user will not see previous messages
    Given There are 1 user where <Name> is me
    Given Users add the following devices: {"Myself": [{"name": "<DeviceName>", "label": "<DeviceLabel>"}]}
    Given I see sign in screen
    When I switch to Log In tab
    And I have entered login <Login>
    And I have entered password <Password>
    And I tap Login button
    And I accept alert if visible
    Then I see First Time overlay

    Examples:
      | Login      | Password      | Name      | DeviceName | DeviceLabel  |
      | user1Email | user1Password | user1Name | Device1    | Device1Label |

  @C3510 @regression @fastLogin
  Scenario Outline: Verify deleting one of the devices from device management by Edit
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    Given Users add the following devices: {"Myself": [{"name": "<DeviceName>"}]}
    When I tap settings gear button
    And I accept alert
    And I select settings item Devices
    And I tap Edit navigation button on Settings page
    And I tap Delete <DeviceName> button on Settings page
    And I confirm with my <Password> the deletion of the device on Settings page
    Then I do not see device <DeviceName> in devices list on Settings page

    Examples:
      | Name      | DeviceName | Password      |
      | user1Name | Device1    | user1Password |

  @C3509 @regression @fastLogin
  Scenario Outline: Verify verifying/unverifying one of the devices
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    Given Users add the following devices: {"Myself": [{"name": "<DeviceName>", "label": "<DeviceLabel>"}]}
    When I tap settings gear button
    And I accept alert
    And I select settings item Devices
    When I open details page of device number 2 on Settings page
    And I tap Verify switcher on Device Details page
    And I tap Back navigation button on Settings page
    Then I see the label Verified is shown for the device <DeviceName>
    When I open details page of device number 2 on Settings page
    And I tap Verify switcher on Device Details page
    And I tap Back navigation button on Settings page
    Then I see the label Not Verified is shown for the device <DeviceName>

    Examples:
      | Name      | DeviceName | DeviceLabel  |
      | user1Name | Device1    | Device1Label |

  @C3492 @regression @fastLogin
  Scenario Outline: Verify link is active for your own device and leads you to device's fingerprint
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email
    Given I see conversations list
    Given I tap on contact name <Contact1>
    When I see 1 conversation entry
    And I tap on THIS DEVICE link
    And I open details page of device number 1 on Settings page
    Then I see fingerprint is not empty on Device Details page

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C14317 @rc @regression @fastLogin
  Scenario Outline: First time when 1:1 conversation is degraded - I can ignore alert screen and send messages with resend button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I open conversation details
    Given I switch to Devices tab on Single user profile page
    Given I open details page of device number 1 on Devices tab
    Given I tap Verify switcher on Device Details page
    Given I tap Back button on Device Details page
    Given I tap X button on Single user profile page
    When Users add the following devices: {"<Contact1>": [{"name": "<DeviceName2>", "label": "<DeviceLabel2>"}]}
    And I type the default message and send it
    And I tap X button on New Device overlay
    And I resend the last message in the conversation with Resend button
    # Wait until the message is sent
    And I wait for 3 seconds
    Then I see 2 default messages in the conversation view

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 |
      | user1Name | user2Name | Device2     | Label2       |

  @C3288 @regression @fastLogin
  Scenario Outline: Verify conversation is downgraded after adding a new device
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I open conversation details
    Given I switch to Devices tab on Single user profile page
    Given I open details page of device number 1 on Devices tab
    Given I tap Verify switcher on Device Details page
    Given I tap Back button on Device Details page
    Given I tap X button on Single user profile page
    When Users add the following devices: {"Myself": [{"name": "<DeviceName2>", "label": "<DeviceLabel2>"}]}
    Then I do not see shield icon in the conversation view
    And I see "<ExpectedMsg>" system message in the conversation view

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 | ExpectedMsg                    |
      | user1Name | user2Name | Device2     | Label2       | YOU STARTED USING A NEW DEVICE |

  @C3286 @regression @rc @fastLogin
  Scenario Outline: Verify conversation is marked as verified after approving all friend's clients in 1-to-1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given Users add the following devices: {"<Contact1>": [{"name": "<DeviceName1>"}, {"name": "<DeviceName2>"}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
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
    And I tap Back button on Device Details page
    And I tap X button on Single user profile page
    Then I see shield icon in the conversation view
    And I see "<VerificationMsg>" system message in the conversation view

    Examples:
      | Name      | Contact1  | DeviceName1 | DeviceName2 | VerificationMsg               |
      | user1Name | user2Name | Device1     | Device2     | ALL FINGERPRINTS ARE VERIFIED |

  @C3291 @rc @regression
  Scenario Outline: Verify device management appearance after 7 sign ins
    Given There is 1 user where <Name> is me
    Given Users add the following devices: {"Myself": [{"name": "<DeviceName1>"}, {"name": "<DeviceName2>"}, {"name": "<DeviceName3>"}, {"name": "<DeviceName4>"}, {"name": "<DeviceName5>"}, {"name": "<DeviceName6>"}, {"name": "<DeviceName7>"}]}
    Given I switch to Log In tab
    Given I have entered login <Email>
    Given I have entered password <Password>
    When I tap Login button
    Then I see Manage Devices overlay

    Examples:
      | Name      | DeviceName1 | DeviceName2 | DeviceName3 | DeviceName4 | DeviceName5 | DeviceName6 | DeviceName7 | Email      | Password      |
      | user1Name | Device1     | Device2     | Device3     | Device4     | Device5     | Device6     | Device7     | user1Email | user1Password |

  @C14314 @regression @fastLogin
  Scenario Outline: Verify you can see device ids of the other conversation participant in participant details view inside a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{"name": "<DeviceName1>"}, {"name": "<DeviceName2>"}], "<Contact2>": [{"name": "<DeviceName3>"}, {"name": "<DeviceName4>"}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given User <Contact2> sends 1 default message to conversation Myself
    Given I see conversations list
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    When I tap on contact name <GroupChatName>
    And I open conversation details
    When I select participant <Contact1> on Group info page
    And I switch to Devices tab on Group participant profile page
    Then I see user <Contact1> device IDs are present on Devices tab
    And I tap X button on Single user profile page
    When I select participant <Contact2> on Group info page
    And I switch to Devices tab on Group participant profile page
    Then I see user <Contact2> device IDs are present on Devices tab

    Examples:
      | Name      | Contact1  | Contact2  | DeviceName1 | DeviceName2 | GroupChatName | DeviceName3 | DeviceName4 |
      | user1Name | user2Name | user3Name | Device1     | Device2     | VerifiedGroup | Device3     | Device4     |

  @C14318 @regression @fastLogin
  Scenario Outline: First time when group conversation is degraded - I can ignore alert screen and send messages with resend button
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Users add the following devices: {"<Contact1>": [{}], "<Contact2>": [{}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation <GroupChatName>
    Given User <Contact2> sends 1 default message to conversation <GroupChatName>
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I open conversation details
    Given I select participant <Contact1> on Group info page
    Given I switch to Devices tab on Group participant profile page
    Given I open details page of device number 1 on Devices tab
    Given I tap Verify switcher on Device Details page
    Given I tap Back button on Device Details page
    Given I tap X button on Single user profile page
    Given I select participant <Contact2> on Group info page
    Given I switch to Devices tab on Group participant profile page
    Given I open details page of device number 1 on Devices tab
    Given I tap Verify switcher on Device Details page
    Given I tap Back button on Device Details page
    Given I tap X button on Single user profile page
    Given I tap X button on Group info page
    When Users add the following devices: {"<Contact1>": [{"name": "<DeviceName2>", "label": "<DeviceLabel2>"}]}
    And I type the default message and send it
    And I tap X button on New Device overlay
    And I resend the last message in the conversation with Resend button
    Then I see 3 default messages in the conversation view

    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceLabel2 | Contact2  | GroupChatName |
      | user1Name | user2Name | Device2     | Label2       | user3Name | ThisGroup     |

  @C3507 @regression @fastLogin
  Scenario Outline: Verify remove, verify and reset session are absent for current device
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    And I tap settings gear button
    And I select settings item Devices
    When I tap my current device on Settings page
    Then I see settings item Key Fingerprint
    And I do not see settings item Verified
    And I do not see settings item Reset session
    And I do not see settings item Remove device

    Examples:
      | Name      |
      | user1Name |

  @C3511 @regression @fastLogin
  Scenario Outline: Verify deleting one of the devices from device information screen
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    Given Users add the following devices: {"Myself": [{"name": "<DeviceName>"}]}
    When I tap settings gear button
    And I accept alert
    And I select settings item Devices
    And I open details page of device number 2 on Settings page
    And I tap Remove Device button on Device Details page
    And I confirm with my <Password> the deletion of the device on Settings page
    # Wait for sync
    And I wait for 3 seconds
    Then I do not see device <DeviceName> in devices list on Settings page

    Examples:
      | Name      | DeviceName | Password      |
      | user1Name | Device1    | user1Password |

  @C3289 @regression @fastLogin
  Scenario Outline: Verify conversation is not verified after checking only one device out of many
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact1>
    Given Users add the following devices: {"<Contact1>": [{"name": "<DeviceName1>"}, {"name": "<DeviceName2>"}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I do not see shield icon in the conversation view
    When I open conversation details
    And I switch to Devices tab on Single user profile page
    And I open details page of device number 2 on Devices tab
    And I tap Verify switcher on Device Details page
    And I tap Back button on Device Details page
    And I tap X button on Single user profile page
    And I do not see shield icon in the conversation view
    Then I do not see "<ExpectedMessage>" system message in the conversation view


    Examples:
      | Name      | Contact1  | DeviceName2 | DeviceName1 | ExpectedMessage               |
      | user1Name | user2Name | Device2     | Device1     | ALL FINGERPRINTS ARE VERIFIED |

  @C3498 @rc @regression @fastLogin
  Scenario Outline: Verify "learn more" leads to the proper page
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    And I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab on Single user profile page
    When I tap "Why verify conversations?" link on Devices tab
    And I wait for 12 seconds
    Then I see "https://support.wire.com" web page opened
    When I tap Back To Wire button
    And I open details page of device number 1 on Devices tab
    And I tap "How do I do that?" link on Devices tab
    And I wait for 12 seconds
    Then I see "https://support.wire.com" web page opened

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C3494 @regression @fastLogin
  Scenario Outline: Verify unverifying of the device in verified conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User adds the following device: {"<Contact1>": [{}]}
    Given I sign in using my email
    Given User <Contact1> sends 1 default message to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I open conversation details
    And I switch to Devices tab on Single user profile page
    And I open details page of device number 1 on Devices tab
    And I tap Verify switcher on Device Details page
    And I tap Back button on Device Details page
    And I tap X button on Single user profile page
    Then I see shield icon in the conversation view
    And I see "<VerificationMsg>" system message in the conversation view
    When I open conversation details
    And I switch to Devices tab on Single user profile page
    And I open details page of device number 1 on Devices tab
    And I tap Verify switcher on Device Details page
    And I tap Back button on Device Details page
    And I tap X button on Single user profile page
    Then I do not see shield icon in the conversation view
    And I see "<UnverificationMsg>" system message in the conversation view

    Examples:
      | Name      | Contact1  | VerificationMsg               | UnverificationMsg     |
      | user1Name | user2Name | ALL FINGERPRINTS ARE VERIFIED | YOU UNVERIFIED ONE OF |

  @C14311 @regression @fastLogin
  Scenario Outline: Verify the appropriate device is signed out if you remove it from settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see conversations list
    When User Myself removes all his registered OTR clients
    Then I see sign in screen

    Examples:
      | Name      |
      | user1Name |

  @C395996 @staging @fastLogin
  Scenario Outline: Verify forwarded message is not delivered in downgraded conversation
    Given There are 3 users where <Name> is me
    Given Users add the following devices: {"<Contact1>": [{"name": "<Device1>", "label": "<Device1label>"}], "<Contact2>": [{}]}
    Given Myself is connected to all other users
    Given I sign in using my email
    Given User <Contact2> sends 1 default message to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I open conversation details
    Given I switch to Devices tab on Single user profile page
    Given I open details page of device number 1 on Devices tab
    Given I tap Verify switcher on Device Details page
    Given I tap Back button on Device Details page
    Given I tap X button on Single user profile page
    Given I navigate back to conversations list
    Given Users add the following devices: {"<Contact1>": [{"name": "<Device2>", "label": "<Device2label>"}]}
    Given User <Contact1> remembers the recent message from user Myself via device <Device1>
    Given User <Contact1> remembers the recent message from user Myself via device <Device2>
    Given I tap on contact name <Contact2>
    When I long tap default message in conversation view
    And I tap on Share badge item
    And I select <Contact1> conversation on Forward page
    And I tap Send button on Forward page
    And I navigate back to conversations list
    And I tap on contact name <Contact1>
    And I tap X button on New Device overlay
    And I see "<ResendLabel>" on the message toolbox in conversation view
    Then User <Contact1> sees the recent message from user Myself via device <Device1> is not changed in 5 seconds
    And User <Contact1> sees the recent message from user Myself via device <Device2> is not changed in 5 seconds

    Examples:
      | Name      | Contact1  | Contact2  | ResendLabel | Device1 | Device1label | Device2 | Device2label |
      | user1Name | user2Name | user3Name | Resend      | Device1 | Device1label | Device2 | Device2label |

  @C395997 @regression @fastLogin
  Scenario Outline: Verify forwarded image is not delivered in downgraded conversation
    Given There are 3 users where <Name> is me
    Given Users add the following devices: {"<Contact1>": [{"name": "<Device1>", "label": "<Device1label>"}], "<Contact2>": [{}]}
    Given Myself is connected to all other users
    Given I sign in using my email
    Given User <Contact2> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I open conversation details
    Given I switch to Devices tab on Single user profile page
    Given I open details page of device number 1 on Devices tab
    Given I tap Verify switcher on Device Details page
    Given I tap Back button on Device Details page
    Given I tap X button on Single user profile page
    Given I navigate back to conversations list
    Given Users add the following devices: {"<Contact1>": [{"name": "<Device2>", "label": "<Device2label>"}]}
    Given User <Contact1> remembers the recent message from user Myself via device <Device1>
    Given User <Contact1> remembers the recent message from user Myself via device <Device2>
    Given I tap on contact name <Contact2>
    When I long tap on image in conversation view
    And I tap on Share badge item
    And I select <Contact1> conversation on Forward page
    And I tap Send button on Forward page
    And I navigate back to conversations list
    And I tap on contact name <Contact1>
    And I tap X button on New Device overlay
    And I see "<ResendLabel>" on the message toolbox in conversation view
    Then User <Contact1> sees the recent message from user Myself via device <Device1> is not changed in 5 seconds
    And User <Contact1> sees the recent message from user Myself via device <Device2> is not changed in 5 seconds

    Examples:
      | Name      | Contact1  | Contact2  | ResendLabel | Picture     | Device1 | Device1label | Device2 | Device2label |
      | user1Name | user2Name | user3Name | Resend      | testing.jpg | Device1 | Device1label | Device2 | Device2label |