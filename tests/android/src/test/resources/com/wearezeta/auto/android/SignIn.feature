Feature: Sign In

  @id326 @smoke
  Scenario Outline: Sign in to ZClient
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    Then I see Contact list

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

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
    #And I switch to email sign in screen
    #And I have entered login <Login2>
    #And I have entered password <Password>
    #And I press Log in button
    #Then Contact list appears with my name <Login2>

    #Examples: 
    #  | Login1  | Password    | Login2   |
    #  | aqaUser | aqaPassword | yourUser |

  @id1413 @regression
  Scenario Outline: Negative case for sign in
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    Then I see error message "<ErrMessage>"

    Examples: 
      | Login   | Password | ErrMessage                          |
      | aaa 	| aaa 	   | Please enter a valid email address. |

# Selendroid cannot interact with external apps
  # @id2020 @regression
  # Scenario Outline: Verify possibility of reseting password from sign in
  #  Given There is 1 user where <Name> is me
  #  Given I see welcome screen
  #  When I switch to email sign in screen
  #  And I press FORGOT PASSWORD button
  #  And I request reset password for <Login>
  #  And I get new <Name> password link
  #  Then I reset password by URL to new <NewPassword>
  #  When I restore the application
  #  And I wait for 2 seconds
  #  And I minimize the application
  #  And I restore the application
  #  And I switch to email sign in screen
  #  And I have entered login <Login>
  #  And I have entered password <NewPassword>
  #  And I press Log in button
  #  Then I see Contact list
  #
  #  Examples: 
  #    | Login      | Password      | Name      | NewPassword |
  #    | user1Email | user1Password | user1Name | qatest1234  |