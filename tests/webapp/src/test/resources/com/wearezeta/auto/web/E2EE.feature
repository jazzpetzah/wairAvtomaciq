Feature: E2EE

  @C1846 @e2ee @regression
  Scenario Outline: Remove remote device from device list
    Given There is 1 user where <Name> is me
    Given user <Name> adds a new device <Device> with label <Label>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    Then I see the history info page
    When I click confirm on history info page
    Then I am signed in properly
    When I click gear button on self profile page
    And I select Settings menu item on self profile page
    And I wait for 2 seconds
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

  @C1847 @e2ee @smoke
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
    When I Sign in using login <Email> and password <Password>
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
    When I click remove device button for device named Device2 on the device limit page
    And I click cancel button for device named Device2 on the device limit page
    Then I see a device named Device2 with label Label2 under managed devices
    When I click remove device button for device named Device1 on the device limit page
    And I enter password "<Password>" to remove device named Device1 on the device limit page
    And I click remove button for device named Device1 on the device limit page
    Then I see the history info page
    And I click confirm on history info page
    And I am signed in properly

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C2100 @e2ee @regression
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
    Given I see Sign In page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then I see the history info page
    And I click confirm on history info page
    And I am signed in properly
    When I click gear button on self profile page
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

  @C12068 @e2ee @smoke
  Scenario Outline: Verify you can receive encrypted messages in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When Contact <Contact> sends encrypted message <EncryptedMessage> to user Myself
    Then I see text message <EncryptedMessage>

    Examples:
      | Email      | Password      | Name      | Contact   | EncryptedMessage |
      | user1Email | user1Password | user1Name | user2Name | EncryptedYo      |

  @C12069 @e2ee @smoke
  Scenario Outline: Verify you can receive encrypted images in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    Then I see sent picture <ImageName> in the conversation view

    Examples:
      | Email      | Password      | Name      | Contact   | ImageName                |
      | user1Email | user1Password | user1Name | user2Name | userpicture_portrait.jpg |

  @C12043 @e2ee @smoke
  Scenario Outline: Verify you can receive encrypted messages in group chat
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <GroupChatName>
    And Contact <Contact1> sends encrypted message <EncryptedMessage> to group conversation <GroupChatName>
    Then I see text message <EncryptedMessage>

    Examples:
      | Email      | Password      | Name      | Contact1   | Contact2  | EncryptedMessage | GroupChatName |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | EncryptedYo      | HybridGroup   |

  @C12044 @e2ee @smoke
  Scenario Outline: Verify you can receive encrypted images in group chat
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <GroupChatName>
    And User <Contact1> sends encrypted image <ImageName> to group conversation <GroupChatName>
    Then I see sent picture <ImageName> in the conversation view

    Examples:
      | Email      | Password      | Name      | Contact1   | Contact2  | ImageName                | GroupChatName |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | userpicture_portrait.jpg | HybridGroup   |

  @C12050 @e2ee @smoke
  Scenario Outline: Verify you receive encrypted content in 1:1 conversation after switching online
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given <Contact> is connected to Myself
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    When I am signed in properly
    And Contact <Contact> sends encrypted message <OnlineMessage> to user Myself
    Then I see text message <OnlineMessage>
    And I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And Contact <Contact> sends encrypted message <OfflineMessage> to user Myself
    And User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    And I see Sign In page
    And I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    Then I see text message <OnlineMessage>
    And I see text message <OfflineMessage>
    And I see sent picture <ImageName> in the conversation view

    Examples:
      | Email      | Password      | Name      | Contact   | OnlineMessage | OfflineMessage | ImageName                |
      | user1Email | user1Password | user1Name | user2Name | Hello         | Are you online | userpicture_portrait.jpg |

  @C12051 @e2ee @smoke
  Scenario Outline: Verify you receive encrypted content in group conversation after switching online
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    When I am signed in properly
    And I open conversation with <GroupChatName>
    And Contact <Contact1> sends encrypted message <OnlineMessage> to group conversation <GroupChatName>
    Then I see text message <OnlineMessage>
    And I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And Contact <Contact1> sends encrypted message <OfflineMessage> to group conversation <GroupChatName>
    And User <Contact1> sends encrypted image <ImageName> to group conversation <GroupChatName>
    And I see Sign In page
    And I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I see Contact list with name <GroupChatName>
    And I open conversation with <GroupChatName>
    Then I see text message <OnlineMessage>
    And I see text message <OfflineMessage>
    And I see sent picture <ImageName> in the conversation view

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | GroupChatName | OnlineMessage | OfflineMessage | ImageName                |
      | user1Email | user1Password | user1Name | user2Name | user3name | GroupChat     | Hello         | Are you online | userpicture_portrait.jpg |

  @C12045 @e2ee @regression
  Scenario Outline: Verify you can see device ids of the other conversation participant in 1:1 conversation details
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I click People button in one to one conversation
    Then I see Single User Profile popover
    When I switch to Devices tab on Single User Profile popover
    Then I verify system message contains <Message1> on Single User Profile popover
    When user <Contact> adds a new device Device1 with label Label1
    And I switch to Details tab on Single User Profile popover
    And I switch to Devices tab on Single User Profile popover
    Then I verify system message contains <Message2> on Single User Profile popover
    And I see all devices of user <Contact> on Single User Profile popover
    When I click on device Device1 of user <Contact> on Single User Profile popover
    Then I verify id of device Device1 of user <Contact> on device detail page of Single User Profile popover
    And I verify fingerprint of device Device1 of user <Contact> on device detail page of Single User Profile popover

    Examples:
      | Email      | Password      | Name      | Contact   | Message1                           | Message2                                     |
      | user1Email | user1Password | user1Name | user2Name | is not using the encrypted version | Wire gives every device a unique fingerprint |

  @C12046 @e2ee @regression
  Scenario Outline: Verify you can see device ids of the other conversation participant in group conversation details
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I am signed in properly
    When I open conversation with <GroupChatName>
    And I click People button in group conversation
    Then I see Group Participants popover
    When I click on participant <Contact1> on Group Participants popover
    And I switch to Devices tab on Single User Profile popover
    Then I verify system message contains <Message1> on Single User Profile popover
    When user <Contact1> adds a new device Device1 with label Label1
    And I switch to Details tab on Single User Profile popover
    And I switch to Devices tab on Single User Profile popover
    Then I verify system message contains <Message2> on Single User Profile popover
    And I see all devices of user <Contact1> on Single User Profile popover
    When I click on device Device1 of user <Contact1> on Single User Profile popover
    Then I verify id of device Device1 of user <Contact1> on device detail page of Single User Profile popover
    And I verify fingerprint of device Device1 of user <Contact1> on device detail page of Single User Profile popover

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | GroupChatName | Message1                           | Message2                                     |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat     | is not using the encrypted version | Wire gives every device a unique fingerprint |

  @C12053 @regression
  Scenario Outline: Verify it is possible to verify 1:1 conversation participants
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given user <Contact> adds a new device Device2 with label Label2
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I click People button in one to one conversation
    Then I see Single User Profile popover
    When I switch to Devices tab on Single User Profile popover
    And I click on device Device1 of user <Contact> on Single User Profile popover
    And I verify device on Device Detail popover
    And I click back button on the Device Detail popover
    Then I see device Device1 of user <Contact> is verified on Single User Profile popover
    Then I do not see user verified icon on Single User Profile popover
    And I click on device Device2 of user <Contact> on Single User Profile popover
    And I verify device on Device Detail popover
    And I click back button on the Device Detail popover
    Then I see device Device2 of user <Contact> is verified on Single User Profile popover
    Then I see user verified icon on Single User Profile popover
    When I click People button in one to one conversation
    # Not yet implemented on webapp:
    #Then I see <ALL_VERIFIED> action in conversation
    #And I see verified icon in conversation

  Examples:
    | Email      | Password      | Name      | Contact   | ALL_VERIFIED                  |
    | user1Email | user1Password | user1Name | user2Name | All fingerprints are verified |

  @C12055 @regression
  Scenario Outline: Verify it is possible to verify group conversation participants
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact1> adds a new device Device2 with label Label2
    Given user <Contact2> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device2 with label Label2
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <GroupChatName>
    And I click People button in group conversation
    Then I see Group Participants popover
    When I click on participant <Contact1> on Group Participants popover
    And I switch to Devices tab on Single User Profile popover
    And I click on device Device1 of user <Contact1> on Single User Profile popover
    And I verify device on Device Detail popover
    And I click back button on the Device Detail popover
    Then I see device Device1 of user <Contact1> is verified on Single User Profile popover
    Then I do not see user verified icon on Single User Profile popover
    And I click on device Device2 of user <Contact1> on Single User Profile popover
    And I verify device on Device Detail popover
    And I click back button on the Device Detail popover
    Then I see device Device2 of user <Contact1> is verified on Single User Profile popover
    Then I see user verified icon on Single User Profile popover
    When I click People button in group conversation
    And I click People button in group conversation
    When I click on participant <Contact2> on Group Participants popover
    And I switch to Devices tab on Single User Profile popover
    And I click on device Device1 of user <Contact2> on Single User Profile popover
    And I verify device on Device Detail popover
    And I click back button on the Device Detail popover
    Then I see device Device1 of user <Contact2> is verified on Single User Profile popover
    Then I do not see user verified icon on Single User Profile popover
    And I click on device Device2 of user <Contact2> on Single User Profile popover
    And I verify device on Device Detail popover
    And I click back button on the Device Detail popover
    Then I see device Device2 of user <Contact2> is verified on Single User Profile popover
    Then I see user verified icon on Single User Profile popover
    When I click People button in group conversation
    # Not yet implemented on webapp:
    #And I see <ALL_VERIFIED> action in conversation
    #And I see verified icon in conversation

  Examples:
    | Email      | Password      | Name      | Contact1  | Contact2  | GroupChatName | ALL_VERIFIED                  |
    | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat     | All fingerprints are verified |

  @C12056 @mute
  Scenario Outline: Verify you get an alert if group conversation participant sends a message from non-verified device
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I open conversation with <GroupChatName>
    And I click People button in group conversation
    And I see Group Participants popover
    And I click on participant <Contact1> on Group Participants popover
    And I switch to Devices tab on Single User Profile popover
    And I click on device Device1 of user <Contact1> on Single User Profile popover
    And I verify device on Device Detail popover
    And I click People button in group conversation
    And I click People button in group conversation
    And I click on participant <Contact2> on Group Participants popover
    And I switch to Devices tab on Single User Profile popover
    And I click on device Device1 of user <Contact2> on Single User Profile popover
    And I verify device on Device Detail popover
    And I click People button in group conversation
    And I see <ALL_VERIFIED> action in conversation
    When user <Contact1> adds a new device Device2 with label Label2
    And Contact <Contact1> sends encrypted message <Message> via device Device2 to group conversation <GroupChatName>
    Then I see <NEW_DEVICE> action for <Contact1> in conversation
    And I see text message <Message>

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | GroupChatName | ALL_VERIFIED                  | NEW_DEVICE                 | Message    |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat     | All fingerprints are verified | started using a new device | Unverified |

  @C12057 @e2ee @regression
  Scenario Outline: My other clients should be notified when I'm login on a new device (pending connections inbox)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And user <Name> adds a new device Device2 with label Label2
    #Then I see <NEW_DEVICE> action in conversation
    And I verify a badge is shown on my avatar
    And I open self profile
    And I see connected devices dialog
    And I see Device2 on connected devices dialog
    And I click OK on connected devices dialog
    And I do not see connected devices dialog

    Examples:
      | Email      | Password      | Name      | Contact   | NEW_DEVICE                     |
      | user1Email | user1Password | user1Name | user2Name | You started using a new device |

  @C28834 @e2ee @regression
  Scenario Outline: Make sure data is restored after switching between temporary login and back to permanent
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I open conversation with <GroupChatName>
    And I write message <GroupMessage>
    And I send message
    Then I see text message <GroupMessage>
    When I open conversation with <Contact1>
    And I write message <UserMessage>
    And I send message
    Then I see text message <UserMessage>
    When I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And I see Sign In page
    And I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then I see the history info page
    When I click confirm on history info page
    And I am signed in properly
    And I open conversation with <GroupChatName>
    Then I do not see text message <GroupMessage>
    When I open conversation with <Contact1>
    Then I do not see text message <UserMessage>
    And I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see Sign In page
    And I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I open conversation with <GroupChatName>
    Then I see text message <GroupMessage>
    When I open conversation with <Contact1>
    Then I see text message <UserMessage>

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | GroupChatName | GroupMessage | UserMessage   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | User1Chat     | Hello Group  | Hello User    |

  @C12054 @mute
  Scenario Outline: Verify you see an alert in verified 1:1 conversation when the other participant sends message from non-verified device
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I write message Hello
    And I send message
    And I click People button in one to one conversation
    Then I see Single User Profile popover
    When I switch to Devices tab on Single User Profile popover
    And I click on device Device1 of user <Contact> on Single User Profile popover
    And I verify device on Device Detail popover
    And I click back button on the Device Detail popover
    Then I see user verified icon on Single User Profile popover
    When I click People button in one to one conversation
    Then I see <ALL_VERIFIED> action in conversation
    When user <Contact> adds a new device Device2 with label Label2
    And Contact <Contact> sends encrypted message <Message> via device Device2 to user Myself
    Then I see <NEW_DEVICE> action in conversation
    # Not sure if we want to check for the message. Should it be shown or not? Assuming it should

    Examples:
      | Email      | Password      | Name      | Contact   | ALL_VERIFIED                  | NEW_DEVICE                 | Message          |
      | user1Email | user1Password | user1Name | user2Name | All fingerprints are verified | started using a new device | Unverified hello |

  @C82513 @e2ee @staging
  Scenario Outline: Verify you can recover from a broken session
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And Contact <Contact> sends encrypted message <Message1> via device Device1 to user Myself
    Then I see text message <Message1>
    When I break the session with device Device1 of user <Contact>
    And Contact <Contact> sends encrypted message <Message2> via device Device1 to user Myself
    Then I see <UNABLE_TO_DECRYPT> action in conversation
    When I click People button in one to one conversation
    And I see Single User Profile popover
    And I switch to Devices tab on Single User Profile popover
    And I click on device Device1 of user <Contact> on Single User Profile popover
    And I click reset session on the Device Detail popover
    And I click People button in one to one conversation
    And Contact <Contact> sends encrypted message <Message3> via device Device1 to user Myself
    Then I see text message <Message3>

    Examples:
      | Email      | Password      | Name      | Contact   | UNABLE_TO_DECRYPT | Message1    | Message2     | Message3    |
      | user1Email | user1Password | user1Name | user2Name | UNABLE TO DECRYPT | First hello | Second hello | Third hello |

  @C82813 @e2ee @staging
  Scenario Outline: Verify you can recover from a broken session between your own devices
    Given There are 2 users where <Name> is me
    Given user <Name> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I see the history info page
    And I click confirm on history info page
    And I am signed in properly
    And I open conversation with <Contact>
    And Contact <Name> sends encrypted message <Message1> via device Device1 to user <Contact>
    Then I see text message <Message1>
    When I break the session with device Device1 of user <Name>
    And Contact <Name> sends encrypted message <Message2> via device Device1 to user <Contact>
    Then I see <UNABLE_TO_DECRYPT> action in conversation
    When I open self profile
    And I wait for 2 seconds
    And I click gear button on self profile page
    And I select Settings menu item on self profile page
    And I wait for 2 seconds
    Then I see a device named Device1 in the devices section
    When I click on the device Device1 in the devices section
    Then I see a device named Device1 with label Label1 in the device details
    When I click the reset session button
    When I click close settings page button
    And I wait for 2 seconds
    And I open conversation with <Contact>
    And Contact <Name> sends encrypted message <Message3> via device Device1 to user <Contact>
    Then I see text message <Message3>

    Examples:
      | Email      | Password      | Name      | Contact   | UNABLE_TO_DECRYPT | Message1    | Message2     | Message3    |
      | user1Email | user1Password | user1Name | user2Name | UNABLE TO DECRYPT | First hello | Second hello | Third hello |

  @C82514 @e2ee @staging
  Scenario Outline: Verify you get no decryption errors when receiving messages on load
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given user <Name> adds a new device Device2 with label Label2
    Given user <Name> adds a new device Device3 with label Label3
    Given user <Name> adds a new device Device4 with label Label4
    Given user <Name> adds a new device Device5 with label Label5
    Given user <Name> adds a new device Device6 with label Label6
    Given user <Contact> adds a new device Device7 with label Label7
    Given user <Name> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I see the history info page
    And I click confirm on history info page
    And I am signed in properly
    And I open conversation with <Contact>
    And Contact <Name> sends encrypted message <StartMessage> via device Device1 to user <Contact>
    Then I see text message <StartMessage>
    When I remember current page
    And I navigate to download page
    And Contact <Contact> sends 50 encrypted messages with prefix <PREFIX1> via device Device1 to user <Name>
    And Contact <Name> sends 50 encrypted messages with prefix <PREFIX2> via device Device1 to user <Contact>
    And I navigate to previously remembered page
    And Contact <Contact> sends 60 encrypted messages with prefix <PREFIX3> via device Device1 to user <Name>
    And Contact <Name> sends 60 encrypted messages with prefix <PREFIX4> via device Device1 to user <Contact>
    And I am signed in properly
    And I open conversation with <Contact>
    And I wait for 30 seconds
    Then I do not see <UNABLE_TO_DECRYPT> action in conversation
    And I see text message First10
    And I see text message First20
    And I see text message First30
    And I see text message First40
    And I see text message Second10
    And I see text message Second20
    And I see text message Second30
    And I see text message Second40
    And I see text message Third10
    And I see text message Third20
    And I see text message Third30
    And I see text message Third40
    And I see text message Third50
    And I see text message Four10
    And I see text message Four20
    And I see text message Four30
    And I see text message Four40
    And I see text message Four50

    Examples:
      | Email      | Password      | Name      | Contact   | UNABLE_TO_DECRYPT | StartMessage | PREFIX1 | PREFIX2 | PREFIX3 | PREFIX4 |
      | user1Email | user1Password | user1Name | user2Name | UNABLE TO DECRYPT | Let's start  | First   | Second  | Third   | Four    |