Feature: Search
  @C471 @id2180 @regression
  Scenario Outline: I should able to swipe to conversation when search is opened (portrait only)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I open Search UI
    And I swipe left to show the conversation view
    Then I do not see People Picker page
    When I swipe right to show the conversations list
    Then I see People Picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C490 @id2848 @regression
  Scenario Outline: I ignore someone from search and clear my inbox (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    Given I wait until <Contact> exists in backend search results
    When I open Search UI
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

  @C524 @id3130 @regression
  Scenario Outline: I ignore someone from search and clear my inbox (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    Given I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see the Incoming connections page
    And I ignore incoming connection request from <Contact> on Incoming connections page
    Then I see the Conversations list with no conversations

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C762 @id2853 @regression @rc
  Scenario Outline: I want to discard the new connect request (sending) by returning to the search results after selecting someone I’m not connected to
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    And I wait until <ContactEmail> exists in backend search results
    When I open Search UI
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

  @C819 @id3882 @regression @rc @rc44
  Scenario Outline: Verify opening conversation with action button (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Open Conversation action button on People Picker page
    Then I do not see People Picker page
    And I see the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C820 @id3891 @regression @rc
  Scenario Outline: Verify opening conversation with action button (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Open Conversation action button on People Picker page
    Then I do not see People Picker page
    And I see the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C537 @id3885 @regression
  Scenario Outline: Verify starting a new group conversation with action button (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Create Conversation action button on People Picker page
    Then I do not see People Picker page
    When I tap conversation name from top toolbar
    Then I see the participant avatar <Contact1> on Group popover
    And I see the participant avatar <Contact2> on Group popover
    And I do not see the participant avatar Myself on Group popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C545 @id3894 @regression
  Scenario Outline: Verify starting a new group conversation with action button (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Create Conversation action button on People Picker page
    Then I do not see People Picker page
    When I tap conversation name from top toolbar
    Then I see the participant avatar <Contact1> on Group popover
    And I see the participant avatar <Contact2> on Group popover
    And I do not see the participant avatar Myself on Group popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C536 @id3884 @regression
  Scenario Outline: Verify sending a photo with action button (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    Given I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Send Image action button on People Picker page
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view
    And I do not see People Picker page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C539 @id3887 @regression
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Send Image action button on People Picker page
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view
    And I do not see People Picker page
    When I tap conversation name from top toolbar
    And I see the Group popover
    Then I do not see the participant avatar Myself on Group popover
    And I see the participant avatar <Contact1> on Group popover
    And I see the participant avatar <Contact2> on Group popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C547 @id3896 @regression
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Send Image action button on People Picker page
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    Then I see a new picture in the conversation view
    And I do not see People Picker page
    When I tap conversation name from top toolbar
    And I see the Group popover
    Then I do not see the participant avatar Myself on Group popover
    And I see the participant avatar <Contact1> on Group popover
    And I see the participant avatar <Contact2> on Group popover

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C534 @id3881 @regression
  Scenario Outline: (AN-3616) Verify button Open is changed to Create after checking second person (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    When I tap the found item <Contact1> on People Picker page
    Then I see Open Conversation action button on People Picker page
    When I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    Then I see Create Conversation action button on People Picker page
    When I type backspace in Search input on People Picker page
    Then I see Open Conversation action button on People Picker page
    When I type backspace in Search input on People Picker page
    Then I do not see Open Conversation action button on People Picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C542 @id3890 @regression
  Scenario Outline: (AN-3616) Verify button Open is changed to Create after checking second person (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    When I tap the found item <Contact1> on People Picker page
    Then I see Open Conversation action button on People Picker page
    When I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    Then I see Create Conversation action button on People Picker page
    When I type backspace in Search input on People Picker page
    Then I see Open Conversation action button on People Picker page
    When I type backspace in Search input on People Picker page
    Then I do not see Open Conversation action button on People Picker page

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C535 @id3883 @regression
  Scenario Outline: Verify starting a call with action button (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Call action button on People Picker page
    Then I do not see People Picker page
    And I see outgoing call

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C543 @id3892 @regression
  Scenario Outline: Verify starting a call with action button (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    When I tap Call action button on People Picker page
    Then I do not see People Picker page
    And I see outgoing call

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C538 @id3886 @regression
  Scenario Outline: Verify starting a group conversation and a group call with action button (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Call action button on People Picker page
    Then I see outgoing call

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @C546 @id3895 @regression
  Scenario Outline: Verify starting a group conversation and a group call with action button (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I wait until <Contact1> exists in backend search results
    Given I wait until <Contact2> exists in backend search results
    Given I see the Conversations list with conversations
    And I open Search UI
    And I enter "<Contact1>" into Search input on People Picker page
    And I tap the found item <Contact1> on People Picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I tap the found item <Contact2> on People Picker page
    When I tap Call action button on People Picker page
    Then I do not see People Picker page
    Then I see outgoing call

    Examples:
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |