Feature: VideoCalling

  @C36388 @calling_basic @rc
  Scenario Outline: Verify I can accept Video call with the app in the foreground
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> starts a video call to me
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
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> starts a video call to me
    And I see incoming call
    And I swipe to ignore the call
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    And I do not see incoming call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |


  @C36362 @calling_basic @rc
  Scenario Outline: Verify I can accept Video call from locked device
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact> starts a video call to me
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
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact> starts a video call to me
    And I see incoming call
    And I swipe to ignore the call
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    And I do not see incoming call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36389 @calling_basic @rc
  Scenario Outline: Verify I can start Video call from the conversation
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I swipe on text input
    And I tap Video Call button from input tools
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

  @C36369 @calling_advanced
  Scenario Outline: Verify I cannot see blocked contact trying to make a video call to me
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User Myself blocks user <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When <Contact> starts a video call to me
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    And I do not see incoming call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36370 @staging
  Scenario Outline: Verify I can make a Video call one after another
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> starts a video call to me
    And I see incoming call
    And I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing video call
    And <Contact> stops all calls to me
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call
    When <Contact> starts a video call to me
    And I see incoming call
    And I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing video call
    And <Contact> stops all calls to me
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36363 @staging
  Scenario Outline: (AN-3536) Verify I can start Video call from Start UI
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
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
    When I hang up ongoing video call
    Then <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36367 @calling_basic @regression
  Scenario Outline: Verify I get missed call indication when someone called
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> starts a video call to me
    And I see incoming call
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    And <Contact> stops calling me
    And I do not see incoming call
    When I tap on contact name <Contact>
    Then I see dialog with missed call from <Contact>

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36366 @calling_basic
  Scenario Outline: Verify I can mute Video call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When <Contact> starts a video call to me using <CallBackend>
    And I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing video call
    When I remember state of mute button for ongoing video call
    And I tap mute button for ongoing video call
    Then I see state of mute button has changed for ongoing video call
    When I remember state of mute button for ongoing video call
    And I tap mute button for ongoing video call
    Then I see state of mute button has changed for ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C49973 @staging
  Scenario Outline: Verify you cannot make audio call to user A while he makes video call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When <Contact> starts a video call to me using <CallBackend>
    And I see incoming call
    And I swipe to ignore the call
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    When I swipe on text input
    And I tap Call button from input tools
    Then I see alert message containing "<ExpectedMsg>"
    And <Contact> verifies that call status to me is changed to connecting in 3 seconds

    Examples:
      | Name      | Contact   | CallBackend | Timeout | ExpectedMsg     |
      | user1Name | user2Name | chrome      | 30      | Try again later |

  @C48236 @staging
  Scenario Outline: Verify I can start video call after another my call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instances using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact1>
    And I swipe on text input
    When I tap Video Call button from input tools
    And I see outgoing call
    And <Contact1> accepts next incoming video call automatically
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing video call
    Then I hang up ongoing video call
    And <Contact1> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call
    And I swipe on text input
    When I tap Video Call button from input tools
    And I see outgoing call
    And <Contact1> accepts next incoming video call automatically
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing video call
    Then I hang up ongoing video call
    And <Contact1> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call
    When I navigate back from dialog page
    And I tap on contact name <Contact2>
    And I swipe on text input
    And I tap Video Call button from input tools
    And I see outgoing call
    And <Contact2> accepts next incoming video call automatically
    And <Contact2> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing video call
    Then I hang up ongoing video call
    And <Contact2> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend | Timeout |
      | user1Name | user2Name | user3Name | chrome      | 30      |

  @C49972 @staging
  Scenario Outline: Verify you cannot make video call to user A while he makes audio call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When <Contact> calls me
    And I see incoming call
    And I swipe to ignore the call
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    When I swipe on text input
    And I tap Video Call button from input tools
    Then I see alert message containing "<ExpectedMsg>"
    And <Contact> verifies that call status to me is changed to connecting in 3 seconds

    Examples:
      | Name      | Contact   | CallBackend | Timeout | ExpectedMsg     |
      | user1Name | user2Name | chrome      | 30      | Try again later |