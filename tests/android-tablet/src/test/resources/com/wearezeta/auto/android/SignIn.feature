Feature: Sign In

  @id326 @smoke
  Scenario Outline: Sign in to ZClient
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I attempt to press Login button
    Then I see personal info page loaded with my name <Name>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |


  @id1413 @regression
  Scenario Outline: Negative case for sign in
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I attempt to press Login button
    Then Login error message appears
    And Contains wrong name or password text

    Examples: 
      | Login   | Password |
      | aaa 	| aaa 	   |