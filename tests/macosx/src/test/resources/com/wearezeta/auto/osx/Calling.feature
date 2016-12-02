Feature: Calling

  @C2358 @smoke
  Scenario Outline: Verify successful 1:1 call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given <Contact1> starts instance using <WaitBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact1>
    Then Soundfile ringing_from_me did not start playing in loop
    When I call
    Then Soundfile ringing_from_me did start playing in loop
    When <Contact1> accept next incoming call automatically
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact1>
    Then Soundfile ringing_from_me did stop playing
    And Soundfile ready_to_talk did start playing
    And I wait for 10 seconds
    And <Contact1> verify to have 1 flows
    And <Contact1> verifies that all audio flows have greater than 0 bytes
    And I hang up call with conversation <Contact1>
    Then I do not see the call controls for conversation <Contact1>
    And Soundfile call_drop did start playing

    Examples: 
      | Login      | Password      | Name      | Contact1  | WaitBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 20      |