Feature: Sign In


@smoke
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

#muted because related story still not closed
@mute
@smoke 
  Scenario Outline: ZClient change name
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on my name
    And I change <Name> to <NewName> 
    And I swipe right to contact list
    Then I see contact list loaded with User name <NewName>
    When I tap on my name <NewName>
    Then I see my new name <NewName> and return old <Name>

    Examples: 
      | Login   | Password    | Name    | NewName     |
      | aqaUser | aqaPassword | aqaUser | NewTestName |
 