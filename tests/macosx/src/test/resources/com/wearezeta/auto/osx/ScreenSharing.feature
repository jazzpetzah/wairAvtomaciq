Feature: Screen Sharing

  @C165194 @smoke
  Scenario Outline: Verify I can switch to screen sharing during maximized video call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accept next incoming video call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    #And <Contact> verifies to have 1 flow
    #And <Contact> verifies that all flows have greater than 0 bytes
    And I click on screen share button
    And I wait for 5 seconds
    Then I verify my self video shows my screen
    Then I verify <Contact> sees my screen

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | Chrome      | 20      |