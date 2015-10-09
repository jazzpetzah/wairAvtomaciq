Feature: Application

  @smoke @id3805
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

  @smoke @id3807
  Scenario Outline: I verify the installed app is not too big
    When I verify the app is not bigger than 121 MB

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @smoke @id3730
  Scenario Outline: Sign Out
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I click menu bar item "Wire" and menu item "Sign Out"
    Then I see Sign In page

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |