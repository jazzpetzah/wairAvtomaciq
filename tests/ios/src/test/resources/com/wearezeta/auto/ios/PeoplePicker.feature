Feature: People Picker

@id1150
@staging
Scenario Outline: Start group chat with users from Top Connections
  Given I have at least 9 connections
  Given I Sign in using login <Login> and password <Password>
  When I see Contact list with my name <Name>
  And I swipe down contact list
  And I see People picker page
  And I re-enter the people picker if top people list is not there
  And I see top people list on People picker page
  Then I tap on 2 top connections
  And I click Create Conversation button  on People picker page
  And I swipe up on group chat page
  And I change conversation name to <ConvoName>
  And I exit the group info page
  And I return to the chat list
  And I see first item in contact list named <ConvoName>
  
  Examples:
    |  Login	 | Password	    | Name	    | ConvoName    |
    |  aqaUser	 | aqaPassword	| aqaUser   | TopGroupTest |