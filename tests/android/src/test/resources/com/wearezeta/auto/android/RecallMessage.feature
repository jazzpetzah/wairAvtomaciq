Feature: Recall Message

  @C202326 @C202328 @staging
  Scenario Outline: Verify I can delete my message everywhere (1:1) (myview and other view)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact1> adds new device <ContactDevice>
    Given I see Conversations list with conversations
    # Delete from otherview
    When I tap on conversation name <Contact1>
    And User <Contact1> send encrypted message "<Message>" via device <ContactDevice> to user Myself
    And I see the message "<Message>" in the conversation view
    And User <Contact1> delete the recent message everywhere from user Myself via device <ContactDevice>
    Then I do not see the message "<Message>" in the conversation view
    # C202328
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
    # TODO: Add check for Should not see trashcan in my view when I am the deleter

    Examples:
      | Name      | Contact1  | Message           | ContactDevice | MySecondDevice | Message2 |
      | user1Name | user2Name | DeleteTextMessage | Device2       | Device1        | Del2     |

  @C202327 @C202329 @staging
  Scenario Outline: Verify I can delete my message everywhere (group) (myview and other view)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact1> adds new device <ContactDevice>
    Given I see Conversations list with conversations
   # Delete from otherview
    When I tap on conversation name <Group>
    And User <Contact1> send encrypted message "<Message>" via device <ContactDevice> to group conversation <Group>
    And I see the message "<Message>" in the conversation view
    And User <Contact1> delete the recent message everywhere from group conversation <Group> via device <ContactDevice>
    Then I do not see the message "<Message>" in the conversation view
    # C202329
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
    # TODO: Add check for Should not see trashcan in my view when I am the deleter

    Examples:
      | Name      | Contact1  | Contact2  | Group  | Message           | ContactDevice | MySecondDevice | Message2 |
      | user1Name | user2Name | user3Name | TGroup | DeleteTextMessage | Device2       | Device1        | Del2     |

  @C202332 @staging
  Scenario Outline: Verify I can delete everywhere works for images
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> adds new device <ContactDevice>
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    And I long tap the recent picture in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see any pictures in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | ContactDevice |
      | user1Name | user2Name | Device1       |

  @C202333 @staging
  Scenario Outline: Verify delete everywhere works for giphy
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> adds new device <ContactDevice>
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "<Message>"
    And I click on the GIF button
    Then I see giphy preview page
    When I click on the giphy send button
    And I long tap the recent picture in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see any pictures in the conversation view
    And I see the message "<Message> · via giphy.com" in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds

    Examples:
      | Name      | Contact   | Message | ContactDevice |
      | user1Name | user2Name | Yo      | Device1       |

  @C202334 @staging
  Scenario Outline: Verify delete everywhere works for link preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> adds new device <ContactDevice>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Link>" and send it
    And I long tap Link Preview container in the conversation view
    And User <Contact> remember the recent message from user Myself via device <ContactDevice>
    And I tap Delete for everyone button on the message bottom menu
    And I tap Delete button on the alert
    Then I do not see Link Preview container in the conversation view
    And User <Contact> see the recent message from user Myself via device <ContactDevice> is changed in 15 seconds


    Examples:
      | Name      | Contact   | Link                    | ContactDevice |
      | user1Name | user2Name | http://www.facebook.com | Device1       |

  @C202335 @staging
  Scenario Outline: Verify delete everywhere works for Share location
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> adds new device <ContactDevice>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
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

  @C202336 @staging
  Scenario Outline: Verify delete everywhere works for file sharing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> adds new device <ContactDevice>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
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

  @C202337 @staging
  Scenario Outline: Verify delete everywhere works for audio messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> adds new device <ContactDevice>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I long tap Audio message button <TapDuration> seconds from cursor toolbar
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

  @C202338 @staging
  Scenario Outline: Verify delete everywhere works for video messages
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I push <FileSize> video file having name "<FileFullName>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> adds new device <ContactDevice>
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
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
      | user1Name | user2Name | 26.00MB  | random_video.mp4 | Device1       |

  @C202330 @C202331 @staging
  Scenario Outline: Verify deleting everywhere is synchronised across own devices when they are online (1:1 and group)
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
    When User Myself delete the recent message everywhere from user <Contact1> via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    When I tap Back button from top toolbar
    And I tap on conversation name <GroupChatName>
    And User Myself send encrypted message "<Message>" via device <Device> to group conversation <GroupChatName>
    Then I see the message "<Message>" in the conversation view
    When User Myself delete the recent message everywhere from group conversation <GroupChatName> via device <Device>
    Then I do not see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message           | Device  | ContactDevice | GroupChatName |
      | user1Name | user2Name | user3Name | DeleteTextMessage | Device1 | Device2       | MyGroup       |