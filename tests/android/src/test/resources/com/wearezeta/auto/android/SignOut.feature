Feature: Sign Out

  Scenario Outline: Sign out from ZClient
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I swipe to personal info screen
    And I pull up for options
    And I press options button <OptionsButton>
    Then I see welcome screen

    Examples: 
      | Login   | Password | Name    | OptionsButton |
      | aqaUser | aqaUser  | aqaUser | Sign out      |
