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

  @smoke @id3437
  Scenario Outline: Mute and unmute 1:1 conversation with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I open context menu of contact <Contact>
    And I click silence in context menu
    Then I see that conversation <Contact> is muted
    When I open context menu of contact <Contact>
    And I click notify in context menu
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3438
  Scenario Outline: Mute and unmute 1:1 conversation with menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Silence"
    Then I see that conversation <Contact> is muted
    When I click menu bar item "Conversation" and menu item "Notify"
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3877
  Scenario Outline: Mute and unmute 1:1 conversation with keyboard shortcut
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I type shortcut combination to mute or unmute a conversation
    Then I see that conversation <Contact> is muted
    When I type shortcut combination to mute or unmute a conversation
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |