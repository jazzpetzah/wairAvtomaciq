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
    And I click on screen share button
    And I wait for 5 seconds
    Then I verify my self video shows my screen
    Then I verify <Contact> sees my screen

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | Chrome      | 20      |

  @C165195 @smoke
  Scenario Outline: Verify I can switch to screen sharing during minimized video call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accept next incoming video call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    When I minimize video call
    Then I see video call is minimized
    When I click on screen share button
    And I wait for 5 seconds
    Then I verify <Contact> sees my screen

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | Chrome      | 20      |


  @C167025 @smoke
  Scenario Outline: Verify I can toggle between screen sharing and video
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accept next incoming video call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I start a video call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    When I click on video button
    And I see my self video is off
    When I click on video button
    Then I see my self video is on
    When I click on screen share button
    And I wait for 5 seconds
    Then I verify <Contact> sees my screen
    When I click on screen share button
    And I see my self video is off

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | Chrome      | 20      |

  @C183898 @smoke @WEBAPP-3094
  Scenario Outline: Verify I can share my screen in a 1:1 audio call
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <Contact>
    When I call
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I click on screen share button
    And I maximize video call via titlebar
    And <Contact> maximises video call
    And I wait for 5 seconds
    Then I verify my self video shows my screen
    Then I verify <Contact> sees my screen

    Examples:
      | Login      | Password      | Name      | Contact   | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name | Chrome      | 20      |

  @C183897 @smoke @WEBAPP-3094
  Scenario Outline: Verify I can share my screen in a group audio call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName1> with <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> starts instance using <CallBackend>
    Given <Contact1>,<Contact2> accept next incoming call automatically
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    When I open conversation with <ChatName1>
    And I call
    Then <Contact1>,<Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I click on screen share button
    And I maximize video call via titlebar
    And <Contact1>,<Contact2> maximises video call
    And I wait for 5 seconds
    Then I verify my self video shows my screen
    Then I verify <Contact1>,<Contact2> sees my screen

    Examples:
      | Login      | Password      | Name      | Contact1   | Contact2   | ChatName1 | CallBackend | Timeout |
      | user1Email | user1Password | user1Name | user2Name  | user3Name  | GC1       | Chrome      | 20      |