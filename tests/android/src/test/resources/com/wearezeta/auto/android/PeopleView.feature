Feature: People View

  @id87 @regression
  Scenario Outline: I can access user details page from group chat and see user name, email and photo
    Given There are 3 users where <Name> is me
    Given <Contact1> has an avatar picture from file <Picture>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> has a name <Contact1NewName>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I see contact list with name <Contact1>
    And I see contact list with name <Contact2>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap conversation details button
    And I tap on group chat contact <Contact1NewName>
    Then I see <Contact1> user name and email

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName   | Picture                      | Contact1NewName   |
      | user1Name | user2Name | user3Name | GroupInfoCheck2 | aqaPictureContact600_800.jpg | aqaPictureContact |

  @id321 @regression @rc @rc42
  Scenario Outline: Leave group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap conversation details button
    And I press options menu button
    And I press LEAVE conversation menu button
    And I confirm leaving
    Then I see Contact list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Name | user2Name | user3Name | LeaveGroupChat |

  @id322 @regression @rc @rc42
  Scenario Outline: Remove from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I see contact list with name <Contact1>
    And I see contact list with name <Contact2>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap conversation details button
    And I tap on group chat contact <Contact2>
    And I click Remove
    And I confirm remove
    Then I do not see <Contact2> on group chat info page
    When I close participants page by UI button
    And I see dialog page
    Then I see message <Message> contact <Contact2> on group page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName       | Message     |
      | user1Name | user2Name | user3Name | RemoveFromGroupChat | You removed |

  @id594 @regression @rc
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
    Given I see Contact list with contacts
    And I see contact list with name <Contact1>
    And I see contact list with name <Contact2>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap conversation details button
    Then I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>
    And I close participants page by UI button
    When I navigate back from dialog page
    And I tap on contact name <GroupChatName>
    And I tap conversation details button
    Then I see the correct participant avatars for <Contact1NewName>,<Contact2NewName>

    Examples:
      | Name      | Contact1  | Contact2  | ParticipantNumber | GroupChatName  | Picture                      | Color1       | Color2       | Contact1NewName   | Contact2NewName       |
      | user1Name | user3Name | user2Name | 3                 | GroupInfoCheck | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow | aqaPictureContact | aqaAvatar TestContact |

  @id1507 @regression @rc @rc42
  Scenario Outline: Verify editing the conversation name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <OldGroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <OldGroupChatName>
    And I tap conversation details button
    And I rename group conversation to <NewConversationName>
    # Clicking X button to close participants view crashes the app
    And I press back button
    Then I see a message informing me that I renamed the conversation to <NewConversationName>
    And I navigate back from dialog page
    And I see contact list with name <NewConversationName>

    Examples:
      | Name      | Contact1  | Contact2  | OldGroupChatName | NewConversationName |
      | user1Name | user2Name | user3Name | oldGroupChat     | newGroupName        |

  @id2236 @regression
  Scenario Outline: Check interaction with options menu
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap conversation details button
    And I see <Contact> user profile page
    And I press options menu button
    Then I see correct 1:1 options menu
    When I tap on center of screen
    Then I see participant page
    And I do not see 1:1 options menu
    When I press options menu button
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
    #Need to delete small swipe check if it will be unstable
    #When I do small swipe down
    #And I wait for 1 second
    #Then I do not see participant page
    #And I see correct 1:1 options menu
    When I tap on center of screen
    And I press options menu button
    And I swipe left
    And I swipe right
    And I swipe up
    Then I do not see 1:1 options menu

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id1509 @regression @rc
  Scenario Outline: Verify you cannot start a 1:1 conversation from a group chat if the other user is not in your contacts list
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>,<Contact2>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I tap conversation details button
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

  @id2291 @regression
  Scenario Outline: (CM-691) Check interaction with participants view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I see contact list with name <Contact1>
    And I see contact list with name <Contact2>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap conversation details button
    Then I see participants page
    And I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>
    When I close participants page by UI button
    Then I do not see participants page
    And I see dialog page
    When I swipe up on dialog page
    Then I see participants page
    When I close participants page by UI button
    Then I do not see participants page
    And I see dialog page
    When I tap conversation details button
    Then I see participants page
    When I press back button
    Then I do not see participants page
    And I see dialog page
    When I tap conversation details button
    And I swipe right
    And I swipe up
    And I swipe left
    Then I see participants page
    And I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  | ParticipantNumber |
      | user1Name | user2Name | user3Name | GroupInfoCheck | 3                 |

  @id2292 @regression
  Scenario Outline: Start 1to1 conversation from participants view
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>,<Contact2>
    Given <Contact1> has group chat <GroupChatName> with <Name>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I tap conversation details button
    And I tap on group chat contact <Contact1>
    And I see <Contact1> user profile page
    And I click Open Conversation button on connected user page
    And I tap conversation details button
    And I close participants page by UI button
    Then I see Connect to <Contact1> Dialog page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |
