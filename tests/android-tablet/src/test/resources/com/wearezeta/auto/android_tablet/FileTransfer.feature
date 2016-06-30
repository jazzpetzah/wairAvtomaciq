Feature: File Transfer

  @C164771 @regression @rc
  Scenario Outline: Verify I can send file and placeholder is shown
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact>
    When I tap File button from cursor toolbar
    And I remember the state of View button on file upload placeholder
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    And I see the result of <FileSize> file upload having name "<FileName>.<FileExtension>" and extension "<FileExtension>"
    Then I wait up to <UploadingTimeout> seconds until the state of View button on file upload placeholder is changed

    Examples:
      | Name      | Contact   | FileName  | FileExtension | FileSize | UploadingTimeout |
      | user1Name | user2Name | qa_random | txt           | 9.00MB   | 20               |
