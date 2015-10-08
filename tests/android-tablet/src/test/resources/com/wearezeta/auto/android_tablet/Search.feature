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

  @id3882 @staging
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

  @id3891 @staging
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
