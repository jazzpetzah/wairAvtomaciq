Feature: Change Profile Picture

  @smoke @id344
  Scenario Outline: Change your profile picture
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on personal screen
    And I press Camera button
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I press Confirm button
    And I return to personal page
    Then I see changed user picture <Picture>

    Examples: 
      | Login   | Password    | Name    | Picture                      |
      | aqaUser | aqaPassword | aqaUser | userpicture_mobile_check.jpg |

  @regression @id1055
  Scenario Outline: Attempt to enter a name with 0 chars
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I attempt to enter an empty name and press return
    And I see error message asking for more characters
    And I attempt to enter an empty name and tap the screen
    And I see error message asking for more characters

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  @regression @id729
  Scenario Outline: Attempt to open About screen in settings
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
	And I click on Settings button on personal page
	And I click on About button on personal page
	Then I see About page

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |  
         
   @staging @id482  
   Scenario Outline: Verify user can access settings
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
	Then I see settings page

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |      
      
   @regression @id1258 
   Scenario Outline: Verify default value for sound settings is all
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on Settings button from the options menu
    When I tap on Sound Alerts
    And I see the Sound alerts page
    Then I verify that all is the default selected value

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
  