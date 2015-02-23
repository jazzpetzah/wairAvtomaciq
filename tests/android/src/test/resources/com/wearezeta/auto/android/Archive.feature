Feature: Archive

  @1511 @staging
  Scenario Outline: Verify you can archive and unarchive
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact>
    Then Contact name <Contact> is not in list
    And I swipe up contact list
    And I swipe right on a <Contact>
    And I see dialog page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @1512 @staging
  Scenario Outline: Verify you can archive and unarchive group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <GroupChatName>
    Then Contact name <GroupChatName> is not in list
    And I swipe up contact list
    And I swipe right on a <GroupChatName>
    And I see dialog page

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat |
