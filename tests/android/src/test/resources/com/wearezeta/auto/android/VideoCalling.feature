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
    And I see ongoing video call
    And I hang up ongoing video call
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |


  @C36390 @calling_basic @rc
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
    And I see ongoing video call
    And I hang up ongoing video call
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36364 @calling_basic @rc
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

  @C36389 @calling_basic @staging
  Scenario Outline: Verify I can start Video call from the conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I swipe on text input
    And I press Video Call button
    Then I see outgoing call
    When <Contact> accepts next incoming video call automatically
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing video call
    When I hang up ongoing video call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36363 @staging
  Scenario Outline: (AN-3536) Verify I can start Video call from Start UI
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I open search by tap
    And I tap on Search input on People picker page
    And I enter "<Contact>" into Search input on People Picker page
    And I tap on user name found on People picker page <Contact>
    And I tap Video Call action button on People Picker page
    Then I see outgoing call
    When <Contact> accepts next incoming video call automatically
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing video call
    And I tap on ongoing video
    When I hang up ongoing video call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36367 @calling_basic @staging
  Scenario Outline: Verify I get missed call indication when someone called
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> starts a video call to me using <CallBackend>
    And I see incoming call
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    And <Contact> stops all calls to me
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see incoming call
    When I tap on contact name <Contact>
    Then I see dialog with missed call from <Contact>

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |