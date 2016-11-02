Feature: Ephemeral Messages

  @C259591 @regression @fastLogin
  Scenario Outline: Verify ephemeral messages don't leave a trace in the database
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    Given I type the default message and send it
    Given I see 1 default message in the conversation view
    # make sure the message arrives before saved
    Given I wait for 5 seconds
    When I remember the recent message from user Myself in the local database
    And I wait for <Timeout> seconds
    Then I see 0 default messages in the conversation view
    And I verify the remembered message has been changed in the local database
    When User <Contact> switches user Myself to ephemeral mode with <Timeout> seconds timeout
    And User <Contact> sends 1 encrypted message to user Myself
    # Wait for the message to be delivered
    And I wait for 3 seconds
    And I see 1 default message in the conversation view
    # make sure the message arrives before saved
    And I wait for 5 seconds
    And I remember the state of the recent message from user <Contact> in the local database
    And I wait for <Timeout> seconds
    Then I see 0 default messages in the conversation view
    And I verify the remembered message has been deleted from the local database

    Examples:
      | Name      | Contact   | DeviceName    | Timeout |
      | user1Name | user2Name | ContactDevice | 15      |

  @C259589 @rc @regression @fastLogin
  Scenario Outline: Verify ephemeral messages are disabled in a group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    Then I do not see Hourglass button in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      |

  @C259584 @rc @regression @fastLogin
  Scenario Outline: Verify sending ephemeral message - no online receiver (negative case)
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    Given I type the default message and send it
    # make sure the message arrives before saved
    Given I wait for 5 seconds
    When I remember the recent message from user Myself in the local database
    And I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    And I wait for <Timeout> seconds
    Then I see 1 message in the conversation view
    And I verify the remembered message has been changed in the local database

    Examples:
      | Name      | Contact   | Timeout | EphemeralTimeLabel |
      | user1Name | user2Name | 15      | seconds            |

  @C259597 @rc @regression @fastLogin
  Scenario Outline: Verify the message is deleted on the sender side when it's read on the receiver side
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    Given I type the default message and send it
    Given I see 1 default message in the conversation view
    When I remember the recent message from user Myself in the local database
    And I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    And I wait for 35 seconds
    And I verify the remembered message has been changed in the local database
    And User <Contact> reads the recent message from user <Name>
    And I wait for 40 seconds
    Then I see 0 messages in the conversation view

    Examples:
      | Name      | Contact   | Timeout | EphemeralTimeLabel | DeviceName    |
      | user1Name | user2Name | 30      | seconds            | ContactDevice |

  @C259586 @rc @regression @fastLogin
  Scenario Outline: Verify switching on/off ephemeral message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    Then I see Ephemeral input placeholder text
    And I see Time Indicator button in conversation view
    And I type the default message and send it
    When I tap Time Indicator button in conversation view
    And I set ephemeral messages expiration timer to Off
    Then I see Standard input placeholder text

    Examples:
      | Name      | Contact   | Timer |
      | user1Name | user2Name | 15    |

  @C259588 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral picture
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I tap Add Picture button from input tools
    Given I accept alert if visible
    Given I accept alert if visible
    Given I select the first picture from Keyboard Gallery
    Given I tap Confirm button on Picture preview page
    #wait to make the transition and image arrival
    Given I wait for 3 seconds
    Given User <Name> sends 1 encrypted message using device <DeviceName> to user <Contact>
    When I remember asset container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | DeviceName |
      | user1Name | user2Name | 15    | myDevice2  |

  @C310632 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral audio message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I long tap Audio Message button from input tools
    Given I tap Send record control button
    Given I see audio message container in the conversation view
    Given User <Name> sends 1 encrypted message using device <DeviceName> to user <Contact>
    When I remember asset container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | DeviceName |
      | user1Name | user2Name | 15    | myDevice2  |

  @C310633 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral video message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I prepare <FileName> to be uploaded as a video message
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I navigate back to conversations list
    Given I tap on contact name <Contact>
    Given I tap Video Message button from input tools
    Given I see video message container in the conversation view
    Given User <Name> sends 1 encrypted message using device <DeviceName> to user <Contact>
    # Wait for delivery of video
    Given I wait for 3 seconds
    When I remember asset container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | FileName    | DeviceName |
      | user1Name | user2Name | 15    | testing.mp4 | myDevice2  |

  @C310634 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral share location
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I navigate back to conversations list
    Given I tap on contact name <Contact>
    Given I tap Share Location button from input tools
    Given I accept alert if visible
    # Small delay waiting location detection animation to finish
    Given I wait for 5 seconds
    Given I tap Send location button from map view
    Given User <Name> sends 1 encrypted message using device <DeviceName> to user <Contact>
    When I remember asset container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | DeviceName |
      | user1Name | user2Name | 15    | myDevice2  |

  @C310635 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral file share
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I navigate back to conversations list
    Given I tap on contact name <Contact>
    Given I tap File Transfer button from input tools
    # Wait for transition
    Given I wait for 5 seconds
    Given I tap file transfer menu item <ItemName>
    Given User <Name> sends 1 encrypted message using device <DeviceName> to user <Contact>
    When I remember asset container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | ItemName                   | DeviceName |
      | user1Name | user2Name | 15    | FTRANSFER_MENU_DEFAULT_PNG | myDevice2  |

  @C310636 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral GIF
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I type the "<GiphyTag>" message
    Given I tap GIF button from input tools
    Given I select the first item from Giphy grid
    Given I tap Send button on Giphy preview page
    #wait for transition and gif is loaded in view
    Given I wait for 3 seconds
    Given User <Name> sends 1 encrypted message using device <DeviceName> to user <Contact>
    When I remember asset container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | GiphyTag | DeviceName |
      | user1Name | user2Name | 15    | sun      | myDevice2  |

  @C310637 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral media link
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I type the "<SoundCloudLink>" message and send it
    Given I see media container in the conversation view
    # make sure the message arrives before saved
    Given I wait for 5 seconds
    When I remember the recent message from user Myself in the local database
    And I wait for <Timer> seconds
    Then I see 1 message in the conversation view
    And I verify the remembered message has been changed in the local database

    Examples:
      | Name      | Contact   | Timer | SoundCloudLink                                                   |
      | user1Name | user2Name | 15    | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C311066 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral link preview
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> minute
    Given I type the "<Link>" message and send it
    Given I navigate back to conversations list
    Given I tap on contact name <Contact>
    Given User <Name> sends 1 encrypted message using device <DeviceName> to user <Contact>
    When I remember asset container state
    And I wait for 60 seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | Link                 | DeviceName |
      | user1Name | user2Name | 1     | https://www.wire.com | myDevice2  |

  @C259596 @regression @fastLogin
  Scenario Outline: Verify the message is deleted on the receiver side when timer is over
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> switches user Myself to ephemeral mode with <Timeout> seconds timeout
    Given User <Contact> sends encrypted message "<Message>" to user Myself
    Given I tap on contact name <Contact>
    When I see the conversation view contains message <Message>
    And I wait for <Timeout> seconds
    Then I see 0 message in the conversation view

    Examples:
      | Name      | Contact   | Message | Timeout | DeviceName    |
      | user1Name | user2Name | y1      | 15      | ContactDevice |

  @C311221 @C259598 @staging @fastLogin
  Scenario Outline: Verify receiving ephemeral assets (picture, video, audio, link preview, location)
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given User <Contact> switches user Myself to ephemeral mode with <EphemeralTimeout> seconds timeout
    # Picture
    When User <Contact> sends encrypted image <Picture> to single user conversation Myself
    And I wait for <SyncTimeout> seconds
    And I see 1 photo in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I see 0 photos in the conversation view
    # Video
    When User <Contact> sends file <FileName> having MIME type <VideoMIME> to single user conversation <Name> using device <DeviceName>
    And I wait for <SyncTimeout> seconds
    And I see video message container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see video message container in the conversation view
    # Audio
    When User <Contact> sends file <AudioFileName> having MIME type <AudioMIME> to single user conversation <Name> using device <DeviceName>
    And I wait for <SyncTimeout> seconds
    And I see audio message container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see audio message container in the conversation view
    # Link Preview
    When User <Contact> switches user Myself to ephemeral mode with 15 seconds timeout
    And User <Contact> sends encrypted message "<Link>" to user Myself
    And I wait for <SyncTimeout> seconds
    And I see link preview container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see link preview container in the conversation view
    # Location
    When User <Contact> shares the default location to user Myself via device <DeviceName>
    And I wait for <SyncTimeout> seconds
    And I see location map container in the conversation view
    And I wait for <EphemeralTimeout> seconds
    Then I do not see location map container in the conversation view
    # Send regular message after ephemeral
    When User <Contact> switches user Myself to ephemeral mode with 0 seconds timeout
    And User <Contact> sends 1 encrypted message using device <DeviceName> to user Myself
    And I see 1 default message in the conversation view
    And I wait for 15 seconds
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   | SyncTimeout | EphemeralTimeout | DeviceName    | Picture     | FileName    | VideoMIME | AudioFileName | AudioMIME | Link         |
      | user1Name | user2Name | 3           | 5                | ContactDevice | testing.jpg | testing.mp4 | video/mp4 | test.m4a      | audio/mp4 | www.wire.com |

  @C259590 @regression @fastLogin
  Scenario Outline: Verify edit/delete/like/copy/forward are switched off
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds a new device <DeviceName> with label <DeviceLabel>
    Given I sign in using my email or phone number
    Given User <Contact> switches user Myself to ephemeral mode with <EphemeralTimeout> seconds timeout
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <EphemeralTimeout> seconds
    Given I type the "<Message1>" message and send it
    When I long tap "<Message1>" message in conversation view
    Then I do not see Edit badge item
    And I do not see Delete badge item
    And I do not see Like badge item
    And I do not see Copy badge item
    And I do not see Forward badge item
    When User <Contact> sends encrypted message "<Message2>" to user Myself
    And I long tap "<Message2>" message in conversation view
    Then I do not see Edit badge item
    And I do not see Delete badge item
    And I do not see Like badge item
    And I do not see Copy badge item
    And I do not see Forward badge item

    Examples:
      | Name      | Contact   | Message1    | Message2    | DeviceName | DeviceLabel | EphemeralTimeout |
      | user1Name | user2Name | message one | message two | ContactDev | DevLabel    | 15               |