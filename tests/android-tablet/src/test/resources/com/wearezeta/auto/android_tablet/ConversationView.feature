Feature: Conversation View

  @id2252 @smoke
  Scenario Outline: Send Message to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list
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

  @id2253 @smoke
  Scenario Outline: Send Hello and Hey to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    And I rotate UI to portrait
    Given I sign in using my email
    And I see the conversations list
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    And I swipe left on text input in the conversation view
    When I tap Ping button in the conversation view
    Then I see the ping message "<Message>" in the conversation view

    Examples: 
      | Name      | Contact   | Message    |
      | user1Name | user2Name | YOU PINGED |

  @id2254 @staging
  Scenario Outline: Send Camera picture to contact in portrait mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    And I rotate UI to portrait
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id2255 @staging
  Scenario Outline: Add people to 1:1 chat in portrait mode
    Given There are 3 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on tablet contact name <Contact1>
    And I see tablet dialog page
    And I tap on profile button
    And I see <Contact1> user profile page
    And I press add contact button
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I see user <Contact2> found on People picker page
    And I tap on user name found on People picker page <Contact2>
    And I see Add to conversation button
    And I click on Add to conversation button
    Then I see group chat page with users <Contact1>,<Contact2>
    And I navigate back from dialog page
    And I see <Contact1> and <Contact2> chat in contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @id2256 @staging
  Scenario Outline: Send message to group chat in portrait mode
    Given There are 3 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    And I rotate UI to portrait
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on tablet contact name <GroupChatName>
    And I see tablet dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @id2238 @staging
  Scenario Outline: Send Message to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on tablet contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Message |
      | user1Email | user1Password | user1Name | user2Name | Yo      |

  @id2239 @staging
  Scenario Outline: Send Hello and Hey to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    And I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Ping button
    Then I see Ping message <Message> in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   | Message    |
      | user1Email | user1Password | user1Name | user2Name | YOU PINGED |

  @id2240 @staging
  Scenario Outline: Send Camera picture to contact in landscape mode
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to <Name>
    And I rotate UI to landscape
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id2241 @staging
  Scenario Outline: Add people to 1:1 chat in landscape mode
    Given There are 3 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on tablet contact name <Contact1>
    And I see tablet dialog page
    And I tap on profile button
    And I see <Contact1> user profile page
    And I press add contact button
    And I see People picker page
    And I tap on Search input on People picker page
    And I enter "<Contact2>" into Search input on People Picker page
    And I see user <Contact2> found on People picker page
    And I tap on user name found on People picker page <Contact2>
    And I see Add to conversation button
    And I click on Add to conversation button
    Then I see group chat page with users <Contact1>,<Contact2>
    And I navigate back from dialog page
    And I see <Contact1> and <Contact2> chat in contact list

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @id2242 @staging
  Scenario Outline: Send message to group chat in landscape mode
    Given There are 3 users where <Name> is me
    Given <Name> is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    And I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list
    When I tap on tablet contact name <GroupChatName>
    And I see tablet dialog page
    And I tap on text input
    And I type the message "<Message>" and send it
    Then I see my message "<Message>" in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | GroupChatName     | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | SendMessGroupChat | Yo      |

  @id2047 @staging
  Scenario Outline: See one-to-one pop-over
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using login <Login> and password <Password>
    When I swipe right to tablet contact list
    And I see conversation list loaded with my name <Name>
    And I tap on tablet contact name <Contact>
    And I see tablet dialog page
    And I tap on profile button
    Then I see participant pop-over
    And I see <Contact> name and email in pop-over
    And I can close pop-over by close button
    When I tap on profile button
    Then I see participant pop-over
    And I see <Contact> name and email in pop-over
    And I can close pop-over by tapping outside

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @id2050 @staging
  Scenario Outline: One-to-one pop-over hidden after rotations
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using login <Login> and password <Password>
    When I swipe right to tablet contact list
    And I see conversation list loaded with my name <Name>
    And I tap on tablet contact name <Contact>
    And I see tablet dialog page
    And I rotate UI to landscape
    And I tap on profile button
    Then I see participant pop-over
    When I rotate UI to portrait
    Then I do not see participant pop-over
    When I tap on profile button
    Then I see participant pop-over
    When I rotate UI to landscape
    Then I do not see participant pop-over

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
