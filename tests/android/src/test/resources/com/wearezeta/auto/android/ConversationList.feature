Feature: Conversation List

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
    And I press back button
    Then I see PlayPause media content button for conversation <Contact1>

    Examples:
      | Name      | Contact1  | SoudCloudLink                                              |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

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

  @id4043 @regression
  Scenario Outline: (AN-2875) Verify I can delete a group conversation from conversation list
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

  @id4053 @regression
  Scenario Outline: Verify I can delete and leave a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I swipe right on a <GroupChatName>
    And I select DELETE from conversation settings menu
    And I click the Leave check box
    And I press DELETE on the confirm alert
    Then I do not see contact list with name <GroupChatName>
    And I open search by tap
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<GroupChatName>" into Search input on People Picker page
    Then I do not see group <GroupChatName> in People picker
    And I navigate back to Conversations List
    And User <Contact1> sent message <Message> to conversation <GroupChatName>
    Then I do not see contact list with name <GroupChatName>
    And I swipe up contact list
    Then I do not see contact list with name <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message |
      | user1Name | user2Name | user3Name | DELETELeave   | tututu  |

  @id4056 @regression
  Scenario Outline: Verify I see picture, ping and call after I delete a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    When I swipe right on a <GroupChatName>
    And I select DELETE from conversation settings menu
    And I press DELETE on the confirm alert
    Then I do not see contact list with name <GroupChatName>
    When Contact <Contact1> sends image <Image> to group conversation <GroupChatName>
    Then I see contact list with name <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I select DELETE from conversation settings menu
    And I press DELETE on the confirm alert
    Then I do not see contact list with name <GroupChatName>
    When Contact <Contact1> ping conversation <GroupChatName>
    Then I see contact list with name <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I select DELETE from conversation settings menu
    And I press DELETE on the confirm alert
    Then I do not see contact list with name <GroupChatName>
    When <Contact1> calls <GroupChatName> using <CallBackend>
    Then I see contact list with name <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       | CallBackend |
      | user1Name | user2Name | user3Name | DELETE        | testing.jpg | autocall    |

  @id4072 @regression
  Scenario Outline: I can mute 1:1 conversation from the conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I swipe right on a <Contact1>
    And I select SILENCE from conversation settings menu
    Then Contact <Contact1> is muted

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @id4073 @regression
  Scenario Outline: I can unmute 1:1 conversation from the conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is silenced to user <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And Contact <Contact1> is muted
    And I swipe right on a <Contact1>
    And I select NOTIFY from conversation settings menu
    Then Contact <Contact1> is not muted

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @id4078 @regression
  Scenario Outline: I can mute group conversation from the conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I swipe right on a <GroupChatName>
    And I select SILENCE from conversation settings menu
    Then Contact <GroupChatName> is muted

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | MUTE          |

  @id4079 @regression
  Scenario Outline: I can unmute group conversation from the conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Group <GroupChatName> gets silenced for user <Name>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And Contact <GroupChatName> is muted
    And I swipe right on a <GroupChatName>
    And I select NOTIFY from conversation settings menu
    Then Contact <GroupChatName> is not muted

    Examples:
      | Name      | Contact1  |Contact2   | GroupChatName |
      | user1Name | user2Name | user3Name | UNMUTE        |

  @id4088 @staging
  Scenario Outline: Verify options menu for outgoing connection request
    Given There are 2 users where <Name> is me
    Given <Name> sent connection request to <Contact1>
    Given I sign in using my email or phone number
    And I see Contact list with contacts
    When I swipe right on a <Contact1>
    Then I see ARCHIVE button in conversation settings menu at position 1
    Then I see BLOCK button in conversation settings menu at position 2
    Then I see CANCEL button in conversation settings menu at position 3


    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

