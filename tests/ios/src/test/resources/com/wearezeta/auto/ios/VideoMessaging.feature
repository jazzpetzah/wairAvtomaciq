Feature: Video Messaging

  @C111938 @rc @regression @fastLogin
  Scenario Outline: Verify recording a video
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{"name": "<DeviceName1>"}]}
    Given I prepare <FileName> to be uploaded as a video message
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Message button from input tools
    Then I see video message container in the conversation view
    # Wait for delivery
    And I wait for 8 seconds
    And I see "<DeliveredLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact   | FileName    | DeviceName1 | DeliveredLabel |
      | user1Name | user2Name | testing.mp4 | device1     | Delivered      |

  @C125733 @rc @regression @fastLogin
  Scenario Outline: Verify receiving video message
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And User <Contact> sends file <FileName> having MIME type <MIMEType> to single user conversation <Name> using device <DeviceName>
    # Wait until video preview is generated
    And I wait for 10 seconds
    When I tap on video message in conversation view
    Then I see video message player page is opened

    Examples:
      | Name      | Contact   | FileName    | MIMEType  | DeviceName |
      | user1Name | user2Name | testing.mp4 | video/mp4 | Device1    |

