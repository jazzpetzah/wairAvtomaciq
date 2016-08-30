Feature: Edit Message

  @C202349 @regression @fastLogin
  Scenario Outline: Verify I cannot edit/delete everywhere another users message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 encrypted message to user Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see 1 default message in the conversation view
    When I long tap default message in conversation view
    Then I do not see Edit badge item
    And I tap on Delete badge item
    And I do not see Delete for Everyone item in Delete menu

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C206256 @regression @fastLogin
  Scenario Outline: Verify impossibility of editing/deleting everywhere message after leaving/being removed from a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <GroupChatName>
    And I type the default message and send it
    And <Contact1> removes Myself from group chat <GroupChatName>
    And I long tap default message in conversation view
    Then I do not see Edit badge item
    And I tap on Delete badge item
    And I do not see Delete for Everyone item in Delete menu

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | RemoveToEdit  |

  @C202354 @regression @fastLogin
  Scenario Outline: Verify I can undo my editing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I type the "<Text>" message
    And I tap Undo button on Edit control
    Then I see the default message in the conversation input

    Examples:
      | Name      | Contact   | Text    |
      | user1Name | user2Name | message |

  @C202350 @regression @fastLogin
  Scenario Outline: Verify I can cancel editing a message by button, by click/tap on something else
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I type the "<Text>" message
    And I tap Cancel button on Edit control
    Then I do not see Confirm button on Edit Message control
    And I see 1 default message in the conversation view
    And I see input placeholder text
    When I long tap default message in conversation view
    And I tap on Edit badge item
    And I type the "<Text>" message
    And I tap Audio Call button
    And I tap Leave button on Calling overlay
    Then I do not see Confirm button on Edit Message control
    And I see 1 default message in the conversation view
    And I see input placeholder text

    Examples:
      | Name      | Contact   | Text    |
      | user1Name | user2Name | message |

  @C206271 @regression @fastLogin
  Scenario Outline: Verify I can delete message for everyone editing it with nothing/space
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "  " message
    And I tap Confirm button on Edit control
    Then I see 0 default messages in the conversation view
    And User <Contact> verifies that the most recent message type from user Myself is RECALLED via device <DeviceName>

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | HisDevice  |

  @C202345 @regression @fastLogin
  Scenario Outline: Verify I can edit my message in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I type the default message and send it
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<Text>" message
    And I tap Confirm button on Edit control
    Then I see last message in the conversation view is expected message <Text>
    And I see 0 default messages in the conversation view

    Examples:
      | Name      | Contact   | Text |
      | user1Name | user2Name | new  |

  @C202372 @regression @fastLogin
  Scenario Outline: Verify editing link with a preview
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I type the "<FacebookPrefix> <FacebookLink>" message and send it
    And I long tap on link preview in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<WirePrefix> <WireLink>" message
    And I tap Confirm button on Edit control
    Then I see link preview container in the conversation view
    And I see link preview source is equal to <WireLink>

    Examples:
      | Name      | Contact   | FacebookLink        | FacebookPrefix | WirePrefix | WireLink        |
      | user1Name | user2Name | http://facebook.com | Check FB       | Look for   | http://wire.com |

  @C202352 @regression @fastLogin
  Scenario Outline: Verify I can edit a message multiple times in a row (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given User <Contact1> adds new device <DeviceName>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I type the default message and send it
    Then User <Contact1> remembers the recent message from group conversation <GroupChatName> via device <DeviceName>
    When I long tap default message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<Text1>" message
    And I tap Confirm button on Edit control
    Then I see last message in the conversation view is expected message <Text1>
    And User <Contact1> sees the recent message from group conversation <GroupChatName> via device <DeviceName> is changed in 15 seconds
    And User <Contact1> remembers the recent message from group conversation <GroupChatName> via device <DeviceName>
    When I long tap "<Text1>" message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<Text2>" message
    And I tap Confirm button on Edit control
    Then I see last message in the conversation view is expected message <Text2>
    And I see 1 message in the conversation view
    And User <Contact1> sees the recent message from group conversation <GroupChatName> via device <DeviceName> is changed in 15 seconds

    Examples:
      | Name      | Contact1  | DeviceName | Contact2  | GroupChatName | Text1  | Text2  |
      | user1Name | user2Name | HisDevice  | user3Name | EditGroup     | Edit 1 | Edit 2 |

  @C202353 @regression @fastLogin
  Scenario Outline: Verify I can switch to edit another message while editing a message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I type the default message and send it
    And I type the "<Text2>" message and send it
    And I long tap default message in conversation view
    And I tap on Edit badge item
    And I long tap "<Text2>" message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<Text2Changed>" message
    And I tap Confirm button on Edit control
    Then I see last message in the conversation view is expected message <Text2Changed>
    And I see 1 default messages in the conversation view
    And I do not see the conversation view contains message <Text2>

    Examples:
      | Name      | Contact   | Text2 | Text2Changed |
      | user1Name | user2Name | msg2  | msgchg       |

  @C202347 @regression @fastLogin
  Scenario Outline: Verify I see changed message if message was edited from another device (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given User Myself adds new device <Device>
    Given User Myself sends encrypted message "<Message>" to user <Contact1>
    Given I see conversations list
    When I tap on contact name <Contact1>
    Then I see the conversation view contains message <Message>
    When User Myself edits the recent message to "<NewMessage>" from user <Contact1> via device <Device>
    Then I do not see the conversation view contains message <Message>
    And I see the conversation view contains message <NewMessage>

    Examples:
      | Name      | Contact1  | Message | Device  | NewMessage |
      | user1Name | user2Name | Hi      | Device1 | Hello      |

  @C202348 @regression @fastLogin
  Scenario Outline: Verify I see changed message if message was edited from another device (group)
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User Myself adds new device <Device>
    Given User Myself sends encrypted message "<Message>" to group conversation <GroupChatName>
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    Then I see the conversation view contains message <Message>
    When User Myself edits the recent message to "<NewMessage>" from group conversation <GroupChatName> via device <Device>
    Then I do not see the conversation view contains message <Message>
    And I see the conversation view contains message <NewMessage>

    Examples:
      | Name      | Contact1  | Device    | Contact2  | GroupChatName | Message | NewMessage |
      | user1Name | user2Name | HisDevice | user3Name | EditGroup     | Hi      | Hello      |

  @C202371 @regression @fastLogin
  Scenario Outline: Verify I can't edit picture/video/audio
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When User <Contact> sends file <VideoFileName> having MIME type <VideoMIMEType> to single user conversation <Name> using device <DeviceName>
    And I wait for 10 seconds
    And I long tap on video message in conversation view
    Then I do not see Edit badge item
    When User <Contact> sends file <AudioFileName> having MIME type <AudioMIMEType> to single user conversation <Name> using device <DeviceName>
    And I wait for 5 seconds
    And I long tap on audio message placeholder in conversation view
    Then I do not see Edit badge item
    When User <Contact> sends encrypted image <Picture> to single user conversation Myself
    And I wait for 5 seconds
    And I long tap on image in conversation view
    Then I do not see Edit badge item

    Examples:
      | Name      | Contact   | VideoFileName | VideoMIMEType | DeviceName | AudioFileName | AudioMIMEType | Picture     |
      | user1Name | user2Name | testing.mp4   | video/mp4     | Device1    | test.m4a      | audio/mp4     | testing.jpg |

  @C202355 @staging @fastLogin
  Scenario Outline: Verify edited message has an additional name and avatar and save its position in the conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends encrypted message "<Message1>" to user <Contact>
    Given User Myself sends encrypted message "<Message2>" to user <Contact>
    Given User Myself sends encrypted message "<Message3>" to user <Contact>
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap on text input
    Then I see Myself username exists in conversation view 1 time
    And I see message "<Message2>" is on 2 position in conversation view
    When I long tap "<Message2>" message in conversation view
    And I tap on Edit badge item
    And I clear conversation text input
    And I type the "<EditMessage>" message
    And I tap Confirm button on Edit control
    Then I see message "<EditMessage>" is on 2 position in conversation view
    And I see Myself username exists in conversation view 2 times

    Examples:
      | Name      | Contact   | Message1    | Message2    | Message3      | EditMessage    |
      | user1Name | user2Name | message one | message two | message three | edited message |