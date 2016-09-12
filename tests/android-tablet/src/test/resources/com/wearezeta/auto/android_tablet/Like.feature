Feature: Like

  @C246204 @staging
  Scenario Outline: I can like/unlike text message by heart/long tap/double tap
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I type the message "<Message>" in the Conversation view
    And I send the typed message in the Conversation view
    And I tap the Text message "<Message>" in the conversation view
    And I remember the state of like button
  # Tap heart like
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view
  # Tap heart unlike
    When I tap Like button in conversation view
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    And I verify the state of like button item is not changed
  # Double tap to like
    When I double tap the Text message "<Message>" in the conversation view
    Then I see Like description with expected text "<Name>" in conversation view
    And I verify the state of like button item is changed
  # Doulbe tap to unlike
    When I double tap the Text message "<Message>" in the conversation view
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    And I verify the state of like button item is not changed
  # Long tap to like
    When I long tap the Text message "<Message>" in the conversation view
    And I tap Like button on the message bottom menu
    Then I see Like description with expected text "<Name>" in conversation view
    And I verify the state of like button item is changed
  # Long tap to unlike
    When I long tap the Text message "<Message>" in the conversation view
    And I tap Unlike button on the message bottom menu
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    And I verify the state of like button item is not changed

    Examples:
      | Name      | Contact   | MessageStatus | Message |
      | user1Name | user2Name | Sent          | OMG     |

  @C246205 @staging
  Scenario Outline: I can like link by heart (Portrait)
    Given There are 2 users where <Name> is me
    Given I rotate UI to portrait
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I type the message "<Url>" in the Conversation view
    And I send the typed message in the Conversation view
    And I tap Link Preview container in the conversation view
    And I press Back button until Wire app is in foreground in 10 seconds
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | Url                     |
      | user1Name | user2Name | http://www.facebook.com |

  @C246206 @staging
  Scenario Outline: I can like a picture by heart
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I see a new picture in the Conversation view
    And I tap Image container in the conversation view
    And I scroll to the bottom of the Conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C246207 @staging
  Scenario Outline: I can like sketch bz heart/long tap/double tap (portrait)
    Given There are 2 users where <Name> is me
    Given I rotate UI to portrait
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I tap Sketch button from cursor toolbar
    And I draw a sketch with 1 colors on Sketch page
    And I tap Send button on Sketch page
    And I scroll to the bottom of the Conversation view
    And I tap Image container in the conversation view
    And I remember the state of like button
    # Tap heart like
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view
    # Tap heart unlike
    When I tap Like button in conversation view
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    And I verify the state of like button item is not changed
    # Double tap to like
    When I double tap Image container in the conversation view
    Then I see Like description with expected text "<Name>" in conversation view
    And I verify the state of like button item is changed
    # Doulbe tap to unlike
    When I double tap Image container in the conversation view
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    And I verify the state of like button item is not changed
    # Long tap to like
    When I long tap Image container in the conversation view
    And I tap Like button on the message bottom menu
    Then I see Like description with expected text "<Name>" in conversation view
    And I verify the state of like button item is changed
    # Long tap to unlike
    When I long tap Image container in the conversation view
    And I tap Unlike button on the message bottom menu
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    And I verify the state of like button item is not changed

    Examples:
      | Name      | Contact   | MessageStatus |
      | user1Name | user2Name | Sent          |

  @C246208 @staging
  Scenario Outline: I can like Soundcloud
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I type the message "<SoundCloudLink>" in the Conversation view
    And I send the typed message in the Conversation view
    And I scroll to the bottom of the Conversation view
    And I tap Soundcloud container in the conversation view
    And I scroll to the bottom of the Conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | SoundCloudLink                                   |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |

  @C246209 @staging
  Scenario Outline: I can like youtube (portrait)
    Given There are 2 users where <Name> is me
    Given I rotate UI to portrait
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I type the message "<YoutubeLink>" in the Conversation view
    And I send the typed message in the Conversation view
    And I tap Youtube container in the conversation view
    And I scroll to the bottom of the Conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | YoutubeLink                                 |
      | user1Name | user2Name | https://www.youtube.com/watch?v=wTcNtgA6gHs |

  @C246210 @staging
  Scenario Outline: I can like location
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given User <Contact> shares his location to user Myself via device <DeviceName>
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I tap Share Location container in the conversation view
    And I press Back button until Wire app is in foreground in 10 seconds
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |

  @C246211 @staging
  Scenario Outline: I can like audio message
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I scroll to the bottom of the Conversation view
    And I tap Audio Message container in the conversation view
    And I scroll to the bottom of the Conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    |

  @C246212 @staging
  Scenario Outline: I can like video message
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given I push <FileSize> video file having name "<FileFullName>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I tap Video message button from cursor toolbar
    And I scroll to the bottom of the Conversation view
    And I tap Video Message container in the conversation view
    And I scroll to the bottom of the Conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | FileSize | FileFullName     |
      | user1Name | user2Name | 1.00MB   | random_video.mp4 |

  @C246213 @staging
  Scenario Outline: I can like file transfer
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given I sign in using my email
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I tap File button from cursor toolbar
    # Wait for file uploaded
    And I wait for 5 seconds
    And I tap File Upload container in the conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | FileName  | FileSize | FileExtension |
      | user1Name | user2Name | qa_random | 1.00MB   | txt           |

  @C246214 @staging
  Scenario Outline: Verify like icon is visible and sorted liker name next to the like icon, and I could like it.
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap the conversation <Contact>
    When I type the message "<Message>" in the Conversation view
    And I send the typed message in the Conversation view
    And User <Contact> likes the recent message from user Myself via device <ContactDevice>
    Then I see Like description with expected text "<Contact>" in conversation view
    And I see Like button in conversation view
    When I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>, <Contact>" in conversation view

    Examples:
      | Name      | Contact   | Message | ContactDevice |
      | user1Name | user2Name | Hi      | Device1       |

  @C246215 @staging
  Scenario Outline: I see likers count instead of names and first/second likes avatars
    Given There are 5 users where <Name> is me
    Given I rotate UI to landscape
    Given <Contact1> is connected to Myself,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1> has group chat <Group> with Myself,<Contact2>,<Contact3>,<Contact4>
    Given User <Contact1> adds new device <D1>
    Given User <Contact2> adds new device <D2>
    Given User <Contact3> adds new device <D3>
    Given User <Contact4> adds new device <D4>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap the conversation <Group>
    And I type the message "<Message>" in the Conversation view
    And I send the typed message in the Conversation view
    When User <Contact1> likes the recent message from group conversation <Group> via device <D1>
    And User <Contact2> likes the recent message from group conversation <Group> via device <D2>
    And User <Contact3> likes the recent message from group conversation <Group> via device <D3>
    And User <Contact4> likes the recent message from group conversation <Group> via device <D4>
    Then I see Like description with expected text "4 people" in conversation view
    And I see First like avatar in conversation view
    And I see Second like avatar in conversation view
    When I tap First like avatar in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Group     | Message | D1 | D2 | D3 | D4 |
      | user1Name | user2Name | user3Name | user4Name | user5Name | LikeGroup | Hi      | D1 | D2 | D3 | D4 |

