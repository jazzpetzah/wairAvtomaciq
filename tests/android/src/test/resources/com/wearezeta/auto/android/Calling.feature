Feature: Calling

  @regression @id373
  Scenario Outline: Verify calling from missed call indicator in conversation
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When Contact <Contact> calls to conversation <Name>
    And I wait for 5 seconds
    And Current call is ended
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see dialog with missed call from <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id373
  Scenario Outline: Verify calling from missed call indicator in Conversation List
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When Contact <Contact> calls to conversation <Name>
    And I wait for 5 seconds
    And Current call is ended
    Then Conversation List contains missed call icon

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
