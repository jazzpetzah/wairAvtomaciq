Feature: Sign In

  @smoke @id476
  Scenario Outline: Sign in ZClient
    Given I am signed out from ZClient
    And I see Sign In screen
    When I press Sign In button
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Sign In button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  @staging @id525
  Scenario Outline: Change Sign in user
    Given I Sign in using login <Login2> and password <Password>
    And I go to user <Name2> profile
    And I open picture settings
    And I choose to select picture from image file
    And I select image file userpicture_portrait.jpg
    And I see photo in User profile
    When I am signed out from ZClient
    And I Sign in using login <Login> and password <Password>
    Then I see Contact list with name <Name>
    And I see name <Name> in User profile
    And I see email of <Name> in User profile
    And I see changed user picture

    Examples: 
      | Login   | Login2   | Password    | Name    | Name2    |
      | aqaUser | yourUser | aqaPassword | aqaUser | yourUser |
