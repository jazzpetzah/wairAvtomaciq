Feature: Rich Media

  @C3183 @rc @regression @IPv6
  Scenario Outline: I can send and play inline youtube link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I post media link <YouTubeLink>
    And I click video container for the first time
    # Wait until web page is loaded
    And I wait for 5 seconds
    Then I see video player page is opened

    Examples:
      | Name      | Contact   | YouTubeLink                                |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU |

  @C3210 @rc @regression @IPv6
  Scenario Outline: (MediaBar disappears on Simulator) Play/pause SoundCloud media link from the media bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
    And I tap on text input
    And I tap on media container in conversation view
    And I scroll media out of sight until media bar appears
    And I pause playing the media in media bar
    Then I see media is paused on Media Bar
    And I press play in media bar
    Then I see media is playing on Media Bar
    And I stop media in media bar
    Then I see media is stopped on Media Bar

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C3205 @regression
  Scenario Outline: (MediaBar disappears on Simulator) Conversation gets scrolled back to playing media when clicking on media bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
    And I scroll to the bottom of the conversation
    And I tap on media container in conversation view
    And I scroll media out of sight until media bar appears
    And I tap on the media bar
    Then I see conversation view is scrolled back to the playing media link <SoundCloudLink>

    Examples:
      | Name      | Contact   | SoundCloudLink                                   |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |

  @C3206 @regression
  Scenario Outline: (MediaBar disappears on Simulator) Verify the Media Bar dissapears after playback finishes - SoundCloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    And I tap on contact name <Contact>
    And I scroll to the bottom of the conversation
    When I tap on media container in conversation view
    And I scroll media out of sight until media bar appears
    Then I wait up to 35 seconds for media bar to disappear

    Examples:
      | Name      | Contact   | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  @C3207 @regression
  Scenario Outline: (MediaBar disappears on Simulator) Verify the Media Bar disappears when playing media is back in view - SoundCloud
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Name> sends 40 encrypted messages to user <Contact1>
    Given User <Name> sends encrypted message "<SoundCloudLink>" to user <Contact1>
    When I tap on contact name <Contact1>
    And I scroll to the bottom of the conversation
    And I tap on media container in conversation view
    When I scroll media out of sight until media bar appears
    And I scroll to the bottom of the conversation
    Then I dont see media bar on dialog page

    Examples:
      | Name      | Contact1  | SoundCloudLink                                                   |
      | user1Name | user2Name | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |


  @C140 @regression
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation - SoundCloud
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 40 encrypted messages to user <Contact1>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact1>
    Given User Myself sends 40 encrypted messages to user <Contact2>
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact2>
    When I tap on contact name <Contact1>
    And I tap on media container in conversation view
    And I navigate back to conversations list
    And I tap Pause button in conversations list next to <Contact1>
    And I tap on contact name <Contact2>
    And I tap on media container in conversation view
    And I navigate back to conversations list
    And I tap Pause button in conversations list next to <Contact2>
    And I tap on contact name <Contact2>
    And I scroll media out of sight until media bar appears
    Then I see media is paused on Media Bar

    Examples:
      | Name      | Contact1  | Contact2  | SoundCloudLink                                                                       |
      | user1Name | user2Name | user3Name | https://soundcloud.com/revealed-recordings/dannic-shermanology-wait-for-you-download |


  @C141 @rc @regression
  Scenario Outline: Play/pause controls can change playing media state (SoundCloud)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted message "<SoundCloudLink>" to user Myself
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