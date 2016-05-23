Feature: Audio Message

  @C129782 @audiomessage @staging
  Scenario Outline: Verify you can send and play the audio file in 1:1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send audio file with length <Time> and name <File> to the current conversation
    And I wait for 60 seconds
    #And I wait until video <File> is uploaded completely
    #And I click play button of video <File> in the conversation view
    #Then I wait until video <File> is downloaded and starts to play
    #And I verify seek bar is shown for video <File> in the conversation view
    #And I verify time for video <File> is <Time> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Time  |
      | user1Email | user1Password | user1Name | user2Name | example.wav | 00:20 |