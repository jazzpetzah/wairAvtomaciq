Feature: Like

  @C226019 @regression @rc
  Scenario Outline: I can like/unlike message from message tool menu
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Txt>" and send it
    And I long tap the Text message "<Txt>" in the conversation view
    And I tap Like button on the message bottom menu
    # C226091
    Then I see Like description with expected text "<Name>" in conversation view
    And I see Like button in conversation view
    When I remember the state of like button
    And I long tap the Text message "<Txt>" in the conversation view
    And I tap Unlike button on the message bottom menu
    # C226035
    Then I verify the state of like button item is changed
    And I see Message status with expected text "<MessageStatus>" in conversation view

    Examples:
      | Name      | Contact   | Txt | MessageStatus | ContactDevice |
      | user1Name | user2Name | Hi  | Sent          | D1            |

  @C226018 @C226020 @regression @rc
  Scenario Outline: I can unlike/like message by tap on like icon & I can like text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given I type the message "<Txt>" and send it
    # C226018
    When I tap the Text message "<Txt>" in the conversation view
    And I remember the state of like button
    Then I see Like button in conversation view
    # C226020
    When I tap Like button in conversation view
    Then I verify the state of like button item is changed
    # C226018
    When I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Message status with expected text "<MessageStatus>" in conversation view

    Examples:
      | Name      | Contact   | Txt | MessageStatus | ContactDevice |
      | user1Name | user2Name | Hi  | Sent          | D1            |

  @C226036 @regression @rc
  Scenario Outline: I can double tap on txt to like and unlike
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Txt>" and send it
    And I double tap the Text message "<Txt>" in the conversation view
    Then I see Like button in conversation view
    And I see Like description with expected text "<Name>" in conversation view
    When I remember the state of like button
    And I double tap the Text message "<Txt>" in the conversation view
    Then I verify the state of like button item is changed
    And I see Message status with expected text "<MessageStatus>" in conversation view

    Examples:
      | Name      | Contact   | Txt | MessageStatus | ContactDevice |
      | user1Name | user2Name | Hi  | Sent          | D1            |

  @C226040 @regression @rc
  Scenario Outline: If message was liked by somebody, like icon is visible and sorted liker name next to the like icon, and I could like it.
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Message>" and send it
    And User <Contact> likes the recent message from user Myself via device <ContactDevice>
    # C226040
    Then I see Like description with expected text "<Contact>" in conversation view
    And I see Like button in conversation view
    When I remember the state of like button
    And I tap Like button in conversation view
    # C226033
    Then I verify the state of like button item is changed
    # C226043
    And I see Like description with expected text "<Name>, <Contact>" in conversation view

    Examples:
      | Name      | Contact   | Message | ContactDevice |
      | user1Name | user2Name | Hi      | Device1       |

  @C226045 @C226048 @regression @rc
  Scenario Outline: Likes should be reset if I edited message / also could like again
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given User <Contact1> adds new devices <ContactDevice>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User Myself adds new device <Device>
    Given I see Conversations list with conversations
    # C226045
    When I tap on conversation name <Contact1>
    And I type the message "<Message>" and send it
    And I tap the Text message "<Message>" in the conversation view
    And I tap Like button in conversation view
    Then I see Like description with expected text "<Name>" in conversation view
    When User Myself edits the recent message to "<NewMessage>" from user <Contact1> via device <Device>
    Then I see the message "<NewMessage>" in the conversation view
    # TODO : remove it once msg meta show automatic
    When I tap the Text message "<NewMessage>" in the conversation view
    Then I see Message status with expected text "<MessageStatus>" in conversation view
    # C226048
    When I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact1  | Message | Device | NewMessage | MessageStatus | ContactDevice |
      | user1Name | user2Name | Yo      | D1     | Hello      | Sent          | D2            |

  @C226049 @C226037 @regression @rc
  Scenario Outline: Verify local delete for my/others message doesn't reappear after someone liked it (negative)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <Device>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # C226049
    When I type the message "<Message>" and send it
    And I long tap the Text message "<Message>" in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And User <Contact> likes the recent message from user Myself via device <Device>
    Then I do not see the message "<Message>" in the conversation view
    # C226037
    When User <Contact> sends encrypted message "<OtherMessage>" via device <Device> to user Myself
    # TODO: Remove following 1 lines once the message present automatic when receive a message.
    And I tap the Text message "<OtherMessage>" in the conversation view
    Then I see Like description with expected text "Tap to like" in conversation view
    # C226050
    When I long tap the Text message "<OtherMessage>" in the conversation view
    And I tap Delete only for me button on the message bottom menu
    And I tap Delete button on the alert
    And User <Contact> likes the recent message from user Myself via device <Device>
    Then I do not see the message "<OtherMessage>" in the conversation view

    Examples:
      | Name      | Contact   | Message | Device  | OtherMessage |
      | user1Name | user2Name | Yo      | Device1 | OMG          |

  @C234618 @regression
  Scenario Outline: Verify receiving a like in a conversation which history was removed (negative)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <Device>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Message>" and send it
    And I navigate back from conversation
    And I swipe right on a <Contact>
    And I select DELETE from conversation settings menu
    And I press DELETE on the confirm alert
    And User <Contact> likes the recent message from user Myself via device <Device>
    Then I see Conversations list with no conversations

    Examples:
      | Name      | Contact   | Message | Device  |
      | user1Name | user2Name | Yo      | Device1 |

  @C226047 @regression
  Scenario Outline: Verify sending like to a person who block me in a 1:1 conversation (negative)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <Device>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I type the message "<Message>" and send it
    And User <Contact> blocks user Myself
    And I tap the Text message "<Message>" in the conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | Message | Device  |
      | user1Name | user2Name | Yo      | Device1 |

  @C226041 @C226042 @regression @rc
  Scenario Outline: I see likers count instead of names with first/second liker avatars, and could open likers list
    Given There are 5 users where <Name> is me
    Given <Contact1> is connected to Myself,<Contact2>,<Contact3>,<Contact4>
    Given <Contact1> has group chat <Group> with Myself,<Contact2>,<Contact3>,<Contact4>
    Given User <Contact1> adds new device <D1>
    Given User <Contact2> adds new device <D2>
    Given User <Contact3> adds new device <D3>
    Given User <Contact4> adds new device <D4>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Group>
    Given I type the message "<Message>" and send it
    When User <Contact1> likes the recent message from group conversation <Group> via device <D1>
    And User <Contact2> likes the recent message from group conversation <Group> via device <D2>
    And User <Contact3> likes the recent message from group conversation <Group> via device <D3>
    And User <Contact4> likes the recent message from group conversation <Group> via device <D4>
    # C226041
    Then I see Like description with expected text "4 people" in conversation view
    # С232581
    And I see First like avatar in conversation view
    And I see Second like avatar in conversation view
    When I tap First like avatar in conversation view
    # C226042
    Then I see user <Contact1> in Liker list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Group     | Message | D1 | D2 | D3 | D4 |
      | user1Name | user2Name | user3Name | user4Name | user5Name | LikeGroup | Hi      | D1 | D2 | D3 | D4 |

  @C226022 @regression @rc
  Scenario Outline: I can like a picture
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I see a picture in the conversation view
    And I tap Image container in the conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C226024 @regression @rc
  Scenario Outline: I can like a sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Sketch button from cursor toolbar
    And I draw a sketch with 1 colors
    And I send my sketch
    And I do not see Message status with expected text "Sending" in conversation view
    And I tap Image container in the conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C226029 @regression
  Scenario Outline: I can like a shared location
    Given I am on Android with Google Location Service
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> shares his location to user Myself via device <DeviceName>
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Share Location container in the conversation view
    And I press Back button until Wire app is in foreground in 10 seconds
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |

  @C226021 @regression @rc
  Scenario Outline: I can like link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<Url>" and send it
    And I tap Link Preview container in the conversation view
    And I press Back button until Wire app is in foreground in 10 seconds
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | Url                     |
      | user1Name | user2Name | http://www.facebook.com |

  @C226030 @regression @rc
  Scenario Outline: I can like audio message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    # TODO: Remove following 3 lines once the message present automatic when receive a message.
    And I scroll to the bottom of conversation view
    And I tap Audio Message container in the conversation view
    And I scroll to the bottom of conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | FileName | MIMEType  | DeviceName |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1    |

  @C226031 @regression @rc
  Scenario Outline: I can like video message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    # TODO: Remove following 3 lines once the message present automatic when receive a message.
    And I scroll to the bottom of conversation view
    And I tap Video Message container in the conversation view
    And I scroll to the bottom of conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | FileName    | MIMEType  | DeviceName |
      | user1Name | user2Name | testing.mp4 | video/mp4 | Device1    |

  @C226032 @regression @rc
  Scenario Outline: I can like file transfer
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
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

  @C226027 @regression @rc
  Scenario Outline: I can like youtube
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<YoutubeLink>" and send it
    And I tap Youtube container in the conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | YoutubeLink                                 |
      | user1Name | user2Name | https://www.youtube.com/watch?v=wTcNtgA6gHs |

  @C226026 @regression @rc
  Scenario Outline: I can like Soundcloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I type the message "<SoundCloudLink>" and send it
    And I scroll to the bottom of conversation view
    And I tap Soundcloud container in the conversation view
    And I scroll to the bottom of conversation view
    And I remember the state of like button
    And I tap Like button in conversation view
    Then I verify the state of like button item is changed
    And I see Like description with expected text "<Name>" in conversation view

    Examples:
      | Name      | Contact   | SoundCloudLink                                   |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |

  @C226051 @regression @rc
  Scenario Outline: Verify receiving like from a blocked person in a group conversation
    Given There are 3 users where <Name> is me
    Given User <Contact1> adds new device <ContactDevice>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <Group> with <Contact1>,<Contact2>
    Given User Myself blocks user <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Group>
    And I type the message "<Message>" and send it
    And User <Contact1> likes the recent message from group conversation <Group> via device <ContactDevice>
    Then I see Like description with expected text "<Contact1>" in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Message | Group      | ContactDevice |
      | user1Name | user2Name | user3Name | M1      | BlockGroup | D1            |