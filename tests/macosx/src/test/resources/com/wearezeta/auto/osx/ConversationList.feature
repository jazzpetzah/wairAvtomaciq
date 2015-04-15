Feature: Conversation List

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
