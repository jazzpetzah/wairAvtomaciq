Feature: Conversation List

  @staging @id3423
  Scenario Outline: Verify I can block user from conversation list with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I open context menu of contact <Contact>
    And I click block in context menu
    Then I see a block warning modal
    And I click cancel button in the block warning
    And I open context menu of contact <Contact>
    And I click block in context menu
    Then I see a block warning modal
    And I click block button in the block warning
    Then I do not see Contact list with name <Contact>
    When User <Contact> sent message <Msg1> to conversation <Name>
    Then I do not see Contact list with name <Contact>
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Contact> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see my avatar on top of Contact list
    And I open conversation with <Name>
    Then I do not see <Action> action for <Name> in conversation

    Examples: 
       | Login      | Password      | Name      | Contact   | Login2     | Password2     | Msg1    | Action |
       | user1Email | user1Password | user1Name | user2Name | user2Email | user2Password | message | LEFT   |

  @smoke @id474 @id481
  Scenario Outline: Mute and unmute conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <Contact>
    And I change mute state of conversation with <Contact>
    And I open self profile
    Then I see conversation <Contact> is muted
    When I open conversation with <Contact>
    And I change mute state of conversation with <Contact>
    And I open self profile
    Then I see conversation <Contact> is unmuted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
