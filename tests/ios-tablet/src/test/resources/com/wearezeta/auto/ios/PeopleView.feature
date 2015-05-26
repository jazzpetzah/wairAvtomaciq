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
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

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
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |
      
  @torun @staging @id2434
  Scenario Outline: Start group chat from 1:1 conversation [PORTRAIT]
  	Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I open conversation details on iPad
    And I see <Contact1> user profile page in iPad popover
    And I press Add button on iPad popover
    And I see People picker page in iPad popover
    And I tap on Search input on People picker page iPad popover
    And I input in People picker search field on iPad popover user name <Contact2>
    And I see user <Contact2> found on People picker page iPad popover
    And I click on connected user <Contact2> avatar on People picker page iPad popover
    And I tap on Search input on People picker page iPad popover
    And I input in People picker search field on iPad popover user name <Contact3>
    And I see user <Contact3> found on People picker page iPad popover
    And I click on connected user <Contact3> avatar on People picker page iPad popover
    And I click on Go button on iPad popover
    And I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I swipe right on group chat page
    And I see Contact list with my name <Name>
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>
    
    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   | Contact3 |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  | user4Nmae|
  
  
  @staging @id2653
  Scenario Outline: Start group chat from 1:1 conversation [LANDSCAPE]
  	Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I Sign in using phone number or login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
   And I open conversation details on iPad
    And I see <Contact1> user profile page in iPad popover
    And I press Add button on iPad popover
    And I see People picker page in iPad popover
    And I tap on Search input on People picker page iPad popover
    And I input in People picker search field on iPad popover user name <Contact2>
    And I see user <Contact2> found on People picker page iPad popover
    And I click on connected user <Contact2> avatar on People picker page iPad popover
    And I tap on Search input on People picker page iPad popover
    And I input in People picker search field on iPad popover user name <Contact3>
    And I see user <Contact3> found on People picker page iPad popover
    And I click on connected user <Contact3> avatar on People picker page iPad popover
    And I click on Go button on iPad popover
    And I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I swipe right on group chat page
    And I see Contact list with my name <Name>
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>
    
    Examples: 
      | Login      | Password      | Name      | Contact1    | Contact2   | Contact3 |
      | user1Email | user1Password | user1Name | user2Name   | user3Name  | user4Name|
