Feature: Sign In

@smoke
@nonUnicode
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
      
@smoke
@nonUnicode
  Scenario Outline: Open/Close People picker
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I press Clear button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
 