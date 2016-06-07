Feature: Video Message

  @C123927 @videomessage @regression
  Scenario Outline: Verify sender can play video message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized video with name <File> to the current conversation
    And I wait until video <File> is uploaded completely
    And I skip if my browser does not support inline video messages
    And I click play button of video <File> in the conversation view
    Then I wait until video <File> is downloaded and starts to play
    And I verify seek bar is shown for video <File> in the conversation view
    And I verify time for video <File> is changing in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size  |
      | user1Email | user1Password | user1Name | user2Name | example.mp4 | 15MB  |

  @C123938 @videomessage @regression
  Scenario Outline: Verify user can delete video message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized video with name <File> to the current conversation
    And I wait until video <File> is uploaded completely
    And I click to delete the latest message
    And I click confirm to delete message
    Then I do not see video message <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size |
      | user1Email | user1Password | user1Name | user2Name | example.mp4 | 5MB  |