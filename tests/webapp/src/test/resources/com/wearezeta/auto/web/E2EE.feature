Feature: E2EE

  @C1846 @e2ee
  Scenario Outline: Remove remote device from device list
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I enter email "<Email>"
    Given I enter password "<Password>"
    Given I check option to remember me
    Given user <Name> adds a new device <Device> with label <Label>
    Given I press Sign In button
    And I see the history info page
    And I click confirm on history info page
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

  @C1847 @e2ee
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

  @C2100 @e2ee
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
    And I see the history info page
    And I click confirm on history info page
    Then I am signed in properly
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

  @C2098 @e2ee
  Scenario Outline: Verify current browser is set as permanent device
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I enter email "<Email>"
    Given I enter password "<Password>"
    When I check option to remember me
    And I press Sign In button
    And I see the history info page
    And I click confirm on history info page
    Then I am signed in properly
    When I click gear button on self profile page
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
    Then I am signed in properly
    And I click gear button on self profile page
    When I select Settings menu item on self profile page
    Then I verify that the device id of the current device is the same
    And I see 0 devices in the devices section

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C2099 @e2ee
  Scenario Outline: Verify current browser is set as temporary device
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I see the history info page
    And I click confirm on history info page
    Then I am signed in properly
    When I click gear button on self profile page
    And I select Settings menu item on self profile page
    And I remember the device id of the current device
    And I click close settings page button
    And I wait for 2 seconds
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see Sign In page
    And I Sign in using login <Email> and password <Password>
    And I see the history info page
    And I click confirm on history info page
    Then I am signed in properly
    When I click gear button on self profile page
    And I select Settings menu item on self profile page
    Then I verify that the device id of the current device is not the same
    And I see 0 devices in the devices section

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C12068 @e2ee
  Scenario Outline: Verify you can receive encrypted messages in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When Contact <Contact> sends encrypted message <EncryptedMessage> to user Myself
    Then I see text message <EncryptedMessage>

    Examples:
      | Email      | Password      | Name      | Contact   | EncryptedMessage |
      | user1Email | user1Password | user1Name | user2Name | EncryptedYo      |

  @C12069 @e2ee
  Scenario Outline: Verify you can receive encrypted and non-encrypted images in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    Then I see sent picture <ImageName> in the conversation view

    Examples:
      | Email      | Password      | Name      | Contact   | ImageName                |
      | user1Email | user1Password | user1Name | user2Name | userpicture_portrait.jpg |

  @C12043 @e2ee
  Scenario Outline: Verify you can receive encrypted messages in group chat
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to Myself
    Given <Contact2> is connected to Myself
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <GroupChatName>
    And Contact <Contact1> sends encrypted message <EncryptedMessage> to group conversation <GroupChatName>
    Then I see text message <EncryptedMessage>

    Examples:
      | Email      | Password      | Name      | Contact1   | Contact2  | EncryptedMessage | GroupChatName |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | EncryptedYo      | HybridGroup   |

  @C12044 @e2ee
  Scenario Outline: Verify you can receive encrypted messages in group chat
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to Myself
    Given <Contact2> is connected to Myself
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <GroupChatName>
    And User <Contact1> sends encrypted image <ImageName> to group conversation <GroupChatName>
    Then I see text message <EncryptedMessage>

    Examples:
      | Email      | Password      | Name      | Contact1   | Contact2  | ImageName                | GroupChatName |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | userpicture_portrait.jpg | HybridGroup   |

  @C12050 @e2ee
  Scenario Outline: Verify you receive encrypted content in 1:1 conversation after switching online
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I switch to Sign In page
    Given I enter email "<Email>"
    Given I enter password "<Password>"
    Given I check option to remember me
    Given I Sign in using login <Email> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    When I am signed in properly
    And I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And Contact <Contact> sends encrypted message <EncryptedMessage> to user Myself
    And User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    And I see Sign In page
    And I enter email "<Email>"
    And I enter password "<Password>"
    And I check option to remember me
    And I Sign in using login <Email> and password <Password>
    Then I see text message <EncryptedMessage>
    And I see sent picture <ImageName> in the conversation view

    Examples:
      | Email      | Password      | Name      | Contact   | EncryptedMessage | ImageName                |
      | user1Email | user1Password | user1Name | user2Name | EncryptedYo      | userpicture_portrait.jpg |

  @C12045 @e2ee
  Scenario Outline: Verify you can see device ids of the other conversation participant in 1:1 conversation details
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
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

    Examples:
      | Email      | Password      | Name      | Contact   | Message1                                | Message2                              |
      | user1Email | user1Password | user1Name | user2Name | user is not using the encrypted version | Every device has a unique fingerprint |