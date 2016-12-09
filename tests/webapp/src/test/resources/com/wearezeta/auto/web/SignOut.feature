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
    When I open preferences by clicking the gear button
    Then I see name <Name> in account preferences
    And I see user email <Email> in account preferences
    And I close preferences
    When I open conversation with <GroupChatName>
    And I write message <Message>
    And I send message
    And I open conversation with <Contact1>
    And I write message <Message>
    And I send message
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I enable checkbox to clear all data
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Email3> and password <Password3>
    And I am signed in properly
    And I see watermark
    When I open search by clicking the people button
    Then I see Search is opened
    And I see Bring Your Friends or Invite People button
    When I close People Picker
    And I open preferences by clicking the gear button
    Then I see name <Contact3> in account preferences
    And I see user email <Email3> in account preferences
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
      | german   | Du wurdest abgemeldet, da deine Session abgelaufen ist. Bitte melde dich erneut an.  |