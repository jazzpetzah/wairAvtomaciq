Feature: File Transfer

  @C82524 @staging
  Scenario Outline: (ZIOS-6392) Verify placeholder is shown for the receiver
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And User <Contact> sends file <FileName>.<FileExt> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Then I wait up to <Timeout> seconds until the file <FileName>.<FileExt> with size <FileSize> is ready for download from conversation view
    When I tap file transfer placeholder
    Then I wait up to <Timeout> seconds until I see a preview of the file named "<FileName>.<FileExt>"

    Examples:
      | Name      | Contact   | FileName | FileExt | FileSize | FileMIME   | ContactDevice | Timeout |
      | user1Name | user2Name | testing  | jpg     | 240 KB   | image/jpeg | device1       | 20      |

  @C82517 @staging
  Scenario Outline: Verify file transfer icon exists in cursor area in 1-to-1 and group conversations
    Given There are <UsersAmount> users where <Name> is me
    Given Myself is connected to all other
    Given Myself has group chat <GroupChatName> with all other
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I see File Transfer button in input tools palette
    Then I navigate back to conversations list
    When I tap on group chat with name <GroupChatName>
    And I click plus button next to text input
    And I see File Transfer button in input tools palette

    Examples:
      | Name      | Contact   | GroupChatName  | UsersAmount |
      | user1Name | user2Name | GroupChat      | 3           |

  @C82518 @staging
  Scenario Outline: Verify placeholder is shown for the sender
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I tap File Transfer button from input tools
    And I tap file transfer menu item <ItemName>
    Then I see file transfer placeholder

    Examples:
      | Name      | Contact   | ItemName    |
      | user1Name | user2Name | DEFAULT_PNG |