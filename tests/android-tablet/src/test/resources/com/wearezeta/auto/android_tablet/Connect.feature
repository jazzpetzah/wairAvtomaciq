Feature: Connect

  @C747 @regression @rc @rc44
  Scenario Outline: (AN-4282 for android 4.4) Send connection request from search by name in landscape
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    And I see Single unconnected user details popover
    And I see user name "<Contact>" on Single unconnected user details popover
    And I tap connect button on Single unconnected user details popover
    And I do not see Single unconnected user details popover
    And I close Search
    Then I see the conversation <Contact> in my conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C746 @regression @rc @rc44
  Scenario Outline: Send connection request from search by name in portrait
    Given There are 2 users where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    And I see Single unconnected user details popover
    And I see user name "<Contact>" on Single unconnected user details popover
    And I tap connect button on Single unconnected user details popover
    And I do not see Single unconnected user details popover
    And I swipe right to show the conversations list
    And I close Search
    Then I see the conversation <Contact> in my conversations list

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C730 @regression @rc @rc44
  Scenario Outline: Accept connection request in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    When I tap the conversation <WaitingMess>
    And I see user name "<Contact>" on Single pending incoming connection page
    And I tap connect button on Single pending incoming connection page
    Then I see the conversation <Contact> in my conversations list
    And I do not see the conversation <WaitingMess> in my conversations list

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C740 @regression @rc @rc44
  Scenario Outline: (AN-2735) Accept connection request in portrait mode
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    When I tap the conversation <WaitingMess>
    And I see user name "<Contact>" on Single pending incoming connection page
    And I tap connect button on Single pending incoming connection page
    Then I see the conversation <Contact> in my conversations list
    And I do not see the conversation <WaitingMess> in my conversations list

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C491 @regression
  Scenario Outline: I want to send connection request by selecting unconnected user from a group conversation (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to Myself,<Contact2>
    Given <Contact1> has group chat <GroupChatName> with Myself,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    Given I do not see the conversation <Contact2> in my conversations list
    Given I tap the conversation <GroupChatName>
    When I tap conversation name from top toolbar
    And I see Group info popover
    And I tap on contact <Contact2> on Group info popover
    And I tap on +connect button on Group unconnected user details page
    And I tap on connect button on Group unconnected user details page
    And I tap conversation name from top toolbar
    And I swipe right to show the conversations list
    Then I see the conversation <Contact2> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | NonConnectedUserChat |

  @C519 @regression
  Scenario Outline: I want to send connection request by selecting unconnected user from a group conversation (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to Myself,<Contact2>
    Given <Contact1> has group chat <GroupChatName> with Myself,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    Given I do not see the conversation <Contact2> in my conversations list
    Given I tap the conversation <GroupChatName>
    When I tap conversation name from top toolbar
    And I see Group info popover
    And I tap on contact <Contact2> on Group info popover
    And I tap on +connect button on Group unconnected user details page
    And I tap on connect button on Group unconnected user details page
    And I tap conversation name from top toolbar
    Then I see the conversation <Contact2> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | NonConnectedUserChat |

  @C789 @regression @rc
  Scenario Outline: Send connection request to user from search results by unique user name (portrait)
    Given There are 2 users where <Name> is me
    Given User <Contact> sets the unique username
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    And I wait until <ContactUniqueUsername> exists in backend search results
    When I open Search UI
    And I enter "<ContactUniqueUsername>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    And I see Single unconnected user details popover
    And I see user name "<Contact>" on Single unconnected user details popover
    And I see unique user name "<ContactUniqueUsername>" on Single unconnected user details popover
    And I tap connect button on Single unconnected user details popover
    And I do not see Single unconnected user details popover
    And I swipe right to show the conversations list
    And I close Search
    Then I see the conversation <Contact> in my conversations list

    Examples:
      | Name      | Contact   | ContactUniqueUsername |
      | user1Name | user2Name | user2UniqueUsername   |

  @C790 @regression @rc
  Scenario Outline: Send connection request to user from search results by unique user name (landscape)
    Given There are 2 users where <Name> is me
    Given User <Contact> sets the unique username
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    And I wait until <ContactUniqueUsername> exists in backend search results
    When I open Search UI
    And I enter "<ContactUniqueUsername>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    And I see Single unconnected user details popover
    And I see user name "<Contact>" on Single unconnected user details popover
    And I see unique user name "<ContactUniqueUsername>" on Single unconnected user details popover
    And I tap connect button on Single unconnected user details popover
    And I do not see Single unconnected user details popover
    And I close Search
    Then I see the conversation <Contact> in my conversations list

    Examples:
      | Name      | Contact   | ContactUniqueUsername |
      | user1Name | user2Name | user2UniqueUsername   |

  @C489 @regression
  Scenario Outline: (AN-4823) Ignore a connect request and reconnect later from search (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    When I tap the conversation <WaitingMess>
    And I see user name "<Contact>" on Single pending incoming connection page
    And I tap ignore button on Single pending incoming connection page
    # Workaround for a bug
    And I wait for 3 seconds
    And I swipe right to show the conversations list
    Then I do not see the conversation <Contact> in my conversations list
    And I do not see the conversation <WaitingMess> in my conversations list
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    And I see Single pending incoming connection popover
    And I see user name "<Contact>" on Single pending incoming connection popover
    And I tap connect button on Single pending incoming connection popover
    And I do not see Single pending incoming connection popover
    And I swipe right to show the conversations list
    And I close Search
    Then I see the conversation <Contact> in my conversations list

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C522 @regression
  Scenario Outline: (AN-4823) Ignore a connect request and reconnect later from search (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    When I tap the conversation <WaitingMess>
    And I see user name "<Contact>" on Single pending incoming connection page
    And I tap ignore button on Single pending incoming connection page
    Then I do not see the conversation <Contact> in my conversations list
    And I do not see the conversation <WaitingMess> in my conversations list
    And I wait until <Contact> exists in backend search results
    And I open Search UI
    And I enter "<Contact>" into Search input on Search page
    And I tap on user name found on Search page <Contact>
    And I see Single pending incoming connection popover
    And I see user name "<Contact>" on Single pending incoming connection popover
    And I tap connect button on Single pending incoming connection popover
    And I do not see Single pending incoming connection popover
    And I close Search
    Then I see the conversation <Contact> in my conversations list

    Examples:
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @C488 @regression
  Scenario Outline: (AN-2954) Inbox count increasing/decreasing correctly (portrait)
    Given There are 4 users where <Name> is me
    Given <Contact2> sent connection request to me
    Given <Contact1> sent connection request to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    When I tap the conversation <WaitingMess2>
    And I scroll to contact <Contact2> on Single pending incoming connection page
    And I see user name "<Contact2>" on Single pending incoming connection page
    And I tap ignore button for user <Contact2> on Single pending incoming connection page
    And I swipe right to show the conversations list
    Then I see the conversation <WaitingMess1> in my conversations list
    When I tap the conversation <WaitingMess1>
    And I see user name "<Contact1>" on Single pending incoming connection page
    And I tap ignore button for user <Contact1> on Single pending incoming connection page
    Then I see the Conversations list with no conversations
    When <Contact3> sent connection request to me
    Then I see the conversation <WaitingMess1> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | WaitingMess2     | WaitingMess1     |
      | user1Name | user2Name | user3Name | user4Name | 2 people waiting | 1 person waiting |

  @C518 @regression
  Scenario Outline: Inbox count increasing/decreasing correctly (landscape)
    Given There are 4 users where <Name> is me
    Given <Contact2> sent connection request to me
    Given <Contact1> sent connection request to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversation
    When I tap the conversation <WaitingMess2>
    And I scroll to contact <Contact2> on Single pending incoming connection page
    And I see user name "<Contact2>" on Single pending incoming connection page
    And I tap ignore button for user <Contact2> on Single pending incoming connection page
    When I tap the conversation <WaitingMess1>
    And I see user name "<Contact1>" on Single pending incoming connection page
    And I tap ignore button for user <Contact1> on Single pending incoming connection page
    Then I see the Conversations list with no conversations
    When <Contact3> sent connection request to me
    Then I see the conversation <WaitingMess1> in my conversations list

    Examples:
      | Name      | Contact1  | Contact2  | Contact3  | WaitingMess2     | WaitingMess1     |
      | user1Name | user2Name | user3Name | user4Name | 2 people waiting | 1 person waiting |

  @C499 @regression
  Scenario Outline: (AN-2896) I can see a new inbox for connection when receive new connection request (portrait)
    Given There are 2 users where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    When <Contact> sent connection request to me
    # Workaround for a bug
    And I swipe right to show the conversations list
    When I tap the conversation <WaitingMsg>
    Then I see user name "<Contact>" on Single pending incoming connection page

    Examples:
      | Name      | Contact   | WaitingMsg       |
      | user1Name | user2Name | 1 person waiting |

  @C526 @regression
  Scenario Outline: I can see a new inbox for connection when receive new connection request (landscape)
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    When <Contact> sent connection request to me
    When I tap the conversation <WaitingMsg>
    Then I see user name "<Contact>" on Single pending incoming connection page

    Examples:
      | Name      | Contact   | WaitingMsg       |
      | user1Name | user2Name | 1 person waiting |

  @C492 @regression
  Scenario Outline: (AN-2897) I want to see that the other person has accepted the connect request in the conversation view (portrait)
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    When I tap the conversation <Contact>
    Then I see user name "<Contact>" on Single pending outgoing connection page
    When <Contact> accepts all requests
    # Workaround for AN-2897 with following 2 lines
    And I navigate back
    And I tap the conversation <Contact>
    Then I see the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C523 @regression
  Scenario Outline: (AN-2897) I want to see that the other person has accepted the connect request in the conversation view (landscape)
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    When I tap the conversation <Contact>
    Then I see user name "<Contact>" on Single pending outgoing connection page
    When <Contact> accepts all requests
    # Workaround for AN-2897 with following 2 lines
    And I navigate back
    And I tap the conversation <Contact>
    Then I see the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C493 @regression
  Scenario Outline: I would not know other person has ignored my connection request
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with conversations
    When I tap the conversation <Contact>
    Then I see user name "<Contact>" on Single pending outgoing connection page
    When <Contact> ignores all requests
    Then I do not see conversation view
    And I see user name "<Contact>" on Single pending outgoing connection page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C761 @regression @rc
  Scenario Outline: I can receive new connection request when app in background
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the Conversations list with no conversations
    When I minimize the application
    And <Contact> sent connection request to me
    And I restore the application
    When I tap the conversation <WaitingMsg>
    Then I see user name "<Contact>" on Single pending incoming connection page

    Examples:
      | Name      | Contact   | WaitingMsg       |
      | user1Name | user2Name | 1 person waiting |