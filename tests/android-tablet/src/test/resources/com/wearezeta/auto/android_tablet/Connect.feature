Feature: Connect

  @id2281 @regression @rc
  Scenario Outline: Send connection request from search by name in landscape
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see Outgoing Connection popover
    And I see the name <Contact> on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2280 @regression @rc
  Scenario Outline: Send connection request from search by name in portrait
    Given There are 2 users where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with no conversations
    And I wait until <Contact> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see Outgoing Connection popover
    And I see the name <Contact> on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2245 @regression @rc
  Scenario Outline: Accept connection request in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversation
    And I see the conversation <WaitingMess> in my conversations list
    When I tap the conversation <WaitingMess>
    And I see the Incoming connections page
    And I accept incoming connection request from <Contact> on Incoming connections page
    Then I see the conversation <Contact> in my conversations list
    And I do not see the conversation <WaitingMess> in my conversations list

    Examples: 
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @id2259 @regression @rc
  Scenario Outline: Accept connection request in portrait mode
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversation
    And I see the conversation <WaitingMess> in my conversations list
    When I tap the conversation <WaitingMess>
    And I see the Incoming connections page
    And I accept incoming connection request from <Contact> on Incoming connections page
    Then I see the conversation view
    And I navigate back
    And I see the conversation <Contact> in my conversations list
    And I do not see the conversation <WaitingMess> in my conversations list

    Examples: 
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @id2852 @regression
  Scenario Outline: I want to send connection request by selecting unconnected user from a group conversation (portrait)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to Myself,<Contact2>
    Given <Contact1> has group chat <GroupChatName> with Myself,<Contact2>
    Given I rotate UI to portrait
    Given I sign in using my email
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
    And I tap Connect button on Group popover
    Then I see Pending button on Group popover
    When I tap Show Details button on conversation view page
    Then I do not see the Group popover
    And I see the conversation <Contact2> in my conversations list

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | NonConnectedUserChat |

  @id3119 @regression
  Scenario Outline: I want to send connection request by selecting unconnected user from a group conversation (landscape)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> is connected to Myself,<Contact2>
    Given <Contact1> has group chat <GroupChatName> with Myself,<Contact2>
    Given I rotate UI to landscape
    Given I sign in using my email
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
    And I tap Connect button on Group popover
    Then I see Pending button on Group popover
    When I tap Show Details button on conversation view page
    Then I do not see the Group popover
    And I see the conversation <Contact2> in my conversations list

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName        |
      | user1Name | user2Name | user3Name | NonConnectedUserChat |

  @id3089 @regression @rc
  Scenario Outline: Send connection request to user from search results by email (portrait)
    Given There are 2 users where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with no conversations
    And I wait until <ContactEmail> exists in backend search results
    When I tap Search input
    And I see People Picker page
    And I enter "<ContactEmail>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see Outgoing Connection popover
    And I see the name <Contact> on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @id3102 @regression @rc
  Scenario Outline: Send connection request to user from search results by email (landscape)
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
    And I see the name <Contact> on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   | ContactEmail |
      | user1Name | user2Name | user2Email   |

  @id2915 @regression @rc
  Scenario Outline: Connect to someone from PYMK by clicking + (portrait)
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact1>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    When I tap Search input
    And I see People Picker page
    And I hide keyboard
    And I remember the name of the first PYMK item on People Picker page
    And I tap + button on the first PYMK item on People Picker page
    Then I do not see the previously remembered PYMK item on People Picker page
    When I close People Picker
    Then I see conversations list with the previously remembered PYMK item
    When I tap Search input
    And I see People Picker page
    And I hide keyboard
    Then I do not see the previously remembered PYMK item on People Picker page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id3117 @regression @rc
  Scenario Outline: Connect to someone from PYMK by clicking + (landscape)
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    When I tap Search input
    And I see People Picker page
    And I hide keyboard
    And I remember the name of the first PYMK item on People Picker page
    And I tap + button on the first PYMK item on People Picker page
    Then I do not see the previously remembered PYMK item on People Picker page
    When I close People Picker
    Then I see conversations list with the previously remembered PYMK item
    When I tap Search input
    And I see People Picker page
    And I hide keyboard
    Then I do not see the previously remembered PYMK item on People Picker page

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id2892 @regression
  Scenario Outline: Connect to someone from PYMK by tap and typing connect message (portrait)
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact1>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    When I tap Search input
    And I see People Picker page
    And I hide keyboard
    And I remember the name of the first PYMK item on People Picker page
    And I tap the first PYMK item on People Picker page
    And I see Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I hide keyboard
    And I do not see the previously remembered PYMK item on People Picker page
    And I close People Picker
    Then I see conversations list with the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id3114 @regression
  Scenario Outline: Connect to someone from PYMK by tap and typing connect message (landscape)
    Given There are 3 users where <Name> is me
    Given <Contact1> is connected to <Contact2>
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    When I tap Search input
    And I see People Picker page
    And I hide keyboard
    And I remember the name of the first PYMK item on People Picker page
    And I tap the first PYMK item on People Picker page
    And I see Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I hide keyboard
    And I do not see the previously remembered PYMK item on People Picker page
    And I close People Picker
    Then I see conversations list with the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  |
      | user1Name | user2Name | user3Name |

  @id2914 @regression @rc
  Scenario Outline: Dismiss PYMK item (portrait)
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given Myself is connected to <Contact1>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I hide keyboard
    When I remember the name of the first PYMK item on People Picker page
    And I do short swipe right the first PYMK item on People Picker page
    And I tap Hide button in the first PYMK item on People Picker page
    Then I do not see the previously remembered PYMK item on People Picker page
    When I close People Picker
    Then I do not see conversations list with the previously remembered PYMK item
    And I tap Search input
    And I see People Picker page
    And I hide keyboard
    When I remember the name of the first PYMK item on People Picker page
    And I do long swipe right the first PYMK item on People Picker page
    Then I do not see the previously remembered PYMK item on People Picker page
    When I close People Picker
    Then I do not see conversations list with the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @id3116 @regression @rc
  Scenario Outline: Dismiss PYMK item (landscape)
    Given There are 4 users where <Name> is me
    Given <Contact1> is connected to <Contact2>,<Contact3>
    Given Myself is connected to <Contact1>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I tap Search input
    And I see People Picker page
    And I hide keyboard
    When I remember the name of the first PYMK item on People Picker page
    And I do short swipe right the first PYMK item on People Picker page
    And I tap Hide button in the first PYMK item on People Picker page
    Then I do not see the previously remembered PYMK item on People Picker page
    When I close People Picker
    Then I do not see conversations list with the previously remembered PYMK item
    And I tap Search input
    And I see People Picker page
    And I hide keyboard
    When I remember the name of the first PYMK item on People Picker page
    And I do long swipe right the first PYMK item on People Picker page
    Then I do not see the previously remembered PYMK item on People Picker page
    When I close People Picker
    Then I do not see conversations list with the previously remembered PYMK item

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @id2845 @regression
  Scenario Outline: Ignore a connect request and reconnect later from search (portrait)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversation
    And I see the conversation <WaitingMess> in my conversations list
    When I tap the conversation <WaitingMess>
    And I see the Incoming connections page
    And I ignore incoming connection request from <Contact> on Incoming connections page
    And I swipe right to show the conversations list
    Then I do not see the conversation <Contact> in my conversations list
    And I do not see the conversation <WaitingMess> in my conversations list
    # Workaround for a bug
    And I tap in the center of Self Profile page
    When I swipe right to show the conversations list
    And I wait until <Contact> exists in backend search results
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see Incoming Connection popover
    And I see the name <Contact> on Incoming Connection popover
    And I tap Accept button on Incoming Connection popover
    And I do not see Incoming Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @id3127 @regression
  Scenario Outline: Ignore a connect request and reconnect later from search (landscape)
    Given There are 2 users where <Name> is me
    Given <Contact> sent connection request to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversation
    And I see the conversation <WaitingMess> in my conversations list
    When I tap the conversation <WaitingMess>
    And I see the Incoming connections page
    And I ignore incoming connection request from <Contact> on Incoming connections page
    Then I do not see the conversation <Contact> in my conversations list
    And I do not see the conversation <WaitingMess> in my conversations list
    And I wait until <Contact> exists in backend search results
    And I tap Search input
    And I see People Picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap the found item <Contact> on People Picker page
    And I see Incoming Connection popover
    And I see the name <Contact> on Incoming Connection popover
    And I tap Accept button on Incoming Connection popover
    And I do not see Incoming Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   | WaitingMess      |
      | user1Name | user2Name | 1 person waiting |

  @id2844 @regression
  Scenario Outline: Inbox count increasing/decreasing correctly (portrait)
    Given There are 4 users where <Name> is me
    Given <Contact2> sent connection request to me
    Given <Contact1> sent connection request to me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversation
    And I see the conversation <WaitingMess2> in my conversations list
    When I tap the conversation <WaitingMess2>
    And I see the Incoming connections page
    And I ignore incoming connection request from <Contact2> on Incoming connections page
    And I navigate back
    Then I see the conversation <WaitingMess1> in my conversations list
    When I tap the conversation <WaitingMess1>
    And I see the Incoming connections page
    And I ignore incoming connection request from <Contact1> on Incoming connections page
    And I navigate back
    Then I see the Conversations list with no conversations
    When <Contact3> sent connection request to me
    Then I see the conversation <WaitingMess1> in my conversations list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | WaitingMess2     | WaitingMess1     |
      | user1Name | user2Name | user3Name | user4Name | 2 people waiting | 1 person waiting |

  @id3118 @regression
  Scenario Outline: Inbox count increasing/decreasing correctly (landscape)
    Given There are 4 users where <Name> is me
    Given <Contact2> sent connection request to me
    Given <Contact1> sent connection request to me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversation
    And I see the conversation <WaitingMess2> in my conversations list
    When I tap the conversation <WaitingMess2>
    And I see the Incoming connections page
    And I scroll the list up to 2 times until <Contact2> entry is visible on Incoming connections page
    And I ignore incoming connection request from <Contact2> on Incoming connections page
    Then I see the conversation <WaitingMess1> in my conversations list
    When I tap the conversation <WaitingMess1>
    And I see the Incoming connections page
    And I scroll the list up to 2 times until <Contact1> entry is visible on Incoming connections page
    And I ignore incoming connection request from <Contact1> on Incoming connections page
    Then I see the Conversations list with no conversations
    When <Contact3> sent connection request to me
    Then I see the conversation <WaitingMess1> in my conversations list

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | WaitingMess2     | WaitingMess1     |
      | user1Name | user2Name | user3Name | user4Name | 2 people waiting | 1 person waiting |

  @id2869 @regression
  Scenario Outline: I can see a new inbox for connection when receive new connection request (portrait)
    Given There are 2 users where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with no conversations
    When <Contact> sent connection request to me
    Then I see the conversation <WaitingMsg> in my conversations list
    When I tap the conversation <WaitingMsg>
    Then I see the Incoming connections page
    And I see email <ContactEmail> on Incoming connections page
    And I see name <Contact> on Incoming connections page

    Examples: 
      | Name      | Contact   | ContactEmail | WaitingMsg       |
      | user1Name | user2Name | user2Email   | 1 person waiting |

  @id3135 @regression
  Scenario Outline: I can see a new inbox for connection when receive new connection request (landscape)
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with no conversations
    When <Contact> sent connection request to me
    Then I see the conversation <WaitingMsg> in my conversations list
    When I tap the conversation <WaitingMsg>
    Then I see the Incoming connections page
    And I see email <ContactEmail> on Incoming connections page
    And I see name <Contact> on Incoming connections page

    Examples: 
      | Name      | Contact   | ContactEmail | WaitingMsg       |
      | user1Name | user2Name | user2Email   | 1 person waiting |

  @id2854 @regression
  Scenario Outline: I want to see that the other person has accepted the connect request in the conversation view (portrait)
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    When I tap the conversation <Contact>
    Then I see Outgoing Pending Connection page
    And I see <Contact> name on the Outgoing Pending Connection page
    When <Contact> accepts all requests
    Then I see the conversation view
    And I see the system connection message contains "<SysMessage>" text on Conversation view page

    Examples: 
      | Name      | Contact   | SysMessage             |
      | user1Name | user2Name | Connected to user2Name |

  @id3128 @regression
  Scenario Outline: I want to see that the other person has accepted the connect request in the conversation view (landscape)
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    When I tap the conversation <Contact>
    Then I see Outgoing Pending Connection page
    And I see <Contact> name on the Outgoing Pending Connection page
    When <Contact> accepts all requests
    Then I see the conversation view
    And I see the system connection message contains "<SysMessage>" text on Conversation view page

    Examples: 
      | Name      | Contact   | SysMessage             |
      | user1Name | user2Name | Connected to user2Name |

  @id2855 @regression
  Scenario Outline: I would not know other person has ignored my connection request
    Given There are 2 users where <Name> is me
    Given Myself sent connection request to <Contact>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    When I tap the conversation <Contact>
    Then I see Outgoing Pending Connection page
    And I see <Contact> name on the Outgoing Pending Connection page
    When <Contact> ignores all requests
    Then I do not see conversation view
    And I see <Contact> name on the Outgoing Pending Connection page

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @id2846 @regression @rc
  Scenario Outline: I can receive new connection request when app in background
    Given There are 2 users where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the Conversations list with no conversations
    When I minimize the application
    And <Contact> sent connection request to me
    And I restore the application
    Then I see the conversation <WaitingMsg> in my conversations list
    When I tap the conversation <WaitingMsg>
    Then I see the Incoming connections page
    And I see email <ContactEmail> on Incoming connections page
    And I see name <Contact> on Incoming connections page

    Examples:
      | Name      | Contact   | ContactEmail | WaitingMsg       |
      | user1Name | user2Name | user2Email   | 1 person waiting |
