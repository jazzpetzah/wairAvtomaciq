Feature: File Transfer

  @C82519 @staging
  Scenario Outline: Verify placeholder is shown for the receiver
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And User <Contact> sends file <FileName>.<FileExt> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Then I wait up to <Timeout> seconds until the file <FileName>.<FileExt> with size <FileSize> is ready for download

    Examples:
      | Name      | Contact   | FileName | FileExt | FileSize | FileMIME   | ContactDevice | Timeout |
      | user1Name | user2Name | testing  | jpg     | 240 KB   | image/jpeg | device1       | 20      |
