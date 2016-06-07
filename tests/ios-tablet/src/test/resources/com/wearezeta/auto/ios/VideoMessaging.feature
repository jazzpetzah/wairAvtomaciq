Feature: Video Messaging

  @C145952 @staging
  Scenario Outline: Verify receiving and playing a video message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When User <Contact> sends file <FileName> having MIME type <MIMEType> to single user conversation <Name> using device <DeviceName>
    And I tap the video message container sent from <Contact>
    #Wait to make sure video is downloaded
    And I wait for <DownloadTimeout> seconds
    Then I see video message player page is opened

    Examples:
      | Name      | Contact   | FileName    | MIMEType  | DeviceName | DownloadTimeout |
      | user1Name | user2Name | testing.mp4 | video/mp4 | Device1    | 6               |