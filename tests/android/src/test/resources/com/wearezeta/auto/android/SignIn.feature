Feature: Sign In

@smoke
@regression
  Scenario Outline: Sign in to ZClient
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login   | Password     | Name    |
      | aqaUser | aqaPassword  | aqaUser |
