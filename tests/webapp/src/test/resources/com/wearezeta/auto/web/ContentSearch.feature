Feature: ContentSearch

  @C424326 @contentsearch @staging
  Scenario Outline: Verify I can search my own message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I switch to Sign In page
    Given I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    And I write message <Message>
    And I send message
    And I click collection button in conversation
    And I enter search query <Message> in collection
    And I see 1 search result
    And I see search result with text <Message>
    And I click search result with text <Message>
    Then I see text message <Message>

    Examples:
      | Email      | Password      | Name      | Contact   | Message |
      | user1Email | user1Password | user1Name | user2Name | FindMe  |