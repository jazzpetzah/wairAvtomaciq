Feature: Conversation List

  @smoke @id3423
  Scenario Outline: Verify I can block user from conversation list with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I open context menu of conversation <Contact>
    And I click block in context menu
    Then I see a block warning modal

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3437
  Scenario Outline: Mute and unmute 1:1 conversation with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I open context menu of conversation <Contact>
    And I click silence in context menu
    Then I see that conversation <Contact> is muted
    When I open context menu of conversation <Contact>
    And I click notify in context menu
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3438
  Scenario Outline: Mute and unmute 1:1 conversation with menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Silence"
    Then I see that conversation <Contact> is muted
    When I click menu bar item "Conversation" and menu item "Notify"
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3877
  Scenario Outline: Mute and unmute 1:1 conversation with keyboard shortcut
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I type shortcut combination to mute or unmute a conversation
    Then I see that conversation <Contact> is muted
    When I type shortcut combination to mute or unmute a conversation
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3435
  Scenario Outline: Mute and unmute group conversation with right click
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I open context menu of conversation <ChatName>
    And I click silence in context menu
    Then I see that conversation <ChatName> is muted
    When I open context menu of conversation <ChatName>
    And I click notify in context menu
    Then I see that conversation <ChatName> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @smoke @id3436
  Scenario Outline: Mute and unmute group conversation with menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Silence"
    Then I see that conversation <ChatName> is muted
    When I click menu bar item "Conversation" and menu item "Notify"
    Then I see that conversation <ChatName> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @smoke @id3878
  Scenario Outline: Mute and unmute group conversation with keyboard shortcut
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I type shortcut combination to mute or unmute a conversation
    Then I see that conversation <ChatName> is muted
    When I type shortcut combination to mute or unmute a conversation
    Then I see that conversation <ChatName> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @smoke @id3427
  Scenario Outline: Delete a 1:1 conversation with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I open context menu of conversation <Contact>
    And I click delete in context menu
    Then I see a delete warning modal for 1:1 conversations

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3910
  Scenario Outline: Delete a 1:1 conversation with menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Delete"
    Then I see a delete warning modal for 1:1 conversations

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3429
  Scenario Outline: Delete a group conversation with right click
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I open context menu of conversation <ChatName>
    And I click delete in context menu
    Then I see a delete warning modal for group conversations

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @smoke @id3911
  Scenario Outline: Delete a group conversation with menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Delete"
    Then I see a delete warning modal for group conversations

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @smoke @id3336
  Scenario Outline: Archive a 1:1 conversation with right click
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I open context menu of conversation <Contact>
    And I click archive in context menu
    Then I do not see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3913
  Scenario Outline: Archive a 1:1 conversation with menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Archive"
    Then I do not see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3914
  Scenario Outline: Archive a 1:1 conversation with keyboard shortcut
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I type shortcut combination to archive a conversation
    Then I do not see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3912
  Scenario Outline: Archive a group conversation with right click
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I open context menu of conversation <ChatName>
    And I click archive in context menu
    Then I do not see Contact list with name <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @smoke @id3915
  Scenario Outline: Archive a group conversation with menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Archive"
    Then I do not see Contact list with name <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |

  @smoke @id3916
  Scenario Outline: Archive a group conversation with keyboard shortcut
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    And I type shortcut combination to archive a conversation
    Then I do not see Contact list with name <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |
  @smoke @id3917
  Scenario Outline: Verify I can start a conversation with menu bar
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I click menu bar item "Conversation" and menu item "Start"
    Then I see people picker

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

   @smoke @id3780
   Scenario Outline: Verify Start (Search) is opened when you press ⌘ N (Mac)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Then I see my avatar on top of Contact list
    When I type shortcut combination to open search
    Then I see people picker

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
