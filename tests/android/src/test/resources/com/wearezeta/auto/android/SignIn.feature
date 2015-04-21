Feature: Sign In

  @id326 @smoke
  Scenario Outline: Sign in to ZClient
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  #@id209 @smoke
  #Scenario Outline: I can change sign in user
    #Given I have 2 users and 0 contacts for 0 users
    #Given I Sign in using login <Login1> and password <Password>
    #And I see Contact list with my name <Login1>
    #When I tap on my name <Login1>
    #workaround
    #And I minimize the application
    #And I restore the application
    #workaround
    #And I tap options button
    #And I tap sign out button
    #And I see sign in and join buttons
    #And I press Sign in button
    #And I have entered login <Login2>
    #And I have entered password <Password>
    #And I press Log in button
    #Then Contact list appears with my name <Login2>

    #Examples: 
    #  | Login1  | Password    | Login2   |
    #  | aqaUser | aqaPassword | yourUser |

  @id1413 @regression
  Scenario Outline: Negative case for sign in
    Given I see sign in screen
    When I press Sign in button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    Then Login error message appears
    And Contains wrong name or password text

    Examples: 
      | Login   | Password |
      | aaa 	| aaa 	   |
      
  @id2020 @regression
  Scenario Outline: Verify possibility of reseting password from sign in
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I see sign in screen
    When I press Sign in button
    And I press FORGOT PASSWORD button
    And I request reset password for <Login>
    Then I reset <Name> password by URL to new <NewPassword>
    When I restore the application
    And I wait for 2 seconds
    And I minimize the application
    And I restore the application
    And I press Sign in button
    And I have entered login <Login>
    And I have entered password <NewPassword>
    And I press Log in button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login      | Password      | Name      | NewPassword | Contact   |
      | user1Email | user1Password | user1Name | qatest1234  | user2Name |