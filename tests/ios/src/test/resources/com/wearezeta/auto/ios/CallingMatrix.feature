Feature: Calling Matrix

  @C343160 @calling_matrix @fastLogin @real
  Scenario Outline: Verify I can make two 1:1 call to <CallBackend> in a row
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I accept alert
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see Calling overlay
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay
    When <Contact> accepts next incoming call automatically
    And I tap Audio Call button
    # Given it some time to connect
    And I wait for 5 seconds
    Then I see Calling overlay
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 20      |

  @C343161 @calling_matrix @fastLogin @real
  Scenario Outline: Verify I can receive two 1:1 call from <CallBackend> in a row
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email
    Given I see conversations list
    Given I tap on contact name <Contact>
    When <Contact> calls me
    And I see Audio Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And I accept alert
    And I see call status message contains "<Contact>"
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay
    When <Contact> calls me
    And I see Audio Call Kit overlay
    And I tap Accept button on Call Kit overlay
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 20      |

  @C343162 @calling_matrix @fastLogin @real
  Scenario Outline: Verify I can make group call with callbackend <WaitBackend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> sets the unique username
    Given User <Contact2> sets the unique username
    Given <Contact1>,<Contact2> starts instance using <WaitBackend>
    Given <Contact1>,<Contact2> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And I tap Audio Call button
    And I accept alert
    Then <Contact1>,<Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And I see Calling overlay
    And <Contact1>,<Contact2> verifies to have 2 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    And I do not see Calling overlay
    And I wait for 5 seconds
    Then <Contact1>,<Contact2> verifies to have 1 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | WaitBackend | Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | chrome      | 20      |

  @C434163 @calling_matrix @fastLogin @real
  Scenario Outline: Verify I can join group call with callbackend <Backend>
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given User <Contact1> sets the unique username
    Given User <Contact2> sets the unique username
    Given <Contact1>,<Contact2> starts instance using <Backend>
    Given <Contact2> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    When I tap on group chat with name <GroupChatName>
    And <Contact1> calls <GroupChatName>
    # Wait until the call is connected
    And I wait for 5 seconds
    And I see Audio Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And I accept alert
    And <Contact2> verify that waiting instance status is changed to active in <Timeout> seconds
    And <Contact1> verify that call status to <GroupChatName> is changed to active in <Timeout> seconds
    And <Contact1>,<Contact2> verifies to have 2 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    And I do not see Calling overlay
    And I wait for 5 seconds
    Then <Contact1>,<Contact2> verifies to have 1 flow
    And <Contact1>,<Contact2> verifies that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact1  | Contact2  | GroupChatName | Backend | Timeout |
      | user1Name | user2Name | user3Name | GroupCall     | chrome  | 20      |

  @C343164 @calling_matrix @fastLogin @real
  Scenario Outline: Put app into background after initiating call with user <WaitBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <WaitBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Call button
    And I accept alert
    Then I close the app for 5 seconds
    And I see Calling overlay
    And <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | WaitBackend | Timeout |
      | user1Name | user2Name | chrome      | 20      |

  @C343165 @calling_matrix @fastLogin @real
  Scenario Outline: Verify putting client to the background during 1-to-1 call <CallBackend> to me
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When <Contact> calls me
    # Wait until the call is connected
    And I wait for 5 seconds
    And I see Audio Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And I accept alert
    Then I see call status message contains "<Contact>"
    When I close the app for 5 seconds
    Then I see call status message contains "<Contact>"
    And <Contact> verifies that call status to me is changed to active in <Timeout> seconds

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 20      |

  @C343166 @calling_matrix @fastLogin @real
  Scenario Outline: Verify I can accept two 1:1 video call from callbackend <CallBackend> in a row
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When <Contact> starts a video call to <Name>
    Then I see Video Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And I accept alert
    And I accept alert
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flows
    And <Contact> verifies that all flows have greater than 0 bytes
    When <Contact> stops outgoing call to me
    And <Contact> starts a video call to <Name>
    Then I see Video Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And <Contact> verifies that call status to <Name> is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flows
    And <Contact> verifies that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C343167 @calling_matrix @fastLogin @real
  Scenario Outline: Verify I can make two 1:1 video call with <CallBackend> in a row
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Video Call button
    And I accept alert
    And I accept alert
    Then I see Video Calling overlay
    And <Contact> verifies to have 1 flows
    And <Contact> verifies that all flows have greater than 0 bytes
    And <Contact> stops incoming call from me
    And <Contact> verifies that waiting instance status is changed to destroyed in <Timeout> seconds
    When <Contact> accepts next incoming call automatically
    And I tap Video Call button
    Then I see Video Calling overlay
    And <Contact> verifies to have 1 flows
    And <Contact> verifies that all flows have greater than 0 bytes

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C343169 @calling_matrix @fastLogin @real
  Scenario Outline: Accept 1:1 call when app is in background
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I press Home button
    When <Contact> calls me
    And I see Audio Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And I accept alert
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    And <Contact> verifies to have 1 flow
    And <Contact> verifies that all flows have greater than 0 bytes
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | chrome      | 60      |

  @C343178 @calling_matrix @fastLogin @real
  Scenario Outline: Verify I can receive 1:1 call from AVS <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email
    Given I see conversations list
    Given I tap on contact name <Contact>
    When <Contact> calls me
    And I see Audio Call Kit overlay
    And I tap Accept button on Call Kit overlay
    And I accept alert
    Then <Contact> verifies that call status to me is changed to active in <Timeout> seconds
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay
    And <Contact> verifies that call to conversation <Name> was successful

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | zcall       | 60      |

  @C343179 @calling_matrix @fastLogin @real
  Scenario Outline: Verify I can make 1:1 call to AVS <CallBackend>
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given <Contact> accepts next incoming call automatically
    Given I sign in using my email
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Audio Call button
    And I accept alert
    Then <Contact> verifies that waiting instance status is changed to active in <Timeout> seconds
    And I see Calling overlay
    When I tap Leave button on Calling overlay
    Then I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | Timeout |
      | user1Name | user2Name | zcall       | 20      |

  @C343180 @calling_matrix @fastLogin @real
  Scenario Outline: AUDIO-1107 Verify application behaviour in case of incoming/outgoing call if there are no permissions to microphone/camera
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> sets the unique username
    Given <Contact> starts instance using <CallBackend>
    Given I sign in using my email
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Audio Call button
    And I dismiss alert
    Then I see alert contains text <AlertText>
    And I dismiss alert
    And I do not see Calling overlay
    When I tap Video Call button
    And I dismiss alert
    Then I see alert contains text <AlertText>
    And I dismiss alert
    And I do not see Calling overlay
    When <Contact> calls me
    And I tap Accept button on Call Kit overlay
    Then I see alert contains text <AlertText>
    And I dismiss alert
    And I do not see Calling overlay
    When <Contact> stops outgoing call to Me
    And <Contact> starts a video call to <Name>
    And I tap Accept button on Call Kit overlay
    Then I see alert contains text <AlertText>
    And I dismiss alert
    And I do not see Calling overlay

    Examples:
      | Name      | Contact   | CallBackend | AlertText         |
      | user1Name | user2Name | chrome      | Wire needs access |
    