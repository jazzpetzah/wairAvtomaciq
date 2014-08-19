Feature: Connect to user

  #Muted till new sync engine client stabilization
  @mute @smoke @id473
  Scenario Outline: Receive invitation from user
    Given I send invitation to <Name> by <Contact>
    When I Sign in using login <Login> and password <Password>
    And I see connect invitation
    And I accept invitation
    Then I see Contact list with name <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | yourContact |
