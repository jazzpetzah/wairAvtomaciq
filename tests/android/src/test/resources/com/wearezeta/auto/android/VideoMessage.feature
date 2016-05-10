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
    When I tap Play button on the recent video message in the conversation view
    # Wait for the video to be fully loaded
    And I wait for 5 seconds
    And I tap Play button on the recent video message in the conversation view
    Then I see the Wire app is not in foreground

    Examples:
      | Name      | Contact   | FileName    | MIMEType  | DeviceName |
      | user1Name | user2Name | testing.mp4 | video/mp4 | Device1    |

