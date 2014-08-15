Feature: Sign Out

  #Muted till new sync engine client stabilization
  @mute @smoke @id479
  Scenario Outline: Sign out from ZClient
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with name <Name>
    When I am signing out
    Then I have returned to Sign In screen

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
