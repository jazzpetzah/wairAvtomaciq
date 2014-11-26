Feature: Conversation List

  @smoke @id474 @id481
  Scenario Outline: Mute and unmute conversation
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open conversation with <Contact>
    And I change conversation mute state
    And I go to user <Name> profile
    Then I see conversation <Contact> is muted
    When I open conversation with <Contact>
    And I change conversation mute state
    And I go to user <Name> profile
    Then I see conversation <Contact> is unmuted

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
