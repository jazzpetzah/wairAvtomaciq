Feature: People View

  @smoke @id3921
  Scenario Outline: Verify opening people popover with menu bar in 1:1 conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "People"
    Then I see Single User Profile popover
    And I see username <Contact> on Single User Profile popover

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @smoke @id3922
  Scenario Outline: Verify opening people popover with menu bar in group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "People"
    Then I see Group Participants popover
    Then I see conversation title <ChatName> on Group Participants popover

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | PeoplePopover |

  @smoke @id3923
  Scenario Outline: Verify adding people to 1:1 conversation with menu bar
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    When I open conversation with <Contact>
    And I click menu bar item "Conversation" and menu item "Add People..."
    Then I see Single User Profile popover
    And I see Add to conversation button on Single User popover

    Examples: 
      | Login      | Password      | Name      | Contact   | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @smoke @id3924
  Scenario Outline: Verify adding people to group conversation with menu bar
    Given There are 4 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <ChatName>
    And I click menu bar item "Conversation" and menu item "Add People..."
    Then I see Group Participants popover
    And I see Add People message on Group Participants popover

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | Contact3  | ChatName      |
      | user1Email | user1Password | user1Name | user2Name | user3Name | user4Name | PeoplePopover |
