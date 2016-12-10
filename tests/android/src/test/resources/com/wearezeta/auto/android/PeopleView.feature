Feature: People View

  @C402 @regression
  Scenario Outline: I can access user details page from group chat and see user name, email and photo
    Given There are 3 users where <Name> is me
    Given <Contact1> has an avatar picture from file <Picture>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> has a name <Contact1NewName>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I see Conversations list with name <Contact1>
    And I see Conversations list with name <Contact2>
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1NewName> on Group info page
    Then I see user name of user <Contact1> on Group connected user details page
    And I see unique user name of user <Contact1> on Group connected user details page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName   | Picture                      | Contact1NewName   |
      | user1Name | user2Name | user3Name | GroupInfoCheck2 | aqaPictureContact600_800.jpg | aqaPictureContact |

  @C685 @regression @rc @legacy
  Scenario Outline: Leave group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap open menu button on Group info page
    And I tap LEAVE button on Conversation options menu overlay page
    And I tap LEAVE button on Confirm overlay page
    Then I see Conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3Name | LeaveGroupChat |

  @C686 @regression @rc @legacy
  Scenario Outline: Remove from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I see Conversations list with name <Contact1>
    And I see Conversations list with name <Contact2>
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact2> on Group info page
    And I tap on remove button on Group connected user details page
    And I tap REMOVE button on Confirm overlay page
    Then I do not see participant <Contact2> on Group info page
    And I tap back button
    And I see message <Message> contact <Contact2> on group page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName       | Message     |
      | user1Name | user2Name | user3Name | RemoveFromGroupChat | YOU REMOVED |

  @C697 @regression @rc
  Scenario Outline: Verify correct group info page information
    Given There are 3 users where <Name> is me
    Given <Contact1> has an avatar picture from file <Picture>
    Given <Contact2> has an accent color <Color1>
    Given <Contact1> has an accent color <Color2>
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> has a name <Contact1NewName>
    Given <Contact2> has a name <Contact2NewName>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I see Conversations list with name <Contact1>
    And I see Conversations list with name <Contact2>
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    Then I see the conversation name is <GroupChatName> on Group info page
    And I see "<ParticipantNumber> people" in sub header on Group info page
    And I tap back button
    And I navigate back from conversation
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    Then I see the participant avatars for <Contact1NewName>,<Contact2NewName> on Group info page

    Examples:
      | Name      | Contact1  | Contact2  | ParticipantNumber | GroupChatName  | Picture                      | Color1       | Color2       | Contact1NewName   | Contact2NewName      |
      | user1Name | user3Name | user2Name | 2                 | GroupInfoCheck | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow | aqaPictureContact | aqaAvatarTestContact |

  @C715 @regression @rc @legacy
  Scenario Outline: Verify editing the conversation name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <OldGroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <OldGroupChatName>
    And I tap conversation name from top toolbar
    And I rename group conversation to <NewConversationName> on Group info page
    # Clicking X button to close participants view crashes the app
    And I tap back button
    Then I see a message informing me that I renamed the conversation to <NewConversationName>
    And I navigate back from conversation
    And I see Conversations list with name <NewConversationName>

    Examples:
      | Name      | Contact1  | Contact2  | OldGroupChatName | NewConversationName |
      | user1Name | user2Name | user3Name | oldGroupChat     | newGroupName        |

  @C395 @regression
  Scenario Outline: Check interaction with options menu
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap conversation name from top toolbar
    And I see user name of user <Contact> on Single connected user details page
    And I tap open menu button on Single connected user details page
    Then I see BLOCK button on Conversation options menu overlay page
    And I see MUTE button on Conversation options menu overlay page
    And I see DELETE button on Conversation options menu overlay page
    And I see ARCHIVE button on Conversation options menu overlay page
    When I tap back button
    Then I see Group info page
    And I do not see Conversation options menu
    When I tap open menu button on Single connected user details page
    And I swipe down
    Then I see Group info page
    And I do not see Conversation options menu
    When I tap open menu button on Single connected user details page
    Then I see LEAVE button on Conversation options menu overlay page
    And I see MUTE button on Conversation options menu overlay page
    And I see DELETE button on Conversation options menu overlay page
    And I see ARCHIVE button on Conversation options menu overlay page
    And I see RENAME button on Conversation options menu overlay page
    When I tap on center of screen
    And I tap open menu button on Single connected user details page
    And I swipe left
    And I swipe right
    And I swipe up
    Then I do not see Conversation options menu

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C716 @regression @rc
  Scenario Outline: Verify you cannot start a 1:1 conversation from a group chat if the other user is not in your contacts list
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>,<Contact2>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact2> on Group info page
    Then I see user name of user <Contact2> on Group unconnected user details page
    When I tap on +connect button on Group unconnected user details page
    And I tap on connect button on Group unconnected user details page
    And I tap on contact <Contact2> on Group info page
    # TODO: Add verification here

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |

  @C396 @regression
  Scenario Outline: Check interaction with participants view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I see Conversations list with name <Contact1>
    And I see Conversations list with name <Contact2>
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    Then I see Group info page
    And I see the conversation name is <GroupChatName> on Group info page
    And I see "<ParticipantNumber> people" in sub header on Group info page
    And I tap back button
    And I see conversation view
    And I tap conversation name from top toolbar
    Then I see Group info page
    When I tap back button
    And I see conversation view
    And I tap conversation name from top toolbar
    And I swipe right
    And I swipe up
    And I swipe left
    Then I see Group info page
    And I see the conversation name is <GroupChatName> on Group info page
    And I see "<ParticipantNumber> people" in sub header on Group info page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  | ParticipantNumber |
      | user1Name | user2Name | user3Name | GroupInfoCheck | 2                 |

  @C397 @regression
  Scenario Outline: Start 1to1 conversation from participants view
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>,<Contact2>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1> on Group info page
    Then I see user name of user <Contact1> on Group connected user details page
    When I tap on open conversation button on Group connected user details page
    And I tap conversation name from top toolbar
    Then I see user name of user <Contact1> on Group connected user details page
    And I see unique user name of user <Contact1> on Group connected user details page
    And I tap back button
    And I see conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |

  @C689 @regression @rc
  Scenario Outline: Check contact personal info in one to one conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap conversation name from top toolbar
    Then I see user name of user <Contact> on Group connected user details page
    And I see unique user name of user <Contact> on Group connected user details page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
