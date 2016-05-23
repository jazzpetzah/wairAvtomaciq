Feature: Audio Messaging

  @C129323 @C129321 @staging @torun
  Scenario Outline: Verify message is started recording by long tapping on the icon
    Given There are 2 user where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I long tap Audio Message button from input tools
    Then I see audio message record progress

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
