Feature: Assets

  @C345366 @regression @fastLogin
  Scenario Outline: Receive V3 assets
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given User <Contact> switches assets to V3 protocol via device <DeviceName>
    Given I create temporary file <BinaryFileSize> in size with name "<BinaryFileName>" and extension "<BinaryFileExt>"
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
     # Picture
    When User <Contact> sends encrypted image <Picture> to single user conversation Myself
    And I wait for <SyncTimeout> seconds
    Then I see 1 photo in the conversation view
     # Video
    When User <Contact> sends file <FileName> having MIME type <VideoMIME> to single user conversation Myself using device <DeviceName>
    And I wait for <SyncTimeout> seconds
    Then I see video message container in the conversation view
     # Audio
    When User <Contact> sends file <AudioFileName> having MIME type <AudioMIME> to single user conversation Myself using device <DeviceName>
    And I wait for <SyncTimeout> seconds
    Then I see audio message container in the conversation view
     # Link Preview
    And User <Contact> sends encrypted message "<Link>" to user Myself
    And I wait for <SyncTimeout> seconds
    Then I see link preview container in the conversation view
     # Binary file
    When User <Contact> sends temporary file <BinaryFileName>.<BinaryFileExt> having MIME type <BinaryFileMIME> to single user conversation Myself using device <DeviceName>
    And I wait for <SyncTimeout> seconds
    Then I see file transfer placeholder
     # TODO: Add Giphy

    Examples:
      | Name      | Contact   | SyncTimeout | DeviceName    | Picture     | FileName    | VideoMIME | AudioFileName | AudioMIME | Link         | BinaryFileName | BinaryFileExt | BinaryFileSize | BinaryFileMIME           |
      | user1Name | user2Name | 5           | ContactDevice | testing.jpg | testing.mp4 | video/mp4 | test.m4a      | audio/mp4 | www.wire.com | testing        | tmp           | 240 KB         | application/octet-stream |

  @C345364 @regression @fastLogin
  Scenario Outline: (Not implemented yet) Verify I could see someone changes his avatar on v3 build
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I open conversation details
    When I remember user picture on Single user profile page
    # TODO: Change it to v3 only when it is supported by iOS client
    And User <Contact> changes avatar picture to default
    # Given it some time to refresh
    And I wait for 7 seconds
    Then I see user picture is changed on Single user profile page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
