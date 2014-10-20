Feature: People Picker

@id1150
@torun
Scenario Outline: Start group chat with users from Top Connections
  Given I have at least 9 connections
  Given I Sign in using login <Login> and password <Password>
  And I see Contact list with my name <Name>
  And I swipe down contact list
  And I see People picker page
  And I re-enter the people picker if top people list is not there
  And I see top people list on People picker page
 
  And I tap on 2 top connections
  
  Examples:
    |  Login	 | Password	 | Name	 | Contact1	 | Contact2	 |
    |  aqaUser	 | aqaPassword	| aqaUser	 | aqaContact1	| aqaContact2	|