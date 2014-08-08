Feature: Sign In

 @id326 @smoke @nonUnicode
  Scenario Outline: Sign in to ZClient
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

 @id209 @smoke @nonUnicode
  Scenario Outline: I can change sign in user
    Given I Sign in using login <Login1> and password <Password>
    And I see Contact list with my name <Login1>
    When I tap on my name <Login1>
    And I tap options button
    And I tap sign out button
    And I see sign in and join buttons
    And I press Sign in button
    And I clear login and password fields
    And I have entered login <Login2>
    And I have entered password <Password>
    And I press Log in button
    Then Contact list appears with my name <Login2>

    Examples: 
      | Login1  | Password    | Login2   |
      | aqaUser | aqaPassword | yourUser |

  @id327 @smoke @nonUnicode
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
