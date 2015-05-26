Feature: People View

 @staging @id
 Scenario Outline: Start group chat with users from contact list [PORTRAIT]
   Given There are 3 users where <Name> is me
   Given Myself is connected to <Contact1>,<Contact2>
   Given I Sign in using phone number or login <Login> and password <Password>
   And I see Contact list with my name <Name>
   When I open search by clicking plus button
   And I see People picker page
   And I tap on Search input on People picker page
   And I input in People picker search field user name <Contact1>
   And I tap on connected user <Contact1> on People picker page
   And I tap on Search input on People picker page
   And I input in People picker search field user name <Contact2>
   And I tap on connected user <Contact2> on People picker page
   And I click on Go button
   Then I see group chat page with users <Contact1>,<Contact2>
   
   Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2    | 
      | user1Email | user1Password | user1Name | user2Name   | user3Name   | 
 
 
 
 @staging @id
 Scenario Outline: Start group chat with users from contact list [LANDSCAPE]
   Given There are 3 users where <Name> is me
   Given Myself is connected to <Contact1>,<Contact2>
   Given I rotate UI to landscape
   Given I Sign in using phone number or login <Login> and password <Password>
   And I see Contact list with my name <Name>
    When I open search by clicking plus button
   And I see People picker page
   And I tap on Search input on People picker page
   And I input in People picker search field user name <Contact1>
   And I tap on connected user <Contact1> on People picker page
   And I tap on Search input on People picker page
   And I input in People picker search field user name <Contact2>
   And I tap on connected user <Contact2> on People picker page
   And I click on Go button
   Then I see group chat page with users <Contact1>,<Contact2>
   
   Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2    |
      | user1Email | user1Password | user1Name | user2Name   | user3Name   |