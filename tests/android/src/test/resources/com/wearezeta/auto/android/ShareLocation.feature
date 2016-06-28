Feature: Share Location

  @C150028 @rc @rc42 @regression
  Scenario Outline: Verify you can share your location
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    Given I tap on contact name <Contact>
    When I tap Share location button from cursor toolbar
    # Let it to find the location
    And I wait for 5 seconds
    And I tap Send button on Share Location page
    Then I see Share Location container in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C150029 @rc @rc42 @regression
  Scenario Outline: Verify you can receive location sharing message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given User <Contact> shares his location to user Myself via device <DeviceName>
    Given I see Contact list with contacts
    When I tap on contact name <Contact>
    And I tap Share Location container in the conversation view
    Then I see the Wire app is not in foreground

    Examples:
      | Name      | Contact   | DeviceName |
      | user1Name | user2Name | device1    |
