Feature: Ephemeral Message

  @C261701 @regression
  Scenario Outline: Verify sending ephemeral text message will be obfuscated when receiver is offline and not been delivered to my other devices
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User Myself adds new device <Mydevice>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When User Myself remembers the recent message from user <Contact> via device <Mydevice>
    And I tap Ephemeral button from cursor toolbar
    And I set timeout to <EphemeralTimeout> on Extended cursor ephemeral overlay
    And I type the message "<Message>" and send it by cursor Send button
    Then I see the message "<Message>" in the conversation view
    And I see Message status with expected text "<EphemeralStatus>" in conversation view
    When I wait for <EphemeralTimeout>
    Then I see Message status with expected text "Sent" in conversation view
    And I do not see the message "<Message>" in the conversation view
    And User Myself sees the recent message from user <Contact> via device <Mydevice> is not changed in 5 seconds
    When I tap Ephemeral button from cursor toolbar
    And I set timeout to Off on Extended cursor ephemeral overlay
    And I type the message "<Message2>" and send it by cursor Send button
    And I wait for <EphemeralTimeout>
    Then I see Message status with expected text "Sent" in conversation view
    And I see the message "<Message2>" in the conversation view
    And User Myself sees the recent message from user <Contact> via device <Mydevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | EphemeralTimeout | Message | EphemeralStatus | Message2 | Mydevice |
      | user1Name | user2Name | 15 seconds       | test5s  | left            | ok       | d1       |

  @C261705 @regression
  Scenario Outline: Verify ephemeral messages are turned off in a group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    Then I do not see Ephemeral button in cursor input

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | MyGroup       |

  @C261706 @regression
  Scenario Outline: Verify edit/delete/like/copy/forward is disabled for ephemeral messages
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Ephemeral button from cursor toolbar
    And I set timeout to <EphemeralTimeout> on Extended cursor ephemeral overlay
    And I type the message "<Message>" and send it by cursor Send button
    And I long tap the obfuscated Text message "<Message>" in the conversation view
    Then I do not see Delete only for me button on the message bottom menu
    And I do not see Delete for everyone button on the message bottom menu
    And I do not see Like button on the message bottom menu
    And I do not see Copy button on the message bottom menu
    And I do not see Forward button on the message bottom menu

    Examples:
      | Name      | Contact   | EphemeralTimeout | Message |
      | user1Name | user2Name | 5 seconds        | yo      |

  @C261704 @regression
  Scenario Outline: Verify sending all types of messages after I enable ephemeral mode
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I push <FileSize> video file having name "random_qa.mp4" to the device
    Given I push local file named "avatarTest.png" to the device
    Given I push <FileSize> file having name "qa_random.txt" to the device
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given I tap Ephemeral button from cursor toolbar
    Given I set timeout to <EphemeraTimeout> on Extended cursor ephemeral overlay
    Given I tap on text input
    # Video
    When I tap Video message button from cursor toolbar
    And I see Video Message container in the conversation view
    And I do not see Message status with expected text "Sending" in conversation view
    And I remember the state of Video Message container in the conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Video Message container is changed
    # Picture
    When I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I remember the state of Image container in the conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Image container is changed
    # Audio message
    When I long tap Audio message cursor button 2 seconds and swipe up
    Then I see Audio Message container in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeraTimeout>
    And I do not see Audio Message container in the conversation view
    And I see Audio Message Placeholder container in the conversation view
    # Ping
    When I tap Ping button from cursor toolbar
    Then I see Ping message "<PingMsg>" in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeraTimeout>
    And I do not see Ping message "YOU PINGED" in the conversation view
    # File
    When I tap File button from cursor toolbar
    Then I see File Upload container in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeraTimeout>
    And I do not see File Upload container in the conversation view
    And I see File Upload Placeholder container in the conversation view
    # Location
    When I tap Share location button from cursor toolbar
    And I tap Send button on Share Location page
    Then I see Share Location container in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I remember the state of Share Location container in the conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Share Location container is changed
    # Link Preview
    # TODO: Link preview should be obfuscated with container instead of pure url text.
    When I type the message "<Link>" and send it by cursor Send button
    Then I see Link Preview container in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I do not see Link Preview container in the conversation view

    Examples:
      | Name      | Contact   | EphemeraTimeout | Link                                                                                               | MessageStatus | PingMsg    | FileSize |
      | user1Name | user2Name | 5 seconds       | http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 | Sending       | YOU PINGED | 1.00MB   |

  @C261715 @regression
  Scenario Outline: Verify I can receive ephemeral text message which is deleted after timeout and timer starts once I open conversation
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact> switches user Myself to ephemeral mode via device <ContactDevice> with <EphemeralTimeout1> seconds timeout
    And User <Contact> sends encrypted message "<Message1>" to user Myself
    And I wait for 8 seconds
    And I tap on conversation name <Contact>
    Then I see the message "<Message1>" in the conversation view
    And I see Message status with expected text "<EphemeralStatus>" in conversation view
    And I wait for <EphemeralTimeout1> seconds
    And I do not see the message "<Message1>" in the conversation view

    Examples:
      | Name      | Contact   | ContactDevice | Message1 | EphemeralTimeout1 | EphemeralStatus |
      | user1Name | user2Name | d1            | y1       | 15                | left            |

  @C261710 @regression
  Scenario Outline: Verify the message is deleted on the sender side when it's read on the receiver side
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> adds new devices <ContactDevice>
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Ephemeral button from cursor toolbar
    And I set timeout to <EphemeralTimeout> on Extended cursor ephemeral overlay
    And I type the message "<Message>" and send it by cursor Send button
    And User <Contact> reads the recent message from user Myself via device <ContactDevice>
    Then I do not see any text message in the conversation view

    Examples:
      | Name      | Contact   | EphemeralTimeout | Message | ContactDevice |
      | user1Name | user2Name | 5 seconds        | yo      | d1            |

  @C261721 @regression
  Scenario Outline: If ephemeral message can’t be sent due to bad network, it can be resend and will not get obfuscated
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Ephemeral button from cursor toolbar
    And I set timeout to <EphemeralTimeout> on Extended cursor ephemeral overlay
    And I enable Airplane mode on the device
    And I see No Internet bar in <NetworkTimeout> seconds
    And I type the message "<Message>" and send it by cursor Send button
    And I see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeralTimeout>
    And I disable Airplane mode on the device
    Then I see the message "<Message>" in the conversation view
    When I do not see No Internet bar in <NetworkTimeout> seconds
    And I resend all the visible messages in conversation view
    And I wait for <EphemeralTimeout>
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | EphemeralTimeout | NetworkTimeout | Message | MessageStatus          |
      | user1Name | user2Name | 5 seconds        | 15             | Yo      | Sending failed. Resend |

  @C261712 @regression
  Scenario Outline: Verify that missed call has stayed after receiver saw it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given User <Contact> adds new devices <ContactDevice>
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When User <Contact> switches user Myself to ephemeral mode via device <ContactDevice> with <EphemeralTimeout> timeout
    And User <Contact> sends encrypted message "<Message1>" to user Myself
    And <Contact> calls me
    And I wait for 5 seconds
    And <Contact> stops calling me
    And User <Contact> sends encrypted message "<Message2>" to user Myself
    And I wait for 5 seconds
    Then I do not see the message "<Message1>" in the conversation view
    And I do not see the message "<Message2>" in the conversation view
    And I see missed call from <Contact> in the conversation

    Examples:
      | Name      | Contact   | EphemeralTimeout | ContactDevice | Message1 | Message2 | CallBackend |
      | user1Name | user2Name | 5 seconds        | d1            | Yo1      | Yo2      | zcall       |

  @C261722 @regression
  Scenario Outline: If a user receives multiple ephemeral messages after being offline, all get the same timer
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I enable Airplane mode on the device
    And I see No Internet bar in <NetworkTimeout> seconds
    And User <Contact> switches user Myself to ephemeral mode via device <ContactDevice> with <EphemeralTimeout> timeout
    And User <Contact> sends encrypted message "<Message1>" to user Myself
    And I wait for 5 seconds
    And User <Contact> sends encrypted message "<Message2>" to user Myself
    And I disable Airplane mode on the device
    And I do not see No Internet bar in <NetworkTimeout> seconds
    Then I see the message "<Message1>" in the conversation view
    When I wait for <EphemeralTimeout>
    Then I do not see any text message in the conversation view

    Examples:
      | Name      | Contact   | EphemeralTimeout | NetworkTimeout | Message1 | Message2 | ContactDevice |
      | user1Name | user2Name | 15 seconds       | 15             | YO1      | YO2      | d1            |

  @C262550 @staging
  Scenario Outline: (Not implemented) Verify I can delete unsent ephemera message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Ephemeral button from cursor toolbar
    And I set timeout to <EphemeralTimeout> on Extended cursor ephemeral overlay
    And I enable Airplane mode on the device
    And I see No Internet bar in <NetworkTimeout> seconds
    And I type the message "<Message>" and send it by cursor Send button
    And I see Message status with expected text "<MessageStatus>" in conversation view
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message | NetworkTimeout | MessageStatus          | EphemeralTimeout |
      | user1Name | user2Name | Yo      | 15             | Sending failed. Resend | 5 seconds        |

  @C261724 @regression
  Scenario Outline: Verify receiving all types of ephemeral messages
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given User <Contact> switches user Myself to ephemeral mode via device <ContactDevice> with <EphemeralTimeout> seconds timeout
    # Ping
    When User <Contact> securely pings conversation Myself
    And I wait for <SyncTimeout> seconds
    And I see Ping message "<Contact> PINGED" in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Ping message "<Contact> PINGED" in the conversation view
    # Video
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <ContactDevice> to user Myself
    And I wait for <SyncTimeout> seconds
    And I see Video Message container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Video Message container in the conversation view
    # Picture
    When User <Contact> sends encrypted image <Picture> to single user conversation Myself
    And I wait for <SyncTimeout> seconds
    And I see Image container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Image container in the conversation view
    # Audio
    When <Contact> sends local file named "<AudioFileName>" and MIME type "<AudioMIMEType>" via device <ContactDevice> to user Myself
    And I wait for <SyncTimeout> seconds
    And I see Audio Message container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Audio Message container in the conversation view
    # Location
    When User <Contact> shares his location to user Myself via device <ContactDevice>
    And I wait for <SyncTimeout> seconds
    And I see Share Location container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Share Location container in the conversation view
    # Link Preview
    When User <Contact> send encrypted message "<URL>" to user Myself
    And I wait for <SyncTimeout> seconds
    And I see Link Preview container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Link Preview container in the conversation view
    # Soundcloud
    When User <Contact> send encrypted message "<SoundCloud>" to user Myself
    And I wait for <SyncTimeout> seconds
    And I see Soundcloud container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Soundcloud container in the conversation view
    # Youtube
    When User <Contact> send encrypted message "<Youtube>" to user Myself
    And I wait for <SyncTimeout> seconds
    And I see Youtube container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Youtube container in the conversation view

    Examples:
      | Name      | Contact   | ContactDevice | EphemeralTimeout | FileName    | MIMEType  | SyncTimeout | Picture     | AudioFileName | AudioMIMEType | URL                     | SoundCloud                                       | Youtube                                     |
      | user1Name | user2Name | d1            | 5                | testing.mp4 | video/mp4 | 1           | testing.jpg | test.m4a      | audio/mp4     | http://www.facebook.com | https://soundcloud.com/sodab/256-ra-robag-wruhme | https://www.youtube.com/watch?v=wTcNtgA6gHs |
