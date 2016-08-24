Feature: Edit

  # This is a Template
  @C0001 @staging
  Scenario Outline: Verify I can edit my message (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message1>
    And I send message
    And I see text message <Message1>
    And I see 2 messages in conversation
    And I click context menu of the latest message

    Examples:
      | Login      | Password      | Name      | Contact   | Message1 |
      | user1Email | user1Password | user1Name | user2Name | message1 |
