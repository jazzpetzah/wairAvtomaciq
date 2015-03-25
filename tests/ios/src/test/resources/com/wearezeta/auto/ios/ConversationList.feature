Feature: Conversation List

  #Muted till new sync engine client stabilization. Mute buttons location is not possible.
  #@smoke @id338
  #Scenario Outline: Mute conversation
  #Given I have 1 users and 1 contacts for 1 users
  #Given I Sign in using login <Login> and password <Password>
  #And I see Contact list with my name <Name>
  #When I swipe right on a <Contact1>
  #And I click mute conversation
  #Then Contact <Contact1> is muted
  #When I swipe right on a <Contact1>
  #And I click mute conversation
  #Then Contact <Contact1> is not muted
  #Examples:
  #| Login   | Password    | Name    | Contact1    |
  #| aqaUser | aqaPassword | aqaUser | aqaContact1 |
  @regression @id1333
  Scenario Outline: Unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <ArchivedUser>
    Given Myself archived conversation with <ArchivedUser>
    And I wait for 30 seconds
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I open archived conversations
    And I tap on contact name <ArchivedUser>
    And I navigate back to conversations view
    Then I see first item in contact list named <ArchivedUser>

    Examples: 
      | Login      | Password      | Name      | ArchivedUser |
      | user1Email | user1Password | user1Name | user2Name    |

  @regression @id1462
  Scenario Outline: Verify silence the conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change  name to <NewName>
    Given User <Name> change  accent color to <Color>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    And I silence conversation <Contact>
    Then I see conversation <Contact> is silenced

    Examples: 
      | Login      | Password      | Name      | Contact   | Color        | NewName |
      | user1Email | user1Password | user1Name | user2Name | BrightOrange | SILENCE |

  @regression @id1332
  Scenario Outline: Verify archive a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
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

  @staging @id1335
  Scenario Outline: Verify unsilence the conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change  name to <NewName>
    Given User <Name> change  accent color to <Color>
    Given <Name> silenced conversation with <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I see conversation <Contact> is silenced
    And I swipe right on a <Contact>
    And I unsilence conversation <Contact>
    Then I see conversation <Contact> is unsilenced

    Examples: 
      | Login      | Password      | Name      | Contact   | Color        | NewName |
      | user1Email | user1Password | user1Name | user2Name | BrightOrange | SILENCE |

  @regression @id1075
  Scenario Outline: Verify messages are marked as read with disappearing unread dot
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given User <Contact> change  name to <NewName>
    Given User <Name> change  accent color to <Color>
    Given I wait for 15 seconds
    Given Contact <Contact> send number <Number2> of message to user <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I see unread <DotSizeSmall> messages dot for <Contact>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe right on Dialog page
    Then I dont see an unread message dot for <Contact>
    And Contact <Contact> send number <Number> of message to user <Name>
    And I see unread <DotSizeBig> messages dot for <Contact>
    When I tap on contact name <Contact>
    And I see dialog page
    And I scroll to the end of the conversation
    And I swipe right on Dialog page
    Then I dont see an unread message dot for <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | Number | NewName    | Color        |Number2 | DotSizeSmall |DotSizeBig |
      | user1Email | user1Password | user1Name | user2Name | 30     | UNREAD DOT | BrightOrange | 2		 | small		|big		|

  @regression @id2040
  Scenario Outline: Verify archive a group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    And I Sign in using login <Login> and password <Password>
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
    And I Sign in using login <Login> and password <Password>
    When I see Contact list with my name <Name>
    And I open archived conversations
    And I tap on contact name <GroupChatName>
    And I navigate back to conversations view
    Then I see first item in contact list named <GroupChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName    |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ArchiveGroupChat |
