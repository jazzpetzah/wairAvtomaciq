Feature: Search

  @staging @id1391
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People
  	Given I have 1 users and 4 contacts for 1 users 
  	Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    When I open People Picker from contact list
    Then I see Top People list in People Picker
  	
  	Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |