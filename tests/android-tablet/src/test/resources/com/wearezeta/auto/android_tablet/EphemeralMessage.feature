Feature: Ephemeral message

  @C399840 @staging
  Scenario Outline: Verify sending all types of messages after I enable ephemeral mode
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I push <FileSize> video file having name "random_qa.mp4" to the device
    Given I push local file named "avatarTest.png" to the device
    Given I push <FileSize> file having name "qa_random.txt" to the device
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    Given I tap Ephemeral button from cursor toolbar
    Given I set timeout to <EphemeraTimeout> on Extended cursor ephemeral overlay
    Given I tap on text input
  # Video
    When I tap Video message button from cursor toolbar
    And I see Video Message container in the conversation view
    And I remember the state of Video Message container in the conversation view
    And I do not see Message status with expected text "Sending" in conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Video Message container is changed
  # Picture
    When I tap Add picture button from cursor toolbar
    And I tap Gallery Camera button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view
    And I remember the state of Image container in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Image container is changed
  # Audio message
    When I long tap Audio message cursor button 5 seconds and swipe up
    Then I see Audio Message container in the conversation view
    And I remember the state of Audio Message container in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Audio Message container is changed
  # Ping
    When I tap Ping button from cursor toolbar
    Then I see Ping message "<PingMsg>" in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeraTimeout>
    And I do not see Ping message "YOU PINGED" in the conversation view
  # File
    When I tap File button from cursor toolbar
    Then I see File Upload container in the conversation view
    And I remember the state of File Upload container in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of File Upload container is changed
  # Location
    When I tap Share location button from cursor toolbar
    And I tap Send button on Share Location page
    Then I see Share Location container in the conversation view
    And I remember the state of Share Location container in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Share Location container is changed
  # Link Preview
    When I type the message "<Link>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    Then I see Link Preview container in the conversation view
    And I remember the state of Link Preview container in the conversation view
    And I do not see Message status with expected text "<MessageStatus>" in conversation view
    And I wait for <EphemeraTimeout>
    And I verify the state of Link Preview container is changed

    Examples:
      | Name      | Contact   | EphemeraTimeout | Link                 | MessageStatus | PingMsg    | FileSize |
      | user1Name | user2Name | 5 seconds       | http://www.wire.com/ | Sending       | YOU PINGED | 1.00MB   |
