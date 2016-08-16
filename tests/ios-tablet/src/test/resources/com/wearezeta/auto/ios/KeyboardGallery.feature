Feature: Keyboard Gallery

  @C194554 @regression @fastLogin
  Scenario Outline: Verify opening gallery tapping on gallery icon [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I tap Camera Roll button on Keyboard Gallery overlay
    And I select the first picture from Camera Roll
    And I tap Confirm button on Picture preview page
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C194555 @regression @fastLogin
  Scenario Outline: I can draw a sketch on picture from gallery [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I select the first picture from Keyboard Gallery
    And I tap Sketch button on Picture Preview page
    And I draw a random sketch
    And I send my sketch
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |