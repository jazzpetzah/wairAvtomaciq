Feature: Conversation View

  @C736 @id2252 @regression @rc
  Scenario Outline: Send Message to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap the text input in the conversation view
    When I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |

  @C726 @id2238 @regression @rc @rc44
  Scenario Outline: Send Message to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I tap the text input in the conversation view
    When I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact   | Message |
      | user1Name | user2Name | Yo      |


  @C738 @id2254 @regression @rc
  Scenario Outline: Send Camera picture to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe right on text input in the conversation view
    When I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C728 @id2240 @regression @rc @rc44
  Scenario Outline: Send Camera picture to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe right on text input in the conversation view
    When I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C466 @id2255 @regression
  Scenario Outline: Add people to 1:1 chat in portrait mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I tap the conversation <Contact1>
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    When I tap Add People button on Single user popover
    And I enter "<Contact2>" into the Search input on Single user popover
    And I tap the avatar of <Contact2> in search results on Single user popover
    And I tap the Add To Conversation button on Single user popover
    Then I see the system message contains "<Action>" text on conversation view page
    And I see the system message contains "<Contact1>" text on conversation view page
    And I see the system message contains "<Contact2>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | Action                     |
      | user1Name | user2Name | user3Name | You started a conversation |

  @C462 @id2241 @regression
  Scenario Outline: Add people to 1:1 chat in landscape mode
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I tap the conversation <Contact1>
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Single user popover
    When I tap Add People button on Single user popover
    And I enter "<Contact2>" into the Search input on Single user popover
    And I tap the avatar of <Contact2> in search results on Single user popover
    And I tap the Add To Conversation button on Single user popover
    Then I see the system message contains "<Action>" text on conversation view page
    And I see the system message contains "<Contact1>" text on conversation view page
    And I see the system message contains "<Contact2>" text on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | Action                     |
      | user1Name | user2Name | user3Name | You started a conversation |

  @C467 @id2256 @regression
  Scenario Outline: Send message to group chat in portrait mode
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
    And I tap the text input in the conversation view
    When I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @C463 @id2242 @regression
  Scenario Outline: Send message to group chat in landscape mode
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
    And I tap the text input in the conversation view
    When I type the message "<Message>" in the conversation view
    And I send the typed message in the conversation view
    Then I see the message "<Message>" in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @C470 @id2047 @regression
  Scenario Outline: Check ability to open and close one-to-one pop-over in different ways
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to <Orientation>
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I see the conversation <Contact> in my conversations list
    When I tap the conversation <Contact>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    And I see the user name <Contact> on Single user popover
    When I tap Close button on Single user popover
    Then I do not see the Single user popover
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    When I tap in the center of Single user popover
    Then I see the Single user popover
    When I tap outside of Single user popover
    Then I do not see the Single user popover
    And I tap the text input in the conversation view
    And I tap Show Tools button on conversation view page
    When I tap Show Details button on conversation view page
    Then I see the Single user popover
    When I navigate back
    Then I do not see the Single user popover

    Examples:
      | Name      | Contact   | Orientation |
      | user1Name | user2Name | landscape   |
      | user1Name | user2Name | portrait    |

  @C375 @id2050 @regression
  Scenario Outline: (AN-2494) One-to-one pop-over hidden after rotations
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    When I tap the text input in the conversation view
    Then I do not see the Single user popover
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    When I rotate UI to landscape
    Then I do not see the Single user popover
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    Then I see the Single user popover
    When I rotate UI to portrait
    Then I do not see the Single user popover

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C482 @id2825 @regression
  Scenario Outline: Send image with camera in group chat (portrait)
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
    And I swipe right on text input in the conversation view
    When I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ChatWithImg   |

  @C483 @id2827 @regression
  Scenario Outline: Send image with camera in group chat (landscape)
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
    And I swipe right on text input in the conversation view
    When I tap Add Picture button in the conversation view
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | ChatWithImg   |

  @C757 @id2828 @regression @rc @rc44
  Scenario Outline: Send existing image from gallery in 1:1 chat (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe right on text input in the conversation view
    When I tap Add Picture button in the conversation view
    And I tap Gallery button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C758 @id2829 @regression @rc
  Scenario Outline: Send existing image from gallery in 1:1 chat (portrait)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe right on text input in the conversation view
    When I tap Add Picture button in the conversation view
    And I tap Gallery button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C772 @id2882 @regression @rc
  Scenario Outline: Verify editing the conversation name (portrait)
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
    When I rename the conversation to "<NewGroupChatName>" on Group popover
    And I tap Close button on Group popover
    Then I do not see the Group popover
    And I see the conversation name system message "<NewGroupChatName>" on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | NewGroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     | NewChatName      |

  @C800 @id3153 @regression @rc @rc44
  Scenario Outline: Verify editing the conversation name (landscape)
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
    When I rename the conversation to "<NewGroupChatName>" on Group popover
    And I tap Close button on Group popover
    Then I do not see the Group popover
    And I see the conversation name system message "<NewGroupChatName>" on conversation view page

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | NewGroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     | NewChatName      |

  @C815 @id3803 @regression @rc @rc44
  Scenario Outline: Send sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I tap the conversation <Contact1>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    And I tap Sketch button on conversation view page
    And I draw a sketch with <NumColors> colors on Sketch page
    When I tap Send button on Sketch page
    Then I see a new picture in the conversation view
    And I tap the new picture in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @C816 @id3804 @regression @rc
  Scenario Outline: Send sketch on picture from gallery
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    And I see the conversation <Contact1> in my conversations list
    And I tap the conversation <Contact1>
    And I see the conversation view
    And I tap Show Tools button on conversation view page
    When I tap Add Picture button in the conversation view
    And I tap Gallery button in the conversation view
    And I tap Sketch button on the picture preview
    And I draw a sketch with <NumColors> colors on Sketch page
    And I tap Send button on Sketch page
    Then I see a new picture in the conversation view
    And I tap the new picture in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |
