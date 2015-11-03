Feature: Conversation List

  @id324 @regression @rc
  Scenario Outline: Mute conversation
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I press options menu button
    And I press SILENCE conversation menu button
    #And I return to group chat page
    #Some elements seem to be missing (e.g. "X" button) so
    #Instead of searching for elements, it works perfectly fine (and faster) just to press back 3 times
    And I press back button
    And I press back button
    #And I navigate back from dialog page
    Then Contact <Contact1> is muted

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @id1510 @regression @rc
  Scenario Outline: Verify conversation list play/pause controls can change playing media state (SoundCloud)
    Given There are 2 users where <Name> is me
    Given <Name> is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<SoudCloudLink>" and send it
    # Workaround for bug with autoscroll
    And I scroll to the bottom of conversation view
    And I press PlayPause media item button
    And I navigate back from dialog page
    And I see Contact list
    Then I see PlayPause media content button for conversation <Contact1>

    Examples: 
      | Name      | Contact1  | SoudCloudLink                                              |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @id1514 @regression
  Scenario Outline: Verify unsilence the conversation
    Given There are 2 users where <Name> is me
    Given <Contact1> is connected to me
    Given <Contact1> is silenced to user <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    Given Contact <Contact1> is muted
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    And I press options menu button
    And I press NOTIFY conversation menu button
    And I press back button
    And I navigate back from dialog page
    Then Contact <Contact1> is not muted

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @id1505 @regression
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation (SoundCloud)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap on text input
    And I type the message "<SoundCloudLink>" and send it
    And I scroll to the bottom of conversation view
    And I press PlayPause media item button
    And I navigate back from dialog page
    Then I see PlayPause media content button for conversation <Contact1>
    When I tap on contact name <Contact2>
    And I see dialog page
    And I press back button
    Then I see PlayPause media content button for conversation <Contact1>
    When I remember the state of PlayPause button next to the <Contact1> conversation
    And I tap PlayPause button next to the <Contact1> conversation
    Then I see the state of PlayPause button next to the <Contact1> conversation is changed

    Examples:
      | Name      | Contact1  | Contact2  | SoundCloudLink                                             |
      | user1Name | user2Name | user3Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @id1513 @regression @rc
  Scenario Outline: Verify messages are marked as read as you look at them so that you can know when there is unread content in a conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with contacts
    And I tap on contact name <Contact1>
    And I see dialog page
    And I scroll to the bottom of conversation view
    And I navigate back from dialog page
    And I remember unread messages indicator state for conversation <Contact1>
    When Contact <Contact1> sends 2 messages to user <Name>
    Then I see unread messages indicator state is changed for conversation <Contact1>
    When I remember unread messages indicator state for conversation <Contact1>
    And Contact <Contact1> sends 8 messages to user <Name>
    Then I see unread messages indicator state is changed for conversation <Contact1>
    When I remember unread messages indicator state for conversation <Contact1>
    And I tap on contact name <Contact1>
    And I see dialog page
    And I scroll to the bottom of conversation view
    And I navigate back from dialog page
    Then I see unread messages indicator state is changed for conversation <Contact1>

    Examples: 
      | Name      | Contact1  |
      | user1Name | user2Name |

  @id4042 @regression
  Scenario Outline: (AN-2969) Verify I can delete a 1:1 conversation from conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given All contacts send me a message <SpotifyLink>
    Given Contact <Contact1> sends image <Image> to single user conversation <Name>
    Given All contacts send me a message "<Message>"
    Given I sign in using my email or phone number
    And I see Contact list with contacts
    And I tap on contact name <Contact1>
    And I see dialog page
    And I scroll to the bottom of conversation view
    And Last message is "<Message>"
    And I navigate back from dialog page
    And I swipe right on a <Contact1>
    And I select DELETE from conversation settings menu
    And I press DELETE on the confirm alert
    Then I see Contact list with no contacts
    And I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact1>
    And I click on open conversation action button on People picker page
    And I see dialog page
    Then I see Connect to <Contact1> Dialog page

    Examples:
      | Name      | Contact1  | Message    | Image       | SpotifyLink                                           |
      | user1Name | user2Name | Tschuessii | testing.jpg | https://open.spotify.com/track/0p6GeAWS4VCZddxNbBtEss |

  @id4043 @staging
  Scenario Outline: Verify I can delete a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Contact <Contact1> sends image <Image> to group conversation <GroupChatName>
    Given User <Contact1> sent message <SpotifyLink> to conversation <GroupChatName>
    Given User <Contact1> sent message "<Message>" to conversation <GroupChatName>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I scroll to the bottom of conversation view
    And Last message is "<Message>"
    And I navigate back from dialog page
    And I swipe right on a <GroupChatName>
    And I select DELETE from conversation settings menu
    And I press DELETE on the confirm alert
    Then I do not see contact list with name <GroupChatName>
    And I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<GroupChatName>" into Search input on People Picker page
    And I tap on group found on People picker page <GroupChatName>
    And I see dialog page
    Then I see group chat page with users <Contact1>,<Contact2>

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName | Message    | Image       | SpotifyLink                                           |
      | user1Name | user2Name | user3Name | DELETE        | Tschuessii | testing.jpg | https://open.spotify.com/track/0p6GeAWS4VCZddxNbBtEss |
