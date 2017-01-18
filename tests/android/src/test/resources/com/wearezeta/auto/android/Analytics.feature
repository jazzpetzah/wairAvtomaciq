Feature: Analytics

  @C165111 @regression @rc @analytics
  Scenario Outline: Verify registration statistics is sent
    When I see welcome screen
    And I verify that <LogType> log contains string "registration.opened_phone_signup"
    And I input a new phone number for user <Name>
    Then I verify that <LogType> log contains string "registration.entered_phone"
    When I input the verification code
    And I verify that <LogType> log contains string "registration.verified_phone"
    And I input my name
    Then I verify that <LogType> log contains string "registration.entered_name"
    And I verify that <LogType> log contains string "registration.succeeded"
    When I select to choose my own picture
    And I select Camera as picture source
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I verify that <LogType> log contains string "registration.added_photo"
    When I see Unique Username Takeover page
    And I see username on Unique Username Takeover page
    And I tap Keep This One button on Unique Username Takeover page
    Then I see Conversations list with no conversations

    Examples:
      | Name      | LogType   |
      | user1Name | ANALYTICS |

  @C167026 @analytics @regression
  Scenario Outline: Verify media statistics is sent
    Given I am on Android with Google Location Service
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I push 1.00MB video file having name "random_qa.mp4" to the device
    Given I push local file named "avatarTest.png" to the device
    Given I push 1.00MB file having name "qa_random.txt" to the device
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Audio message
    When I long tap Audio message cursor button 3 seconds and swipe up
    And I see Audio Message container in the conversation view
    And I wait for 5 seconds
    Then I verify that <LogType> log contains string "media.sent_audio_message"
    And I verify that <LogType> log contains string "ACTION=audio"
    # Text message
    When I type the message "<Message>" and send it by cursor Send button
    Then I verify that <LogType> log contains string "media.completed_media_action"
    And I verify that <LogType> log contains string "ACTION=text"
    # Ping
    When I tap Ping button from cursor toolbar
    Then I verify that <LogType> log contains string "ACTION=ping"
    # Sketch
    When I tap Sketch button from cursor toolbar
    And I draw a sketch with 2 colors
    And I send my sketch
    Then I verify that <LogType> log contains string "ACTION=photo"
    # Gif viq Giphy
    When I type the message "<Message>"
    And I tap Gif button from cursor toolbar
    And I select a random gif from the grid preview
    And I see giphy preview page
    And I tap on the giphy Send button
    Then I verify that <LogType> log contains string "ACTION=photo" 2 times
    # Photo from camera
    When I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    Then I verify that <LogType> log contains string "ACTION=photo" 3 times
    # Photo from gallery
    When I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    Then I verify that <LogType> log contains string "ACTION=photo" 4 times
    # Location
    When I tap Share location button from cursor toolbar
    And I tap Send button on Share Location page
    # Issue: Autoscroll
    And I scroll to the bottom of conversation view
    And I see Share Location container in the conversation view
    Then I verify that <LogType> log contains string "ACTION=location"
    # Video message
    When I tap Video message button from cursor toolbar
    # Issue: Autoscroll, and wait for video message animation
    And I wait for 3 seconds
    And I scroll to the bottom of conversation view
    And I see Video Message container in the conversation view
    And I wait for 10 seconds
    Then I verify that <LogType> log contains string "media.sent_video_message"
    And I verify that <LogType> log contains string "ACTION=video"
    # File
    When I tap File button from cursor toolbar
    # Issue: Autoscroll
    And I scroll to the bottom of conversation view
    And I see File Upload container in the conversation view
    And I wait for 5 seconds
    Then I verify that <LogType> log contains string "ACTION=file"
    # Outgoing audio call
    When I tap Audio Call button from top toolbar
    And I see outgoing call
    Then I verify that <LogType> log contains string "ACTION=outgoing_audio_call"
    And I hang up outgoing call
    # Outgoing video call
    When I tap Video Call button from top toolbar
    And I see outgoing video call
    Then I verify that <LogType> log contains string "ACTION=outgoing_video_call"
    And I hang up outgoing call
    And I verify that <LogType> log contains string "media.completed_media_action" 12 times

    Examples:
      | Name      | Contact   | Message | LogType   |
      | user1Name | user2Name | Yo      | ANALYTICS |
