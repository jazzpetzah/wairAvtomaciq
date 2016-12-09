Feature: File Transfer

  @C82815 @filetransfer @regression @localytics
  Scenario Outline: Verify file can be uploaded and re-downloaded by sender himself in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I enable localytics via URL parameter
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized file with name <File> to the current conversation
    Then I verify icon of file <File> in the conversation view
    And I see file transfer for file <File> in the conversation view
    And I verify size of file <File> is <Size> in the conversation view
    When I wait until file <File> is uploaded completely
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
    When I click icon to download file <File> in the conversation view
    And I wait until file <File> is downloaded completely
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
    # And I verify the downloaded file is the same as the uploaded file <File>
    And I see localytics event <Event> with attributes <Attributes>

    Examples:
      | Login      | Password      | Name      | Contact   | File       | Size  | Type | Event                        | Attributes                                                                                            |
      | user1Email | user1Password | user1Name | user2Name | C82815.txt | 0B    | TXT  | media.completed_media_action | {\"action\":\"file\",\"conversation_type\":\"one_to_one\",\"is_ephemeral\":false,\"with_bot\":false}" |
      | user1Email | user1Password | user1Name | user2Name | C82815.zip | 512KB | ZIP  | media.completed_media_action | {\"action\":\"file\",\"conversation_type\":\"one_to_one\",\"is_ephemeral\":false,\"with_bot\":false}" |

  @C95632 @filetransfer @regression
  Scenario Outline: Verify file can be uploaded and re-downloaded by sender himself in group
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    Then I see file transfer button in conversation input
    When I send <Size> sized file with name <File> to the current conversation
    Then I verify icon of file <File> in the conversation view
    And I see file transfer for file <File> in the conversation view
    And I verify size of file <File> is <Size> in the conversation view
    When I wait until file <File> is uploaded completely
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
    When I click icon to download file <File> in the conversation view
    And I wait until file <File> is downloaded completely
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
  # And I verify the downloaded file is the same as the uploaded file <File>

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | File          | Size  | Type   | ChatName          |
      | user1Email | user1Password | user1Name | user2Name | user3Name | C95632.txt    | 0B    | TXT    | SendFileGroupChat |
      | user1Email | user1Password | user1Name | user2Name | user3Name | C95632.tar.gz | 512KB | TAR.GZ | SendFileGroupChat |

  @C82816 @filetransfer @regression
  Scenario Outline: Verify big file can be uploaded and re-downloaded by sender himself in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized file with name <File> to the current conversation
    Then I verify status of file <File> is UPLOADING… in the conversation view if possible
    And I see file transfer for file <File> in the conversation view
    And I verify size of file <File> is <Size> in the conversation view
    When I wait until file <File> is uploaded completely
    And I verify icon of file <File> in the conversation view
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
    When I click icon to download file <File> in the conversation view
    Then I verify status of file <File> is DOWNLOADING… in the conversation view if possible
    When I wait until file <File> is downloaded completely
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify icon of file <File> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
  # And I verify the downloaded file is the same as the uploaded file <File>

    Examples:
      | Login      | Password      | Name      | Contact   | File       | Type | Size |
      | user1Email | user1Password | user1Name | user2Name | C82816.txt | TXT  | 24MB |

  @C82817 @filetransfer @regression
  Scenario Outline: Verify warning is shown if file size is too big
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized file with name <File> to the current conversation
    Then I see file transfer limit warning modal
    When I click on "Ok" button in file transfer limit warning modal
    Then I do not see file transfer limit warning modal
    And I do not see file transfer for file <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File       | Size |
      | user1Email | user1Password | user1Name | user2Name | C82817.txt | 26MB |

  @C82818 @filetransfer @mute
  Scenario Outline: Verify error on sender side is shown if upload breaks
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized file with name <File> to the current conversation
    And I verify status of file <File> is UPLOADING… in the conversation view
    When I refresh page
    Then I verify status of file <File> is UPLOAD FAILED in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File       | Size |
      | user1Email | user1Password | user1Name | user2Name | C82818.jpg | 24MB |

  @C82819 @filetransfer @regression
  Scenario Outline: Verify re-download is possible on sender side if download is interrupted
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized file with name <File> to the current conversation
    And I wait until file <File> is uploaded completely
    When I click icon to download file <File> in the conversation view
    And I refresh page
    Then I verify type of file <File> is <Type> in the conversation view
    When I click icon to download file <File> in the conversation view
    And I wait until file <File> is downloaded completely
    # Then I verify the downloaded file is the same as the uploaded file <File>

    Examples:
      | Login      | Password      | Name      | Contact   | File       | Size | Type |
      | user1Email | user1Password | user1Name | user2Name | C82819.txt | 24MB | TXT  |

  @C82822 @filetransfer @regression
  Scenario Outline: Verify sender is able to cancel upload
    Given There are 2 users where <Name> is me
    Given <Contact2> has unique username
    Given <Name> is connected to <Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login2> and password <Password2>
    Given I am signed in properly
    When I see Contact list with name <Name>
    And I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I see Contact list with name <Contact2>
    And I open conversation with <Contact2>
    And I see file transfer button in conversation input
    When I send <Size> sized file with name <File> to the current conversation
    And I cancel file upload of file <File>
    Then I do not see file transfer for file <File> in the conversation view
    When I open preferences by clicking the gear button
    And I click logout in account preferences
    And I see the clear data dialog
    And I click logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I am signed in properly
    And I open conversation with <Name>
    Then I do not see file transfer for file <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact2  | File       | Size | Login2     | Password2     |
      | user1Email | user1Password | user1Name | user2Name | C82822.txt | 24MB | user2Email | user2Password |

  @C87933 @filetransfer @regression
  Scenario Outline: Verify file can be downloaded and decrypted by receiver in 1:1
    Given There are 2 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact1>
    When I open conversation with <Contact1>
    When <Contact1> sends <Size> sized file with name <File> via device Device1 to user <Name>
    When I wait until placeholder for file <File> disappears
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
    When I click icon to download file <File> in the conversation view
    And I verify status of file <File> is DOWNLOADING… in the conversation view if possible
    And I wait until file <File> is downloaded completely
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
  # And I verify the downloaded file is the same as the uploaded file <File>

    Examples:
      | Login      | Password      | Name      | Contact1  | File       | Size | Type |
      | user1Email | user1Password | user1Name | user2Name | C87933.txt | 15MB | TXT  |

  @C95631 @filetransfer @regression
  Scenario Outline: Verify file can be downloaded and decrypted by receiver in group
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    When <Contact1> sends <Size> sized file with name <File> via device Device1 to group conversation <ChatName>
    When I wait until placeholder for file <File> disappears
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
    When I click icon to download file <File> in the conversation view
    And I verify status of file <File> is DOWNLOADING… in the conversation view if possible
    And I wait until file <File> is downloaded completely
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
  # And I verify the downloaded file is the same as the uploaded file <File>
    When I open conversation with <Contact1>
    And I see 1 messages in conversation
    And I open conversation with <ChatName>
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | File       | Size | Type | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | C95631.txt | 24MB | TXT  | GroupChat |

  @C95630 @filetransfer @regression
  Scenario Outline: Verify file can be downloaded and decrypted by sender on second device
    Given There are 2 users where <Name> is me
    Given user <Name> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    When <Name> sends <Size> sized file with name <File> via device Device1 to user <Contact>
    When I wait until placeholder for file <File> disappears
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
    When I click icon to download file <File> in the conversation view
    And I wait until file <File> is downloaded completely
    Then I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
# And I verify the downloaded file is the same as the uploaded file <File>

    Examples:
      | Login      | Password      | Name      | Contact   | File       | Size | Type |
      | user1Email | user1Password | user1Name | user2Name | C95630.txt | 15MB | TXT  |