Feature: Audio Messaging

  @C129323 @C129321 @regression
  Scenario Outline: Verify message is started recording by long tapping on the icon
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    Then I see audio message record container

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C129327 @regression
  Scenario Outline: Verify sending voice message by check icon tap
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    And I tap Send record control button
    Then I see audio message placeholder

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C129341 @C129345 @regression
  Scenario Outline: Verify receiving a voice message and deleting it
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact1>
    When User <Contact1> sends file <FileName> having MIME type <FileMIME> to single user conversation <Name> using device <ContactDevice>
    Then I see audio message placeholder
    When I long tap on audio message placeholder in conversation view
    And I tap on Delete badge item
    Then I do not see audio message placeholder

    Examples:
      | Name      | Contact1  | FileName | FileMIME  | ContactDevice |
      | user1Name | user2Name | test.m4a | audio/mp4 | Device1       |

  @C129326 @regression
  Scenario Outline: Verify sending voice message by swipe up
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I record 5 seconds long audio message and send it using swipe up gesture
    Then I see audio message placeholder

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C131214 @regression
  Scenario Outline: Verify cancelling recorded audio message preview
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    And I tap Cancel record control button
    Then I do not see audio message placeholder
    And I see Audio Message button in input tools palette

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |