Feature: User Profile

  #Muted till new sync engine client stabilization
  @mute @smoke @id180
  Scenario Outline: Change user picture from image file
    Given I Sign in using login <Login> and password <Password>
    And I go to user <Name> profile
    When I open picture settings
    And I choose to select picture from image file
    And I select image file userpicture_landscape.jpg
    Then I see changed user picture from image userpicture_landscape.jpg

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  #Not stable
  @mute @regression @id425
  Scenario Outline: Change user picture from camera
    Given I Sign in using login <Login> and password <Password>
    And I go to user <Name> profile
    And I open picture settings
    When I choose to select picture from camera
    And I shoot picture using camera
    Then I see changed user picture from camera

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
