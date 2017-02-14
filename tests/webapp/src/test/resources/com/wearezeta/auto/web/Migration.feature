Feature: Migration

  @migration1
  Scenario Outline: Verify migration is successful from DB version 3 to 4 to 6 to staging
    Given I initially deploy version with tag <DBVersion3>
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <DBVersion3> to user Myself
    And I see text message <DBVersion3>
    And I deploy version with tag <DBVersion4>
    And I refresh page
    Then I am signed in properly
    And I open conversation with <Contact>
    And Contact <Contact> sends message <DBVersion4> to user Myself
    And I see text message <DBVersion3>
    And I wait for 20 seconds
    And I see text message <DBVersion4>
    And I deploy version with tag <DBVersion6>
    And I refresh page
    Then I am signed in properly
    And I open conversation with <Contact>
    And Contact <Contact> sends message <DBVersion6> to user Myself
    And I see text message <DBVersion3>
    And I see text message <DBVersion4>
    And I wait for 20 seconds
    And I see text message <DBVersion6>
    When I deploy latest staging version
    And I refresh page
    Then I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message Staging to user Myself
    Then I see text message <DBVersion3>
    And I see text message <DBVersion4>
    And I see text message Staging

    Examples:
      | Login      | Password      | Name      | Contact   | DBVersion3       | DBVersion4       | DBVersion6       |
      | user1Email | user1Password | user1Name | user2Name | 2016-03-10-10-25 | 2016-04-06-15-06 | 2016-05-09-15-44 |

  @migration2
  Scenario Outline: Verify migration is successful from older DB versions straight to current
    Given I initially deploy version with tag <DBVersion>
    Given There are 2 users where <Name> is me
    Given user <Contact> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And Contact <Contact> sends message <DBVersion> to user Myself
    Then I see text message <DBVersion>
#    When Contact <Contact> sends message <Link> via device Device1 to user me
#    Then I see link <LinkInPreview> in link preview message
#    When <Contact> sends audio file <AudioFile> via device Device1 to user Myself
#    Then I wait until audio <AudioFile> is uploaded completely
#    When <Contact> sends <Size> sized video with name <VideoFile> via device Device1 to user me
#    Then I see video message <VideoFile> in the conversation view
    When User <Contact> sends location <LocationName> with <Latitude> and <Longitude> to user Myself via device Device1
    Then I see location message <LocationName> with <Latitude> and <Longitude> in the conversation view
    When I deploy latest staging version
    And I refresh page
    Then I am signed in properly
    When I open conversation with <Contact>
    Then I see text message <DBVersion>
#    Then I see link <LinkInPreview> in link preview message
#    Then I see audio message <AudioFile> in the conversation view
#    Then I see video message <VideoFile> in the conversation view
    Then I see location message <LocationName> with <Latitude> and <Longitude> in the conversation view
    And Contact <Contact> sends message Staging to user Myself
    And I wait for 20 seconds
    And I see text message Staging

    Examples:
      | Login      | Password      | Name      | Contact   | DBVersion        | Latitude | Longitude | LocationName | VideoFile      | Size  | AudioFile      | ImageName                | Link     | LinkInPreview    |
#      | user1Email | user1Password | user1Name | user2Name | 2016-03-10-10-25 |
#      | user1Email | user1Password | user1Name | user2Name | 2016-04-06-15-06 |
#      | user1Email | user1Password | user1Name | user2Name | 2016-05-09-15-44 |
      | user1Email | user1Password | user1Name | user2Name | 2016-08-29-14-54 | 12.94    | 54.29     | Stralsund    | migration2.mp4 | 15MB  | migration2.m4a | userpicture_portrait.jpg | wire.com | https://wire.com |