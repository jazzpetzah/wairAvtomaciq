Feature: Application

  @smoke @id3805
  Scenario Outline: Save size and position of window and check for maximum/minimum size
    Given There are 1 users where <Name> is me
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I ensure initial positioning
    And I resize the app to the max by hand
    Then I verify app is in fullscreen
    When I restart the app
    And I see my avatar on top of Contact list
    Then I verify app is in fullscreen
    When I ensure initial positioning
    And I resize the app to the min by hand
    Then I verify app is in minimum size
    When I restart the app
    And I see my avatar on top of Contact list
    Then I verify app is in minimum size
    When I ensure initial positioning
    And I maximize the app
    Then I verify app is in fullscreen
    When I restart the app
    And I see my avatar on top of Contact list
    Then I verify app is in fullscreen
    When I ensure initial positioning
    And I resize the app to the min by hand
    And I resize the app to width 999 px and height 800 px
    And I change position of the app to X 100 and Y 100
    And I restart the app
    And I see my avatar on top of Contact list
    Then I verify app width is 999 px and height is 800 px
    And I verify app X coordinate is 100 and Y coordinate is 100

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @smoke @id3995
  Scenario: Verify size of sign in screen window
    When I ensure initial positioning
    Then I verify app width is 403 px and height is 618 px
    And I resize the app to the max by hand
    Then I verify app width is 403 px and height is 618 px
    When I ensure initial positioning
    And I resize the app to the min by hand
    Then I verify app width is 403 px and height is 618 px

  @staging @id3807
  Scenario: I verify the installed app is not too big
    When I verify the app is not bigger than 400 MB

  @smoke @id3529
  Scenario Outline: Verify existing About page
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I click menu bar item "Help" and menu item "About Wire"
    Then I verify about window is visible
    When I close the about window
    Then I verify about window is not visible

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

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

  @smoke @id3981
  Scenario Outline: Verify I can quit the app using menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I click menu bar item "Wire" and menu item "Quit Wire"
    And I wait for 5 seconds
    Then I verify app has quit

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3982
  Scenario: Verify I can quit the app using shortcut Alt + F4
    When I type shortcut combination to quit the app
    And I wait for 5 seconds
    Then I verify app has quit
