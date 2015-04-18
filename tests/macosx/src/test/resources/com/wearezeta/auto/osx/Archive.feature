Feature: Archive

  @smoke @id1041
  Scenario Outline: Archive and unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <Contact>
    And I archive conversation with <Contact>
    And I open self profile
    Then I do not see conversation <Contact> in contact list
    When I go to archive
    And I open conversation with <Contact>
    Then I see contact <Contact> in Contact list

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1531
  Scenario Outline: Verify you can archive and unarchive group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I open conversation with <ChatName>
    When I archive conversation with <ChatName>
    And I open self profile
    Then I do not see conversation <ChatName> in contact list
    When I go to archive
    And I open conversation with <ChatName>
    And I open self profile
    Then I see contact <ChatName> in Contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName         |
      | user1Email | user1Password | user1Name | user2Name | user3Name | ArchiveGroupChat |
