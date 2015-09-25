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
    And I resize the app to width 999 px and height 999 px
    And I change position of the app to X 100 and Y 100
    And I restart the app
    Then I verify app width is 999 px and height is 999 px
    And I verify app X coordinate is 100 and Y coordinate is 100

    Examples: 
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @staging @id3423 @torun
   Scenario Outline: Verify I can block user from conversation list with right click
      Given There are 3 users where <Name> is me
      Given Myself is connected to <Contact1>,<Contact2>
      Given I switch to sign in page
      Given I Sign in using login <Login> and password <Password>
      And I see my avatar on top of Contact list
      And I see Contact list with name <Contact1>
      And I see Contact list with name <Contact2>
      And I open context menu of contact <Contact2>
      And I click block in context menu

      Examples: 
         | Login      | Password      | Name      | Contact1  | Contact2  | CallBackend |
         | user1Email | user1Password | user1Name | user2Name | user3Name | autocall    |

  