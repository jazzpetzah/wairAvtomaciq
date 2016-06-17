Feature: Lock/Unlock

  @C472 @id2181 @regression
  Scenario Outline: (AN-2898) UI saves its state after device lock/unlock (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact1> sends encrypted message <MessageGroup> to group conversation <GroupChatName>
    Given User <Contact1> sends encrypted message <Message1to1> to user Myself
    When I lock the device
    And I unlock the device
    Then I see the conversation <Contact1> in my conversations list
    When I open Search UI
    When I lock the device
    And I unlock the device
    Then I see People Picker page
    When I close People Picker
    And I tap conversations list settings button
    And I see settings page
    And I lock the device
    And I unlock the device
    Then I see settings page
    When I navigate back
    And I tap in the center of Self Profile page
    And I tap Change Photo button on Take Picture view
    And I tap Take Photo button on Take Picture view
    And I lock the device
    And I unlock the device
    Then I see Take Photo button on Take Picture view
    When I navigate back
    And I navigate back
    And I tap the conversation <GroupChatName>
    And I lock the device
    And I unlock the device
    Then I see the message "<MessageGroup>" in the conversation view
    When I tap conversation name from top toolbar
    And I see the Group popover
    And I lock the device
    And I unlock the device
    Then I see the Group popover
    And I tap Close button on Group popover
    When I swipe right to show the conversations list
    And I tap the conversation <Contact1>
    And I lock the device
    And I unlock the device
    Then I see the message "<Message1to1>" in the conversation view
    When I tap conversation name from top toolbar
    And I see the Single user popover
    And I lock the device
    And I unlock the device
    Then I see the Single user popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Message1to1 | MessageGroup | ItemName |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Msg1to1     | MsgGroup     | ABOUT    |