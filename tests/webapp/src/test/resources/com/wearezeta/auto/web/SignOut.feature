Feature: Sign Out

  @C14312 @e2ee @regression
  Scenario Outline: Make sure user does not see data of user of previous sessions on same browser
    Given There are 4 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open self profile
    Then I see user name on self profile page <Name>
    And I see user email on self profile page <Email>
    When I open conversation with <GroupChatName>
    And I write message <Message>
    And I send message
    And I open conversation with <Contact1>
    And I write message <Message>
    And I send message
    And I open self profile
    And I click gear button on self profile page
    And I select Log out menu item on self profile page
    And I see the clear data dialog
    And I enable checkbox to clear all data
    And I click Logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Email3> and password <Password3>
    And I see Welcome page
    And I confirm keeping picture on Welcome page
    And I am signed in properly
    Then I see Search is opened
    And I see Bring Your Friends or Invite People button
    Then I see user name on self profile page <Contact3>
    And I see user email on self profile page <Email3>
    And I do not see Contact list with name <GroupChatName>
    And I do not see Contact list with name <Contact1>

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | Contact3  | Email3     | Password3     | GroupChatName | Message         |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | user4Email | user4Password | User1Chat     | Hello User1Chat |

  @C87934 @regression
  Scenario Outline: Verify session expired info is visible on login page
    Given I switch to Sign In page
    Given I open <Language> session expired login page
    Then I see Sign In page
    And I verify session expired message is visible
    And I verify session expired message is equal to <SessionExpiredMessage>

    Examples:
      | Language | SessionExpiredMessage                                                                |
      | english  | You were signed out because your session expired. Please log in again.               |
      | german   | Du wurdest abgemeldet, da deine Session abgelaufen ist. Bitte logge dich erneut ein. |