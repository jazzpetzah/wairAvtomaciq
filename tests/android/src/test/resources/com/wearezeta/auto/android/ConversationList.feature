Feature: Conversation List

  @id324 @smoke
  Scenario Outline: Mute conversation
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click mute conversation <Contact1>
    Then Contact <Contact1> is muted
    When I swipe right on a <Contact1>
    And I click mute conversation <Contact1>
    Then Contact <Contact1> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact1  |
      | user1Email | user1Password | user1Name | user2Name |