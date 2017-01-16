Feature: Conversation List

  @C731 @regression @rc @rc44
  Scenario Outline: Mute and unmute conversation from conversation details in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    And I tap conversation name from top toolbar
    And I see Single connected user details popover
    And I tap open menu button on Single connected user details popover
    When I tap <ItemSilence> button on Single conversation options menu
    And I tap open menu button on Single connected user details popover
    Then I see <ItemNotify> button on Single conversation options menu
    And I tap outside of Single connected user details popover
    And I do not see Single connected user details popover
    Then I see the conversation <Contact> in my conversations list is silenced
    And I tap conversation name from top toolbar
    Then I see Single connected user details popover
    When I tap open menu button on Single connected user details popover
    And I tap <ItemNotify> button on Single conversation options menu
    And I tap open menu button on Single connected user details popover
    Then I see <ItemSilence> button on Single conversation options menu
    When I tap outside of Single connected user details popover
    Then I do not see Single connected user details popover
    Then I see the conversation <Contact> in my conversations list is not silenced

    Examples:
      | Name      | Contact   | ItemSilence | ItemNotify |
      | user1Name | user2Name | MUTE        | UNMUTE     |

  @C741 @regression @rc
  Scenario Outline: Mute and unmute conversation from conversation details in portrait mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    And I tap conversation name from top toolbar
    And I see Single connected user details popover
    And I tap open menu button on Single connected user details popover
    When I tap <ItemSilence> button on Single conversation options menu
    And I tap open menu button on Single connected user details popover
    Then I see <ItemNotify> button on Single conversation options menu
    And I tap outside of Single connected user details popover
    And I do not see Single connected user details popover
    When I swipe right to show the conversations list
    When I tap the conversation <Contact>
    And I tap conversation name from top toolbar
    Then I see Single connected user details popover
    When I tap open menu button on Single connected user details popover
    And I tap <ItemNotify> button on Single conversation options menu
    And I tap open menu button on Single connected user details popover
    Then I see <ItemSilence> button on Single conversation options menu
    When I tap outside of Single connected user details popover
    Then I do not see Single connected user details popover
    When I swipe right to show the conversations list
    Then I see the conversation <Contact> in my conversations list is not silenced

    Examples:
      | Name      | Contact   | ItemSilence | ItemNotify |
      | user1Name | user2Name | MUTE        | UNMUTE     |

  @C503 @regression
  Scenario Outline: Mute and unmute conversation from conversations list in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <ItemSilence> button on Single conversation options menu
    Then I do not see Single conversation options menu
    And I see the conversation <Contact1> in my conversations list is silenced
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <ItemNotify> button on Single conversation options menu
    Then I do not see Single conversation options menu
    And I see the conversation <Contact1> in my conversations list is not silenced

    Examples:
      | Name      | Contact1  | Contact2  | ItemSilence | ItemNotify |
      | user1Name | user2Name | user3Name | MUTE        | UNMUTE     |

  @C527 @regression
  Scenario Outline: Mute and unmute conversation from conversations list in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <ItemSilence> button on Single conversation options menu
    Then I do not see Single conversation options menu
    And I see the conversation <Contact1> in my conversations list is silenced
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <ItemNotify> button on Single conversation options menu
    Then I do not see Single conversation options menu
    And I see the conversation <Contact1> in my conversations list is not silenced

    Examples:
      | Name      | Contact1  | Contact2  | ItemSilence | ItemNotify |
      | user1Name | user2Name | user3Name | MUTE        | UNMUTE     |

  @C771 @soundcloud
  Scenario Outline: (AN-4030) Verify play/pause controls are visible in the list if there is active media item in other conversation (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    And I tap on text input
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    And I swipe right to show the conversations list
    Then I see Play button next to the conversation name <Contact>

    Examples:
      | Name      | Contact   | SoundCloudLink                                                      |
      | user1Name | user2Name | https://soundcloud.com/scottisbell/scott-isbell-tonight-feat-adessi |

  @C652 @regression
  Scenario Outline: (AN-3300) Verify play/pause controls are visible in the list if there is active media item in other conversation (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    And I tap on text input
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    Then I see Play button next to the conversation name <Contact>

    Examples:
      | Name      | Contact   | SoundCloudLink                                                      |
      | user1Name | user2Name | https://soundcloud.com/scottisbell/scott-isbell-tonight-feat-adessi |

  @C823 @regression @rc @rc44
  Scenario Outline: Verify I can delete a 1:1 conversation from conversation list (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And User <Contact1> sends encrypted message <Msg1> to user Myself
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see Single conversation options menu
    And I do not see conversation <Contact1> in my conversations list
    And I wait until <Contact1> exists in backend search results
    When I open Search UI
    And I enter "<Contact1>" into Search input on Search page
    Then I see "<Contact1>" avatar in Search result list
    And I close Search
    When User <Contact1> sends encrypted message <Msg2> to user Myself
    When I tap the conversation <Contact1>
    And I see the message "<Msg2>" in the conversation view
    And I do not see the message "<Msg1>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | DeleteItem | Msg1       | Msg2       |
      | user1Name | user2Name | user3Name | DELETE     | YoMessage1 | YoMessage2 |

  @C549 @regression
  Scenario Outline: Verify I can delete a group conversation from conversation list (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And User <Contact1> sends encrypted message <Msg1> to group conversation <GroupChatName>
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see Single conversation options menu
    And I do not see conversation <GroupChatName> in my conversations list
    When I open Search UI
    And I enter "<GroupChatName>" into Search input on Search page
    Then I see "<GroupChatName>" group avatar in Search result list
    And I close Search
    And User <Contact1> sends encrypted message <Msg2> to group conversation <GroupChatName>
    When I tap the conversation <GroupChatName>
    And I see the message "<Msg2>" in the conversation view
    And I do not see the message "<Msg1>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Msg1       | Msg2       | DeleteItem |
      | user1Name | user2Name | user3Name | GroupChat     | YoMessage1 | YoMessage2 | DELETE     |

  @C548 @regression
  Scenario Outline: Verify I can delete a group conversation from conversation list (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And User <Contact1> sends encrypted message <Msg1> to group conversation <GroupChatName>
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    And I swipe right to show the conversations list
    Then I do not see Single conversation options menu
    And I do not see conversation <GroupChatName> in my conversations list
    When I open Search UI
    And I enter "<GroupChatName>" into Search input on Search page
    Then I see "<GroupChatName>" group avatar in Search result list
    And I close Search
    And User <Contact1> sends encrypted message <Msg2> to group conversation <GroupChatName>
    When I tap the conversation <GroupChatName>
    And I see the message "<Msg2>" in the conversation view
    And I do not see the message "<Msg1>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Msg1       | Msg2       | DeleteItem |
      | user1Name | user2Name | user3Name | GroupChat     | YoMessage1 | YoMessage2 | DELETE     |

  @C550 @regression
  Scenario Outline: (BUG AN-3438) Verify I can delete and leave a group conversation from conversation list (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    And I see the conversations list with conversations
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap on leave checkbox on Confirm overlay page
    And I tap DELETE button on Confirm overlay page
    Then I do not see Single conversation options menu
    And I do not see conversation <GroupChatName> in my conversations list
    When I open Search UI
    And I enter "<GroupChatName>" into Search input on Search page
    Then I do not see "<GroupChatName>" group avatar in Search result list
    And I close Search
    And User <Contact1> sends encrypted message <Message> to group conversation <GroupChatName>
    Then I do not see conversation <GroupChatName> in my conversations list
    When I do long swipe up on conversations list
    Then I do not see conversation <GroupChatName> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message | DeleteItem |
      | user1Name | user2Name | user3Name | DELETELeave   | huhuhu  | DELETE     |

  @C551 @regression
  Scenario Outline: (BUG AN-3438) Verify I can delete and leave a group conversation from conversation list (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    And I see the conversations list with conversations
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap on leave checkbox on Confirm overlay page
    And I tap DELETE button on Confirm overlay page
    And I swipe right to show the conversations list
    Then I do not see Single conversation options menu
    And I do not see conversation <GroupChatName> in my conversations list
    When I open Search UI
    And I enter "<GroupChatName>" into Search input on Search page
    Then I do not see "<GroupChatName>" group avatar in Search result list
    And I close Search
    And User <Contact1> sends encrypted message <Message> to group conversation <GroupChatName>
    Then I do not see conversation <GroupChatName> in my conversations list
    When I do long swipe up on conversations list
    Then I do not see conversation <GroupChatName> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message | DeleteItem |
      | user1Name | user2Name | user3Name | DELETELeave   | huhuhu  | DELETE     |

  @C552 @regression
  Scenario Outline: Verify I see picture, ping and call after I delete a group conversation from conversation list (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    And I see the conversations list with conversations
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    And I swipe right to show the conversations list
    Then I do not see Single conversation options menu
    And I do not see conversation <GroupChatName> in my conversations list
    When I open Search UI
    And I enter "<GroupChatName>" into Search input on Search page
    Then I see "<GroupChatName>" group avatar in Search result list
    And I close Search
    And User <Contact1> sends encrypted image <Image> to group conversation <GroupChatName>
    Then I see conversation <GroupChatName> in my conversations list
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see conversation <GroupChatName> in my conversations list
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see conversation <GroupChatName> in my conversations list
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see conversation <GroupChatName> in my conversations list
    When <Contact1> calls <GroupChatName>
    Then I see conversation <GroupChatName> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       | CallBackend | DeleteItem |
      | user1Name | user2Name | user3Name | DELETE        | testing.jpg | zcall       | DELETE     |

  @C553 @regression
  Scenario Outline: Verify I see picture, ping and call after I delete a group conversation from conversation list (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    And I see the conversations list with conversations
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see Single conversation options menu
    And I do not see conversation <GroupChatName> in my conversations list
    When I open Search UI
    And I enter "<GroupChatName>" into Search input on Search page
    Then I see "<GroupChatName>" group avatar in Search result list
    And I close Search
    And User <Contact1> sends encrypted image <Image> to group conversation <GroupChatName>
    Then I see conversation <GroupChatName> in my conversations list
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see conversation <GroupChatName> in my conversations list
    When User <Contact1> securely pings conversation <GroupChatName>
    Then I see conversation <GroupChatName> in my conversations list
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <DeleteItem> button on Single conversation options menu
    And I tap DELETE button on Confirm overlay page
    Then I do not see conversation <GroupChatName> in my conversations list
    When <Contact1> calls <GroupChatName>
    Then I see conversation <GroupChatName> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Image       | CallBackend | DeleteItem |
      | user1Name | user2Name | user3Name | DELETE        | testing.jpg | zcall       | DELETE     |

  @C562 @regression
  Scenario Outline: I can mute 1:1 conversation from the conversation list (portrait)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <SilenceItem> button on Single conversation options menu
    Then I see the conversation <Contact1> in my conversations list is silenced

    Examples:
      | Name      | Contact1  | SilenceItem |
      | user1Name | user2Name | MUTE        |

  @C563 @regression
  Scenario Outline: I can mute 1:1 conversation from the conversation list (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <SilenceItem> button on Single conversation options menu
    Then I see the conversation <Contact1> in my conversations list is silenced

    Examples:
      | Name      | Contact1  | SilenceItem |
      | user1Name | user2Name | MUTE        |

  @C560 @regression
  Scenario Outline: I can unmute 1:1 conversation from the conversation list (portrait)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is silenced to user <Name>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list is silenced
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <NotifyItem> button on Single conversation options menu
    Then I see the conversation <Contact1> in my conversations list is not silenced

    Examples:
      | Name      | Contact1  | NotifyItem |
      | user1Name | user2Name | UNMUTE     |

  @C561 @regression
  Scenario Outline: I can unmute 1:1 conversation from the conversation list (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is silenced to user <Name>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list is silenced
    When I open options menu of <Contact1> on conversation list page
    Then I see Single conversation options menu
    When I tap <NotifyItem> button on Single conversation options menu
    Then I see the conversation <Contact1> in my conversations list is not silenced

    Examples:
      | Name      | Contact1  | NotifyItem |
      | user1Name | user2Name | UNMUTE     |

  @C558 @regression
  Scenario Outline: I can mute group conversation from the conversation list (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <SilenceItem> button on Single conversation options menu
    Then I see the conversation <GroupChatName> in my conversations list is silenced

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | SilenceItem |
      | user1Name | user2Name | user3Name | MUTE          | MUTE        |

  @C559 @regression
  Scenario Outline: I can mute group conversation from the conversation list (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <SilenceItem> button on Single conversation options menu
    Then I see the conversation <GroupChatName> in my conversations list is silenced

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | SilenceItem |
      | user1Name | user2Name | user3Name | MUTE          | MUTE        |

  @C556 @regression
  Scenario Outline: I can unmute group conversation from the conversation list (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Group <GroupChatName> gets silenced for user <Name>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list is silenced
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <NotifyItem> button on Single conversation options menu
    Then I see the conversation <GroupChatName> in my conversations list is not silenced

    Examples:
      | Name      | Contact1  | Contact2  | NotifyItem | GroupChatName |
      | user1Name | user2Name | user3Name | UNMUTE     | UNMUTE        |

  @C557 @regression
  Scenario Outline: I can unmute group conversation from the conversation list (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Group <GroupChatName> gets silenced for user <Name>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list is silenced
    When I open options menu of <GroupChatName> on conversation list page
    Then I see Single conversation options menu
    When I tap <NotifyItem> button on Single conversation options menu
    Then I see the conversation <GroupChatName> in my conversations list is not silenced

    Examples:
      | Name      | Contact1  | Contact2  | NotifyItem | GroupChatName |
      | user1Name | user2Name | user3Name | UNMUTE     | UNMUTE        |



