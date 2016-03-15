Feature: Calling

  @C2358 @smoke @id3918
  Scenario Outline: Verify successful 1:1 call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> starts instance using <WaitBackend>
    Given <Contact1> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I see my avatar on top of Contact list
    And I open conversation with <Contact1>
    When I call
    Then <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact1>
    And I wait for 10 seconds
    And <Contact1> verify to have 1 flows
    And <Contact1> verify that all flows have greater than 0 bytes
    And I hang up call with conversation <Contact1>
    Then I do not see the call controls for conversation <Contact1>

    Examples: 
      | Login      | Password      | Name      | Contact1  | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 20      |