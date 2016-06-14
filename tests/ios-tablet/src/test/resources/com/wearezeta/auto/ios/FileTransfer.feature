Feature: File Transfer

  @C145955 @regression
  Scenario Outline: Verify sending the file in an empty conversation and text after it [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap File Transfer button from input tools
    And I tap file transfer menu item <ItemName>
    When I type the default message and send it
    Then I see 1 default messages in the conversation view
    And I see file transfer placeholder

    Examples:
      | Name      | Contact   | ItemName                   |
      | user1Name | user2Name | FTRANSFER_MENU_DEFAULT_PNG |

  @C145956 @staging
  Scenario Outline: Verify downloading and opening file for a preview [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And User <Contact> sends file <FileName>.<FileExt> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Then I wait up to <Timeout> seconds until the file <FileName>.<FileExt> with size <FileSize> is ready for download from conversation view
    When I tap file transfer placeholder
    Then I wait up to <Timeout> seconds until I see a preview of the file named "<FileName>"

    Examples:
      | Name      | Contact   | FileName | FileExt | FileSize | FileMIME   | ContactDevice | Timeout |
      | user1Name | user2Name | testing  | jpg     | 240 KB   | image/jpeg | device1       | 20      |