Feature: Video Calling

  @C164773 @regression @rc
  Scenario Outline: Verify I can start Video call from the conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact>
    When I tap Video Call button from top toolbar
    Then I see outgoing video call
    When <Contact> accepts next incoming video call automatically
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing video call
    When I hang up ongoing video call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C164775 @regression @rc
  Scenario Outline: Verify the video call is not terminated if I lock and unlock device
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    Given I tap the conversation <Contact>
    Given <Contact> accepts next incoming video call automatically
    When I tap Video Call button from top toolbar
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing video call
    And I lock the device
    And I wait for 5 seconds
    And I unlock the device
    Then I see ongoing video call
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C164776 @staging
  Scenario Outline: Verify I can accept Video call from locked device (app in background)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with conversations
    When I minimize the application
    And I lock the device
    And <Contact> starts a video call to me
    Then I see incoming video call
    When I swipe to accept the call
    Then I see ongoing video call
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |
