Feature: VideoCalling

  @C36388 @videocalling @staging
  Scenario Outline: Verify I can accept Video call with the app in the foreground
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> calls me using <CallBackend>
And I swipe to accept the call
And I wait for 1 seconds
#    And I answer the call from the overlay bar
#     Then <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | CallBackend         | Timeout |
      | user1Name | user2Name | chrome:48.0.2564.97 | 60      |