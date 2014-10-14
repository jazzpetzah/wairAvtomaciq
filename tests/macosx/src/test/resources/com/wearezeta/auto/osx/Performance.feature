Feature: Performance
 @performance
 Scenario Outline: Normal usage performance testing
    Given Generate <UsersNumber> and connect to <Login> contacts
 	And I Sign in using login <Login> and password <Password>
 	And I see Contact list with name <Login>
 	And I <Login> start testing cycle for <Time> minutes
 	
 	
 	Examples: 
      | Login     | Password | UsersNumber | Time  |
      | perfUser  | perfPass | 10          | 20    |
