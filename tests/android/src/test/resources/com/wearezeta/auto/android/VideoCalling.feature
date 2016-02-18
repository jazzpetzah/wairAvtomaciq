Feature: VideoCalling

  @C36388 @calling_basic @staging
  Scenario Outline: Verify I can accept Video call with the app in the foreground
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> starts a video call to me using <CallBackend>
    And I see incoming call
    And I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing call
    And I hang up ongoing call
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |


  @C36390 @calling_basic @staging
  Scenario Outline: Verify I can decline Video call with the app in the foreground
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> starts a video call to me using <CallBackend>
    And I see incoming call
    And I swipe to ignore the call
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    And I do not see incoming call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |


  @C36362 @calling_basic @staging
  Scenario Outline: Verify I can accept Video call from locked device
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact> starts a video call to me using <CallBackend>
    Then I see incoming call
    And I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing call
    And I hang up ongoing call
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36364 @calling_basic @staging
  Scenario Outline: Verify I can decline Video call from the locked device
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact> starts a video call to me using <CallBackend>
    And I see incoming call
    And I swipe to ignore the call
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    And I do not see incoming call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |