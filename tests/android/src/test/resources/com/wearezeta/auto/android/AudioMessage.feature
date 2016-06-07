Feature: Audio Message

  @C131173 @regression @rc @rc42
  Scenario Outline: Verify hint appears on voice icon tapping
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I tap Audio message button from cursor toolbar
    Then I see hint message "<HintMessage>" of cursor button

    Examples:
      | Name      | Contact   | HintMessage                           |
      | user1Name | user2Name | Tap and hold to send an audio message |

  @C131179 @C131175 @C131176 @regression @rc @rc42
  Scenario Outline: Verify sending voice message by long tap > swipe up
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message cursor button <TapDuration> seconds and swipe up
    Then I see cursor toolbar
    And I see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131176 @staging
  Scenario Outline: Verify microphone is changed to play icon after releasing the thumb
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message microphone button <TapDuration> seconds and remember icon
    And I verify the state of audio message microphone button in the conversation view is changed

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131180 @C131195 @C131197 @regression @rc @rc42
  Scenario Outline: Verify sending voice message by long tap > release the humb > tap on the check icon -> play/pause audio message
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap on audio message send button
    Then I see cursor toolbar
    And I see Audio Message container in the conversation view
    When I remember the state of recent audio message seekbar
    And I remember the state of Play button on the recent audio message in the conversation view
    And I tap Play button on the recent audio message in the conversation view
    Then I verify the state of recent audio message seekbar in the conversation view is changed
    And I verify the state of Play button on the recent audio message in the conversation view is changed
    When I tap Pause button on the recent audio message in the conversation view
    Then I verify the state of Play button on the recent audio message in the conversation view is not changed

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 10          |

  @C131188 @regression @rc
  Scenario Outline: Verify getting a chathead when voice message is sent in the other conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact2>
    When <Contact1> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    Then I see new message notification "<Notification>"

    Examples:
      | Name      | Contact1  | Contact2  | FileName | MIMEType  | DeviceName | Notification            |
      | user1Name | user2Name | user3Name | test.m4a | audio/mp4 | Device1    | Shared an audio message |

  @C131192 @C131193 @C131189 @regression @rc @rc42
  Scenario Outline: Verify failing downloading voice message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    # C131189
    Then I see Audio Message container in the conversation view
    When I enable Airplane mode on the device
    # Wait for network is totally disabled
    And I wait for 3 seconds
    And I tap Play button on the recent audio message in the conversation view
    Then I see No Internet bar in <NetworkTimeout> seconds
    When I disable Airplane mode on the device
    And I do not see No Internet bar in <NetworkTimeout> seconds
    And I remember the state of Play button on the recent audio message in the conversation view
    And I tap Play button on the recent audio message in the conversation view
    # Wait for the audio to be fully downloaded
    And I wait for 5 seconds
    Then I verify the state of Play button on the recent audio message in the conversation view is changed

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName | NetworkTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    | 10             |

  @C131183 @C131184 @regression @rc
  Scenario Outline: Verify failing sending/retrying voice message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I enable Airplane mode on the device
    # Wait for network is totally disabled
    And I wait for 3 seconds
    And I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I tap on audio message send button
    And I see Audio Message container in the conversation view
    And I remember the state of Retry button on the recent audio message in the conversation view
    And I disable Airplane mode on the device
    # Wait for sync
    And I wait for 10 seconds
    And I tap Retry button on the recent audio message in the conversation view
    # Retry button changes to Play button
    Then I verify the state of Retry button on the recent audio message in the conversation view is changed
    # Wait for the audio to be fully uploaded, then retry button changes to play button
    When I wait for 10 seconds
    And I remember the state of Play button on the recent audio message in the conversation view
    And I tap Play button on the recent audio message in the conversation view
    Then I verify the state of Play button on the recent audio message in the conversation view is changed

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131182 @C131177 @regression @rc @rc42
  Scenario Outline: Verify playing/cancelling sending voice message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
    And I remember the state of audio message preview seekbar
    And I tap on audio message play button
    And I verify the state of audio message preview seekbar is changed
    And I tap on audio message cancel button
    Then I see cursor toolbar
    And I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131194 @C131196 @C131202 @regression @rc @rc42
  Scenario Outline: Verify playing a received voice message + DO NOT playing in the background
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    And I see Audio Message container in the conversation view
    And I remember the state of recent audio message seekbar
    And I tap Play button on the recent audio message in the conversation view
    # Wait 10 seconds until the message is fully downloaded
    And I wait for 10 seconds
    Then I verify the state of recent audio message seekbar in the conversation view is changed
    # Wait until play button changes to pause button
    When I wait for 2 seconds
    When I remember the state of Pause button on the recent audio message in the conversation view
    And I minimize the application
    # Wait until Wire is minimized
    And I wait for 3 seconds
    And I restore the application
    Then I verify the state of Pause button on the recent audio message in the conversation view is changed
    When I long tap Audio Message container in the conversation view
    Then I do not see Copy button on the action mode bar
    When I tap Delete button on the action mode bar
    And I tap Delete button on the alert
    Then I do not see Audio Message container in the conversation view

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    |

  @C139849 @staging
  Scenario Outline: (AN-4067) Verify that play of audio message will be stopped by incoming voice call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    Given <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    When I see Audio Message container in the conversation view
    And I remember the state of recent audio message seekbar
    And I tap Play button on the recent audio message in the conversation view
    # Wait 10 seconds until the message is fully downloaded
    And I wait for 10 seconds
    Then I verify the state of recent audio message seekbar in the conversation view is changed
    When I remember the state of Pause button on the recent audio message in the conversation view
    And <Contact> calls me
    And I see incoming call from <Contact>
    And <Contact> stops calling me
    And I do not see incoming call
    Then I verify the state of Pause button on the recent audio message in the conversation view is changed

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName | CallBackend |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    | autocall    |

  @C139851 @staging
  Scenario Outline: (AN-4067) Verify that play of audio message will be stopped by incoming video call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    Given <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    When I see Audio Message container in the conversation view
    And I remember the state of recent audio message seekbar
    And I tap Play button on the recent audio message in the conversation view
    # Wait 10 seconds until the message is fully downloaded
    And I wait for 10 seconds
    Then I verify the state of recent audio message seekbar in the conversation view is changed
    When I remember the state of Pause button on the recent audio message in the conversation view
    When <Contact> starts a video call to me
    And I see incoming video call
    And <Contact> stops calling me
    And I do not see incoming video call
    Then I verify the state of Pause button on the recent audio message in the conversation view is changed

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName | CallBackend |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    | chrome      |

  @C139852 @staging
  Scenario Outline: (AN-4107) Verify that record of audio message will be stopped by incoming voice/video call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I long tap Audio message button from cursor toolbar without releasing my finger
    And I wait for 3 seconds
    And <Contact> calls me
    And I see incoming call from <Contact>
    And <Contact> stops calling me
    And I do not see incoming call
    Then I see cancel button on audio message recorder

    Examples:
      | Name      | Contact   | CallBackend |
      | user1Name | user2Name | autocall    |