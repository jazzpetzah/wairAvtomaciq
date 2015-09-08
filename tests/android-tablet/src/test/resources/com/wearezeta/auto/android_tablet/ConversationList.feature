Feature: Conversation List

  @id2246 @smoke @rc
  Scenario Outline: Mute and unmute conversation in landscape mode
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
    Then I see <ItemNotify> menu item on Single user popover
    And I tap Show Details button on conversation view page
    And I do not see the Single user popover
    And I see the conversation <Contact> in my conversations list is silenced
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    And I tap Options button on Single user popover
    When I select <ItemNotify> menu item on Single user popover
    Then I see <ItemSilence> menu item on Single user popover
    And I tap Show Details button on conversation view page
    And I do not see the Single user popover
    And I see the conversation <Contact> in my conversations list is not silenced

    Examples: 
      | Name      | Contact   | ItemSilence | ItemNotify |
      | user1Name | user2Name | SILENCE     | NOTIFY     |

  @id2260 @smoke @rc
  Scenario Outline: Mute and unmute conversation in portrait mode
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
    Then I see <ItemNotify> menu item on Single user popover
    And I tap Show Details button on conversation view page
    And I do not see the Single user popover
    And I see the conversation <Contact> in my conversations list is silenced
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    And I tap Options button on Single user popover
    When I select <ItemNotify> menu item on Single user popover
    Then I see <ItemSilence> menu item on Single user popover
    And I tap Show Details button on conversation view page
    And I do not see the Single user popover
    And I see the conversation <Contact> in my conversations list is not silenced

    Examples: 
      | Name      | Contact   | ItemSilence | ItemNotify |
      | user1Name | user2Name | SILENCE     | NOTIFY     |

  @id2881 @staging
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

  @id3140 @staging
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
