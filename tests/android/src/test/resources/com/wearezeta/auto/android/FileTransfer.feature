Feature: File transfer

  @staging @C87628
  Scenario Outline: Verify placeholder is shown for the sender
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap plus button in text input
    And I tap File button from input tools
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    Then I see the result of <FileSize> file upload having name "<FileName>.<FileExtension>" and extension "<FileExtension>"

    Examples:
      | Name      | Contact1  | FileName  | FileExtension | FileSize | UploadingTimeout |
      | user1Name | user2Name | qa_random | txt           | 9.00MB   | 20               |

  @staging @C87636
  Scenario Outline: Verify warning is shown for file size more than 25Mb
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileFullName>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap plus button in text input
    And I tap File button from input tools
    Then I see alert message containing "<AlertMessage>"

    Examples:
      | Name      | Contact1  | FileFullName  | FileSize | AlertMessage                                       |
      | user1Name | user2Name | qa_random.txt | 26.00MB  | Uploading files larger than 25MB is not supported. |

  @staging @C87629 @C87632 @torun
  Scenario Outline: Verify placeholder is shown for the receiver
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And <Contact1> sends <FileSize> file having name "<FileName>.<FileExtension>" and MIME type "<MimeType>" via device Device1 to user Myself
    Then I see new message notification "Shared a file"
    And I see the result of <FileSize> file received having name "<FileName>.<FileExtension>" and extension "<FileExtension>" in 60 seconds


    Examples:
      | Name      | Contact1  | FileName  | FileSize | FileExtension | MimeType   |
      | user1Name | user2Name | qa_random | 3.00MB   | txt           | text/plain |

  @staging @C87639
  Scenario Outline: Verify retry sending a file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I enable Airplane mode on the device
    And I tap on contact name <Contact1>
    And I tap plus button in text input
    And I tap File button from input tools
    Then I see the result of <FileSize> file upload having name "<FileName>.<FileExtension>" and extension "<FileExtension>" failed
    When I disable Airplane mode on the device
    And I wait for 5 seconds
    And I tap Retry button on file upload placeholder
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    Then I see the result of <FileSize> file upload having name "<FileName>.<FileExtension>" and extension "<FileExtension>"

    Examples:
      | Name      | Contact1  | FileName  | FileExtension | FileSize | UploadingTimeout |
      | user1Name | user2Name | qa_random | txt           | 9.00MB   | 20               |