Feature: User Profile

  @smoke @id180
  Scenario Outline: Change user picture from image file
    Given I have 1 users and 0 contacts for 0 users
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I go to user <Name> profile
    When I open picture settings
    And I see photo in User profile
    And I choose to select picture from image file
    And I select image file userpicture_landscape.jpg
    And I open picture settings
    Then I see changed user picture

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

  #@regression
#  @staging @id425
#  Scenario Outline: Change user picture from camera
#    Given I have 1 users and 0 contacts for 0 users
#    Given I Sign in using login <Login> and password <Password>
#    And I see my name <Name> in Contact list
#    And I go to user <Name> profile
#    When I open picture settings
#    And I see photo in User profile
#    And I choose to select picture from camera
#    And I shoot picture using camera
#    Then I see changed user picture

#    Examples: 
#      | Login   | Password    | Name    |
#      | aqaUser | aqaPassword | aqaUser |

  @staging @id183
  Scenario Outline: Profile photo can be deleted
    Given I have 1 users and 0 contacts for 0 users
    Given I Sign in using login <Login> and password <Password>
    And I see my name <Name> in Contact list
    And I go to user <Name> profile
    When I open picture settings
    And I choose to select picture from image file
    And I select image file userpicture_landscape.jpg
    And I open picture settings
    And I see changed user picture
    And I select to remove photo
    And I confirm photo removing
    Then I see user profile picture is not set

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |
