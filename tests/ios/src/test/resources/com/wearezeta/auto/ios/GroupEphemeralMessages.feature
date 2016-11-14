Feature: Group Ephemeral Messages

  @C318636 @rc @regression @fastLogin
  Scenario Outline: Verify sending ephemeral picture
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    When I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    Then I tap Confirm button on Picture preview page
    # wait to make the transition and image arrival
    And I wait for 3 seconds
    When I remember asset container state at cell 1
    And I wait for <Timeout> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Timeout | DeviceName |
      | user1Name | user2Name | user3Name | Epheme grp    | 15      | device2    |

  @C320772 @regression @fastLogin
  Scenario Outline: ZIOS-7568 Verify timer is applied to the all messages until turning it off
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I type the default message and send it
    # Need to tap on message due to ZIOS-7568. Step should be delete once bug is fixed.
    Given I tap default message in conversation view
    Given I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    Given I tap Add Picture button from input tools
    Given I accept alert if visible
    Given I accept alert if visible
    Given I select the first picture from Keyboard Gallery
    Given I tap Confirm button on Picture preview page
    # Need to tap on message due to ZIOS-7568. Step should be delete once bug is fixed.
    Given I tap on image in conversation view
    Given I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    When I tap Time Indicator button in conversation view
    And I set ephemeral messages expiration timer to Off
    And I type the "<TestMsg>" message and send it
    # Need to tap on message due to ZIOS-7568. Step should be delete once bug is fixed.
    And I tap "<TestMsg>" message in conversation view
    Then I do not see "<EphemeralTimeLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Timer | EphemeralTimeLabel | TestMsg |
      | user1Name | user2Name | user3Name | Epheme grp    | 15    | seconds            | hi      |

  @C320773 @regression @fastLogin
  Scenario Outline: Verify ephemeral messages are not sent to my other devices
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    When User Myself remembers the recent message from group conversation <GroupChatName> via device <DeviceName>
    And I type the default message and send it
    Then User Myself sees the recent message from group conversation <GroupChatName> via device <DeviceName> is not changed in 5 seconds

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Timer | DeviceName |
      | user1Name | user2Name | user3Name | Epheme grp    | 15    | myDevice2  |

  @C318629 @rc @regression @fastLogin
  Scenario Outline: Verify the message is deleted on the sender side when it's read on the receiver side
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    Given I type the default message and send it
    When I remember the recent message from user Myself in the local database
    And I wait for <Timeout> seconds
    And I verify the remembered message has been changed in the local database
    And User <Contact1> reads the recent message from group conversation <GroupChatName>
    # Wait until read message action propagates
    And I wait for 3 seconds
    Then I see 0 messages in the conversation view
    And I verify the remembered message has been deleted from the local database

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Timeout | ContactDevice |
      | user1Name | user2Name | user3Name | Epheme grp    | 5       | d1            |

  @C318634 @regression @fastLogin
  Scenario Outline: Verify the message is deleted on the receiver side when timer is over
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> switches group conversation <GroupChatName> to ephemeral mode with <Timeout> seconds timeout
    Given I sign in using my email or phone number
    Given User <Contact1> sends encrypted message "<Message>" to group conversation <GroupChatName>
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    When I see the conversation view contains message <Message>
    And I wait for <Timeout> seconds
    Then I do not see the conversation view contains message <Message>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Timeout | Message |
      | user1Name | user2Name | user3Name | Epheme grp    | 15      | m1      |

  @C320786 @rc @regression @fastLogin
  Scenario Outline: Verify receiving ephemeral assets (picture, video, audio, link preview, location)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on group chat with name <GroupChatName>
    Given User <Contact1> switches group conversation <GroupChatName> to ephemeral mode with <EphemeralTimeout> seconds timeout
    # Picture
    When User <Contact1> sends encrypted image <Picture> to group conversation <GroupChatName>
    And I wait for <SyncTimeout> seconds
    And I see 1 photo in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I see 0 photos in the conversation view
    # Video
    When User <Contact1> sends file <FileName> having MIME type <VideoMIME> to group conversation <GroupChatName> using device <DeviceName>
    And I wait for <SyncTimeout> seconds
    And I see video message container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see video message container in the conversation view
    # Audio
    When User <Contact1> sends file <AudioFileName> having MIME type <AudioMIME> to group conversation <GroupChatName> using device <DeviceName>
    And I wait for <SyncTimeout> seconds
    And I see audio message container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see audio message container in the conversation view
    # Link Preview
    When User <Contact1> switches user <Contact1> to ephemeral mode with 15 seconds timeout
    And User <Contact1> sends encrypted message "<Link>" to group conversation <GroupChatName>
    And I wait for <SyncTimeout> seconds
    And I see link preview container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    And I see 0 message in the conversation view
    Then I do not see link preview container in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | SyncTimeout | EphemeralTimeout | DeviceName    | GroupChatName | Picture     | FileName    | VideoMIME | AudioFileName | AudioMIME | Link                    |
      | user1Name | user2Name | user3Name | 3           | 15               | ContactDevice | Epheme grp    | testing.jpg | testing.mp4 | video/mp4 | test.m4a      | audio/mp4 | check this www.wire.com |

  @C320784 @rc @regression @fastLogin
  Scenario Outline: Verify the message is not deleted for users that didn't read the message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> adds new device <DeviceName1>
    Given User <Contact2> adds new device <DeviceName2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> switches group conversation <GroupChatName> to ephemeral mode with <Timeout> seconds timeout
    Given User <Contact1> sends encrypted message "<Message>" to group conversation <GroupChatName>
    Given User <Contact1> remembers the recent message from group conversation <GroupChatName> via device <DeviceName1>
    Given User <Contact2> remembers the recent message from group conversation <GroupChatName> via device <DeviceName2>
    Given User <Contact2> reads the recent message from group conversation <GroupChatName>
    Given I wait for <Timeout> seconds
    Given User <Contact1> sees the recent message from group conversation <GroupChatName> via device <DeviceName1> is changed in <Timeout> seconds
    Given User <Contact2> sees the recent message from group conversation <GroupChatName> via device <DeviceName2> is changed in <Timeout> seconds
    When I tap on group chat with name <GroupChatName>
    Then I see the conversation view contains message <Message>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Timeout | Message | DeviceName1 | DeviceName2 |
      | user1Name | user2Name | user3Name | Epheme grp    | 5       | m1      | DeviceName1 | DeviceName2 |
