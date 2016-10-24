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

  @C259584 @C259585 @staging @fastLogin
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
    Then I see "<EphemeralTimeLabel>" on the message toolbox in conversation view
    When I remember the recent message from user Myself in the local database
    And I wait for <Timeout> seconds
    Then I see 1 message in the conversation view
    And I verify the remembered message has been changed in the local database

    Examples:
      | Name      | Contact   | Timeout | EphemeralTimeLabel |
      | user1Name | user2Name | 15      | seconds            |

  @C259586 @staging @fastLogin
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

  @torun @C259588 @staging @fastLogin
  Scenario Outline: Verify sending ephemeral picture/audio/video/ping/location/file/gif
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Hourglass button in conversation view
    And I set ephemeral messages expiration timer to <Timer> seconds
    #And I tap Add Picture button from input tools
    #And I accept alert
    #And I accept alert
    #And I select the first picture from Keyboard Gallery
    #And I tap Confirm button on Picture preview page
    #Then I see 1 photo in the conversation view
    #And I remember Image container state
    #And I wait for 15 seconds
    #Then I see asset container state is changed
    #And I long tap Audio Message button from input tools
    #And I tap Send record control button
    #Then I see audio message container in the conversation view
    #And I remember Audio container state
    #And I tap Share Location button from input tools
    #And I accept alert
    # Small delay waiting location detection animation to finish
    #And I wait for 5 seconds
    #And I tap Send location button from map view
    #And I remember Location container state
    #Then I see location map container in the conversation view
    And I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    And I tap Send button on Giphy preview page
    And I remember GIF container state
    And I wait for 15 seconds
    Then I see asset container state is changed

    Examples:
      | Name      | Contact   | Timer | GiphyTag |
      | user1Name | user2Name | 15    | sun      |
