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

  @C48237 @calling_advanced
  Scenario Outline: Verify I can accept video call after another incoming call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instances using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given <Contact1> starts a video call to me
    Given I see incoming call
    Given I swipe to accept the call
    Given <Contact1> verifies that call status to me is changed to active in <Timeout> seconds
    Given I see ongoing video call
    Given I hang up ongoing video call
    Given <Contact1> verifies that call status to me is changed to destroyed in <Timeout> seconds
    When <Contact1> starts a video call to me
    And I see incoming call
    And I swipe to accept the call
    Then <Contact1> verifies that call status to me is changed to active in <Timeout> seconds
    When I see ongoing video call
    And I hang up ongoing video call
    Then <Contact1> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call
    When <Contact2> starts a video call to me
    And I see incoming call
    And I swipe to accept the call
    Then <Contact2> verifies that call status to me is changed to active in <Timeout> seconds
    When I see ongoing video call
    And I hang up ongoing video call
    Then <Contact2> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend | Timeout |
      | user1Name | user2Name | user3Name | chrome      | 60      |


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
    And I tap Video Call button from top toolbar
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

  @C36370 @calling_advanced
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
    And <Contact> stops calling me
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call
    When <Contact> starts a video call to me
    And I see incoming call
    And I swipe to accept the call
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I see ongoing video call
    And <Contact> stops calling me
    Then <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C36363 @calling_advanced
  Scenario Outline: Verify I can start Video call from Start UI
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I open Search UI
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
    When <Contact> starts a video call to me
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

  @C49973 @calling_advanced
  Scenario Outline: Verify you cannot make audio call to user A while he makes video call
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When <Contact> starts a video call to me
    And I see incoming call
    And I swipe to ignore the call
    Then <Contact> verifies that call status to me is changed to connecting in <Timeout> seconds
    When I tap Audio Call button from top toolbar
    Then I see alert message containing "<ExpectedMsg>"
    And <Contact> verifies that call status to me is changed to connecting in 3 seconds

    Examples:
      | Name      | Contact   | CallBackend | Timeout | ExpectedMsg     |
      | user1Name | user2Name | chrome      | 30      | Try again later |

  @C48236 @calling_advanced
  Scenario Outline: Verify I can start video call after another my call
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1>,<Contact2> start instances using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And I tap on contact name <Contact1>
    When I tap Video Call button from top toolbar
    And I see outgoing call
    And <Contact1> accepts next incoming video call automatically
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing video call
    Then I hang up ongoing video call
    And <Contact1> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call
    When I tap Video Call button from top toolbar
    And I see outgoing call
    And <Contact1> accepts next incoming video call automatically
    And <Contact1> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see ongoing video call
    Then I hang up ongoing video call
    And <Contact1> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call
    When I navigate back from dialog page
    And I tap on contact name <Contact2>
    When I tap Video Call button from top toolbar
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

  @C49972 @calling_advanced
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
    When I tap Video Call button from top toolbar
    Then I see alert message containing "<ExpectedMsg>"
    And <Contact> verifies that call status to me is changed to connecting in 3 seconds

    Examples:
      | Name      | Contact   | CallBackend | Timeout | ExpectedMsg     |
      | user1Name | user2Name | chrome      | 30      | Try again later |

  @C36368 @calling_basic @rc
  Scenario Outline: Verify I can disable video in Video call and enable it back
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
    And I remember state of video button for ongoing video call
    And I see video self preview
    When I tap video button for ongoing video call
    Then I see state of video button has changed for ongoing video call
    And I remember state of video button for ongoing video call
    And I do not see video self preview
    When I tap video button for ongoing video call
    Then I see state of video button has changed for ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 30      |

  @C58890 @calling_advanced
  Scenario Outline: Verify that receiving ping, message or picture have not affect to ongoing call
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
    When User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    And User <Contact> sends encrypted message <Message> to user Myself
    And User <Contact> securely pings conversation <Name>
    # Wait until content is delivered
    And I wait for 5 seconds
    Then <Contact> verifies that call status to me is changed to active in 3 seconds
    And I see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout | Message | ImageName   |
      | user1Name | user2Name | chrome      | 30      | Testing | testing.jpg |

  @C58886 @staging
  Scenario Outline: Verify I can accept Video call from background
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I minimize the application
    And <Contact> starts a video call to me
    And I see incoming call
    And I swipe to accept the call
    Then I see ongoing video call
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And I hang up ongoing video call
    And <Contact> verifies that call status to me is changed to destroyed in <Timeout> seconds
    And I do not see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 30      |

  @C58888 @staging
  Scenario Outline: Verify video call is not terminated after putting client to background and restore
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And <Contact> starts a video call to me
    And I see incoming call
    And I swipe to accept the call
    And I see ongoing video call
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    When I minimize the application
    And I wait for 5 seconds
    And I restore the application
    Then <Contact> verifies that call status to me is changed to active in 3 seconds
    And I see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 30      |

  @C36378 @staging
  Scenario Outline: Verify video call is not terminated if I lock and unlock device
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to me
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    And <Contact> starts a video call to me
    And I see incoming call
    And I swipe to accept the call
    And I see ongoing video call
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    When I lock the device
    And I wait for 5 seconds
    And I unlock the device
    Then <Contact> verifies that call status to me is changed to active in 3 seconds
    And I see ongoing video call

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 30      |

  @C36389 @staging
  Scenario Outline: Verify video call is terminated after 1 minute if nobody responds
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Video Call button from top toolbar
    Then I see ongoing video call
    And I wait for <Timeout> seconds
    And I do not see ongoing video call
    Then I see dialog with missed call from YOU

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 65      |