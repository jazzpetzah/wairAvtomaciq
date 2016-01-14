Feature: Rich Media

  @C714 @id1504 @regression @rc @rc42
  Scenario Outline: Verify you can play/pause SoundCloud media from the Media Bar in conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And Contact <Contact1> send 18 messages to user Myself
    And I scroll to the bottom of conversation view
    And I tap on text input
    And I type the message "<SoundCloudLink>" and send it
    And Contact <Contact1> send message to user Myself
    And I scroll to the bottom of conversation view
    And I press PlayPause media item button
    And I remember the state of PlayPause media item button
    And I swipe down on dialog page until Mediabar appears
    Then I see PAUSE on Mediabar
    And I press PlayPause on Mediabar button
    When I scroll to the bottom of conversation view
    Then I verify the state of PlayPause media item button is changed

    Examples:
      | Name      | Contact1  | SoundCloudLink                                              |
      | user1Name | user2Name | https://soundcloud.com/binary_for_breakfast/star-wars-theme |
      
  @C717 @id1510 @regression @rc
  Scenario Outline: Verify conversation list play/pause controls can change playing SoundCloud media state
    Given There are 2 users where <Name> is me
    Given <Name> is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<SoudCloudLink>" and send it
    # Workaround for bug with autoscroll
    And I scroll to the bottom of conversation view
    And I press PlayPause media item button
    And I press back button
    Then I see PlayPause media content button for conversation <Contact1>

    Examples:
      | Name      | Contact1  | SoudCloudLink                                               |
      | user1Name | user2Name | https://soundcloud.com/binary_for_breakfast/star-wars-theme |
      
  @C412 @id1505 @regression
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation (SoundCloud)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<SoundCloudLink>" and send it
    And I scroll to the bottom of conversation view
    And I press PlayPause media item button
    And I navigate back from dialog page
    Then I see PlayPause media content button for conversation <Contact1>
    When I tap on contact name <Contact2>
    And I see dialog page
    And I press back button
    Then I see PlayPause media content button for conversation <Contact1>
    When I remember the state of PlayPause button next to the <Contact1> conversation
    And I tap PlayPause button next to the <Contact1> conversation
    Then I see the state of PlayPause button next to the <Contact1> conversation is changed

    Examples:
      | Name      | Contact1  | Contact2  | SoundCloudLink                                              |
      | user1Name | user2Name | user3Name | https://soundcloud.com/binary_for_breakfast/star-wars-theme |
      
  @C675 @id170 @regression @rc @rc42
  Scenario Outline: Verify you can send youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<YoutubeLink>" and send it
    Then I see Play button on Youtube container

    Examples:
      | Name      | Contact1  | YoutubeLink                                 |
      | user1Name | user2Name | https://www.youtube.com/watch?v=wTcNtgA6gHs |