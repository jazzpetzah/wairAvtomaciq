 Feature: Block user
 
 @id676 @regression
 Scenario Outline: I want to block a person from 1:1 conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe up on dialog page
    And I see <Contact> user name and email
	And I Press Block button
    And I confirm block
    Then I do not see Contact list with name <Contact>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
   	And I see user <Contact> found on People picker page
   	And I tap on user name found on People picker page <Contact>
    Then User info should be shown with Block button
    
 Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
      
@id673 @regression
 Scenario Outline: I want to unblock someone from their Profile view
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
   	And I see user <Contact> found on People picker page
   	And I tap on user name found on People picker page <Contact>
    Then User info should be shown with Block button
    And I click Unblock button 
    And I see dialog page
    And I navigate back from dialog page
    And I see contact list loaded with User name <Contact>
    
 Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
 
 @regression @id720
 Scenario Outline: I do not want to be seen in the search results of someone I blocked
    Given User <Contact> blocks user <Login>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I wait for 30 seconds
   	Then I see than no results found

 Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |      
      
@id680 @regression   
Scenario Outline: I want to see user has been blocked within the Start UI
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
    And I tap on user name found on People picker page <Contact>
    And I see connect to <Contact> dialog
    And I tap on edit connect request field
    And I type Connect request "<Message>"
    And I press Connect button
    Then I see contact list loaded with User name <Contact>
    When I tap on contact name <Contact>
    And I see that connection is pending
	And I Press Block button on connect to page
    And I confirm block on connect to page
    And I wait for 5 seconds
    Then I do not see Contact list with name <Contact>
    And I swipe down contact list
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in search field user name to connect to <Contact>
   	And I see user <Contact> found on People picker page
   	And I tap on user name found on People picker page <Contact>
    Then User info should be shown with Block button
    And I click Unblock button
    
 Examples: 
      | Login   | Password    | Name    | Contact    | Message      | 
      | aqaUser | aqaPassword | aqaUser | yourIgnore | Hello friend | 