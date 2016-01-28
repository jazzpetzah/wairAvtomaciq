Feature: People View

  @C742 @id2261 @regression @rc
  Scenario Outline: Check contact personal info in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    And I see the user name <Contact> on Single user popover
    And I see the user email <ContactEmail> on Single user popover

    Examples:
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @C732 @id2247 @regression @rc @rc44
  Scenario Outline: Check contact personal info in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    And I see the user name <Contact> on Single user popover
    And I see the user email <ContactEmail> on Single user popover

    Examples:
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @C739 @id2257 @regression @rc
  Scenario Outline: Leave group conversation in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
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

  @C729 @id2243 @regression @rc @rc44
  Scenario Outline: Leave group conversation in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
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

  @C468 @id2258 @regression
  Scenario Outline: Remove from group chat in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    And I see the participant avatar <Contact2> on Group popover
    And I tap the participant avatar <Contact2> on Group popover
    When I tap Remove button on Group popover
    And I confirm removal from the group chat on Group popover
    Then I do not see the participant avatar <Contact2> on Group popover
    When I tap Close button on Group popover
    Then I do not see the Group popover
    And I see the system message contains "<Action> <Contact2>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName       | Action      |
      | user1Name | user2Name | user3Name | RemoveFromGroupChat | You removed |

  @C464 @id2244 @regression
  Scenario Outline: Remove from group chat in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    And I see the participant avatar <Contact2> on Group popover
    And I tap the participant avatar <Contact2> on Group popover
    When I tap Remove button on Group popover
    And I confirm removal from the group chat on Group popover
    Then I do not see the participant avatar <Contact2> on Group popover
    When I tap Close button on Group popover
    Then I do not see the Group popover
    And I see the system message contains "<Action> <Contact2>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName       | Action      |
      | user1Name | user2Name | user3Name | RemoveFromGroupChat | You removed |

  @C748 @id2282 @regression @rc
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the Search input
    And I see People Picker page
    And I keep on reopening People Picker until I see Top People
    And I tap <Contact1> avatar in Top People
    When I tap Open Conversation button on People Picker page
    Then I see the conversation view
    And I see the chat header message contains "<Contact1>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C749 @id2283 @regression @rc @rc44
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the Search input
    And I see People Picker page
    And I keep on reopening People Picker until I see Top People
    And I tap <Contact1> avatar in Top People
    When I tap Open Conversation button on People Picker page
    Then I see the conversation view
    And I do not see the Open Conversation button on People Picker page
    And I see the conversation <Contact1> in my conversations list
    And I see the chat header message contains "<Contact1>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C508 @id2898 @regression
  Scenario Outline: Start 1:1 conversation from group pop-over (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Contact <Contact1> sends message "<Message>" to user Myself
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    When I see the participant avatar <Contact1> on Group popover
    And I tap the participant avatar <Contact1> on Group popover
    And I tap Open Conversation button on Group popover
    Then I do not see the Group popover
    And I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message |
      | user1Name | user2Name | user3Name | GroupChat     | Msg     |

  @C531 @id3152 @regression
  Scenario Outline: Start 1:1 conversation from group pop-over (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given Contact <Contact1> sends message "<Message>" to user Myself
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    When I see the participant avatar <Contact1> on Group popover
    And I tap the participant avatar <Contact1> on Group popover
    And I tap Open Conversation button on Group popover
    Then I do not see the Group popover
    And I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message |
      | user1Name | user2Name | user3Name | GroupChat     | Msg     |

  @C481 @id2824 @regression
  Scenario Outline: I can access user details page from group details pop-over (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    When I see the participant avatar <Contact1> on Group popover
    And I tap the participant avatar <Contact1> on Group popover
    Then I see the user name <Contact1> on Group popover
    And I see the user email <Contact1Email> on Group popover

    Examples:
      | Name      | Contact1  | Contact1Email | Contact2  | GroupChatName |
      | user1Name | user2Name | user2Email    | user3Name | GroupChat     |

  @C530 @id3150 @regression
  Scenario Outline: I can access user details page from group details pop-over (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    When I see the participant avatar <Contact1> on Group popover
    And I tap the participant avatar <Contact1> on Group popover
    Then I see the user name <Contact1> on Group popover
    And I see the user email <Contact1Email> on Group popover

    Examples:
      | Name      | Contact1  | Contact1Email | Contact2  | GroupChatName |
      | user1Name | user2Name | user2Email    | user3Name | GroupChat     |

  @C763 @id2856 @regression @rc
  Scenario Outline: I see conversation name, number of participants and their avatars in group pop-over (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    When I tap Show Details button on conversation view page
    Then I see the Group popover
    And I see the participant avatars <Contact1>,<Contact2> on Group popover
    And I see the conversation name "<GroupChatName>" on Group popover
    And I see "<Subheader>" subheader on Group popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Subheader |
      | user1Name | user2Name | user3Name | GroupChat     | 2 PEOPLE  |

  @C801 @id3154 @regression @rc
  Scenario Outline: I see conversation name, number of participants and their avatars in group pop-over (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    When I tap Show Details button on conversation view page
    Then I see the Group popover
    And I see the participant avatars <Contact1>,<Contact2> on Group popover
    And I see the conversation name "<GroupChatName>" on Group popover
    And I see "<Subheader>" subheader on Group popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Subheader |
      | user1Name | user2Name | user3Name | GroupChat     | 2 PEOPLE  |

  @C507 @id2895 @regression
  Scenario Outline: Check interaction with options menu (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    When I tap Options button on Group popover
    And I see <ItemLeave> menu item on Group popover
    And I navigate back
    Then I do not see <ItemLeave> menu item on Group popover
    And I see the Group popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ItemLeave |
      | user1Name | user2Name | user3Name | GroupChat     | LEAVE     |

  @C529 @id3148 @regression
  Scenario Outline: Check interaction with options menu (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    When I tap Options button on Group popover
    And I see <ItemLeave> menu item on Group popover
    And I navigate back
    Then I do not see <ItemLeave> menu item on Group popover
    And I see the Group popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ItemLeave |
      | user1Name | user2Name | user3Name | GroupChat     | LEAVE     |

  @C773 @id2883 @regression @rc
  Scenario Outline: Verify you cannot start a 1:1 conversation from a group chat if the other user is not in your contacts list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to Myself,<Contact2>
    Given <Contact1> has group chat <GroupChatName> with Myself,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I do not see the conversation <Contact2> in my conversations list
    And I see the conversation <GroupChatName> in my conversations list
    And I tap the conversation <GroupChatName>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    And I see the participant avatar <Contact2> on Group popover
    And I tap the participant avatar <Contact2> on Group popover
    Then I do not see Open Conversation button on Group popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | NonConnectedUserChat |
      