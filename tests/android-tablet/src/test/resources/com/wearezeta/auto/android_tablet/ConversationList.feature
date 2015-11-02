Feature: Conversation List

  @id2246 @regression @rc
  Scenario Outline: Mute and unmute conversation from conversation details in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    And I tap Options button on Single user popover
    When I select <ItemSilence> menu item on Single user popover
    And I tap Options button on Single user popover
    Then I see <ItemNotify> menu item on Single user popover
    And I tap outside of Single user popover
    And I do not see the Single user popover
    Then I see the conversation <Contact> in my conversations list is silenced
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    When I tap Options button on Single user popover
    And I select <ItemNotify> menu item on Single user popover
    And I tap Options button on Single user popover
    Then I see <ItemSilence> menu item on Single user popover
    When I tap outside of Single user popover
    Then I do not see the Single user popover
    Then I see the conversation <Contact> in my conversations list is not silenced

    Examples:
      | Name      | Contact   | ItemSilence | ItemNotify |
      | user1Name | user2Name | SILENCE     | NOTIFY     |

  @id2260 @regression @rc
  Scenario Outline: Mute and unmute conversation from conversation details in portrait mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    And I tap Options button on Single user popover
    When I select <ItemSilence> menu item on Single user popover
    And I tap Options button on Single user popover
    Then I see <ItemNotify> menu item on Single user popover
    And I tap outside of Single user popover
    And I do not see the Single user popover
    When I swipe right to show the conversations list
    Then I see the conversation <Contact> in my conversations list is silenced
    When I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    When I tap Options button on Single user popover
    And I select <ItemNotify> menu item on Single user popover
    And I tap Options button on Single user popover
    Then I see <ItemSilence> menu item on Single user popover
    When I tap outside of Single user popover
    Then I do not see the Single user popover
    When I swipe right to show the conversations list
    Then I see the conversation <Contact> in my conversations list is not silenced

    Examples:
      | Name      | Contact   | ItemSilence | ItemNotify |
      | user1Name | user2Name | SILENCE     | NOTIFY     |

  @id2888 @regression
  Scenario Outline: Mute and unmute conversation from conversations list in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    When I swipe right the conversations list item <Contact1>
    Then I see Conversation Actions overlay
    When I select <ItemSilence> menu item on Conversation Actions overlay
    Then I do not see Conversation Actions overlay
    And I see the conversation <Contact1> in my conversations list is silenced
    When I swipe right the conversations list item <Contact1>
    Then I see Conversation Actions overlay
    When I select <ItemNotify> menu item on Conversation Actions overlay
    Then I do not see Conversation Actions overlay
    And I see the conversation <Contact1> in my conversations list is not silenced

    Examples:
      | Name      | Contact1  | Contact2  | ItemSilence | ItemNotify |
      | user1Name | user2Name | user3Name | SILENCE     | NOTIFY     |

  @id3137 @regression
  Scenario Outline: Mute and unmute conversation from conversations list in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    When I swipe right the conversations list item <Contact1>
    Then I see Conversation Actions overlay
    When I select <ItemSilence> menu item on Conversation Actions overlay
    Then I do not see Conversation Actions overlay
    And I see the conversation <Contact1> in my conversations list is silenced
    When I swipe right the conversations list item <Contact1>
    Then I see Conversation Actions overlay
    When I select <ItemNotify> menu item on Conversation Actions overlay
    Then I do not see Conversation Actions overlay
    And I see the conversation <Contact1> in my conversations list is not silenced

    Examples:
      | Name      | Contact1  | Contact2  | ItemSilence | ItemNotify |
      | user1Name | user2Name | user3Name | SILENCE     | NOTIFY     |

  @id2881 @regression @rc
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap the text input in the conversation view
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    And I swipe right to show the conversations list
    Then I see Play button next to the conversation name <Contact>

    Examples:
      | Name      | Contact   | SoundCloudLink                                             |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @id3140 @regression
  Scenario Outline: Verify play/pause controls are visible in the list if there is active media item in other conversation (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap the text input in the conversation view
    When I type the message "<SoundCloudLink>" in the conversation view
    And I send the typed message in the conversation view
    And I scroll to the bottom of the conversation view
    And I tap Play button in the conversation view
    Then I see Play button next to the conversation name <Contact>

    Examples:
      | Name      | Contact   | SoundCloudLink                                             |
      | user1Name | user2Name | https://soundcloud.com/juan_mj_10/led-zeppelin-rock-n-roll |

  @id4046 @regression @rc
  Scenario Outline: Verify I can delete a 1:1 conversation from conversation list (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And Contact <Contact1> sends message "<Msg1>" to user Myself
    When I swipe right the conversations list item <Contact1>
    Then I see Conversation Actions overlay
    When I select <DeleteItem> menu item on Conversation Actions overlay
    And I confirm conversation deletion on Conversation Actions overlay
    Then I do not see Conversation Actions overlay
    And I do not see conversation <Contact1> in my conversations list
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact1>" into Search input on People Picker page
    Then I see "<Contact1>" avatar on People Picker page
    And I close People Picker
    When Contact <Contact1> sends message "<Msg2>" to user Myself
    Then I see conversation <Contact1> in my conversations list
    When I tap the conversation <Contact1>
    Then I see the conversation view
    And I see the message "<Msg2>" in the conversation view
    And I do not see the message "<Msg1>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | DeleteItem | Msg1       | Msg2       |
      | user1Name | user2Name | user3Name | DELETE     | YoMessage1 | YoMessage2 |

  @id4045 @regression
  Scenario Outline: Verify I can delete a group conversation from conversation list (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And Contact <Contact1> sends message "<Msg1>" to conversation <GroupChatName>
    When I swipe right the conversations list item <GroupChatName>
    Then I see Conversation Actions overlay
    When I select <DeleteItem> menu item on Conversation Actions overlay
    And I confirm conversation deletion on Conversation Actions overlay
    Then I do not see Conversation Actions overlay
    And I do not see conversation <GroupChatName> in my conversations list
    When I tap Search input
    And I see People Picker page
    And I enter "<GroupChatName>" into Search input on People Picker page
    Then I see "<GroupChatName>" group avatar on People Picker page
    And I close People Picker
    And Contact <Contact1> sends message "<Msg2>" to conversation <GroupChatName>
    Then I see conversation <GroupChatName> in my conversations list
    When I tap the conversation <GroupChatName>
    Then I see the conversation view
    And I see the message "<Msg2>" in the conversation view
    And I do not see the message "<Msg1>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Msg1       | Msg2       | DeleteItem |
      | user1Name | user2Name | user3Name | GroupChat     | YoMessage1 | YoMessage2 | DELETE     |

  @id4044 @regression
  Scenario Outline: Verify I can delete a group conversation from conversation list (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And Contact <Contact1> sends message "<Msg1>" to conversation <GroupChatName>
    When I swipe right the conversations list item <GroupChatName>
    Then I see Conversation Actions overlay
    When I select <DeleteItem> menu item on Conversation Actions overlay
    And I confirm conversation deletion on Conversation Actions overlay
    And I swipe right to show the conversations list
    Then I do not see Conversation Actions overlay
    And I do not see conversation <GroupChatName> in my conversations list
    When I tap Search input
    And I see People Picker page
    And I enter "<GroupChatName>" into Search input on People Picker page
    Then I see "<GroupChatName>" group avatar on People Picker page
    And I close People Picker
    And Contact <Contact1> sends message "<Msg2>" to conversation <GroupChatName>
    Then I see conversation <GroupChatName> in my conversations list
    When I tap the conversation <GroupChatName>
    Then I see the conversation view
    And I see the message "<Msg2>" in the conversation view
    And I do not see the message "<Msg1>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Msg1       | Msg2       | DeleteItem |
      | user1Name | user2Name | user3Name | GroupChat     | YoMessage1 | YoMessage2 | DELETE     |
