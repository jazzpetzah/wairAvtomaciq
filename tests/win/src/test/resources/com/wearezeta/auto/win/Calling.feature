Feature: Calling

  @smoke @id3918
  Scenario Outline: Verify successful 1:1 call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> starts waiting instance using <WaitBackend>
    Given <Contact1> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact1>
    When I call
    And I see the calling bar
    And <Contact1> verify that waiting instance status is changed to active in <Timeout> seconds
    And I wait for 10 seconds
    And <Contact1> verify to have 1 flows
    And <Contact1> verify that all flows have greater than 0 bytes
    When I end the call
    Then I do not see the calling bar

    Examples: 
      | Login      | Password      | Name      | Contact1  | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 60      |
