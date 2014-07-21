Feature: User Profile

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
  
  @staging
  Scenario Outline: Open and Close settings page
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap options button
    And I tap settings button
    Then I see settings page
    When I press back button
    Then I see personal info page
    
     Examples: 
      | Login   | Password    | Name    | 
      | aqaUser | aqaPassword | aqaUser | 