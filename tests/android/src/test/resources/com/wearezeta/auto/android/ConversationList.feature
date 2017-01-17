Feature: Conversations list

  @C56396 @regression
  Scenario Outline: (AN-4605) Verify contacts banner does not appear anymore after you opened contacts once
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    When I accept First Time overlay as soon as it is visible
    Then I see contact hint banner
    When I open Search UI
    And I tap Clear button
    Then I do not see contact hint banner
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Log out" settings menu item
    And I confirm sign out
    And I sign in using my email
    #AN-4605 Workaround: I should not see First time overlay
    Then I accept First Time overlay as soon as it is visible
    And I see Conversations list
    And I do not see contact hint banner

    Examples:
      | Name      |
      | user1Name |

  @C719 @regression @rc
  Scenario Outline: (AN-4024) Verify messages are marked as read as you look at them so that you can know when there is unread content in a conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I tap on conversation name <Contact1>
    And I scroll to the bottom of conversation view
    And I navigate back from conversation
    And I remember unread messages indicator state for conversation <Contact1>
    When User <Contact1> sends 2 encrypted messages to user Myself
    Then I see unread messages indicator state is changed for conversation <Contact1>
    When I remember unread messages indicator state for conversation <Contact1>
    And User <Contact1> sends 8 encrypted messages to user Myself
    Then I see unread messages indicator state is changed for conversation <Contact1>
    When I remember unread messages indicator state for conversation <Contact1>
    And I tap on conversation name <Contact1>
    And I scroll to the bottom of conversation view
    And I navigate back from conversation
    Then I see unread messages indicator state is changed for conversation <Contact1>

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C822 @regression @rc
  Scenario Outline: CM-998 Verify I can delete a 1:1 conversation from Conversations list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    # Workaround for AN-4115, need to open conversation view before receiving message
    Given I tap on conversation name <Contact1>
    Given User <Contact1> sends encrypted image <Image> to single user conversation Myself
    Given User <Contact1> sends encrypted message <Message> to user Myself
    # Wait for all messages are syned
    Given I wait for 10 seconds
    Given I scroll to the bottom of conversation view
    Given I see the most recent conversation message is "<Message>"
    Given I navigate back from conversation
    When I swipe right on a <Contact1>
    And I tap DELETE button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I see Conversations list with no conversations
    When I wait until <Contact1> exists in backend search results
    And I open Search UI
    And I type user name "<Contact1>" in search field
    And I tap on user name found on Search page <Contact1>
    And I tap Open Conversation action button on Search page
    Then I see conversation view

    Examples:
      | Name      | Contact1  | Message    | Image       |
      | user1Name | user2Name | Tschuessii | testing.jpg |

  @C95626 @regression
  Scenario Outline: Verify deleting a conversation is synchronised to all devices
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I see Conversations list with name <Contact>
    When User Myself adds new device <DeviceName>
    And User <Contact> sends encrypted message blabla to user Myself
    And User Myself deletes single user conversation <Contact> using device <DeviceName>
    Then I wait up to <Timeout> seconds until conversation <Contact> disappears from the list

    Examples:
      | Name      | Contact   | DeviceName | Timeout |
      | user1Name | user2Name | device1    | 15      |

  @C444 @regression
  Scenario Outline: Verify I can delete a group conversation from Conversations list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given User <Contact1> sends encrypted message <SpotifyLink> to group conversation <GroupChatName>
    Given User <Contact1> sends encrypted image <Image> to group conversation Myself
    Given User <Contact1> sends encrypted message <Message> to group conversation <GroupChatName>
    Given I tap on conversation name <GroupChatName>
    # Wait for sync
    Given I wait for 10 seconds
    When I scroll to the bottom of conversation view
    And I see the most recent conversation message is "<Message>"
    And I navigate back from conversation
    And I swipe right on a <GroupChatName>
    And I tap DELETE button on Group conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see Conversations list with name <GroupChatName>
    And I open Search UI
    And I type group name "<GroupChatName>" in search field
    And I see group <GroupChatName> in Search result list
    When I tap Clear button
    And User <Contact1> sends encrypted message <Message> to group conversation <GroupChatName>
    Then I see Conversations list with name <GroupChatName>
    When I tap on conversation name <GroupChatName>
    Then I see the most recent conversation message is "<Message>"

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message    | Image       | SpotifyLink                                           |
      | user1Name | user2Name | user3Name | DELETE        | Tschuessii | testing.jpg | https://open.spotify.com/track/0p6GeAWS4VCZddxNbBtEss |

  @C445 @regression
  Scenario Outline: (BUG AN-3438) Verify I can delete and leave a group conversation from Conversations list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I swipe right on a <GroupChatName>
    And I tap DELETE button on Group conversation options menu
    And I tap on leave checkbox on Confirm overlay page
    And I tap DELETE button on Confirm overlay page
    Then I do not see Conversations list with name <GroupChatName>
    And I open Search UI
    And I type group name "<GroupChatName>" in search field
    Then I do not see group <GroupChatName> in Search result list
    And I tap Clear button
    And User <Contact1> sends encrypted message <Message> to group conversation <GroupChatName>
    Then I do not see Conversations list with name <GroupChatName>
    And I swipe up Conversations list
    Then I do not see Conversations list with name <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message |
      | user1Name | user2Name | user3Name | DELETELeave   | tututu  |

  @C446 @regression
  Scenario Outline: Verify I see picture, ping and call after I delete a group conversation from Conversations list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I swipe right on a <GroupChatName>
    And I tap DELETE button on Group conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see Conversations list with name <GroupChatName>
    When User <Contact1> sends encrypted image <Image> to group conversation <GroupChatName>
    # Wait for SE sync
    And I wait for 5 seconds
    Then I see Conversations list with name <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I tap DELETE button on Group conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see Conversations list with name <GroupChatName>
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see Conversations list with name <GroupChatName>
    When I swipe right on a <GroupChatName>
    And I tap DELETE button on Group conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see Conversations list with name <GroupChatName>
    When <Contact1> calls <GroupChatName>
    And I see incoming call
    And <Contact1> stops calling <GroupChatName>
    Then I see Conversations list with name <GroupChatName>

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       | CallBackend |
      | user1Name | user2Name | user3Name | DELETE        | testing.jpg | zcall       |

  @C451 @regression
  Scenario Outline: I can mute 1:1 conversation from the Conversations list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I swipe right on a <Contact1>
    And I tap MUTE button on Single conversation options menu
    Then Conversation <Contact1> is muted

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C452 @regression
  Scenario Outline: I can unmute 1:1 conversation from the Conversations list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given <Contact1> is silenced to user <Name>
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given Conversation <Contact1> is muted
    When I swipe right on a <Contact1>
    And I tap UNMUTE button on Single conversation options menu
    Then Conversation <Contact1> is not muted

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C453 @regression
  Scenario Outline: I can mute group conversation from the Conversations list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And I swipe right on a <GroupChatName>
    And I tap MUTE button on Single conversation options menu
    Then Conversation <GroupChatName> is muted

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | MUTE          |

  @C454 @regression
  Scenario Outline: I can unmute group conversation from the Conversations list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given Group <GroupChatName> gets silenced for user <Name>
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    And Conversation <GroupChatName> is muted
    And I swipe right on a <GroupChatName>
    And I tap UNMUTE button on Group conversation options menu
    Then Conversation <GroupChatName> is not muted

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | UNMUTE        |

  @C455 @regression
  Scenario Outline: Verify options menu for outgoing connection request
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I swipe right on a <Contact>
    Then I see ARCHIVE button on Single conversation options menu
    And I see BLOCK button on Single conversation options menu

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C456 @regression
  Scenario Outline: Verify there is no options menu for incoming connection requests
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to <Name>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with name <WaitingMess1>
    When I swipe right on a <WaitingMess1>
    Then I see Conversations list with name <WaitingMess1>

    Examples:
      | Name      | Contact   | WaitingMess1     |
      | user1Name | user2Name | 1 person waiting |

  @C457 @regression
  Scenario Outline: Verify that options menu from list is the same as opened from the other user profile
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I swipe right on a <Contact1>
    Then I see MUTE button on Single conversation options menu
    And I see ARCHIVE button on Single conversation options menu
    And I see DELETE button on Single conversation options menu
    And I see BLOCK button on Single conversation options menu
    And I tap back button
    When I tap on conversation name <Contact1>
    And I tap conversation name from top toolbar
    And I tap open menu button on Single connected user details page
    Then I see MUTE button on Single conversation options menu
    And I see ARCHIVE button on Single conversation options menu
    And I see DELETE button on Single conversation options menu
    And I see BLOCK button on Single conversation options menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C458 @regression
  Scenario Outline: Verify that options menu from list is the same as opened from the participants view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I swipe right on a <GroupChatName>
    Then I see MUTE button on Group conversation options menu
    And I see ARCHIVE button on Group conversation options menu
    And I see DELETE button on Group conversation options menu
    And I see LEAVE button on Group conversation options menu
    And I tap back button
    When I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap open menu button on Group info page
    Then I see MUTE button on Group conversation options menu
    And I see RENAME button on Group conversation options menu
    And I see ARCHIVE button on Group conversation options menu
    And I see DELETE button on Group conversation options menu
    And I see LEAVE button on Group conversation options menu

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | MenuItems     |

  @C565 @regression
  Scenario Outline: (AN-2551) Check there is no leave checkbox when you delete conversation where you was dropped
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> removes <Name> from group <GroupChatName>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I swipe right on a <GroupChatName>
    And I tap DELETE button on Group conversation options menu
    Then I do not see leave checkbox on Confirm overlay page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | NoLeaveBox    |

  @C564 @regression
  Scenario Outline: I can open options menu by tap on three dots button
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I short swipe right on a <Contact1>
    And I see three dots option menu button
    And I tap the three dots option menu button
    Then I see MUTE button on Single conversation options menu

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |
