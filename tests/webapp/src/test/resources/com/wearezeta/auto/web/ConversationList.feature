Feature: Conversation List

  @smoke @id1545
  Scenario Outline: Archive and unarchive conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I archive conversation <Contact>
    Then I do not see Contact list with name <Contact>
    When I open archive
    And I unarchive conversation <Contact>
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id1918
  Scenario Outline: Mute 1on1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I set muted state for conversation <Contact>
    And I open self profile
    Then I see that conversation <Contact> is muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1919
  Scenario Outline: Unmute 1on1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I muted conversation with <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I see that conversation <Contact> is muted
    When I set unmuted state for conversation <Contact>
    And I open self profile
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @regression @id1720
  Scenario Outline: Verify Ping icon in the conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    When I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open self profile
    When User <Contact> pinged in the conversation with <Name>
    And I see ping icon in conversation with <Contact>
    Then I verify ping icon in conversation with <Contact> has <ColorName> color

    Examples: 
      | Login      | Password      | Name      | ColorName  | Contact   |
      | user1Email | user1Password | user1Name | StrongBlue | user2Name |

  @regression @id2998
  Scenario Outline: Verify you silence the conversation when you press ⌥⇧⌘L (Mac) or alt + ctrl + L (Win)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    And I see that conversation <Contact> is not muted
    When I click on options button for conversation <Contact>
    Then I see correct tooltip for silence button in options popover
    When I type shortcut combination to mute or unmute the conversation <Contact>
    Then I see that conversation <Contact> is muted
    When I type shortcut combination to mute or unmute the conversation <Contact>
    Then I see that conversation <Contact> is not muted

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id3211
  Scenario Outline: Verify I can cancel leaving a 1:1 conversation from conversation list
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact>
    When I click on options button for conversation <Contact>
    And I click the option to block in the options popover
    Then I see a block warning modal
    And I click cancel button in the block warning
    Then I see Contact list with name <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id3214
  Scenario Outline: Verify I can cancel leaving a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When I click on options button for conversation <ChatName>
    And I click the option to leave in the options popover
    Then I see a leave warning modal
    And I click cancel button in the leave warning
    Then I see Contact list with name <ChatName>

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName       |
      | user1Email | user1Password | user1Name | user2Name | user3Name | LeaveGroupChat |

  @staging @id3218
  Scenario Outline: Verify I can delete a group conversation from conversation list
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    When I click on options button for conversation <ChatName>
    And I click delete in the options popover
    Then I see a delete warning modal
    And I click delete button in the delete warning
    Then I do not see Contact list with name <ChatName>
    When I open People Picker from Contact List
    And I type <ChatName> in search field of People Picker
    Then I see group conversation <ChatName> found in People Picker
    And I close People Picker
    When User <Contact1> sent message <Msg1> to conversation <ChatName>
    Then I see Contact list with name <ChatName>
    And I open self profile
    And I click gear button on self profile page
    And I select Sign out menu item on self profile page
    And User <Contact1> is me
    And I see Sign In page
    And I Sign in using login <Login2> and password <Password2>
    And I see my avatar on top of Contact list
    When I open conversation with <ChatName>
    Then I do not see <Message> action in conversation

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName        | Msg1    | Login2     | Password2     | Message |
      | user1Email | user1Password | user1Name | user2Name | user3Name | DeleteGroupChat | message | user2Email | user2Password | LEFT    |
