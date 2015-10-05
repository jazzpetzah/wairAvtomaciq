Feature: Conversation View

  @staging @id3897
  Scenario Outline: Verify I can ping a conversation using the menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Ping"
    Then I see ping message <PING>
    And I click menu bar item "Conversation" and menu item "Ping"
    Then I see ping message <PING_AGAIN>

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name | pinged | pinged again |


  @staging @id3781
   Scenario Outline: Verify you ping in a conversation when you press ⌘ + K (Mac)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I type shortcut combination to ping
    Then I see ping message <PING>
    When I type shortcut combination to ping
    Then I see ping message <PING_AGAIN>

    Examples: 
      | Login      | Password      | Name      | Contact   | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name | pinged | pinged again |

   @staging @id3782
   Scenario Outline: Verify you start a call in a conversation when you press ⌘ T (Mac)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I type shortcut combination to start a call
    Then I see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id3898
  Scenario Outline: Verify I can call a conversation using the menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Call"
    Then I see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id3905
  Scenario Outline: Verify I can ping a group conversation using the menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Ping"
    Then I see ping message <PING>
    And I click menu bar item "Conversation" and menu item "Ping"
    Then I see ping message <PING_AGAIN>

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName  | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | GroupChat | pinged | pinged again |


  @staging @id3908
  Scenario Outline: Verify you ping in a group conversation when you press ⌘ + K (Mac)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I type shortcut combination to ping
    Then I see ping message <PING>
    When I type shortcut combination to ping
    Then I see ping message <PING_AGAIN>

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName  | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | GroupChat | pinged | pinged again |

   @staging @id3909
   Scenario Outline: Verify you start a call in a group conversation when you press ⌘ T (Mac)
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I type shortcut combination to start a call
    Then I see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName  |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | GroupChat |

  @staging @id3907
  Scenario Outline: Verify I can call a group conversation using the menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Call"
    Then I see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName  |
         | user1Email | user1Password | user1Name | user2Name  | user3Name  | GroupChat |

  @staging @id3919
  Scenario Outline: Verify I can undo redo using menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I write random message
    And I verify that random message was typed
    And I click menu bar item "Edit" and menu item "Undo"
    And I verify that message "" was typed
    And I click menu bar item "Edit" and menu item "Redo"
    And I verify that random message was typed

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id3920
  Scenario Outline: Verify I can undo redo using when I press ⌘ Z and ⌘ ⇧ Z
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I write random message
    And I verify that random message was typed
    And I type shortcut combination to undo
    And I verify that message "" was typed
    And I type shortcut combination to redo
    And I verify that random message was typed

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |