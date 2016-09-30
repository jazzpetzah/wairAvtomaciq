Feature: Notifications

  @C147866 @regression @rc
  Scenario Outline: Verify push notifications are received after successful registration
    Given I am on Android 4.4 or better
    Given I see welcome screen
    Given I input a new phone number for user <Name>
    Given I input the verification code
    Given I input my name
    Given I select to choose my own picture
    Given I select Camera as picture source
    Given I tap Take Photo button on Take Picture view
    Given I tap Confirm button on Take Picture view
    Given User <Name> is me
    Given There is 1 additional user
    Given Myself is connected to <Contact>
    When I see Conversations list with name <Contact>
    And I minimize the application
    And User <Contact> sends encrypted message <Message> to user Myself
    Then I see the message "<Message>" in push notifications list

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | hello   |

  @C131187 @regression
  Scenario Outline: (CM-1071) Verify push notifications after receiving any type of message
    Given I am on Android with Google Location Service
    Given I am on Android 4.4 or better
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I minimize the application
    # Txt Msg
    When User <Contact> send encrypted message "<TxtMsg>" via device <DeviceName> to user Myself
    Then I see the message "<TxtMsg>" in push notifications list
    And I clear Wire push notifications
    # Img Msg
    When User <Contact> sends encrypted image <Image> to single user conversation Myself
    Then I see the message "Shared a picture" in push notifications list
    And I clear Wire push notifications
    # Ping
    When User <Contact> securely pings conversation Myself
    Then I see the message "Pinged" in push notifications list
    And I clear Wire push notifications
    # Location
    When User <Contact> shares his location to user Myself via device <DeviceName>
    Then I see the message "Shared a location" in push notifications list
    And I clear Wire push notifications
    # File asset
    When <Contact> sends <FileSize> file having name "<FileName>" and MIME type "<FileMIMEType>" via device <DeviceName> to user Myself
    Then I see the message "Shared a file" in push notifications list
    And I clear Wire push notifications
    # Video msg
    When <Contact> sends local file named "<VideoFileName>" and MIME type "<VideoMIMEType>" via device <DeviceName> to user Myself
    Then I see the message "Shared a video message" in push notifications list
    And I clear Wire push notifications
    # Audio msg
    When <Contact> sends local file named "<AudioFileName>" and MIME type "<AudioMIMEType>" via device <DeviceName> to user Myself
    Then I see the message "Shared an audio message" in push notifications list

    Examples:
      | Name      | Contact   | VideoFileName | VideoMIMEType | DeviceName | AudioFileName | AudioMIMEType | TxtMsg | Image       | FileSize | FileName      | FileMIMEType |
      | user1Name | user2Name | testing.mp4   | video/mp4     | Device1    | test.m4a      | audio/mp4     | OMG    | testing.jpg | 3.00MB   | qa_random.txt | text/plain   |

  @C165125 @regression @rc
  Scenario Outline: Verify no GCM notifications are shown for muted chats
    Given I am on Android 4.4 or better
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given User <Contact> sends encrypted message <Message> to user Myself
    Given I swipe right on a <Contact>
    Given I select MUTE from conversation settings menu
    Given Conversation <Contact> is muted
    When I minimize the application
    And I wait for 2 seconds
    And User <Contact> sends encrypted message <Message> to user Myself
    Then I do not see the message "<Message>" in push notifications list

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | hello   |

  @C248344 @regression @GCMToken
  Scenario Outline: Verify unregister push token at backend and see if client can resume getting notifications by itself
    Given I am on Android with GCM Service
    Given I am on Android 4.4 or better
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I minimize the application
    And I unregister GCM push token in 10 seconds
    And User <Contact> sends encrypted message "<Message>" to user Myself
    Then I do not see the message "<Message>" in push notifications list
    When I restore the application
    And I wait for 2 seconds
    And I minimize the application
    And User <Contact> sends encrypted message "<Message2>" to user Myself
    Then I see the message "<Message2>" in push notifications list

    Examples:
      | Name      | Contact   | Message | Message2 |
      | user1Name | user2Name | Yo      | Nop      |

  @C226044 @regression
  Scenario Outline: When somebody likes my message - I receive notification (app in background)
    Given I am on Android 4.4 or better
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Message>" and send it by cursor Send button
    And I minimize the application
    And User <Contact> likes the recent message from user Myself via device <ContactDevice>
    Then I see the message "<Notification>" in push notifications list

    Examples:
      | Name      | Contact   | Message | ContactDevice | Notification |
      | user1Name | user2Name | Hi      | Device1       | your message |