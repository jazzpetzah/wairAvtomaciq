Feature: Video Messaging

  @C111938 @staging
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
