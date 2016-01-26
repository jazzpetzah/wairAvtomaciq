Feature: E2EE

  @C3226 @rc @regression
  Scenario Outline: Verify you can receive encrypted and non-encrypted messages in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When Contact <Contact> sends encrypted message <EncryptedMessage> to user Myself
    And Contact <Contact> sends message <SimpleMessage> to user Myself
    And I tap on contact name <Contact>
    Then I see non-encrypted message <SimpleMessage> 1 time in the conversation view
    And I see encrypted message <EncryptedMessage> 1 time in the conversation view

    Examples:
      | Name      | Contact   | EncryptedMessage | SimpleMessage |
      | user1Name | user2Name | EncryptedYo      | SimpleYo      |

  @C1847 @regression
  Scenario Outline: Verify you can remove extra devices and log in successfully if too many devices are registered for your account
    Given There is 1 user where <Name> is me
    Given User <Name> adds new devices <DeviceToRemove>,Device2,Device3,Device4,Device5,Device6,Device7
    # Workaround for AN-3281
    # Given I sign in using my email or phone number
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    When I see Manage Devices overlay
    And I tap Manage Devices button on Manage Devices overlay
    And I select "<DeviceToRemove>" settings menu item
    And I select "Remove device" settings menu item
    And I see device removal password confirmation dialog
    And I enter <Password> into the device removal password confirmation dialog
    And I tap OK button on the device removal password confirmation dialog
    And I do not see "<DeviceToRemove>" settings menu item
    And I press Back button 3 times
    When I do not see Manage Devices overlay
    Then I see Contact list with no contacts

    Examples:
      | Password      | Name      | DeviceToRemove |
      | user1Password | user1Name | Device1        |

  @C3227 @rc @regression
  Scenario Outline: Verify you can receive encrypted and non-encrypted images in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    And User <Contact> sends image <ImageName> to single user conversation Myself
    And I tap on contact name <Contact>
    And I scroll to the bottom of conversation view
    Then I see non-encrypted image 1 time in the conversation view
    And I see encrypted image 1 time in the conversation view

    Examples:
      | Name      | Contact   | ImageName   |
      | user1Name | user2Name | testing.jpg |

  @C3241 @staging
  Scenario Outline: Verify you can receive encrypted and non-encrypted messages in group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When Contact <Contact1> sends encrypted message <EncryptedMessage> to group conversation <GroupChatName>
    And Contact <Contact2> sends message <SimpleMessage> to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    Then I see non-encrypted message <SimpleMessage> 1 time in the conversation view
    And I see encrypted message <EncryptedMessage> 1 time in the conversation view

    Examples:
      | Name      | Contact1   | Contact2  | EncryptedMessage | SimpleMessage | GroupChatName |
      | user1Name | user2Name  | user3Name | EncryptedYo      | SimpleYo      | HybridGroup   |

  @C3234 @staging
  Scenario Outline: Verify you receive encrypted content in 1:1 conversation after switching online
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And Contact <Contact1> sends encrypted message <Message1> to user Myself
    Then Last message is <Message1>
    When I enable Airplane mode on the device
    And User <Contact1> sends encrypted image <Picture> to single user conversation Myself
    Then I do not see new picture in the dialog
    When Contact <Contact1> sends encrypted message <Message2> to user Myself
    Then Last message is <Message1>
    When I disable Airplane mode on the device
    And I scroll to the bottom of conversation view
    Then Last message is <Message2>
    And I see new picture in the dialog

    Examples:
      | Name      | Contact1  | Message1 | Message2 | Picture     |
      | user1Name | user2Name | Msg1     | Msg2     | testing.jpg |

  @C3235 @staging
  Scenario Outline: Verify you can receive encrypted content in group conversation after switching online
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And Contact <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    Then Last message is <Message1>
    When I enable Airplane mode on the device
    And User <Contact1> sends encrypted image <Picture> to group conversation <GroupChatName>
    Then I do not see new picture in the dialog
    When Contact <Contact2> sends encrypted message <Message2> to group conversation <GroupChatName>
    Then Last message is <Message1>
    When I disable Airplane mode on the device
    And I scroll to the bottom of conversation view
    Then Last message is <Message2>
    And I see new picture in the dialog

    Examples:
      | Name      | Contact1  | Contact2  | Message1 | Message2 | Picture     | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | Msg2     | testing.jpg | GroupConvo    |

  @C3242 @staging
  Scenario Outline: Verify you can receive encrypted and non-encrypted images in group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted image <ImageName> to group conversation <GroupChatName>
    And User <Contact1> sends image <ImageName2> to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    And I scroll to the bottom of conversation view
    Then I see non-encrypted image 1 time in the conversation view
    And I see encrypted image 1 time in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | ImageName   | ImageName2    | GroupChatName |
      | user1Name | user2Name | user3Name | testing.jpg | hdpicture.jpg | GroupConvo    |

  @C3229 @staging
  Scenario Outline: Verify you can see device ids of the other conversation participant in participant details view inside a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When Contact <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    And Contact <Contact2> sends encrypted message <Message1> to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    And I tap conversation details button
    And I select contact <Contact1>
    And I select single participant tab "Devices"
    Then 1 devices are shown in single participant devices tab
    And I verify all device ids of user <Contact1> are shown in single participant devices tab
    When I close single participant page by UI button
    And I select contact <Contact2>
    And I select single participant tab "Devices"
    Then 1 devices are shown in single participant devices tab
    And I verify all device ids of user <Contact2> are shown in single participant devices tab

    Examples:
      | Name      | Contact1  | Contact2  | Message1 | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | GroupConvo    |

  @C3228 @staging
  Scenario Outline: Verify you can see device ids of the other conversation participant in 1:1 conversation details
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When Contact <Contact1> sends encrypted message <Message1> to user Myself
    And I tap on contact name <Contact1>
    And I tap conversation details button
    And I select single participant tab "Devices"
    Then 1 devices are shown in single participant devices tab
    And I verify all device ids of user <Contact1> are shown in single participant devices tab

    Examples:
      | Name      | Contact1  | Message1 |
      | user1Name | user2Name | Msg1     |

