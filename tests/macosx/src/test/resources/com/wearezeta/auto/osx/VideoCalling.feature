Feature: VideoCalling

  @videocalling @staging
  Scenario Outline: Verify I can put macbook into sleep after video call
    Given My browser supports calling
    Given There are 2 users where <Name> is me
    Given <Contact> has unique username
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given <Contact> verifies that waiting instance status is changed to waiting in <Timeout> seconds
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    And I open conversation with <Contact>
    When I start a video call
    Then I see my self video view
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies to get audio data from me
    And <Contact> verifies to get video data from me
    And <Contact> verifies that all audio flows have greater than 0 bytes
    And <Contact> verifies that all video flows have greater than 0 bytes
    When I end the video call
    Then I do not see the call controls for conversation <Contact>
    And I do not see my self video view
    And Mac would enter hibernate mode when closing the lid

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | chrome      | 30      |
