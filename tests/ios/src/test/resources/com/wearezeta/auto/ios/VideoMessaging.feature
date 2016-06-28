Feature: Video Messaging

  @C111938 @rc @regression
  Scenario Outline: Verify recording a video
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I prepare <FileName> to be uploaded as a video message
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Message button from input tools
    Then I see a preview of video message

    Examples:
      | Name      | Contact   | FileName    |
      | user1Name | user2Name | testing.mp4 |

  @C125733 @rc @regression
  Scenario Outline: Verify receiving video message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And User <Contact> sends file <FileName> having MIME type <MIMEType> to single user conversation <Name> using device <DeviceName>
    # Wait until video preview is generated
    And I wait for 6 seconds
    When I tap the video message container sent from <Contact>
    # Wait to make sure video is downloaded
    And I wait for 12 seconds
    Then I see video message player page is opened

    Examples:
      | Name      | Contact   | FileName    | MIMEType  | DeviceName |
      | user1Name | user2Name | testing.mp4 | video/mp4 | Device1    |

