Feature: Performance
 @torun
 Scenario Outline: Normal usage performance testing
 	Given I Sign in using login <Login> and password <Password>
 	And I see Contact list with name <Name>
 	And Send mixed messages and scroll conversations <Count> times
 	
 	
 	Examples: 
      | Login   | Password    | Name    | Contact     | Count  |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | 3	   |
