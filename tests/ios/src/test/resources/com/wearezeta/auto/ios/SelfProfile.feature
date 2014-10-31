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

  @staging @id1055
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
