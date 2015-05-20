Feature: People View

  @id83 @id87 @regression
  Scenario Outline: I can access user details page from group chat and see user name, email and photo
    Given There are 3 users where <Name> is me
    Given <Contact1> has an avatar picture from file <Picture>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> has a name <Contact1NewName>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list
    And I see contact list with name <Contact1>
    And I see contact list with name <Contact2>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe up on dialog page
    And I tap on group chat contact <Contact1NewName>
    Then I see <Contact1> user name and email

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName   | Picture                      | Contact1NewName   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupInfoCheck2 | aqaPictureContact600_800.jpg | aqaPictureContact |

  @id321 @smoke @regression
  Scenario Outline: Leave group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe up on dialog page
    And I press options menu button
    And I press Leave conversation button
    And I confirm leaving
    Then I see Contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat |

  @id322 @smoke @regression
  Scenario Outline: Remove from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    And I see contact list with name <Contact1>
    And I see contact list with name <Contact2>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe up on dialog page
    And I tap on group chat contact <Contact2>
    And I click Remove
    And I confirm remove
    Then I do not see <Contact2> on group chat info page
    And I return to group chat page
    And I see dialog page
    Then I see message <Message> contact <Contact2> on group page

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName       | Message     |
      | user1Email | user1Password | user1Name | user2Name | user3Name | RemoveFromGroupChat | YOU REMOVED |

  @id594 @regression
  Scenario Outline: Verify correct group info page information
    Given There are 3 users where <Name> is me
    Given <Contact1> has an avatar picture from file <Picture>
    Given <Contact2> has an accent color <Color1>
    Given <Contact1> has an accent color <Color2>
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> has a name <Contact1NewName>
    Given <Contact2> has a name <Contact2NewName>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    And I see contact list with name <Contact1>
    And I see contact list with name <Contact2>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe up on dialog page
    Then I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>
    And I return to group chat page
    When I navigate back from dialog page
    And I tap on contact name <GroupChatName>
    And I tap conversation details button
    Then I see the correct participant <Contact1NewName> and <Contact2NewName> avatars

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ParticipantNumber | GroupChatName  | Picture                      | Color1       | Color2       | Contact1NewName   | Contact2NewName       |
      | user1Email | user1Password | user1Name | user3Name | user2Name | 3                 | GroupInfoCheck | aqaPictureContact600_800.jpg | BrightOrange | BrightYellow | aqaPictureContact | aqaAvatar TestContact |

  @id1395 @smoke @regression
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Contact <Contact1> send message to user <Name>
    Given Contact <Name> send message to user <Contact1>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I wait for 30 seconds
    #fix for first login
    And I minimize the application
    And I wait for 5 seconds
    And I restore the application
    #end of fix for first login
    And I wait for 25 seconds
    When I swipe down contact list
    And I see People picker page
    And I tap on <Contact1> in Top People
    And I tap on create conversation
    Then I see dialog page

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @id1507 @staging
  Scenario Outline: Verify editing the conversation name
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <OldGroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <OldGroupChatName>
    And I tap conversation details button
    And I rename group conversation to <NewConversationName>
    And I return to group chat page
    Then I see a message informing me that I renamed the conversation to <NewConversationName>
    And I navigate back from dialog page
    And I see contact list with name <NewConversationName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | OldGroupChatName | NewConversationName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | oldGroupChat     | newGroupName        |

  @id2236 @regression
  Scenario Outline: Check interaction with options menu
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap conversation details button
    And I see <Contact> user profile page
    And I press options menu button
    Then I see correct 1:1 options menu
    When I tap on center of screen
    Then I see profile page
    And I do not see 1:1 options menu
    When I press options menu button
    Then I see correct 1:1 options menu
    When I press back button
    Then I see profile page
    And I do not see 1:1 options menu
    When I press options menu button
    And I swipe down
    Then I see profile page
    And I do not see 1:1 options menu
    When I press options menu button
    Then I do not see profile page
    And I see correct 1:1 options menu
    When I do small swipe down
    Then I do not see 1:1 options menu
    And I see profile page
    When I press options menu button
    Then I see correct 1:1 options menu
    And I do not see profile page
    When I swipe left
    And I swipe right
    And I swipe up
    Then I see correct 1:1 options menu
    And I do not see profile page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id2214 @staging
  Scenario Outline: I can dismiss PYMK by Hide button
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I press Clear button
    And I wait for 30 seconds
    And I swipe down contact list
    And I see People picker page
    And I swipe on random connect
    And I click on PYMK hide button
    Then I do not see random connect

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

  @id2213 @staging
  Scenario Outline: I can dismiss PYMK by swipe
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list
    When I swipe down contact list
    And I see People picker page
    And I press Clear button
    And I wait for 30 seconds
    And I swipe down contact list
    And I see People picker page
    And I swipe on random connect
    And I hide random connect by swipe
    Then I do not see random connect

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |

  @id1509 @staging
  Scenario Outline: Verify you cannot start a 1:1 conversation from a group chat if the other user is not in your contacts list
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given <Contact1> is connected to <Contact2>
    Given <Contact1> has group chat <OldGroupChatName> with <Name>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    Given I see Contact list
    When I tap on contact name <OldGroupChatName>
    And I tap conversation details button
    And I tap on group chat contact <Contact2>
    Then I see connect to unconnected user page with user <Contact2>
    When I click on the unconnected user page connect or pending button
    And I press Connect button
    And I tap on group chat contact <Contact2>
    Then I see connect to unconnected user page pending button
    When I click on the unconnected user page connect or pending button
    Then I see connect to unconnected user page pending button

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | OldGroupChatName | NewConversationName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | oldGroupChat     | newGroupName        |
