Feature: Delete

  @C111956 @staging
  Scenario Outline: Verify I can delete messages in 1:1 and from second device
    Given There are 2 users where <Name> is me
    Given user <Name> adds a new device SecondDevice with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    Given I see Contact list with name <Contact>
    When I open conversation with <Contact>
    Then I send picture <PictureName> to the current conversation
    And I write message <YouTubeLink>
    And I send message
    And I write message <SpotifyLink>
    And I send message
    And I write message <SoundCloudLink>
    And I send message
    And I click ping button
    And I write message car
    And I send message
    And I write message cat
    And I click GIF button
    And I see Giphy popup
    And I verify that the search of the Giphy popup contains cat
    And I see gif image in Giphy popup
    And I click send button in Giphy popup
    And I see sent gif in the conversation view
    And I wait for 2 seconds
    And I send 1MB sized file with name example.txt to the current conversation
    And I wait until file example.txt is uploaded completely
    # Video-message
      #second sending of messages, these will be deleted
      Then I send picture <PictureName> to the current conversation
      And I write message <YouTubeLink>
      And I send message
      And I write message <SpotifyLink>
      And I send message
      And I write message <SoundCloudLink>
      And I send message
      And I click ping button
      And I write message car
      And I send message
      And I write message cat
      And I click GIF button
      And I see Giphy popup
      And I verify that the search of the Giphy popup contains cat
      And I see gif image in Giphy popup
      And I click send button in Giphy popup
      Then I see sent gif in the conversation view
      And I wait for 2 seconds
      And I send 1MB sized file with name example.txt to the current conversation
      And I wait until file example.txt is uploaded completely
      # Video-message
    And I wait for 1 seconds
    # With video-messages there are 20 messages
    And I see 18 messages in conversation
    When User Myself delete the recent 9 messages from user <Contact> via device SecondDevice
    And I wait for 2 seconds
    Then I see 9 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | PictureName               | YouTubeLink                                | SpotifyLink                                           | SoundCloudLink                                           |
      | user1Email | user1Password | user1Name | user2Name | userpicture_landscape.jpg | http://www.youtube.com/watch?v=JOCtdw9FG-s | https://open.spotify.com/track/0p6GeAWS4VCZddxNbBtEss | https://soundcloud.com/wearegalantis/peanut-butter-jelly |

  @C111959 @regression
  Scenario Outline: Verify deleted messages remain deleted after I archive and unarchive a conversation
    Given There are 2 users where <Name> is me
    Given user <Name> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message_1>
    And I send message
    And I write message <Message_2>
    And I send message
    And I write message <Message_3>
    And I send message
    Then I see text message <Message_1>
    And I see text message <Message_2>
    And I see text message <Message_3>
    When I click to delete the latest message
    And I click confirm to delete message
    And User Myself deletes the recent 1 message from user <Contact> via device Device1
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I do not see text message <Message_3>
    When I archive conversation <Contact>
    And I do not see Contact list with name <Contact>
    And I open archive
    And I see archive list with name <Contact>
    And I unarchive conversation <Contact>
    And I see Contact list with name <Contact>
    And I open conversation with <Contact>
    Then I see text message <Message_1>
    And I do not see text message <Message_2>
    And I do not see text message <Message_3>

    Examples:
      | Login      | Password      | Name      | Contact   | Message_1      | Message_2      | Message_3      |
      | user1Email | user1Password | user1Name | user2Name | Test_Message_1 | Test_Message_2 | Test_Message_3 |


  @C111960 @staging
  Scenario Outline: Verify messages get deleted even when I was offline on time of deletion
    Given There are 2 users where <Name> is me
    Given user <Name> adds a new device SecondDevice with label Label1
    Given user <Contact> adds a new device ContactDevice with label Label1
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I see the history info page
    Given I click confirm on history info page
    Given I am signed in properly
    Given I open conversation with <Contact>
    Given I write message <Message1>
    Given I send message
    Given I write message <Message2>
    Given I send message
    Given Contact <Contact> sends message <Message3> to user Myself
    Given I see text message <Message1>
    Given I see text message <Message2>
    Given I see text message <Message3>
    Given I open self profile
    Given I click gear button on self profile page
    Given I select Log out menu item on self profile page
    Given I see the clear data dialog
    Given I click Logout button on clear data dialog
    Given I see Sign In page
    And Contact Myself sends message <Message4> via device SecondDevice to user <Contact>
    And Contact <Contact> sends message <Message5> via device ContactDevice to user Myself
    When User Myself deletes the recent 4 messages from user <Contact> via device SecondDevice
    And Contact Myself sends message <Message6> via device SecondDevice to user <Contact>
    And I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    Then I see text message <Message1>
    And I see text message <Message6>
    And I do not see text message <Message2>
    And I do not see text message <Message3>
    And I do not see text message <Message4>
    And I do not see text message <Message5>
    And I see 3 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message1  | Message2       | Message3       | Message4       | Message5       | Message6  |
      | user1Email | user1Password | user1Name | user2Name | Remains 1 | Gets deleted 2 | Gets deleted 3 | Gets deleted 4 | Gets deleted 5 | Remains 6 |


  @C111958 @staging
  Scenario Outline: I cannot delete certain types of messages (system messages)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I open conversation with <Contact1>
    When I hover over the latest message
    Then I do not see delete button for latest message
    When I click People button in group conversation
    And I see Group Participants popover
    And I click Add People button on Single User Profile popover
    And I input user name <Contact2> in search field on Group Participants popover
    And I select user <Contact2> from Group Participants popover search results
    And I choose to create conversation from Single User Profile popover
    And I see STARTED action in conversation
    And I hover over the latest message
    Then I do not see delete button for latest message
    When I click People button in group conversation
    And I see Group Participants popover
    And I change group conversation title to <ChatName> on Group Participants popover
    And I click People button in group conversation
    And I see RENAMED action in conversation
    And I hover over the latest message
    Then I do not see delete button for latest message
    When I click People button in group conversation
    And I see Group Participants popover
    And I click on participant <Contact2> on Group Participants popover
    And I click Remove button on Group Participants popover
    And I confirm remove from group chat on Group Participants popover
    And I click People button in group conversation
    And I see REMOVED action in conversation
    And I hover over the latest message
    Then I do not see delete button for latest message
    When I click People button in group conversation
    And I see Group Participants popover
    And I click Add People button on Group Participants popover
    And I input user name <Contact2> in search field on Group Participants popover
    And I select user <Contact2> from Group Participants popover search results
    And I choose to create group conversation from Group Participants popover
    And I see ADDED action in conversation
    And I hover over the latest message
    Then I do not see delete button for latest message
    When I call
    And I wait for 5 seconds
    And I hang up call with conversation <ChatName>
    And I see CALLED action in conversation
    And I hover over the latest message
    Then I do not see delete button for latest message

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | New name |
