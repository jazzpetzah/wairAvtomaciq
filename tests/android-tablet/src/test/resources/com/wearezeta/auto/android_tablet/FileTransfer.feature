Feature: File Transfer

  @C164771 @regression @rc
  Scenario Outline: Verify I can send file and placeholder is shown
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I push <FileSize><FileSizeType> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap the conversation <Contact>
    And I tap File button from cursor toolbar
    And I wait up to <UploadingTimeout> seconds until <FileSize> <FileSizeType> file with extension "<FileExtension>" is uploaded
    Then I see the result of "<FileSize> <FileSizeType>" file upload having name "<FileName>.<FileExtension>" and extension "<FileExtension>"

    Examples:
      | Name      | Contact   | FileName  | FileExtension | FileSize | UploadingTimeout | FileSizeType |
      | user1Name | user2Name | qa_random | txt           | 1.00     | 20               | MB           |
