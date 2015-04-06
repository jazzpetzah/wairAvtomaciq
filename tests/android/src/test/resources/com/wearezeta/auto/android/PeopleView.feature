Feature: People View

  @id83 @id87 @regression
  Scenario Outline: I can access user details page from group chat and see user name, email and photo
    Given There are 3 users where <Name> is me
    Given <Contact1> has an avatar picture from file <Picture>
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> has a name <Contact1NewName>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe up on dialog page
    And I tap on group chat contact <Contact1NewName>
    Then I see <Contact1> user name and email

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName   | Picture                      | Contact1NewName   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupInfoCheck2 | aqaPictureContact600_800.jpg | aqaPictureContact |

  @id319 @regression
  Scenario Outline: I can create group chat from People picker
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact1>
    And I tap on user name found on People picker page <Contact1>
    And I add in search field user name to connect to <Contact2>
    And I tap on user name found on People picker page <Contact2>
    #And I press back button
    And I tap on create conversation
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName           |
      | user1Email | user1Password | user1Name | user3Name | user2Name | PeoplePicker GroupChat2 |

  @id321 @smoke @regression
  Scenario Outline: Leave group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe up on dialog page
    And I press Right conversation button
    And I press Leave conversartion button
    And I confirm leaving
    Then I see Contact list with my name <Name>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat |

  @id322 @smoke @regression 
  Scenario Outline: Remove from group chat
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I swipe up on dialog page
    And I tap on group chat contact <Contact2>
    And I click Remove
    And I confirm remove
    Then I do not see <Contact2> on group chat info page
    And I return to group chat page
    And I see dialog page
    Then I see  message <Message> contact <Contact2> on group page

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
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I swipe up on dialog page
    Then I see that the conversation name is <GroupChatName>
    And I see the correct number of participants in the title <ParticipantNumber>
    And I see the correct participant <Contact1NewName> and <Contact2NewName> avatars

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
    And I see Contact list with my name <Name>
    And I wait for 90 seconds
    When I swipe down contact list
    And I see People picker page
    And I tap on <Contact1> in Top People
    And I tap on create conversation
    Then I see dialog page

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

