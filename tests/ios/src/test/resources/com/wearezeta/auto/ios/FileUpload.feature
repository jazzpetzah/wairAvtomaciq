Feature: File Upload

  @C82519 @staging @torun
  Scenario Outline: Verify placeholder is shown for the receiver
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I create temporary file <TmpFileSize> in size with name "<TmpFileName>" and extension "<TmpFileExt>"
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And User <Contact> sends file <TmpFileName>.<TmpFileExt> having MIME type <TmpFileMIME> to single user conversation <Name> using device <ContactDevice>
    Then I wait up to <Timeout> seconds until download progress for the file <TmpFileName>.<TmpFileExt> with size <TmpFileSize> is finished

    Examples:
      | Name      | Contact   | TmpFileName | TmpFileExt | TmpFileSize | TmpFileMIME | ContactDevice | Timeout |
      | user1Name | user1Name | test        | txt        | 500.00KB    | text/plain  | device1       | 20      |
