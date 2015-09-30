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

  @staging @id3423
  Scenario Outline: Verify I can block user from conversation list with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I open context menu of contact <Contact>
    And I click block in context menu
    Then I see a block warning modal
    And I click cancel button in the block warning
    And I open context menu of contact <Contact>
    And I click block in context menu
    Then I see a block warning modal
    And I click block button in the block warning
    Then I do not see Contact list with name <Contact>
    When User <Contact> sent message <Msg1> to conversation <Name>
    Then I do not see Contact list with name <Contact>
    When I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Contact> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see my avatar on top of Contact list
    And I open conversation with <Name>
    Then I do not see <Action> action for <Name> in conversation

    Examples: 
       | Login      | Password      | Name      | Contact   | Login2     | Password2     | Msg1    | Action |
       | user1Email | user1Password | user1Name | user2Name | user2Email | user2Password | message | LEFT   |

  