Feature: Edit

  @C206267 @regression
  Scenario Outline: Verify I can edit my message in 1:1
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message1>
    Then I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    When I click context menu of the latest message
    And I click to edit message in context menu
    And I delete 8 characters from the conversation input
    And I write message <Message2>
    And I send message
    Then I do not see text message <Message1>
    And I see text message <Message2>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 | Message2       |
      | user1Email | user1Password | user1Name | user2Name | message1 | edited message |

  @C206268 @regression
  Scenario Outline: Verify I can edit my message in group conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName>
    And I write message <Message1>
    Then I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    When I click context menu of the latest message
    And I click to edit message in context menu
    And I delete 8 characters from the conversation input
    And I write message <Message2>
    And I send message
    Then I do not see text message <Message1>
    And I see text message <Message2>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  |Contact2   | ChatName  | Message1 | Message2       |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | message1 | edited message |

  @C206280 @regression
  Scenario Outline: Verify I cannot edit another users message
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>, <Contact2>
    Given <Name> has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given Contact <Contact1> sends message <Message1> via device Device1 to group conversation <ChatName>
    And I see text message <Message1>
    And I see 2 messages in conversation
    When I click context menu of the latest message
    And I do not see edit button in context menu for latest message
    When I click context menu of the latest message
    And I see text message <Message1>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact1  |Contact2   | ChatName  | Message1 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | message1 |
