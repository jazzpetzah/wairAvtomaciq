Feature: Lock/Unlock

  @C472 @id2181 @regression
  Scenario Outline: (AN-2898) UI saves its state after device lock/unlock (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Contact <Contact1> sends message "<MessageGroup>" to conversation <GroupChatName>
    Given Contact <Contact1> sends message "<Message1to1>" to user Myself
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I lock the device
    And I unlock the device
    Then I see the conversation <Contact1> in my conversations list
    When I tap the Search input
    And I see People Picker page
    When I lock the device
    And I unlock the device
    Then I see People Picker page
    When I close People Picker
    And I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    And I lock the device
    And I unlock the device
    Then I see my name on Self Profile page
    When I tap Options button on Self Profile page
    And I see "<ItemName>" menu item on Self Profile page
    And I lock the device
    And I unlock the device
    Then I see "<ItemName>" menu item on Self Profile page
    When I navigate back
    And I tap in the center of Self Profile page
    And I tap Change Picture button on Self Profile page
    And I see Take Photo button on Self Profile page
    And I lock the device
    And I unlock the device
    Then I see Take Photo button on Self Profile page
    When I navigate back
    And I navigate back
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I lock the device
    And I unlock the device
    Then I see the message "<MessageGroup>" in the conversation view
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    And I lock the device
    And I unlock the device
    Then I see the Group popover
    And I tap Close button on Group popover
    When I swipe right to show the conversations list
    And I tap the conversation <Contact1>
    And I see the conversation view
    And I lock the device
    And I unlock the device
    Then I see the message "<Message1to1>" in the conversation view
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    And I lock the device
    And I unlock the device
    Then I see the Single user popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Message1to1 | MessageGroup | ItemName |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Msg1to1     | MsgGroup     | ABOUT    |