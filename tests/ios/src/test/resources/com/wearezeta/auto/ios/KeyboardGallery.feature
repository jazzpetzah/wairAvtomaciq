Feature: Keyboard Gallery

  @C169229 @real
  Scenario Outline: Verify full screen camera is opened by tapping on arrows icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I tap Fullscreen Camera button on Keyboard Gallery overlay
    And I tap Take Photo button on Camera page
    And I tap Use Photo button on Picture preview page
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C173057 @regression
  Scenario Outline: Verify opening gallery tapping on gallery icon
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I tap Camera Roll button on Keyboard Gallery overlay
    And I select the first picture from Camera Roll
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C183864 @staging
  Scenario Outline: Verify switching camera keyboard with all others
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given I tap on contact name <Contact>
    When I tap Add Picture button from input tools
    Then I see Camera Roll button on Keyboard Gallery overlay
    When I tap File Transfer button from input tools
    Then I see file transfer menu item <ItemName>
    And I do not see Camera Roll button on Keyboard Gallery overlay
    And I do not see the on-screen keyboard

    Examples:
      | Name      | Contact   | ItemName                   |
      | user1Name | user2Name | FTRANSFER_MENU_DEFAULT_PNG |
