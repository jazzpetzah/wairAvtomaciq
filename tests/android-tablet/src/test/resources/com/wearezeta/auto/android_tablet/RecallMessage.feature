Feature: Recall Message

  @C246267 @C246269 @regression
  Scenario Outline:  Verify I can delete my message everywhere (1:1) (myview + others view)
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new device <ContactDevice>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    # Delete from otherview
    When I tap the conversation <Contact1>
    And User <Contact1> send encrypted message "<Message>" via device <ContactDevice> to user Myself
    And I see the message "<Message>" in the conversation view
    And User <Contact1> delete the recent message everywhere from user Myself via device <ContactDevice>
    Then I do not see the message "<Message>" in the conversation view
    And I see the trashcan next to the name of <Contact1> in the conversation view
    # Delete from my view
    When User Myself adds new device <MySecondDevice>
    And User Myself send encrypted message "<Message2>" via device <MySecondDevice> to user <Contact1>
    And I see the message "<Message2>" in the conversation view
    And User <Contact1> remember the recent message from user Myself via device <ContactDevice>
    And I long tap the Text message "<Message2>" in the conversation view
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see the message "<Message2>" in the conversation view
    And User <Contact1> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds
    And I do not see the trashcan next to the name of Myself in the conversation view

    Examples:
      | Name      | Contact1  | Message           | ContactDevice | MySecondDevice | Message2 |
      | user1Name | user2Name | DeleteTextMessage | Device2       | Device1        | Del2     |

  @C246268 @C246270 @regression
  Scenario Outline: Verify I can delete my message everywhere(group) (myview + others view)
    Given There are 3 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given User <Contact1> adds new device <ContactDevice>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
   # Delete from otherview
    When I tap the conversation <Group>
    And User <Contact1> send encrypted message "<Message>" via device <ContactDevice> to group conversation <Group>
    And I see the message "<Message>" in the conversation view
    And User <Contact1> delete the recent message everywhere from group conversation <Group> via device <ContactDevice>
    Then I do not see the message "<Message>" in the conversation view
    And I see the trashcan next to the name of <Contact1> in the conversation view
   # Delete from my view
    When User Myself adds new device <MySecondDevice>
    And User Myself send encrypted message "<Message2>" via device <MySecondDevice> to group conversation <Group>
    And I see the message "<Message2>" in the conversation view
    And User <Contact1> remember the recent message from group conversation <Group> via device <ContactDevice>
    And I long tap the Text message "<Message2>" in the conversation view
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see the message "<Message2>" in the conversation view
    And User <Contact1> see the recent message from group conversation <Group> via device <ContactDevice> is changed in 15 seconds
    And I do not see the trashcan next to the name of Myself in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Group  | Message           | ContactDevice | MySecondDevice | Message2 |
      | user1Name | user2Name | user3Name | TGroup | DeleteTextMessage | Device2       | Device1        | Del2     |


  @C246271 @regression
  Scenario Outline: Verify I can delete everywhere works for images
    Given There are 2 users where <Name> is me
    Given I push local file named "<FileName>" to the device
    Given I rotate UI to landscape
    Given <Contact> is connected to me
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Gallery Camera button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I long tap Image container in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see any new picture in the Conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | ContactDevice | FileName       |
      | user1Name | user2Name | Device1       | avatarTest.png |

  @C246272 @regression
  Scenario Outline: Verify delete everywhere works for giphy
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given <Contact> is connected to me
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I tap on text input
    And I type the message "<Message>" in the Conversation view
    And I tap Giphy button in the conversation view
    Then I see Giphy preview page
    When I tap Send button on the Giphy preview page
    And I long tap Image container in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see any new picture in the Conversation view
    And I see the message "<Message> · via giphy.com" in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | Message | ContactDevice |
      | user1Name | user2Name | Yo      | Device1       |

  @C246273 @regression
  Scenario Outline: Verify delete everywhere works for link preview
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap the conversation <Contact>
    When I type the message "<Link>" in the Conversation view
    And I send the typed message in the Conversation view
    And I hide keyboard
    And I long tap Link Preview container in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Link Preview container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | Link                    | ContactDevice |
      | user1Name | user2Name | http://www.facebook.com | Device1       |

  @C246274 @regression
  Scenario Outline: Verify delete everywhere works for Share location
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap the conversation <Contact>
    When I tap Share location button from cursor toolbar
    And I tap Send button on Share Location page
    And I long tap Share Location container in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Share Location container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | ContactDevice |
      | user1Name | user2Name | device1       |

  @C246275 @regression
  Scenario Outline: Verify delete everywhere works for file sharing
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap the conversation <Contact>
    When I tap File button from cursor toolbar
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I long tap File Upload container in the conversation view
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see File Upload container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | FileName  | FileExtension | FileSize | ContactDevice | UploadingTimeout |
      | user1Name | user2Name | qa_random | txt           | 1.00MB   | device1       | 20               |

  @C246276 @regression
  Scenario Outline: Verify delete everywhere works for audio messages
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap the conversation <Contact>
    When I long tap Audio message button from cursor toolbar for <TapDuration> seconds
    And I tap audio recording Send button
    # Wait for the audio to be fully uploaded
    And I wait for 15 seconds
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I long tap Audio Message container in the conversation view
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Audio Message container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | TapDuration | ContactDevice |
      | user1Name | user2Name | 5           | Device1       |

  @C246277 @regression
  Scenario Outline: Verify delete everywhere works for video messages
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <ContactDevice>
    Given I sign in using my email
    Given I push <FileSize> video file having name "<FileFullName>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap the conversation <Contact>
    When I tap Video message button from cursor toolbar
    Then I see Video Message container in the conversation view
  # Wait for the video to be fully uploaded
    And I wait for 20 seconds
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I long tap Video Message container in the conversation view
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Video Message container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | FileSize | FileFullName     | ContactDevice |
      | user1Name | user2Name | 1.00MB   | random_video.mp4 | Device1       |