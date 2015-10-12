Feature: Search

  @id2249 @regression
  Scenario Outline: Open/Close Search by different actions in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversation
    When I tap the Search input
    And I see People Picker page
    And I close People Picker
    Then I see the conversations list with conversation
    When I tap the Search input
    And I see People Picker page
    And I hide keyboard
    And I navigate back
    Then I see the conversations list with conversation
    When I do long swipe down on conversations list
    And I see People Picker page
    And I hide keyboard
    And I do short swipe down on People Picker page
    Then I see People Picker page
    When I do long swipe down on People Picker page
    Then I see the conversations list with conversation
    And I do not see People Picker page
    When I do short swipe down on conversations list
    Then I do not see People Picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2263 @regression
  Scenario Outline: Open/Close Search by different actions in portrait mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversation
    When I tap the Search input
    And I see People Picker page
    And I close People Picker
    Then I see the conversations list with conversation
    When I tap the Search input
    And I see People Picker page
    And I hide keyboard
    And I navigate back
    Then I see the conversations list with conversation
    When I do long swipe down on conversations list
    And I see People Picker page
    And I hide keyboard
    And I do short swipe down on People Picker page
    Then I see People Picker page
    When I do long swipe down on People Picker page
    Then I see the conversations list with conversation
    And I do not see People Picker page
    When I do short swipe down on conversations list
    Then I do not see People Picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2180 @regression
  Scenario Outline: I should able to swipe to conversation when search is opened (portrait only)
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with no conversations
    When I tap the Search input
    And I see People Picker page
    And I swipe left to show the conversation view
    Then I do not see People Picker page
    When I swipe right to show the conversations list
    Then I see People Picker page

    Examples: 
      | Name      |
      | user1Name |

  @id2848 @regression
  Scenario Outline: I ignore someone from search and clear my inbox (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversation
    Given I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see the Incoming connections page
    And I ignore incoming connection request from <Contact> on Incoming connections page
    # Workaround for a bug
    And I tap in the center of Self Profile page
    And I tap in the center of Self Profile page
    And I swipe right to show the conversations list
    Then I see the Conversations list with no conversations

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id3130 @regression
  Scenario Outline: I ignore someone from search and clear my inbox (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversation
    Given I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see the Incoming connections page
    And I ignore incoming connection request from <Contact> on Incoming connections page
    Then I see the Conversations list with no conversations

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2853 @regression @rc
  Scenario Outline: I want to discard the new connect request (sending) by returning to the search results after selecting someone I’m not connected to
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with no conversations
    And I wait until <ContactEmail> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<ContactEmail>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see Outgoing Connection popover
    And I tap Close button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the Conversations list with no conversations

    Examples:
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @id3882 @regression @rc
  Scenario Outline: Verify opening conversation with action button (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Open Conversation button on People Picker page
    Then I do not see People Picker page
    And I see the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id3891 @regression @rc
  Scenario Outline: Verify opening conversation with action button (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Open Conversation button on People Picker page
    Then I do not see People Picker page
    And I see the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id3885 @regression
  Scenario Outline: Verify starting a new group conversation with action button (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Create Conversation button on People Picker page
    Then I do not see People Picker page
    And I see the conversation view
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    Then I see the participant avatar Myself on Group popover
    And I see the participant avatar <Contact1> on Group popover
    And I see the participant avatar <Contact2> on Group popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |

  @id3894 @regression
  Scenario Outline: Verify starting a new group conversation with action button (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Create Conversation button on People Picker page
    Then I do not see People Picker page
    And I see the conversation view
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    Then I see the participant avatar Myself on Group popover
    And I see the participant avatar <Contact1> on Group popover
    And I see the participant avatar <Contact2> on Group popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |

  @id3884 @regression
  Scenario Outline: Verify sending a photo with action button (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Camera button on People Picker page
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    And I do not see People Picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id3893 @regression
  Scenario Outline: Verify sending a photo with action button (portrait)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Camera button on People Picker page
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    And I do not see People Picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id3887 @regression
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Camera button on People Picker page
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    And I do not see People Picker page
    And I see the conversation view
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    Then I see the participant avatar Myself on Group popover
    And I see the participant avatar <Contact1> on Group popover
    And I see the participant avatar <Contact2> on Group popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |

  @id3896 @regression
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Camera button on People Picker page
    And I tap Take Photo button in the conversation view
    And I confirm the picture for the conversation view
    Then I see a new picture in the conversation view
    And I do not see People Picker page
    And I see the conversation view
    When I tap Show Tools button on conversation view page
    And I tap Show Details button on conversation view page
    And I see the Group popover
    Then I see the participant avatar Myself on Group popover
    And I see the participant avatar <Contact1> on Group popover
    And I see the participant avatar <Contact2> on Group popover

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName |
      | user1Name | user2Name | user3Name | GroupChat     |

  @id3881 @staging
  Scenario Outline: (AN-2884) Verify button Open is changed to Create after checking second person (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact1>" into Search input on People Picker page
    When I tap the found item <Contact1> on People Picker page
    Then I see Open Conversation button on People Picker page
    When I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    Then I see Create Conversation button on People Picker page
    When I tap the found item <Contact2> on People Picker page
    Then I see Open Conversation button on People Picker page
    When I tap the found item <Contact1> on People Picker page
    Then I do not see Open Conversation button on People Picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id3890 @staging
  Scenario Outline: (AN-2884) Verify button Open is changed to Create after checking second person (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact1>" into Search input on People Picker page
    When I tap the found item <Contact1> on People Picker page
    Then I see Open Conversation button on People Picker page
    When I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    Then I see Create Conversation button on People Picker page
    When I tap the found item <Contact2> on People Picker page
    Then I see Open Conversation button on People Picker page
    When I tap the found item <Contact1> on People Picker page
    Then I do not see Open Conversation button on People Picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id3883 @regression
  Scenario Outline: Verify starting a call with action button (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Call button on People Picker page
    Then I do not see People Picker page
    And I see the conversation view
    And I see calling overlay Big bar

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id3892 @regression
  Scenario Outline: Verify starting a call with action button (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Call button on People Picker page
    Then I do not see People Picker page
    And I see the conversation view
    And I see calling overlay Big bar

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
