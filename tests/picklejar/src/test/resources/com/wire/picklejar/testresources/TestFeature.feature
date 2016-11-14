Feature: Calling_Matrix

  @C5359 @calling_matrix
  Scenario Outline: Verify I can make 1:1 audio call to <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I call
    Then <Contact> accepts next incoming call automatically
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And I hang up call with conversation <Contact>
    Then I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:53.0.2785.116 | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:52.0.2743.82  | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:49.0         | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:48.0.2       | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:45.0.1       | 20      |

  @C5360 @calling_matrix
  Scenario Outline: Verify I can make 1:1 video call to <CallBackend>
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    And I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
#    And I see the ongoing call controls for conversation <Contact>
    And I wait for 5 seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    And I end the video call
#    Then I do not see the call controls for conversation <Contact>

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend          | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome:53.0.2785.116 | 20      |
      | user1Email | user1Password | user1Name | user2Name | chrome:52.0.2743.82  | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:49.0         | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:48.0.2       | 20      |
      | user1Email | user1Password | user1Name | user2Name | firefox:45.0.1       | 20      |