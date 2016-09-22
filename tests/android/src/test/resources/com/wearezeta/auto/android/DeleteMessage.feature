Feature: Delete Message

  @C111638 @regression @rc @C111637
  Scenario Outline: Verify deleting own text message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "<Message1>" and send it by cursor Send button
    When I long tap the Text message "<Message1>" in the conversation view
    # C111638
    Then I see Copy button on the message bottom menu
    And I see Forward button on the message bottom menu
    And I see Edit button on the message bottom menu
    And I see Delete only for me button on the message bottom menu
    And I see Delete for everyone button on the message bottom menu
    When I tap Delete only for me button on the message bottom menu
    And I see alert message containing pure text "<AlertText>" in the title
    And I tap Delete button on the alert
    # C111637
    Then I do not see the message "<Message1>" in the conversation view

    Examples:
      | Name      | Contact   | Message1 | AlertText           |
      | user1Name | user2Name | Yo1      | Delete only for me? |

  @C111644 @regression @rc
  Scenario Outline: Verify deleting is synchronised across own devices when they are online
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User Myself adds new device <Device>
    Given User <Contact1> adds new device <ContactDevice>
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And User Myself send encrypted message "<Message>" via device <Device> to user <Contact1>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message from user <Contact1> via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    When I tap Back button from top toolbar
    And I tap on conversation name <GroupChatName>
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
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "<YoutubeLink>" and send it by cursor Send button
    And I hide keyboard
    And I long tap Youtube container in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see the message "<YoutubeLink>" in the conversation view
    And I do not see Youtube container in the conversation view
    When I type the message "<SoundcloudLink>" and send it by cursor Send button
    And I hide keyboard
    And I long tap Soundcloud container in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see the message "<SoundcloudLink>" in the conversation view
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
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And User <Contact> send encrypted message "<Message>" to user Myself
    And I long tap the Text message "<Message>" in the conversation view
    And I see Delete only for me button on the message bottom menu
    And I do not see Delete for everyone button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
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
    Given I see Conversations list with conversations
    # My ping
    When I tap on conversation name <Contact>
    And I tap Ping button from cursor toolbar
    And User <Contact> securely pings conversation Myself
    And I see Ping message "<Message2>" in the conversation view
    And I long tap the Ping message "<Message1>" in the conversation view
    Then I see Delete only for me button on the message bottom menu
    And I see Delete for everyone button on the message bottom menu
    When I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Ping message "<Message1>" in the conversation view
    # Other ping
    When I long tap the Ping message "<Message2>" in the conversation view
    And I see Delete only for me button on the message bottom menu
    And I do not see Delete for everyone button on the message bottom menu
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And I do not see Ping message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1   | CallBackend | Message2         |
      | user1Name | user2Name | You pinged | autocall    | user2Name pinged |

  @C111642 @regression @rc
  Scenario Outline: (AN-4171) Verify deleting the shared file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact1>
    And I tap File button from cursor toolbar
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    When I long tap File Upload container in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see File Upload container in the conversation view

    Examples:
      | Name      | Contact1  | FileName  | FileExtension | FileSize | UploadingTimeout |
      | user1Name | user2Name | qa_random | txt           | 1.00MB   | 20               |

  @C111645 @regression @rc @C111647
  Scenario Outline: Verify deleting is synchronised across own devices when one of them was offline
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    When User Myself adds new device <Device>
    And User <Contact1> adds new device <ContactDevice>
    And I see Conversations list with conversations
    And I tap on conversation name <Contact1>
    And User Myself send encrypted message "<Message>" via device <Device> to user <Contact1>
    And I tap Back button from top toolbar
    And I tap on conversation name <GroupChatName>
    And User Myself send encrypted message "<Message>" via device <Device> to group conversation <GroupChatName>
    And I enable Airplane mode on the device
    And I see No Internet bar in 15 seconds
    And User Myself deletes the recent message from user <Contact1> via device <Device>
    And User Myself deletes the recent message from group conversation <GroupChatName> via device <Device>
    And I disable Airplane mode on the device
    And I do not see No Internet bar in 20 seconds
    Then I do not see the message "<Message>" in the conversation view
    When I tap Back button from top toolbar
    And I tap on conversation name <Contact1>
    Then I do not see the message "<Message>" in the conversation view
    When I type the message "<Message2>" and send it by cursor Send button
    And User Myself remember the recent message from user <Contact1> via device <Device>
    And I enable Airplane mode on the device
    And I see No Internet bar in 20 seconds
    And I long tap the Text message "<Message2>" in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see the message "<Message2>" in the conversation view
    When I disable Airplane mode on the device
    And I do not see No Internet bar in 20 seconds
    # Wait for SE sync
    And I wait for 20 seconds
    Then User Myself see the recent message from user <Contact1> via device <Device> is changed

    Examples:
      | Name      | Contact1  | Contact2  | Message          | Device  | ContactDevice | GroupChatName | Message2        |
      | user1Name | user2Name | user3Name | MessageRemoteDel | Device1 | Device2       | MyGroup       | MessageLocalDel |

  @C111640 @regression @rc
  Scenario Outline: Verify deleting the picture, gif from Giphy
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "<Message>"
    And I tap Gif button from cursor toolbar
    And I select a random gif from the grid preview
    Then I see giphy preview page
    When I tap on the giphy Send button
    Then I see a picture in the conversation view
    When I long tap Image container in the conversation view
    And I tap Delete only for me button on the message bottom menu
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
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I enable Airplane mode on the device
    And I type the message "<Message>" and send it by cursor Send button
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Delete only for me button on the message bottom menu
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
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap audio recording Send button
    # Wait for the audio to be fully uploaded
    And I wait for 5 seconds
    And I long tap Audio Message container in the conversation view
    Then I do not see Copy button on the message bottom menu
    When I tap Delete only for me button on the message bottom menu
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
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I enable Airplane mode on the device
    And I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap audio recording Send button
    And I long tap Audio Message container in the conversation view
    Then I do not see Copy button on the message bottom menu
    When I tap Delete only for me button on the message bottom menu
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
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    And I see Audio Message container in the conversation view
    And I wait for 5 seconds
    And I enable Airplane mode on the device
    And I tap Play button on the recent audio message in the conversation view
    And I long tap Audio Message container in the conversation view
    Then I do not see Copy button on the message bottom menu
    When I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    |

  @C150030 @regression
  Scenario Outline: Verify you can delete Share Location placeholder from conversation view
    Given I am on Android with Google Location Service
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> shares his location to user Myself via device <DeviceName>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Share Location container in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Share Location container in the conversation view

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |

  @C165145 @regression @rc
  Scenario Outline: I can delete link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> send encrypted message "<Link>" to user Myself
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Link Preview container in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Link Preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                                                                                               |
      | user1Name | user2Name | http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 |