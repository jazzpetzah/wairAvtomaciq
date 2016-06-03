Feature: Audio Messaging

  @C129323 @C129321 @regression
  Scenario Outline: Verify message is started recording by long tapping on the icon
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    Then I see audio message record container

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C129327 @regression
  Scenario Outline: Verify sending voice message by check icon tap
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    And I tap Send record control button
    Then I see audio message placeholder

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C129341 @C129345 @regression
  Scenario Outline: Verify receiving a voice message and deleting it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact1>
    When User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Then I see audio message placeholder
    When I long tap on audio message placeholder in conversation view
    And I tap on Delete badge item
    Then I do not see audio message placeholder

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       |

  @C129326 @regression
  Scenario Outline: Verify sending voice message by swipe up
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I record 5 seconds long audio message and send it using swipe up gesture
    Then I see audio message placeholder

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C131214 @regression
  Scenario Outline: Verify cancelling recorded audio message preview
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    And I tap Cancel record control button
    Then I do not see audio message placeholder
    And I see Audio Message button in input tools palette

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C129349 @staging
  Scenario Outline: Verify deleting playing voice message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I record 30 seconds long audio message and send it using swipe up gesture
    And I tap Play audio message button
    And I long tap on audio message placeholder in conversation view
    And I tap on Delete badge item
    Then I do not see audio message placeholder

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C131214 @regression
  Scenario Outline: Verify not sent yet audio message is preserved on minimising the app
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    And I close the app for 3 seconds
    Then I see audio message record container

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C131219 @regression
  Scenario Outline: Verify not sent yet audio message is deleted on switching between the conversations
    Given There are 3 user where <Name> is me
    Given Myself is connected to all other
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    Then I see audio message record container
    When I navigate back to conversations list
    And I tap on contact name <Contact2>
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I do not see audio message record container

    Examples:
      | Name      | Contact   | Contact2  |
      | user1Name | user2Name | user3Name |

  @C129346 @staging
  Scenario Outline: Verify impossibility of saving voice message before downloading
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact1>
    When User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    And I long tap on audio message placeholder in conversation view
    Then I do not see Save badge item
    When I tap Play audio message button
    # Small wait to make sure download is completed
    And I wait for 5 seconds
    And I long tap on audio message placeholder in conversation view
    Then I see Save badge item

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       |

  @C131217 @staging
  Scenario Outline: Verify playback is stopped when other audio message starts playing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    When I tap Play audio message button on audio message placeholder number 2
    # Wait until the audio is downloaded and starts playback
    And I wait for <AudioDownloadTimeout> seconds
    And I remember the state of Pause button on the second audio message placeholder
    And I tap Play audio message button on audio message placeholder number 1
    # Wait until the audio is downloaded
    And I wait for <AudioDownloadTimeout> seconds
    Then I verify the state of Pause button on audio message placeholder is changed

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice | AudioDownloadTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       | 7                    |
  
  @C139855 @staging
  Scenario Outline: (Bug ZIOS-6759) Verify playback is stopped when incoming call has appeared
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When User <Contact> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    And I tap on contact name <Contact>
    And I remember the state of Play button on audio message placeholder
    And I tap Play audio message button
    # Wait to make sure the audio file is downloaded and starts playback
    And I wait for <AudioDownloadTimeout> seconds
    And <Contact> calls me
    And I see call status message contains "<Contact> calling"
    And I tap Ignore button on Calling overlay
    Then I verify the state of Play button on audio message placeholder is not changed

    Examples:
      | Name      | Contact   | FileName | FileMIME  | ContactDevice | CallBackend | AudioDownloadTimeout |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       | chrome      | 5                    |