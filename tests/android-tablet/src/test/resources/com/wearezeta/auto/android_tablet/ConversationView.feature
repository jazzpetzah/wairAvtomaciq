Feature: Conversation View

  @C736 @regression @rc
  Scenario Outline: Send Message to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    And I tap on text input
    When I type the message "<Message>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C726 @regression @rc @rc44
  Scenario Outline: Send Message to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    And I tap on text input
    When I type the message "<Message>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C728 @regression @rc @rc44
  Scenario Outline: Send Camera picture to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    When I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C466 @regression
  Scenario Outline: Add people to 1:1 chat in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact1>
    And I tap conversation name from top toolbar
    And I see Single connected user details popover
    When I tap create group button on Single connected user details popover
    And I enter "<Contact2>" into Search input on Search page
    And I tap on user name found on Search page <Contact2>
    And I tap on Add to conversation button on Search page
    Then I see the system message contains "<Action>" text on conversation view page
    And I see the system message contains "<Contact1>" text on conversation view page
    And I see the system message contains "<Contact2>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | Action    |
      | user1Name | user2Name | user3Name | You added |

  @C462 @regression
  Scenario Outline: Add people to 1:1 chat in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact1>
    And I tap conversation name from top toolbar
    And I see Single connected user details popover
    When I tap create group button on Single connected user details popover
    And I enter "<Contact2>" into Search input on Search page
    And I hide keyboard
    And I tap on user name found on Search page <Contact2>
    And I tap on Add to conversation button on Search page
    Then I see the system message contains "<Action>" text on conversation view page
    And I see the system message contains "<Contact1>" text on conversation view page
    And I see the system message contains "<Contact2>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | Action    |
      | user1Name | user2Name | user3Name | You added |

  @C467 @regression
  Scenario Outline: Send message to group chat in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <GroupChatName>
    And I tap on text input
    When I type the message "<Message>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @C463 @regression
  Scenario Outline: Send message to group chat in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <GroupChatName>
    And I tap on text input
    When I type the message "<Message>" in the conversation view
    And I send the typed message by cursor Send button in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @C470 @regression
  Scenario Outline: Check ability to open and close one-to-one pop-over in different ways
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to <Orientation>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I tap the conversation <Contact>
    And I tap conversation name from top toolbar
    Then I see Single connected user details popover
    And I see user name "<Contact>" on Single connected user details popover
    When I press Back button 1 time
    Then I do not see Single connected user details popover
    When I tap conversation name from top toolbar
    Then I see Single connected user details popover
    When I tap center of Single connected user details popover
    Then I see Single connected user details popover
    When I tap outside of Single connected user details popover
    Then I do not see Single connected user details popover
    And I tap on text input
    And I hide keyboard
    When I tap conversation name from top toolbar
    Then I see Single connected user details popover
    When I navigate back
    Then I do not see Single connected user details popover

    Examples:
      | Name      | Contact   | Orientation |
      | user1Name | user2Name | landscape   |
      | user1Name | user2Name | portrait    |

  @C375 @regression
  Scenario Outline: (AN-2494) One-to-one pop-over hidden after rotations
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    When I tap on text input
    Then I do not see Single connected user details popover
    When I tap conversation name from top toolbar
    Then I see Single connected user details popover
    When I rotate UI to landscape
    Then I do not see Single connected user details popover
    When I tap conversation name from top toolbar
    Then I see Single connected user details popover
    When I rotate UI to portrait
    Then I do not see Single connected user details popover

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C483 @regression
  Scenario Outline: Send image with camera in group chat (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <GroupChatName>
    When I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ChatWithImg   |

  @C757 @regression @rc @rc44
  Scenario Outline: Send existing image from gallery in 1:1 chat (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact>
    When I tap Add picture button from cursor toolbar
    And I tap Gallery Camera button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C772 @regression @rc
  Scenario Outline: Verify editing the conversation name (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <GroupChatName>
    And I tap conversation name from top toolbar
    And I see Group info popover
    When I rename group conversation to <NewGroupChatName> on Group info popover
    And I press Back button 2 time
    Then I do not see Group info popover
    And I see the system message of new conversation name "<NewGroupChatName>" on Conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | NewGroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     | NewChatName      |

  @C800 @regression @rc @rc44
  Scenario Outline: Verify editing the conversation name (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <GroupChatName>
    And I tap conversation name from top toolbar
    And I see Group info popover
    When I rename group conversation to <NewGroupChatName> on Group info popover
    And I press Back button 2 time
    Then I do not see Group info popover
    And I see the system message of new conversation name "<NewGroupChatName>" on Conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | NewGroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     | NewChatName      |

  @C815 @regression @rc @rc44
  Scenario Outline: Send sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact1>
    And I tap Sketch button from cursor toolbar
    And I draw a sketch with <NumColors> colors on Sketch page
    When I tap Send button on Sketch page
    Then I see a new picture in the conversation view
    And I tap Image container in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @C816 @regression @rc
  Scenario Outline: Send sketch on picture from gallery
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I push local file named "<FileName>" to the device
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I tap the conversation <Contact1>
    When I tap Add picture button from cursor toolbar
    And I tap Gallery Camera button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I see a new picture in the conversation view
    And I tap Image container in the conversation view
    And I tap on Sketch button on the recent image in the conversation view
    And I draw a sketch with <NumColors> colors on Sketch page
    And I tap Send button on Sketch page
    Then I see a new picture in the conversation view
    And I tap Image container in the conversation view

    Examples:
      | Name      | Contact1  | NumColors | FileName       |
      | user1Name | user2Name | 6         | avatarTest.png |
