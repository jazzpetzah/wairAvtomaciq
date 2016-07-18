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
    And I tap on group chat contact <Contact1NewName>
    Then I see <Contact1> user name and email

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName   | Picture                      | Contact1NewName   |
      | user1Name | user2Name | user3Name | GroupInfoCheck2 | aqaPictureContact600_800.jpg | aqaPictureContact |

  @C685 @regression @rc @rc42
  Scenario Outline: Leave group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I press options menu button
    And I press LEAVE conversation menu button
    And I confirm leaving
    Then I see Conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3Name | LeaveGroupChat |

  @C686 @regression @rc @rc42
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
    And I tap on group chat contact <Contact2>
    And I click Remove
    And I confirm remove
    Then I do not see <Contact2> on group chat info page
    When I close participants page by UI button
    Then I see message <Message> contact <Contact2> on group page

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
    Then I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>
    And I close participants page by UI button
    When I navigate back from dialog page
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    Then I see the correct participant avatars for <Contact1NewName>,<Contact2NewName>

    Examples:
      | Name      | Contact1  | Contact2  | ParticipantNumber | GroupChatName  | Picture                      | Color1       | Color2       | Contact1NewName   | Contact2NewName       |
      | user1Name | user3Name | user2Name | 2                 | GroupInfoCheck | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow | aqaPictureContact | aqaAvatarTestContact  |

  @C715 @regression @rc @rc42
  Scenario Outline: Verify editing the conversation name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <OldGroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <OldGroupChatName>
    And I tap conversation name from top toolbar
    And I rename group conversation to <NewConversationName>
    # Clicking X button to close participants view crashes the app
    And I press back button
    Then I see a message informing me that I renamed the conversation to <NewConversationName>
    And I navigate back from dialog page
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
    And I see <Contact> user profile page
    And I press options menu button
    Then I see correct 1:1 options menu
    When I press back button
    Then I see participant page
    And I do not see 1:1 options menu
    When I press options menu button
    And I swipe down
    Then I see participant page
    And I do not see 1:1 options menu
    When I press options menu button
    And I see correct 1:1 options menu
    When I tap on center of screen
    And I press options menu button
    And I swipe left
    And I swipe right
    And I swipe up
    Then I do not see 1:1 options menu

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
    And I tap on group chat contact <Contact2>
    Then I see user name <Contact2> on non connected user page
    When I click Connect button on non connected user page
    And I click Connect button on connect to page
    And I tap on group chat contact <Contact2>
    Then I see Pending button on pending user page
    When I click Pending button on pending user page
    Then I see Pending button on pending user page

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
    Then I see participants page
    And I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>
    When I close participants page by UI button
    Then I do not see participants page
    And I see conversation view
    When I tap conversation name from top toolbar
    Then I see participants page
    When I press back button
    Then I do not see participants page
    And I see conversation view
    When I tap conversation name from top toolbar
    And I swipe right
    And I swipe up
    And I swipe left
    Then I see participants page
    And I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>

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
    And I tap on group chat contact <Contact1>
    Then I see <Contact1> user profile page
    And I click Open Conversation button on connected user page
    When I tap conversation name from top toolbar
    Then I see <Contact1> user name and email
    When I close participants page by UI button
    Then I see conversation view

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
    Then I see <Contact> user name and email

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |