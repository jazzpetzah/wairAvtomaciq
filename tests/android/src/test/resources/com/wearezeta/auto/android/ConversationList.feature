Feature: Conversation List

  @C719 @id1513 @regression @rc
  Scenario Outline: Verify messages are marked as read as you look at them so that you can know when there is unread content in a conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
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

  @C822 @id4042 @regression @rc
  Scenario Outline: (AN-2969) Verify I can delete a 1:1 conversation from conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given All contacts send me a message <SpotifyLink>
    Given Contact <Contact1> sends image <Image> to single user conversation <Name>
    Given All contacts send me a message "<Message>"
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
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

  @C444 @id4043 @regression
  Scenario Outline: (AN-2875) Verify I can delete a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Contact <Contact1> sends image <Image> to group conversation <GroupChatName>
    Given User <Contact1> sent message <SpotifyLink> to conversation <GroupChatName>
    Given User <Contact1> sent message "<Message>" to conversation <GroupChatName>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
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

  @C445 @id4053 @regression
  Scenario Outline: Verify I can delete and leave a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
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

  @C446 @id4056 @regression
  Scenario Outline: Verify I see picture, ping and call after I delete a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
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

  @C451 @id4072 @regression
  Scenario Outline: I can mute 1:1 conversation from the conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I swipe right on a <Contact1>
    And I select SILENCE from conversation settings menu
    Then Contact <Contact1> is muted

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C452 @id4073 @regression
  Scenario Outline: I can unmute 1:1 conversation from the conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is silenced to user <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And Contact <Contact1> is muted
    And I swipe right on a <Contact1>
    And I select NOTIFY from conversation settings menu
    Then Contact <Contact1> is not muted

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C453 @id4078 @regression
  Scenario Outline: I can mute group conversation from the conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I swipe right on a <GroupChatName>
    And I select SILENCE from conversation settings menu
    Then Contact <GroupChatName> is muted

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | MUTE          |

  @C454 @id4079 @regression
  Scenario Outline: I can unmute group conversation from the conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Group <GroupChatName> gets silenced for user <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And Contact <GroupChatName> is muted
    And I swipe right on a <GroupChatName>
    And I select NOTIFY from conversation settings menu
    Then Contact <GroupChatName> is not muted

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | UNMUTE        |

  @C455 @id4088 @regression
  Scenario Outline: Verify options menu for outgoing connection request
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I swipe right on a <Contact>
    Then I see ARCHIVE button in conversation settings menu
    And I see BLOCK button in conversation settings menu

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C456 @id4089 @regression
  Scenario Outline: Verify there is no options menu for incoming connection requests
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see contact list with name <WaitingMess1>
    When I swipe right on a <WaitingMess1>
    Then I see contact list with name <WaitingMess1>

    Examples:
      | Name      | Contact   | WaitingMess1     |
      | user1Name | user2Name | 1 person waiting |

  @C457 @id4090 @regression
  Scenario Outline: Verify that options menu from list is the same as opened from the other user profile
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I swipe right on a <Contact1>
    Then I see SILENCE button in conversation settings menu
    And I see ARCHIVE button in conversation settings menu
    And I see DELETE button in conversation settings menu
    And I see BLOCK button in conversation settings menu
    And I press back button
    When I tap on contact name <Contact1>
    And I see dialog page
    And I tap conversation details button
    When I press options menu button
    Then I see SILENCE button in option menu
    And I see ARCHIVE button in option menu
    And I see DELETE button in option menu
    And I see BLOCK button in option menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C458 @id4091 @regression
  Scenario Outline: Verify that options menu from list is the same as opened from the participants view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I swipe right on a <GroupChatName>
    Then I see SILENCE button in conversation settings menu
    And I see ARCHIVE button in conversation settings menu
    And I see DELETE button in conversation settings menu
    And I see LEAVE button in conversation settings menu
    And I press back button
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap conversation details button
    When I press options menu button
    Then I see SILENCE button in option menu
    And I see RENAME button in option menu
    And I see ARCHIVE button in option menu
    And I see DELETE button in option menu
    And I see LEAVE button in option menu

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | MenuItems     |

  @C565 @id4093 @regression
  Scenario Outline: (AN-2551) Check there is no leave checkbox when you delete conversation where you was dropped
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> removes <Name> from group <GroupChatName>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I swipe right on a <GroupChatName>
    And I select DELETE from conversation settings menu
    Then I do not see the Leave check box

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | NoLeaveBox    |

  @C564 @id4092 @regression
  Scenario Outline: I can open options menu by tap on three dots button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I short swipe right on a <Contact1>
    And I see three dots option menu button
    And I press the three dots option menu button
    Then I see SILENCE button in conversation settings menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |