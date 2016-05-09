Feature: Delete

  @C111956 @staging
  Scenario Outline: Verify I can delete messages in 1:1 and from second device
    Given There are 2 users where <Name> is me
    Given user <Name> adds a new device SecondDevice with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I send picture <PictureName> to the current conversation
    And I write message <YouTubeLink>
    And I send message
    And I write message <SpotifyLink>
    And I send message
    And I write message <SoundCloudLink>
    And I send message
    And I click ping button
    And I write message car
    And I send message
    And I write message cat
    And I click GIF button
    And I see Giphy popup
    And I verify that the search of the Giphy popup contains cat
    And I see gif image in Giphy popup
    And I click send button in Giphy popup
    And I see sent gif in the conversation view
    And I wait for 2 seconds
    And I send 10MB sized file with name example.txt to the current conversation
    And I wait until file <File> is uploaded completely
    # Video-message
      #second sending of messages, these will be deleted
      Then I send picture <PictureName> to the current conversation
      And I write message <YouTubeLink>
      And I send message
      And I write message <SpotifyLink>
      And I send message
      And I write message <SoundCloudLink>
      And I send message
      And I click ping button
      And I write message car
      And I send message
      And I write message cat
      And I click GIF button
      And I see Giphy popup
      And I verify that the search of the Giphy popup contains cat
      And I see gif image in Giphy popup
      And I click send button in Giphy popup
      Then I see sent gif in the conversation view
      And I wait for 2 seconds
      And I send 10MB sized file with name example.txt to the current conversation
      And I wait until file <File> is uploaded completely
      # Video-message
    And I wait for 1 seconds
    # With video-messages there are 20 messages
    And I see 18 messages in conversation
    When User Myself delete the recent 9 messages from user <Contact> via device SecondDevice
    And I wait for 2 seconds
    Then I see 9 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | PictureName               | YouTubeLink                                | SpotifyLink                                           | SoundCloudLink                                           |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg | http://www.youtube.com/watch?v=JOCtdw9FG-s | https://open.spotify.com/track/0p6GeAWS4VCZddxNbBtEss | https://soundcloud.com/wearegalantis/peanut-butter-jelly |