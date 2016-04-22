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

  @staging @C87629 @C87632
  Scenario Outline: Verify placeholder is shown for the receiver
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And <Contact1> sends <FileSize> file having name "<FileName>.<FileExtension>" and MIME type "<MimeType>" via device Device1 to user Myself
    Then I see new message notification "Shared a file"
    And I see the result of <FileSize> file received having name "<FileName>.<FileExtension>" and extension "<FileExtension>"

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

  @staging @C87643
  Scenario Outline: Verify notification on sender side if upload has failed
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I push <FileSize> file having name "<FileName>.<FileExtension>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap plus button in text input
    And I tap File button from input tools
    And I tap back button in upper toolbar
    And I tap on contact name <Contact2>
    And I enable Airplane mode on the device
    Then I see new message notification "File upload failed"
    And I disable Airplane mode on the device

    Examples:
      | Name      | Contact1  | Contact2  | FileName  | FileExtension | FileSize  |
      | user1Name | user2Name | user3Name | qa_random | txt           | 24.00MB   |


  @staging @C87635
  Scenario Outline: Verify downloading file by sender
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I push local file named "<FileName>.<FileExtension>" to the device
    Given I remove the file "1_<FileName>.<FileExtension>" from device's sdcard
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I tap plus button ion text input
    And I tap File button from input tools
    And I wait up to <UploadingTimeout> seconds until <FileSize> file with extension "<FileExtension>" is uploaded
    And I tap View button on file upload placeholder
    And I save file from file dialog
    Then I wait up <DownloadTimeout> seconds until <FileExactSize> file having name "1_<FileName>.<FileExtension>" and MIME type "<MIMEType>" is downloaded to the device

    Examples:
      | Name      | Contact1  | FileName | FileExtension | FileSize | UploadingTimeout | MIMEType  | DownloadTimeout | FileExactSize |
      | user1Name | user2Name | animated | gif           | 440KB    | 20               | image/gif | 10              | 451009B       |

  @staging @C87634
  Scenario Outline: Verify downloading file by receiver
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I remove the file "<FileName>.<FileExtension>" from device's sdcard
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And <Contact1> sends local file named "<FileName>.<FileExtension>" and MIME type "<MIMEType>" via device Device1 to user Myself
    And I see the result of <FileSize> file received having name "<FileName>.<FileExtension>" and extension "<FileExtension>" in <ReceivingTimeout> seconds
    And I remember the state of Download button on file download placeholder
    And I tap Download button on file download placeholder
    And I wait up to <DownloadTimeout> seconds until the state of Download button on file download placeholder is changed
    And I tap View button on file download placeholder
    And I save file from file dialog
    Then I wait up <DownloadTimeout> seconds until <FileExactSize> file having name "<FileName>.<FileExtension>" and MIME type "<MIMEType>" is downloaded to the device

    Examples:
      | Name      | Contact1  | FileName   | FileExtension | FileSize | MIMEType  | DownloadTimeout | FileExactSize | ReceivingTimeout |
      | user1Name | user2Name | avatarTest | png           | 5.68KB   | image/png | 10              | 5813B         | 60               |
