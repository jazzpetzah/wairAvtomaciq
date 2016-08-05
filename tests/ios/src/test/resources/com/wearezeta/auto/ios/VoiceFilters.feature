Feature: Voice Filters

  @C165138 @rc @regression @fastLogin
  Scenario Outline: Verify you can record an audio message and apply voice filter to it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 5 seconds
    And I tap Stop Recording button on Voice Filters overlay
    And I tap <ButtonsCount> random effect buttons on Voice Filters overlay
    And I tap Confirm button on Voice Filters overlay
    Then I see audio message container in the conversation view
    And I do not see Confirm button on Voice Filters overlay

    Examples:
      | Name      | Contact   | ButtonsCount |
      | user1Name | user2Name | 4            |

  @C165139 @regression @fastLogin
  Scenario Outline: Verify you can cancel filtered voice message sending
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 5 seconds
    And I tap Stop Recording button on Voice Filters overlay
    And I tap <ButtonsCount> random effect buttons on Voice Filters overlay
    And I tap Cancel button on Voice Filters overlay
    Then I do not see audio message container in the conversation view
    And I do not see Confirm button on Voice Filters overlay
    And I do not see Start Recording button on Voice Filters overlay

    Examples:
      | Name      | Contact   | ButtonsCount |
      | user1Name | user2Name | 2            |

  @C165156 @regression @fastLogin
  Scenario Outline: ZIOS-6904 Verify voice filter control is not dismissed if audio recording is in progress and other UI event happens
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 5 seconds
    And I navigate back to conversations list
    And I tap on contact name <Contact2>
    And I navigate back to conversations list
    And I tap on contact name <Contact1>
    Then I do not see audio message container in the conversation view
    And I see Confirm button on Voice Filters overlay
    When I close the app for 5 seconds
    Then I see Confirm button on Voice Filters overlay
    When <Contact1> calls me
    And I tap Ignore button on Calling overlay
    And I do not see Calling overlay
    Then I see Confirm button on Voice Filters overlay

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | autocall    |

  @C165155 @regression @fastLogin
  Scenario Outline: ZIOS-6903 Voice Filters: Verify voice filter control is preserved if one has already recorded something
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given <Contact1> starts instance using <CallBackend>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 5 seconds
    And I tap Stop Recording button on Voice Filters overlay
    And I navigate back to conversations list
    And I tap on contact name <Contact2>
    And I navigate back to conversations list
    And I tap on contact name <Contact1>
    Then I do not see audio message container in the conversation view
    And I see Confirm button on Voice Filters overlay
    When I close the app for 5 seconds
    Then I see Confirm button on Voice Filters overlay
    When <Contact1> calls me
    And I tap Ignore button on Calling overlay
    And I do not see Calling overlay
    Then I see Confirm button on Voice Filters overlay

    Examples:
      | Name      | Contact1  | Contact2  | CallBackend |
      | user1Name | user2Name | user3Name | autocall    |

  @C165140 @regression @fastLogin
  Scenario Outline: Verify you can retry recording of filtered voice message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 5 seconds
    And I tap Stop Recording button on Voice Filters overlay
    And I tap <ButtonsCount> random effect buttons on Voice Filters overlay
    And I tap Redo button on Voice Filters overlay
    Then I see Start Recording button on Voice Filters overlay

    Examples:
      | Name      | Contact   | ButtonsCount |
      | user1Name | user2Name | 1            |

  @C165178 @regression @fastLogin
  Scenario Outline: Verify only one recorder works at a time
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Audio Message button from input tools
    And I tap Start Recording button on Voice Filters overlay
    And I wait for 5 seconds
    And I long tap Audio Message button for <Duration> seconds from input tools
    Then I do not see audio message container in the conversation view
    And I tap Stop Recording button on Voice Filters overlay

    Examples:
      | Name      | Contact   | Duration |
      | user1Name | user2Name | 5        |