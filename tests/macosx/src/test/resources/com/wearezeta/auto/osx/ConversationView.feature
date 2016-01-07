Feature: Conversation View

  @C2344 @smoke
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

  @C2333 @smoke
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

  @C2334 @smoke
  Scenario Outline: Verify you start a call in a conversation when you press ⌘ R (Mac)
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

  @C2345 @smoke
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

  @C2346 @smoke
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
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | pinged | pinged again |

  @C2348 @smoke
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
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  | PING   | PING_AGAIN   |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | pinged | pinged again |

  @C2349 @smoke
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
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2347 @smoke
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
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @C2359 @smoke
  Scenario Outline: Verify I can undo redo using menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I click menu bar item "Edit" and menu item "Undo"
    Then I verify that message "" was typed
    When I click menu bar item "Edit" and menu item "Redo"
    Then I verify that random message was typed

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2360 @smoke
  Scenario Outline: Verify I can undo redo using shortcuts ⌘ Z and ⌘ ⇧ Z
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I type shortcut combination to undo
    Then I verify that message "" was typed
    When I type shortcut combination to redo
    Then I verify that random message was typed

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2367 @smoke
  Scenario Outline: Verify I can select all, cut and paste using menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I click menu bar item "Edit" and menu item "Select All"
    And I click menu bar item "Edit" and menu item "Cut"
    Then I verify that message "" was typed

    # We can not paste something in automation due to security
    #     When I click menu bar item "Edit" and menu item "Paste"
    #     Then I verify that random message was typed
    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2368 @smoke
  Scenario Outline: Verify I can select all, cut and paste using shortcuts ⌘ A, ⌘ X and ⌘ V
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I type shortcut combination to select all
    And I type shortcut combination to cut
    Then I verify that message "" was typed

    # We can not paste something in automation due to security
    #     When I type shortcut combination to paste
    #     Then I verify that random message was typed
    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2369 @smoke
  Scenario Outline: Verify I can select all, copy and paste using menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I click menu bar item "Edit" and menu item "Select All"
    And I click menu bar item "Edit" and menu item "Copy"
    Then I verify that random message was typed

    # We can not paste something in automation due to security
    #     When I click menu bar item "Edit" and menu item "Paste"
    #     Then I verify that random message was typed
    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @C2370 @smoke
  Scenario Outline: Verify I can select all, copy and paste using shortcuts ⌘ A, ⌘ C and ⌘ V
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I write random message
    Then I verify that random message was typed
    When I type shortcut combination to select all
    And I type shortcut combination to copy
    Then I verify that random message was typed

    # We can not paste something in automation due to security
    #     When I type shortcut combination to paste
    #     Then I verify that random message was typed
    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |