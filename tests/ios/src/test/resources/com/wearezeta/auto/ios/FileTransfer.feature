Feature: File Transfer

  @C82524 @regression @rc @fastLogin
  Scenario Outline: Verify placeholder is shown for the receiver
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given I sign in using my email or phone number
    Given User <Contact> sends temporary file <FileName>.<FileExt> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Given I see conversations list
    Given I tap on contact name <Contact>
    # Wait for sync
    Given I wait for 3 seconds
    When I wait up to <Timeout> seconds until the file <FileName>.<FileExt> with size <FileSize> is ready for download from conversation view
    And I tap file transfer action button
    Then I see File Actions menu

    Examples:
      | Name      | Contact   | FileName | FileExt | FileSize | FileMIME                 | ContactDevice | Timeout |
      | user1Name | user2Name | testing  | bin     | 240 KB   | application/octet-stream | device1       | 20      |

  @C82517 @regression @fastLogin
  Scenario Outline: Verify file transfer icon exists in cursor area in 1-to-1 and group conversations
    Given There are <UsersAmount> users where <Name> is me
    Given Myself is connected to all other
    Given Myself has group chat <GroupChatName> with all other
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I see File Transfer button in input tools palette
    Then I navigate back to conversations list
    When I tap on group chat with name <GroupChatName>
    And I see File Transfer button in input tools palette

    Examples:
      | Name      | Contact   | GroupChatName | UsersAmount |
      | user1Name | user2Name | GroupChat     | 3           |

  @C82518 @regression @fastLogin
  Scenario Outline: Verify placeholder is shown for the sender
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap File Transfer button from input tools
    # Wait for transition
    And I wait for 5 seconds
    And I tap file transfer menu item <ItemName>
    Then I see file transfer placeholder
    #wait tp make sure file was delivered
    And I wait for 5 seconds
    And I see "<DeliveredLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact   | ItemName                   | DeviceName1 | DeliveredLabel |
      | user1Name | user2Name | FTRANSFER_MENU_DEFAULT_PNG | device1     | Delivered      |

  @C82529 @regression @fastLogin
  Scenario Outline: Verify not supported file has no preview and share menu is opened
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I create temporary file <FileSize> in size with name "<FileName>" and extension "<FileExt>"
    Given I sign in using my email or phone number
    Given User <Contact1> sends temporary file <FileName>.<FileExt> having MIME type <FileMIME> to group conversation <GroupChatName> using device <ContactDevice>
    Given User <Contact1> sends 1 encrypted message to user Myself
    Given I see conversations list
    When I tap on contact name <GroupChatName>
    # Wait for the placeholder
    And I wait for 3 seconds
    Then I wait up to <Timeout> seconds until the file <FileName>.<FileExt> with size <FileSize> is ready for download from conversation view
    When I tap file transfer action button
    Then I wait up to <Timeout> seconds until I see generic file share menu

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | FileName | FileExt | FileSize | FileMIME                 | ContactDevice | Timeout |
      | user1Name | user2Name | user3Name | FTransfer     | testing  | tmp     | 240 KB   | application/octet-stream | device1       | 20      |

  @C95960 @rc @regression @fastLogin
  Scenario Outline: Verify sending file in the empty conversation and text after it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap File Transfer button from input tools
    # Wait for the placeholder
    And I wait for 5 seconds
    And I tap file transfer menu item <ItemName>
    Then I see file transfer placeholder
    When I type the default message and send it
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact   | ItemName                   |
      | user1Name | user2Name | FTRANSFER_MENU_DEFAULT_PNG |

  @C82523 @regression @fastLogin
  Scenario Outline: Verify notification is shown if file size is more than 25 MB
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap File Transfer button from input tools
    # Wait for the placeholder
    And I wait for 5 seconds
    And I tap file transfer menu item <ItemName>
    Then I see alert contains text <ExpectedAlertText>
    When I accept alert
    Then I do not see file transfer placeholder

    Examples:
      | Name      | Contact   | ItemName | ExpectedAlertText        |
      | user1Name | user2Name | TOO_BIG  | You can send files up to |