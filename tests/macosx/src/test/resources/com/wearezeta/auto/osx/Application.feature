Feature: Application

  @staging @id3805
  Scenario Outline: Save size and position of window and check for maximum/minimum size
    When I ensure initial positioning
    And I resize the app to the max by hand
    Then I verify app is in fullscreen
    When I restart the app
    Then I verify app is in fullscreen
    When I ensure initial positioning
    And I resize the app to the min by hand
    Then I verify app is in minimum size
    When I restart the app
    Then I verify app is in minimum size
    When I ensure initial positioning
    And I maximize the app
    Then I verify app is in fullscreen
    When I restart the app
    Then I verify app is in fullscreen
    When I ensure initial positioning
    And I resize the app to the min by hand
    And I resize the app to width 999 px and height 800 px
    And I change position of the app to X 100 and Y 100
    And I restart the app
    Then I verify app width is 999 px and height is 800 px
    And I verify app X coordinate is 100 and Y coordinate is 100

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |
