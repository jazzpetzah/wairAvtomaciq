Feature: People View

  @id2257 @smoke
  Scenario Outline: Leave group conversation in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Details button on conversation view page
    And I see the Group popover
    And I tap Options button on Group popover
    When I select <ItemLeave> menu item on Group popover
    And I confirm leaving the group chat on Group popover
    Then I do not see the Group popover
    And I do not see the conversation <GroupChatName> in my conversations list

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName  | ItemLeave |
      | user1Name | user2Name | user3Name | LeaveGroupChat | LEAVE     |

  @id2258 @smoke
  Scenario Outline: Remove from group chat in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Details button on conversation view page
    And I see the Group popover
    And I see the participant avatar <Contact2> on Group popover
    And I tap the participant avatar <Contact2> on Group popover
    When I tap Remove button on Group popover
    And I confirm removal from the group chat on Group popover
    Then I do not see the participant avatar <Contact2> on Group popover
    And I tap Show Details button on conversation view page
    And I do not see the Group popover
    And I see the system message contains "<Action> <Contact2>" text on conversation view page

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName       | Action      |
      | user1Name | user2Name | user3Name | RemoveFromGroupChat | YOU REMOVED |

  @id2282 @smoke
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list
    And I tap the Search input
    And I see People Picker page
    And I keep on reopening People Picker until I see Top People
    And I tap <Contact1> avatar in Top People
    When I tap Create Conversation button
    Then I see the conversation view
    And I see the chat header message contains "<Contact1>" text on conversation view page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id2243 @smoke
  Scenario Outline: Leave group conversation in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    And I see the conversations list
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Details button on conversation view page
    And I see the Group popover
    And I tap Options button on Group popover
    When I select <ItemLeave> menu item on Group popover
    And I confirm leaving the group chat on Group popover
    Then I do not see the Group popover
    And I do not see the conversation <GroupChatName> in my conversations list

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName  | ItemLeave |
      | user1Name | user2Name | user3Name | LeaveGroupChat | LEAVE     |

  @id2244 @smoke
  Scenario Outline: Remove from group chat in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    And I see the conversations list
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Details button on conversation view page
    And I see the Group popover
    And I see the participant avatar <Contact2> on Group popover
    And I tap the participant avatar <Contact2> on Group popover
    When I tap Remove button on Group popover
    And I confirm removal from the group chat on Group popover
    Then I do not see the participant avatar <Contact2> on Group popover
    And I tap Show Details button on conversation view page
    And I do not see the Group popover
    And I see the system message contains "<Action> <Contact2>" text on conversation view page

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName       | Action      |
      | user1Name | user2Name | user3Name | RemoveFromGroupChat | YOU REMOVED |


  @id2283 @smoke
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    And I see the conversations list
    And I tap the Search input
    And I see People Picker page
    And I keep on reopening People Picker until I see Top People
    And I tap <Contact1> avatar in Top People
    When I tap Create Conversation button
    Then I see the conversation view
    And I see the chat header message contains "<Contact1>" text on conversation view page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |
