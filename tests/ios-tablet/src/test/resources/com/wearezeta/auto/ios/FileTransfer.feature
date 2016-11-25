Feature: File Transfer

  @C145955 @rc @regression @fastLogin
  Scenario Outline: Verify sending the file in an empty conversation and text after it [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap File Transfer button from input tools
    # Wait for file transfer menu
    And I wait for 3 seconds
    And I tap on iPad file transfer menu item <ItemName>
    When I type the default message and send it
    Then I see 1 default messages in the conversation view
    And I see file transfer placeholder

    Examples:
      | Name      | Contact   | ItemName                   |
      | user1Name | user2Name | FTRANSFER_MENU_DEFAULT_PNG |

  @C145956 @rc @regression @fastLogin
  Scenario Outline: Verify downloading and opening file for a preview [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given User <Contact> sends temporary file <FileName>.<FileExt> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given I see conversations list
    Given I tap on contact name <Contact>
    # Wait for the placeholder to be loaded
    Given I wait for 3 seconds
    When I wait up to <Timeout> seconds until the file <FileName>.<FileExt> with size <FileSize> is ready for download from conversation view
    And I tap file transfer action button
    Then I see File Actions menu

    Examples:
      | Name      | Contact   | FileName | FileExt | FileSize | FileMIME                 | ContactDevice | Timeout |
      | user1Name | user2Name | testing  | bin     | 240 KB   | application/octet-stream | device1       | 20      |