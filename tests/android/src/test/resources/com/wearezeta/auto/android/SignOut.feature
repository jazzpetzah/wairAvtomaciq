Feature: Sign Out

  @id329 @smoke
  Scenario Outline: Sign out from ZClient
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    #workaround
    And I minimize the application
    And I restore the application
    #workaround
    And I tap options button
    And I tap sign out button
    Then I see welcome screen

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
