Feature: File Transfer

  @C82815 @filetransfer
  Scenario Outline: Verify file can be uploaded and re-downloaded by sender himself in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
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
    And I verify type of file <File> is <Type> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size    | Type  |
      | user1Email | user1Password | user1Name | user2Name | example.txt | 0B      | PLAIN |
      | user1Email | user1Password | user1Name | user2Name | example.zip | 512KB   | ZIP   |

  @C82816 @filetransfer
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
    Then I verify icon of file <File> in the conversation view
    And I see file transfer for file <File> in the conversation view
    And I verify size of file <File> is <Size> in the conversation view
    And I verify status of file <File> is UPLOADING… in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size |
      | user1Email | user1Password | user1Name | user2Name | example.txt | 25MB |

  @C82817 @filetransfer
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
      | Login      | Password      | Name      | Contact   | File        | Size |
      | user1Email | user1Password | user1Name | user2Name | example.txt | 26MB |

  @C82822 @filetransfer
  Scenario Outline: Verify sender is able to cancel upload
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send <Size> sized file with name <File> to the current conversation
    And I cancel file upload of file <File>
    And I verify status of file <File> is CANCELED in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size    |
      | user1Email | user1Password | user1Name | user2Name | example.txt | 25600KB |

  @C82823 @filetransfer
  Scenario Outline: Verify gifs are inlined if shared via file transfer
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send file <File> to the current conversation
    And I see sent gif in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        |
      | user1Email | user1Password | user1Name | user2Name | example.gif |

  @C82824 @filetransfer
  Scenario Outline: Verify JPEGs and PNGs are inlined if shared via file transfer
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I see file transfer button in conversation input
    When I send file <File> to the current conversation
    And I see sent picture <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File         |
      | user1Email | user1Password | user1Name | user2Name | example.jpg  |
      | user1Email | user1Password | user1Name | user2Name | example.jpeg |
      | user1Email | user1Password | user1Name | user2Name | example.png  |

  @C87933 @filetransfer
  Scenario Outline: Verify file can be downloaded and decrypted by receiver in 1:1
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    When <Contact> sends <Size> sized file with name <File> via device Device1 to user <Name>
    Then I see file transfer for file <File> in the conversation view
    And I verify status of file <File> is UPLOADING… in the conversation view
    And I verify icon of file <File> in the conversation view
    And I verify size of file <File> is <Size> in the conversation view
    And I verify type of file <File> is <Type> in the conversation view
    And I wait until file <File> is uploaded completely
    When I click to download file <File> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact   | File        | Size | Type |
      | user1Email | user1Password | user1Name | user2Name | example.txt | 15MB | TEXT |