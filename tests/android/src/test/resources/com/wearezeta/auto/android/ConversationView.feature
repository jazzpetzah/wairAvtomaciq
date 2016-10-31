Feature: Conversation View

  @C688 @regression @rc
  Scenario Outline: Mute conversation from conversation view
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I tap options menu button
    And I tap MUTE conversation menu button
    And I tap back button
    And I tap back button
    Then Conversation <Contact1> is muted

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C414 @regression
  Scenario Outline: Verify unsilence the conversation from conversation view
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to me
    Given I sign in using my email or phone number
    Given <Contact1> is silenced to user <Name>
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given Conversation <Contact1> is muted
    When I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I tap options menu button
    And I tap UNMUTE conversation menu button
    And I tap back button
    And I navigate back from conversation
    Then Conversation <Contact1> is not muted

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C381 @regression
  Scenario Outline: Send Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "<Message>" and send it by cursor Send button
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C682 @regression @rc @legacy
  Scenario Outline: Send Camera picture to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C700 @regression @rc @legacy
  Scenario Outline: Create group conversation from 1:1
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I see <Contact1> user profile page
    And I tap add contact button
    And I tap on Search input on Search page
    And I type user name "<Contact2>" in search field
    And I see user <Contact2> in Search result list
    And I tap on user name found on Search page <Contact2>
    And I see Add to conversation button
    And I tap on Add to conversation button
    # Workaround for AN-4011, for following two steps
    And I tap conversation name from top toolbar
    And I tap back button
    Then I see group chat page with users <Contact1>,<Contact2>
    And I navigate back from conversation
    And I see group conversation with <Contact1>,<Contact2> in conversations list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C18044 @regression
  Scenario Outline: I see creation header when someone create group conversation with me
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>
    When I tap on conversation name <GroupChatName>
    # Workaround for AN-4011, for following two steps
    And I tap conversation name from top toolbar
    And I tap back button
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName      |
      | user1Name | user2Name | user3Name | MeAddedToGroupChat |

  @C684 @regression @rc
  Scenario Outline: Send message to group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And I tap on text input
    And I type the message "<Message>" and send it by cursor Send button
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @C671 @regression @rc
  Scenario Outline: Send Long Message to contact
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap on text input
    And I type the message "LONG_MESSAGE" and send it by cursor Send button
    Then I see the message "LONG_MESSAGE" in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C379 @regression @C183868
  Scenario Outline: Send existing image from gallery (portrait) in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C419 @regression
  Scenario Outline: I want to exit fullscreen view in landscape (rotations)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view
    And I tap Image container in the conversation view
    And I wait for 1 seconds
    And I tap on Fullscreen button on the recent image in the conversation view
    And I rotate UI to landscape
    And I tap Image Close button on Take Picture view
    Then I rotate UI to portrait
    And I navigate back from conversation
    And I see Conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C674 @regression @rc
  Scenario Outline: Send GIF format pic
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given User <Contact> sends encrypted image <GifName> to single user conversation Myself
    When I tap on conversation name <Contact>
    And I scroll to the bottom of conversation view
    Then I see a picture in the conversation view
    And I see the picture in the conversation is animated
    #workaround for AN-4574
    When User <Contact> sends encrypted message "<Message>" to user Myself
    And I tap Image container in the conversation view
    And I wait for 1 seconds
    And I tap on Fullscreen button on the recent image in the conversation view
    Then I see the picture in the preview is animated

    Examples:
      | Name      | Contact   | GifName      | Message |
      | user1Name | user2Name | animated.gif | Yo      |

  @C672 @regression @rc
  Scenario Outline: Send image with non default camera (portrait) in group chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap Add picture button from cursor toolbar
    And I tap Switch Camera button on Extended cursor camera overlay
    And I tap Take Photo button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C77948 @regression @rc
  Scenario Outline: Verify the upper toolbar and back arrow are visible
    Given There is 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    Then I see the upper toolbar
    And I tap Back button from top toolbar
    Then I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    Then I see the upper toolbar
    And I tap Back button from top toolbar
    Then I see Conversations list with conversations

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |

  @C77958 @regression
  Scenario Outline: Verify video call icon is not shown in a group conversation on the upper toolbar
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>
    When I tap on conversation name <GroupChatName>
    Then I see the audio call button in upper toolbar
    And I do not see the video call button in upper toolbar
    And I navigate back from conversation
    And I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    Then I see the audio call button in upper toolbar
    And I see the video call button in upper toolbar

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     |
      | user1Name | user2Name | user3Name | SendMessGroupChat |

  @C78372 @regression
  Scenario Outline: Verify title is not changed on receiving messages in other conversations
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And the conversation title should be "<Contact1>"
    And User <Contact2> sends encrypted message "<Message1>" to user Myself
    And I tap new message notification "<Message1>"
    Then I see the message "<Message1>" in the conversation view
    And the conversation title should be "<Contact2>"
    And I tap conversation name from top toolbar
    And I tap back button
    When I tap Back button from top toolbar
    And I tap on conversation name <Contact1>
    And User <Contact2> sends encrypted message "<Message2>" to user Myself
    And I see new message notification "<Message2>"
    Then the conversation title should be "<Contact1>"

    Examples:
      | Name      | Contact1  | Contact2  | Message1 | Message2 |
      | user1Name | user2Name | user3Name | Msg1     | Msg2     |

  @C111617 @regression @rc @legacy
  Scenario Outline: Verify cursor action buttons are shown together with a text field
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    Then I see cursor toolbar
    And I see text input

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C111622 @regression @rc
  Scenario Outline: Verify tooltip is shown in different condition
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap Ping button from cursor toolbar
    Then I see tooltip of text input
    When I tap on text input
    Then I see tooltip of text input
    When I type the message "<Message>"
    And I do not see tooltip of text input

    Examples:
      | Name      | Contact1  | Message |
      | user1Name | user2Name | testing |

  @C111631 @rc @regression
  Scenario Outline: Verify cursor and toolbar are not shown when I left/was removed from group and show when I was re-join the group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap options menu button
    And I tap LEAVE conversation menu button
    And I confirm leaving
    And I see Conversations list
    And I open Search UI
    And I type group name "<GroupChatName>" in search field
    And I tap on group found on Search page <GroupChatName>
    Then I see the upper toolbar
    And I do not see text input
    And I do not see cursor toolbar
    When User <Contact1> adds user Myself to group chat <GroupChatName>
    Then I see text input
    And I see cursor toolbar

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3Name | LeaveGroupChat |

  @C111635 @rc @regression
  Scenario Outline: Verify the cursor is not shown on the new device after you leave the group from another device
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Myself leave group chat <GroupChatName>
    Given Myself is unarchived group chat <GroupChatName>
    When I sign in using my email
    And I accept First Time overlay as soon as it is visible
    And I see Conversations list with conversations
    And I tap on conversation name <GroupChatName>
    Then I do not see text input
    And I do not see cursor toolbar

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3Name | LeaveGroupChat |

  @C250837 @C250838 @regression
  Scenario Outline: Verify cursor send button is visible when input is not empty although you switch conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I type the message "<Message>"
    Then I see Send button in cursor input
    When I navigate back from conversation
    And I tap on conversation name <Contact2>
    And I navigate back from conversation
    And I tap on conversation name <Contact1>
    Then I see Send button in cursor input

    Examples:
      | Name      | Contact1  | Contact2  | Message |
      | user1Name | user2Name | user3Name | YoNo    |

  @C250856 @regression
  Scenario Outline: Verify I see someone is typing in 1:1 conversation
    Given There are 2 users where <Name> is me
    Given User <Contact> adds new device <ContactDevice>
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And User <Contact> is typing in the conversation <Name>
    Then I see <Contact> is typing

    Examples:
      | Name      | Contact   | ContactDevice |
      | user1Name | user2Name | Device1       |

  @C250857 @unstable
  Scenario Outline: Verify I see someone are typing in group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given User <Contact1> adds new device <D1>
    Given User <Contact2> adds new device <D2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    #TODO: User <Contact1> is typing in the conversation <GroupChatName> is not stable on jenkins (nothing happens on device)
    And User <Contact1> is typing in the conversation <GroupChatName>
    And User <Contact2> is typing in the conversation <GroupChatName>
    Then I see <Contact1>,<Contact2> are typing

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | D1 | D2 |
      | user1Name | user2Name | user3Name | TypingGroup   | D1 | D2 |