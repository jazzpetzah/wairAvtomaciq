Feature: Assets V2 and V3

  @C345360 @regression
  Scenario Outline: Verify I can receive V3 assets
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName>
    Given User <Contact> switches assets to V3 via device <DeviceName>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # Giphy
    When User <Contact> sends giphy to user Myself with query "robin" via device <DeviceName>
    Then I see Image container in the conversation view
    And I see the picture in the conversation is animated
    #Image
    When User <Contact> sends encrypted image <ImageFile> to single user conversation Myself
    And I scroll to the bottom of conversation view
    Then I see Image container in the conversation view
    # Link Preview
    When User <Contact> send encrypted message "<Url>" via device <DeviceName> to user Myself
    And I scroll to the bottom of conversation view
    Then I see Link Preview container in the conversation view
    # Video
    When <Contact> sends local file named "<VideoFile>" and MIME type "<VideoMIMEType>" via device <DeviceName> to user Myself
    And I scroll to the bottom of conversation view
    Then I see Video Message container in the conversation view
    And I see Play button on the recent video message in the conversation view
    # Audio
    When <Contact> sends local file named "<AudioFile>" and MIME type "<AudioMIMEType>" via device <DeviceName> to user Myself
    And I scroll to the bottom of conversation view
    Then I see Audio Message container in the conversation view
    And I see Play button on the recent audio message in the conversation view

    Examples:
      | Name      | Contact   | DeviceName | ImageFile      | Url                  | VideoFile   | VideoMIMEType | AudioFile | AudioMIMEType |
      | user1Name | user2Name | device1    | avatarTest.png | https://www.wire.com | testing.mp4 | video/mp4     | test.m4a  | audio/mp4     |

  @C345363 @regression
  Scenario Outline: Verify I could see someone change his avatar on v3 build(Update 'picture' and 'asset')
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap conversation name from top toolbar
    And I take screenshot
    And <Contact> has an avatar picture from file <avatar>
    Then I verify the previous and the current screenshots are different

    Examples:
      | Name      | Contact   | avatar         |
      | user1Name | user2Name | avatarTest.png |
