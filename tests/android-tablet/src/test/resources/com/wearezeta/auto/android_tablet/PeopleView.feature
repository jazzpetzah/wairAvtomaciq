Feature: People View

  @C742 @regression @rc
  Scenario Outline: Check contact personal info in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap conversation name from top toolbar
    Then I see user name "<Contact>" on Single connected user details popover

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C732 @regression @rc @rc44
  Scenario Outline: Check contact personal info in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I tap on conversation name <Contact>
    And I tap conversation name from top toolbar
    Then I see user name "<Contact>" on Single connected user details popover

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C739 @regression @rc
  Scenario Outline: Leave group conversation in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap open menu button on Group info popover
    When I tap <ItemLeave> button on Group conversation options menu
    And I tap <ItemLeave> button on Confirm overlay page
    Then I do not see Group info popover
    And I do not see the conversation <GroupChatName> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  | ItemLeave |
      | user1Name | user2Name | user3Name | LeaveGroupChat | LEAVE     |

  @C729 @regression @rc @rc44
  Scenario Outline: Leave group conversation in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap open menu button on Group info popover
    When I tap <ItemLeave> button on Group conversation options menu
    And I tap <ItemLeave> button on Confirm overlay page
    Then I do not see Group info popover
    And I do not see the conversation <GroupChatName> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName  | ItemLeave |
      | user1Name | user2Name | user3Name | LeaveGroupChat | LEAVE     |

  @C468 @regression
  Scenario Outline: Remove from group chat in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact2> on Group info popover
    When I tap on remove button on Group connected user details page
    And I tap REMOVE button on Confirm overlay page
    Then I do not see participant <Contact2> on Group info popover
    When I press Back button 1 time
    Then I do not see Group info popover
    And I see the system message contains "<Action> <Contact2>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName       | Action      |
      | user1Name | user2Name | user3Name | RemoveFromGroupChat | You removed |

  @C464 @regression
  Scenario Outline: Remove from group chat in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact2> on Group info popover
    When I tap on remove button on Group connected user details page
    And I tap REMOVE button on Confirm overlay page
    Then I do not see participant <Contact2> on Group info popover
    When I press Back button 1 time
    Then I do not see Group info popover
    And I see the system message contains "<Action> <Contact2>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName       | Action      |
      | user1Name | user2Name | user3Name | RemoveFromGroupChat | You removed |

  @C748 @regression @rc
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given Myself wait until 2 people are in the Top People list on the backend
    Given I see the conversations list with conversations
    And I open Search UI
    And I tap <Contact1> avatar in Top People
    When I tap Open Conversation action button on Search page
    Then I see the conversation view

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C749 @regression @rc @rc44
  Scenario Outline: Verify starting 1:1 conversation with a person from Top People in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given Myself wait until 1 person is in the Top People list on the backend
    And I open Search UI
    And I tap <Contact1> avatar in Top People
    When I tap Open Conversation action button on Search page
    Then I see the conversation view
    And I do not see Open Conversation action button on Search page
    And I see the conversation <Contact1> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C508 @regression
  Scenario Outline: Start 1:1 conversation from group pop-over (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact1> sends encrypted message <Message> to user Myself
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    When I tap on contact <Contact1> on Group info popover
    And I tap on open conversation button on Group connected user details page
    Then I do not see Group info popover
    And I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message |
      | user1Name | user2Name | user3Name | GroupChat     | Msg     |

  @C531 @regression
  Scenario Outline: Start 1:1 conversation from group pop-over (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given User <Contact1> sends encrypted message <Message> to user Myself
    Given I tap on conversation name <GroupChatName>
    Given I tap conversation name from top toolbar
    When I tap on contact <Contact1> on Group info popover
    And I tap on open conversation button on Group connected user details page
    Then I do not see Group info popover
    And I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Message |
      | user1Name | user2Name | user3Name | GroupChat     | Msg     |

  @C481 @regression
  Scenario Outline: I can access user details page from group details pop-over (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1> on Group info popover
    Then I see user name "<Contact1>" on Group connected user details page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |

  @C530 @regression
  Scenario Outline: I can access user details page from group details pop-over (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> sets the unique username
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact1> on Group info popover
    Then I see user name "<Contact1>" on Group connected user details page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |

  @C763 @regression @rc
  Scenario Outline: I see conversation name, number of participants and their avatars in group pop-over (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    When I tap conversation name from top toolbar
    Then I see participant <Contact1> on Group info popover
    And I see participant <Contact2> on Group info popover
    And I see the conversation name is "<GroupChatName>" on Group info popover
    And I see "<Subheader>" in sub header on Group info popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Subheader |
      | user1Name | user2Name | user3Name | GroupChat     | 2 people  |

  @C801 @regression @rc
  Scenario Outline: I see conversation name, number of participants and their avatars in group pop-over (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    When I tap conversation name from top toolbar
    Then I see participant <Contact1> on Group info popover
    And I see participant <Contact2> on Group info popover
    And I see the conversation name is "<GroupChatName>" on Group info popover
    And I see "<Subheader>" in sub header on Group info popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Subheader |
      | user1Name | user2Name | user3Name | GroupChat     | 2 people  |

  @C507 @regression
  Scenario Outline: Check interaction with options menu (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I see Group info popover
    When I tap open menu button on Group info popover
    And I see <ItemLeave> button on Group conversation options menu
    And I navigate back
    Then I do not see <ItemLeave> button on Group conversation options menu
    And I see Group info popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ItemLeave |
      | user1Name | user2Name | user3Name | GroupChat     | LEAVE     |

  @C529 @regression
  Scenario Outline: Check interaction with options menu (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    When I tap open menu button on Group info popover
    And I see <ItemLeave> button on Group conversation options menu
    And I navigate back
    Then I do not see <ItemLeave> button on Group conversation options menu
    And I see Group info popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | ItemLeave |
      | user1Name | user2Name | user3Name | GroupChat     | LEAVE     |

  @C773 @regression @rc
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
    And I tap on conversation name <GroupChatName>
    And I tap conversation name from top toolbar
    And I tap on contact <Contact2> on Group info popover
    Then I see user name "<Contact2>" on Group unconnected user details page
    #TODO: Check the left action button label

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | NonConnectedUserChat |
      