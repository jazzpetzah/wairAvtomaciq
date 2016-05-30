Feature: Audio Message

  @C131173 @staging
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

  @C131179 @staging  @C131175 @C131176
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

  @C131180 @staging
  Scenario Outline: Verify sending voice message by long tap > release the humb > tap on the check ion
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

    Examples:
      | Name      | Contact   | TapDuration |
      | user1Name | user2Name | 5           |

  @C131188 @staging
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

  @C131192 @C131193 @staging
  Scenario Outline: (CM-958) Verify failing downloading voice message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    And I enable Airplane mode on the device
    # Wait for network is totally disabled
    And I wait for 3 seconds
    And I remember the state of Play button on the recent audio message in the conversation view
    And I tap Play button on the recent audio message in the conversation view
    # Wait for the button to get retry glyph
    And I wait for 3 seconds
    Then I verify the state of Play button on the recent audio message in the conversation view is changed
    When I disable Airplane mode on the device
    # Wait for sync
    And I wait for 10 seconds
    And I tap Retry button on the recent audio message in the conversation view
    # Wait for the audio to be fully downloaded, then retry button changes to play button
    And I wait for 5 seconds
    Then I verify the state of Play button on the recent audio message in the conversation view is not changed
    When I tap Play button on the recent audio message in the conversation view
    Then I verify the state of Play button on the recent audio message in the conversation view is changed

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    |

  @C131183 @C131184 @staging
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

  @C131182 @staging @C131177
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

  @C131194 @staging
  Scenario Outline: Verify playing a received voice message
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
    Then I verify the state of recent audio message seekbar in the conversation view is changed

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    |