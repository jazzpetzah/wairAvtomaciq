Feature: Rich Media

  @C714 @outdate_regression @outdate_rc @legacy
  Scenario Outline: (load bug AN-4559) Verify you can play/pause SoundCloud media from the Media Bar which is shown below the upper toolbar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I see the upper toolbar
    And I remember the state of upper toolbar
    And User <Contact1> sends 18 encrypted messages to user Myself
    # Wait until message fully loaded
    And I wait for 3 seconds
    And I tap on text input
    And I type the message "<SoundCloudLink>" and send it by cursor Send button
    And User <Contact1> sends encrypted message to user Myself
    And I scroll to the bottom of conversation view
    And I wait for 3 seconds
    And I tap Play button on SoundCloud container
    And I remember the state of Play button on SoundCloud container
    And I swipe down on conversation until Mediabar appears
    And I tap PlayPause on Mediabar button
    Then I verify the state of upper toolbar item is not changed
    And I see the media bar is below the upper toolbar
    When I scroll to the bottom of conversation view
    Then I verify the state of Play button on SoundCloud container is changed

    Examples:
      | Name      | Contact1  | SoundCloudLink                                   |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |

  @C717 @outdate_regression @outdate_rc
  Scenario Outline: Verify Conversations list play/pause controls can change playing SoundCloud media state
    Given There are 2 users where <Name> is me
    Given <Name> is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap on text input
    And I type the message "<SoudCloudLink>" and send it by cursor Send button
    # Workaround for bug with autoscroll for next two lines
    And I scroll to the bottom of conversation view
    And I scroll up the conversation view
    And I tap Play button on SoundCloud container
    And I tap back button
    Then I see PlayPause media content button for conversation <Contact1>

    Examples:
      | Name      | Contact1  | SoudCloudLink                                    |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |

  @C412 @outdate_regression
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation (SoundCloud)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap on text input
    And I type the message "<SoundCloudLink>" and send it by cursor Send button
    And I scroll to the bottom of conversation view
    And I tap Play button on SoundCloud container
    And I tap back button
    Then I see PlayPause media content button for conversation <Contact1>
    When I tap on conversation name <Contact2>
    And I tap back button
    Then I see PlayPause media content button for conversation <Contact1>
    When I remember the state of PlayPause button next to the <Contact1> conversation
    And I tap PlayPause button next to the <Contact1> conversation
    Then I see the state of PlayPause button next to the <Contact1> conversation is changed

    Examples:
      | Name      | Contact1  | Contact2  | SoundCloudLink                                   |
      | user1Name | user2Name | user3Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |

  @C675 @regression @rc @legacy
  Scenario Outline: Verify you can send youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap on text input
    And I type the message "<YoutubeLink>" and send it by cursor Send button
    Then I see Play button on Youtube container

    Examples:
      | Name      | Contact1  | YoutubeLink                                 |
      | user1Name | user2Name | https://www.youtube.com/watch?v=wTcNtgA6gHs |

  @C139848 @outdate_regression
  Scenario Outline: AN-4152 Verify that play of soundcloud track will be stopped by incoming voice call
    Given There are 2 users where <Name> is me
    Given <Name> is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> sends encrypted message <SoundCloudLink> to user Myself
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I scroll to the bottom of conversation view
    And I tap Play button on SoundCloud container
    And I remember the state of Pause button on SoundCloud container
    And <Contact> calls me
    And I see incoming call from <Contact>
    And <Contact> stops calling me
    And I do not see incoming call
    And I scroll to the bottom of conversation view
    Then I verify the state of Pause button on SoundCloud container is changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                   | CallBackend |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme | zcall       |

  @C139850 @outdate_regression
  Scenario Outline: Verify that play of soundcloud track will be stopped by incoming video call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given User <Contact> sends encrypted message <SoundCloudLink> to user Myself
    Given I tap on conversation name <Contact>
    When I scroll to the bottom of conversation view
    And I tap Play button on SoundCloud container
    And I remember the state of Pause button on SoundCloud container
    And <Contact> starts a video call to me
    And I see incoming video call
    And <Contact> stops calling me
    And I do not see incoming video call
    And I scroll to the bottom of conversation view
    Then I verify the state of Pause button on SoundCloud container is changed

    Examples:
      | Name      | Contact   | CallBackend | SoundCloudLink                                   |
      | user1Name | user2Name | chrome      | https://soundcloud.com/sodab/256-ra-robag-wruhme |
