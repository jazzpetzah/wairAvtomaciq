Feature: Video Messaging

  @C145952 @rc @regression @fastLogin
  Scenario Outline: Verify receiving and playing a video message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends file <FileName> having MIME type <MIMEType> to single user conversation <Name> using device <DeviceName>
    Given I see conversations list
    Given I tap on contact name <Contact>
    # Wait to make sure video is downloaded
    Given I wait for <DownloadTimeout> seconds
    When I tap on video message in conversation view
    # Wait to make sure video is downloaded
    And I wait for <DownloadTimeout> seconds
    Then I see video message player page is opened

    Examples:
      | Name      | Contact   | FileName    | MIMEType  | DeviceName | DownloadTimeout |
      | user1Name | user2Name | testing.mp4 | video/mp4 | Device1    | 6               |

  @C145951 @rc @regression @fastLogin
  Scenario Outline: Verify recording a video message [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I prepare <FileName> to be uploaded as a video message
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    And I tap Video Message button from input tools
    Then I see video message container in the conversation view

    Examples:
      | Name      | Contact   | FileName    |
      | user1Name | user2Name | testing.mp4 |