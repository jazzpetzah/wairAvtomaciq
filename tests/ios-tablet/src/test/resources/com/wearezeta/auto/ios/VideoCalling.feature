Feature: Video Calling

  @C28847 @calling_basic
  Scenario Outline: Verify initiating Video call [PORTRAIT]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    Then I see call status message contains "<Contact> RINGING"
    And I see Leave button on Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C28848 @calling_basic
  Scenario Outline: Verify initiating Video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    Then I see call status message contains "<Contact> RINGING"
    And I see Leave button on Calling overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C28849 @calling_basic
  Scenario Outline: Verify cancelling Video call [PORTRAIT]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    Then I see call status message contains "<Contact> RINGING"
    When I tap Leave button on Calling overlay
    Then I see missed call from contact YOU

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C28850 @calling_basic
  Scenario Outline: Verify cancelling Video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    Then I see call status message contains "<Contact> RINGING"
    When I tap Leave button on Calling overlay
    Then I see missed call from contact YOU

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C28852 @staging
  Scenario Outline: Verify accepting video call [PORTRAIT]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When <Contact> starts a video call to <Name> using <CallBackend>
    And I see call status message contains "<Contact> CALLING"
    And I tap Accept button on Calling overlay
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    Then I see Mute button on Calling overlay
    And I see Leave button on Calling overlay
    And I see Call Video button on Calling overlay
    And I tap Leave button on Calling overlay

    Examples:
      | Name      | Contact   | CallBackend         | Timeout |
      | user1Name | user2Name | chrome:48.0.2564.97 | 60      |

  @C28852 @staging
  Scenario Outline: Verify accepting video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When <Contact> starts a video call to <Name> using <CallBackend>
    And I see call status message contains "<Contact> CALLING"
    And I tap Accept button on Calling overlay
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verify to have 1 flows
    And <Contact> verify that all flows have greater than 0 bytes
    Then I see Mute button on Calling overlay
    And I see Leave button on Calling overlay
    And I see Call Video button on Calling overlay
    And I tap Leave button on Calling overlay

    Examples:
      | Name      | Contact   | CallBackend         | Timeout |
      | user1Name | user2Name | chrome:48.0.2564.97 | 60      |

  @C28856 @staging
  Scenario Outline: Verify finishing video call [PORTRAIT]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    And I see Calling overlay
    And I see Mute button on Calling overlay
    And I see Leave button on Calling overlay
    And I see Call Video button on Calling overlay
    And I tap Leave button on Calling overlay
    Then I see dialog page

    Examples:
      | Name      | Contact   | CallBackend         | Timeout |
      | user1Name | user2Name | chrome:48.0.2564.97 | 60      |

  @28857 @staging
  Scenario Outline: Verify finishing video call [LANDSCAPE]
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts waiting instance using <CallBackend>
    Given <Contact> accepts next incoming video call automatically
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I click plus button next to text input
    And I click Video Call button
    And I see Calling overlay
    And I see Mute button on Calling overlay
    And I see Leave button on Calling overlay
    And I see Call Video button on Calling overlay
    And I tap Leave button on Calling overlay
    Then I see dialog page

    Examples:
      | Name      | Contact   | CallBackend         | Timeout |
      | user1Name | user2Name | chrome:48.0.2564.97 | 60      |