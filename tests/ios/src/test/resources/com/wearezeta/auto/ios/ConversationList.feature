Feature: Conversation List

  #Muted till new sync engine client stabilization. Mute buttons location is not possible.
  @smoke @id338
  Scenario Outline: Mute conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click mute conversation
    Then Contact <Contact1> is muted
    When I swipe right on a <Contact1>
    And I click mute conversation
    Then Contact <Contact1> is not muted

    Examples: 
      | Login   | Password    | Name    | Contact1    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
