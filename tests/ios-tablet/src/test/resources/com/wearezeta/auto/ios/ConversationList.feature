Feature: Conversation List

  @staging @id2378 @id2568
  Scenario Outline: Verify archive a conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I archive conversation <Contact>
    Then I dont see conversation <Contact> in contact list
    And I long swipe right to archive conversation <Contact2>
    Then I dont see conversation <Contact2> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list
    Then I see user <Contact2> in contact list

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2 |
      | user1Email | user1Password | user1Name | user2Name | user3Name|
      
  @staging @id2378 @id2568
  Scenario Outline: Verify archive a conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I archive conversation <Contact>
    Then I dont see conversation <Contact> in contact list
    And I long swipe right to archive conversation <Contact2>
    Then I dont see conversation <Contact2> in contact list
    And I open archived conversations on iPad
    Then I see user <Contact> in contact list
    Then I see user <Contact2> in contact list

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2 |
      | user1Email | user1Password | user1Name | user2Name | user3Name|
      
  @staging @id2674
  Scenario Outline: Verify archive a group conversation [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using phone number or login <Login> and password <Password>
    When I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I archive conversation <GroupChatName>
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations on iPad
    Then I see user <GroupChatName> in contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ArchiveGroupChat |
      
  @staging @id2674
  Scenario Outline: Verify archive a group conversation [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    When I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    And I archive conversation <GroupChatName>
    Then I dont see conversation <GroupChatName> in contact list
    And I open archived conversations on iPad
    Then I see user <GroupChatName> in contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ArchiveGroupChat |
      
  @staging @id2675
  Scenario Outline: Unarchive conversation [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    And I wait for 30 seconds
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I open archived conversations on iPad
    And I tap on contact name <ArchivedUser>
    And I navigate back to conversations view
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Login      | Password      | Name      | ArchivedUser |
      | user1Email | user1Password | user1Name | user2Name    |
      
  @staging @id2675
  Scenario Outline: Unarchive conversation [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    And I wait for 30 seconds
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I open archived conversations on iPad
    And I tap on contact name <ArchivedUser>
    And I navigate back to conversations view
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Login      | Password      | Name      | ArchivedUser |
      | user1Email | user1Password | user1Name | user2Name    |
      
  @staging @id2376
  Scenario Outline: I can open search UI by tapping + button [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by clicking plus button
    And I see People picker page

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
      
  @staging @id2376
  Scenario Outline: I can open search UI by tapping + button [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open search by clicking plus button
    And I see People picker page

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
      
  @staging @id2369
  Scenario Outline: Verify Ping animation in the conversations list [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When Contact <Contact> ping conversation <Name>
    And I wait for 10 seconds
    Then I see change of state for first conversation cell

    Examples: 
      | Login      | Password      | Name      | Contact   | NewName  | Color        |
      | user1Email | user1Password | user1Name | user2Name | PING     | BrightOrange |
      
  @staging @id2369
  Scenario Outline: Verify Ping animation in the conversations list [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When Contact <Contact> ping conversation <Name>
    And I wait for 10 seconds
    Then I see change of state for first conversation cell

    Examples: 
      | Login      | Password      | Name      | Contact   | NewName  | Color        |
      | user1Email | user1Password | user1Name | user2Name | PING     | BrightOrange |
      
  @staging @id2376
  Scenario Outline: Verify messages are marked as read with disappearing unread dot [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given Contact <Contact> send number <Number> of message to user <Name>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe right on Dialog page
    Then I see change of state for first conversation cell


    Examples: 
      | Login      | Password      | Name      | Contact   | NewName    | Color        |Number |
      | user1Email | user1Password | user1Name | user2Name | UNREAD DOT | BrightYellow | 2 	   |
      
   @staging @id2711
  Scenario Outline: Verify messages are marked as read with disappearing unread dot [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change name to <NewName>
    Given User <Name> change accent color to <Color>
    Given Contact <Contact> send number <Number> of message to user <Name>
    Given I rotate UI to landscapety c
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I remember the state of the first conversation cell
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe right on Dialog page
    Then I see change of state for first conversation cell


    Examples: 
      | Login      | Password      | Name      | Contact   | NewName    | Color        |Number |
      | user1Email | user1Password | user1Name | user2Name | UNREAD DOT | BrightYellow | 2 	   |