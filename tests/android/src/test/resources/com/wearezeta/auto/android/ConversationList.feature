Feature: Conversation List

  @id324 @smoke
  Scenario Outline: Mute conversation
    Given I have 1 users and 1 contacts for 1 users
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click mute conversation <Contact1>
    Then Contact <Contact1> is muted
    When I swipe right on a <Contact1>
    And I click mute conversation <Contact1>
    Then Contact <Contact1> is not muted

    Examples: 
      | Login   | Password    | Name    | Contact1    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |