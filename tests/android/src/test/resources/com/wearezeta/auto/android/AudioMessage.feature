Feature: Audio Message

  @C131179 @regression @rc @legacy
  Scenario Outline: Verify sending voice message by long tap > swipe up
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message cursor button <TapDuration> seconds and swipe up
    Then I see cursor toolbar
    And I see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131176 @regression @rc @legacy
  Scenario Outline: Verify microphone is changed to play icon after releasing the thumb
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message microphone button <TapDuration> seconds and remember icon
    And I verify the state of audio message microphone button in the conversation view is changed

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131180 @regression @rc @legacy
  Scenario Outline: Verify Play/Pause recorded audio message after long tap the audio cursor icon
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap audio recording Send button
    Then I see cursor toolbar
    And I see Audio Message container in the conversation view
    And I wait up to <UploadTimeout> seconds until audio message upload is completed
    When I remember the state of recent audio message seekbar
    And I tap Play button on the recent audio message in the conversation view
    And I wait up to <PlayTimeout> seconds until audio message play is started
    Then I verify the state of recent audio message seekbar in the conversation view is changed
    When I tap Pause button on the recent audio message in the conversation view
    Then I verify the state of Play button on the recent audio message in the conversation view is changed

    Examples:
      | Name      | Contact   | TapDuration | UploadTimeout | PlayTimeout |
      | user1Name | user2Name | 15          | 60            | 10          |

  @C131188 @regression @rc
  Scenario Outline: Verify getting a chathead when voice message is sent in the other conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact2>
    When <Contact1> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    Then I see new message notification "<Notification>"

    Examples:
      | Name      | Contact1  | Contact2  | FileName | MIMEType  | DeviceName | Notification            |
      | user1Name | user2Name | user3Name | test.m4a | audio/mp4 | Device1    | Shared an audio message |

  @C131189 @regression @rc @legacy
  Scenario Outline: (AN-4839) Verify receiving/downloading/re-downloading a voice message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    Then I see Audio Message container in the conversation view
    When I enable Airplane mode on the device
    And I see No Internet bar in <NetworkTimeout> seconds
    And I tap Play button on the recent audio message in the conversation view
    Then I see No Internet bar in <NetworkTimeout> seconds
    When I disable Airplane mode on the device
    And I do not see No Internet bar in <NetworkTimeout> seconds
    And I tap Play button on the recent audio message in the conversation view
    Then I wait up to <DownloadTimeout> seconds until audio message download is completed
    And I wait up to <PlayTimeout> seconds until audio message play is started

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName | NetworkTimeout | DownloadTimeout | PlayTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    | 45             | 30              | 10          |

  @C131183 @regression @rc
  Scenario Outline: Verify sending/resending audio message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I enable Airplane mode on the device
    And I see No Internet bar in <NetworkTimeout> seconds
    And I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap audio recording Send button
    Then I see Audio Message container in the conversation view
    Then I see Message status with expected text "<MessageStatusFailed>" in conversation view
    When I remember the state of Retry button on the recent audio message in the conversation view
    And I disable Airplane mode on the device
    And I do not see No Internet bar in <NetworkTimeout> seconds
    And I tap Retry button on the recent audio message in the conversation view
    # Retry button changes to Play button
    Then I verify the state of Retry button on the recent audio message in the conversation view is changed
    And I wait up to <UploadTimeout> seconds until audio message upload is completed
    When I tap Play button on the recent audio message in the conversation view
    Then I wait up to <PlayTimeout> seconds until audio message play is started

    Examples:
      | Name      | Contact   | MessageStatusFailed    | TapDuration | NetworkTimeout | UploadTimeout | PlayTimeout |
      | user1Name | user2Name | Sending failed. Resend | 5           | 45             | 60            | 10          |

  @C131177 @regression @rc @legacy
  Scenario Outline: Verify playing and cancelling recorded audio message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I remember the state of audio message preview seekbar
    And I tap audio recording Play button
    Then I verify the state of audio message preview seekbar is changed
    When I tap audio recording Cancel button
    Then I see cursor toolbar
    And I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131194 @regression @rc @legacy
  Scenario Outline: Verify playing/deleting a received voice message and pausing when in background
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    Then I see Audio Message container in the conversation view
    # Wait until the seekbar initialized completely
    And I wait for 3 seconds
    When I remember the state of recent audio message seekbar
    And I tap Play button on the recent audio message in the conversation view
    Then I wait up to <DownloadTimeout> seconds until audio message download is completed
    And I wait up to <PlayTimeout> seconds until audio message play is started
    And I verify the state of recent audio message seekbar in the conversation view is changed
    And I minimize the application
    And I restore the application
    Then I verify the state of Pause button on the recent audio message in the conversation view is changed
    When I long tap Audio Message container in the conversation view
    Then I do not see Copy button on the message bottom menu
    When I tap Delete button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName | DownloadTimeout | PlayTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    | 30              | 10          |

  @C139849 @regression
  Scenario Outline: (AN-4067) Verify that play of audio message will be stopped by incoming voice call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    When I see Audio Message container in the conversation view
    And I remember the state of recent audio message seekbar
    And I tap Play button on the recent audio message in the conversation view
    Then I wait up to <DownloadTimeout> seconds until audio message download is completed
    And I wait up to <PlayTimeout> seconds until audio message play is started
    And I verify the state of recent audio message seekbar in the conversation view is changed
    And <Contact> calls me
    And I see incoming call from <Contact>
    And <Contact> stops calling me
    And I do not see incoming call
    Then I verify the state of Pause button on the recent audio message in the conversation view is changed

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName | CallBackend | DownloadTimeout | PlayTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    | zcall       | 30              | 10          |

  @C139851 @regression
  Scenario Outline: (AN-4067) Verify that play of audio message will be stopped by incoming video call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    When I see Audio Message container in the conversation view
    And I remember the state of recent audio message seekbar
    And I tap Play button on the recent audio message in the conversation view
    Then I wait up to <DownloadTimeout> seconds until audio message download is completed
    And I wait up to <PlayTimeout> seconds until audio message play is started
    And I verify the state of recent audio message seekbar in the conversation view is changed
    And <Contact> starts a video call to me
    And I see incoming video call
    And <Contact> stops calling me
    And I do not see incoming video call
    Then I verify the state of Pause button on the recent audio message in the conversation view is changed

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName | CallBackend | DownloadTimeout | PlayTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    | chrome      | 30              | 10          |

  @C139852 @regression
  Scenario Outline: Verify that incoming voice/video call will cancel record of audio message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message button from cursor toolbar without releasing my finger
    And I wait for 3 seconds
    And <Contact> calls me
    And I see incoming call from <Contact>
    And <Contact> stops calling me
    And I do not see incoming call
    Then I do not see audio message is recording

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | zcall       |

  @C165127 @regression
  Scenario Outline: I can record voice message by single tap on mic, apply filter and send it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # single tap to record check
    When I tap Audio message button from cursor toolbar
    Then I see Voice recording overlay
    When I tap Start Record button on Voice filters overlay
    And I wait for <MessageDuration> seconds
    And I tap Stop Record button on Voice filters overlay
    # filter check
    Then I see Voice filters overlay
    When I tap the 3rd Filter button on Voice filters overlay
    Then I see voice graph on Voice filters overlay
    # send message check
    When I tap Approve button on Voice filters overlay
    Then I see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | MessageDuration |
      | user1Name | user2Name | 10              |
