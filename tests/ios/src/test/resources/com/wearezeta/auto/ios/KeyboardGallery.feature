Feature: Keyboard Gallery

  @C169229 @real @torun
  Scenario Outline: Verify full screen camera is opened by tapping on arrows icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I tap Fullscreen Camera button on Keyboard Gallery overlay
    And I tap Take Photo button on Camera page
    And I tap Confirm button on Picture preview page
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |
