Feature: Ephemeral Group Message

  @C318635 @regression
  Scenario Outline: (Group) Verify sending all types of messages after I enable ephemeral mode in group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I push <FileSize> video file having name "random_qa.mp4" to the device
    Given I push local file named "avatarTest.png" to the device
    Given I push <FileSize> file having name "qa_random.txt" to the device
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
    Given I tap Ephemeral button from cursor toolbar
    Given I set timeout to <EphemeraTimeout> on Extended cursor ephemeral overlay
    Given I tap on text input
    # Video
    When I tap Video message button from cursor toolbar
    And I see Video Message container in the conversation view
    And I remember the state of Video Message container in the conversation view
    And I wait for <EphemeraTimeout>
    Then I verify the state of Video Message container is changed
    # Picture
    When I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view
    And I remember the state of Image container in the conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Image container is changed
    # Audio message
    When I long tap Audio message cursor button 2 seconds and swipe up
    Then I see Audio Message container in the conversation view
    And I wait for <EphemeraTimeout>
    And I do not see Audio Message container in the conversation view
    And I see Audio Message Placeholder container in the conversation view
    # Ping
    When I tap Ping button from cursor toolbar
    Then I see Ping message "<PingMsg>" in the conversation view
    And I wait for <EphemeraTimeout>
    And I do not see Ping message "YOU PINGED" in the conversation view
    # File
    When I tap File button from cursor toolbar
    Then I see File Upload container in the conversation view
    And I wait for <EphemeraTimeout>
    And I do not see File Upload container in the conversation view
    And I see File Upload Placeholder container in the conversation view
    # Location
    When I tap Share location button from cursor toolbar
    And I tap Send button on Share Location page
    Then I see Share Location container in the conversation view
    And I remember the state of Share Location container in the conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Share Location container is changed
    # Link Preview
    # TODO: Link preview should be obfuscated with container instead of pure url text.
    When I type the message "<Link>" and send it by cursor Send button
    Then I see Link Preview container in the conversation view
    And I do not see Link Preview container in the conversation view

    Examples:
      | Name      | Group | Contact   | Contact1  | EphemeraTimeout | Link                                                                                               | PingMsg    | FileSize |
      | user1Name | YoG   | user2Name | user3Name | 5 seconds       | http://www.lequipe.fr/Football/Actualites/L-olympique-lyonnais-meilleur-centre-de-formation/703676 | YOU PINGED | 1.00MB   |

  @C320770 @regression
  Scenario Outline: (Group) Verify in group sending ephemeral text message will be obfuscated when receiver is offline and not been delivered to my other devices
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User Myself adds new device <Mydevice>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
    When User Myself remembers the recent message from group conversation <Group> via device <Mydevice>
    And I tap Ephemeral button from cursor toolbar
    And I set timeout to <EphemeralTimeout> on Extended cursor ephemeral overlay
    And I type the message "<Message>" and send it by cursor Send button
    Then I see the message "<Message>" in the conversation view
    And User Myself sees the recent message from group conversation <Group> via device <Mydevice> is not changed in 5 seconds
    When I wait for <EphemeralTimeout>
    Then I do not see the message "<Message>" in the conversation view
    When I tap Ephemeral button from cursor toolbar
    And I set timeout to Off on Extended cursor ephemeral overlay
    And I type the message "<Message2>" and send it by cursor Send button
    Then I see the message "<Message2>" in the conversation view
    And User Myself sees the recent message from group conversation <Group> via device <Mydevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | EphemeralTimeout | Message | Message2 | Mydevice | Group   | Contact1  |
      | user1Name | user2Name | 15 seconds       | test5s  | ok       | d1       | YoGroup | user3Name |

  @C320775 @regression
  Scenario Outline: (Group) Verify edit/delete/like/copy/forward is disabled for ephemeral messages in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
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
      | Name      | Contact   | EphemeralTimeout | Message | Group   | Contact1  |
      | user1Name | user2Name | 5 seconds        | yo      | YoGroup | user3Name |

  @C320790 @regression
  Scenario Outline: (Group) Verify timer is applyed to all messages until turning it off in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
    When I tap Ephemeral button from cursor toolbar
    And I set timeout to <EphemeralTimeout> on Extended cursor ephemeral overlay
    And I type the message "<Message>" and send it by cursor Send button
    And I wait for <EphemeralTimeout>
    Then I do not see the message "<Message>" in the conversation view
    When I type the message "<Message2>" and send it by cursor Send button
    And I wait for <EphemeralTimeout>
    Then I do not see the message "<Message2>" in the conversation view
    When I tap Ephemeral button from cursor toolbar
    And I set timeout to Off on Extended cursor ephemeral overlay
    And I type the message "<Message3>" and send it by cursor Send button
    And I wait for <EphemeralTimeout>
    Then I see the message "<Message3>" in the conversation view

    Examples:
      | Name      | Contact   | EphemeralTimeout | Message | Group   | Contact1  | Message2 | Message3 |
      | user1Name | user2Name | 5 seconds        | yo      | YoGroup | user3Name | dd       | hk       |

  @C321197 @regression
  Scenario Outline: (Group) Verify the message is deleted on the sender side when it's read by anyone in group and I can receive eph msg in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
    When I tap Ephemeral button from cursor toolbar
    And I set timeout to <EphemeralTimeout> on Extended cursor ephemeral overlay
    And I type the message "<Message>" and send it by cursor Send button
    # Wait until SE received last message
    And I wait for 5 seconds
    And User <Contact> reads the recent message from group conversation <Group> via device <ContactDevice>
    Then I do not see any text messages in the conversation view
    When User <Contact> switches group conversation <Group> to ephemeral mode via device <ContactDevice> with <EphemeralTimeout2> timeout
    And User <Contact> sends encrypted message "<Message2>" to group conversation <Group>
    Then I see the message "<Message2>" in the conversation view
    And I wait for <EphemeralTimeout2>
    And I do not see any text messages in the conversation view

    Examples:
      | Name      | Contact   | EphemeralTimeout | Message | Group   | Contact1  | ContactDevice | Message2 | EphemeralTimeout2 |
      | user1Name | user2Name | 5 seconds        | yo      | YoGroup | user3Name | d1            | Do       | 15 seconds        |

  @C321198 @regression
  Scenario Outline: (Group) Verify receiving an ephemeral message in Group on the multiple devices and deleted on all recevier
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User Myself adds new device <OwnDevice>
    Given I see Conversations list with conversations
    When User <Contact> switches group conversation <Group> to ephemeral mode via device <ContactDevice> with <EphemeralTimeout> timeout
    And User Myself remember the recent message from group conversation <Group> via device <OwnDevice>
    And User <Contact> sends encrypted message "<Message>" to group conversation <Group>
    And User Myself sees the recent message from group conversation <Group> via device <OwnDevice> is changed in 15 seconds
    And User Myself reads the recent message from group conversation <Group> via device <OwnDevice>
    And I wait for <EphemeralTimeout>
    Then User Myself see the recent message from group conversation <Group> via device <OwnDevice> is not changed in 15 seconds
    # Wait for SE Sync
    When I wait for 5 seconds
    And I tap on conversation name <Group>
    Then I do not see any text messages in the conversation view

    Examples:
      | Name      | Contact   | EphemeralTimeout | Message | Group   | Contact1  | ContactDevice | OwnDevice |
      | user1Name | user2Name | 15 seconds       | yo      | YoGroup | user3Name | d1            | d2        |

  @C321205 @regression
  Scenario Outline: (Group) Verify receiving all types of ephemeral messages in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
    Given User <Contact> switches group conversation <Group> to ephemeral mode via device <ContactDevice> with <EphemeralTimeout> seconds timeout
    # Ping
    When User <Contact> securely pings conversation <Group>
    And I wait for <SyncTimeout> seconds
    And I see Ping message "<Contact> PINGED" in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Ping message "<Contact> PINGED" in the conversation view
    # Video
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <ContactDevice> to group conversation <Group>
    And I wait for <SyncTimeout> seconds
    And I see Video Message container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Video Message container in the conversation view
    # Picture
    When User <Contact> sends encrypted image <Picture> to group conversation <Group>
    And I wait for <SyncTimeout> seconds
    And I see Image container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Image container in the conversation view
    # Audio
    When <Contact> sends local file named "<AudioFileName>" and MIME type "<AudioMIMEType>" via device <ContactDevice> to group conversation <Group>
    And I wait for <SyncTimeout> seconds
    And I see Audio Message container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Audio Message container in the conversation view
    # Location
    When User <Contact> shares his location to group conversation <Group> via device <ContactDevice>
    And I wait for <SyncTimeout> seconds
    And I see Share Location container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Share Location container in the conversation view
    # Link Preview
    When User <Contact> send encrypted message "<URL>" to group conversation <Group>
    And I wait for <SyncTimeout> seconds
    And I see Link Preview container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Link Preview container in the conversation view
    # Soundcloud
    When User <Contact> send encrypted message "<SoundCloud>" to group conversation <Group>
    And I wait for <SyncTimeout> seconds
    And I see Soundcloud container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Soundcloud container in the conversation view
    # Youtube
    When User <Contact> send encrypted message "<Youtube>" to group conversation <Group>
    And I wait for <SyncTimeout> seconds
    And I see Youtube container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see Youtube container in the conversation view

    Examples:
      | Name      | Contact   | Contact1  | Group   | ContactDevice | EphemeralTimeout | FileName    | MIMEType  | SyncTimeout | Picture        | AudioFileName | AudioMIMEType | URL                     | SoundCloud                                       | Youtube                                     |
      | user1Name | user2Name | user3Name | yogroup | d1            | 5                | testing.mp4 | video/mp4 | 1           | avatarTest.png | test.m4a      | audio/mp4     | http://www.facebook.com | https://soundcloud.com/sodab/256-ra-robag-wruhme | https://www.youtube.com/watch?v=wTcNtgA6gHs |

  @C321206 @regression
  Scenario Outline: (Group) If a user receives multiple ephemeral messages after being offline, all get the same timer
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
    Given I enable Airplane mode on the device
    Given I see No Internet bar in <NetworkTimeout> seconds
    Given User <Contact> switches group conversation <Group> to ephemeral mode via device <ContactDevice> with <EphemeralTimeout> timeout
    Given User <Contact> sends encrypted message "<Message1>" to group conversation <Group>
    Given I wait for 5 seconds
    Given User <Contact> sends encrypted message "<Message2>" to group conversation <Group>
    Given I disable Airplane mode on the device
    When I do not see No Internet bar in <NetworkTimeout> seconds
    Then I see the message "<Message1>" in the conversation view
    And I see the message "<Message2>" in the conversation view
    When I wait for <EphemeralTimeout>
    Then I do not see any text messages in the conversation view

    Examples:
      | Name      | Contact   | Contact1  | EphemeralTimeout | NetworkTimeout | Message1 | Message2 | ContactDevice | Group   |
      | user1Name | user2Name | user3Name | 15 seconds       | 15             | YO1      | YO2      | d1            | YoGroup |

  @C321207 @regression
  Scenario Outline: (Group) If ephemeral message can’t be sent due to bad network, it can be resend and will not get obfuscated
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
    Given I tap Ephemeral button from cursor toolbar
    Given I set timeout to <EphemeralTimeout> on Extended cursor ephemeral overlay
    Given I enable Airplane mode on the device
    Given I see No Internet bar in <NetworkTimeout> seconds
    Given I type the message "<Message>" and send it by cursor Send button
    Given I see Message status with expected text "<MessageStatus>" in conversation view
    Given I wait for <EphemeralTimeout>
    Given I disable Airplane mode on the device
    Given I do not see No Internet bar in <NetworkTimeout> seconds
    Given I see the message "<Message>" in the conversation view
    When I resend all the visible messages in conversation view
    And I wait for <EphemeralTimeout>
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Contact1  | EphemeralTimeout | NetworkTimeout | Message | MessageStatus          | Group   |
      | user1Name | user2Name | user3Name | 5 seconds        | 15             | Yo      | Sending failed. Resend | YoGroup |

  @C321208 @regression
  Scenario Outline: (Group) Verify the message is not deleted for users that didn't read the message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given Myself has group chat <Group> with <Contact>,<Contact1>
    Given User <Contact> adds new device <ContactDevice>
    Given User <Contact1> adds new device <ContactDevice2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When User <Contact> switches group conversation <Group> to ephemeral mode via device <ContactDevice> with <EphemeralTimeout> timeout
    And User <Contact> sends encrypted message "<Message>" via device <ContactDevice> to group conversation <Group>
    # Wait for SE sync
    And I wait for 5 seconds
    And User <Contact1> reads the recent message from group conversation <Group> via device <ContactDevice2>
    # Wait for SE Sync
    When I wait for 5 seconds
    And I tap on conversation name <Group>
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | EphemeralTimeout | Message | Group   | Contact1  | ContactDevice | ContactDevice2 |
      | user1Name | user2Name | 15 seconds       | yo      | YoGroup | user3Name | d1            | d2             |
