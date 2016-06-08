Feature: E2EE

  @C3226 @rc @regression
  Scenario Outline: Verify you can receive encrypted and non-encrypted messages in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact> sends encrypted message <EncryptedMessage> to user Myself
    And User <Contact> sends message <SimpleMessage> to user Myself
    And I tap on contact name <Contact>
    Then I see message <SimpleMessage> 1 time in the conversation view
    And I see message <EncryptedMessage> 1 time in the conversation view

    Examples: 
      | Name      | Contact   | EncryptedMessage | SimpleMessage |
      | user1Name | user2Name | EncryptedYo      | SimpleYo      |

  @C3230 @regression
  Scenario Outline: Verify you can remove extra devices and log in successfully if too many devices are registered for your account
    Given There is 1 user where <Name> is me
    Given User <Name> adds new devices <DeviceToRemove>,<OtherDevice>,Device3,Device4,Device5,Device6,Device7
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
    # Delete device will take time, should verify at first it already return back to device list view
    And I see "<OtherDevice>" settings menu item
    And I do not see "<DeviceToRemove>" settings menu item
    And I press Back button 3 times
    When I do not see Manage Devices overlay
    Then I see Contact list with no contacts

    Examples: 
      | Password      | Name      | DeviceToRemove | OtherDevice |
      | user1Password | user1Name | Device1        | Device2     |

  @C3227 @rc @regression
  Scenario Outline: Verify in latest version you only can receive encrypted images in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    And User <Contact> sends image <ImageName> to single user conversation Myself
    And I tap on contact name <Contact>
    And I scroll to the bottom of conversation view
    Then I see 1 image in the conversation view

    Examples: 
      | Name      | Contact   | ImageName   |
      | user1Name | user2Name | testing.jpg |

  @C3234 @rc @regression
  Scenario Outline: Verify you receive encrypted content in 1:1 conversation after switching online
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And User <Contact1> sends encrypted message <Message1> to user Myself
    Then I see the most recent conversation message is "<Message1>"
    When I enable Airplane mode on the device
    And User <Contact1> sends encrypted image <Picture> to single user conversation Myself
    Then I do not see any pictures in the conversation view
    When User <Contact1> sends encrypted message <Message2> to user Myself
    Then I see the most recent conversation message is "<Message1>"
    When I disable Airplane mode on the device
    # Wait for sync
    And I wait for 10 seconds
    And I scroll to the bottom of conversation view
    Then I see the most recent conversation message is "<Message2>"
    And I see a picture in the conversation view

    Examples: 
      | Name      | Contact1  | Message1 | Message2 | Picture     |
      | user1Name | user2Name | Msg1     | Msg2     | testing.jpg |

  @C3235 @rc @regression
  Scenario Outline: Verify you can receive encrypted content in group conversation after switching online
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And User <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    Then I see the most recent conversation message is "<Message1>"
    When I enable Airplane mode on the device
    And User <Contact1> sends encrypted image <Picture> to group conversation <GroupChatName>
    Then I do not see any pictures in the conversation view
    When User <Contact2> sends encrypted message <Message2> to group conversation <GroupChatName>
    Then I see the most recent conversation message is "<Message1>"
    When I disable Airplane mode on the device
    # Wait for sync
    And I wait for 10 seconds
    And I scroll to the bottom of conversation view
    Then I see the most recent conversation message is "<Message2>"
    And I see a picture in the conversation view

    Examples: 
      | Name      | Contact1  | Contact2  | Message1 | Message2 | Picture     | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | Msg2     | testing.jpg | GroupConvo    |

  @C3241 @regression
  Scenario Outline: Verify you can receive encrypted and non-encrypted messages in group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message <EncryptedMessage> to group conversation <GroupChatName>
    And User <Contact2> sends message <SimpleMessage> to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    Then I see message <SimpleMessage> 1 time in the conversation view
    And I see message <EncryptedMessage> 1 time in the conversation view

    Examples: 
      | Name      | Contact1  | Contact2  | EncryptedMessage | SimpleMessage | GroupChatName |
      | user1Name | user2Name | user3Name | EncryptedYo      | SimpleYo      | HybridGroup   |

  @C3242 @regression
  Scenario Outline: Verify you can only receive encrypted images in group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted image <ImageName> to group conversation <GroupChatName>
    And User <Contact1> sends image <ImageName> to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    And I scroll to the bottom of conversation view
    Then I see 1 image in the conversation view

    Examples: 
      | Name      | Contact1  | Contact2  | ImageName   | GroupChatName |
      | user1Name | user2Name | user3Name | testing.jpg | GroupConvo    |

  @C3229 @rc @regression
  Scenario Outline: Verify you can see device ids of the other conversation participant in participant details view inside a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    And User <Contact2> sends encrypted message <Message1> to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    And I tap conversation name from top toolbar
    And I select contact <Contact1>
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify all device ids of user <Contact1> are shown in single participant devices tab
    When I close single participant page by UI button
    And I select contact <Contact2>
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify all device ids of user <Contact2> are shown in single participant devices tab

    Examples: 
      | Name      | Contact1  | Contact2  | Message1 | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | GroupConvo    |

  @C3228 @rc @regression
  Scenario Outline: Verify you can see device ids of the other conversation participant in 1:1 conversation details
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message <Message1> to user Myself
    And I tap on contact name <Contact1>
    And I tap conversation name from top toolbar
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify all device ids of user <Contact1> are shown in single participant devices tab

    Examples: 
      | Name      | Contact1  | Message1 |
      | user1Name | user2Name | Msg1     |

  @C3232 @regression
  Scenario Outline: Verify the device id is not changed after relogin
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message <EncMessage> to user Myself
    And I tap on contact name <Contact1>
    Then I see message <EncMessage> 1 times in the conversation view
    When I press back button
    And I tap conversations list settings button
    And I tap options button
    And I tap settings button
    When I select "Privacy & Security" settings menu item
    And I select "Devices" settings menu item
    And I tap current device in devices settings menu
    Then I remember the device id shown in the device detail view
    When I press back button
    And I press back button
    And I press back button
    When I select "Account" settings menu item
    And I select "Log out" settings menu item
    Then I confirm sign out
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    And I see Contact list with contacts
    When I tap on contact name <Contact1>
    Then I see message <EncMessage> 1 times in the conversation view
    When I press back button
    And I tap conversations list settings button
    And I tap options button
    And I tap settings button
    When I select "Privacy & Security" settings menu item
    And I select "Devices" settings menu item
    And I tap current device in devices settings menu
    Then I verify the remembered device id is shown in the device detail view

    Examples: 
      | Name      | Contact1  | EncMessage |
      | user1Name | user2Name | Bla        |

  @C3236 @rc @regression
  Scenario Outline: Verify newly added people in a group conversation don't see a history
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to Myself,<Contact2>,<Contact3>
    Given <Contact1> has group chat <GroupChatName> with <Contact2>,<Contact3>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message <EncMessage> to group conversation <GroupChatName>
    And User <Contact2> sends encrypted message <EncMessage> to group conversation <GroupChatName>
    And User <Contact3> sends encrypted message <EncMessage> to group conversation <GroupChatName>
    And I wait for 5 seconds
    And User <Contact1> adds user Myself to group chat <GroupChatName>
    And I tap on contact name <GroupChatName>
    Then I see message <EncMessage> 0 times in the conversation view

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | GroupChatName | EncMessage |
      | user1Name | user2Name | user3Name | user4Name | EncryptedGrp  | Bla        |

  @C3231 @rc @regression
  Scenario Outline: Verify the appropriate device is signed out if you remove it from settings
    Given There are 1 users where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    When User Myself removes all his registered OTR clients
    Then I see welcome screen
    And I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible

    Examples: 
      | Name      |
      | user1Name |

  @C3514 @regression
  Scenario Outline: On first login on 2nd device there should be an explanation that user will not see previous messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    When User <Name> sends encrypted message <EncMessage> to user <Contact1>
    Then I sign in using my email
    And I see First Time overlay
    When I tap Got It button on First Time overlay
    Then I see Contact list with contacts
    When I tap on contact name <Contact1>
    Then I see message <EncMessage> 0 times in the conversation view

    Examples: 
      | Name      | Contact1  | EncMessage |
      | user1Name | user2Name | Bla        |

  @C3515 @C3237 @regression
  Scenario Outline: Verify green shield showed in other user profile when I verify all his devices
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message <Message1> to user Myself
    And I tap on contact name <Contact1>
    And I tap conversation name from top toolbar
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I remember state of 1st device
    And I verify 1st device
    And I see state of 1st device is changed
    Then I see shield in participant profile

    Examples: 
      | Name      | Contact1  | Message1 |
      | user1Name | user2Name | Msg1     |

  @C3238 @regression @rc
  Scenario Outline: Verify you see an alert in verified 1:1 conversation when the other participants types something from non-verified device
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message "<Message1>" to user Myself
    And I tap on contact name <Contact1>
    And I tap conversation name from top toolbar
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify 1st device
    When I press back button
    Then I see a message informing me conversation is verified
    And User <Contact1> adds new device Device2
    When User <Contact1> sends encrypted message "<Message1>" via device Device2 to user Myself
    Then I see a message informing me conversation is not verified caused by user <Contact1>

    Examples: 
      | Name      | Contact1  | Message1 |
      | user1Name | user2Name | Msg1     |

  @C3240 @rc @regression
  Scenario Outline: Verify you get an alert if group conversation participant sends a message from non-verified device
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message "<Message1>" to group conversation <GroupChatName>
    When User <Contact2> sends encrypted message "<Message1>" to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    And I tap conversation name from top toolbar
    And I select contact <Contact1>
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify 1st device
    And I press back button
    And I select contact <Contact2>
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify 1st device
    And I press back button
    And I press back button
    Then I see a message informing me conversation is verified
    And User <Contact1> adds new device Device2
    When User <Contact1> sends encrypted message "<Message1>" via device Device2 to group conversation <GroupChatName>
    Then I see a message informing me conversation is not verified caused by user <Contact1>

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | Message1 |
      | user1Name | user2Name | user3Name | EncryptedGrp  | Msg1     |

  @C12083 @regression
  Scenario Outline: When I'm entering a verified conversation, a green shield will appear at the bottom right
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message "<Message1>" to user Myself
    And I tap on contact name <Contact1>
    And I remember verified conversation shield state
    And I tap conversation name from top toolbar
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify 1st device
    When I press back button
    Then I see a message informing me conversation is verified
    And I see verified conversation shield state has changed

    Examples: 
      | Name      | Contact1  | Message1 |
      | user1Name | user2Name | Msg1     |

  @C3516 @regression
  Scenario Outline: User should appear in verified list in group conversations details when all of his fingerprints are verified
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> adds new devices Device1,Device2
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact2> sends encrypted message "<Message1>" to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    And I tap conversation name from top toolbar
    And I select contact <Contact1>
    And I select single participant tab "Devices"
    Then I see 2 devices is shown in single participant devices tab
    When I verify 1st device
    And I select single participant tab "Devices"
    And I verify 2nd device
    And I press back button
    Then I see the verified participant avatar for <Contact1>
    When I select contact <Contact2>
    And I select single participant tab "Devices"
    When I verify 1st device
    And I press back button
    Then I see the verified participant avatars for <Contact1>,<Contact2>
    When I press back button
    Then I see a message informing me conversation is verified

    Examples: 
      | Name      | Contact1  | Contact2  | Message1 | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | GroupConvo    |

  @C12066 @C3239 @regression @rc
  Scenario Outline: Verify I see system message when verify all other user's device in group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    And User <Contact2> sends encrypted message <Message1> to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    And I tap conversation name from top toolbar
    And I select contact <Contact1>
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify 1st device
    When I close single participant page by UI button
    And I select contact <Contact2>
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify 1st device
    When I close single participant page by UI button
    And I press back button
    And I see a message informing me conversation is verified

    Examples: 
      | Name      | Contact1  | Contact2  | Message1 | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | GroupConvo    |

  @C12082 @regression
  Scenario Outline: First time when group conversation is degraded - I can ignore takeover screen and send message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message <Message1> to group conversation <GroupChatName>
    And User <Contact2> sends encrypted message <Message1> to group conversation <GroupChatName>
    And I tap on contact name <GroupChatName>
    And I tap conversation name from top toolbar
    And I select contact <Contact1>
    And I select single participant tab "Devices"
    And I verify 1st device
    And I close single participant page by UI button
    And I select contact <Contact2>
    And I select single participant tab "Devices"
    And I verify 1st device
    And I close single participant page by UI button
    And I press back button
    Then I see a message informing me conversation is verified
    When User <Contact1> adds new device Device1
    And I tap on text input
    And I type the message "<Message2>" and send it without hiding keyboard
    When I see takeover screen from user "<Contact1>"
    Then I tap send anyway button
    And I do not see takeover screen
    Then I see message <Message2> 1 times in the conversation view

    Examples: 
      | Name      | Contact1  | Contact2  | Message1 | Message2 | GroupChatName |
      | user1Name | user2Name | user3Name | Msg1     | Msg2     | GroupConvo    |

  @C3513 @regression
  Scenario Outline: If user uses only old Wire builds which don't support E2EE I should see system message inside his profile
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    When I tap conversation name from top toolbar
    And I select single participant tab "Devices"
    Then I see no encrypted device text for user <Contact> in header of device detail page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C3512 @regression
  Scenario Outline: After login by phone on not 1st device I have to be asked for email login
    Given There is 1 user where <Name> is me
    Given User Myself adds new device <Device>
    Given I sign in using my phone number
    Then I see forced email login page
    And I have entered login <Email>
    And I have entered password <Password>
    And I press Log in button
    And I accept First Time overlay as soon as it is visible
    Then I see Contact list with no contacts

    Examples: 
      | Name      | Email      | Password      | Device  |
      | user1Name | user1Email | user1Password | device1 |

  @C12081 @regression
  Scenario Outline: When 1:1 conversation was degraded - I can ignore takeover screen and send message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message "<Message1>" to user Myself
    And I tap on contact name <Contact1>
    And I tap conversation name from top toolbar
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify 1st device
    When I press back button
    Then I see a message informing me conversation is verified
    And User <Contact1> adds new device <Device>
    And I tap on text input
    And I type the message "<Message2>"
    And I press Send button
    When I see takeover screen from user "<Contact1>"
    Then I tap send anyway button
    And I do not see takeover screen
    Then I see the message "<Message2>" in the conversation view

    Examples: 
      | Name      | Contact1  | Device  | Message1 | Message2        |
      | user1Name | user2Name | device2 | Msg1     | MsgToSendAnyway |

  @C12065 @regression
  Scenario Outline: (AN-3683) When 1:1 conversation was degraded - I can manage new device to verified and resend message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact1> sends encrypted message "<Message1>" to user Myself
    And I tap on contact name <Contact1>
    And I tap conversation name from top toolbar
    And I select single participant tab "Devices"
    Then I see 1 device is shown in single participant devices tab
    And I verify 1st device
    When I press back button
    Then I see a message informing me conversation is verified
    And User <Contact1> adds new device <Device>
    And I tap on text input
    And I type the message "<Message2>"
    And I press Send button
    When I see takeover screen from user "<Contact1>"
    Then I tap show device button
    And I do not see takeover screen
    # Workaround : tap on devices tab
    And I select single participant tab "Devices"
    #TODO: detect new device and verify it instead of trying to verify each device
    And I verify 1st device
    And I verify 2nd device
    When I press back button
    Then I see a message informing me conversation is verified

    Examples: 
      | Name      | Contact1  | Device  | Message1 | Message2    |
      | user1Name | user2Name | device2 | Msg1     | MsgToResend |
