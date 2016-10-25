Feature: Ephemeral Messages

  @C259591 @regression @fastLogin
  Scenario Outline: Verify ephemeral messages don't leave a trace in the database
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timeout> seconds
    Given I type the default message and send it
    Given I see 1 default message in the conversation view
    When I remember the recent message from user Myself in the local database
    And I wait for <Timeout> seconds
    Then I see 0 default messages in the conversation view
    And I verify the remembered message has been changed in the local database
    When User <Contact> switches user Myself to ephemeral mode with <Timeout> seconds timeout
    And User <Contact> sends 1 encrypted message to user Myself
    # Wait for the message to be delivered
    And I wait for 3 seconds
    And I see 1 default message in the conversation view
    And I remember the state of the recent message from user <Contact> in the local database
    And I wait for <Timeout> seconds
    Then I see 0 default messages in the conversation view
    And I verify the remembered message has been deleted from the local database

    Examples:
      | Name      | Contact   | DeviceName    | Timeout |
      | user1Name | user2Name | ContactDevice | 15      |

  @C259589 @regression @fastLogin
  Scenario Outline: Verify ephemeral messages are disabled in a group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Name> has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    Then I do not see Hourglass button in conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | TESTCHAT      | 

  @C259584 @C259585 @regression @fastLogin
  Scenario Outline: Verify sending ephemeral message - no online receiver (negative case)
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timeout> seconds
    And I type the default message and send it
    And I see 1 default message in the conversation view
    And I remember the recent message from user Myself in the local database
    Then I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    When I wait for <Timeout> seconds
    Then I see 1 message in the conversation view
    And I verify the remembered message has been changed in the local database

    Examples:
      | Name      | Contact   | Timeout | EphemeralTimeLabel |
      | user1Name | user2Name | 15      | seconds            |

  @C264665 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral message - online receiver (positive case)
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timeout> seconds
    And I type the default message and send it
    And I see 1 default message in the conversation view
    And I remember the recent message from user Myself in the local database
    Then I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    When I wait for <Timeout> seconds
    And I verify the remembered message has been changed in the local database
    And User <Contact> reads the recent message from user <Name>
    And I wait for <Timeout> seconds
    Then I see 0 message in the conversation view


    Examples:
      | Name      | Contact   | Timeout | EphemeralTimeLabel | DeviceName    |
      | user1Name | user2Name | 15      | seconds            | ContactDevice |

  @C259586 @regression @fastLogin
  Scenario Outline: Verify switching on/off ephemeral message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    Then I see Ephemeral input placeholder text
    And I see Time Indicator button in conversation view
    When I type the default message and send it
    And I tap Time Indicator button in conversation view
    And I set ephemeral messages expiration timer to Off
    Then I see Standard input placeholder text

    Examples:
      | Name      | Contact   | Timer |
      | user1Name | user2Name | 15    |

  @C259588 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral picture
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    And I tap Add Picture button from input tools
    And I accept alert
    And I accept alert
    And I select the first picture from Keyboard Gallery
    And I tap Confirm button on Picture preview page
    Then I see 1 photo in the conversation view
    And I remember Image container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer |
      | user1Name | user2Name | 15    |

  @C310632 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral audio message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    And I long tap Audio Message button from input tools
    And I tap Send record control button
    Then I see audio message container in the conversation view
    And I remember Audio container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer |
      | user1Name | user2Name | 15    |

  @C310633 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral video message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I prepare <FileName> to be uploaded as a video message
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    And I tap Video Message button from input tools
    Then I see video message container in the conversation view
    # Wait for delivery
    And I wait for 8 seconds
    And I remember Video container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | FileName    |
      | user1Name | user2Name | 15    | testing.mp4 |

  @C310634 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral share location
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    And I tap Share Location button from input tools
    And I accept alert
    # Small delay waiting location detection animation to finish
    And I wait for 5 seconds
    And I tap Send location button from map view
    And I remember Location container state
    And I see location map container in the conversation view
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer |
      | user1Name | user2Name | 15    |

  @torun @C310635 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral file share
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap Hourglass button in conversation view
    Given I set ephemeral messages expiration timer to <Timer> seconds
    Given I navigate back to conversations list
    Given I tap on contact name <Contact>
    Given I tap File Transfer button from input tools
    # Wait for transition
    Given I wait for 2 seconds
    Given I tap file transfer menu item <ItemName>
    Given I see file transfer placeholder
    #wait tp make sure file was delivered
    Given I wait for 5 seconds
    When I remember File Share container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | ItemName                   |
      | user1Name | user2Name | 15    | FTRANSFER_MENU_DEFAULT_PNG |

  @C310636 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral GIF
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    And I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    And I tap Send button on Giphy preview page
    And I remember GIF container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | GiphyTag |
      | user1Name | user2Name | 15    | sun      |

  #works with message content in database and count of messages
  @C310637 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral media link
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    And I type the "<SoundCloudLink>" message and send it
    #wait to be sure video is delivered
    And I wait for 5 seconds
    Then I see media container in the conversation view
    When I remember the recent message from user Myself in the local database
    And I wait for <Timer> seconds
    Then I see 1 message in the conversation view
    And I verify the remembered message has been changed in the local database

    Examples:
      | Name      | Contact   | Timer | SoundCloudLink                                                   |
      | user1Name | user2Name | 15    | https://soundcloud.com/tiffaniafifa2/overdose-exo-short-acoustic |

  #works with screenshot comparison
  @C311066 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral link preview
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    And I type the "<Link>" message and send it
    Then I see link preview container in the conversation view
    And I remember Link Preview container state
    And I wait for <Timer> seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | Link                 |
      | user1Name | user2Name | 15    | https://www.wire.com |