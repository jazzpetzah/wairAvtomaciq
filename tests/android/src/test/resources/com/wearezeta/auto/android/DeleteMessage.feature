Feature: Delete Message

  @C111638 @regression @rc @C111637
  Scenario Outline: Verify deleting own text message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message1>" and send it
    And I type the message "<Message2>" and send it
    When I long tap the Text message "<Message1>" in the conversation view
    Then I see Copy button on the action mode bar
    And I see Delete button on the action mode bar
    When I tap the Text message "<Message2>" in the conversation view
    Then I do not see Copy button on the action mode bar
    When I tap Delete button on the action mode bar
    And I see alert message containing "<AlertText>" in the title
    And I tap Delete button on the alert
    Then I do not see the message "<Message1>" in the conversation view
    And I do not see the message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1 | Message2 | AlertText       |
      | user1Name | user2Name | Yo1      | Yo2      | Delete messages |

  @C111644 @regression @rc
  Scenario Outline: Verify deleting is synchronised across own devices when they are online
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User Myself adds new device <Device>
    Given User <Contact1> adds new device <ContactDevice>
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And User Myself send encrypted message "<Message>" via device <Device> to user <Contact1>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message from user <Contact1> via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    When I tap Back button from top toolbar
    And I tap on contact name <GroupChatName>
    And User Myself send encrypted message "<Message>" via device <Device> to group conversation <GroupChatName>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message from group conversation <GroupChatName> via device <Device>
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message           | Device  | ContactDevice | GroupChatName |
      | user1Name | user2Name | user3Name | DeleteTextMessage | Device1 | Device2       | MyGroup       |

  @C111641 @regression @rc
  Scenario Outline: Verify deleting the media file (sound could, youtube)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<YoutubeLink>" and send it
    And I type the message "<SoundcloudLink>" and send it
    And I hide keyboard
    When I scroll down the conversation view
    And I long tap Youtube container in the conversation view
    And I scroll up the conversation view
    And I tap Soundcloud container in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see the message "<YoutubeLink>" in the conversation view
    And I do not see Youtube container in the conversation view
    And I do not see the message "<SoundcloudLink>" in the conversation view
    And I do not see Soundcloud container in the conversation view

    Examples:
      | Name      | Contact   | YoutubeLink                                 | SoundcloudLink                                                      |
      | user1Name | user2Name | https://www.youtube.com/watch?v=gIQS9uUVmgk | https://soundcloud.com/scottisbell/scott-isbell-tonight-feat-adessi |

  @C111639 @regression @rc
  Scenario Outline: Verify deleting received text message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And User <Contact> send encrypted message "<Message>" to user Myself
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message           |
      | user1Name | user2Name | DeleteTextMessage |

  @C111643 @regression @rc
  Scenario Outline: Verify deleting ping
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Ping button from cursor toolbar
    And User <Contact> securely pings conversation Myself
    And I see Ping message "<Message2>" in the conversation view
    And I long tap the Ping message "<Message1>" in the conversation view
    And I tap the Ping message "<Message2>" in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see Ping message "<Message1>" in the conversation view
    And I do not see Ping message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1   | CallBackend | Message2         |
      | user1Name | user2Name | You pinged | autocall    | user2Name pinged |

  @C111642 @regression @rc
  Scenario Outline: AN-4171 Verify deleting the shared file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact1>
    And I tap File button from cursor toolbar
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    When I long tap File Upload container in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see File Upload container in the conversation view

    Examples:
      | Name      | Contact1  | FileName  | FileExtension | FileSize | UploadingTimeout |
      | user1Name | user2Name | qa_random | txt           | 1.00MB   | 20               |

  @C111645 @regression @rc @C111647
  Scenario Outline: (AN-3934) Verify deleting is synchronised across own devices when one of them was offline
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    When User Myself adds new device <Device>
    And User <Contact1> adds new device <ContactDevice>
    And I see Contact list with contacts
    And I tap on contact name <Contact1>
    And User Myself send encrypted message "<Message>" via device <Device> to user <Contact1>
    And I tap Back button from top toolbar
    And I tap on contact name <GroupChatName>
    And User Myself send encrypted message "<Message>" via device <Device> to group conversation <GroupChatName>
    # The following step should be delete , which is blocked by AN-3934
    And I tap Back button from top toolbar
    And I enable Airplane mode on the device
    And User Myself deletes the recent message from user <Contact1> via device <Device>
    And User Myself deletes the recent message from group conversation <GroupChatName> via device <Device>
    And I disable Airplane mode on the device
    # Wait for sync
    And I wait for 10 seconds
    # This line also should be deleted when AN-3934 fixed
    And I tap on contact name <GroupChatName>
    Then I do not see the message "<Message>" in the conversation view
    When I tap Back button from top toolbar
    And I tap on contact name <Contact1>
    Then I do not see the message "<Message>" in the conversation view
    When I type the message "<Message2>" and send it
    And User Myself remember the recent message from user <Contact1> via device <Device>
    And I enable Airplane mode on the device
    And I long tap the Text message "<Message2>" in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see the message "<Message2>" in the conversation view
    When I disable Airplane mode on the device
    And I wait for 10 seconds
    Then User Myself see the recent message from user <Contact1> via device <Device> is changed

    Examples:
      | Name      | Contact1  | Contact2  | Message           | Device  | ContactDevice | GroupChatName | Message2  |
      | user1Name | user2Name | user3Name | DeleteTextMessage | Device1 | Device2       | MyGroup       | MyMessage |

  @C111640 @regression @rc
  Scenario Outline: Verify deleting the picture, gif from Giphy
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap on text input
    And I type the message "<Message>"
    And I click on the GIF button
    Then I see giphy preview page
    When I click on the giphy send button
    Then I see a picture in the conversation view
    When I long tap the recent picture in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see any pictures in the conversation view
    And I see the message "<Message> · via giphy.com" in the conversation view

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C123605 @regression
  Scenario Outline: Verify delete unsent message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I enable Airplane mode on the device
    And I type the message "<Message>" and send it
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see the message "<Message>" in the conversation view
    And I disable Airplane mode on the device

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |


  @C131212 @rc @regression
  Scenario Outline: Verify deleting audio message after upload
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap audio recording Send button
    # Wait for the audio to be fully uploaded
    And I wait for 5 seconds
    And I long tap Audio Message container in the conversation view
    Then I do not see Copy button on the action mode bar
    When I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131221 @regression
  Scenario Outline: Verify deleting failed to upload voice message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I enable Airplane mode on the device
    And I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap audio recording Send button
    And I long tap Audio Message container in the conversation view
    Then I do not see Copy button on the action mode bar
    When I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131200 @C131201 @regression
  Scenario Outline: Verify deleting failed to downloaded voice message (+ delete audio message sent by file transfer)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    And I see Audio Message container in the conversation view
    And I wait for 5 seconds
    And I enable Airplane mode on the device
    And I tap Play button on the recent audio message in the conversation view
    And I long tap Audio Message container in the conversation view
    Then I do not see Copy button on the action mode bar
    When I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    |

  @C150030 @regression
  Scenario Outline: Verify you can delete Share Location placeholder from conversation view
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> shares his location to user Myself via device <DeviceName>
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Share Location container in the conversation view
    And I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see Share Location container in the conversation view

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |
