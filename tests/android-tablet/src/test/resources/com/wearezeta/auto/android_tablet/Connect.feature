Feature: Connect

  @id2281 @smoke
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
    And I enter connection message "<Message>" on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @id2280 @smoke
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
    And I enter connection message "<Message>" on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   | Message       |
      | user1Name | user2Name | Hellow friend |

  @id2245 @smoke
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

  @id2259 @smoke
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
  Scenario Outline: I want to send connection request by selecting unconnected user from a group conversation
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
    When I enter connection message "<Message>" on Group popover
    And I tap Connect button on Group popover
    Then I see Pending button on Group popover
    When I tap Show Details button on conversation view page
    Then I do not see the Group popover
    And I see the conversation <Contact2> in my conversations list
    And I tap the conversation <Contact2>
    And I see the message "<Message>" in the conversation view

    Examples: 
      | Name      | Contact1  | Contact2  | GroupChatName        | Message |
      | user1Name | user2Name | user3Name | NonConnectedUserChat | Yo man! |

  @id2989 @regression
  Scenario Outline: I can send connection request to user from search results by email
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
    And I enter connection message "<Message>" on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I close People Picker
    Then I see the conversation <Contact> in my conversations list

    Examples: 
      | Name      | Contact   | ContactEmail | Message       |
      | user1Name | user2Name | user2Email   | Hellow friend |

  @id2915 @staging
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

  @id3117 @staging
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

  @id2892 @staging
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
    And I enter connection message "<Message>" on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I hide keyboard
    And I do not see the previously remembered PYMK item on People Picker page
    And I close People Picker
    Then I see conversations list with the previously remembered PYMK item
    When I switch to the conversation with the previously remembered PYMK item
    Then I see the outgoing invitation message "<Message>" on conversation view page

    Examples: 
      | Name      | Contact1  | Contact2  | Message       |
      | user1Name | user2Name | user3Name | Hellow friend |

  @id3114 @staging
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
    And I enter connection message "<Message>" on Outgoing Connection popover
    And I tap Connect button on Outgoing Connection popover
    And I do not see Outgoing Connection popover
    And I see People Picker page
    And I hide keyboard
    And I do not see the previously remembered PYMK item on People Picker page
    And I close People Picker
    Then I see conversations list with the previously remembered PYMK item
    When I switch to the conversation with the previously remembered PYMK item
    Then I see the outgoing invitation message "<Message>" on conversation view page

    Examples: 
      | Name      | Contact1  | Contact2  | Message       |
      | user1Name | user2Name | user3Name | Hellow friend |