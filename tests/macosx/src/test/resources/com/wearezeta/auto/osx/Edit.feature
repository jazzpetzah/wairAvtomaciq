Feature: Edit

  @C225975 @smoke
  Scenario Outline: Verify I can edit my message via context menu in 1:1
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
    When I click context menu of the last message
    And I click edit in message context menu for my own message
    And I delete 8 characters from the conversation input
    And I write message <Message2>
    And I send message
    Then I do not see text message <Message1>
    And I see text message <Message2>
    And I see 2 messages in conversation

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 | Message2       |
      | user1Email | user1Password | user1Name | user2Name | message1 | edited message |