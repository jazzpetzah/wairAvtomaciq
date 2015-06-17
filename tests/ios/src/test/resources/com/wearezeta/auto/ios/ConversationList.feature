Feature: Conversation List

  @regression @id1333
  Scenario Outline: Unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    And I wait for 30 seconds
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I open archived conversations
    And I tap on contact name <ArchivedUser>
    And I navigate back to conversations view
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Login      | Password      | Name      | ArchivedUser |
      | user1Email | user1Password | user1Name | user2Name    |

  @regression @id1332 @id2171 @id2172
  Scenario Outline: Verify archive a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I archive conversation <Contact>
    Then I dont see conversation <Contact> in contact list
    And I long swipe right to archive conversation <Contact2>
    Then I dont see conversation <Contact2> in contact list
    And I open archived conversations
    Then I see user <Contact> in contact list
    Then I see user <Contact2> in contact list

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2 |
      | user1Email | user1Password | user1Name | user2Name | user3Name|

  @regression @id1075 @id2153
  Scenario Outline: Verify messages are marked as read with disappearing unread dot
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given Contact <Contact> send number <Number2> of message to user <Name>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe right on Dialog page
    Then I see change of state for first conversation cell

    Examples: 
      | Login      | Password      | Name      | Contact   | NewName    | Color        |Number2 |
      | user1Email | user1Password | user1Name | user2Name | UNREAD DOT | BrightYellow | 2 	    |

  @regression @id2040
  Scenario Outline: Verify archive a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using phone number or login <Login> and password <Password>
    When I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I archive conversation <GroupChatName>
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations
    Then I see user <GroupChatName> in contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @regression @id2041
  Scenario Outline: Verify unarchive a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Myself archived conversation having groupname <GroupChatName>
    Given I Sign in using phone number or login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open archived conversations
    And I tap on contact name <GroupChatName>
    And I navigate back to conversations view
    Then I see first item in contact list named <GroupChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ArchiveGroupChat |

  @staging @id1369
  Scenario Outline: Verify Ping animation in the conversations list
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When Contact <Contact> ping conversation <Name>
    And I wait for 10 seconds
    Then I see ping symbol

    Examples: 
      | Login      | Password      | Name      | Contact   | NewName  | Color        |
      | user1Email | user1Password | user1Name | user2Name | PING     | BrightOrange |

  @smoke @id2761
  Scenario Outline: Verify conversations are sorted according to most recent activity
    Given There are 4 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>,<Contact3>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And Contact <Contact3> send number <Number> of message to user <Name>
    And I see first item in contact list named <Contact3>
    When Contact <Contact2> ping conversation <Name>
    And I see first item in contact list named <Contact2>
    When Contact <Contact1> sends image <Picture> to single user conversation <Name>
    Then I see first item in contact list named <Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | Contact3     |Number |  Picture      |
      | user1Email | user1Password | user1Name | user2Name | user3name  | user4name    | 2 	   | testing.jpg   |