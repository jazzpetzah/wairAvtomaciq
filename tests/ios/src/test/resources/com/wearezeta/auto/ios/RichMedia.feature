Feature: Rich Media

  @C3183 @rc @regression @IPv6 @fastLogin
  Scenario Outline: I can send and play inline youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<YouTubeLink>" message and send it
    #wait to be sure video is delivered
    And I wait for 8 seconds
    And I see "<DeliveredLabel>" on the message toolbox in conversation view
    And I tap on media container in conversation view
    # Wait until web page is loaded
    And I wait for 5 seconds
    Then I see video player page is opened

    Examples:
      | Name      | Contact   | YouTubeLink                                | DeviceName1 | DeliveredLabel |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU | device1     | Delivered      |

  @C3210 @regression @IPv6 @fastLogin
  Scenario Outline: (MediaBar disappears on Simulator) Play/pause SoundCloud media link from the media bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap on text input
    And I tap on media container in conversation view
    And I scroll to the top of the conversation
    And I pause playing the media in media bar
    Then I see media is paused on Media Bar
    And I tap Play in media bar
    Then I see media is playing on Media Bar
    And I stop media in media bar
    Then I see media is stopped on Media Bar

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C3205 @regression @fastLogin
  Scenario Outline: (MediaBar disappears on Simulator) Conversation gets scrolled back to playing media when clicking on media bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I scroll to the bottom of the conversation
    Given I tap on media container in conversation view
    When I scroll to the top of the conversation
    And I tap on the media bar
    Then I see media container <SoundCloudLink> in conversation view

    Examples:
      | Name      | Contact   | SoundCloudLink                                   |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |

  @C3206 @regression @fastLogin
  Scenario Outline: (MediaBar disappears on Simulator) Verify the Media Bar dissapears after playback finishes - SoundCloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I scroll to the bottom of the conversation
    When I tap on media container in conversation view
    And I scroll to the top of the conversation
    Then I wait up to 35 seconds for media bar to disappear

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C3207 @regression @fastLogin
  Scenario Outline: (MediaBar disappears on Simulator) Verify the Media Bar disappears when playing media is back in view - SoundCloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given User <Name> sends 40 encrypted messages to user <Contact1>
    Given User <Name> sends encrypted message "<SoundCloudLink>" to user <Contact1>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I scroll to the bottom of the conversation
    Given I tap on media container in conversation view
    When I scroll to the top of the conversation
    And I scroll to the bottom of the conversation
    Then I do not see media bar in the conversation view

    Examples:
      | Name      | Contact1  | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C140 @regression @fastLogin
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation - SoundCloud
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User Myself sends 40 encrypted messages to user <Contact1>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact1>
    Given User Myself sends 40 encrypted messages to user <Contact2>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact2>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    Given I tap on media container in conversation view
    Given I navigate back to conversations list
    When I tap Pause button in conversations list next to <Contact1>
    And I tap on contact name <Contact2>
    And I tap on media container in conversation view
    And I navigate back to conversations list
    And I tap Pause button in conversations list next to <Contact2>
    And I tap on contact name <Contact2>
    And I scroll to the bottom of the conversation
    Then I see media is paused on Media Bar

    Examples:
      | Name      | Contact1  | Contact2  | SoundCloudLink                                                                       |
      | user1Name | user2Name | user3Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |

  @C141 @rc @regression @fastLogin
  Scenario Outline: Play/pause controls can change playing media state (SoundCloud)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted message "<SoundCloudLink>" to user Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    And I remember media container state
    And I tap on media container in conversation view
    And I navigate back to conversations list
    And I wait for 1 second
    And I tap Pause button in conversations list next to <Contact>
    And I tap on contact name <Contact>
    Then I see media container state is not changed
    When I remember media container state
    And I navigate back to conversations list
    And I wait for 1 second
    And I tap Play button in conversations list next to <Contact>
    And I tap on contact name <Contact>
    Then I see media container state is changed

    Examples:
      | Name      | Contact   | SoundCloudLink                                                            |
      | user1Name | user2Name | https://soundcloud.com/isabella-emanuelsson/david-guetta-she-wolf-falling |