Feature: Video Message

  @C119743 @staging @C119740
  Scenario Outline: Verify video is played full screen
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    And I see Video Message container in the conversation view
    And I tap Play button on the recent video message in the conversation view
    # Wait for the video to be fully loaded
    And I wait for 5 seconds
    Then I see the Wire app is not in foreground

    Examples:
      | Name      | Contact   | FileName    | MIMEType  | DeviceName |
      | user1Name | user2Name | testing.mp4 | video/mp4 | Device1    |

  @C119748 @staging
  Scenario Outline: (AN-3951) Verify download video message in offline mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When <Contact> sends local file named "<FileName>" and MIME type "<MIMEType>" via device <DeviceName> to user Myself
    And I see Video Message container in the conversation view
    And I remember the state of Play button on the recent video message in the conversation view
    And I enable Airplane mode on the device
    And I tap Play button on the recent video message in the conversation view
    #Wait for animation
    And I wait for 5 seconds
    Then I verify the state of Play button on the recent video message in the conversation view is changed
    When I disable Airplane mode on the device
    # Wait for sync
    And I wait for 10 seconds
    And I tap Play button on the recent video message in the conversation view
    # Wait for the video to be fully loaded
    And I wait for 5 seconds
    Then I verify the state of Play button on the recent video message in the conversation view is not changed
    When I tap Play button on the recent video message in the conversation view
    Then I see the Wire app is not in foreground

    Examples:
      | Name      | Contact   | FileName    | MIMEType  | DeviceName |
      | user1Name | user2Name | testing.mp4 | video/mp4 | Device1    |

  @C119736 @C119735 @staging
  Scenario Outline: Verify cancelling sending a video message after it was recorded
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I push local file named "<FileName>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I tap Video message button from cursor toolbar
    And I see video message preview
    And I do not send recorded video from video message preview
    Then I do not see Video Message container in the conversation view

    Examples:
      | Name      | Contact   | FileName    |
      | user1Name | user2Name | testing.mp4 |

  @C119739 @staging
  Scenario Outline: Verify notification is never shown if video is too big (more than 25mb)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I push <FileSize> video file having name "<FileFullName>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I tap Video message button from cursor toolbar
    And I send recorded video from video message preview
    Then I see video message compressing overlay

    Examples:
      | Name      | Contact   | FileSize | FileFullName      |
      | user1Name | user2Name | 26.00MB  | random_video.mp4  |